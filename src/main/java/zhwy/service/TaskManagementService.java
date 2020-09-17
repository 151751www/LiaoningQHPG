package zhwy.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.web.multipart.MultipartFile;

public interface TaskManagementService {
    /**
     * 任务计划管理方法：增加，删除，修改
     * @param taskName 任务计划名称
     * @param startTime 起始时间
     * @param repeatInterval 重复间隔
     * @param dateType 重复间隔单位或任务计划类型：minute,hour,day,month,year
     * @param operationType 操作类型：add,delete,update,run,runNow等
     * @return OK
     */
    JSONArray taskManagement(String taskName,String type,String newName, String startTime,String stopTime, String repeatInterval, String dateType, String operationType, MultipartFile file);
    /**
     * 查询所有任务计划
     * @return JSONArray
     */
    JSONObject selectTasks();

}
