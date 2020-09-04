package zhwy.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zhwy.dao.StationDao;
import zhwy.dao.UserInfoDao;
import zhwy.service.StationService;
import zhwy.service.UserInfoService;

import javax.annotation.Resource;

@Service
@Resource
public class UserInfoServiceImpl implements UserInfoService {
    private static Logger logger = LoggerFactory.getLogger(UserInfoServiceImpl.class);
    @Autowired
    public UserInfoDao userInfoDao;


    @Override
    public String addUser(String userName, String passWord, String relName, String mobile, String phone) throws Exception {
        return userInfoDao.addUser(userName,passWord,relName,mobile,phone);
    }
    @Override
    public String updateUser(String userName, String passWord, String relName, String mobile, String phone) throws Exception {
        return userInfoDao.updateUser(userName,passWord,relName,mobile,phone);
    }

    @Override
    public String selectUser(String userName,String[] arr) throws Exception {
        return userInfoDao.selectUser(userName,arr);
    }

    @Override
    public String delUser(String userName) throws Exception {
        return userInfoDao.delUser(userName);
    }

}
