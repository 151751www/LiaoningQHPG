package zhwy.util;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;


@Component
@SuppressWarnings("all")
@PropertySource("classpath:address.properties")
public class MakeGradsFile {
    private static Logger logger = LoggerFactory.getLogger(MakeGradsFile.class);

    /**
     * 使用格点数据，绘制图形
     * 特别提示：使用的经纬度范围是1位小数或者两位小数的，所以进行操作时注意精度丢失，此处采用的是BigDecimal的方法
     * @param jsonArray
     * @param element
     * @param latLonStr
     * @param colorStr
     * @param steps
     * @return 图片的名称
     * @throws Exception
     */
    //程序所在路径
    @Value("${imgPath}")
    String imgPath;
    @Value("${gradsFile}")
    String gradsFile;
    @Value("${eraPath}")
    String eraPath;
    @Value("${colorsPath}")
    String colorsPath;
    @Value("${liaoningcityPath}")
    String liaoningcityPath;








    public String drawGeDianPic(double[][] arrValue,String latLonStr,String colorStr,String steps,JSONArray latLonJSON) throws Exception{
        String saveFolder=imgPath+"grads/";
        /*//获取系统时间
        Date curDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String time = sdf.format(curDate);*/

        InetAddress address = InetAddress.getLocalHost();
        String hostAddress = address.getHostAddress(); //返回IP地址
        //String gradsFile=sysProp.getProperty("gradsFile");
        //String saveFolder = sysProp.getProperty("saveFolder");
        File f = new File(saveFolder);
        if(!f.exists()&&!f.isDirectory()){
           // f.mkdir();
            f.mkdirs();
        }

        //确定经纬度范围，106.1|112.8|31.8|39.1
        BigDecimal MinLon =new BigDecimal(latLonStr.split("\\|")[0]);
        BigDecimal MaxLon =new BigDecimal(latLonStr.split("\\|")[1]);
        BigDecimal MinLat =new BigDecimal(latLonStr.split("\\|")[2]);
        BigDecimal MaxLat =new BigDecimal(latLonStr.split("\\|")[3]);
        BigDecimal step=new BigDecimal(steps);

        int x = MaxLon.subtract(MinLon).divide(step).intValue() + 2;  //134    135
        int y = MaxLat.subtract(MinLat).divide(step).intValue() + 2;  //145    147

        File ftemp = new File(saveFolder);
        if(!ftemp.exists()&&!ftemp.isDirectory()){
            ftemp.mkdirs();
        }
        Date curDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssddd");
        String time = sdf.format(curDate);
        boolean isSuccess=getTxt(latLonJSON,time,"lon","lat","value");


        String grdFilePath=saveFolder+"sta"+time+".grd";
        String gsFilePath=saveFolder + "Shaded"+time+".gs";
        String ctlFilePath= saveFolder + "sta"+time+".ctl";
        String picPath=saveFolder+"Shaded"+time+".png";
        //String zheZhaoPic=shpPath+city+".png";
        String textFilePath=saveFolder+time+"Station.txt";

        //写grd文件
        File file=new File(grdFilePath);
        DataOutputStream out=new DataOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
        for(int i=0;i<y;i++){
            for(int j=0;j<x;j++){  //88:95
                out.write(switchfloat2byte((float)arrValue[i][j]));
            }
        }
        out.close();

        getGeDianGrdCtl(x, y, MinLon.toString(), MinLat.toString(), step.toString(), ctlFilePath,grdFilePath);
        getGeDianPic(MaxLon.toString(), MinLon.toString(), MaxLat.toString(), MinLat.toString(), gsFilePath,ctlFilePath, picPath, colorStr,textFilePath);
        //调用grADS软件,画色斑图
        runGradsFile(gradsFile,gsFilePath);
        File picFile=new File(picPath);
        while(!picFile.exists()){
            Thread.sleep(2000);
        }
        //合成图片
       // picPath=HeChengPic(zheZhaoPic, picPath);

        //删除无用文件
        File ctlFile=new File(ctlFilePath);
        if(ctlFile.exists()){
           // ctlFile.delete();
        }
        File grdFile=new File(grdFilePath);
        if(grdFile.exists()){
           // grdFile.delete();
        }
        File gsFile=new File(gsFilePath);
        if(gsFile.exists()){
            //gsFile.delete();
        }
        File gsText=new File(textFilePath);
        if(gsText.exists()){
           // gsText.delete();
        }
        String path=picPath.replace(imgPath,"/img/");
        return path;
    }

