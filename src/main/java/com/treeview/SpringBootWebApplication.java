package com.treeview;

import com.treeview.utils.SpringUtil;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.annotation.EnableScheduling;

@ServletComponentScan
@ImportResource("classpath:applicationContext.xml")
@PropertySources(
        value = {
                @PropertySource(value ={"classpath:envs/${config.env}/config.properties", "classpath:envs/${config.env}/jdbc.properties"}, encoding = "utf-8")
        }
)
@EnableCaching
@Configuration
@EnableScheduling
@MapperScan("com.treeview.mapper")
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class})
public class SpringBootWebApplication extends SpringBootServletInitializer implements ApplicationListener<ContextRefreshedEvent> {
    static{
        initEnv();
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(SpringBootWebApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(SpringBootWebApplication.class);
        ConfigurableApplicationContext context = application.run(args);
        SpringUtil.setContext(context);
    }

    public static void initEnv() {
        String configEnv = System.getProperty("config.env");
        if (configEnv == null) {
            System.setProperty("config.env", "test");
        }
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        ApplicationContext context = event.getApplicationContext();
    }
}