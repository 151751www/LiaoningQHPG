package zhwy.dao;

import com.alibaba.fastjson.JSONArray;
import zhwy.entity.Task;

public interface TaskDao {
    JSONArray selectTasks(String taskName);
    String addTask(Task task);
    String deleteTask(String name);
    String updateTask(Task task,String name);
    String selectTasksExe(String name);
}
