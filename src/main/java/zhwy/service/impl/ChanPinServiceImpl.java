package zhwy.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.poi.xwpf.usermodel.*;
import org.apache.xmlbeans.XmlCursor;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zhwy.PoiForFile.PoiUtils;
import zhwy.dao.ChanPinDao;
import zhwy.service.ChanPinService;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Resource
public class ChanPinServiceImpl implements ChanPinService {
    @Autowired
    ChanPinDao chanPinDao;
    @Autowired
    PoiUtils poiUtils;

    public JSONArray getSumYrarDate(String stationType, String beginTime, String endTime, String stationNum) throws Exception {
        return chanPinDao.getSumYrarDate(stationType, beginTime, endTime, stationNum);
    }

    public JSONArray getJiZhiDate(String stationType, String beginTime, String endTime, String stationNum) throws Exception {
        return chanPinDao.getJiZhiDate(stationType, beginTime, endTime, stationNum);
    }



    public JSONArray getAvgMathDate(String stationType, String beginTime, String endTime, String stationNum, String obsv) throws Exception {
        JSONArray resultArray = new JSONArray();
        JSONArray objectarr;
        JSONObject jsonObject;
        String tiaojian = "";
        String[] key = null;
        String tableName = "surf_aws_month_data";
        if (obsv.equals("气温")) {
            tiaojian = ",cast(AVG (surf.tem_avg) as decimal(5, 1)) AS val,cast(AVG (surf.tem_max) as decimal(5, 1)) AS vmax,cast(AVG (surf.tem_min) as decimal(5, 1)) AS vmin ";
            key = new String[]{"math", "val", "vmax", "vmin"};
        } else if (obsv.equals("风向")) {
            String[] fangwei = {"NNE", "NE", "ENE", "E", "ESE", "SE", "SSE", "S", "SSW", "SW", "WSW", "W", "WNW", "NW", "NNW", "N"};
            key = new String[fangwei.length + 1];
            key[0] = "math";
            for (int i = 0; i < fangwei.length; i++) {
                tiaojian += ",cast(AVG(surf.win_" + fangwei[i] + "_freq) as decimal(5, 1)) as " + fangwei[i];
                key[i + 1] = fangwei[i];
            }
        } else if (!obsv.equals("")) {
            key = new String[]{"math", "val"};
            Map<String, String> map = new HashMap<String, String>();
            map.put("降水量", "pre_month");
            map.put("风速", "win_s_avg_month");
            map.put("气压", "prs_avg");
            map.put("日照时数", "ssh");
            map.put("相对湿度", "rhu_avg");
            if (map.containsKey(obsv)) {
                tiaojian = ",cast(AVG (surf." + map.get(obsv) + ") as decimal(5, 1)) AS val ";
            }
        }
        List<Map<String, Object>> list = chanPinDao.getAvgMathDate(beginTime, endTime, stationNum, tiaojian, key, tableName);
        if (key != null) {
            for (int i = 0; i < key.length; i++) {
                if (!key[i].equals("math")) {
                    jsonObject = new JSONObject(true);
                    objectarr = new JSONArray();
                    Object object;
                    for (int j = 0; j < 12; j++) {
                        object = null;
                        for (int k = 0; k < list.size(); k++) {
                            int month = Integer.parseInt(String.valueOf(list.get(k).get("math")));
                            if (month == j + 1) {
                                object = list.get(k).get(key[i]);
                                break;
                            }
                        }
                        objectarr.add(j, object);
                    }
                    jsonObject.put(key[i], objectarr);
                    resultArray.add(jsonObject);
                }
            }
        }
        return resultArray;
    }


    public JSONArray getWin_d_AvgMathDate( String beginTime, String endTime, String stationNum) throws Exception {
        String tiaojian = "";
        String tableName = "surf_aws_month_data";
        String[] fangwei = {"NNE", "NE", "ENE", "E", "ESE", "SE", "SSE", "S", "SSW", "SW", "WSW", "W", "WNW", "NW", "NNW", "N"};
        for (int i = 0; i < fangwei.length; i++) {
            tiaojian += ",cast(AVG(surf.win_" + fangwei[i] + "_freq) as decimal(5, 1)) as " + fangwei[i];
        }
        List<Map<String, Object>> list = chanPinDao.getAvgMathDate(beginTime, endTime, stationNum, tiaojian, null, tableName);
        return JSONArray.parseArray(JSON.toJSONString(list));
    }



