package zhwy;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import zhwy.util.Common;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Map;
import org.apache.commons.math3.special.Gamma;


public class Json {

   // public static void main(String[] args) {

       /* String fullPath = null;
        //例如：fullPath="D:/myroot/test.json"

        // 生成json格式文件
        try {
            // 保证创建一个新文件
            File file = new File("E:\\json文件测试\\Obsvelement");
            if (!file.getParentFile().exists()) { // 如果父目录不存在，创建父目录
                file.getParentFile().mkdirs();
            }
            if (file.exists()) { // 如果已存在,删除旧文件
                file.delete();
            }
            file.createNewFile();

            String [] hourObsv={"气压","水汽压","气温","相对湿度","露点温度","云","能见度","降水量","蒸发量","风","地温","草面温度","冻土","雪深"};
            String [] hourKey={"prs","vap","tem","rhu","dpt","clo","vis","pre","evp","win","gst","lgst","frs","snow"};
            String [] dayObsv={"气压","水汽压","气温","相对湿度","云","能见度","降水量","蒸发量","风","地温","草面温度","冻土","雪深","日照"};
            String [] dayKey={"prs","vap","tem","rhu","clo","vis","pre","evp","win","gst","lgst","frs","snow","ssh"};
            String [] monthObsv={"气压","水汽压","气温","相对湿度","云","能见度","降水量","蒸发量","风","地温","草面温度","冻土","雪深","日照"};
            String [] monthKey={"prs","vap","tem","rhu","clo","vis","pre","evp","win","gst","lgst","frs","snow","ssh"};
            String [] yearObsv={"气压","水汽压","气温","相对湿度","云","降水量","蒸发量","风","地温","冻土","雪深","日照"};
            String [] yearKey={"prs","vap","tem","rhu","clo","pre","evp","win","gst","frs","snow","ssh"};
            Common common=new Common();
            Map<String,String> obsvMap=null;
            JSONObject rootH = new JSONObject();
            JSONObject rootD = new JSONObject();
            JSONObject rootM = new JSONObject();
            JSONObject rootY = new JSONObject();
            JSONObject root1 = new JSONObject();
            String [][]arrObsv={hourObsv,dayObsv,monthObsv,yearObsv};
            String [][]arrkey={hourKey,dayKey,monthKey,yearKey};
            for (int j=0;j<arrObsv.length;j++){
                String[] obsv=arrObsv[j];
                String [] zrrkey=arrkey[j];
                if(j==0){
                    obsvMap=common.itemH;
                }else if(j==1){
                    obsvMap=common.itemD;
                }else if(j==2){
                    obsvMap=common.itemM;
                }else if(j==3){
                    obsvMap=common.itemY;
                }
                for (int i=0;i<obsv.length;i++){
                    JSONArray array = new JSONArray();
                    String obsvKey=zrrkey[i];
                    for (Map.Entry<String,String>entry:obsvMap.entrySet()){
                        String key=entry.getKey();
                        if(key.startsWith(obsvKey)){
                            JSONObject  jsonObject=new JSONObject();
                            jsonObject.put("val",entry.getKey());
                            jsonObject.put("name",entry.getValue());
                            array.add(jsonObject);
                        }
                    }
                    if(j==0){
                        rootH.put(obsv[i],array);
                    }else if(j==1){
                        rootD.put(obsv[i],array);
                    }else if(j==2){
                        rootM.put(obsv[i],array);
                    }else if(j==3){
                        rootY.put(obsv[i],array);
                    }
                }
            }


            root1.put("时",rootH);
            root1.put("日",rootD);
            root1.put("月",rootM);
            root1.put("年",rootY);

            // 格式化json字符串
            String jsonString = formatJson(root1.toString());

            // 将格式化后的字符串写入文件
            Writer write = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
            write.write(jsonString);
            write.flush();
            write.close();
        } catch (Exception e) {
            e.printStackTrace();
        }*/
   // }
    public static void main(String[] args) {
        //直接利用这个可以求gamma函数的一阶偏导数
        double cc= Gamma.digamma(1);
        double a = Math.pow(Math.PI,2)/6.0+Math.pow(cc,2);
        System.out.println(a);
        System.out.println(Sencondderivative(1));
        //getGammaInv();
    }
    public static double Firstderivative(double x) {
        double Firstgamm=Gamma.gamma(x)*Gamma.digamma(x);
        return Firstgamm;
    }
    public static double Sencondderivative(double x){
        double Sencondgamm=(Math.pow(Firstderivative(x)/Gamma.gamma(x),2)+Gamma.trigamma(x))*Gamma.gamma(x);
        return Sencondgamm;
    }

    public static  double getGammaInv(double x,double avgx,double cv,double cs){
        double  a=4/Math.pow(cs,2);
        double b=2/(avgx*cv*cs);
        double a0=avgx*(1-((2*cv)/cs));
        double y=(Math.pow(b,a)/Sencondderivative(a))*Math.pow(x-a0,a-1)*(1/Math.pow(Math.E,b*(x-a0)));
       return  y;
    }
    /**
     * 返回格式化JSON字符串。
     *
     * @param json 未格式化的JSON字符串。
     * @return 格式化的JSON字符串。
     */
    public static String formatJson(String json) {
        StringBuffer result = new StringBuffer();

        int length = json.length();
        int number = 0;
        char key = 0;

        // 遍历输入字符串。
        for (int i = 0; i < length; i++) {
            // 1、获取当前字符。
            key = json.charAt(i);

            // 2、如果当前字符是前方括号、前花括号做如下处理：
            if ((key == '[') || (key == '{')) {
                // （1）如果前面还有字符，并且字符为“：”，打印：换行和缩进字符字符串。
                if ((i - 1 > 0) && (json.charAt(i - 1) == ':')) {
                    result.append('\n');
                    result.append(indent(number));
                }

                // （2）打印：当前字符。
                result.append(key);

                // （3）前方括号、前花括号，的后面必须换行。打印：换行。
                result.append('\n');

                // （4）每出现一次前方括号、前花括号；缩进次数增加一次。打印：新行缩进。
                number++;
                result.append(indent(number));

                // （5）进行下一次循环。
                continue;
            }

            // 3、如果当前字符是后方括号、后花括号做如下处理：
            if ((key == ']') || (key == '}')) {
                // （1）后方括号、后花括号，的前面必须换行。打印：换行。
                result.append('\n');

                // （2）每出现一次后方括号、后花括号；缩进次数减少一次。打印：缩进。
                number--;
                result.append(indent(number));

                // （3）打印：当前字符。
                result.append(key);

                // （4）如果当前字符后面还有字符，并且字符不为“，”，打印：换行。
                if (((i + 1) < length) && (json.charAt(i + 1) != ',')) {
                    result.append('\n');
                }

                // （5）继续下一次循环。
                continue;
            }

            // 4、如果当前字符是逗号。逗号后面换行，并缩进，不改变缩进次数。
            if ((key == ',')) {
                result.append(key);
                result.append('\n');
                result.append(indent(number));
                continue;
            }

            // 5、打印：当前字符。
            result.append(key);
        }

        return result.toString();
    }


    /**
     * 单位缩进字符串。
     */
    private static String SPACE = "   ";




    /**
     * 返回指定次数的缩进字符串。每一次缩进三个空格，即SPACE。
     *
     * @param number 缩进次数。
     * @return 指定缩进次数的字符串。
     */
    private static String indent(int number) {
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < number; i++) {
            result.append(SPACE);
        }
        return result.toString();
    }

}
