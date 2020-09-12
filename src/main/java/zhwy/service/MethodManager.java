package zhwy.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class MethodManager {


    public MethodManager() {

    }

    /**
     *
     * @param multipartFile  上传的文件
     * @param type  长序列或短序列
     * @param dataType   时  日  年
     * @return
     * @throws IOException
     */
    public String getFileContent(MultipartFile multipartFile,String type,String dataType) throws IOException {
        JSONArray jsonArray=new JSONArray();
        Reader reader ;
        reader = new InputStreamReader(multipartFile.getInputStream(), StandardCharsets.UTF_8);
        BufferedReader br = new BufferedReader( reader);
        Date time=new Date();
        String line;
        JSONObject jsonObject;
        String date="";
        if(!multipartFile.getOriginalFilename().endsWith(".txt")&&!multipartFile.getOriginalFilename().endsWith(".TXT")){
            return  "上传文件格式不正确,只能上传txt文件";
        }
        while ((line = br.readLine()) != null) {
            String[] arr=line.trim().split("\\s+");
            if((dataType.equals("时")&&arr.length!=25)||(dataType.equals("日")&&arr.length!=33)||dataType.equals("年")&&arr.length!=2){
                return  "上传文件格式不正确";
            }
            int startNum;
            if(dataType.equals("时")||dataType.equals("年")){
                startNum=1;
            }else{
                startNum=2;
            }
            for (int i=startNum;i<arr.length;i++){
                if(arr[i].equals("999999")){
                    continue;
                }
                switch (dataType) {
                    case "时":
                        if (i < 10) {
                            date = arr[0] + " 0" + i;
                        } else {
                            date = arr[0] + " " + i;
                        }
                        break;
                    case "日":
                        if (i < 10) {
                            if (Integer.parseInt(arr[1]) < 10) {
                                date = arr[0] + "-0" + arr[1] + "-0" + (i - 1);
                            } else {
                                date = arr[0] + "-" + arr[1] + "-0" + (i - 1);
                            }
                        } else {
                            if (Integer.parseInt(arr[1]) < 10) {
                                date = arr[0] + "-0" + arr[1] + "-" + (i - 1);
                            } else {
                                date = arr[0] + "-" + arr[1] + "-" + (i - 1);
                            }
                        }
                        break;
                    case "年":
                        date = arr[0];
                        break;
                }
                jsonObject=new JSONObject();
                jsonObject.put("时间",date);
                jsonObject.put(type,arr[i]);
                jsonArray.add(jsonObject);
            }
        }
        reader.close();
        return  jsonArray.toJSONString();
    }

}
