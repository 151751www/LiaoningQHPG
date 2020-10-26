package zhwy.PoiForFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.poi.ooxml.POIXMLDocumentPart;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xddf.usermodel.chart.XDDFChartData;
import org.apache.poi.xddf.usermodel.chart.XDDFDataSource;
import org.apache.poi.xddf.usermodel.chart.XDDFDataSourcesFactory;
import org.apache.poi.xddf.usermodel.chart.XDDFNumericalDataSource;
import org.apache.poi.xwpf.usermodel.XWPFChart;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTChart;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTTitle;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTTx;
import org.openxmlformats.schemas.drawingml.x2006.main.CTTextBody;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public abstract class BaseSection {

    PoiUtils poiUtils=new PoiUtils();

    //替换章节标题
    public abstract void replaceSectionTitle(XWPFDocument document, XWPFParagraph paragraph);

    //替换章节内容
    public abstract void replaceBody(JSONArray leinianzhi,JSONArray avgMonth,XWPFParagraph paragraph,String keyInParaText,String stationType,String beginTime,String endTime,String beginTime2,String endTime2,String stationNum,String stationName)throws  Exception;



    //替换统计图数据
    public  void replaceChart(String keyInParaText,XWPFDocument document,JSONArray array ,String ytitle,String [] xarr,String obsv) throws Exception{
        JSONObject object;
        JSONArray jsonArray;
        List<Number[]> values = new ArrayList<>();
        Number[] value1;
        Object obj;
        //x轴
        String[] categories;
        if("section4chart4,section4chart5,section4chart6,section4chart7,section4chart8".indexOf(keyInParaText)!=-1){
            object=array.getJSONObject(0);
            categories=new String[object.size()];
            value1=new Number[object.size()];
            int i=0;
            Iterator iter = object.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                categories[i]=entry.getKey().toString();
                value1[i]=Double.parseDouble(entry.getValue().toString());
                i++;
            }
            values.add(value1);
        }else{
            categories=new String[12];
            for(int i=0;i<12;i++){
                categories[i]=(i+1)+"";
            }
            for (int i=0;i<array.size();i++){
                object=array.getJSONObject(i);
                Iterator iter = object.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry entry = (Map.Entry) iter.next();
                    jsonArray=JSONArray.parseArray(JSON.toJSONString(entry.getValue()));
                    value1=new Number[jsonArray.size()];
                    for (int j=0;j<jsonArray.size();j++){
                        obj=jsonArray.get(j);
                        if(obj!=null){
                            value1[j]=Double.parseDouble(obj.toString());
                        }else{
                            value1[j]=null;
                        }
                    }
                    values.add(value1);
                }
            }
        }

        XWPFChart xChart;
        for (POIXMLDocumentPart part : document.getCharts()) {
            if (part instanceof XWPFChart) {
                String key=part.toString().replaceAll("Name: /word/charts/","").replace("- Content Type: application/vnd.openxmlformats-officedocument.drawingml.chart+xml","").replace(".xml","").trim();
                if(keyInParaText.indexOf(key)>0){
                    xChart = (XWPFChart) part;
                    generateChart(xChart, xarr,categories,values,ytitle);
                    break;
                }
            }
        }

    }

    /**
     * 获取模板中表格的标题
     * @param chart
     * @return
     */
    public String getTitle(CTChart chart) {
        CTTitle title=null;
        try {
            title = chart.getTitle();
            if (title != null) {
                CTTx tx = title.getTx();
                CTTextBody tb = tx.getRich();
                return tb.getPArray(0).getRArray(0).getT();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return "";
    }

    /**
     *
     * @param chart 模板中统计图对象
     * @param series 系列
     * @param categories x轴
     * @param values 具体的值
     */
    public void generateChart(XWPFChart chart, String[] series, String[] categories, List<Number[]> values,String ytitle)throws  Exception {
        String chartTitle="";
        List<XDDFChartData> data = chart.getChartSeries();
        XDDFChartData bar = data.get(0);

        int numOfPoints = categories.length;

        String categoryDataRange = chart.formatRange(new CellRangeAddress(1, numOfPoints, 0, 0));

        XDDFDataSource<?> categoriesData = XDDFDataSourcesFactory.fromArray(categories, categoryDataRange, 0);
        for (int i = 0; i < values.size(); i++) {
            final String valuesDataRange = chart.formatRange(new CellRangeAddress(1, numOfPoints, i + 1, i + 1));
            Number[] value = values.get(i);
            final XDDFNumericalDataSource<? extends Number> valuesData = XDDFDataSourcesFactory.fromArray(value, valuesDataRange, i + 1);
            XDDFChartData.Series ser;
            if (i < 3) {
                ser = bar.getSeries().get(i);
                ser.replaceData(categoriesData, valuesData);
            } else {
                ser = bar.addSeries(categoriesData, valuesData);
            }
            CellReference cellReference = chart.setSheetTitle(series[i], 1);
            ser.setTitle(series[i], cellReference);
        }
        chart.plot(bar);
        chart.setTitleText(chartTitle);
        chart.setTitleOverlay(false);
    }

    // 替换图名称
    public abstract void replaceChartName(XWPFParagraph paragraph,String keyInParaText,String stationType,String beginTime,String endTime,String beginTime2,String endTime2,String stationNum,String stationName);



}