    public List<Map<String, Object>> getAvgYearDate(String stationType, String beginTime, String endTime, String stationNum, String obsv) throws Exception {
        JSONArray resultArray = new JSONArray();
        String tiaojian = "";
        String tableName = "surf_aws_year_data";
        if (obsv.equals("气温")) {
            tiaojian = ",cast(AVG (surf.tem_avg) as decimal(5, 1)) AS val,cast(AVG (surf.tem_max) as decimal(5, 1)) AS vmax,cast(AVG (surf.tem_min) as decimal(5, 1)) AS vmin ";
        } else if (obsv.equals("风向")) {
            String[] fangwei = {"NNE", "NE", "ENE", "E", "ESE", "SE", "SSE", "S", "SSW", "SW", "WSW", "W", "WNW", "NW", "NNW", "N"};
            for (int i = 0; i < fangwei.length; i++) {
                tiaojian += ",cast(AVG(surf.win_" + fangwei[i] + "_freq) as decimal(5, 1)) as " + fangwei[i];
            }
        } else if(obsv.equals("日照时数")){
            tiaojian = ",cast(AVG (surf.ssh) as decimal(5, 1)) AS val ";
        }
        if (tiaojian.startsWith(",")) {
            tiaojian = tiaojian.substring(1);
        }
        List<Map<String, Object>> list = chanPinDao.getAvgYearDate(beginTime, endTime, stationNum, tiaojian, tableName);

        return list;
    }

    //插入大标题
    public void insertTitle(XWPFParagraph paragraph) {
        String title = "1. 气候概况";
        List<XWPFRun> runs = paragraph.getRuns();
        int runSize = runs.size();
        /**Paragrap中每删除一个run,其所有的run对象就会动态变化，即不能同时遍历和删除*/
        int haveRemoved = 0;
        for (int runIndex = 0; runIndex < runSize; runIndex++) {
            paragraph.removeRun(runIndex - haveRemoved);
            haveRemoved++;
        }
        /**3.插入新的Run即将新的文本插入段落*/
        XWPFRun createRun = paragraph.insertNewRun(0);
        createRun.setText(title);
        XWPFRun separtor = paragraph.insertNewRun(1);
        /**两段之间添加换行*/
        separtor.setText("\r");
        //设置字体大小
        createRun.setFontSize(16);
        //是否加粗
        createRun.setBold(true);
        //设置字体
        createRun.setFontFamily("黑体");
        //设置居中
        paragraph.setAlignment(ParagraphAlignment.LEFT);
    }

    //替换章节表名
    public void replaceTableName(XWPFParagraph paragraph, String staNum, String Year, String keyInParaText) throws Exception {
        String tableName = "";
        if (keyInParaText.equals("TableName1")) {
            tableName = staNum + "气象站相关气象要素的累年值（" + Year + "年）";
        } else if (keyInParaText.equals("TableName2")) {
            tableName = staNum + "气象站相关气象要素极值及出现年份（" + Year + "年）";
        }else if (keyInParaText.equals("TableName3")) {
            tableName = staNum + "气象站累年平均年、月风向频率表（单位：%）";
        }
        poiUtils.setTableOrChartTitle(paragraph, tableName);
    }

    //生成表
    public void insertTable(XWPFDocument document, XWPFParagraph paragraph, JSONArray array, String[] tableTitle) throws Exception {
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
        JSONObject object;
        String[] tablearray;
        for (int i = 0; i < array.size(); i++) {
            object = array.getJSONObject(i);
            tablearray = new String[tableTitle.length];
            for (int j = 0; j < tableTitle.length; j++) {
                if(tableTitle[j].equals("风向")){
                    tablearray[j] = object.getString("math");
                }else{
                    tablearray[j] = object.getString(tableTitle[j]);
                }
            }
            list.add(tablearray);
        }
        //根据数据生成表格
        inserInfo(table, list, tableTitle);
        //设置表格中所有单元格水平居中对齐
        poiUtils.setTableCenter(table);
    }

    @Override
    public String FileMake(String stationType, String beginTime, String endTime, String stationNum) throws Exception {
        return null;
    }

