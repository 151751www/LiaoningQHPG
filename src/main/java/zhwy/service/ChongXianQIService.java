package zhwy.service;

import zhwy.datatable.DataRow;
import zhwy.datatable.DataTable;
import org.apache.commons.math3.special.Gamma;


public class ChongXianQIService {

    public ChongXianQIService()
    {
    }


    public double[] _arrValue = { };
    public boolean _isHigh0 = true;

    //根据均值、Cv、Cs/Cv计算相应的虚线或实线图的对应关系
    public  DataTable GetP3LineNew(double avgX, double Cv, double Cs_Cv)
    {
        double Cs = Cs_Cv * Cv;
        double a = GetValue_a(Cs);
        double b = GetValue_b(avgX, Cs, Cv);
        double a0 = GetValue_a0(avgX, Cs, Cv);

        DataTable dtP3XuXian = new  DataTable();
        dtP3XuXian.getColumns().Add("a%", Double.class);
        dtP3XuXian.getColumns().Add("Value", Double.class);
        double[] arrP = { 0.01, 0.02, 0.03, 0.04, 0.05, 0.06, 0.07, 0.08, 0.09, 0.10, 0.20, 0.30, 0.40, 0.50, 0.60, 0.70, 0.80, 0.90, 1.00, 2.00, 3.00, 4.00, 5.00, 6.00, 7.00, 8.00, 9.00, 10.00, 11.00, 12.00, 13.00, 14.00, 15.00, 16.00, 17.00, 18.00, 19.00, 20.00, 22.00, 24.00, 26.00, 28.00, 30.00, 32.00, 34.00, 36.00, 38.00, 40.00, 42.00, 44.00, 46.00, 48.00, 50.00, 52.00, 54.00, 56.00, 58.00, 60.00, 62.00, 64.00, 66.00, 68.00, 70.00, 72.00, 74.00, 76.00, 78.00, 80.00, 81.00, 82.00, 83.00, 84.00, 85.00, 86.00, 87.00, 88.00, 89.00, 90.00, 91.00, 92.00, 93.00, 94.00, 95.00, 96.00, 97.00, 98.00, 99.00, 99.10, 99.20, 99.30, 99.40, 99.50, 99.60, 99.70, 99.80, 99.90, 99.91, 99.92, 99.93, 99.94, 99.95, 99.96, 99.97, 99.98, 99.99, };
        double[] arrXp3 = new double[arrP.length];
        //Application excel = new Application();



        for (int i = 0; i < arrP.length; i++)
        {
            /*double tp = GammaInv(1.0 - arrP[i] / 100, a, 1);
            double Xp = tp / b + a0;
            arrXp3[i] = Xp;*/
            arrXp3[i]=getGammaInv(arrP[i],a,b,a0);
        }

        for (int i = 0; i < arrP.length; i++)
        {
            DataRow dr = dtP3XuXian.NewRow();
            dr.set("a%", arrP[i]);

            if (!_isHigh0)
            {
                dr.set("Value", String.format("%.4f", (-1 * arrXp3[i])));
            }
            else
            {
                dr.set("Value", String.format("%.4f", arrXp3[i]));
            }
            dtP3XuXian.getRows().Add(dr);
        }

        return dtP3XuXian;
    }

    //根据数据计算而得的X平均值、Cv、Cs/Cv画出黑实点
    public  DataTable GetP3BlackPointNew(double avgX, double Cv, double Cs_Cv, double[] arrValue)
    {
        double Cs = Cs_Cv * Cv;
        double a = GetValue_a(Cs);
        double b = GetValue_b(avgX, Cs, Cv);
        double a0 = GetValue_a0(avgX, Cs, Cv);

        DataTable dtShiDian = new  DataTable();

        dtShiDian.getColumns().Add("Value", Double.class);
        dtShiDian.getColumns().Add("p", Double.class);
       /* Application excel = new Application();
        double[] arrP_value = new double[arrValue.length];
        double[] p_value = new double[arrValue.length];
        for (int i = 0; i < arrValue.length; i++)
        {
            double tp = (arrValue[i] - a0) * b;
            arrP_value[i] =excel.WorksheetFunction.GammaDist(tp, a, 1, true);
        }*/
        for (int i = 0; i < arrValue.length; i++)
        {
            DataRow dr = dtShiDian.NewRow();
            dr.set("Value", arrValue[i]);
            //dr.set("p", (1 - arrP_value[i]) * 100);
            dtShiDian.getRows().Add(dr);
        }
        return dtShiDian;
    }


