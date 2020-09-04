package zhwy.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class Common {

    @Autowired (required=false)
    HttpServletResponse response;

    //跨域
    public void getCrossOrigin(){
        response.setHeader("Access-Control-Allow-Origin","*");
        response.setHeader("Access-Control-Allow-Headers","*");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods","PUT,POST,GET,DELETE,OPTIONS");
        response.setHeader("Content-Type", "application/json;charset=utf-8");
    }

    public List<Map<String,Object>> getList(String Array,String [] Name){
        JSONArray array=JSONArray.parseArray(Array);
        List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
        Map<String,Object>map;
        for (int i=0;i<array.size();i++){
            JSONObject object= (JSONObject) array.get(i);
            map=new HashMap<String,Object>();
            for (int j=0;j<Name.length;j++){
                map.put(Name[j],object.get(Name[j]));
            }
            list.add(map);
        }
        return  list;

    }



}
