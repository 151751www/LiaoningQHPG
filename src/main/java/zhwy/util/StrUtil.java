package zhwy.util;

public class StrUtil {

    public static String NullToSpace(String str){
        if(str==null||"null".equals(str)){
            str="";
        }
        return str;
    }
}