    // P3算法

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
                if (String.valueOf(arrValue[i])!=null)
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
                if (String.valueOf(arrValue[i])!=null)
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
                if (String.valueOf(arrValue[i])!=null)
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

    public  DataTable GetCanShuPNew(double[] arrValue)
    {
        DataTable dtCanShu = new  DataTable();
        if (arrValue != null && arrValue.length > 0)
        {
            double avgx = GetAvgValue(arrValue);
            double Cv = GetCv(arrValue);
            double Cs = GetCs(arrValue);

            dtCanShu.getColumns().Add("avgx", Double.class);
            dtCanShu.getColumns().Add("Cv", Double.class);
            dtCanShu.getColumns().Add("Cs_Cv", Double.class);
            dtCanShu.getColumns().Add("Cs", Double.class);

            DataRow dr = dtCanShu.NewRow();
            dr.set("avgx", String.format("%.3f", avgx));
            dr.set("Cv", String.format("%.3f", Cv));
            dr.set("Cs_Cv", String.format("%.3f", Cs / Cv));
            dr.set("Cs", String.format("%.3f", Cs));
            dtCanShu.getRows().Add(dr);
        }
        return dtCanShu;
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



    /// <summary>
    /// 根据输入信息计算结果
    /// </summary>
    /// <param name="avgX">数组的平均值</param>
    /// <param name="Cv">变差系数</param>
    /// <param name="Cs_Cv">偏态系数/变差系数</param>
    /// <param name="arrFrequency">概率 %</param>
    /// <returns>不同重现期对应的值</returns>
    public  DataTable GetP3TableResultNew(double avgX, double Cv, double Cs_Cv, double[] arrFrequency)
    {
        double Cs = Cs_Cv * Cv;
        double a = GetValue_a(Cs);
        double b = GetValue_b(avgX, Cs, Cv);
        double a0 = GetValue_a0(avgX, Cs, Cv);

        DataTable dtResult = new  DataTable();
        dtResult.getColumns().Add("均值", Double.class);
        dtResult.getColumns().Add("Cv", Double.class);
        dtResult.getColumns().Add("Cs/Cv", Double.class);
        for (int i = 0; i < arrFrequency.length; i++)
        {
            String colName = String.format("%.2f", 1.0/arrFrequency[i]);
            if (colName.contains(".00"))
                colName = colName.substring(0, colName.length() - 3);
            dtResult.getColumns().Add(colName, Double.class);
        }

        DataRow dr = dtResult.NewRow();
        dr.set("均值", avgX);
        dr.set("Cv", Cv);
        dr.set("Cs/Cv", Cs_Cv);

        //Application excel = new Application();
        for (int i = 0; i < arrFrequency.length; i++)
        {
            //double tp = excel.WorksheetFunction.GammaInv(1 - arrFrequency[i] / 100.0, a, 1);
            //double Xp = tp / b + a0;
            double Xp=getGammaInv(arrFrequency[i],a,b,a0);
            dr.set(i + 3, String.format("%.2f", Xp));
        }
        //excel.Quit();// 关闭Excel进程
        //excel = null; //确保Excel进程关闭
        //// 安全回收进程
        ////System.GC.GetGeneration(excel);
        dtResult.getRows().Add(dr);
        return dtResult;
    }


    // 耿贝尔分布

    /// <summary>
    /// 计算参数a,u
    /// </summary>
    /// <param name="arrValue"></param>
    /// <param name="a"></param>
    /// <param name="u"></param>
    /// <returns></returns>
    public double[] boolGetCanShuG(double[] arrValue)
    {
        boolean success = false;
        double a = 0;
        double u = 0;
        if (arrValue != null && arrValue.length > 0)
        {
            int dataCount = arrValue.length;

            // 先求经验分布函数
            arrValue=BubbleSort(arrValue, "asc"); //升序排列
            double[] fx = new double[dataCount];
            for (int i = 0; i < dataCount; i++)
            {
                fx[i] = ((i + 1) / (dataCount + 1));
            }

            // 序列变换
            double[] y = new double[dataCount];
            for (int i = 0; i < dataCount; i++)
            {
                double yy = -1.0 * Math.log(fx[i]);
                y[i] = -1.0 * Math.log(yy);
            }


            // 参数计算a、u
            //均方差
            double junfangChaX = Math.sqrt(GetFangCha(arrValue));
            double junfangChaY = Math.sqrt(GetFangCha(y));
            //均值
            double avgx = GetAvgValue(arrValue);
            double avgy = GetAvgValue(y);

            a = junfangChaY / junfangChaX;
            u = avgx - avgy / a;
            return new double[]{a,u};
        }
        return null;
    }

    /// <summary>
    /// 计算参数Cs、Cv、平均值
    /// </summary>
    /// <param name="arrValue"></param>
    /// <returns></returns>
    public  DataTable GetCanShuGNew(double[] arrValue)
    {
        DataTable dtCanShu = new  DataTable();
        if (arrValue != null && arrValue.length > 0)
        {
            int dataCount = arrValue.length;

            // 先求经验分布函数
            arrValue=BubbleSort(arrValue, "asc"); //升序排列
            double[] fx = new double[dataCount];
            for (int i = 0; i < dataCount; i++)
            {
                fx[i] = ((i + 1) /(dataCount + 1));
            }

            // 序列变换
            double[] y = new double[dataCount];
            for (int i = 0; i < dataCount; i++)
            {
                double yy = -1.0 * Math.log(fx[i]);
                y[i] = -1.0 * Math.log(yy);
            }


            // 参数计算a、u
            //均方差
            double junfangChaX = Math.sqrt(GetFangCha(arrValue));
            double junfangChaY = Math.sqrt(GetFangCha(y));
            //均值
            double avgx = GetAvgValue(arrValue);
            double avgy = GetAvgValue(y);

            double a = junfangChaY / junfangChaX;
            double u = avgx - avgy / a;

            double Cs = 1.14;
            double Cv = 2.0 / (avgx * a * Cs);

            dtCanShu.getColumns().Add("avgx", Double.class);
            dtCanShu.getColumns().Add("Cv", Double.class);
            dtCanShu.getColumns().Add("Cs_Cv", Double.class);
            dtCanShu.getColumns().Add("Cs", Double.class);
            dtCanShu.getColumns().Add("a", Double.class);
            dtCanShu.getColumns().Add("u", Double.class);

            DataRow dr = dtCanShu.NewRow();
            dr.set("avgx", String.format("%.3f", avgx));
            dr.set("Cv", String.format("%.3f", Cv));
            dr.set("Cs_Cv", String.format("%.3f", Cs / Cv));
            dr.set("Cs", String.format("%.3f", Cs));
            dr.set("a", String.format("%.3f", a));
            dr.set("u", String.format("%.3f", u));
            dtCanShu.getRows().Add(dr);
        }
        return dtCanShu;
    }

    public  DataTable GetCanShuGNew2(double[] arrValue)
    {
        DataTable dtCanShu = new  DataTable();
        if (arrValue != null && arrValue.length > 0)
        {
            double a;
            double u;
            double[]au=boolGetCanShuG(arrValue);
            if (au!=null)
            {
                a=au[0];
                u=au[1];
                //均值
                double avgx = GetAvgValue(arrValue);

                double Cs = 1.14;
                double Cv = 2.0 / (avgx * u * Cs);

                dtCanShu.getColumns().Add("avgx", Double.class);
                dtCanShu.getColumns().Add("Cv", Double.class);
                dtCanShu.getColumns().Add("Cs_Cv", Double.class);
                dtCanShu.getColumns().Add("Cs", Double.class);
                dtCanShu.getColumns().Add("a", Double.class);
                dtCanShu.getColumns().Add("u", Double.class);

                DataRow dr = dtCanShu.NewRow();
                dr.set("avgx", String.format("%.3f", avgx));
                dr.set("Cv", String.format("%.3f", Cv));
                dr.set("Cs_Cv", String.format("%.3f", Cs / Cv));
                dr.set("Cs", String.format("%.3f", Cs));
                dr.set("a", String.format("%.3f", a));
                dr.set("u", String.format("%.3f", u));
                dtCanShu.getRows().Add(dr);
            }
        }
        return dtCanShu;
    }

    /// <summary>
    ///
    /// </summary>
    /// <param name="t">代表重现期</param>
    /// <param name="arrValue"></param>
    /// <returns>预测的值</returns>
    public double GetXt(int t, double[] arrValue)
    {
        double Xt = 0;
        if (arrValue != null && arrValue.length > 0)
        {
            double a;
            double u;
            if (boolGetCanShuG(arrValue)!=null)
            {
                a=boolGetCanShuG(arrValue)[0];
                u=boolGetCanShuG(arrValue)[1];
                Xt = u - 1.0 / a * (Math.log(-1.0 * Math.log(1 - 1.0 / t)));
            }
        }
        return Xt;
    }

    public double GetXt(double p, double[] arrValue)
    {
        double Xt = 0;
        if (arrValue != null && arrValue.length > 0)
        {
            double a;
            double u;
            if (boolGetCanShuG(arrValue)!=null)
            {
                a=boolGetCanShuG(arrValue)[0];
                u=boolGetCanShuG(arrValue)[1];
                Xt = u - 1.0 / a * (Math.log(-1.0 * Math.log(1 - p)));
            }
        }
        return Xt;
    }

    public  DataTable GetGTableResultNew(double[] arrValue, double[] arrFrequency)
    {
        DataTable dtResult = new  DataTable();

        //参数表
        DataTable dtCanShu = GetCanShuGNew(arrValue);
        if (dtCanShu != null && dtCanShu.getRows().getCount() > 0)
        {
            //  取出参数
            double avgx = Double.parseDouble(dtCanShu.getRows().get(0).get("avgx").toString());
            double Cv = Double.parseDouble(dtCanShu.getRows().get(0).get("Cv").toString());
            double Cs_Cv = Double.parseDouble(dtCanShu.getRows().get(0).get("Cs_Cv").toString());
            double a = Double.parseDouble(dtCanShu.getRows().get(0).get("a").toString());
            double u = Double.parseDouble(dtCanShu.getRows().get(0).get("u").toString());


            dtResult.getColumns().Add("均值", Double.class);
            dtResult.getColumns().Add("Cv", Double.class);
            dtResult.getColumns().Add("Cs/Cv", Double.class);
            for (int i = 0; i < arrFrequency.length; i++)
            {
                dtResult.getColumns().Add(String.valueOf(arrFrequency[i]), Double.class);
            }

            DataRow dr = dtResult.NewRow();
            dr.set("均值", avgx);
            dr.set("Cv", Cv);
            dr.set("Cs/Cv", Cs_Cv);
            for (int i = 0; i < arrFrequency.length; i++)
            {
                double Xt = u - 1.0 / a * (Math.log(-1.0 * Math.log(1 - arrFrequency[i] / 100.0)));
                dr.set(i + 3, String.format("%.2f", Xt));
            }
            dtResult.getRows().Add(dr);
        }
        return dtResult;
    }


    public  DataTable GetDataByArray(double[] arrData)
    {
        DataTable dt = new DataTable();
        dt.getColumns().Add("数据");
        dt.getColumns().Add("概率");
        //Array.Sort(arrData);
        //Array.Reverse(arrData);
        //for (int i = 1; i <= arrData.length; i++)
        //{
        //    double gaiLv = Convert.ToDouble(i) / Convert.ToDouble(arrData.length + 1);
        //    DataRow row = dt.NewRow();
        //    row["概率"] = gaiLv;
        //    row["数据"] = arrData[i - 1];
        //    dt.Rows.Add(row);
        //}
        return dt;
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

    public  double getGammaInv(double x,double a,double b,double a0){
        double y=(Math.pow(b,a)/Sencondderivative(a))*Math.pow(x-a0,a-1)*(1/Math.pow(Math.E,b*(x-a0)));
        return  y;
    }
    public  double Firstderivative(double x) {
        double Firstgamm=Gamma.gamma(x)*Gamma.digamma(x);
        return Firstgamm;
    }
    public  double Sencondderivative(double x){
        double Sencondgamm=(Math.pow(Firstderivative(x)/Gamma.gamma(x),2)+Gamma.trigamma(x))*Gamma.gamma(x);
        return Sencondgamm;
    }

}
