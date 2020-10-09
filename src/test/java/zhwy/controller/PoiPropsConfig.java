package zhwy.controller;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


import java.util.Map;

@ConfigurationProperties(prefix = "application.yml")
@Component
public class PoiPropsConfig {

    //把章节名作为key,章节类的全路径名作为value
    private Map<String,String> section;

    public void setSection(Map<String, String> section) {
        this.section = section;
    }

    public Map<String, String> getSection() {
        return section;
    }

    @Override
    public String toString() {
        for (Map.Entry<String,String> entry:section.entrySet()){
            String key = entry.getKey();
            String value = entry.getValue();
            System.out.println(key+":"+value);
        }
        return null;
    }
}
