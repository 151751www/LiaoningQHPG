package zhwy.dao.impl;

import com.alibaba.fastjson.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import zhwy.dao.TaskDao;
import zhwy.entity.Task;
import zhwy.util.GeneralDaoImpl;

import java.util.*;

@Component
public class TaskDaoImpl implements TaskDao {

    @Autowired
    private GeneralDaoImpl generalDao;

    @Override
    public JSONArray selectTasks(String taskName) {
        JSONArray array=new JSONArray();
        String name="";
        if(!"".equals(taskName)){
            StringBuffer sb=new StringBuffer("(");
            String [] taskArr=taskName.split(",");
            for(int i=0;i<taskArr.length;i++){
                if(i!=0){
                    sb.append(",");
                }
                sb.append("'").append(taskArr[i]).append("'");
            }
            sb.append(")");
            name=" and name in "+sb.toString();
        }

        String sql="select name,state,CONVERT (VARCHAR(100), lastTime, 20) AS lastTime," +
                "CONVERT (VARCHAR(100), nextTime, 20) AS nextTime ,planFre,\n" +
                "CONVERT (VARCHAR(100), beginTime, 20) AS beginTime,CONVERT (VARCHAR(100), stopTime, 20) AS stopTime," +
                "CONVERT (VARCHAR(100), dataTime, 20) AS dataTime,isstart,isdisable  from task_info where 1=1  "+name;

        String [] keys={"name","state","lastTime","nextTime","planFre","beginTime","stopTime","dataTime","isstart",
                "isdisable"};

        try {
            array=generalDao.getDataBySql(sql,keys);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return array;
    }

    @Override
    public String addTask(Task task) {
        String result="";
        try {
            //先查询任务计划是否已存在
            String sql="select name from task_info where name='"+task.getName()+"'";
            Map<String,Object> map=generalDao.getDataMap(sql);
            if(map!=null&&map.size()>0){
                result="已存在该任务计划！";
            }else{
                Object [] arr={task.getName(),task.getState(),task.getLastTime(),task.getNextTime(),task.getPlanFre(),
                        task.getBeginTime(),task.getStopTime(),task.getDataTime(),task.getExec(),task.getIsstart(),
                        task.getIsdisable()};
                sql="insert into task_info (name,state,lastTime,nextTime,planFre,beginTime,stopTime,dataTime,execScript," +
                        "isstart,isdisable) " +
                        "values (?,?,?,?,?,?,?,?,?,?,?)";
                int num=generalDao.updateSql(sql,arr);
                if(num>0){
                    result="OK";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public String deleteTask(String name) {
        String result="";
        String sql="delete from task_info where name=?";
        Object [] arr={name};
        try{
            int num=generalDao.updateSql(sql,arr);
            if(num>0){
                result="OK";
            }
        }catch (Exception e){
            e.printStackTrace();
            return "NO";
        }
        return result;
    }

    @Override
    public String updateTask(Task task ,String name) {
        String result="";
        //将Task属性存入list,如果属性为null,不修改
        List<Object> list=new ArrayList<Object>();
        list.add(task.getName());
        list.add(task.getState());
        list.add(task.getLastTime());
        list.add(task.getNextTime());
        list.add(task.getPlanFre());
        list.add(task.getBeginTime());
        list.add(task.getStopTime());
        list.add(task.getDataTime());
        list.add(task.getExec());
        list.add(task.getIsstart());
        list.add(task.getIsdisable());
        list.add(name);
        String [] arrStr={"name","state","lastTime","nextTime","planFre","beginTime","stopTime","dataTime","execScript",
                "isstart","isdisable"};
        StringBuffer sb=new StringBuffer("update task_info set ");
        int j=0;
        for(int i=0;i<arrStr.length;i++){
            if(list.get(j)!=null && !"".equals(list.get(j))){
                if(j!=0){
                    sb.append(",");
                }
                sb.append(arrStr[i]).append("=?");
                j++;
            }else{
                list.remove(j);
            }
        }
        sb.delete(sb.toString().length()-1,sb.toString().length()-1);
        sb.append(" where name=? ");
        try{
            int num=generalDao.updateSql(sb.toString(),list.toArray());
            if(num>0){
                result="OK";
            }
        }catch (Exception e){
            e.printStackTrace();
            return "NO";
        }
        return result;
    }

    @Override
    public String selectTasksExe(String name) {
        String exec="";
        String sql="select execScript from task_info where name='"+name+"'";
        try {
            Map<String,Object> map=generalDao.getDataMap(sql);
            if(map!=null&&map.size()>0){
                exec=(String)map.get("execScript");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return exec;
    }
}
