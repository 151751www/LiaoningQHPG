package zhwy.controller;

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
import zhwy.service.ChongXianQIService;
import zhwy.service.DataMethodService;
import zhwy.util.Common;

import java.net.URLDecoder;
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
            @ApiImplicitParam(name = "file",value = "年极值文件，",paramType = "formData",required = true,dataType = "file"),
            @ApiImplicitParam(name = "stationNum", value = "台站号(54236,54668)", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "obsvName", value = "要素名称(平均气温，最高气温，...)", required = true, paramType = "query", dataType = "String")

    })
    public String uploadFileForChongxianqi( MultipartFile file,String stationNum,String obsvName)  {
        //跨域
        common.getCrossOrigin();
        JSONObject jsObject=new JSONObject();
        String result=dataMethodService.getFileContent(file,"年值","年");
        if(result.indexOf("上传文件格式不正确")!=-1){
            jsObject.put("上传失败",result);
        }else{
            JSONArray array=JSONArray.parseArray(result);
            JSONArray resultArray=new JSONArray();
            JSONObject jsonObject;
            for (int i=0;i<array.size();i++){
                jsonObject=new JSONObject(true);
                jsonObject.put("站号",stationNum);
                jsonObject.put("时间",array.getJSONObject(i).get("时间"));
                jsonObject.put("要素",obsvName);
                jsonObject.put("年值",array.getJSONObject(i).get("年值"));
                resultArray.add(i,jsonObject);
            }
            jsObject.put("上传成功",resultArray.toJSONString());
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
        JSONArray sequenceLlist;
        try {
            String tiaojian=", '"+stationNum+"' as 站号, '"+obsvName+"' as 要素 ";
            sequenceLlist = dataMethodService.getXulieYanchangData(stationType,"年", beginTime, endTime, stationNum, obsvval,"年值",tiaojian,new String[]{"站号","时间","要素","年值"});
            if(sequenceLlist.size()==0){
                jsObject.put("查询失败","请重新选择需要查询的一段时间的年极值，原查询结果为空");
            }else{
                String ChangXulie= sequenceLlist.toJSONString();
                jsObject.put("查询成功",ChangXulie);
            }
        }catch (Exception e){
            logger.error("年极值查询失败"+e.getMessage());
            e.printStackTrace();
            jsObject.put("查询失败","年极值查询失败"+e.getMessage());
        }
        return  jsObject.toJSONString();
    }


    @ApiOperation(value = "重现期查询")
    @PostMapping(value = "/getDataForChongxianqi")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "yearData", value = "各年极值([{\"时间\":\"1951\",\"年值\":\"11.7\"},{\"时间\":\"1952\",\"年值\":\"12\"},{\"时间\":\"1953\",\"年值\":\"12\"},...])", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "suanfaType", value = "算法类型（耿贝尔分布法，皮尔逊Ⅲ分布法）", required = true, paramType = "query", dataType = "String")

    })
    public String getDataForChongxianqi( String  yearData,String suanfaType)  {
        String result = null;
        JSONObject jsonObject=new JSONObject();
        //跨域
        common.getCrossOrigin();
        try {
            yearData= URLDecoder.decode(yearData,"utf-8");
            suanfaType= URLDecoder.decode(suanfaType,"utf-8");
            List<Map<String,Object>> data=common.getList(yearData,new String[]{"时间","年值","要素"});
            String obsvName=String.valueOf(data.get(0).get("要素"));
            if(suanfaType.equals("耿贝尔分布法")){
                result =chongXianQIService.getChongXianQiForgumbel(data,obsvName);
            }else if(suanfaType.equals("皮尔逊Ⅲ分布法")){
                result=chongXianQIService.getXpForP3(data,obsvName);
            }
            jsonObject.put("重现期计算成功",result);
        }catch (Exception e){
            logger.error("ChongXianQiController---getDataForChongxianqi   计算重现期失败："+e.getMessage());
            e.printStackTrace();
            jsonObject.put("重现期计算失败",e.getMessage());
        }
        return jsonObject.toJSONString();
    }


}
