package zhwy.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
@PropertySource("classpath:address.properties")
public class PerseService {
    private static Logger logger = LoggerFactory.getLogger(PerseService.class);
    @Value("${peisePath}")
    String peisePath;
    //获取配色分类
    public JSONArray getPeiseType() throws Exception{
        JSONArray array=new JSONArray();
        JSONObject object;
        File file = new File(peisePath);		//获取其file对象
        if(!file.exists()){
            file.mkdir();
        }
        File[] fs = file.listFiles();	//遍历path下的文件和目录，放在File数组中
        for(File f:fs){					//遍历File[]数组
            if(!f.isDirectory()){
                String fileName=f.getName();
                if(fileName.endsWith(".txt")){
                    object=new JSONObject();
                    String type=fileName.replace(".txt","");
                    object.put("配色分类",type);
                    object.put("配色文件",f.getPath());
                    array.add(object);
                }
            }
        }
        return  array;
    }
    //获取配色方案
    public String GetLegendColor(String filePath) throws  Exception{
        String fileCode = getFileCode(filePath);
        BufferedReader in = null;
        JSONArray array=new JSONArray();
        JSONObject object;
        try {
            in = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), fileCode));
            String lineStr;
            String strLevel="";
            List<String > list=new ArrayList<>();
            while ((lineStr = in.readLine()) != null) {
                lineStr=lineStr.trim();
                if(lineStr.startsWith("'")&&lineStr.endsWith("'")){
                    lineStr=lineStr.substring(1,lineStr.length()-1);
                }
                if (lineStr.indexOf("clevs")!=-1){
                    strLevel = lineStr.split( "clevs")[1];
                }else if (lineStr.indexOf("rgb") !=-1){
                    String[] items =lineStr.split("\\s+");
                    list.add(items[3] + "," + items[4] + "," + items[5]);
                }
            }
            strLevel="-999999 " + strLevel + " 999999";

            String[] arrLevel = strLevel.split("\\s+");
            for (int i = 0; i < list.size(); i++)
            {
                object=new JSONObject();
                String color = list.get(i);
                String min = arrLevel[i];
                String max = arrLevel[i + 1];
                object.put("colorMinValues",min);
                object.put("colorMaxValues",max);
                object.put("colorValues",color);
                array.add(object);
            }
        } catch (UnsupportedEncodingException e) {
            logger.error(String.format("文件 %s 编码异常", filePath));
        } catch (FileNotFoundException e) {
            logger.error(String.format("文件 %s 不存在", filePath));
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return array.toJSONString();
    }
    public static String getFileCode(String filePath) {
        String code = null;
        BufferedInputStream bin = null;
        try {
            bin = new BufferedInputStream(new FileInputStream(filePath));
            int p = (bin.read() << 8) + bin.read();
            switch (p) {
                case 0xefbb:
                    code = "utf-8";
                    break;
                case 0xfffe:
                    code = "Unicode";
                    break;
                case 0xfeff:
                    code = "utf-16be";
                    break;
                default:
                    code = "GBK";
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (bin != null) {
                    bin.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return code;
    }

    public String DelLegendColorFile(String fileName)throws  Exception
    {
        boolean dele = false;
        File file = new File(peisePath);		//获取其file对象
        File[] fs = file.listFiles();	//遍历path下的文件和目录，放在File数组中
        for(File f:fs){
            if(f.getName().indexOf(fileName)!=-1){
                 dele=f.delete();
            }
        }
          if(dele==true){
              return "删除成功";
          }else{
              return "删除失败";
          }
    }

    //保存配色
    public String SaveLegendColorFile(String jsonColor, String typeName, String type)throws Exception
    {
        String result="";
        StringBuilder sb = new StringBuilder();
        JSONArray jsonArray= JSON.parseArray(jsonColor);
        File fi=new File(peisePath);
        if(!fi.exists()){
            fi.mkdir();
        }
        String  filePath = peisePath + typeName + ".txt";
        File file = new File(filePath);
        if (!file.exists()||(file.exists()&&type.equals("update"))){
            if(!file.exists()){
                file.createNewFile();
            }
            FileWriter sw = new FileWriter(filePath,false);
            String  strLevel = "";
            int index = 21;
            String strIndex = "";
            sw.write("*Generated by GrADS Color Maker");
            sw.write("\r\n");
            for (int i = 0; i < jsonArray.size(); i++)
            {
                JSONObject object=jsonArray.getJSONObject(i);
                String colorMinValues=object.getString("colorMinValues");
                String colorMaxValues=object.getString("colorMaxValues");
                String colorValues=object.getString("colorValues").replace(","," ");
                strIndex+=index+" ";
                if(i==0){
                    if(colorMinValues.equals("-999999")){
                        strLevel=colorMaxValues;
                    }else{
                        strLevel=colorMinValues+" "+colorMaxValues;
                    }

                }else if(!colorMaxValues.equals("999999")){
                    strLevel+=" "+colorMaxValues;
                }

                sw.write("'set rgb " + index+" " +colorValues+"'");
                System.out.println("'set rgb " + index+" " +colorValues+"'");
                sw.write("\r\n");
                index++;
            }
            sw.write("\r\n");
            sw.write("'set rbcols " + strIndex + "'");
            sw.write("\r\n");
            sw.write("'set clevs " + strLevel + "'");
            sw.write("\r\n");
            sw.write("return");
            sw.flush();
            sw.close();
            result="配色方案保存成功";
        }else if(file.exists()&&type.equals("add")){
            result = "配色方案已存在";
        }
        return result;
    }

}

