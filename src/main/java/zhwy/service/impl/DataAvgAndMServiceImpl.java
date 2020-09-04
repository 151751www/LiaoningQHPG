package zhwy.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zhwy.dao.DataAvgAndMDao;
import zhwy.datatable.DataColumn;
import zhwy.datatable.DataTable;
import zhwy.entity.ChongxianqiResult;
import zhwy.entity.ReturnMessage;
import zhwy.service.ChongXianQIService;
import zhwy.service.DataAvgAndMService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Resource
public class DataAvgAndMServiceImpl implements DataAvgAndMService {
    private static Logger logger = LoggerFactory.getLogger(DataAvgAndMServiceImpl.class);
    @Autowired
    private DataAvgAndMDao dataAvgAndMDao;


    public String getDataForAvg (String stationType,String dataType,String  key,String value, String StartTime,String EndTime, String city,String stationNUm,String suanfaType,String cnty){
       String message="";
        try {
            message=dataAvgAndMDao.DataForAvg(stationType,dataType,key,value,StartTime,EndTime,city,stationNUm,suanfaType,cnty);
        }catch (Exception e){
            logger.error("DataAvgAndMService  文件中查询要素数据错误 ："+e);
            message="DataAvgAndMService  文件中查询要素数据错误 ";
        }
        return  message;
    }
    public String getStationQUShi(String stationType,String dataType, String obsveName, String startTime, String endTime, String stationNum,String city,String cnty){
        String message="";
        String []key={"",""};
        try {
            message=dataAvgAndMDao.getquahi( stationType, dataType,  obsveName,  startTime,  endTime,  stationNum, city, cnty);
        }catch (Exception e){
            logger.error("DataAvgAndMService  文件中查询趋势单站数据错误 ："+e);
            message="DataAvgAndMService  文件中查询趋势单站数据错误 ";
        }
        return  message;
    }
    public String getABR(JSONArray array,String type){
        String message="";
        double[] date = new double[3];
        JSONArray resuarray=new JSONArray();
        JSONObject object;
        String [] key={"k", "b", "R" };

        try {
            if(type.equals("1")){
                date=dataAvgAndMDao.GetFileNiHeCanShuAB(array,"time","data");
            }else if(type.equals("2")){
                date=dataAvgAndMDao.GetFileNiHeCanShuAB(array,"stanum","data");
            }
            for (int i=0;i<date.length;i++){
                object=new JSONObject();
                object.put(key[i],date[i]);
                resuarray.add(object);
            }
            message=resuarray.toString();
        }catch (Exception e){
            logger.error("DataAvgAndMService-----getStationQUShi查询趋势分析中相关系数计算错误 ："+e);
            message="DataAvgAndMService-----getStationQUShi中查询趋势单站数据错误 ";
        }
        return  message;
    }

    public String  ChongxianqiCanshu(String obsv,String num,String startYear,String endYear){
        ChongxianqiResult jsonResult=new ChongxianqiResult();
        List<Map<String,Object>>datelist=dataAvgAndMDao.getChongXianDate(obsv,num,startYear,endYear);
        List<Double> listValue=new ArrayList<Double>();
        if (datelist.size()>0)
        {
            for (int i = 0; i < datelist.size(); i++)
            {
                double b=Double.parseDouble(datelist.get(i).get(obsv).toString());
                listValue.add(b);
            }
        }
        double[] arrShuju=new double[listValue.size()] ;
        for (int i=0;i<listValue.size();i++){
            arrShuju[i]=listValue.get(i);
        }

         ChongXianQIService getResult = new ChongXianQIService();
        jsonResult.listShuJu = listValue;
        jsonResult.dtCanShu = getResult.GetCanShuPNew(arrShuju);

        double avgX = Double.parseDouble((jsonResult.dtCanShu.getRows().get(0).get("avgx")).toString());
        double Cv = Double.parseDouble((jsonResult.dtCanShu.getRows().get(0).get("Cv")).toString());
        double Cs_Cv = Double.parseDouble((jsonResult.dtCanShu.getRows().get(0).get("Cs_Cv")).toString());
        double Cs = Double.parseDouble((jsonResult.dtCanShu.getRows().get(0).get("Cs")).toString());


        jsonResult.dtP3DottedLine = getResult.GetP3LineNew(avgX, Cv, Cs_Cv);
        jsonResult.dtP3BlackPoint = getResult.GetP3BlackPointNew(avgX, Cv, Cs_Cv, arrShuju);
        String json = JSONObject.toJSONString(jsonResult);
        return  json;
    }

    /**
     *
     * @param rateStr X年一遇  逗号分隔
     * @param avgXC 修改的均值
     * @param cvC  修改后的CV
     * @param cs_CvC 修改后的 cs_Cv
     * @param shuju1 数据库查询到的数据
     * @return
     */
    public String ChongxianqiChonghui(String rateStr,String avgXC,String cvC,String cs_CvC,String[] shuju1){
        ChongxianqiResult jsonResult=new ChongxianqiResult();

        double[] shuju = new double[shuju1.length];
        for (int i = 0; i < shuju1.length; i++)
        {
            shuju[i] = Double.parseDouble(shuju1[i]);
        }

        ChongXianQIService getResult = new ChongXianQIService();
        jsonResult.dtCanShu = getResult.GetCanShuPNew(shuju);


        double avgX = Double.parseDouble((jsonResult.dtCanShu.getRows().get(0).get("avgx")).toString());
        double Cv = Double.parseDouble((jsonResult.dtCanShu.getRows().get(0).get("Cv")).toString());
        double Cs_Cv = Double.parseDouble((jsonResult.dtCanShu.getRows().get(0).get("Cs_Cv")).toString());
        double Cs = Double.parseDouble((jsonResult.dtCanShu.getRows().get(0).get("Cs")).toString());



        jsonResult.dtP3DottedLine = getResult.GetP3LineNew(avgX, Cv, Cs_Cv);
        jsonResult.dtP3BlackPoint = getResult.GetP3BlackPointNew(avgX, Cv, Cs_Cv, shuju);



        double avgXChange = Double.parseDouble(avgXC);
        double cvChange = Double.parseDouble(cvC);
        double cs_CvChange = Double.parseDouble(cs_CvC);


        jsonResult.dtP3FullLine = getResult.GetP3LineNew(avgXChange, cvChange, cs_CvChange);

        String[] arrRateStr = rateStr.split(",");
        double[] arrFrequency = new double[arrRateStr.length];
        for (int i = 0; i < arrRateStr.length; i++)
        {
            arrFrequency[i] = 1.0 / Double.parseDouble(arrRateStr[i]);
        }

        jsonResult.dtP3Result.setRows(getResult.GetP3TableResultNew(avgXChange, cvChange, cs_CvChange, arrFrequency));
        jsonResult.dtP3Result.setTotal(jsonResult.dtP3Result.getRows().getRows().getCount());

        DataTable columnName = new DataTable();
        columnName.getColumns().Add("field", String.class);
        columnName.getColumns().Add("title", String.class);
        for (DataColumn col:jsonResult.dtP3Result.getRows().getColumns()) {
            columnName.getRows().Add(new String[]{col.getColumnName(),col.getCaption()});
        }

        jsonResult.dtP3ResultColumn = columnName;
        String json = JSONObject.toJSONString(jsonResult);
        return json;

    }
}
