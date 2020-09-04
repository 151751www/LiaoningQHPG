package zhwy.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import zhwy.dao.DataMethodDao;
import zhwy.service.DataMethodService;
import zhwy.service.MethodManager;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Resource
public class DataMethodServiceImpl implements DataMethodService {
    private static Logger logger = LoggerFactory.getLogger(StationServiceImpl.class);
    @Autowired
    public DataMethodDao dataMethodDao;
    public List<Map<String,Object>> getXulieYanchangData(String timeType, String beginTime, String endTime, String stationNum, String obsvName) throws Exception {
        return dataMethodDao.getXulieYanchangData(timeType,beginTime,endTime,stationNum,obsvName);
    }

    public  List<Map<String,Object>> getYanchangDataResult(List<Map<String,Object>> dtData)throws Exception
    {
        List<Map<String,Object>> dtResult = null;
        //try {
            double[] arrLongValue = new double[dtData.size()];
            ArrayList<Double> listValue = new ArrayList<Double>();
            for (int i = 0; i < dtData.size(); i++)
            {
                double longxulie= Double.parseDouble((String)dtData.get(i).get("长序列")) ;
                arrLongValue[i] = longxulie;
                if (dtData.get(i).get("短序列")!=null&&!String.valueOf(dtData.get(i).get("短序列")).equals(""))
                {
                    listValue.add(Double.parseDouble((String)dtData.get(i).get("短序列")));
                }
            }
            Object[] arrShortValue = listValue.toArray();

            int arrLength = arrShortValue.length;
            double[] arrX = new double[arrLength];
            double[] arrY = new double[arrLength];
            for (int i = 0; i < arrLength; i++)
            {
                arrX[i] = arrLongValue[i];
                arrY[i] = (double) arrShortValue[i];
            }
            double avgX = 0;
            double avgY = 0;
            double avgXY = 0;
            double avgXX = 0;
            double[] XY = new double[arrLength];
            double[] XX = new double[arrLength];
            for (int i = 0; i < arrLength; i++)
            {
                avgX += arrX[i];
                avgY += arrY[i];
                XY[i] = arrX[i] * arrY[i];
                XX[i] = arrX[i] * arrX[i];
            }
            avgX = avgX / arrLength;
            avgY = avgY / arrLength;
            for (int i = 0; i < arrLength; i++)
            {
                avgXY += XY[i];
                avgXX += XX[i];
            }
            avgXY = avgXY / arrLength;
            avgXX = avgXX / arrLength;

            double k = (avgX * avgY - avgXY) / (avgX * avgX - avgXX);//y=kx+a中的b
            double a = avgY - (k * avgX);//a

             dtResult = dtData;
            for (int i = 0; i < dtResult.size(); i++)
            {
                if (dtData.get(i).get("长序列")!=null&&!String.valueOf(dtData.get(i).get("长序列")).equals("")){
                    if(dtData.get(i).get("短序列")==null){
                        dtResult.get(i).put("短序列","-");
                    }
                    Double duanXuLie=(Double.parseDouble((String) dtResult.get(i).get("长序列"))) * k + a;
                    BigDecimal b = new BigDecimal(duanXuLie);
                    dtResult.get(i).put("短序列更正值",b.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue());
                }
            }
       /* }catch (Exception e){
            logger.error("DataMethodServiceImpl----- getYanchangDataResult 短序列订正延长报错"+e);
            e.printStackTrace();
        }*/
        return dtResult;
    }

    @Override
    public List<Map<String, Object>> getHeBingDataResult(List<Map<String, Object>> LData, List<Map<String, Object>> SData,String timeType) throws ParseException {
        if(LData.size()<SData.size()){
            return null;
        }
        String sim="";
        if(timeType.equals("时")){
            sim="yyyy-MM-dd HH";
        }else if(timeType.equals("日")){
            sim="yyyy-MM-dd";
        }else if(timeType.equals("年")){
            sim="yyyy";
        }
        SimpleDateFormat sdf=new SimpleDateFormat(sim);
        Date Sstart=sdf.parse((SData.get(0).get("时间")).toString());
        Date Send=sdf.parse((SData.get(SData.size()-1).get("时间")).toString());
        Date Lstart=sdf.parse((LData.get(0).get("时间")).toString());
        Date Lend=sdf.parse((LData.get(SData.size()-1).get("时间")).toString());

        if(Sstart.after(Lstart)&&Send.after(Lend)){
            for (int i=0;i<LData.size();i++){
                Object duanxulie=null;
                for (int s=0;s<SData.size();s++){
                    if(LData.get(i).get("时间").equals(SData.get(s).get("时间"))){
                        duanxulie=SData.get(s).get("短序列");
                        break;
                    }
                }
                LData.get(i).put("短序列",duanxulie);
            }
        }
        return LData;
    }

    @Override
    public String getFileContent(MultipartFile multipartFile,String type,String dataType) {
        MethodManager methodManager=new MethodManager();
        String result="";
        try {
            result=methodManager.getFileContent(multipartFile,type,dataType);
        }catch (Exception e){
            logger.error("DataMethodServiceImpl----- getFileContent解析文件报错"+e);
            e.printStackTrace();
        }
        return result;
    }


}
