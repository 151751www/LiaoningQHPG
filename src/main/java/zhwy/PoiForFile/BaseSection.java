package zhwy.PoiForFile;

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
import java.util.List;
import java.util.Map;

public abstract class BaseSection {

    PoiUtils poiUtils=new PoiUtils();

    //替换章节标题
    public abstract void replaceSectionTitle(XWPFDocument document, XWPFParagraph paragraph);

    //替换章节内容
    public abstract void replaceBody(XWPFParagraph paragraph,String keyInParaText,String stationType,String beginTime,String endTime,String beginTime2,String endTime2,String stationNum,String stationName)throws  Exception;



    //替换统计图数据
    public  void replaceChart(XWPFDocument document,JSONArray array){
        JSONObject object;
        String[] series=null;
        //x轴
        String[] categories=new String[array.size()];
        List<Number[]> values = new ArrayList<>();
        Number[] value1;

        for (int i=0;i<array.size();i++){
            object=array.getJSONObject(i);
            categories[i]=object.getString("math");
            if(i==0){
                series=new String[object.size()-1];
                int obval=0;
                for (Map.Entry<String,Object>map:object.entrySet()){
                    if(obval!=0){
                        series[obval-1]=map.getKey();
                    }
                    obval++;
                }
            }
        }
        for (int i=0;i<series.length;i++){
            value1=new Number[array.size()];
            for (int j=0;j<array.size();j++){
                object=array.getJSONObject(j);
                value1[j]=Double.parseDouble(object.getString(series[i]));
            }
            values.add(value1);
        }
        try {
            XWPFChart xChart = null;
            for (POIXMLDocumentPart part : document.getCharts()) {
                if (part instanceof XWPFChart) {
                    xChart = (XWPFChart) part;
                    generateChart(xChart, series,categories,values);

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
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
    public void generateChart(XWPFChart chart, String[] series, String[] categories, List<Number[]> values) {
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
