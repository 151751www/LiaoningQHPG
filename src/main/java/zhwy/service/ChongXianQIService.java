package zhwy.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;


import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Resource
public class ChongXianQIService {


    String [] obsv={"气温","气压","水汽压","相对湿度","能见度","降水量","蒸发","积雪深度","风速","温度","地温","地面温度","界值","雪压"};
    String [] danwei={"℃","hPa","hPa","%","m","mm","mm","cm","m/s","℃","℃","℃","cm","kg/m²"};
    //耿贝尔
    public String   getChongXianQiForgumbel(List<Map<String,Object>> listmap,String obsvName) throws  Exception{
        JSONObject jsonObjectxp;
        JSONObject jsonObjectTemp;
        JSONArray arrayxp=new JSONArray();
        JSONArray arraytemp=new JSONArray();
        double[] li =new double[listmap.size()];
        double[] M=new double[listmap.size()];
        double sumLi=0.00;
        for (int i=0;i<listmap.size();i++){
            li[i]=Double.parseDouble(listmap.get(i).get("年值").toString());
            M[i]=i+1;
            sumLi+=Double.parseDouble(listmap.get(i).get("年值").toString());
        }
        double[] pm = new double[99];
        double pmx=0.00;
        double[] x_xp=new double[99];
        double[] y_xp=new double[99];
        int []x_year=new int[99];
        BigDecimal bg;
        for (int i=0;i<99;i++){
            pmx=pmx+0.01;
            pm[i]=pmx;
            bg= new BigDecimal(pmx);
            x_xp[i]=-Math.log(-Math.log(pmx));
            y_xp[i]=bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            x_year[i]= (int) (1/pmx);
        }
        double[] x=li;
        double[]temp=BubbleSort(x,"asc");
        int N=li.length;
        double[] x_temp=new double[M.length];

        double[] y_temp=new double[M.length];
        double y[]=new double[M.length];
        for (int i=0;i<M.length;i++){
            x_temp[i]=-Math.log(-Math.log((M[i]/(N+1))));
            y[i]=-Math.log(-Math.log(1-M[i]/(N+1)));
            y_temp[i]=M[i]/(N+1);
        }

        double ave_x=sumLi/x.length;
        double dVar=0;
        for(int i=0;i<x.length;i++){//求方差
                dVar+=(x[i]-ave_x)*(x[i]-ave_x);
             }
        double s_x=Math.sqrt(dVar/x.length);
        double ysum=0.00;
        for(int i=0;i<y.length;i++){
            ysum+=y[i];
        }
        double ave_y=ysum/y.length;
        double y_dVar=0;
        for(int i=0;i<y.length;i++){//求方差
            y_dVar+=(y[i]-ave_y)*(y[i]-ave_y);
        }
        double s_y=Math.sqrt(y_dVar/y.length);
        double a=s_x/s_y;
        double u=ave_x-s_x/s_y*ave_y;
        double[] xp=new double[pm.length];
        for (int i=0; i<pm.length;i++){
            xp[i]=u-a*Math.log(-Math.log(pm[i]));
            //xp[i]=u-a*Math.log(-Math.log(1-pm[i]));
            bg= new BigDecimal(xp[i]);
            xp[i]=bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();//保留两位小数
        }
        for (int i=0;i<xp.length;i++){
            jsonObjectxp=new JSONObject(true);
           // jsonObjectxp.put("x_xp",x_xp[i]);
            jsonObjectxp.put("y_xp",y_xp[i]);
            jsonObjectxp.put("xp",xp[i]);
            jsonObjectxp.put("year",x_year[i]);
            arrayxp.add(i,jsonObjectxp);
        }
        for (int i=0;i<temp.length;i++){
            jsonObjectTemp=new JSONObject(true);
            //jsonObjectTemp.put("x_temp",x_temp[i]);
            jsonObjectTemp.put("y_temp",y_temp[i]);
            jsonObjectTemp.put("temp",temp[i]);
            arraytemp.add(i,jsonObjectTemp);
        }
        String odanwei="";
        for ( int i=0;i<obsv.length;i++){
            if(obsvName.indexOf(obsv[i])!=-1){
                odanwei=danwei[i];
                continue;
            }
        }
        JSONObject  jsonObject=new JSONObject(true);
        jsonObject.put("单位",odanwei);
        jsonObject.put("直线图",arrayxp);
        jsonObject.put("散点图",arraytemp);
     return  jsonObject.toJSONString();

    }



    public double[] BubbleSort(double[] array,String order){

        for(int i=0;i<array.length-1;i++){
            for(int j=0;j<array.length-1-i;j++){
                if(order.equals("asc")){
                    if(array[j]>array[j+1]){
                        double temp=array[j];
                        array[j]=array[j+1];
                        array[j+1]=temp;
                    }
                }else if(order.equals("desc")){
                    if(array[j]<array[j+1]){
                        double temp=array[j];
                        array[j]=array[j+1];
                        array[j+1]=temp;
                    }
                }
            }
        }

        return  array;
    }

