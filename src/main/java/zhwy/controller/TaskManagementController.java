package zhwy.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.fastjson.JSONArray;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import zhwy.service.TaskManagementService;
import zhwy.util.WinTaskUtil;

import java.io.File;

@RestController(value="/tasks")
@Api(value = "/taskManagement",tags = {"任务计划管理"})
public class TaskManagementController {

    @Autowired
    TaskManagementService taskManagementService;

    @PostMapping(value = "/taskManagement")
    @ApiOperation(value = "taskManagement",notes = "任务计划管理")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "任务计划名称",required=true,paramType="query",dataType="String"),
            @ApiImplicitParam(name = "newName", value = "新任务计划名称",required=false,paramType="query",dataType="String"),
            @ApiImplicitParam(name = "startTime", value = "开始时间,格式:yyyy-mm-dd hh:mm:ss",required=false,paramType="query",dataType="String"),
            @ApiImplicitParam(name = "stopTime", value = "终止时间,格式:yyyy-mm-dd hh:mm:ss",required=false,paramType="query",dataType="String"),
            @ApiImplicitParam(name = "repeatInterval", value = "重复间隔", required=false,paramType="query",dataType="String"),
            @ApiImplicitParam(name = "dateType", value = "重复间隔单位,例如：分,小时,天,月,年等", required=false,paramType="query",dataType="String"),
            @ApiImplicitParam(name = "operationType", value = "操作类型,对任务计划执行什么操作,例如:增加,修改,删除,引用,启用,立即执行,自定义执行",required=true,paramType="query",dataType="String"),
            @ApiImplicitParam(name = "file", value = "上传的文件",required=false,paramType="query",dataType="MultipartFile")
    })
    public JSONArray taskManagement(String name,String newName,String startTime,String stopTime,String repeatInterval,String dateType,String operationType,MultipartFile file){
        return taskManagementService.taskManagement(name,newName,startTime,stopTime,repeatInterval,dateType,operationType,file);
    }

    @PostMapping(value = "/selectTasks")
    @ApiOperation(value = "selectTasks",notes = "查询任务计划")
    public JSONArray selectTasks(){

        return taskManagementService.selectTasks();
    }

}
