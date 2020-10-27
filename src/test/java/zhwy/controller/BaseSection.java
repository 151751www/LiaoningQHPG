package zhwy.controller;

import org.apache.poi.ooxml.POIXMLDocumentPart;
import org.apache.poi.xwpf.usermodel.*;
import org.apache.xmlbeans.XmlCursor;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTChart;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTTitle;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTTx;
import org.openxmlformats.schemas.drawingml.x2006.main.CTTextBody;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBorder;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblBorders;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcBorders;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STBorder;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseSection {
    PoiUtils poiUtils = new PoiUtils();

    //替换章节标题
    public abstract void replaceSectionTitle(XWPFDocument document, XWPFParagraph paragraph);

    //替换章节内容
    public abstract void replaceBody(XWPFParagraph paragraph);

    //替换章节表名
    public  void replaceTableName(XWPFParagraph paragraph){
        String tableName="水果销售数据";
        poiUtils.setTableOrChartTitle(paragraph,tableName);
    }

    //生成表
    public void insertTable(XWPFDocument document, XWPFParagraph paragraph) {
        /*1.将段落原有文本(原有所有的Run)全部删除*/
        poiUtils.deleteRun(paragraph);
        /*生成表格插入提示游标*/
        XmlCursor cursor = paragraph.getCTP().newCursor();
        // 在指定游标位置插入表格
        XWPFTable table = document.insertNewTbl(cursor);
        //设置表格居中
        table.setTableAlignment(TableRowAlign.CENTER);
        //模拟表格数据
        List<String[]> list = new ArrayList<>();
        list.add(new String[]{"苹果", "100", "6", "600", "10.7"});
        list.add(new String[]{"香蕉", "200", "5", "1000", "17.9"});
        list.add(new String[]{"桃子", "300", "4", "1200", "21.4"});
        list.add(new String[]{"葡萄", "400", "3", "1200", "21.4"});
        list.add(new String[]{"西瓜", "500", "2", "1000", "17.9"});
        list.add(new String[]{"车厘子", "600", "1", "600", "10.7"});
        //根据数据生成表格
        inserInfo(table, list);
        //设置表格中所有单元格水平居中对齐
        poiUtils.setTableCenter(table);
    }

    //替换统计图数据
    public  void replaceChart(XWPFDocument document,String key){
        //模拟统计图数据
        //系列
        String[] series={"销售量(kg)","销售额(元)","净盈利额(元)"};
        //x轴
        String[] categories={"星期一","星期二","星期三","星期四","星期五","星期六","星期日"};
        List<Number[]> values = new ArrayList<>();
        //一周的销售量
        Number[] value1 = {60,null,74,52,66,88,90};
        //一周的销售额
        Number[] value2 = {450.2,652.1,null,384.6,486.5,688.9,711.1};
        //一周的净盈利额
        Number[] value3 = {200.2,326.4,266,159.5,222.2,355.5,369.5};

        values.add(value1);
        values.add(value2);
        values.add(value3);

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
    public abstract void generateChart(XWPFChart chart,String[]series,String[]categories,List<Number[]> values);

    // 替换图名称
    public abstract void replaceChartName(XWPFParagraph paragraph);

    /**
     * 把信息插入表格,因为每个章节的表格生成都一样，避免麻烦，所以统一用这个数据
     * @param table
     * @param list
     */
    private void inserInfo(XWPFTable table, List<String[]> list) {

        //设置表格宽度
        table.setWidth(10000);
        XWPFTableRow row = table.getRow(0);
        CTTblBorders borders=table.getCTTbl().getTblPr().addNewTblBorders();
        CTBorder hBorder=borders.addNewInsideH();//内部横线
        hBorder.setVal(STBorder.Enum.forString("none"));
        hBorder.setSz(new BigInteger("1"));

        CTBorder vBorder=borders.addNewInsideV();//内部竖线
        vBorder.setVal(STBorder.Enum.forString("none"));
        vBorder.setSz(new BigInteger("1"));

        CTBorder lBorder=borders.addNewLeft();
        lBorder.setVal(STBorder.Enum.forString("none"));
        lBorder.setSz(new BigInteger("1"));

        CTBorder rBorder=borders.addNewRight();
        rBorder.setVal(STBorder.Enum.forString("none"));
        rBorder.setSz(new BigInteger("1"));

        CTBorder tBorder=borders.addNewTop();
        tBorder.setVal(STBorder.Enum.forString("thick"));
        tBorder.setSz(new BigInteger("10"));

        CTBorder bBorder=borders.addNewBottom();
        bBorder.setVal(STBorder.Enum.forString("thick"));
        bBorder.setSz(new BigInteger("10"));


        row.getCell(0).setText("指标");
        row.addNewTableCell().setText("销售数量");
        row.addNewTableCell().setText("单价");
        row.addNewTableCell().setText("销售总额");
        row.addNewTableCell().setText("销售额占比");


        CTTcBorders tblBorders1 = row.getCell(0).getCTTc().getTcPr().addNewTcBorders();
        tblBorders1.addNewLeft().setVal(STBorder.NONE);
        tblBorders1.addNewRight().setVal(STBorder.NONE);
        tblBorders1.addNewBottom().setVal(STBorder.NIL);
        CTTcBorders tblBorders2 = row.getCell(0).getCTTc().getTcPr().addNewTcBorders();
        tblBorders2.addNewLeft().setVal(STBorder.NONE);
        tblBorders2.addNewRight().setVal(STBorder.NONE);
        tblBorders2.addNewBottom().setVal(STBorder.NIL);
        CTTcBorders tblBorders23= row.getCell(0).getCTTc().getTcPr().addNewTcBorders();
        tblBorders23.addNewLeft().setVal(STBorder.NONE);
        tblBorders23.addNewRight().setVal(STBorder.NONE);
        tblBorders23.addNewBottom().setVal(STBorder.NIL);
        CTTcBorders tblBorders4= row.getCell(0).getCTTc().getTcPr().addNewTcBorders();
        tblBorders4.addNewLeft().setVal(STBorder.NONE);
        tblBorders4.addNewRight().setVal(STBorder.NONE);
        tblBorders4.addNewBottom().setVal(STBorder.NIL);
        CTTcBorders tblBorders5= row.getCell(0).getCTTc().getTcPr().addNewTcBorders();
        tblBorders5.addNewLeft().setVal(STBorder.NONE);
        tblBorders5.addNewRight().setVal(STBorder.NONE);
        tblBorders5.addNewBottom().setVal(STBorder.NIL);



        for (int i = 0; i < list.size(); i++) {
            row = table.createRow();
            String[] obj = list.get(i);
            for (int j = 0; j < obj.length; j++) {
                row.getCell(j).setText(obj[j]);
            }
        }
    }
}
