package zhwy.util;

import zhwy.entity.Task;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WinTaskUtil {
    /**
     * 添加windows任务计划
     * @param taskName 任务计划名称
     * @param path 可执行文件路径
     * @param batName 可执行文件名称
     * @param startTime 任务计划起始时间
     * @param repeatInterval 重复间隔
     * @param dateType 重复间隔单位或任务计划类型：minute,hour,day,month,year
     * @return OK
     */
    public static String addWinTask(String taskName,String path,String batName,String startTime,String stopTime,String repeatInterval,
                                    String dateType){
        String result;

        result= WinTaskUtil.doTaskXml(taskName,path,batName,startTime,stopTime,repeatInterval,dateType);
        if("OK".equals(result)){
            //例：schtasks /create /ru system  /tn 国家站分 /xml C:\Users\aa\Desktop\log\renwubijiao\国家站分.xml
            String importCmd="schtasks /create /ru system /tn "+taskName+" /xml "+path+"\\"+taskName+".xml";
            System.out.println(importCmd);
            result= WinTaskUtil.doTask(importCmd);
        }
        return result;
    }

    /**
     * 删除windows任务计划
     * @param taskName 任务计划名称
     * @return OK
     */
    public static String deleteWinTask(String taskName){
        String deleteCmd="schtasks /delete /tn "+taskName+" /f";
        return WinTaskUtil.doTask(deleteCmd);
    }

    /**
     * 立即执行任务计划
     * @param taskName 任务计划名称
     * @return OK
     */
    public static String runNow(String taskName){
        String runCmd="schtasks /run /tn "+taskName+" /i";
        return WinTaskUtil.doTask(runCmd);
    }

    /**
     * 禁用任务计划
     * @param taskName 任务计划名称
     * @return OK
     */
    public static String disbaleTask(String taskName){
        String cmd="schtasks /change /tn "+taskName+" /disable";
        return WinTaskUtil.doTask(cmd);
    }

    /**
     * 启用任务计划
     * @param taskName 任务计划名称
     * @return OK
     */
    public static String enbaleTask(String taskName){
        String cmd="schtasks /change /tn "+taskName+" /enable";
        return WinTaskUtil.doTask(cmd);
    }

    /**
     *修改任务计划
     */
    public static String changeTask(String taskName,String startTime,String stopTime,String path,String repeatInterval,
                                    String dateType){

        StringBuffer sb=new StringBuffer("schtasks /change /tn ");
        sb.append(taskName);
        if(startTime!=null){
            String startDate=startTime.substring(11,16);
            startTime=startTime.substring(0,11).replace("-","/");
            sb.append(" /sd ").append(startTime);//运行任务的第一个日期
            sb.append(" /st ").append(startDate);
        }
        if(stopTime!=null){
            String endDate=stopTime.substring(11,16);
            stopTime=stopTime.substring(0,11).replace("-","/");
            sb.append(" /ed ").append(stopTime);//运行任务的最后一个日期
            sb.append(" /et ").append(endDate);
        }
        long repeat=Long.parseLong(repeatInterval);//重复间隔
        if("小时".equals(dateType)){
            repeat=repeat*60;
        }else if("日".equals(dateType)){
            repeat=repeat*60*24;
        }
        sb.append(" /ri ").append(repeat);
        if(path!=null){
            sb.append(" /tr ").append(path);//新执行脚本
        }
        return WinTaskUtil.doTask(sb.toString());

    }

    /**
     * cmd窗口执行命令
     * @param cmd cmd窗口可执行命令
     */
    public static String doTask(String cmd){
        try {
            Process process = Runtime.getRuntime().exec(cmd);
            InputStream is = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String content = br.readLine();
            while (content != null) {
                System.out.println(content);
                content = br.readLine();
            }
            return "OK";
        } catch (IOException e) {
            e.printStackTrace();
            return "NO";
        }
    }

    /**
     * 制作任务计划xml文件
     * @param taskName 任务计划名称
     * @param path 可执行文件路径
     * @param batName 可执行文件名称
     * @param startTime 起始时间
     * @param repeatInterval 重复间隔
     * @param dateType 重复间隔单位或任务计划类型：minute,hour,day,month,year
     * @return OK
     */
    public static String doTaskXml(String taskName,String path,String batName,String startTime,String stopTime,String repeatInterval,
                             String dateType){
        //根据开始时间获取月，日 yyyy-MM-dd HH:mm:ss
        String month=startTime.substring(5,7);
        String day=startTime.substring(8,10);
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now =new Date();

        StringBuilder sb=new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-16\"?>\n" +
                "<Task version=\"1.2\" xmlns=\"http://schemas.microsoft.com/windows/2004/02/mit/task\">\n" +
                "  <RegistrationInfo>\n" +
                "    <Date>");
        sb.append(sdf.format(now).replace(" ","T"));//创建时间
        sb.append("</Date>\n" +
                "    <Author>");
        sb.append(System.getProperty("user.name"));//添加当前电脑用户
        sb.append("</Author>\n" +
                "  </RegistrationInfo>\n" +
                "  <Triggers>\n" +
                "    <CalendarTrigger>");
        //分钟和小时特有部分 根据重复间隔设置几分钟或几小时
        if("分".equals(dateType)||"小时".equals(dateType)){
            sb.append("<Repetition>\n" +
                    "        <Interval>PT");
            sb.append(repeatInterval);//设置几分钟或几小时
            if("分".equals(dateType)){
                sb.append("M");
            }else{
                sb.append("H");
            }
            sb.append("</Interval>\n" +
                    "        <Duration>P1D</Duration>\n" +
                    "        <StopAtDurationEnd>false</StopAtDurationEnd>\n" +
                    "      </Repetition>");
        }
        sb.append("<StartBoundary>");
        sb.append(startTime.replace(" ","T"));
        sb.append("</StartBoundary>\n<EndBoundary>");
        sb.append(stopTime.replace(" ","T"));
        sb.append("</EndBoundary>\n" +
                "      <Enabled>true</Enabled>\n");
        //默认每月1日执行一次  年就是一年执行12次
        if("月".equals(dateType)||"年".equals(dateType)){
            sb.append("<ScheduleByMonth>\n" +
                    "        <DaysOfMonth>\n" +
                    "          <Day>");
            sb.append(day);
            sb.append("</Day>\n" +
                    "        </DaysOfMonth>\n" +
                    "        <Months>\n");
            if("月".equals(dateType)){
                sb.append("          <January />\n" +
                        "          <February />\n" +
                        "          <March />\n" +
                        "          <April />\n" +
                        "          <May />\n" +
                        "          <June />\n" +
                        "          <July />\n" +
                        "          <August />\n" +
                        "          <September />\n" +
                        "          <October />\n" +
                        "          <November />\n" +
                        "          <December />\n");
            }else{
                sb.append("<");
                sb.append(numberToMonth(month));
                sb.append("/>\n");
            }
            sb.append(" </Months>\n" +
                    "      </ScheduleByMonth>");
        }else{
            sb.append("<ScheduleByDay>\n" +
                    "<DaysInterval>");

            //小时和分钟设置1日，以日为重复间隔单位，根据重复间隔设置
            if("分".equals(dateType)||"小时".equals(dateType)||"天".equals(dateType)){
                if("天".equals(dateType)){
                    sb.append(repeatInterval);
                }else{
                    sb.append("1");
                }
            }
            sb.append("</DaysInterval>\n" +
                    "      </ScheduleByDay>\n");
        }
        sb.append("    </CalendarTrigger>\n" +
                "  </Triggers>\n" +
                "  <Principals>\n" +
                "    <Principal id=\"Author\">\n" +
                "      <UserId>");
        sb.append(System.getProperty("user.name"));//添加当前电脑用户
        sb.append("</UserId>\n" +
                "      <LogonType>InteractiveToken</LogonType>\n" +
                "      <RunLevel>LeastPrivilege</RunLevel>\n" +
                "    </Principal>\n" +
                "  </Principals>\n" +
                "  <Settings>\n" +
                "    <MultipleInstancesPolicy>IgnoreNew</MultipleInstancesPolicy>\n" +
                "    <DisallowStartIfOnBatteries>true</DisallowStartIfOnBatteries>\n" +
                "    <StopIfGoingOnBatteries>true</StopIfGoingOnBatteries>\n" +
                "    <AllowHardTerminate>true</AllowHardTerminate>\n" +
                "    <StartWhenAvailable>false</StartWhenAvailable>\n" +
                "    <RunOnlyIfNetworkAvailable>false</RunOnlyIfNetworkAvailable>\n" +
                "    <IdleSettings>\n" +
                "      <StopOnIdleEnd>true</StopOnIdleEnd>\n" +
                "      <RestartOnIdle>false</RestartOnIdle>\n" +
                "    </IdleSettings>\n" +
                "    <AllowStartOnDemand>true</AllowStartOnDemand>\n" +
                "    <Enabled>true</Enabled>\n" +
                "    <Hidden>false</Hidden>\n" +
                "    <RunOnlyIfIdle>false</RunOnlyIfIdle>\n" +
                "    <WakeToRun>false</WakeToRun>\n" +
                "    <ExecutionTimeLimit>P3D</ExecutionTimeLimit>\n" +
                "    <Priority>7</Priority>\n" +
                "  </Settings>\n" +
                "  <Actions Context=\"Author\">\n" +
                "    <Exec>\n" +
                "      <Command>");
        //执行脚本路径，例如：C:\Users\aa\Desktop\log\run.bat
        sb.append(path);
        sb.append("\\");
        sb.append(batName);

        sb.append("</Command>\n" +
                "      <WorkingDirectory>");
        sb.append(path);
        sb.append("</WorkingDirectory>\n" +
                "    </Exec>\n" +
                "  </Actions>\n" +
                "</Task>");
        //将拼成的字符串做成xml文件
        OutputStream out=null;
        try {
            File file = new File(path.replace("\\","/"));//路径斜杆必须转，否则不识别
            if (!file.exists()) {
                boolean addFlag=file.mkdirs();
                if(!addFlag){
                    throw new Exception("创建文件夹失败");
                }
            }
            file=new File((path+"\\"+taskName+".xml").replace("\\","/"));
            if (!file.exists()) {
                boolean addFlag=file.createNewFile();
                if(!addFlag){
                    throw new Exception("创建文件失败");
                }
            }else{
                boolean deleteFlag=file.delete();
                if(!deleteFlag){
                    throw new Exception("删除文件失败");
                }
                boolean addFlag=file.createNewFile();
                if(!addFlag){
                    throw new Exception("创建文件失败");
                }
            }
            out = new FileOutputStream(file) ;
            byte[] b = sb.toString().getBytes() ;
            out.write(b) ;
            out.close() ;
        } catch (IOException e) {
            e.printStackTrace();
            return  "NO";
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(out!=null){
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "OK";
    }

    /**
     * 数字转为英文月份
     * @param number 字符串格式的数字
     * @return 英文月份
     */
    public static  String numberToMonth(String number){
        String month;
        switch(number){
            case "02":
                month= "February";
                break;
            case "03":
                month= "March";
                break;
            case "04":
                month= "April";
                break;
            case "05":
                month= "May";
                break;
            case "06":
                month= "June";
                break;
            case "07":
                month= "July";
                break;
            case "08":
                month= "August";
                break;
            case "09":
                month= "September";
                break;
            case "10":
                month= "October";
                break;
            case "11":
                month= "November";
                break;
            case "12":
                month= "December";
                break;
            default:
                month="January";
        }
        return month;
    }

    /**
     * 获取配置文件路径
     * @author: jcw
     * @createDate: 2020年6月9日 上午10:28:34
     * @return path  工作路径
     * @throws UnsupportedEncodingException
     */
    public static String getSetFileDir() throws UnsupportedEncodingException {
        String domain = WinTaskUtil.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        if(isWindows()) {
            domain = domain.substring(1);
        }
        if(domain.toLowerCase().endsWith(".jar")) {
            domain = domain.substring(0, domain.lastIndexOf("/") + 1);
        }
        domain = java.net.URLDecoder.decode(domain, "UTF-8");
        return domain;
    }
    public static boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("windows");
    }

}
