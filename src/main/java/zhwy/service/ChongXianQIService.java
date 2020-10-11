package zhwy.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComThread;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Resource
public class ChongXianQIService {
    private static Logger logger = LoggerFactory.getLogger(ChongXianQIService.class);


    String [] obsv={"气温","气压","水汽压","相对湿度","能见度","降水量","蒸发","积雪深度","风速","温度","地温","地面温度","界值","雪压"};
    String [] danwei={"℃","hPa","hPa","%","m","mm","mm","cm","m/s","℃","℃","℃","cm","kg/m²"};
    //耿贝尔
    public String   getChongXianQiForgumbel(List<Map<String,Object>> listmap,String obsvName) throws  Exception{
        JSONObject jsonObjectxp;
        JSONObject jsonObjectTemp;
        JSONObject jsonTable;
        JSONArray arrayxp=new JSONArray();
        JSONArray arraytemp=new JSONArray();
        JSONArray arrayTable=new JSONArray();
        double[] li =new double[listmap.size()];
        double[] M=new double[listmap.size()];
        for (int i=0;i<listmap.size();i++){
            li[i]=Double.parseDouble(listmap.get(i).get("年值").toString());
            M[i]=i+1;
        }
        double[] pm = new double[99];
        double pmx=0.00;
        double[] x_xp=new double[99];
        double[] y_xp=new double[99];
        double []x_year=new double[99];
        BigDecimal bg;
        for (int i=0;i<99;i++){
            pmx=pmx+0.01;
            pm[i]=pmx;
            bg= new BigDecimal(pmx);
            x_xp[i]=-Math.log(-Math.log(pmx));
            y_xp[i]=bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            //x_year[i]= (int) (1/y_xp[i]);
            x_year[i]= (1/(1-y_xp[i]));
            bg=new BigDecimal(x_year[i]);
            x_year[i]=bg.setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();
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
            bg= new BigDecimal(y_temp[i]);
            y_temp[i]=bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();//保留两位小数
        }

        double ave_x=GetAvgValue(temp);
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
            bg= new BigDecimal(xp[i]);
            xp[i]=bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();//保留两位小数
        }
        jsonTable=new JSONObject(true);
        for (int i=0;i<xp.length;i++){
            jsonObjectxp=new JSONObject(true);
           // jsonObjectxp.put("x_xp",x_xp[i]);
            arrayxp.add(i,jsonObjectxp);
            bg= new BigDecimal(xp[i]);
            xp[i]=bg.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();//保留1位小数
            if(y_xp[i]==0.01||y_xp[i]==0.5||y_xp[i]==0.67||y_xp[i]==0.8||y_xp[i]==0.9||y_xp[i]==0.95||y_xp[i]==0.97||y_xp[i]==0.98||y_xp[i]==0.99){

                if(y_xp[i]==0.97){
                    jsonTable.put("30",xp[i]);
                }else if(y_xp[i]==0.67){
                    jsonTable.put("3",xp[i]);
                }else {
                    jsonTable.put(String.valueOf((int)x_year[i]),xp[i]);
                }
            }
            jsonObjectxp.put("y_xp",y_xp[i]);
            jsonObjectxp.put("xp",xp[i]);
        }
        arrayTable.add(jsonTable);
        for (int i=0;i<temp.length;i++){
            jsonObjectTemp=new JSONObject(true);
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
        jsonObject.put("xData",y_xp);
        jsonObject.put("直线图",arrayxp);
        jsonObject.put("散点图",arraytemp);
        jsonObject.put("表格数据",arrayTable);
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

    public String getXpForP3(List<Map<String,Object>> listmap,String obsvName){
        JSONObject jsonObjectxp;
        JSONObject jsonXpTable;
        JSONObject jsonObjectTemp;
        JSONArray arrayxp=new JSONArray();
        JSONArray arraytemp=new JSONArray();
        JSONArray arrayTable=new JSONArray();
        double[] li =new double[listmap.size()];
        for (int i=0;i<listmap.size();i++){
            li[i]=Double.parseDouble(listmap.get(i).get("年值").toString());
        }
        Map<String,Double> map=GetCanShuPNew(li);
        double a=GetValue_a(map.get("Cs"));
        double b=GetValue_b(map.get("avgx"),map.get("Cs"),map.get("Cv"));
        double a0=GetValue_a0(map.get("avgx"),map.get("Cs"),map.get("Cv"));
        double[] pm = new double[99];
        double pmx=0.00;
        double[] y_xp=new double[99];
        int []x_year=new int[99];
        BigDecimal bg;
        for (int i=0;i<99;i++){
            pmx=pmx+0.01;
            pm[i]=pmx;
            bg= new BigDecimal(pmx);
            y_xp[i]=bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            x_year[i]= (int) (1/y_xp[i]);
        }
        double[] x=li;
        double[]temp=BubbleSort(x,"desc");
        double[] y_temp=new double[temp.length];
        for (int i=0;i<temp.length;i++){
            double countY=temp.length+1;
            y_temp[i]=(i+1)/countY;
            bg= new BigDecimal(y_temp[i]);
            y_temp[i]=bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();//保留2位小数
        }
        double[] xp=getGammainv(pm,a,b,a0);
        jsonXpTable=new JSONObject(true);
        for (int i=0;i<xp.length;i++){
            jsonObjectxp=new JSONObject(true);
            arrayxp.add(i,jsonObjectxp);
            bg= new BigDecimal(xp[i]);
            xp[i]=bg.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();//保留1位小数
            //0  1 2  4 9  19  32  49  98
            if(i==0||i==1||i==2||i==4||i==9||i==19||i==32||i==49||i==98){
                if(i==2){
                    jsonXpTable.put(""+30,xp[i]);
                }else if(i==32){
                    jsonXpTable.put(""+3,xp[i]);
                }else {
                    jsonXpTable.put(""+x_year[i],xp[i]);
                }
            }
            jsonObjectxp.put("y_xp",y_xp[i]);
            jsonObjectxp.put("xp",xp[i]);
        }
        arrayTable.add(jsonXpTable);
        for (int i=0;i<temp.length;i++){
            jsonObjectTemp=new JSONObject(true);
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
        jsonObject.put("xData",y_xp);
        jsonObject.put("直线图",arrayxp);
        jsonObject.put("散点图",arraytemp);
        jsonObject.put("表格数据",arrayTable);
        return  jsonObject.toJSONString();
    }

    public double [] getGammainv(double[] X,double a,double b,double a0){
        ComThread.InitSTA();
        ActiveXComponent xl = new ActiveXComponent("Excel.Application");
        double [] array=new double[X.length];
        try {
            xl.setProperty("Visible", new Variant(true));//设置程序可见
            Object workbooks = xl.getProperty("Workbooks").toDispatch();
            Object workbook = Dispatch.get((Dispatch) workbooks,"Add").toDispatch();
            Object sheet = Dispatch.get((Dispatch) workbook,"ActiveSheet").toDispatch();
            for(int i=0;i<X.length;i++){
                Object a2 = Dispatch.invoke((Dispatch) sheet, "Range", Dispatch.Get,
                        new Object[] {"A2"},
                        new int[1]).toDispatch();
                Dispatch.put((Dispatch) a2, "Formula", "=GAMMAINV(1-"+X[i]+","+a+",1/"+b+")+"+a0);
                array[i]=Double.parseDouble(String.valueOf(Dispatch.get((Dispatch) a2, "Value")));
            }
            Variant f = new Variant(false);
            Dispatch.call((Dispatch) workbook, "Close", f);
        } catch (Exception e) {
            logger.error("gammainv函数获取xp出错  getGammainv"+e.getMessage());
            e.printStackTrace();
        } finally {
            xl.invoke("Quit", new Variant[] {});
            ComThread.Release();
        }
        return array;
    }

}
