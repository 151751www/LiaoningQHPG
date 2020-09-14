package zhwy.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import zhwy.dao.TaskDao;
import zhwy.entity.Task;
import zhwy.service.TaskManagementService;
import zhwy.util.FileUtil;
import zhwy.util.WinTaskUtil;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

@Service
public class TaskManagementServiceimpl  implements TaskManagementService {
    @Autowired
    public TaskDao taskDao;
    /**
     * 任务计划管理方法：增加，删除，修改
     * 方案：根据需求生成xml任务计划文件，通过schtasks /create 导入
     */
    @Override
    public JSONArray taskManagement(String taskName,String newName, String startTime,String stopTime,
                                    String repeatInterval, String dateType, String operationType, MultipartFile file) {
        String result="";
        String path=null;
        String fileName=null;
        SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //开始时间不为空，增加任务计划时，设置结束时间为开始时间50年后
        if(!"".equals(startTime)){
            try {
                if(stopTime==null||"".equals(stopTime)){
                    Calendar cal=Calendar.getInstance();
                    cal.setTime(sdf.parse(startTime));
                    cal.add(Calendar.YEAR,50);
                    stopTime=sdf.format(cal.getTime());
                }
                System.out.println(WinTaskUtil.getSetFileDir());
                path=WinTaskUtil.getSetFileDir().replace("/","\\")+"task";
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //保存文件
        if(!"".equals(file.getOriginalFilename())){
            FileUtil.saveFile(file,path);
            //做runTask.bat执行用户上传的jar,py
            if(file.getOriginalFilename().endsWith(".jar")||file.getOriginalFilename().endsWith(".py")){
                fileName=FileUtil.createBat(file.getOriginalFilename(),path);
            }else{
                fileName=file.getOriginalFilename();
            }
        }
        //根据计划类型分类
        if("增加".equals(operationType)){
            //将增加的任务计划插入数据库
            Task task=new Task();
            task.setName(taskName);
            task.setState("全部准备就绪");
            task.setLastTime(startTime);
            task.setNextTime(startTime);
            task.setPlanFre("每隔"+repeatInterval+dateType+"执行一次");
            task.setDataTime(startTime);
            task.setBeginTime(startTime);
            task.setStopTime(stopTime);
            task.setExec(fileName);
            task.setIsstart("true");
            task.setIsdisable("false");
            result=taskDao.addTask(task);
            if("OK".equals(result)){
                //添加任务计划，并立即启用
                result=WinTaskUtil.addWinTask(taskName,path,fileName,startTime,stopTime,repeatInterval,dateType);
            }
        }else if("删除".equals(operationType)){
            //直接通过命令删除任务计划
            result = taskDao.deleteTask(taskName);
            //删除数据库任务计划记录
            if("OK".equals(result)) {
                result=WinTaskUtil.deleteWinTask(taskName);
            }
        }else if("修改".equals(operationType)){
            //修改数据库中的任务计划
            Task task=new Task();
            task.setName(newName);
            task.setState("全部准备就绪");
            task.setLastTime(startTime);
            task.setNextTime(startTime);
            task.setPlanFre("每隔"+repeatInterval+dateType+"执行一次");
            task.setDataTime(startTime);
            task.setBeginTime(startTime);
            task.setExec(fileName);
            task.setIsstart("true");
            task.setIsdisable("false");
            result = taskDao.updateTask(task,taskName);
            //修改任务计划
            if("OK".equals(result)) {
                //修改任务计划失败，使用先删除旧的任务计划，再添加新的任务计划，达到修改的效果
                //result=WinTaskUtil.changeTask(taskName, startTime,stopTime,path+"\\"+fileName,repeatInterval,dateType);
                WinTaskUtil.deleteWinTask(taskName);
                if(fileName==null){
                    fileName =taskDao.selectTasksExe(newName);
                }
                if("".equals(stopTime)){
                    JSONArray taskJSON=taskDao.selectTasks(taskName);
                    stopTime=taskJSON.getJSONObject(0).getString("stopTime");
                }
                result=WinTaskUtil.addWinTask(newName,path,fileName,startTime,stopTime,repeatInterval,dateType);
            }
        }else if("立即执行".equals(operationType)){//立即执行
            result=WinTaskUtil.runNow(taskName);
        }else if("禁用".equals(operationType)){//禁用
            Task task=new Task();
            task.setState("禁用");
            task.setIsstart("false");
            task.setIsdisable("true");
            result = taskDao.updateTask(task,taskName);
            if("OK".equals(result)) {
                result=WinTaskUtil.disbaleTask(taskName);
            }
        }else if("启用".equals(operationType)){//启用
            Task task=new Task();
            task.setState("全部准备就绪");
            task.setIsstart("true");
            task.setIsdisable("false");
            result = taskDao.updateTask(task,taskName);
            if("OK".equals(result)) {
                result=WinTaskUtil.enbaleTask(taskName);
            }
        }else if("自定义执行".equals(operationType)){
            //自定义执行，修改任务计划开始结束时间
            Task task=new Task();
            task.setBeginTime(startTime);
            task.setStopTime(stopTime);
            result=taskDao.updateTask(task,taskName);
            if("OK".equals(result)) {
                //修改任务计划失败，采用删除任务计划，增加新任务计划方式实现自定义执行
                //result=WinTaskUtil.changeTask(taskName, startTime,stopTime,path+"\\"+fileName,repeatInterval,dateType);
                WinTaskUtil.deleteWinTask(taskName);
                if(fileName==null){
                    fileName =taskDao.selectTasksExe(taskName);
                }
                if("".equals(repeatInterval)||"".equals(dateType)){
                    JSONArray taskJSON=taskDao.selectTasks(taskName);
                    String repeat=taskJSON.getJSONObject(0).getString("planFre");
                    repeat=repeat.substring(2);
                    repeat=repeat.substring(0,repeat.length()-4);
                    if(repeat.indexOf("小时")!=-1){
                        repeatInterval=repeat.substring(0,repeat.length()-2);
                        dateType="小时";
                    }else{
                        repeatInterval=repeat.substring(0,repeat.length()-1);
                        dateType=repeat.substring(repeat.length()-1);
                    }
                }
                result=WinTaskUtil.addWinTask(taskName,path,fileName,startTime,stopTime,repeatInterval,dateType);
            }
        }
        JSONArray array = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result",result);
        array.add(jsonObject);
        return array;
    }

    @Override
    public JSONArray selectTasks() {

        return taskDao.selectTasks("");
    }

}
