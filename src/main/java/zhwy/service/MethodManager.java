package zhwy.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.web.multipart.MultipartFile;
import zhwy.datatable.DataColumn;
import zhwy.datatable.DataRow;
import zhwy.datatable.DataTable;
import zhwy.entity.JsonMethodResult;
import zhwy.entity.LangFileFirstLine;
import zhwy.entity.LangMethodName;
import zhwy.entity.MethodResult;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MethodManager {

    private String _dataFilePath ;

    public MethodManager(String dataFilePath, String method)
    {
        this._dataFilePath = dataFilePath;
    }

    public MethodManager() {

    }


    public DataTable GetFileContentMethod14(String methodName, String filePath, String timeFormat) throws IOException {
        DataTable table = new DataTable();
        String firstLine = "";
         firstLine = CheckFileRight(methodName, _dataFilePath);
        if (!firstLine.equals(""))
        {
            return table;
        }

        String[] arrCol = firstLine.split(",");
        for (int i = 0; i < arrCol.length; i++)
        {
            table.getColumns().Add(arrCol[i]);
        }

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(filePath)),
                    "UTF-8"));
            String line = null;
            while ((line = br.readLine()) != null) {//数据以逗号分隔

                if (line.trim().equals(""))
                {
                    continue;
                }

                String[] arrItem = line.split(",");
                DataRow newRow = table.NewRow();

                for (int i = 0; i < arrItem.length; i++)
                {
                    String str = "";
                    str = String.valueOf(arrItem[i]);
                    newRow.set(i,str);
                }

                table.getRows().Add(newRow);
                }
            br.close();
        } catch (Exception e) {
            System.err.println("read errors :" + e);
        }
        for (DataColumn col:table.getColumns()) {
            col.setCaption(col.getColumnName());
        }
        return table;
    }


    private String  CheckFileRight(String methodKey, String filePath) throws IOException {
        LangMethodName langMethodName=new LangMethodName();
        String firstLine="";
        boolean success = false;
        String line="";

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(filePath)),
                    "UTF-8"));
            line=br.readLine();
            br.close();
        } catch (Exception e) {
            System.err.println("read errors :" + e);
        }
        if (methodKey == langMethodName.Method01)
        {
            switch (line)
            {
                case LangFileFirstLine.Line01
                        : success = true; break;
                case LangFileFirstLine.Line02: success = true; break;
                case LangFileFirstLine.Line03: success = true; break;
                default: break;
            }
        }
        else if (methodKey == langMethodName.Method02 || methodKey == langMethodName.Method10)
        {
            switch (line)
            {
                case LangFileFirstLine.Line04: success = true; break;
                default: break;
            }
        }
        else if (methodKey == langMethodName.Method03)
        {
            switch (line)
            {
                case LangFileFirstLine.Line05: success = true; break;
                default: break;
            }
        }
        else if (methodKey == langMethodName.Method04  )
        {
            switch (line)
            {
                case LangFileFirstLine.Line06_1: success = true; break;
                case LangFileFirstLine.Line07_1: success = true; break;
                default: break;
            }
        }
        else if (methodKey == langMethodName.Method05)
        {
            switch (line)
            {
                case LangFileFirstLine.Line08: success = true; break;
                case LangFileFirstLine.Line09: success = true; break;
                default: break;
            }
        }
        else if (methodKey == langMethodName.Method06)
        {
            switch (line)
            {
                case LangFileFirstLine.Line10: success = true; break;
                default: break;
            }
        }
        else if (methodKey == langMethodName.Method11)
        {
            switch (line)
            {
                case LangFileFirstLine.Line11: success = true; break;
                default: break;
            }
        }
        else if (methodKey == langMethodName.Method12)
        {
            switch (line)
            {
                case LangFileFirstLine.Line06: success = true; break;
                case LangFileFirstLine.Line07: success = true; break;
                default: break;
            }
        }
        else if (methodKey == langMethodName.Method13)
        {
            switch (line)
            {
                case LangFileFirstLine.Line12: success = true; break;
                default: break;
            }
        }
        else if (methodKey == langMethodName.Method14)
        {
            switch (line)
            {
                case LangFileFirstLine.Line13: success = true; break;
                default: break;
            }
        }
        else if (methodKey == langMethodName.Method15)
        {
            switch (line)
            {
                case LangFileFirstLine.Line14: success = true; break;
                default: break;
            }
        }
        else if (methodKey == langMethodName.Method16)
        {
            switch (line)
            {
                case LangFileFirstLine.Line15: success = true; break;
                default: break;
            }
        }
        else if (methodKey == langMethodName.Method17 || methodKey == langMethodName.Method18)
        {
            switch (line)
            {
                case LangFileFirstLine.Line16: success = true; break;
                default: break;
            }
        }
        else if (methodKey == langMethodName.Method19)
        {
            switch (line)
            {
                case LangFileFirstLine.Line17: success = true; break;
                default: break;
            }
        }
        if (success)
        {
            firstLine = line;
        }
        return firstLine;
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
        Reader reader = null;
        reader = new InputStreamReader(multipartFile.getInputStream(), "utf-8");
        BufferedReader br = new BufferedReader( reader);
        Date time=new Date();
        String line;
        JSONObject jsonObject;
        String date="";
        while ((line = br.readLine()) != null) {
            String[] arr=line.trim().split("\\s+");
            if((dataType.equals("时")&&arr.length!=25)||(dataType.equals("日")&&arr.length!=33)||dataType.equals("年")&&arr.length!=2){
                return  "上传文件格式不正确";
            }
            int startNum=0;
            if(dataType.equals("时")||dataType.equals("年")){
                startNum=1;
            }else{
                startNum=2;
            }
            for (int i=startNum;i<arr.length;i++){
                if(arr[i].equals("999999")){
                    continue;
                }
                if(dataType.equals("时")){
                    if(i<10){
                        date=arr[0]+" 0"+i;
                    }else{
                        date=arr[0]+" "+i;
                    }
                }else if(dataType.equals("日")){
                    if(i<10){
                        if(Integer.parseInt(arr[1])<10){
                            date=arr[0]+"-0"+arr[1]+"-0"+(i-1);
                        }else{
                            date=arr[0]+"-"+arr[1]+"-0"+(i-1);
                        }
                    }else{
                        if(Integer.parseInt(arr[1])<10){
                            date=arr[0]+"-0"+arr[1]+"-"+(i-1);
                        }else{
                            date=arr[0]+"-"+arr[1]+"-"+(i-1);
                        }
                    }
                }else if(dataType.equals("年")){
                    date=arr[0];
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
