package zhwy.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class FileUtil {
    public static String saveFile(MultipartFile file, String path){
        try {
            if(file != null) {
                //获取文件后缀名
                String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
                //需要保存的文件对象
                File targetFile = new File(path,  file.getOriginalFilename());
                //判断该文件对象在磁盘上是否存在
                if(!targetFile.exists()){
                    //磁盘上不存在，则进行文件的创建
                    targetFile.mkdirs();
                }
                //文件从内存中保存到磁盘上(该方法是springmvc封装的方法)
                file.transferTo(targetFile);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "NO";
        }
        return "OK";
    }
    public static String createBat(String fileName,String path){
        String cmd="";
        String [] fileArr=fileName.split(".");
        if(fileName.endsWith(".jar")){
            cmd="java -jar "+fileName;
        }
        OutputStream out=null;
        fileName=fileName.substring(0,fileName.indexOf("."))+"Task.bat";
        try {
            File file = new File(path.replace("\\","/"));//路径斜杆必须转，否则不识别
            if (!file.exists()) {
                boolean addFlag=file.mkdirs();
                if(!addFlag){
                    throw new Exception("创建文件夹失败");
                }
            }
            file=new File((path+"\\"+fileName).replace("\\","/"));
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
            byte[] b = cmd.getBytes() ;
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
        return fileName;
    }
}
