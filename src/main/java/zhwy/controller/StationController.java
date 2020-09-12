package zhwy.controller;

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

    @ApiOperation(value = "获取市")
    @PostMapping("/getCity")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "stationType", value = "台站类型（国家站，区域站）", required = true, paramType = "query", dataType = "String")
    })
    public String getCity(String stationType) {

        String message;
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
        String message;
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
            e.printStackTrace();
            logger.error("StationController 文件中县名加载失败：" + e);
            return "StationController 县名加载失败：" + e;
        }
    }

    @ApiOperation(value="获取台站名称")
    @PostMapping("/getStation")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "stationType", value = "台站类型（国家站，区域站）", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "city", value = "市名",  paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "cnty", value = "县名", paramType = "query", dataType = "String")
    })
    public String getRegStation(String stationType,String city ,String cnty){
        String message;
        //跨域
        common.getCrossOrigin();
        try {
            message = stationService.getRegStation(stationType,city,cnty);
            return message;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("StationController 文件中台站名称加载失败：" + e);
            return "StationController 文件中台站名称加载失败：" + e;
        }
    }
    @ApiOperation(value = "获取编码")
    @PostMapping("/getAreaCode")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "areaName", value = "地区名称", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "areaType", value = "地区类型（市，县）", required = true, paramType = "query", dataType = "String")
    })
    public String getAreaCode(String areaName,String areaType ) {
        String message ;
        //跨域
        common.getCrossOrigin();
        if(areaName==null||areaName.equals("")||areaType==null||areaType.equals("")){
            return "地区名称和类型不可为空" ;
        }
        try {
            message = stationService.getAreaCode(areaName,areaType);
            return message;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("StationController 文件中地区编码加载失败：" + e);
            return "StationController 文件中地区编码加载失败：" + e;
        }

    }


}