    public String drawWindPic(String jsonStr,double[][] arrValue_Fs,double[][] arrValue_Fx,String latLonStr,String colorStr,String steps,JSONArray latLonJSON) throws Exception{
        String saveFolder=imgPath+"grads/";
        String showFolder=imgPath+"grads/";
        //获取系统时间
        Date curDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssddd");
        String time = sdf.format(curDate);

        InetAddress address = InetAddress.getLocalHost();
        String hostAddress = address.getHostAddress(); //返回IP地址
        File f = new File(saveFolder);
        if(!f.exists()&&!f.isDirectory()){
            f.mkdirs();
        }

        //确定经纬度范围，106.1|112.8|31.8|39.1
        BigDecimal MinLon =new BigDecimal(latLonStr.split("\\|")[0]);
        BigDecimal MaxLon =new BigDecimal(latLonStr.split("\\|")[1]);
        BigDecimal MinLat =new BigDecimal(latLonStr.split("\\|")[2]);
        BigDecimal MaxLat =new BigDecimal(latLonStr.split("\\|")[3]);
        BigDecimal step=new BigDecimal(steps);

        int x = MaxLon.subtract(MinLon).divide(step).intValue() + 1;  //134    135
        int y = MaxLat.subtract(MinLat).divide(step).intValue() + 1;  //145    147
        boolean isSuccess= getTxt(latLonJSON,time,"lon","lat","value");
        //写grd文件
        String grdGrdPath=saveFolder + "grdGrd"+time+".grd";
        File file=new File(grdGrdPath);
        DataOutputStream out=new DataOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
        for(int i=0;i<y;i++){
            for(int j=0;j<x;j++){  //88:95
                out.write(switchfloat2byte((float)arrValue_Fs[i][j]));
            }
        }
        for(int i=0;i<y;i++){
            for(int j=0;j<x;j++){  //88:95
                out.write(switchfloat2byte((float)arrValue_Fx[i][j]));
            }
        }
        out.close();

        //String shpPath=saveFolder+city+".png";

        String grdCtlPath=saveFolder+"grdCtl"+time+".ctl";
        String gsPath=saveFolder + "Wind"+time+".gs";
        String picPath=saveFolder+ "Shaded"+time+".png";
        String txtpath=saveFolder+time+"Station.txt";

        getWindGrdCtl(x,y,MinLon.toString(),MinLat.toString(),step.toString(),grdCtlPath,grdGrdPath);

        //生成对应的gs文件
        GengsWindShaded(MaxLon.toString(),MinLon.toString(),MaxLat.toString(),MinLat.toString(),grdCtlPath,gsPath,colorStr,picPath,txtpath);

        //调用grADS软件,画色斑图
        runGradsFile(gradsFile,gsPath);
        File picFile=new File(picPath);
        while(!picFile.exists()){
            Thread.sleep(1000);
        }
        //删除中间文件
        File grdGrdFile=new File(grdGrdPath);
        if(grdGrdFile.exists()){
            grdGrdFile.delete();
        }
        File grdCtlFile=new File(grdCtlPath);
        if(grdCtlFile.exists()){
            grdCtlFile.delete();
        }
        File gsFile=new File(gsPath);
        if(gsFile.exists()){
            gsFile.delete();
        }
        File txtFile=new File(txtpath);
        if(txtFile.exists()){
            txtFile.delete();
        }
        //合成图片
        /*String newPicPath=HeChengPic(shpPath, picPath);
        File pic=new File(newPicPath);
        if(pic.exists()){
            newPicPath=showFolder+"Shaded"+time+".png";
        }
        else{
            newPicPath="";
        }*/
        File pic=new File(picPath);
        if(pic.exists()){
            picPath=showFolder+"Shaded"+time+".png";
        }
        else{
            picPath="";
        }
        String path=picPath.replace(imgPath,"/img/");
        return path;
    }

