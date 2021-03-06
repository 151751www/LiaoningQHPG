package zhwy.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;
import zhwy.service.DataAvgAndMService;
import zhwy.service.DataMethodService;
import zhwy.util.Common;
import zhwy.util.StrUtil;

import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Api(position = 9,tags = "辽宁气候评估----数据原始值、平均值和极值")
@RestController
@SessionAttributes
@RequestMapping("/AvgAndM")
public class DataAvgAndMController {
    private static Logger logger = LoggerFactory.getLogger(DataAvgAndMController.class);

    @Autowired
    private Common common;
    @Autowired
    private DataAvgAndMService dataAvgAndMService;
    @Autowired
    private DataMethodService dataMethodService;




    @ApiOperation(value = "数据平均值、极值和原始值")
    @PostMapping("/getDataForAvg")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "stationType", value = "台站类型(国家站、区域站)", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "dataType", value = "数据类型（时，日，月，年）", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "ObsveName", value = "要素称(tem_avg，tem_max，...)", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "StartTime", value = "开始名称 (时：yyyy-MM-dd HH，日：yyyy-MM-dd，月：yyyy-MM，年：yyyy）", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "Obsveval", value = "要素名时间（平均气温，最高气温，...)", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "EndTime", value = "结束时间（时：yyyy-MM-dd HH，日：yyyy-MM-dd，月：yyyy-MM，年：yyyy）", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "city", value = "市名(沈阳市，铁岭市...)", required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "cnty", value = "县名", required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "stationNum", value = "台站号(54236,54668)", required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "suanfaType", value = "算法类型（平均值，原值，极大值，极小值）", required = true, paramType = "query", dataType = "String")

    })
    public String getDataForAvg(String stationType,String dataType,String ObsveName,String Obsveval, String StartTime,String EndTime, String city,String stationNum,String suanfaType,String cnty) throws ParseException {
        String result="";
        common.getCrossOrigin();
        if(stationType==null ||stationType.equals("")){
            result="台站类型不能为空";
        }else if(dataType==null ||dataType.equals("")){
            result="数据类型不能为空";
        }else if(ObsveName==null ||ObsveName.equals("")||Obsveval==null ||Obsveval.equals("")){
            result="要素名称不能为空";
        }else if(StartTime==null||StartTime.equals("")){
            result="开始时间不能为空";
        }else if(EndTime==null||EndTime.equals("")){
            result="结束时间不能为空";
        }else if("平均值,原始值,极大值,极小值".indexOf(suanfaType)==-1){
            result="算法类型错误";
        }

        SimpleDateFormat sim=null;
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if(dataType.equals("时")){
            sim=new SimpleDateFormat("yyyy-MM-dd HH");
        }else if(dataType.equals("日")){
            sim=new SimpleDateFormat("yyyy-MM-dd");
        }else if(dataType.equals("月")){
            sim=new SimpleDateFormat("yyyy-MM");
        }else if(dataType.equals("年")){
            sim=new SimpleDateFormat("yyyy");
        }
        try {
           Date startDate= sim.parse(StartTime);
            Date endDate =sim.parse(EndTime);
            StartTime=simpleDateFormat.format(startDate);
            EndTime=simpleDateFormat.format(endDate);
        }catch (Exception e){
            result="请重新选择开始结束时间";
            if(dataType.equals("时")){
                result+=",格式为 yyyy-MM-dd HH";
            }else if(dataType.equals("日")){
                result+=",格式为 yyyy-MM-dd";
            }else if(dataType.equals("月")){
                result+=",格式为 yyyy-MM";
            }else if(dataType.equals("年")){
                result+=",格式为 yyyy";
            }
        }
        if(result.equals("")){
            result=dataAvgAndMService.getDataForAvg(stationType,dataType,Obsveval,ObsveName,StartTime,EndTime,city,stationNum,suanfaType,cnty);
        }else{
            result= StrUtil.packageResult(result);
        }
        return result;
    }



    @ApiOperation(value = "趋势分析")
    @PostMapping("/getStationQUShi")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "stationType", value = "台站类型(国家站、区域站)", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "dataType", value = "数据类型（时，日，月，年）", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "obsveName", value = "要素名称(tem_avg，tem_max，...)", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "StartTime", value = "开始时间 (时：yyyy-MM-dd HH，日：yyyy-MM-dd，月：yyyy-MM，年：yyyy）", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "EndTime", value = "结束时间（时：yyyy-MM-dd HH，日：yyyy-MM-dd，月：yyyy-MM，年：yyyy）", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "stationNum", value = "台站号(54236,54668)", required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "city", value = "市名(沈阳市，铁岭市...)", required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "cnty", value = "县名", required = false, paramType = "query", dataType = "String")

    })
    public String getStationQUShi(String stationType,String dataType, String obsveName, String StartTime, String EndTime, String stationNum,String city,String cnty) throws ParseException {
        String result="";
        common.getCrossOrigin();
        if(dataType==null ||dataType.equals("")){
            result="数据类型不能为空";
        }else if(obsveName==null ||obsveName.equals("")){
            result="要素名称不能为空";
        }

        SimpleDateFormat sim=null;
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if(dataType.equals("时")){
            sim=new SimpleDateFormat("yyyy-MM-dd HH");
        }else if(dataType.equals("日")){
            sim=new SimpleDateFormat("yyyy-MM-dd");
        }else if(dataType.equals("月")){
            sim=new SimpleDateFormat("yyyy-MM");
        }else if(dataType.equals("年")){
            sim=new SimpleDateFormat("yyyy");
        }
        if(StartTime!=null&&!StartTime.equals("")&&EndTime!=null&&!EndTime.equals("")){
            try {
                Date startDate= sim.parse(StartTime);
                Date endDate =sim.parse(EndTime);
                StartTime=simpleDateFormat.format(startDate);
                EndTime=simpleDateFormat.format(endDate);
            }catch (Exception e){
                result="请重新选择开始结束时间";
                if(dataType.equals("时")){
                    result+=",格式为 yyyy-MM-dd HH";
                }else if(dataType.equals("日")){
                    result+=",格式为 yyyy-MM-dd";
                }else if(dataType.equals("月")){
                    result+=",格式为 yyyy-MM";
                }else if(dataType.equals("年")){
                    result+=",格式为 yyyy";
                }
            }
        }

        if(result.equals("")){
            result=dataAvgAndMService.getStationQUShi(stationType,dataType,obsveName,StartTime,EndTime,stationNum,city, cnty);
        }else{
            result=StrUtil.packageResult(result);
        }
        return result;
    }

    @ApiOperation(value = "获取方程系数")
    @PostMapping("/getKBR")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "obsvdate", value = "台站类型([{\"staname\":\"康平\",\"stanum\":\"54244\",\"time\":\"2015-08-29 00:00:00\",\"data\":19.1},{\"staname\":\"法库\",\"stanum\":\"54245\",\"time\":\"2015-08-29 00:00:00\",\"data\":19.0},...])", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "type", value = "种类（1，代表单站某一时间段；2，代表具体时间多站）", required = true, paramType = "query", dataType = "String")
    })
    public String getKBR(String obsvdate,String type)  {
        String result="";
        common.getCrossOrigin();
        try {
            obsvdate= URLDecoder.decode(obsvdate,"utf-8");
            JSONArray jsonArray= JSON.parseArray(obsvdate);
            result=dataAvgAndMService.getABR(jsonArray,type);
        }catch (Exception e){
        e.printStackTrace();
            logger.error("获取方程系数失败"+e.getMessage());
            result="获取方程系数失败"+e.getMessage();
        }
        return result;
    }

    @ApiOperation(value = "序列订正延长")
    @PostMapping("/getXuLieYanChang")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sequence", value = "长序列结果([{\"时间\":\"1961-01-02\",\"长序列\": \"-220\",\"短序列\": \"-220\"}, {\"时间\": \"1961-01-05\",,\"长序列\": \"-220\"\"短序列\": \"-195\" },...])", required = true, paramType = "query", dataType = "String")
    })
    public String getXuLieYanChang(String sequence)  {
        common.getCrossOrigin();
        String result="";
        List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
        try
        {
            sequence=URLDecoder.decode(sequence,"utf-8");
            List<Map<String,Object>> dt=common.getList(sequence,new String []{"时间","长序列","短序列","站号","要素"});
            if (dt != null && dt.size() > 0)
            {
                list=dataMethodService.getYanchangDataResult(dt);
                JSONArray jsonArray=new JSONArray();
                JSONObject jsonObject;
                for (int i=0;i<list.size();i++){
                    jsonObject=new JSONObject(true);
                    jsonObject.put("时间",list.get(i).get("时间"));
                    jsonObject.put("站号",list.get(i).get("站号"));
                    jsonObject.put("要素",list.get(i).get("要素"));
                    jsonObject.put("短序列",list.get(i).get("短序列"));
                    jsonObject.put("短序列订正值",list.get(i).get("短序列订正值"));
                    jsonObject.put("长序列",list.get(i).get("长序列"));
                    jsonArray.add(jsonObject);
                }
                result=jsonArray.toJSONString();
            }
        }
        catch (Exception e)
        {
            logger.error("历史序列订正延长查询失败"+e.getMessage());
            e.printStackTrace();
            result="历史序列订正延长失败"+e.getMessage();
        }
        return  result;
    }
    @ApiOperation(value = "上传短序列文件", notes = "上传文件", httpMethod="POST" ,consumes="multipart/form-data")
    @PostMapping(value = "/uploadSFile")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "file",value = "单个序列文件，",paramType = "formData",required = true,dataType = "file"),
            @ApiImplicitParam(name = "dataType", value = "数据类型（时，日，年）", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "stationNum", value = "台站号(54236)", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "obsvName", value = "要素名称(平均气温，最高气温，...)", required = true, paramType = "query", dataType = "String")

    })
    public String uploadSFile( MultipartFile file,String dataType,String  stationNum,String obsvName)  {
        //跨域
        common.getCrossOrigin();
        JSONObject jsObject=new JSONObject();
        if(("时，日，年").indexOf(dataType)<0){
            jsObject.put("失败","序列订正延长只支持小时数据，日数据，年数据");
            return jsObject.toJSONString() ;
        }
        if(stationNum==null ||stationNum.equals("")){
            jsObject.put("失败","请选择短序列文件所属的台站");
            return jsObject.toJSONString() ;
        }
        if(obsvName==null ||obsvName.equals("")){
            jsObject.put("失败","请选择短序列文件表示的要素");
            return jsObject.toJSONString() ;
        }
        if(file==null){
            jsObject.put("失败","短序列文件null");
            return jsObject.toJSONString() ;
        }
        String result=dataMethodService.getFileContent(file,"短序列",dataType);
        JSONArray jsonArray=JSON.parseArray(result);
        JSONArray jsonArrayAfter=new JSONArray();
        JSONObject jsonObjectbefor;
        JSONObject jsonObjectafter;
        for(int i=0;i<jsonArray.size();i++){
            jsonObjectbefor=jsonArray.getJSONObject(i);
            jsonObjectafter=new JSONObject(true);
            jsonObjectafter.put("时间",jsonObjectbefor.get("时间"));
            jsonObjectafter.put("站号",stationNum);
            jsonObjectafter.put("要素",obsvName);
            jsonObjectafter.put("短序列",jsonObjectbefor.get("短序列"));
            jsonArrayAfter.add(jsonObjectafter);
        }
        result=jsonArrayAfter.toJSONString();
        if("上传文件格式不正确".equals(result)){
            jsObject.put("失败",result);
        }else{
            jsObject.put("成功",result);
        }
        return jsObject.toJSONString() ;
    }

    @ApiOperation(value = "短序列数据库查询")
    @PostMapping(value = "/duanXulieDate")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "stationType", value = "台站类型（国家站，区域站）", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "timeType", value = "数据类型（时，日，月，年）", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "beginTime", value = "短序列开始时间 (时：yyyy-MM-dd HH，日：yyyy-MM-dd，月：yyyy-MM，年：yyyy）", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "endTime", value = "短序列结束时间（时：yyyy-MM-dd HH，日：yyyy-MM-dd，月：yyyy-MM，年：yyyy）", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "stationNum", value = "短序列台站号(54236,54668)", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "obsvval", value = "短序列要素名称(tem_avg，tem_max，...)", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "obsvName", value = "短序列要素名称(平均气温，最高气温，...)", required = true, paramType = "query", dataType = "String")
    })
    public String duanXulieDate(String stationType,String timeType,String beginTime,String endTime,String stationNum,String obsvval,String obsvName)  {
        //跨域
        common.getCrossOrigin();
        JSONObject jsObject=new JSONObject();
        JSONArray sequenceLlist;
        try {
            String[] prarm={"站号","时间","要素","短序列"};
            String tiaojian=", '"+stationNum+"' as 站号, '"+obsvName+"' as 要素 ";
            sequenceLlist = dataMethodService.getXulieYanchangData(stationType,timeType, beginTime, endTime, stationNum, obsvval,"短序列",tiaojian,prarm);
            if(sequenceLlist.size()==0){
                jsObject.put("失败","请重新选择需要查询的一段时间的短序列，原查询结果为空");
            }else{
                jsObject.put("成功",sequenceLlist);
            }
        }catch (Exception e){
            logger.error("短序列查询失败"+e.getMessage());
            jsObject.put("失败","短序列查询失败"+e.getMessage());
        }
        return jsObject.toJSONString();
    }

    @ApiOperation(value = "长序列数据库查询")
    @PostMapping(value = "/changXulieDate")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "stationType", value = "台站类型（国家站，区域站）", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "timeType", value = "数据类型（时，日，月，年）", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "beginTime", value = "长序列开始时间 (时：yyyy-MM-dd HH，日：yyyy-MM-dd，月：yyyy-MM，年：yyyy）", required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "endTime", value = "长序列结束时间（时：yyyy-MM-dd HH，日：yyyy-MM-dd，月：yyyy-MM，年：yyyy）", required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "stationNum", value = "长序列台站号(54236,54668)", required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "obsvName", value = "长序列要素名称(tem_avg，tem_max，...)", required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "sequenceS", value = "短序列([{\"时间\":\"1961-01-02\",\"站号\":\"54236\",,\"要素\":\"最高气温\",\"短序列\": \"-220\"}, {\"时间\": \"1961-01-05\",\"站号\":\"54236\",,\"要素\":\"最高气温\",\"短序列\": \"-195\" },...])", required = true, paramType = "query", dataType = "String")
    })
    public String changXulieDate(String stationType,String timeType,String beginTime,String endTime,String stationNum,String obsvName,String sequenceS)  {
        //跨域
        common.getCrossOrigin();
       String result="";
        JSONObject jsObject=new JSONObject();
        JSONArray sequenceLlist;
        try {
            sequenceS=URLDecoder.decode(sequenceS,"utf-8");
            List<Map<String,Object>> sequenceSlist=common.getList(sequenceS,new String[]{"时间","站号","要素","短序列"});
            String [] prarm={"时间","长序列"};
            sequenceLlist = dataMethodService.getXulieYanchangData(stationType,timeType, beginTime, endTime, stationNum, obsvName,"长序列","",prarm);
            if(("时，日，年").indexOf(timeType)<0){
                jsObject.put("失败","序列订正延长只支持小时数据，日数据，年数据");
                return jsObject.toJSONString();
            }
            if(sequenceLlist.size()>0){
                List<Map<String,Object>> list =common.getList(sequenceLlist.toJSONString(),prarm);
                result=getHebingXulieDate(list,sequenceSlist,timeType).replace("/","");
            }else{
                jsObject.put("失败","长序列查询结果为null");
                result=jsObject.toJSONString();
            }
        }catch (Exception e){
            logger.error("查询长序列失败"+e.getMessage());
            e.printStackTrace();
            jsObject.put("失败","上传长序列失败"+e.getMessage());
            return  jsObject.toJSONString();
        }
        return  result;
    }

    @ApiOperation(value = "上传长序列文件", notes = "上传文件", httpMethod="POST" ,consumes="multipart/form-data")
    @PostMapping(value = "/uploadLFile")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "timeType", value = "数据类型（时，日，月，年）", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "file",value = "长序列文件，",paramType = "formData",required = true,dataType = "file"),
            @ApiImplicitParam(name = "sequenceS", value = "短序列([{\"时间\":\"1961-01-02\",\"站号\":\"54236\",,\"要素\":\"最高气温\",\"短序列\": \"-220\"}, {\"时间\": \"1961-01-05\",\"站号\":\"54236\",,\"要素\":\"最高气温\",\"短序列\": \"-195\" },...])", required = true, paramType = "query", dataType = "String")
    })
    public String uploadLFile(String timeType, MultipartFile file,String sequenceS)  {
        //跨域
        common.getCrossOrigin();
        String result="";
        JSONObject jsObject=new JSONObject();
        List<Map<String,Object>> sequenceSlist;
        try {
            sequenceS=URLDecoder.decode(sequenceS,"utf-8");
            sequenceSlist=common.getList(sequenceS,new String[]{"站号","时间","要素","短序列"});
            if(("时，日，年").indexOf(timeType)<0){
                jsObject.put("失败","序列订正延长只支持小时数据，日数据，年数据");
                result= jsObject.toJSONString();
            }else{
                String sequenceL=dataMethodService.getFileContent(file,"长序列",timeType);
                List<Map<String,Object>> sequenceLlist=common.getList(sequenceL,new String[]{"时间","长序列"});
                if(sequenceLlist.size()==0){
                    jsObject.put("失败","请重新上传长序列文件，原长序列文件为null");
                    result= jsObject.toJSONString();
                }else if(sequenceSlist.size()==0){
                    jsObject.put("失败","请重新上传短序列文件，原短序列文件为null");
                    result= jsObject.toJSONString();
                }else{
                    result=getHebingXulieDate(sequenceLlist,sequenceSlist,timeType);
                }
            }
        }catch (Exception e){
            logger.error("上传长序列失败"+e.getMessage());
            jsObject.put("上传失败","上传长序列失败"+e.getMessage());
            result= jsObject.toJSONString();
        }
        return  result;
    }

    public String  getHebingXulieDate(List<Map<String,Object>> sequenceLlist,List<Map<String,Object>> sequenceSlist,String timeType) {
       JSONObject jsObject=new JSONObject();
        try {
           List<Map<String,Object>>listResult=dataMethodService.getHeBingDataResult(sequenceLlist,sequenceSlist,timeType);
           if(listResult==null){
               jsObject.put("失败","长序列结果集长度小于短序列结果集，无法做短序列订正延长，请重新查询或者上传长序列！");
           }else if(listResult.size()>0){
               if(listResult.get(0).get("error")!=null){
                   jsObject.put("失败",listResult.get(0).get("error"));
               }else{
                   JSONObject hebingObject;
                   JSONArray jsonArray=new JSONArray();
                   for(int i=0;i<listResult.size();i++){
                       hebingObject=new JSONObject();
                       for (Map.Entry<String,Object> map:listResult.get(i).entrySet()) {
                           hebingObject.put(map.getKey(),String.valueOf(map.getValue()));
                       }
                       jsonArray.add(i,hebingObject);
                   }
                   jsObject.put("成功",jsonArray.toString());
               }
           }
       }catch (Exception e){
            logger.error("合并长段序列失败-----getHebingXulieDate"+e.getMessage());
            jsObject.put("失败","上传长序列失败"+e.getMessage());
       }
        return  jsObject.toJSONString();
    }

    @ApiOperation(value = "短序列入库保存")
    @PostMapping(value = "/saveDuanXulieDate")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sequence", value = "序列订正延长结果([{\"时间\":\"1961-01-02\",\"长序列\": \"-220\",\"短序列\": \"-220\"}, {\"时间\": \"1961-01-05\",,\"长序列\": \"-220\"\"短序列\": \"-195\" },...])", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "timeType", value = "数据类型（时，日，月，年）", required = true, paramType = "query", dataType = "String")
    })
    public String saveDuanXulieDate(String sequence,String timeType)  {
        //跨域
        common.getCrossOrigin();
        String  result="";
        try {
            sequence=URLDecoder.decode(sequence,"utf-8");
            List<Map<String,Object>> dt=common.getList(sequence,new String []{"时间","长序列","短序列","站号","要素","短序列订正值"});
           result=dataMethodService.saveDuanxulie(dt,timeType);

        }catch (Exception e){
            logger.error("短序列保存数据库失败"+e.getMessage());
            result="保存失败："+e.getMessage();
        }
        return  result;
    }


    @ApiOperation(value = "序列订正数据查询")
    @PostMapping(value = "/getXulieDingZheng")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "timeType", value = "数据类型（时，日，月，年）", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "beginTime", value = "开始时间 (时：yyyy-MM-dd HH，日：yyyy-MM-dd，月：yyyy-MM，年：yyyy）", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "endTime", value = "结束时间（时：yyyy-MM-dd HH，日：yyyy-MM-dd，月：yyyy-MM，年：yyyy）", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "stationNum", value = "台站号(54236,54668)", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "obsvName", value = "要素名称(最高气温，平均值，...)", required = true, paramType = "query", dataType = "String")
    })
    public String getXulieDingZheng(String timeType,String beginTime,String endTime,String stationNum,String obsvName)  {
        //跨域
        common.getCrossOrigin();
        String result="";
        try {
            result=dataMethodService.getXulieDingZheng(timeType,beginTime,endTime,stationNum,obsvName);
        }catch (Exception e){
            logger.error("查询短序列失败"+e.getMessage());
            e.printStackTrace();
            result="查询失败"+e.getMessage();
        }
        return  result;
    }
}
