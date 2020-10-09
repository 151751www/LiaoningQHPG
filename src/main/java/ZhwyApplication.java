import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@ComponentScan("zhwy")
public class ZhwyApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZhwyApplication.class, args);
    }

}
