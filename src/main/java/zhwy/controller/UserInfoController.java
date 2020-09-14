package zhwy.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
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
import zhwy.service.UserInfoService;
import zhwy.util.Common;

@Api(position = 9,tags = "辽宁气候评估----用户信息")
@RestController
@SessionAttributes
@RequestMapping("/UserInfo")
public class UserInfoController {
    private static Logger logger = LoggerFactory.getLogger(UserInfoController.class);
    @Autowired
    private Common common;
    @Autowired
    private UserInfoService userInfoService;

    @ApiOperation(value = "用户新增")
    @PostMapping("/addUser")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userName", value = "用户名", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "passWord", value = "密码", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "relName", value = "姓名", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "mobile", value = "手机号码", required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "phone", value = "固定电话（区号-电话号）", required = false, paramType = "query", dataType = "String")
    })
    public String addUser(String userName,String passWord,String relName,String mobile,String phone) {

        String message = "";
        //跨域
        common.getCrossOrigin();
        try {
            if (userName == null || userName.equals("")) {
                message = "用户名不能为空！";
            }else if(passWord==null ||passWord.equals("")){
                message ="密码不能为空";
            } else if(relName==null||relName.equals("")){
               message="姓名不能为空";
            }else if(mobile!=null&&!mobile.equals("")&&!mobile.matches("^1[3|4|5|7|8][0-9]\\d{4,8}$")) {
                message="手机号码格式不正确";
            }else if(phone!=null&&!phone.equals("")&&!phone.matches("^[0][1-9]{2,3}-[0-9]{5,10}$")){
                message="固定电话格式不正确，区号-电话号";
            }else{
                message=userInfoService.addUser(userName,passWord,relName,mobile,phone);
            }

            return message;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("UserInfoController ---addUser  新增用户失败：" + e);
            return "新增用户失败：" + e;
        }
    }

    @ApiOperation(value="用户修改")
    @PostMapping("/updateUser")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userName", value = "用户名", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "passWord", value = "密码", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "relName", value = "姓名", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "mobile", value = "手机号码", required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "phone", value = "固定电话（区号-电话号）", required = false, paramType = "query", dataType = "String")
    })
    public String updateUser(String userName,String passWord,String relName,String mobile,String phone){
        String message = "";
        //跨域
        common.getCrossOrigin();
        try {
            if (userName == null || userName.equals("")) {
                message = "用户名不能为空！";
            }else if(passWord==null ||passWord.equals("")){
                message ="密码不能为空";
            } else if(relName==null||relName.equals("")){
                message="姓名不能为空";
            }else if(mobile!=null&&!mobile.equals("")&&!mobile.matches("^1[3|4|5|7|8][0-9]\\d{4,8}$")) {
                message="手机号码格式不正确";
            }else if(phone!=null&&!phone.equals("")&&!phone.matches("^[0][1-9]{2,3}-[0-9]{5,10}$")){
                message="固定电话格式不正确，区号-电话号";
            }else{
                message=userInfoService.updateUser(userName,passWord,relName,mobile,phone);
            }

            return message;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("UserInfoController ---updateUser  修改用户失败：" + e);
            return "修改用户失败：" + e;
        }
    }

    @ApiOperation(value = "用户查询")
    @PostMapping("/selectUser")
    @ApiImplicitParams({
            @ApiImplicitParam(name="userName",value = "用户名",required = false,paramType = "query",dataType ="String")
    })
    public String selectUser(String userName){
        String message="";
        common.getCrossOrigin();
        try {
            String[] arr={"用户名","姓名","手机","电话"};
            message=userInfoService.selectUser(userName,arr);
        }catch (Exception e){
            e.printStackTrace();
            logger.error("UserInfoController ---selectUser  查询用户失败：" + e);
            return "查询用户失败：" + e;
        }
        return message;
    }

    @ApiOperation(value = "用户删除")
    @PostMapping("/delUser")
    @ApiImplicitParams({
            @ApiImplicitParam(name="userName",value = "用户名",required = true,paramType = "query",dataType = "String")
    })
    public String delUser(String userName){
        String message="";
        common.getCrossOrigin();
        try {
            if(userName!=null||!userName.equals("")){
                message=userInfoService.delUser(userName);
            }else{
                message="用户名不能为空";
            }

        }catch (Exception e){
            e.printStackTrace();
            logger.error("UserInfoController ---selectUser  删除用户失败：" + e);
            return "删除用户失败：" + e;
        }
        return message;
    }

    @ApiOperation(value = "用户登录")
    @PostMapping("/loginIn")
    @ApiImplicitParams({
            @ApiImplicitParam(name="userName",value = "用户名",required = true,paramType = "query",dataType = "String"),
            @ApiImplicitParam(name="password",value = "密码",required = true,paramType = "query",dataType = "String")
    })
    public String loginIn(String userName,String password){
        String message="";
        common.getCrossOrigin();
        try {
            if(userName==null||userName.equals("")){
                message="用户名不能为空";
            }else if(password==null||password.equals("")){
                message="密码不能为空";
            }
            if(message.equals("")){
                String[] arr={"密码"};
                String user=userInfoService.selectUser(userName,arr);
                JSONArray  userJson=JSONArray.parseArray(user);
                if(userJson.size()>0){
                    JSONObject object=userJson.getJSONObject(0);
                    String pwd=object.getString("密码");
                    if(pwd.equals(password)){
                        message="用户名，密码验证成功";
                    }else{
                        message="密码错误";
                    }
                }else{
                    message="该用户不存在";
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            logger.error("UserInfoController ---loginIn  删除用户失败：" + e);
            return "登录失败：" + e;
        }
        return message;
    }
}