    private static void GengsWindShaded(String MaxLon,String MinLon,String MaxLat,String MinLat,String grdCtlPath,String gsPath,String colorStr,String picPath,String txtpath) throws Exception
    {
        File file=new File(gsPath);
        //使用更加普遍
        PrintWriter out = new PrintWriter(file);
        out.println("'reinit'");
        out.println("'open "+grdCtlPath+"'");
        out.println("'set grads off'");
        out.println("'set grid on'");
        out.println("'set lon "+MinLon+" "+MaxLon+"'");
        out.println("'set lat "+MinLat+" "+MaxLat+"'");
        out.println("'set csmooth on'");
        out.println("'set poli off'");
        out.println("'set annot 0 0.0'");
        out.println("'set parea 0 11 0 8.5'");
        out.println("'set mproj scaled'");



        if(colorStr.length()>10){
            JSONArray arr=JSON.parseArray(colorStr);
            JSONObject jo=null;
            String ccols="",PeiSeClevs="";//自定义配色的等级分段
            int index=21;
            for(int i=arr.size()-1;i>=0;i--){
                jo=arr.getJSONObject(i);
                String color=jo.getString("colorValues");
                String[] cols=color.split(",");
                out.println("'set rgb "+index+" "+cols[0]+" "+cols[1]+" "+cols[2]+"'");
                if(!"".equals(ccols)){
                    ccols+=" ";
                }
                ccols+=index;
                index++;
                if(i>0){
                    String maxV=jo.getString("colorMaxValues");
                    if(!"".equals(PeiSeClevs)){
                        PeiSeClevs+=" ";
                    }
                    PeiSeClevs+=maxV;
                }
            }


            out.println("'set clevs "+PeiSeClevs+"'");
            out.println("'set ccols "+ccols+"'");
        }else{
            //自定义颜色风速
            out.println("'set rgb 31 239 243 255'");
            out.println("'set rgb 32 0 182 255'");
            out.println("'set rgb 33 0 255 237'");
            out.println("'set rgb 34 0 255 135'");
            out.println("'set rgb 35 1 255 45'");
            out.println("'set rgb 36 45 255 0'");
            out.println("'set rgb 37 134 255 0'");
            out.println("'set rgb 38 224 255 1'");
            out.println("'set rgb 39 255 185 0'");
            out.println("'set rgb 40 255 97 0'");
            out.println("'set rgb 41 254 0 0'");

            out.println("'set clevs 0 2 4 6 8 10 15 20 25 30'");
            out.println("'set ccols 31 32 33 34 35 36 37 38 39 40 41'");
        }

        out.println("'set gxout shaded '");
        out.println("'d fs'");

        out.println("'set gxout barb '");
        out.println("'set digsize 0.05'");
        out.println("'set cthick 6'");
        out.println("'d skip(-fs*sin(fx*3.1415926/180)*2.5,10);skip(-fs*cos(fx*3.1415926/180)*2.5,10)'");

        //数值叠加
        Integer dataCount=0;//txt行数
        BufferedReader bufferedReader=new BufferedReader(new FileReader(txtpath));
        while (bufferedReader.readLine()!=null){
            dataCount++;
        }
        out.println("cnt="+(dataCount+1));//有多少站
        out.println("i=1");
        out.println("while(i<cnt)");
        out.println("Str=read('"+txtpath+"')");
        out.println("lineStr=sublin(Str,2)");
        out.println("lon=subwrd(lineStr,1)");
        out.println("lat=subwrd(lineStr,2)");
        out.println("data=subwrd(lineStr,3)");
        out.println("'query w2xy 'lon' 'lat");
        out.println("x = subwrd(result,3)");
        out.println("y = subwrd(result,6)");
        out.println("'draw String 'x' 'y' 'data''");
        out.println("i=i+1");
        out.println("endwhile");
        out.println("ret=close('"+txtpath+"')");
        //尺寸
        out.println("'printim "+picPath+" png x725 y683 white'");
        out.println("quit;");
        out.flush();
        out.close();
    }
    public static void makeWindFile(String jsonStr,String staGridFile) throws Exception
    {

        File file=new File(staGridFile);
        DataOutputStream out=new DataOutputStream(new BufferedOutputStream(new FileOutputStream(file)));

        //如果有数据，就出图；如果没有就出白图
        JSONArray jsonArray=JSON.parseArray(jsonStr);
        String tmp="";
        float tim=0.0f;
        int nlev=1,nflag=1;
        int count=jsonArray.size();
        for (int i = 0; i < count; i++) {
            JSONObject jsonObject=jsonArray.getJSONObject(i);
            tmp=jsonObject.getString("站号");
            if(tmp.length()>7)
            {
                //substring 包前不包后
                tmp=tmp.substring(0, 7);
            }
            else {
                //字符串前面补齐，后面补齐方法：%-7s；
                tmp=String.format("%-7s", tmp);
            }
            float lat=Float.parseFloat(jsonObject.getString("纬度"));
            float lon=Float.parseFloat(jsonObject.getString("经度"));
            float U=Float.parseFloat(jsonObject.get("风速").toString());
            float V=Float.parseFloat(jsonObject.get("风向").toString());

            out.write("".getBytes());
            out.write(tmp.getBytes());
            out.write(switchfloat2byte(lat));
            out.write(switchfloat2byte(lon));
            out.write(switchfloat2byte(tim));
            out.write(switchint2byte(nlev));
            out.write(switchint2byte(nflag));
            out.write(switchfloat2byte(U));
            out.write(switchfloat2byte(V));
        }

        if(count>0)
        {
            nlev = 0;
            float lastLat=Float.parseFloat(jsonArray.getJSONObject(count-1).getString("纬度"));
            float lastLon=Float.parseFloat(jsonArray.getJSONObject(count-1).getString("经度"));
            out.write("".getBytes());
            out.write(tmp.getBytes());
            out.write(switchfloat2byte(lastLat));
            out.write(switchfloat2byte(lastLon));
            out.write(switchfloat2byte(tim));
            out.write(switchint2byte(nlev));
            out.write(switchint2byte(nflag));
        }
        out.close();
    }

    public String drawShuZhiChanPinPic(String timeType, String sttime, String dateType,String obsv,String hig  ) throws Exception{
        String shuZhiChanPinPath=imgPath+"shuzhi/";
        //获取系统时间
        Date curDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssddd");
        String time = sdf.format(curDate);

        InetAddress address = InetAddress.getLocalHost();
        String hostAddress = address.getHostAddress(); //返回IP地址
        File f = new File(shuZhiChanPinPath);
        if(!f.exists()&&!f.isDirectory()){
            f.mkdirs();
        }
        String gsPath=shuZhiChanPinPath +"Shuzhi"+time+".gs";
        String picPath=shuZhiChanPinPath+ "Shuzhi"+time+".png";


        //生成对应的gs文件
        GengsShuZhiChanPinShaded(eraPath,colorsPath,liaoningcityPath,gsPath,picPath, sttime,obsv,timeType );

        //调用grADS软件,画色斑图
        runGradsFile(gradsFile,gsPath);
        File picFile=new File(picPath);
        while(!picFile.exists()){
            Thread.sleep(1000);
        }

        File gsFile=new File(gsPath);
        if(gsFile.exists()){
           gsFile.delete();
        }

        File pic=new File(picPath);
        if(pic.exists()){
            picPath=shuZhiChanPinPath+"Shuzhi"+time+".png";
        } else{
            picPath="";
        }
        String path=picPath.replace(imgPath,"/img/");
        return path;
    }

