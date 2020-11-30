package zhwy;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.math3.special.Gamma;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;


public class Json {

    public static Map<String,String> itemH =new HashMap<String,String>();
    public static Map<String,String> itemD =new HashMap<String,String>();
    public static Map<String,String> itemM =new HashMap<String,String>();
    public static Map<String,String> itemY =new HashMap<String,String>();


    static {

        itemH.put("prs", "气压");
        itemH.put("prs_sea", "海平面气压");
        itemH.put("prs_max", "最高气压");
        itemH.put("prs_min", "最低气压");

        itemH.put("tem", "气温");
        itemH.put("tem_max", "最高气温");
        itemH.put("tem_min", "最低气温");

        itemH.put("dpt", "露点温度");
        itemH.put("rhu", "相对湿度");
        itemH.put("rhu_min", "最小相对湿度");

        itemH.put("vap", "水汽压");
        itemH.put("vap_min", "最小水汽压");


        itemH.put("pre", "降水量");

        itemH.put("evp_big", "大型蒸发量");

        itemH.put("win_s_avg_2mi", "2分钟平均风速");
        itemH.put("win_s_avg_10mi", "10分钟平均风速");
        itemH.put("win_s_max", "最大风速");
        itemH.put("win_s_inst_max", "极大风速");
        itemH.put("win_s_inst", "瞬时风速");

        itemH.put("gst", "地面温度");
        itemH.put("gst_max", "最高地面温度");
        itemH.put("gst_min", "最低地面温度");
        itemH.put("gst_5cm", "5cm地温");
        itemH.put("gst_10cm", "10cm地温");
        itemH.put("gst_15cm", "15cm地温");
        itemH.put("gst_20cm", "20cm地温");
        itemH.put("gst_40cm", "40cm地温");
        itemH.put("gst_80cm", "80cm地温");
        itemH.put("gst_160cm", "160cm地温");
        itemH.put("gst_320cm", "320cm地温");

        itemH.put("lgst", "草面（雪面）温度");
        itemH.put("lgst_max", "草面（雪面）最高温度");
        itemH.put("lgst_min", "草面（雪面）最低温度");


        itemH.put("vis_hor_1mi", "1分钟平均水平能见度");
        itemH.put("vis_hor_10mi", "10分钟平均水平能见度");
        itemH.put("vis_min", "最小水平能见度");
        itemH.put("vis", "定时能见度");

        itemH.put("clo_cov", "总云量");
        itemH.put("clo_cov_low", "低云量");
        itemH.put("clo_cov_lm", "云量");
        itemH.put("clo_height_lom", "云底高度");

        itemH.put("snow_depth", "积雪深度");
        itemH.put("snow_prs", "雪压");

        itemH.put("frs_1st_top", "第1冻土层上界值");
        itemH.put("frs_1st_bot", "第1冻土层下界值");
        itemH.put("frs_2nd_top", "第2冻土层上界值");
        itemH.put("frs_2nd_bot", "第2冻土层下界值");


        itemD.put("prs_avg", "平均气压");
        itemD.put("prs_max", "最高气压");
        itemD.put("prs_min", "最低气压");
        itemD.put("prs_sea_avg", "平均海平面气压");

        itemD.put("tem_avg", "平均气温");
        itemD.put("tem_max", "最高气温");
        itemD.put("tem_min", "最低气温");

        itemD.put("vap_avg", "平均水汽压");

        itemD.put("rhu_avg", "平均相对湿度");
        itemD.put("rhu_min", "最小相对湿度");

        itemD.put("clo_cov_avg", "平均总云量");
        itemD.put("clo_cov_low_avg", "平均低云量");

        itemD.put("vis_min", "最小水平能见度");

        itemD.put("pre_max_1h", "1小时最大降水量");
        itemD.put("pre_time_2008", "20-08时降水量");
        itemD.put("pre_time_0820", "08-20时降水量");
        itemD.put("pre_time_2020", "20-20时降水量");
        itemD.put("pre_time_0808", "08-08时降水量");

        itemD.put("evp_small", "小型蒸发量");
        itemD.put("evp_big", "大型蒸发量");

        itemD.put("snow_depth", "积雪深度");
        itemD.put("snow_prs", "雪压");

        itemD.put("win_s_2mi_avg", "平均2分钟风速");
        itemD.put("win_s_10mi_avg", "平均10分钟风速");
        itemD.put("win_s_max", "最大风速");
        itemD.put("win_s_inst_max", "极大风速");

        itemD.put("gst_avg", "平均地面温度");
        itemD.put("gst_max", "最高地面温度");
        itemD.put("gst_min", "最低地面温度");

        itemD.put("gst_avg_5cm", "平均5cm地温");
        itemD.put("gst_avg_10cm", "平均10cm地温");
        itemD.put("gst_avg_15cm", "平均15cm地温");
        itemD.put("gst_avg_20cm", "平均20cm地温");
        itemD.put("gst_avg_40cm", "平均40cm地温");
        itemD.put("gst_avg_80cm", "平均80cm地温");
        itemD.put("gst_avg_160cm", "平均160cm地温");
        itemD.put("gst_avg_320cm", "平均320cm地温");

        itemD.put("frs_1st_top", "第1冻土层上界值");
        itemD.put("frs_1st_bot", "第1冻土层下界值");
        itemD.put("frs_2nd_top", "第2冻土层上界值");
        itemD.put("frs_2nd_bot", "第2冻土层下界值");
        itemD.put("ssh", "日照时数");

        itemD.put("lgst_avg", "草面（雪面）温度");
        itemD.put("lgst_max", "草面（雪面）最高温度");
        itemD.put("lgst_min", "草面（雪面）最低温度");


        itemM.put("prs_avg", "平均气压");
        itemM.put("prs_max", "最高气压");
        itemM.put("prs_min", "最低气压");
        itemM.put("prs_sea_avg", "平均海平面气压");

        itemM.put("tem_avg", "平均气温");
        itemM.put("tem_max", "最高气温");
        itemM.put("tem_min", "最低气温");

        itemM.put("vap_avg", "平均水汽压");

        itemM.put("rhu_avg", "平均相对湿度");
        itemM.put("rhu_min", "最小相对湿度");

        itemM.put("clo_cov_avg", "平均总云量");
        itemM.put("clo_cov_low_avg", "平均低云量");

        itemM.put("vis_min", "最小水平能见度");

        itemM.put("pre_max_1h", "1小时最大降水量");
        itemM.put("pre_time_2008", "20-08时降水量");
        itemM.put("pre_time_0820", "08-20时降水量");
        itemM.put("pre_time_2020", "20-20时降水量");
        itemM.put("pre_time_0808", "08-08时降水量");

        itemM.put("evp_small","小型蒸发量");
        itemM.put("evp_big", "大型蒸发量");

        itemM.put("snow_depth", "最大积雪深度");
        itemM.put("snow_prs", "雪压");


        itemM.put("win_s_2mi_avg", "平均2分钟风速");
        itemM.put("win_s_10mi_avg", "平均10分钟风速");
        itemM.put("win_s_max", "最大风速");
        itemM.put("win_s_inst_max", "极大风速");

        itemM.put("gst_avg", "平均地面温度");
        itemM.put("gst_max", "最高地面温度");
        itemM.put("gst_min", "最低地面温度");

        itemM.put("gst_avg_5cm", "平均5cm地温");
        itemM.put("gst_avg_10cm", "平均10cm地温");
        itemM.put("gst_avg_15cm", "平均15cm地温");
        itemM.put("gst_avg_20cm", "平均20cm地温");
        itemM.put("gst_avg_40cm", "平均40cm地温");
        itemM.put("gst_avg_80cm", "平均80cm地温");
        itemM.put("gst_avg_160cm", "平均160cm地温");
        itemM.put("gst_avg_320cm", "平均320cm地温");

        itemM.put("frs_depth_max","最大冻土深度");

        itemM.put("ssh", "日照时数");

        itemM.put("lgst_avg", "平均草面(雪面)温度");
        itemM.put("lgst_max", "草面（雪面）最高温度");
        itemM.put("lgst_min", "草面（雪面）最低温度");



        itemY.put("prs_avg", "平均气压");
        itemY.put("prs_max", "最高气压");
        itemY.put("prs_min", "最低气压");
        itemY.put("prs_sea_avg", "平均海平面气压");

        itemY.put("tem_avg", "平均气温");
        itemY.put("tem_avg_max", "平均最高气温");
        itemY.put("tem_avg_min", "平均最低气温");
        itemY.put("tem_max", "最高气温");
        itemY.put("tem_min", "最低气温");

        itemY.put("vap_avg", "平均水汽压");

        itemY.put("rhu_avg", "平均相对湿度");
        itemY.put("rhu_min", "最小相对湿度");

        itemY.put("clo_cov_avg", "平均总云量");
        itemY.put("clo_cov_low_avg", "平均低云量");


        itemY.put("pre_1m", "年降水量");
        itemY.put("pre_max_1h", "1小时最大降水量");
        itemY.put("pre_time_2020", "20-20时降水量");
        itemM.put("pre_time_0808", "08-08时降水量");


        itemY.put("evp", "蒸发量");
        itemY.put("evp_big", "大型蒸发量");

        itemY.put("snow_depth_max", "最大积雪深度");
        itemY.put("snow_prs", "最大积雪深度");

        itemY.put("win_s_2mi_avg", "平均2分钟风速");
        itemY.put("win_s_max", "最大风速");
        itemY.put("win_s_inst_max", "极大风速");
        itemY.put("win_s_tim", "定时风速");

        itemY.put("gst_avg", "平均地面温度");
        itemY.put("egst_max_avg_mon","平均最高地面温度");
        itemY.put("gst_avg_min","平均最低地面温度");
        itemY.put("gst_max", "最高地面温度");
        itemY.put("gst_min", "最低地面温度");

        itemY.put("gst_avg_5cm", "平均5cm地温");
        itemY.put("gst_avg_10cm", "平均10cm地温");
        itemY.put("gst_avg_15cm", "平均15cm地温");
        itemY.put("gst_avg_20cm", "平均20cm地温");
        itemY.put("gst_avg_40cm", "平均40cm地温");
        itemY.put("gst_avg_80cm", "平均80cm地温");
        itemY.put("gst_avg_160cm", "平均160cm地温");
        itemY.put("gst_avg_320cm", "平均320cm地温");

        itemY.put("lgst_avg", "平均草面(雪面)温度");
        itemY.put("lgst_max_avg", "草面（雪面）最高温度");
        itemY.put("lgst_min", "草面（雪面）最低温度");
        itemY.put("lgst_max", "草面（雪面）最高温度");
        itemY.put("lgst_min", "草面（雪面）最低温度");



        itemY.put("frs_depth_max", "最大冻土深度");

        itemY.put("ssh", "日照时数");

    }