    /**
     * @param table
     * @param list
     */
    public void inserInfo(XWPFTable table, List<String[]> list, String[] tableTitle) {

        //设置表格宽度
        //table.setWidth(10000);

        XWPFTableRow row = table.getRow(0);
        CTTblBorders borders = table.getCTTbl().getTblPr().addNewTblBorders();
        CTBorder hBorder = borders.addNewInsideH();//内部横线
        hBorder.setVal(STBorder.Enum.forString("none"));
        hBorder.setSz(new BigInteger("1"));

        CTBorder vBorder = borders.addNewInsideV();//内部竖线
        vBorder.setVal(STBorder.Enum.forString("none"));
        vBorder.setSz(new BigInteger("1"));

        CTBorder lBorder = borders.addNewLeft();
        lBorder.setVal(STBorder.Enum.forString("none"));
        lBorder.setSz(new BigInteger("1"));

        CTBorder rBorder = borders.addNewRight();
        rBorder.setVal(STBorder.Enum.forString("none"));
        rBorder.setSz(new BigInteger("1"));

        CTBorder tBorder = borders.addNewTop();
        tBorder.setVal(STBorder.Enum.forString("thick"));
        tBorder.setSz(new BigInteger("20"));

        CTBorder bBorder = borders.addNewBottom();
        bBorder.setVal(STBorder.Enum.forString("thick"));
        bBorder.setSz(new BigInteger("20"));
        CTTcPr tcpr;

        row.getCell(0).setText(tableTitle[0]);
        tcpr = row.getCell(0).getCTTc().addNewTcPr();
        CTTcBorders ctBorderc0 = tcpr.addNewTcBorders();
        CTBorder bBc0 = ctBorderc0.addNewBottom();
        bBc0.setVal(STBorder.Enum.forString("thick"));
        bBc0.setSz(new BigInteger("8"));
        XWPFTableCell cell ;
        if(tableTitle.length>10){
            for (int i = 1; i < 10; i++) {
                cell = row.addNewTableCell();
                tcpr = cell.getCTTc().addNewTcPr();
                cell.setText(tableTitle[i]);
                CTTcBorders ctBorder = tcpr.addNewTcBorders();
                CTBorder bB = ctBorder.addNewBottom();
                bB.setVal(STBorder.Enum.forString("thick"));
                bB.setSz(new BigInteger("8"));
                table.setWidth(8000);
            }
            for (int i = 0; i < list.size(); i++) {
                row = table.createRow();
                String[] obj = list.get(i);
                for (int j = 0; j < 10; j++) {
                    row.getCell(j).setText(obj[j]);
                }
            }
            XWPFTableRow row2 = table.createRow();
            XWPFTableCell cel2;
            for (int i = 10; i < 20; i++) {
                cel2 = row2.getCell(i-10);
                tcpr = cel2.getCTTc().addNewTcPr();
                if(i==10){
                    cel2.setText(tableTitle[0]);
                }else if (i<tableTitle.length+1){
                    cel2.setText(tableTitle[i-1]);
                }

                CTTcBorders ctBorder = tcpr.addNewTcBorders();
                CTBorder bB = ctBorder.addNewBottom();
                bB.setVal(STBorder.Enum.forString("thick"));
                bB.setSz(new BigInteger("8"));
                CTBorder bf = ctBorder.addNewTop();
                bf.setVal(STBorder.Enum.forString("thick"));
                bf.setSz(new BigInteger("20"));
                table.setWidth(8000);
            }
            for (int i = 0; i < list.size(); i++) {
                row = table.createRow();
                String[] obj = list.get(i);
                for (int j = 10; j < 20; j++) {
                    if(j==10){
                        row.getCell(j-10).setText(obj[0]);
                    }else if(j<obj.length+1){
                        row.getCell(j-10).setText(obj[j-1]);
                    }
                }
            }
        }else{
            for (int i = 1; i < tableTitle.length; i++) {
                 cell = row.addNewTableCell();
                tcpr = cell.getCTTc().addNewTcPr();
                cell.setText(tableTitle[i]);
                CTTcBorders ctBorder = tcpr.addNewTcBorders();
                CTBorder bB = ctBorder.addNewBottom();
                bB.setVal(STBorder.Enum.forString("thick"));
                bB.setSz(new BigInteger("8"));
                if (tableTitle.length == 2) {
                    CTTblWidth cellw = tcpr.addNewTcW();
                    cellw.setType(STTblWidth.DXA);
                    cellw.setW(BigInteger.valueOf(650 * 5));
                } else {
                    table.setWidth(8000);
                }
            }
            for (int i = 0; i < list.size(); i++) {
                row = table.createRow();
                String[] obj = list.get(i);
                for (int j = 0; j < obj.length; j++) {
                    row.getCell(j).setText(obj[j]);
                }
            }
        }

    }

