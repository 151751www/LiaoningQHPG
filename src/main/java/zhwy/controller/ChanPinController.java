package zhwy.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.xwpf.usermodel.BodyElementType;
import org.apache.poi.xwpf.usermodel.IBodyElement;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;
import zhwy.PoiForFile.BaseSection;
import zhwy.PoiForFile.PoiUtils;
import zhwy.service.ChanPinService;
import zhwy.util.Common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

@Api(position = 9,tags = "辽宁气候评估----产品服务")
@RestController
@SessionAttributes
@RequestMapping("/ChanpinFuWu")
public class ChanPinController {

    private static Logger logger = LoggerFactory.getLogger(DataAvgAndMController.class);

    @Autowired
    private Common common;
    @Autowired
    private ChanPinService chanPinService;
    @Autowired
    private PoiUtils poiUtils;


    @ApiOperation(value = "气象要素累年值查询")
    @PostMapping(value = "/getSumYrarDate")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "stationType", value = "台站类型（国家站，区域站）", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "beginTime", value = "累年值开始时间 (年：yyyy）", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "endTime", value = "累年值结束时间（年：yyyy）", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "stationNum", value = "台站号(54236,54668)", required = true, paramType = "query", dataType = "String")
    })
    public String getSumYrarDate(String stationType,String beginTime,String endTime,String stationNum)  {
        //跨域
        common.getCrossOrigin();
        JSONObject jsObject=new JSONObject();
        try {
            JSONArray array=chanPinService.getSumYrarDate(stationType,beginTime,endTime,stationNum);
            jsObject.put("查询成功",array.toJSONString());
        }catch (Exception e){
            logger.error("气象要素累年值查询"+e.getMessage());
            e.printStackTrace();
            jsObject.put("查询失败","气象要素累年值查询"+e.getMessage());
        }
        return  jsObject.toJSONString();
    }
    @ApiOperation(value = "气象要素极值查询")
    @PostMapping(value = "/getJiZhiDate")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "stationType", value = "台站类型（国家站，区域站）", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "beginTime", value = "极值开始时间 (年：yyyy）", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "endTime", value = "极值结束时间（年：yyyy）", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "stationNum", value = "台站号(54236,54668)", required = true, paramType = "query", dataType = "String")
    })
    public String getJiZhiDate(String stationType,String beginTime,String endTime,String stationNum)  {
        //跨域
        common.getCrossOrigin();
        JSONObject jsObject=new JSONObject();
        try {
            JSONArray array=chanPinService.getJiZhiDate(stationType,beginTime,endTime,stationNum);
            jsObject.put("查询成功",array.toJSONString());
        }catch (Exception e){
            logger.error("气象要素极值查询失败"+e.getMessage());
            e.printStackTrace();
            jsObject.put("查询失败","气象要素极值查询"+e.getMessage());
        }
        return  jsObject.toJSONString();
    }

    @ApiOperation(value = "产品服务气温表查询")
    @PostMapping(value = "/getDateCharForTem")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "stationType", value = "台站类型（国家站，区域站）", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "beginTime", value = "累年值开始时间 (年：yyyy）", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "endTime", value = "累年值结束时间（年：yyyy）", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "stationNum", value = "台站号(54236,54668)", required = true, paramType = "query", dataType = "String")
    })
    public String getDateCharForTem(String stationType,String beginTime,String endTime,String stationNum)  {
        //跨域
        common.getCrossOrigin();
        JSONObject jsObject=new JSONObject();
        try {
            JSONArray array=chanPinService.getAvgMathDate(stationType,beginTime,endTime,stationNum,"气温");
            jsObject.put("查询成功",array.toJSONString());
        }catch (Exception e){
            logger.error("气象要素气温查询失败"+e.getMessage());
            e.printStackTrace();
            jsObject.put("查询失败","气象要素查询失败"+e.getMessage());
        }
        return  jsObject.toJSONString();
    }
    @ApiOperation(value = "产品服务气压表查询")
    @PostMapping(value = "/getDateCharForPrs")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "stationType", value = "台站类型（国家站，区域站）", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "beginTime", value = "累年值开始时间 (年：yyyy）", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "endTime", value = "累年值结束时间（年：yyyy）", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "stationNum", value = "台站号(54236,54668)", required = true, paramType = "query", dataType = "String")
    })
    public String getDateCharForPrs(String stationType,String beginTime,String endTime,String stationNum)  {
        //跨域
        common.getCrossOrigin();
        JSONObject jsObject=new JSONObject();
        try {
            JSONArray array=chanPinService.getAvgMathDate(stationType,beginTime,endTime,stationNum,"气压");
            jsObject.put("查询成功",array.toJSONString());
        }catch (Exception e){
            logger.error("气象要素气压查询失败"+e.getMessage());
            e.printStackTrace();
            jsObject.put("查询失败","气象要素查询失败"+e.getMessage());
        }
        return  jsObject.toJSONString();
    }
    @ApiOperation(value = "产品服务降水量查询")
    @PostMapping(value = "/getDateCharForPre")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "stationType", value = "台站类型（国家站，区域站）", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "beginTime", value = "累年值开始时间 (年：yyyy）", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "endTime", value = "累年值结束时间（年：yyyy）", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "stationNum", value = "台站号(54236,54668)", required = true, paramType = "query", dataType = "String")
    })
    public String getDateCharForPre(String stationType,String beginTime,String endTime,String stationNum)  {
        //跨域
        common.getCrossOrigin();
        JSONObject jsObject=new JSONObject();
        try {
            JSONArray array=chanPinService.getAvgMathDate(stationType,beginTime,endTime,stationNum,"降水量");
            jsObject.put("查询成功",array.toJSONString());
        }catch (Exception e){
            logger.error("气象要素降水量查询失败"+e.getMessage());
            e.printStackTrace();
            jsObject.put("查询失败","气象要素查询失败"+e.getMessage());
        }
        return  jsObject.toJSONString();
    }
    @ApiOperation(value = "产品服务风速表查询")
    @PostMapping(value = "/getDateCharForFS")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "stationType", value = "台站类型（国家站，区域站）", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "beginTime", value = "累年值开始时间 (年：yyyy）", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "endTime", value = "累年值结束时间（年：yyyy）", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "stationNum", value = "台站号(54236,54668)", required = true, paramType = "query", dataType = "String")
    })
    public String getDateCharForFS(String stationType,String beginTime,String endTime,String stationNum)  {
        //跨域
        common.getCrossOrigin();
        JSONObject jsObject=new JSONObject();
        try {
            JSONArray array=chanPinService.getAvgMathDate(stationType,beginTime,endTime,stationNum,"风速");
            jsObject.put("查询成功",array.toJSONString());
        }catch (Exception e){
            logger.error("气象要素风速查询失败"+e.getMessage());
            e.printStackTrace();
            jsObject.put("查询失败","气象要素查询失败"+e.getMessage());
        }
        return  jsObject.toJSONString();
    }

    @ApiOperation(value = "产品服务制作")
    @PostMapping(value = "/FileMake")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "stationType", value = "台站类型（国家站，区域站）", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "beginTime", value = "累年值开始时间 (年：yyyy）", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "endTime", value = "累年值结束时间（年：yyyy）", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "beginTime2", value = "极值开始时间 (年：yyyy）", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "endTime2", value = "极值结束时间（年：yyyy）", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "stationNum", value = "台站号", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "stationName", value = "台站名", required = true, paramType = "query", dataType = "String")
    })
    public String FileMake(String stationType,String beginTime,String endTime,String beginTime2,String endTime2,String stationNum,String stationName){
        common.getCrossOrigin();
        Pattern pattern = Pattern.compile("\\$\\{(.+?)\\}", Pattern.CASE_INSENSITIVE);
        XWPFDocument document = null;
        InputStream inputStream=null;

        try {
            String fileName="F://项目总览//辽宁//生态保护重大工程气候效应评估系统//12_成果截图和ppt//产品模板.docx";
            File file = ResourceUtils.getFile(fileName);
            inputStream = new FileInputStream(file);
            document = new XWPFDocument(inputStream);


            Iterator<XWPFParagraph> itPara = document.getParagraphsIterator();
            String classPath = "zhwy.PoiForFile.Section";

            //处理文字
            while (itPara.hasNext()) {
                XWPFParagraph paragraph = itPara.next();
                String paraText = paragraph.getText();
                //如果没有匹配到指定格式的关键词占位符（如${title}格式的）则不进行后续处理
                if (!pattern.matcher(paraText).find()) {
                    continue;
                }
                //提取出文档模板占位符中的章节标题
                String keyInParaText = paraText.split("\\$\\{")[1].split("\\}")[0];
                if(keyInParaText.startsWith("section1")){
                    classPath = "zhwy.PoiForFile.Section1";
                }else if(keyInParaText.startsWith("section2")){
                    classPath = "zhwy.PoiForFile.SectionTem";
                }
                //如果占位符是大标题
                if ("title".equalsIgnoreCase(keyInParaText)) {
                    chanPinService.insertTitle(paragraph);
                    continue;
                }

                //如果占位符代表章节标题
                if (keyInParaText.contains("section") && keyInParaText.contains("Title")) {
                    //获取章节类名
                    String name = keyInParaText.substring(0, 8);
                    //获取章节类的路径
                    //通过类路径获取类对象
                    BaseSection base = (BaseSection) Class.forName(classPath).newInstance();
                    base.replaceSectionTitle(document, paragraph);
                    continue;
                }
                //如果占位符代表章节文本描述
                if (keyInParaText.contains("body")) {
                    BaseSection base = (BaseSection) Class.forName(classPath).newInstance();
                    base.replaceBody(paragraph,keyInParaText,stationType,beginTime,endTime,beginTime2,endTime2,stationNum,stationName);
                    continue;
                }
                //如果占位符代表表名
                if (keyInParaText.contains("TableName")) {
                    if(keyInParaText.equals("TableName2")){
                        chanPinService.replaceTableName(paragraph,stationName,beginTime2+"~"+endTime2,keyInParaText);
                    }else if(keyInParaText.equals("TableName1")){
                        chanPinService.replaceTableName(paragraph,stationName,beginTime+"~"+endTime,keyInParaText);
                    }
                    continue;
                }
                //如果占位符代表表
                if (keyInParaText.startsWith("table")) {
                    BaseSection base = (BaseSection) Class.forName(classPath).newInstance();
                    JSONArray array=chanPinService.getSectionData(keyInParaText,stationType,beginTime,endTime,beginTime2,endTime2,stationNum,stationName);
                    String [] tableTitle=null;
                    if(keyInParaText.equals("table2")){
                        tableTitle=new String[]{"气象要素","数值","出现时间"};
                    }else if(keyInParaText.equals("table1")){
                        tableTitle=new String[]{"气象要素","累年平均值"};
                    }
                    chanPinService.insertTable(document, paragraph, array,tableTitle);
                    continue;
                }
                //如果占位符代表统计图
                if (keyInParaText.endsWith("chart")) {
                    paragraph.removeRun(0);
                    BaseSection base = (BaseSection) Class.forName(classPath).newInstance();
                    JSONArray array=chanPinService.getSectionData(keyInParaText,stationType,beginTime,endTime,beginTime2,endTime2,stationNum,stationName);
                    base.replaceChart(document,array);
                    continue;
                }
                //如果占位符代表图名
                if (keyInParaText.contains("chartName")) {
                    BaseSection base = (BaseSection) Class.forName(classPath).newInstance();
                    base.replaceChartName(paragraph,keyInParaText,stationType,beginTime,endTime,beginTime2,endTime2,stationNum,stationName);
                    continue;
                }
            }

            //再遍历一次文档,把没有替换的占位符段落删除
            List<IBodyElement> elements = document.getBodyElements();
            int indexTable = 0;
            for (int k = 0; k < elements.size(); k++) {
                IBodyElement bodyElement = elements.get(k);
                //所有段落，如果有${}格式的段落便删除该段落
                if (bodyElement.getElementType().equals(BodyElementType.PARAGRAPH)) {
                    XWPFParagraph p = (XWPFParagraph) bodyElement;
                    String paraText = p.getText();
                    boolean flag = false;
                    if (pattern.matcher(paraText).find()) {
                        flag = document.removeBodyElement(k);
                        if (flag) {
                            k--;
                        }
                    }
                }
                //如果是表格，那么给表格的前一个段落(即表名加上编号，如表1)
                if (bodyElement.getElementType().equals(BodyElementType.TABLE)) {
                    indexTable++;
                    XWPFParagraph tableTitleParagraph = (XWPFParagraph) elements.get(k - 1);
                    StringBuilder tableTitleText = new StringBuilder(tableTitleParagraph.getParagraphText());
                    tableTitleText.insert(0, "表" + indexTable + " ");
                    poiUtils.setTableOrChartTitle(tableTitleParagraph, tableTitleText.toString());
                }
            }

            //给章节与小节添加序号
            poiUtils.init(document);

            //导出word文档
            FileOutputStream docxFos = new FileOutputStream("D://test1.docx");
            document.write(docxFos);
            docxFos.flush();
            docxFos.close();
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  "成功";
    }

}
