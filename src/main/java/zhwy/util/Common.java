package zhwy.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Component
public class Common {

    @Autowired (required=false)
    HttpServletResponse response;

    //跨域
    public void getCrossOrigin(){
        response.setHeader("Access-Control-Allow-Origin","*");
        response.setHeader("Access-Control-Allow-Headers","Origin, X-Requested-With, Content-Type, Accept");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods","PUT,POST,GET,DELETE,OPTIONS");
        response.setHeader("Content-Type", "application/json;charset=utf-8");
    }
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

        itemH.put("win_d_avg_2mi", "2分钟平均风向");
        itemH.put("win_s_avg_2mi", "2分钟平均风速");
        itemH.put("win_s_10mi", "10分钟平均风向");
        itemH.put("win_d _10mi", "10分钟平均风速");
        itemH.put("win_d_s_max", "最大风速的风向");
        itemH.put("win_s_max", "最大风速");
        itemH.put("win_d_inst_max", "极大风速的风向");
        itemH.put("win_s_inst_max", "极大风速");
        itemH.put("win_d_inst", "瞬时风速的风向");
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
        itemD.put("win_d_s_max", "最大风速的风向");
        itemD.put("win_s_max", "最大风速");
        itemD.put("win_d_inst_max", "极大风速的风向");
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

        itemM.putAll(itemD);
        itemM.remove("frs_1st_top");
        itemM.remove("frs_1st_bot");
        itemM.remove("frs_2nd_top");
        itemM.remove("frs_2nd_bot");
        itemM.put("frs_depth_max","最大冻土深度");

    }

}
