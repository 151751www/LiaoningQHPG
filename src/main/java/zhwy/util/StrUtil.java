package zhwy.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class StrUtil {

    public static String NullToSpace(String str){
        if(str==null||"null".equals(str)){
            str="";
        }
        return str;
    }
    public static String packageResult(String message){
        JSONArray jsonArray=new JSONArray();
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("status","NO");
        jsonArray.add(jsonObject);
        JSONObject jsonObject1=new JSONObject();
        jsonObject1.put("message",message);
        jsonArray.add(jsonObject1);
        return jsonArray.toJSONString();
    }
}
