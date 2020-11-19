package zhwy.controller;

import com.alibaba.fastjson.JSONArray;
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
import zhwy.service.PerseService;
import zhwy.util.Common;

import java.net.URLDecoder;

@Api(position = 10,tags = "辽宁气候评估----配色方案")
@RestController
@RequestMapping("/peise")
public class PeiseController {
    private static Logger logger = LoggerFactory.getLogger(HuiTuGongJuController.class);

    @Autowired
    PerseService peise;
    @Autowired
    private Common common;

    @PostMapping("/getLegendType")
    @ApiOperation(value = "获取配色分类")
    public String getLegendType()  {

        //跨域
        common.getCrossOrigin();
        String result="";
        try {
           JSONArray array=peise.getPeiseType();
            result=array.toJSONString();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("查找配色分类失败："+e.getMessage());
            return "查找配色分类失败："+e.getMessage();
        }
        return result;
    }
    @PostMapping("/getLegendColor")
    @ApiOperation(value = "获取配色方案")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "filePath", value = "配色文件", required = true, paramType = "query", dataType = "String")

    })
    public String getLegendColor(String filePath)  {
        //跨域
        common.getCrossOrigin();
        String result="";
        try {
            result=peise.GetLegendColor(filePath);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("查找配色方案失败："+e.getMessage());
            return "查找配色方案失败："+e.getMessage();
        }
        return result;
    }
    @PostMapping("/delLegendColorFile")
    @ApiOperation(value = "删除配色方案")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "fileName", value = "配色名称", required = true, paramType = "query", dataType = "String")

    })
    public String delLegendColorFile(String fileName)  {
        //跨域
        common.getCrossOrigin();
        String result="";
        try {
            result=peise.DelLegendColorFile(fileName);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("删除配色方案失败："+e.getMessage());
            return "删除配色方案失败："+e.getMessage();
        }
        return result;
    }
    @PostMapping("/saveLegendColorFile")
    @ApiOperation(value = "保存配色方案")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "jsonColor", value = "配色方案json串([{\"colorMinValues\":\"0\",\"colorMaxValues\":\"18.5\",\"colorValues\":\"228,95,18\"},{\"colorMinValues\":\"18.6\",\"colorMaxValues\":\"19\",\"colorValues\":\"132,255,0\"},{\"colorMinValues\":\"19.1\",\"colorMaxValues\":\"19.5\",\"colorValues\":\"62,194,191\"},{\"colorMinValues\":\"19.6\",\"colorMaxValues\":\"21.5\",\"colorValues\":\"213,44,205\"},{},{}])", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "fileName", value = "配色名称", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "type", value = "新增或修改（add,update）", required = true, paramType = "query", dataType = "String")

    })
    public String SaveLegendColorFile(String jsonColor,String  fileName,String type)  {
        //跨域
        common.getCrossOrigin();
        String result="";
        try {
            jsonColor= URLDecoder.decode(jsonColor,"utf-8");
            result=peise.SaveLegendColorFile(jsonColor,fileName,type);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("保存配色方案失败："+e.getMessage());
            return "保存配色方案失败："+e.getMessage();
        }
        return result;
    }

}
