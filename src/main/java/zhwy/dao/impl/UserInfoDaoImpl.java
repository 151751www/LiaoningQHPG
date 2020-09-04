package zhwy.dao.impl;


import com.alibaba.fastjson.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import zhwy.dao.StationDao;
import zhwy.dao.UserInfoDao;
import zhwy.util.GeneralDaoImpl;

import java.util.Arrays;
import java.util.Map;

@Component
public class UserInfoDaoImpl implements UserInfoDao {
    private static Logger logger = LoggerFactory.getLogger(UserInfoDaoImpl.class);
    @Autowired
    private GeneralDaoImpl generalDao;
    public String  addUser(String userName,String passWord,String relName,String mobile,String phone) throws Exception {
        String message="新增用户失败";
        String sql="";
        Map<String,Object> map=generalDao.getDataMap("select count(1) as num from user_info where user_name='"+userName+"'");
        if(map.size()>0){
            if((int)map.get("num")==0){
                sql="INSERT INTO user_info  (user_id,user_name,user_password,user_real_name,user_mobile,user_phone,register_date)VALUES(NEWID(),?,?,?,?,?,getdate())";
                Object [] arr={userName,passWord,relName,mobile,phone};
                try {
                    int num=generalDao.updateSql(sql,arr);
                    if(num>0){
                        message="新增用户成功";
                    }
                }catch (Exception e){
                    logger.error("UserInfoDaoImpl--addUser--遇到问题:" + e);
                    e.printStackTrace();
                }
            }else{
                message="新增用户失败,该用户名已存在";
            }
        }
        return message;
    }

    public String  updateUser(String userName,String passWord,String relName,String mobile,String phone) throws Exception {
        String message="修改用户失败";
        String  sql="";
        Map<String,Object> map=generalDao.getDataMap("select user_name from user_info where user_name='"+userName+"'");
        if(map.size()>0){
            sql="update user_info set  user_password=?,user_real_name=?,user_mobile=?,user_phone=? where user_name= ? ";
            Object [] updatearr={passWord,relName,mobile,phone,userName};
            try {
                int num=generalDao.updateSql(sql,updatearr);
                if(num>0){
                    message="修改用户成功";
                }
            }catch (Exception e){
                logger.error("UserInfoDaoImpl--updateUser--遇到问题:" + e);
                e.printStackTrace();
            }

        }else{
            message="修改用户失败,该用户名不存在";
        }
        return message;
    }
    public String selectUser(String userName,String[] arr)throws Exception{
        String message="";
        StringBuilder sql=new StringBuilder();
        if(Arrays.asList(arr).contains("密码")){
            sql.append("select user_name as 用户名 ,user_real_name as 姓名,user_mobile as 手机,user_phone 电话,user_password as 密码 from user_info ");
        }else{
            sql.append("select user_name as 用户名 ,user_real_name as 姓名,user_mobile as 手机,user_phone 电话 from user_info ");
        }
        if(userName!=null&&!userName.equals("")){
            sql.append(" where user_name='"+userName+"'");
        }
        JSONArray result=generalDao.getDataBySql(sql.toString(),arr);
        message=result.toString();
        return message;
    }

    public String  delUser(String userName) throws Exception {
        String message="删除用户失败";
        StringBuilder sql=new StringBuilder();

        sql.append("delete from user_info where user_name= ?");
        Object [] updatearr={userName};
        try {
            int num=generalDao.updateSql(sql.toString(),updatearr);
            if(num>0){
                message="删除用户成功";
            }else if(num==0){
                message="该用户不存在";
            }
        }catch (Exception e){
            logger.error("UserInfoDaoImpl--delUser--遇到问题:" + e);
            e.printStackTrace();
        }
        return message;
    }

}
