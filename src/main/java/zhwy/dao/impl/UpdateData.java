package zhwy.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import zhwy.util.GeneralDaoImpl;

@Component
public class UpdateData {
    @Autowired
    private GeneralDaoImpl generalDao;

    /**
     * 根据小时数据计算月表中月累计平均降水量
     * @return
     */
    public String updatePerMonthTable(){
        String result="";
        //计算每月的累计值
        String sql="";

        return  result;
    }
}
