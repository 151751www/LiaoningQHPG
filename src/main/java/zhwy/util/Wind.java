package zhwy.util;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class Wind {
	
    private static String[] _arrWindDirChinese16 = new String[] { "北", "北东北", "东北", "东东北", "东", "东东南", "东南", "南东南", "南", "南西南", "西南", "西西南", "西", "西西北", "西北", "北西北" };
    private static String[] _arrWindDirEnglish16 = new String[] { "N", "NNE", "NE", "ENE", "E", "ESE", "SE", "SSE", "S", "SSW", "SW", "WSW", "W", "WNW", "NW", "NNW" };

    private static String[] _arrWindDirChinese8 = new String[] { "北", "东北", "东", "东南", "南", "西南", "西", "西北" };
    private static String[] _arrWindDirEnglish8 = new String[] { "N", "NE", "E", "SE", "S", "SW", "W", "NW" };

    private static String[] _arrWindDirChinese4 = new String[] { "北", "东", "南", "西" };
    private static String[] _arrWindDirEnglish4 = new String[] { "N", "E", "S", "W" };

    /**
     * 获取风向方位，包含静风。
     * @param windDirCount 方位数量，赋值4、8、16。
     * @param isChinese 为汉字输出、false为英文输出。
     * @return 风向方位数组。
     */
    public  String[] GetWindDirList(int windDirCount, boolean isChinese)
    {
    	String[] arrItem = null;
        if (isChinese)
        {
            switch (windDirCount)
            {
                case 4: arrItem = _arrWindDirChinese4; break;
                case 8: arrItem = _arrWindDirChinese8; break;
                case 16: arrItem = _arrWindDirChinese16; break;
                default:
                    break;
            }
        }
        else
        {
            switch (windDirCount)
            {
                case 4: arrItem = _arrWindDirEnglish4; break;
                case 8: arrItem = _arrWindDirEnglish8; break;
                case 16: arrItem = _arrWindDirEnglish16; break;
                default:
                    break;
            }
        }

        String[] arrResult = null;
        if (arrItem != null)
        {
        	String ppc = isChinese ? "静风" : "PPC";
            arrResult = new String[arrItem.length + 1];

            for (int i = 0; i < arrItem.length; i++)
            {
                arrResult[i] = arrItem[i];
            }

            arrResult[arrResult.length - 1] = ppc;
        }

        return arrResult;
    }
    /**
     * 各风向累年平均频率，用于风向玫瑰图显示。例如传入单站点多年观测值，方法内部计算每年风频情况，再进行多年平均。
     * 如需计算时段内的值，而非时段内累年平均，可将arrTime参数处理成同一年，再传入该方法。
     * @param windDirCount 风向数量，赋值4、8、16。
     * @param arrTime 时间数组。
     * @param arrWindSpeed 风向数组。
     * @param arrWindDir 小数位数。气象规范上要求保留到整数，即该参数为0。有特殊要求可酌情设置小数位数。
     * @param decimalDigit 各风向累年平均频率。
     * @return
     */
    public Map<String, String> GetWindDirRate(int windDirCount, String[] arrTime, double[] arrWindSpeed, double[] arrWindDir, int decimalDigit)
    {
    	//小数位数
        int maxTemp = Integer.parseInt(arrTime[0].substring(0,4));
        int minTemp = Integer.parseInt(arrTime[0].substring(0,4));
        for (int i = 0; i < arrTime.length; i++) {
			int Year = Integer.parseInt(arrTime[i].substring(0,4));
			if(Year>maxTemp){
				maxTemp = Year;
			}if(minTemp>Year){
				minTemp = Year;
			}
		}
        int beginYear = minTemp;
        int endYear = maxTemp;
        int yearCount = endYear - beginYear + 1;
        String[] arrWindDirItem = GetWindDirList(windDirCount, false);
        Map<String,Double> htEveryYearData = new HashMap<String,Double>();
        List<Map<String, Double>> listYearData = new ArrayList<Map<String, Double>>();
        for (int year = beginYear; year <= endYear; year++){
            for(String windDir:arrWindDirItem){
                if (htEveryYearData.containsKey(windDir)){
                	htEveryYearData.put(windDir, 0.0);
                }
                else{
                    htEveryYearData.put(windDir,0.0);
                }
            }

            int allCount = 0;
            for (int i = 0; i < arrTime.length; i++)
            {
                if (Integer.parseInt(arrTime[i].substring(0,4)) == year)
                {
                	//判断是否为静风    判断方位
                	String windDir = ConvertWindDirNumberToText(arrWindSpeed[i], arrWindDir[i], windDirCount, false);
                    allCount++;
                    if (!htEveryYearData.containsKey(windDir))
                    {
                        htEveryYearData.put(windDir,1.0);
                    }
                    else
                    {//double转为int   四舍五入？
                        htEveryYearData.put(windDir,(htEveryYearData.get(windDir)) + 1.0);
                    }
                }
            }

            for(String windDir:arrWindDirItem){
                htEveryYearData.put(windDir,Double.parseDouble(String.format("%."+decimalDigit+"f",(htEveryYearData.get(windDir) / allCount * 100))));
            }
            listYearData.add(htEveryYearData);
        }
        //把泛型里的数据循环累加，求平均频率放入字典中。
        Map<String, String> dicResult = new HashMap<String, String>();
        for(String windDir:arrWindDirItem)
        {
            double sumValue = 0;
            for(Map<String,Double> ht:listYearData){
                sumValue += ht.get(windDir);
            }
            String rate = String.format("%."+decimalDigit+"f",(sumValue / yearCount));
            dicResult.put(windDir, rate);
        }
        return dicResult;
    }
    
    /**
     * 风向角度转换为方位。
     * @param windSpeed 风速
     * @param windDir 角度
     * @param windDirCount 方位，支持4、8、16方位
     * @param isChinese true为汉字，false为字母
     * @return 方位字符
     */
    public  String ConvertWindDirNumberToText(double windSpeed, double windDir, int windDirCount, boolean isChinese)
    {
        /*
       1  北 N 0 348.76 ~ 11.25
       2  北东北 NNE 22.5 11.26 ~ 33.75
       3  东北 NE 45 33.76 ~ 56.25
       4  东东北 ENE 67.5 56.26 ~ 78.75
       5  东 E 90 78.76 ~ 101.25
       6  东东南 ESE 112.5 101.26 ~ 123.75
       7  东南 SE 135 123.76 ~ 146.25
       8  南东南 SSE 157.5 146.26 ~ 168.75
       9  南 S 180 168.76 ~ 191.25
       10 南西南 SSW 202.5 191.26 ~ 213.75
       11 西南 SW 225 213.76 ~ 236.25
       12 西西南 WSW 247.5 236.26 ~ 258.75
       13 西 W 270 258.76 ~ 281.25
       14 西西北 WNW 292.5 281.26 ~ 303.75
       15 西北 NW 315 303.76 ~ 326.25
       16 北西北 NNW 337.5 326.26 ~ 348.75
       17 静风 C
       八方位
       51 北 N 0 337.6 ~ 22.5
       52 东北 NE 45 22.6 ~ 67.5
       53 东 E 90 67.6 ~ 112.5
       54 东南 SE 135 112.6 ~ 157.5
       55 南 S 180 157.6 ~ 202.5
       56 西南 SW 225 202.6 ~ 247.5
       57 西 W 270 247.6 ~ 292.5
       58 西北 NW 315 292.6 ~ 337.5
       59 静风 C
        */

        String result = "";
        if (windSpeed <= 0.2)
        {
            result = isChinese ? "静风" : "PPC";
            return result;
        }

        double num = 11.25 * (16 / windDirCount);

        if (windDir > 360 - num || windDir <= num)
        {
            result = isChinese ? "北" : "N";
        }
        else
        {
            String[] arrWindDirChinese = null;
            String[] arrWindDirWord = null;
            switch (windDirCount)
            {
                case 16:
                    arrWindDirChinese = _arrWindDirChinese16;
                    arrWindDirWord = _arrWindDirEnglish16;
                    break;
                case 8:
                    arrWindDirChinese = _arrWindDirChinese8;
                    arrWindDirWord = _arrWindDirEnglish8;
                    break;
                case 4:
                    arrWindDirChinese = _arrWindDirChinese4;
                    arrWindDirWord = _arrWindDirEnglish4;
                    break;
                default:
                    break;
            }

            double beginValue = -num;

            //北方位提前判断，所以从1开始循环
            for (int i = 1; i < arrWindDirChinese.length; i++)
            {
                beginValue += num * 2;
                if (windDir > beginValue && windDir <= beginValue + num * 2)
                {
                    result = isChinese ? arrWindDirChinese[i] : arrWindDirWord[i];
                    break;
                }
            }
        }
        return result;
    }
}
