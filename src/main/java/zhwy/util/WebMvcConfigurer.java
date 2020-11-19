package zhwy.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebMvcConfigurer extends WebMvcConfigurerAdapter {

    @Value("${imgPath}")
    String imgPath;
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/img/**").addResourceLocations("file:"+imgPath);
       // registry.addResourceHandler("/img/**").addResourceLocations("file:F:/Liaoning/imgs/grads/");
        super.addResourceHandlers(registry);
    }


}
