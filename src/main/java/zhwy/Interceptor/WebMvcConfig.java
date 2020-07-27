package zhwy.Interceptor;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
 
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {
 
    @Autowired
    private LoginInterceptor loginInterceptor;
 
    /**
     * 重写添加拦截器方法并添加配置拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //拦截哪些路径("/**":代表拦截所有路径);
        registry.addInterceptor(loginInterceptor)
        		.addPathPatterns("/huanJingQiXiangYuBao/saveHuanJingData")
        		.addPathPatterns("/huanJingQiXiangYuBao/saveProductInfo")
        		.addPathPatterns("/huanJingQiXiangYuBao/ruKu").excludePathPatterns("/**");
        super.addInterceptors(registry);
    }
}
