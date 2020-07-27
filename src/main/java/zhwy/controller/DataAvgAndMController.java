package zhwy.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;
import zhwy.service.DataAvgAndMService;
import zhwy.util.Common;

import java.util.HashMap;
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



    @ApiOperation(value = "数据平均值")
    @PostMapping("/getDataForAvg")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "stationType", value = "台站类型", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "dataType", value = "数据类型（时，日，月，年）", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "ObsveName", value = "要素名称", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "StartTime", value = "开始时间", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "EndTime", value = "结束时间", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "city", value = "市名", required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "cnty", value = "县名", required = false, paramType = "query", dataType = "String")

    })
    public String getDataForAvg(String stationType,String dataType,String ObsveName, String StartTime,String EndTime, String city,String cnty){
        String result="";
        common.getCrossOrigin();
        Map<String,String> obsvMap=new HashMap<String,String>();
        String key="";
        String value="";
        if(stationType==null ||stationType.equals("")){
            result="台站类型不能为空";
        }else if(dataType==null ||dataType.equals("")){
            result="数据类型不能为空";
        }else if(ObsveName==null ||ObsveName.equals("")){
            result="要素名称不能为空";
        }else if(StartTime==null||StartTime.equals("")){
            result="开始时间不能为空";
        }else if(EndTime==null||EndTime.equals("")){
            result="结束时间不能为空";
        }
        if("时".equals(dataType)){
            obsvMap=common.itemH;
        }else if("日".equals(dataType)){
            obsvMap=common.itemD;
        }else if("月".equals(dataType)){
            obsvMap=common.itemM;
        }else if("年".equals(dataType)){
            obsvMap=common.itemY;
        }else{
            result="数据类型无效";
        }
        if(result.equals("")){
            for(Map.Entry<String,String>entry:obsvMap.entrySet()){
                if(ObsveName.equals(entry.getKey())||ObsveName.equals(entry.getValue())){
                    key=entry.getKey();
                    value=entry.getValue();
                }
            }
            if(!key.equals("")&&!value.equals("")){
                result=dataAvgAndMService.getDataForAvg(stationType,dataType,key,value,StartTime,EndTime,city,cnty);
            }else{
                result="要素名称无效";
            }
        }
        return result;
    }

    @ApiOperation(value = "数据极值")
    @PostMapping("/getDataForM")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "stationType", value = "台站类型", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "dataType", value = "数据类型（时，日，月，年）", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "ObsveName", value = "要素名称", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "StartTime", value = "开始时间", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "EndTime", value = "结束时间", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "city", value = "市名", required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "cnty", value = "县名", required = false, paramType = "query", dataType = "String")

    })
    public String getDataForM(String stationType,String dataType,String ObsveName, String StartTime,String EndTime, String city,String cnty){
        String result="";
        common.getCrossOrigin();
        Map<String,String> obsvMap=new HashMap<String,String>();
        String key="";
        String value="";
        if(stationType==null ||stationType.equals("")){
            result="台站类型不能为空";
        }else if(dataType==null ||dataType.equals("")){
            result="数据类型不能为空";
        }else if(ObsveName==null ||ObsveName.equals("")){
            result="要素名称不能为空";
        }else if(StartTime==null||StartTime.equals("")){
            result="开始时间不能为空";
        }else if(EndTime==null||EndTime.equals("")){
            result="结束时间不能为空";
        }
        if("时".equals(dataType)){
            obsvMap=common.itemH;
        }else if("日".equals(dataType)){
            obsvMap=common.itemD;
        }else if("月".equals(dataType)){
            obsvMap=common.itemM;
        }else if("年".equals(dataType)){
            obsvMap=common.itemY;
        }else{
            result="数据类型无效";
        }
        if(result.equals("")){
            for(Map.Entry<String,String>entry:obsvMap.entrySet()){
                if(ObsveName.equals(entry.getKey())||ObsveName.equals(entry.getValue())){
                    key=entry.getKey();
                    value=entry.getValue();
                }
            }
            if(!key.equals("")&&!value.equals("")){
                result=dataAvgAndMService.getDataForM(stationType,dataType,key,value,StartTime,EndTime,city,cnty);
            }else{
                result="要素名称无效";
            }

        }
        return result;
    }

    @ApiOperation(value = "数据原始值")
    @PostMapping("/getDataForOrig")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "stationType", value = "台站类型", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "dataType", value = "数据类型（时，日，月，年）", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "ObsveName", value = "要素名称", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "StartTime", value = "开始时间", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "EndTime", value = "结束时间", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "city", value = "市名", required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "cnty", value = "县名", required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "page", value = "第几页", required = true, paramType = "query", dataType = "Integer"),
            @ApiImplicitParam(name = "pagesize", value = "每页显示条数", required = true, paramType = "query", dataType = "Integer")

    })
    public String getDataForOrig(String stationType,String dataType,String ObsveName, String StartTime,String EndTime, String city,String cnty,Integer page,Integer pagesize){
        String result="";
        common.getCrossOrigin();
        Map<String,String> obsvMap=new HashMap<String,String>();
        String key="";
        String value="";
        if(stationType==null ||stationType.equals("")){
            result="台站类型不能为空";
        }else if(dataType==null ||dataType.equals("")){
            result="数据类型不能为空";
        }else if(ObsveName==null ||ObsveName.equals("")){
            result="要素名称不能为空";
        }else if(StartTime==null||StartTime.equals("")){
            result="开始时间不能为空";
        }else if(EndTime==null||EndTime.equals("")){
            result="结束时间不能为空";
        }else if(pagesize<1||page<1){
            result="需要查询的页数和每页显示的条数应>0";
        }
        if("时".equals(dataType)){
            obsvMap=common.itemH;
        }else if("日".equals(dataType)){
            obsvMap=common.itemD;
        }else if("月".equals(dataType)){
            obsvMap=common.itemM;
        }else if("年".equals(dataType)){
            obsvMap=common.itemY;
        }else{
            result="数据类型无效";
        }
        if(result.equals("")){
            for(Map.Entry<String,String>entry:obsvMap.entrySet()){
                if(ObsveName.equals(entry.getKey())||ObsveName.equals(entry.getValue())){
                    key=entry.getKey();
                    value=entry.getValue();
                }
            }
            if(!key.equals("")&&!value.equals("")){
                result=dataAvgAndMService.getDataForOrig(stationType,dataType,key,value,StartTime,EndTime,city,cnty,page,pagesize);
            }else{
                result="要素名称无效";
            }
        }
        return result;
    }

}
