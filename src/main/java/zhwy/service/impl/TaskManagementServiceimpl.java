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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class TaskManagementServiceimpl  implements TaskManagementService {
    @Autowired
    public TaskDao taskDao;
    /**
     * 任务计划管理方法：增加，删除，修改
     * 方案：根据需求生成xml任务计划文件，通过schtasks /create 导入
     */
    @Override
    public JSONArray taskManagement(String taskName,String type,String newName, String startTime,String stopTime,
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
                //path=WinTaskUtil.getSetFileDir().replace("/","\\")+"task";
                //暂时按照jar包路径获取统计目录
                path=WinTaskUtil.getSetFileDir().replace("/","\\");
                path=path.substring(path.indexOf("\\")+1);
                if(path.contains("!")){
                    path=path.substring(0,path.indexOf("!"));
                    path=path.substring(0,path.lastIndexOf("\\"))+"\\task";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if("".equals(type)){
            type="观测资料";
        }
        //保存文件
        if(file!=null&&!"".equals(file.getOriginalFilename())){
            FileUtil.saveFile(file,path);
            //做runTask.bat执行用户上传的jar,py
            if(Objects.requireNonNull(file.getOriginalFilename()).endsWith(".jar")){
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
            task.setType(type);
            task.setState("全部准备就绪");
            task.setLastTime(startTime);
            task.setNextTime(startTime);
            task.setPlanFre("每隔"+repeatInterval+dateType+"执行一次");
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
                    JSONArray taskJSON=taskDao.selectTasks(taskName,"");
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
                    JSONArray taskJSON=taskDao.selectTasks(taskName,"");
                    String repeat=taskJSON.getJSONObject(0).getString("planFre");
                    repeat=repeat.substring(2);
                    repeat=repeat.substring(0,repeat.length()-4);
                    if(repeat.contains("小时")){
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
    public JSONObject selectTasks() {
        JSONObject jsonObject=new JSONObject();
        //获取任务类型数量
        List<Map<String,Object>> mapList=taskDao.selectType();
        if(mapList!=null&&mapList.size()>0){
            for(int i=0;i<mapList.size();i++){
                JSONArray array=taskDao.selectTasks("",(String)mapList.get(i).get("type"));
                //计算上次执行时间，下次执行时间
                array=calDate(array);
                jsonObject.put((String)mapList.get(i).get("type"),array);
            }
        }
        return jsonObject;
    }

    public JSONArray calDate(JSONArray array){
        if(array!=null &&array.size()>0){
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            long now=new Date().getTime();
            String repeat;
            String dateType;
            for (Object o : array) {
                JSONObject jsonObject = (JSONObject) o;
                //启用和禁用改为布尔类型
                jsonObject.put("isstart", "true".equals(jsonObject.getString("isstart")));
                jsonObject.put("isdisable", "true".equals(jsonObject.getString("isdisable")));
                //利用现在时间与开始时间差对重复间隔取商，比较大小计算
                //获取重复间隔
                String repeatNew = jsonObject.getString("planFre");
                repeatNew = repeatNew.substring(2);
                repeatNew = repeatNew.substring(0, repeatNew.length() - 4);
                if (repeatNew.contains("小时") ) {
                    repeat = repeatNew.substring(0, repeatNew.length() - 2);
                    dateType = "小时";
                } else {
                    repeat = repeatNew.substring(0, repeatNew.length() - 1);
                    dateType = repeatNew.substring(repeatNew.length() - 1);
                }
                try {
                    if ("分，小时，天".contains(dateType)) {
                        long repeatNum = 0;//重复间隔毫秒数
                        if ("分".equals(dateType)) {
                            repeatNum = (Integer.parseInt(repeat)) * 1000 * 60;
                        } else if ("小时".equals(dateType)) {
                            repeatNum = (Integer.parseInt(repeat)) * 1000 * 60 * 60;
                        } else if ("天".equals(dateType)) {
                            repeatNum = (Integer.parseInt(repeat)) * 1000 * 60 * 60 * 24;
                        }
                        //计算取商
                        long startDate = sdf.parse(jsonObject.getString("beginTime")).getTime();
                        long difference = (now - startDate) / repeatNum;

                        calendar.setTimeInMillis(startDate + difference * repeatNum);
                        jsonObject.put("lastTime", sdf.format(calendar.getTime()));
                        calendar.setTimeInMillis(startDate + (difference + 1) * repeatNum);
                        jsonObject.put("nextTime", sdf.format(calendar.getTime()));
                    } else {
                        if ("月".equals(dateType)) {
                            calendar.setTimeInMillis(now);
                            Calendar calStart = Calendar.getInstance();
                            calStart.setTime(sdf.parse((String) jsonObject.get("beginTime")));
                            int difference = ((calendar.get(Calendar.MONTH) - calStart.get(Calendar.MONTH)) +
                                    (calendar.get(Calendar.YEAR) - calStart.get(Calendar.YEAR)) * 12) /
                                    Integer.parseInt(repeat);
                            int year = difference / 12;
                            int month = difference % 12;
                            calStart.add(Calendar.YEAR, year);
                            calStart.add(Calendar.MONTH, month);
                            jsonObject.put("lastTime", sdf.format(calStart.getTime()));
                            calStart.add(Calendar.MONTH, Integer.parseInt(repeat));
                            jsonObject.put("nextTime", sdf.format(calStart.getTime()));
                        } else if ("年".equals(dateType)) {
                            Calendar calStart = Calendar.getInstance();
                            calStart.setTime(sdf.parse((String) jsonObject.get("beginTime")));
                            int difference = (calendar.get(Calendar.YEAR) - calStart.get(Calendar.YEAR)) /
                                    Integer.parseInt(repeat);
                            calStart.add(Calendar.YEAR, difference * (Integer.parseInt(repeat)));
                            jsonObject.put("lastTime", sdf.format(calStart.getTime()));
                            calStart.add(Calendar.YEAR, Integer.parseInt(repeat));
                            jsonObject.put("nextTime", sdf.format(calStart.getTime()));
                        }
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        }
        return array;
    }

}