    private static void GengsShuZhiChanPinShaded(String eraPath,String ColorsParh,String liaoningcityPath,String gsPath,String picPath,String time ,String obav,String timeType) throws Exception
    {
        String ePath="";
        String colorsgs="";
        String suanfa="";
        String title="";
        String obsv="";
        if(obav.equals("气温")){
            ePath=eraPath+"temperaturePressure/";
            suanfa="ave(t2m-273.15,t=1,t=24)";
            title=" 2 metre temperature";
            colorsgs="Colors_Tmp_7color.gs";
            obsv="t2m";
        }else if(obav.equals("气压")){
            ePath=eraPath+"temperaturePressure/";
            suanfa="ave(sp,t=1,t=24)";
            title=" Surface pressure(Pa)";
            colorsgs="100colors.gs";
            obsv="sp";
        }else if(obav.equals("10m风速")){
            ePath=eraPath+"wind/";
            suanfa="ave(mag(u10,v10),t=1,t=24)";
            title=" 10 metre wind gust(m/s)";
            colorsgs="Colors_Wind.gs";
            obsv="mag(u10,v10)";
        }

        File file=new File(gsPath);
        //使用更加普遍
        PrintWriter out = new PrintWriter(file);
        out.println("'reinit'");
        if(timeType.equals("日")){
            out.println("'sdfopen "+ePath+time.substring(0,4)+"/"+time+".nc'");
            out.println("'set lon 118 126'");
            out.println("'set lat 38.5 43.8'");
            out.println("'set grid off'");
            out.println("'set grads off'");
            out.println("'set poli off'");
            out.println("'set ylint 1'");
            out.println("'set parea 0.4 10 0.6 8'");
            out.println("'run "+ColorsParh+colorsgs+"'");
            out.println("'set csmooth on'");
            out.println("'set gxout shaded'");
            out.println("'d smth9("+suanfa+")' " );
        }else if(timeType.equals("月")){
            int days=30;
            int year=Integer.parseInt(time.substring(0,4));
            int month=Integer.parseInt(time.substring(-2));
            if(month==1||month==3||month==5||month==7||month==8||month==10||month==12){
                days=31;
            }else if(month==2){
                if(year%4==0&&year%100!=0||year%400==0){
                    days=29;
                }else{
                    days=29;
                }
            }else {
                days=30;
            }
            String it1="";
            String itx="";
            for (int i=1;i<=days;i++){
                if(obsv.equals("气温")){
                    it1+=obsv+"."+i+"-273(t=1)+";
                    itx+=obsv+"."+i+"-273(t='it')+";
                }else{
                    it1+=obsv+"."+i+"(t=1)+";
                    itx+=obsv+"."+i+"(t='it')+";
                }
            }
            it1=it1.substring(0,it1.length()-1);
            itx=itx.substring(0,itx.length()-1);
            int hour=24*days;
            out.println("day =1");
            out.println("while (day<"+days+")");
            out.println("if(day<10)");
            out.println("'sdfopen "+ePath+year+"/"+time+"0'day'.nc'");
            out.println("else");
            out.println("'sdfopen "+ePath+year+"/"+time+"'day'.nc'");
            out.println("endif");
            out.println("day=day+1");
            out.println("'endwhile'");
            out.println("'set lon 118 126'");
            out.println("'set lat 38.5 43.8'");
            out.println("'set grid off'");
            out.println("'set grads off'");
            out.println("'set poli off'");
            out.println("'set ylint 1'");
            out.println("'set parea 0.4 10 0.6 8'");
            out.println("'run "+ColorsParh+colorsgs+"'");
            out.println("'set csmooth on'");
            out.println("'set gxout shaded'");
            out.println("'define hgt="+it1+"'");
            out.println("it = 2");
            out.println("while(it<=24)");
            out.println("'set t 'it''");
            out.println("'define hgt2="+itx+"'");
            out.println("'hgt=hgt+hgt2'");
            out.println("it=it+1");
            out.println("endwhile");
            out.println("'d hgt/"+hour+"'");
        }else if(timeType.equals("年")){
            int days=365;
            int twoDays=28;
            int year=Integer.parseInt(time.substring(0,4));
            if(year%4==0&&year%100!=0||year%400==0){
                days=366;
                twoDays=29;
            }
            String it1="";
            String itx="";
            for (int i=1;i<=days;i++){
                if(obsv.equals("气温")){
                    it1+=obsv+"."+i+"-273(t=1)+";
                    itx+=obsv+"."+i+"-273(t='it')+";
                }else{
                    it1+=obsv+"."+i+"(t=1)+";
                    itx+=obsv+"."+i+"(t='it')+";
                }
            }
            it1=it1.substring(0,it1.length()-1);
            itx=itx.substring(0,itx.length()-1);
            int hour=24*days;
            out.println("month =1");
            out.println("while (month<12)");
            out.println("    day =1");
            out.println("days = 1");
            out.println(" if(month!=2)");
            out.println("  if(month=1|month=3|month=5|month=7|month=8|month=10|month=12)");
            out.println("      days = 31");
            out.println("  else ");
            out.println("      days = 30");
            out.println("endif");
            out.println(" else");
            out.println(" days = "+twoDays+"");
            out.println(" endif");
            out.println("while (day<days)");
            out.println("if(month<10)");
            out.println("if(day<10)");
            out.println("'sdfopen "+ePath+year+"/"+time+"0'month'"+"0'day'.nc'");
            out.println("else");
            out.println("'sdfopen "+ePath+year+"/"+time+"0'month'"+"'day'.nc'");
            out.println("endif");
            out.println("else");
            out.println("if(day<10)");
            out.println("'sdfopen "+ePath+year+"/"+time+"'month'"+"0'day'.nc'");
            out.println("else");
            out.println("'sdfopen "+ePath+year+"/"+time+"'month'"+"'day'.nc'");
            out.println("endif");
            out.println("endif");
            out.println("day=day+1");
            out.println("'endwhile'");
            out.println("month=month+1");
            out.println("'endwhile'");
            out.println("'set lon 118 126'");
            out.println("'set lat 38.5 43.8'");
            out.println("'set grid off'");
            out.println("'set grads off'");
            out.println("'set poli off'");
            out.println("'set ylint 1'");
            out.println("'set parea 0.4 10 0.6 8'");
            out.println("'run "+ColorsParh+colorsgs+"'");
            out.println("'set csmooth on'");
            out.println("'set gxout shaded'");
            out.println("'define hgt="+it1+"'");
            out.println("it = 2");
            out.println("while(it<=24)");
            out.println("'set t 'it''");
            out.println("'define hgt2="+itx+"'");
            out.println("'hgt=hgt+hgt2'");
            out.println("it=it+1");
            out.println("endwhile");
            out.println("'d hgt/"+hour+"'");
        }else if(timeType.equals("季")){
            int days=0;
            int twoDays=28;
            int monthend=0;
            int monthstart=0;
            int year=Integer.parseInt(time.substring(0,4));
            int lastyear=year-1;
            if(year%4==0&&year%100!=0||year%400==0){
                days=366;
                twoDays=29;
            }
            if(time.substring(-1).endsWith("1")){
                days=31+twoDays+31;
                monthend=2;
                monthstart=1;
            }else if(time.substring(-1).endsWith("2")){
                days=31+30+31;
                monthend=5;
                monthstart=3;
            }else if(time.substring(-1).endsWith("3")){
                days=30+31+31;
                monthend=8;
                monthstart=6;
            }else if(time.substring(-1).endsWith("4")){
                days=30+31+30;
                monthend=11;
                monthstart=9;
            }
            String it1="";
            String itx="";
            for (int i=1;i<=days;i++){
                if(obsv.equals("气温")){
                    it1+=obsv+"."+i+"-273(t=1)+";
                    itx+=obsv+"."+i+"-273(t='it')+";
                }else{
                    it1+=obsv+"."+i+"(t=1)+";
                    itx+=obsv+"."+i+"(t='it')+";
                }
            }
            it1=it1.substring(0,it1.length()-1);
            itx=itx.substring(0,itx.length()-1);
            int hour=24*days;
            if(time.substring(-1).endsWith("1")){
                out.println("    day12 =1");
                out.println("while (day12<31)");
                out.println("if(day12<10)");
                out.println("'sdfopen "+ePath+lastyear+"/"+lastyear+"120'day12'.nc'");
                out.println("else");
                out.println("'sdfopen "+ePath+lastyear+"/"+lastyear+"12'day12'.nc'");
                out.println("endif");
                out.println("day=day+1");
                out.println("'endwhile'");
            }
            out.println("month ="+monthstart);
            out.println("while (month<="+monthend+")");
            out.println("    day =1");
            out.println("days = 1");
            out.println(" if(month!=2)");
            out.println("  if(month=1|month=3|month=5|month=7|month=8|month=10|month=12)");
            out.println("      days = 31");
            out.println("  else ");
            out.println("      days = 30");
            out.println("endif");
            out.println(" else");
            out.println(" days = "+twoDays);
            out.println(" endif");
            out.println("while (day<days)");
            out.println("if(month<10)");
            out.println("if(day<10)");
            out.println("'sdfopen "+ePath+year+"/"+year+"0'month'"+"0'day'.nc'");
            out.println("else");
            out.println("'sdfopen "+ePath+year+"/"+year+"0'month'"+"'day'.nc'");
            out.println("endif");
            out.println("else");
            out.println("if(day<10)");
            out.println("'sdfopen "+ePath+year+"/"+year+"'month'"+"0'day'.nc'");
            out.println("else");
            out.println("'sdfopen "+ePath+year+"/"+year+"'month'"+"'day'.nc'");
            out.println("endif");
            out.println("endif");
            out.println("day=day+1");
            out.println("'endwhile'");
            out.println("month=month+1");
            out.println("'endwhile'");
            out.println("'set lon 118 126'");
            out.println("'set lat 38.5 43.8'");
            out.println("'set grid off'");
            out.println("'set grads off'");
            out.println("'set poli off'");
            out.println("'set ylint 1'");
            out.println("'set parea 0.4 10 0.6 8'");
            out.println("'run "+ColorsParh+colorsgs+"'");
            out.println("'set csmooth on'");
            out.println("'set gxout shaded'");
            out.println("'define hgt="+it1+"'");
            out.println("it = 2");
            out.println("while(it<=24)");
            out.println("'set t 'it''");
            out.println("'define hgt2="+itx+"'");
            out.println("'hgt=hgt+hgt2'");
            out.println("it=it+1");
            out.println("endwhile");
            out.println("'d hgt/"+hour+"'");

        }
        out.println("'draw title"+title+"'");
        out.println("'cbarn 1 1 10.3 4.4' " );
        out.println("'set line 1 1 2'");
        out.println("'draw shp "+liaoningcityPath+"'");
        out.println("'printim "+picPath+" png  x1200 y800 white'");
        out.println("quit;");
        out.flush();
        out.close();
    }

