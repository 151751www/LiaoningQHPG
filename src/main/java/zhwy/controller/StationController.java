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
import zhwy.service.StationService;
import zhwy.util.Common;

import java.util.HashMap;
import java.util.Map;

@Api(position = 9,tags = "辽宁气候评估----基本信息")
@RestController
@SessionAttributes
@RequestMapping("/Stations")
public class StationController {
    private static Logger logger = LoggerFactory.getLogger(StationController.class);
    @Autowired
    private Common common;
    @Autowired
    private StationService stationService;

    String [] hourObsv={"气压","水汽压","气温","相对湿度","露点温度","云","能见度","降水量","蒸发量","风","地温","草面温度","冻土","雪深"};
    String [] hourKey={"prs","vap","tem","rhu","dpt","clo","vis","pre","evp","win","gst","lgst","frs","snow"};
    String [] dayObsv={"气压","水汽压","气温","相对湿度","云","能见度","降水量","蒸发量","风","地温","草面温度","冻土","雪深","日照"};
    String [] dayKey={"prs","vap","tem","rhu","clo","vis","pre","evp","win","gst","lgst","frs","snow","ssh"};
    String [] monthObsv={"气压","水汽压","气温","相对湿度","云","能见度","降水量","蒸发量","风","地温","草面温度","冻土","雪深","日照"};
    String [] monthKey={"prs","vap","tem","rhu","clo","vis","pre","evp","win","gst","lgst","frs","snow","ssh"};
    String [] yearObsv={"气压","水汽压","气温","相对湿度","云","能见度","降水量","蒸发量","风","地温","草面温度","冻土","雪深","日照"};
    String [] yearKey={"prs","vap","tem","rhu","clo","vis","pre","evp","win","gst","lgst","frs","snow","ssh"};


    @ApiOperation(value = "获取市")
    @PostMapping("/getCity")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "stationType", value = "台站类型（国家站，区域站）", required = true, paramType = "query", dataType = "String")
    })
    public String getCity(String stationType) {

        String message = "";
        //跨域
        common.getCrossOrigin();
        try {
            if (stationType == null || stationType.equals("")) {
                message = "台站类型不能为空！";
            } else {
                message = stationService.getCity(stationType);
            }
            return message;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            logger.error("StationController 文件中市名加载失败：" + e);
            return "市名加载失败：" + e;
        }
    }

    @ApiOperation(value="获取县")
    @PostMapping("/getCounty")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "stationType", value = "台站类型（国家站，区域站）", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "city", value = "市名", required = false, paramType = "query", dataType = "String")
    })
    public String getCounty(String stationType ,String city){
        String message = "";
        //跨域
        common.getCrossOrigin();
        try {
            if (stationType == null || stationType.equals("")) {
                message = "台站类型不能为空！";
            } else {
                message = stationService.getCounty(stationType,city);
            }
            return message;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            logger.error("StationController 文件中县名加载失败：" + e);
            return "StationController 县名加载失败：" + e;
        }
    }

    @ApiOperation(value="获取要素大类")
    @PostMapping("/getObsv")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dataType", value = "数据类型（时，日，月，年）", required = true, paramType = "query", dataType = "String")
    })
    public String getObsv(String dataType ){
        String message = "";
        //跨域
        common.getCrossOrigin();

        String [] Obsvlement;
        String [] key ;
        JSONArray   array=new JSONArray();
        JSONObject obsv ;
        if(dataType.equals("时")){
            Obsvlement=hourObsv;
            key=hourKey;
        }else if(dataType.equals("日")){
            Obsvlement=dayObsv;
            key=dayKey;
        }else if(dataType.equals("月")){
            Obsvlement=monthObsv;
            key=monthKey;
        }else if(dataType.equals("年")){
            Obsvlement=yearObsv;
            key=yearKey;
        }else{
            Obsvlement=null;
            key=null;
        }
        try {
            if(Obsvlement!=null){
                for (int i=0;i<Obsvlement.length;i++){
                    obsv=new JSONObject();
                    obsv.put("要素名称",Obsvlement[i]);
                    obsv.put("要素编码",key[i]);
                    array.add(obsv);
                }
                message=array.toString();
            }else{
                message="数据类型不匹配，请重新输入数据类型，例如“时，日，月，年";
            }
        }catch (Exception e){
            logger.error("StationController  文件加载要素大类出问题："+e);
            message="加载大类要素失败："+e;
        }finally {
            return message;
        }
    }

    @ApiOperation(value="获取具体要素")
    @PostMapping("/getSpecObsv")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dataType", value = "数据类型（时，日，月，年）", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "ObsveName", value = "要素名称", required = true, paramType = "query", dataType = "String")
    })
    public String getSpecObsv(String dataType,String ObsveName ){
        String message = "";
        //跨域
        common.getCrossOrigin();
        JSONArray   array=new JSONArray();
        Map<String,String> obsv =new HashMap<String,String>() ;
        Map<String,String> dataMap=new HashMap<String,String>();
        String obsvKey="";
        String arrayObsv[];
        String arrayKey[];
        JSONObject jsonObject;
        if("时".equals(dataType)){
            dataMap=common.itemH;
            arrayObsv=hourObsv;
            arrayKey=hourKey;
        }else if("日".equals(dataType)){
            dataMap=common.itemD;
            arrayObsv=dayObsv;
            arrayKey=dayKey;
        }else if("月".equals(dataType)){
            dataMap=common.itemM;
            arrayObsv=monthObsv;
            arrayKey=monthKey;
        }else if("年".equals(dataType)){
            dataMap=common.itemY;
            arrayObsv=yearObsv;
            arrayKey=yearKey;
        }else{
            message="无效的数据类型";
            arrayObsv=null;
            arrayKey=null;

        }
        try {
            if(message.equals("")){
                for (int i=0;i<arrayObsv.length;i++){
                    if(ObsveName.equals(arrayObsv[i])){
                        obsvKey=arrayKey[i];
                    }
                }
                for (Map.Entry<String,String>entry:dataMap.entrySet()){
                    String key=entry.getKey();
                    if(key.startsWith(obsvKey)){
                        jsonObject=new JSONObject();
                        jsonObject.put(entry.getKey(),entry.getValue());
                        array.add(jsonObject);
                    }
                }
            }
            if(array.size()>0&&!obsvKey.equals("")){
                message=array.toString();
            }else{
                message="无效的要素名称";
            }
        }catch (Exception e){
            logger.error("StationController  文件加载要素大类出问题："+e);
            message="加载大类要素失败："+e;
        }finally {
            return message;
        }
    }

}