    public static void main(String[] args) {

        String fullPath = null;
        //例如：fullPath="D:/myroot/test.json"

        // 生成json格式文件
        try {
            // 保证创建一个新文件
            File file = new File("E:\\json文件测试\\Obsvelement2");
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

            Map<String,String> obsvMap=null;
            JSONObject rootH = new JSONObject(true);
            JSONObject rootD = new JSONObject(true);
            JSONObject rootM = new JSONObject(true);
            JSONObject rootY = new JSONObject(true);
            JSONObject root1 = new JSONObject(true);
            String [][]arrObsv={hourObsv,dayObsv,monthObsv,yearObsv};
            String [][]arrkey={hourKey,dayKey,monthKey,yearKey};

            for (int j=0;j<arrObsv.length;j++){
                String[] obsv=arrObsv[j];
                String [] zrrkey=arrkey[j];
                if(j==0){
                    obsvMap=itemH;
                }else if(j==1){
                    obsvMap=itemD;
                }else if(j==2){
                    obsvMap=itemM;
                }else if(j==3){
                    obsvMap=itemY;
                }
                for (int i=0;i<obsv.length;i++){
                    JSONArray array = new JSONArray();
                    JSONObject obsvObject=new JSONObject(true);
                    String obsvKey=zrrkey[i];
                    for (Map.Entry<String,String>entry:obsvMap.entrySet()){
                        String key=entry.getKey();
                        if(key.startsWith(obsvKey)){
                            JSONObject  jsonObject=new JSONObject(true);
                            jsonObject.put("val",entry.getKey());
                            jsonObject.put("name",entry.getValue());
                            array.add(jsonObject);
                        }
                    }
                    obsvObject.put("value",obsvKey);
                    obsvObject.put("label",obsv[i]);
                    obsvObject.put("children",array);
                    if(j==0){
                        rootH.put(obsv[i],obsvObject);
                    }else if(j==1){
                        rootD.put(obsv[i],obsvObject);
                    }else if(j==2){
                        rootM.put(obsv[i],obsvObject);
                    }else if(j==3){
                       rootY.put(obsv[i],obsvObject);
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
        }
    }
  /*  public static void main(String[] args)  {
        try {
            writeExcelWithFormula("Formulas.xlsx");
        }catch (Exception e){
            e.printStackTrace();
        }

    }
*/


        public static void writeExcelWithFormula(String fileName) {
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Numbers");
            Row row = sheet.createRow(0);
            row.createCell(0).setCellValue(10);
            row.createCell(1).setCellValue(20);
            row.createCell(2).setCellValue(30);
            //set formula cell
            row.createCell(3).setCellFormula("A1*B1*C1");
            System.out.println(row.getCell(3).getCellFormula());
            System.out.println(row.getCell(3).getNumericCellValue());
        }
    public static  double getGammaInv(double x){
        double  a=2.1786195721735817;
        double b=0.053036345708270725;
        double a0=23.047140221402216;
        double ad=Math.pow(b,a);
        double gamma1=Gamma.gamma(2.28065545);
        double d1=Math.pow(b,a)/gamma1;
        double xa0=x-a0;
        double d2=Math.pow(xa0,a-1);
        double d3=Math.pow(Math.E,-b*(1-a0));
        double y=d1*d2*d3*100;
        System.out.println("x:"+x+";y:"+y);
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