    public JSONArray getSectionData(String keyInParaText, String stationType, String beginTime, String endTime, String beginTime2, String endTime2, String stationNum, String stationName) throws Exception {
        JSONArray array = null;
        if (keyInParaText.equals("table2")) {
            array = getJiZhiDate(stationType, beginTime2, endTime2, stationNum);
        } else if (keyInParaText.equals("table1")) {
            array = getSumYrarDate(stationType, beginTime, endTime, stationNum);
        } else if (keyInParaText.equals("table3")) {
            array = getWin_d_AvgMathDate( beginTime, endTime, stationNum);
        } else if (keyInParaText.endsWith("chart1")) {
            array = getAvgMathDate(stationType, beginTime, endTime, stationNum, "气温");
        } else if (keyInParaText.endsWith("chart2")) {
            array = getAvgMathDate(stationType, beginTime, endTime, stationNum, "降水量");
        } else if (keyInParaText.endsWith("chart3")) {
            array = getAvgMathDate(stationType, beginTime, endTime, stationNum, "风速");
        } else if (keyInParaText.endsWith("chart4")) {
            List<Map<String, Object>> list = getAvgYearDate(stationType, beginTime, endTime, stationNum, "风向");
            array = JSONArray.parseArray(JSON.toJSONString(list));
        } else if (keyInParaText.endsWith("chart5")) {
            array = getAvgMathDate(stationType, beginTime, endTime, stationNum, "风向");
            array=getWin_d_jijie(array,1);//冬为第一个季节  1表示春
        }else if (keyInParaText.endsWith("chart6")) {
            array = getAvgMathDate(stationType, beginTime, endTime, stationNum, "风向");
            array=getWin_d_jijie(array,2);//冬为第一个季节  2表示夏
        }else if (keyInParaText.endsWith("chart7")) {
            array = getAvgMathDate(stationType, beginTime, endTime, stationNum, "风向");
            array=getWin_d_jijie(array,3);//冬为第一个季节  3表示秋
        }else if (keyInParaText.endsWith("chart8")) {
            array = getAvgMathDate(stationType, beginTime, endTime, stationNum, "风向");
            array=getWin_d_jijie(array,0);//冬为第一个季节  0表示冬
        }else if (keyInParaText.endsWith("chart9")) {
            array = getAvgMathDate(stationType, beginTime, endTime, stationNum, "日照时数");
        }else if (keyInParaText.endsWith("chart10")) {
            array = getAvgMathDate(stationType, beginTime, endTime, stationNum, "相对湿度");
            JSONArray array2=getAvgMathDate(stationType, beginTime2, endTime2, stationNum, "相对湿度");
            JSONObject object=array2.getJSONObject(0);
            array.add(1,object);
        } else if (keyInParaText.endsWith("chart11")) {
            array = getAvgMathDate(stationType, beginTime, endTime, stationNum, "气压");
        }
        return array;
    }

    public JSONArray getWin_d_jijie(JSONArray avgMonth, int num) {

        JSONObject jsonObject ;
        JSONObject jijieObject = new JSONObject();
        JSONArray jsonArray=new JSONArray();
        JSONObject jsonob=new JSONObject(true);
        int[] montg = {1, 4, 7, 10};

        for (int i = 0; i < avgMonth.size(); i++) {
            jsonObject = avgMonth.getJSONObject(i);
            for (Map.Entry<String, Object> map : jsonObject.entrySet()) {
                String fangwei = map.getKey().toString();
                double avg = 0.0;
                JSONArray jijiearr = new JSONArray();
                JSONArray fangweidate = JSONArray.parseArray(map.getValue().toString());
                if (fangweidate.size() == 12) {
                    double sum = 0.0;
                    int length = 0;
                    for (int l = 0; l < fangweidate.size() - 1; l++) {
                        if (l == 0 && fangweidate.get(11) != null) {
                            sum = Double.parseDouble(fangweidate.get(11).toString());
                            length = 1;
                        }
                        if (fangweidate.get(l) != null) {
                            sum += Double.parseDouble(fangweidate.get(l).toString());
                            length++;
                        }
                        for (int m = 0; m < montg.length; m++) {
                            if (l == montg[m]) {
                                avg = sum / length;
                                avg = new BigDecimal(avg).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
                                ;
                                jijiearr.add(m, avg);
                                sum = 0.0;
                                length = 0;
                            }
                        }
                    }
                }
                jijieObject.put(fangwei, jijiearr);
            }
        }
        for (Map.Entry<String, Object> map : jijieObject.entrySet()) {
            JSONArray fangweidate = JSONArray.parseArray(map.getValue().toString());
            jsonob.put(map.getKey(),Double.parseDouble(fangweidate.get(num).toString()));
        }
        jsonArray.add(0,jsonob);
        return  jsonArray;
    }

}