    public static void MakeWindFileCtl(String staCtlPath,String staGrdPath,String staMapPath) throws FileNotFoundException
    {
        File file=new File(staCtlPath);
        PrintWriter out=new PrintWriter(file);

        out.println("DSET  "+staGrdPath);
        out.println("DTYPE station");
        out.println("STNMAP " +staMapPath);
        out.println("UNDEF -9999.0");
        out.println("TDEF 1 linear 00Z09Feb2017  1hr");
        out.println("VARS 2");
        out.println("U 0 99  fengsu");
        out.println("V 0 99  fengxiang");
        out.println("ENDVARS");
        out.close();

    }

    public void getWindGrdCtl(int x,int y,String MinLon,String MinLat,String step,String ctlPath,String grdPath) throws FileNotFoundException
    {
        File file=new File(ctlPath);
        PrintWriter out=new PrintWriter(file);
        out.println("DSET  " +grdPath);
        out.println("UNDEF -9999.0");
        out.println("XDEF "+x+" LINEAR "+MinLon+" "+step);
        out.println("YDEF "+y+" LINEAR "+MinLat+" "+step);
        out.println("ZDEF 1  LINEAR 1 1");
        out.println("TDEF 1 linear 00Z09Feb2017  1hr ");
        out.println("VARS 2");
        out.println("fs 0 99 FengSu");
        out.println("fx 0 99 FengXiang");
        out.println("ENDVARS");
        out.flush();
        out.close();
    }

