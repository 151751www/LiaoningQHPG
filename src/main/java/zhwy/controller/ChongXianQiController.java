package zhwy.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;
import zhwy.service.ChongXianQIService;
import zhwy.service.DataAvgAndMService;
import zhwy.service.DataMethodService;
import zhwy.util.Common;
import org.apache.commons.math3.special.Gamma;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Api(position = 9,tags = "辽宁气候评估----重现期计算")
@RestController
@SessionAttributes
@RequestMapping("/ChongXianQi")
public class ChongXianQiController {

    private static Logger logger = LoggerFactory.getLogger(DataAvgAndMController.class);

    @Autowired
    private Common common;
    @Autowired
    private ChongXianQIService chongXianQIService;
    @Autowired
    private DataMethodService dataMethodService;

    @ApiOperation(value = "上传年极值文件", notes = "上传文件", httpMethod="POST" ,consumes="multipart/form-data")
    @PostMapping(value = "/uploadFileForChongxianqi")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "file",value = "年极值文件，",paramType = "formData",required = true,dataType = "file")
    })
    public String uploadFileForChongxianqi( MultipartFile file)  {
        //跨域
        common.getCrossOrigin();
        JSONObject jsObject=new JSONObject();
        String result=dataMethodService.getFileContent(file,"年值","年");
        if(result.indexOf("上传文件格式不正确")!=-1){
            jsObject.put("上传失败",result);
        }else{
            jsObject.put("上传成功",result);
        }
        return jsObject.toJSONString() ;
    }
    @ApiOperation(value = "年极值数据库查询")
    @PostMapping(value = "/getYrarDate")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "stationType", value = "台站类型（国家站，区域站）", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "beginTime", value = "开始时间 (年：yyyy）", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "endTime", value = "结束时间（年：yyyy）", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "stationNum", value = "台站号(54236,54668)", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "obsvval", value = "要素名称(tem_avg，tem_max，...)", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "obsvName", value = "要素名称(平均气温，最高气温，...)", required = true, paramType = "query", dataType = "String")
    })
    public String getYrarDate(String stationType,String beginTime,String endTime,String stationNum,String obsvval,String obsvName)  {
        //跨域
        common.getCrossOrigin();
        JSONObject jsObject=new JSONObject();
        List<Map<String,Object>> sequenceLlist;
        try {
            String tiaojian=", '"+stationNum+"' as 站号, '"+obsvName+"' as 要素 ";
            sequenceLlist = dataMethodService.getXulieYanchangData(stationType,"年", beginTime, endTime, stationNum, obsvval,"年极值",tiaojian);
            if(sequenceLlist.size()==0){
                jsObject.put("查询失败","请重新选择需要查询的一段时间的年极值，原查询结果为空");
            }else{
                String ChangXulie= JSONArray.parseArray(JSON.toJSONString(sequenceLlist)).toJSONString();
                jsObject.put("查询成功",ChangXulie);
            }
        }catch (Exception e){
            logger.error("年极值查询失败"+e.getMessage());
            e.printStackTrace();
            jsObject.put("查询失败","年极值查询失败"+e.getMessage());
        }
        return  StringEscapeUtils.unescapeJava(jsObject.toJSONString());
    }





    @ApiOperation(value = "重现期查询")
    @PostMapping(value = "/getDataForChongxianqi")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "yearData", value = "各年极值([{\"时间\":\"1951\",\"年值\":\"11.7\"},{\"时间\":\"1952\",\"年值\":\"12\"},{\"时间\":\"1953\",\"年值\":\"12\"},...])", required = true, paramType = "query", dataType = "String")
    })
    public String getDataForChongxianqi( String  yearData,String obsvName)  {
        String result;
        JSONObject jsonObject=new JSONObject();
        //跨域
        common.getCrossOrigin();
        List<Map<String,Object>> data=common.getList(yearData,new String[]{"时间","年值"});
        try {
            result =chongXianQIService.getChongXianQiForgumbel(data,obsvName);
            jsonObject.put("重现期计算成功",result);
        }catch (Exception e){
            logger.error("ChongXianQiController---getDataForChongxianqi   计算重现期失败："+e.getMessage());
            e.printStackTrace();
            jsonObject.put("重现期计算失败",e.getMessage());
        }
        return StringEscapeUtils.unescapeJava(jsonObject.toJSONString());
    }

    @ApiOperation(value = "重现期查询P3")
    @PostMapping(value = "/getDataForP3Chongxianqi")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "yearData", value = "各年极值([{\"时间\":\"1951\",\"年值\":\"11.7\"},{\"时间\":\"1952\",\"年值\":\"12\"},{\"时间\":\"1953\",\"年值\":\"12\"},...])", required = true, paramType = "query", dataType = "String")
    })
    public String getDataForP3Chongxianqi( String  yearData)  {
        String result;
        JSONObject jsonObject=new JSONObject();
        //跨域
        common.getCrossOrigin();
        List<Map<String,Object>> data=common.getList(yearData,new String[]{"时间","年值"});
        try {
            double[] li =new double[data.size()];
            for (int i=0;i<data.size();i++){
                li[i]=Double.parseDouble(data.get(i).get("年值").toString());
            }
            Map<String,Double> map=chongXianQIService.GetCanShuPNew(li);
            double a=chongXianQIService.GetValue_a(map.get("Cs"));
            double b=chongXianQIService.GetValue_b(map.get("avgx"),map.get("Cs"),map.get("Cv"));
            double a0=chongXianQIService.GetValue_a0(map.get("avgx"),map.get("Cs"),map.get("Cv"));
            double gama=Gamma.gamma(a);
            jsonObject.put("avgx",map.get("avgx"));
            jsonObject.put("Cv",map.get("Cv"));
            jsonObject.put("Cs_Cv",map.get("Cs_Cv"));
            jsonObject.put("Cs",map.get("Cs"));
            jsonObject.put("gama",gama);
            jsonObject.put("a0",a0);
            jsonObject.put("a",a);
            jsonObject.put("b",b);
        }catch (Exception e){
            logger.error("ChongXianQiController---getDataForChongxianqi   计算重现期失败："+e.getMessage());
            e.printStackTrace();
            jsonObject.put("重现期计算失败",e.getMessage());
        }
        return StringEscapeUtils.unescapeJava(jsonObject.toJSONString());
    }

}
