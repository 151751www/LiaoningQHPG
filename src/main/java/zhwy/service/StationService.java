package zhwy.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zhwy.controller.StationController;
import zhwy.dao.impl.StationDaoImpl;

import javax.annotation.Resource;

@Service
@Resource
public class StationService {
    private static Logger logger = LoggerFactory.getLogger(StationService.class);
    @Autowired
    public StationDaoImpl stationDao;

    public  String  getCity(String  stationType)  {
        String result="";
        try {
            result=stationDao.getCity(stationType);
        }catch (Exception e){
            logger.error("市名查询失败"+e.getMessage());
            result="市名查询失败"+e.getMessage();
        }
    return result;
    }

    public String getCounty(String stationType, String city) {
        String result="";
        try {
            result=stationDao.getCounty(stationType,city);
        }catch (Exception e){
            logger.error("县名查询失败"+e.getMessage());
            result="县名查询失败"+e.getMessage();
        }
        return result;

    }
}