    /**
     * 调用grADS软件出图
     * @param exePath grads.exe文件的存放位置
     * @param GsFile gs文件的存放位置
     * @throws Exception
     */
    private static void runGradsFile(String exePath,String GsFile) throws Exception
    {
        String command=exePath+" -lbc "+GsFile;//设置外部程序的启动参数命令行参数
        Runtime rm=Runtime.getRuntime();
        Process process=rm.exec("cmd /c "+command);
        process.waitFor();
    }


    /**
     * 浮点转换为字节
     *
     * @param f
     * @return
     */
    public static byte[] switchfloat2byte(float f) {

        // 把float转换为byte[]
        int fbit = Float.floatToIntBits(f);

        byte[] b = new byte[4];
        for (int i = 0; i < 4; i++) {
            b[i] = (byte) (fbit >> (24 - i * 8));
        }

        // 翻转数组
        int len = b.length;
        // 建立一个与源数组元素类型相同的数组
        byte[] dest = new byte[len];
        // 为了防止修改源数组，将源数组拷贝一份副本
        System.arraycopy(b, 0, dest, 0, len);
        byte temp;
        // 将顺位第i个与倒数第i个交换
        for (int i = 0; i < len / 2; ++i) {
            temp = dest[i];
            dest[i] = dest[len - i - 1];
            dest[len - i - 1] = temp;
        }
        return dest;
    }

