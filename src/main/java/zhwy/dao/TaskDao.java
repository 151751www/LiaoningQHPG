package zhwy.dao;

import com.alibaba.fastjson.JSONArray;
import zhwy.entity.Task;
import java.util.List;
import java.util.Map;

public interface TaskDao {
    JSONArray selectTasks(String taskName,String type);
    String addTask(Task task);
    String deleteTask(String name);
    String updateTask(Task task,String name);
    String selectTasksExe(String name);
    List<Map<String,Object>> selectType();
}