    /// <summary>
    /// 求数组的平均值
    /// </summary>
    /// <param name="arrValue"></param>
    /// <returns></returns>
    public double GetAvgValue(double[] arrValue)
    {
        double sum = 0;
        double num = 0;
        double avg = 0;
        if (arrValue != null && arrValue.length > 0)
        {
            for (int i = 0; i < arrValue.length; i++)
            {
                if (null!=String.valueOf(arrValue[i]))
                {
                    sum = sum + arrValue[i];
                    num = num + 1;
                }
            }
            if (num > 0)
            {
                avg = sum * 1.0 / num;
            }
        }
        return avg;
    }

    /// <summary>
    /// 求数组的方差
    /// </summary>
    /// <param name="arrValue"></param>
    /// <returns></returns>
    public double GetFangCha(double[] arrValue)
    {
        double sum = 0;
        double num = 0;
        double fangCha = 0;
        if (arrValue != null && arrValue.length > 0)
        {
            double avgValue = GetAvgValue(arrValue);
            for (int i = 0; i < arrValue.length; i++)
            {
                if (null!=String.valueOf(arrValue[i]))
                {
                    sum = sum + Math.pow((arrValue[i] - avgValue), 2);
                    num = num + 1;
                }
            }
            if (num > 0)
            {
                fangCha = sum * 1.0 / num;
            }
        }
        return fangCha;
    }

    /// <summary>
    /// 变差系数
    /// </summary>
    /// <param name="arrValue"></param>
    /// <returns></returns>
    public double GetCv(double[] arrValue)
    {
        double CvValue = 0;
        if (arrValue != null && arrValue.length > 0)
        {
            double avgValue = GetAvgValue(arrValue);
            double fangCha = GetFangCha(arrValue);
            CvValue = 1.0 * Math.pow(fangCha, 0.5) / avgValue;
        }
        return CvValue;
    }

    /// <summary>
    /// 偏态系数
    /// </summary>
    /// <param name="arrValue"></param>
    /// <returns></returns>
    public double GetCs(double[] arrValue)
    {
        double sum = 0;
        double num = 0;
        double CsValue = 0;
        if (arrValue != null && arrValue.length > 0)
        {
            double avgValue = GetAvgValue(arrValue);
            double fangCha = GetFangCha(arrValue);
            for (int i = 0; i < arrValue.length; i++)
            {
                if (null!=String.valueOf(arrValue[i]))
                {
                    sum = sum + Math.pow((arrValue[i] - avgValue), 3);
                    num = num + 1;
                }
            }
            if (num > 0)
            {
                CsValue = 1.0 * sum / (num * Math.pow(fangCha, 1.5));
            }
        }
        return CsValue;
    }

    public Map<String,Double> GetCanShuPNew(double[] arrValue)
    {
        Map<String, Double> map=new HashMap<>();
        if (arrValue != null && arrValue.length > 0)
        {
            double avgx = GetAvgValue(arrValue);
            double Cv = GetCv(arrValue);
            double Cs = GetCs(arrValue);
            double Cs_Cv=(Cs / Cv);
            BigDecimal  bgavgx= new BigDecimal(avgx);
            BigDecimal  bgCv= new BigDecimal(Cv);
            BigDecimal  bgCs_Cv= new BigDecimal(Cs_Cv);
            BigDecimal  bgCs= new BigDecimal(Cs);
            map.put("avgx",bgavgx.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue());
            map.put("Cv",bgCv.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue());
            map.put("Cs_Cv",bgCs_Cv.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue());
            map.put("Cs",bgCs.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue());
        }
        return map;
    }

    /// <summary>
    /// a
    /// </summary>
    /// <param name="Cs">偏态系数</param>
    /// <returns></returns>
    public double GetValue_a(double Cs)
    {
        double a = 4.0 / Math.pow(Cs, 2);
        return a;
    }

    /// <summary>
    /// b
    /// </summary>
    /// <param name="avgValue">数组的平均值</param>
    /// <param name="Cs">偏态系数</param>
    /// <param name="Cv">变差系数</param>
    /// <returns></returns>
    public double GetValue_b(double avgValue, double Cs, double Cv)
    {
        double b = 2.0 / (avgValue * Cv * Cs);
        return b;
    }

    /// <summary>
    /// a0
    /// </summary>
    /// <param name="arrValue"></param>
    /// <returns></returns>
    public double GetValue_a0(double avgValue, double Cs, double Cv)
    {
        double a0 = avgValue * (1 - 2.0 * Cv / Cs);
        return a0;
    }


}