    /**
     * 将遮罩和色斑图合成
     * @param zheZhaoPicPath 遮罩的全路径
     * @param picPath 色斑图的全路径
     * @return
     */
    public static String HeChengPic(String zheZhaoPicPath,String picPath){
        try {
            BufferedImage bImage=ImageIO.read(new File(picPath));
            int width=bImage.getWidth();
            int height=bImage.getHeight();
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = image.createGraphics();
            g.setColor(Color.WHITE);
            g.fillRect(0, 0,width, height);
            g.drawImage(bImage, 0, 0, width, height, null);
            bImage = ImageIO.read(new File(zheZhaoPicPath));
            g.drawImage(bImage, 0, 0, width, height, null);
            g.dispose();
            ImageIO.write(image, "png", new File(picPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return picPath;
    }

    public void getGeDianGrdCtl(int x,int y,String MinLon,String MinLat,String step,String ctlFilePath,String grdFilePath) throws FileNotFoundException
    {
        File file=new File(ctlFilePath);
        PrintWriter out=new PrintWriter(file);
        out.println("DSET  " + grdFilePath);
        out.println("UNDEF -9999.0");
        out.println("XDEF "+x+" LINEAR "+MinLon+" "+step);
        out.println("YDEF "+y+" LINEAR "+MinLat+" "+step);
        out.println("ZDEF 1  LINEAR 1 1");
        out.println("TDEF 1 linear 00Z09Feb2017  1hr ");
        out.println("VARS 1");
        out.println("val 0 99 year");
        out.println("ENDVARS");
        out.flush();
        out.close();
    }
    private void getGeDianPic(String MaxLon,String MinLon,String MaxLat,String MinLat,String gsFilePath,String ctlFilePath,String picPath,String colorStr,String textFilePath) throws IOException
    {

//		String gs = saveFolder + "temp/Shaded"+time+".gs";


        File file=new File(gsFilePath);
        //使用更加普遍
        PrintWriter out = new PrintWriter(file);
        out.println("'reinit'");
//		out.println("'open " + saveFolder + "temp/sta"+time+".ctl'");
        out.println("'open "+ctlFilePath+"'");
        out.println("'set grads off'");
        out.println("'set grid off'");
        out.println("'set lon "+MinLon+" "+MaxLon+"'");
        out.println("'set lat "+MinLat+" "+MaxLat+"'");


        out.println("'set csmooth on'");
        out.println("'set gxout shaded '");


        out.println("'set annot 0 0.0'");
        out.println("'set mproj scaled'");
        out.println("'set annot 1 0'");
        out.println("'set poli off'");
        out.println("'set mproj scaled'");
        out.println("'set parea 0 11 0 8.5'");


        JSONArray arr= JSON.parseArray(colorStr);
        JSONObject jo=null;
        String ccols="",PeiSeClevs="";//自定义配色的等级分段
        int index=21;

        for(int i=arr.size()-1;i>=0; i--){
            jo=arr.getJSONObject(i);
            String color=jo.getString("colorValues");
            String[] cols=color.split(",");
            out.println("'set rgb "+index+" "+cols[0]+" "+cols[1]+" "+cols[2]+"'");
            if(!"".equals(ccols)){
                ccols+=" ";
            }
            ccols+=index;
            index++;
            if(i>0){
                String maxV=jo.getString("colorMaxValues");
                if(!"".equals(PeiSeClevs)){
                    PeiSeClevs+=" ";
                }
                PeiSeClevs+=maxV;


            }
        }
        out.println("'set clevs "+PeiSeClevs+"'");
        out.println("'set ccols "+ccols+"'");
        out.println("'d val'");
        //数值叠加
       /* Integer dataCount=0;//txt行数
        BufferedReader bufferedReader=new BufferedReader(new FileReader(textFilePath));
        while (bufferedReader.readLine()!=null){
            dataCount++;
        }
        out.println("cnt="+(dataCount+1));//有多少站
        out.println("i=1");
        out.println("while(i<cnt)");
        out.println("Str=read('"+textFilePath+"')");
        out.println("lineStr=sublin(Str,2)");
        out.println("lon=subwrd(lineStr,1)");
        out.println("lat=subwrd(lineStr,2)");
        out.println("data=subwrd(lineStr,3)");
        out.println("'query w2xy 'lon' 'lat");
        out.println("x = subwrd(result,3)");
        out.println("y = subwrd(result,6)");
        out.println("'draw String 'x' 'y' 'data''");
        out.println("i=i+1");
        out.println("endwhile");
        out.println("ret=close('"+textFilePath+"')");*/
        out.println("'printim "+picPath+" png x725 y683 white'");
        out.println("quit;");
        out.flush();
        out.close();
    }

    /**
     * 圆点图
     * @param jsonStr
     * @param element
     * @param type
     * @param latLonStr
     * @return
     * @throws Exception
     */
    public String drawYuanDianPic(String latLonStr,String time,int dataCount) throws Exception{
        String saveFolder=imgPath+"grads/";
        File f = new File(saveFolder);
        if(!f.exists()&&!f.isDirectory()){
            f.mkdir();
        }

        //确定经纬度范围，106.1|112.8|31.8|39.1
        String MinLon =latLonStr.split("\\|")[0];
        String MaxLon =latLonStr.split("\\|")[1];
        String MinLat =latLonStr.split("\\|")[2];
        String MaxLat =latLonStr.split("\\|")[3];

        String gsFilePath=saveFolder + "yuandiantu"+time+".gs";
        String txtFilePath=saveFolder+time+"Station.txt";
        String picPath=saveFolder+"Shaded"+time+".png";
        boolean isSuccess= GetYuanDianGs(MaxLon, MinLon, MaxLat, MinLat, gsFilePath,txtFilePath,picPath,dataCount);
        if(isSuccess){
            //调用grADS软件,画色斑图
            runGradsFile(gradsFile,saveFolder + "yuandiantu"+time+".gs");
            File picFile=new File(picPath);
            while(!picFile.exists()){
                Thread.sleep(2000);
            }

          /*  //合成图片
            String zheZhaoPic=shpPath+city+".png";
            picPath=HeChengPic(zheZhaoPic, picPath);*/

            //删除无用文件
            File gsFile=new File(gsFilePath);
            if(gsFile.exists()){
               // gsFile.delete();
            }
            File txtFile=new File(txtFilePath);
            if(txtFile.exists()){
               // txtFile.delete();
            }
        }
        return picPath;
    }


    private boolean GetYuanDianGs(String MaxLon,String MinLon,String MaxLat,String MinLat,String gsFilePath,String txtFilePath,String picPath,int dataCount)
    {
        String shpPath=imgPath+"makePic/";
        boolean isSuccess=false;
        File file=new File(gsFilePath);
        String PeiSeClevs="";//自定义配色的等级分段
        //使用更加普遍
        PrintWriter out=null;
        try {
            out = new PrintWriter(file);
            out.println("'reinit'");
            out.println("'open " + shpPath + "yuandiantu.ctl'");
            out.println("'set lon "+MinLon+" "+MaxLon+"'");
            out.println("'set lat "+MinLat+" "+MaxLat+"'");
            out.println("'set grads off'");
            out.println("'set grid off'");
            out.println("'set digsize 0'");

            out.println("'set annot 0 0.0'");
            out.println("'set mproj scaled'");
            out.println("'set annot 1 0'");
            out.println("'set poli off'");
            out.println("'set mproj scaled'");
            out.println("'set parea 0 11 0 8.5'");


            out.println("'d rain'");
           // out.println("'draw shp "+shpPath+city+".shp'");

            out.println("cnt="+dataCount);//有多少站
            out.println("i=1");
            out.println("'set rgb 21 200 17 10'");
            out.println("'set rgb 22 254 69 34'");
            out.println("'set rgb 23 255 115 41'");
            out.println("'set rgb 24 246 217 0'");
            out.println("'set rgb 25 255 255 150'");
            out.println("'set ccols  21 22 23 24 25'");
            out.println("while(i<cnt)");
            out.println("Str=read('"+txtFilePath+"')");
            out.println("lineStr=sublin(Str,2)");
            out.println("lon=subwrd(lineStr,1)");
            out.println("lat=subwrd(lineStr,2)");
            out.println("data=subwrd(lineStr,3)");
            out.println("'query w2xy 'lon' 'lat");
            out.println("x = subwrd(result,3)");
            out.println("y = subwrd(result,6)");
            out.println("if(data>0 & data<=1)");
            out.println("'set line 21 4 6'");
            out.println("'draw mark 3 'x' 'y' 0.05'");
            out.println("endif");
            out.println("if(data>1 & data<=3)");
            out.println("'set line 22 4 6'");
            out.println("'draw mark 3 'x' 'y' 0.08'");
            out.println("endif");
            out.println("if(data>3 & data<=5)");
            out.println("'set line 23 4 6'");
            out.println("'draw mark 3 'x' 'y' 0.11'");
            out.println("endif");
            out.println("if(data>5 & data<=10)");
            out.println("'set line 24 4 6'");
            out.println("'draw mark 3 'x' 'y' 0.14'");
            out.println("endif");
            out.println("if(data>10 & data<=100)");
            out.println("'set line 25 4 6'");
            out.println("'draw mark 3 'x' 'y' 0.17'");
            out.println("endif");
            out.println("i=i+1");
            out.println("endwhile");
            out.println("ret=close('"+txtFilePath+"')");
            out.println("'printim "+picPath+" png x725 y683 white'");
            out.println("quit;");
            out.flush();
            out.close();
            isSuccess=true;
        } catch (FileNotFoundException e) {
            isSuccess=false;
            e.printStackTrace();
        }finally{
            if(out!=null){
                out.flush();
                out.close();
            }
        }
        return isSuccess;
    }
    /**
     * 生成圆点图的点的信息
     * @param jsonData  数据
     * @param saveFolder txt的保存路径
     * @param time 生成txt的时间
     * @param colLonName 经度列列名
     * @param colLatName 纬度列列名
     * @param yaosu 要素列列名
     * @return 返回文本文件的路径
     * @throws Exception 异常
     */
    public boolean getTxt(JSONArray jsonData,String time,String colLonName,String colLatName,String yaosu){
        String saveFolder=imgPath+"grads/";
        String txt = saveFolder +time +"Station.txt";
        boolean isSuccess=false;
        File file=new File(txt);

        PrintWriter out=null;
        try {
            out = new PrintWriter(file);
            for (int i = 0; i < jsonData.size(); i++) {
                String lat = jsonData.getJSONObject(i).getString(colLatName);
                String lon = jsonData.getJSONObject(i).getString(colLonName);
                String value = jsonData.getJSONObject(i).getString(yaosu);
                out.println(lon+" "+lat+" "+value);
            }
            out.flush();
            out.close();
            isSuccess=true;
        } catch (FileNotFoundException e) {
            isSuccess=false;
            e.printStackTrace();
        }finally{
            if(out!=null){
                out.flush();
                out.close();
            }
        }
        return isSuccess;
    }
    public static byte[] switchint2byte(int res) {
        byte[] targets = new byte[4];

        targets[0] = (byte) (res & 0xff);// 最低位
        targets[1] = (byte) ((res >> 8) & 0xff);// 次低位
        targets[2] = (byte) ((res >> 16) & 0xff);// 次高位
        targets[3] = (byte) (res >>> 24);// 最高位,无符号右移。
        return targets;
    }

}
