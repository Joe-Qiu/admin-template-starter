package com.treeview.configurations;

import com.google.gson.Gson;
import com.treeview.enums.SessionCacheType;
import com.treeview.filter.LoggingFilter;
import com.treeview.interceptor.GlobalInterceptor;
import com.treeview.shiro.SaasRealm;
import com.treeview.shiro.ShiroTagFreeMarkerConfigurer;
import com.treeview.shiro.TemplateSessionValidationScheduler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.session.mgt.eis.MemorySessionDAO;
import org.apache.shiro.spring.remoting.SecureRemoteInvocationExecutor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.util.ThreadContext;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.scheduling.concurrent.ScheduledExecutorFactoryBean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.ThreadPoolExecutor;

@Slf4j
@Configuration
@EnableCaching
@MapperScan("com.treeview.mapper")
@EnableConfigurationProperties({TemplateProperties.class})
public class AutoConfiguration implements WebMvcConfigurer {
    @Lazy
    @Resource
    private SaasRealm saasRealm;

    @Resource
    private TemplateProperties templateProperties;

    @Resource
    @Lazy
    private TemplateSessionValidationScheduler templateSessionValidationScheduler;

    @Resource
    private GlobalInterceptor globalInterceptor;

    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private String redisPort;

    @Bean(name = {"shiroCache"})
    public CacheManager cacheManager() {
        EhCacheManager cacheManager = new EhCacheManager();
        cacheManager.setCacheManagerConfigFile("classpath:cache/shiroCache.xml");
        return cacheManager;
    }

    @Bean
    public EhCacheManagerFactoryBean ehCacheManager() {
        EhCacheManagerFactoryBean factoryBean = new EhCacheManagerFactoryBean();
        factoryBean.setConfigLocation(new ClassPathResource("cache/localCache.xml"));
        return factoryBean;
    }

    @Bean
    public EhCacheCacheManager cacheManager(EhCacheManagerFactoryBean ehCacheManager) {
        EhCacheCacheManager cacheManager = new EhCacheCacheManager();
        cacheManager.setCacheManager(ehCacheManager.getObject());
        return cacheManager;
    }

    @Bean
    public SecurityManager securityManager(SessionManager sessionManager) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(this.saasRealm);
        securityManager.setSessionManager(sessionManager);
        securityManager.setRememberMeManager(cookieRememberMeManager());
        ThreadContext.bind(securityManager);
        securityManager.setCacheManager(cacheManager());
        SecurityUtils.setSecurityManager(securityManager);
        return securityManager;
    }

    @Bean
    public SessionManager sessionManager() {
        DefaultWebSessionManager defaultWebSessionManager = new DefaultWebSessionManager();
        if (NumberUtils.isDigits(templateProperties.getSessionTimeout())) {
            defaultWebSessionManager.setGlobalSessionTimeout(Long.parseLong(templateProperties.getSessionTimeout()));
        } else {
            defaultWebSessionManager.setGlobalSessionTimeout(30 * 60 * 1000L);
        }
        defaultWebSessionManager.setDeleteInvalidSessions(true);
        defaultWebSessionManager.setSessionIdCookieEnabled(true);
        defaultWebSessionManager.setSessionIdUrlRewritingEnabled(false);
        defaultWebSessionManager.setSessionIdCookie(new SimpleCookie("KSESSIONID"));

        String cacheType = templateProperties.getSessionCacheType();

        defaultWebSessionManager.setSessionDAO(new MemorySessionDAO());
        if (StringUtils.isNotEmpty(cacheType)) {
            if (cacheType.equalsIgnoreCase(SessionCacheType.MEMORY.getType())) {
                defaultWebSessionManager.setSessionDAO(new MemorySessionDAO());
            } else if (cacheType.equalsIgnoreCase(SessionCacheType.REDIS.getType())) {
                RedisSessionDAO sessionDAO = new RedisSessionDAO();
                sessionDAO.setRedisManager(redisManager());

                defaultWebSessionManager.setSessionDAO(sessionDAO);
            }
        }

        //定义要使用的无效的Session定时调度器
        defaultWebSessionManager.setSessionValidationScheduler(templateSessionValidationScheduler);
        //是否定时检查session
        defaultWebSessionManager.setSessionValidationSchedulerEnabled(true);

        return defaultWebSessionManager;
    }

    @Bean
    public RedisManager redisManager() {
        RedisManager redisManager = new RedisManager();
        redisManager.setHost(redisHost + ":" + redisPort);
        redisManager.setTimeout(0);
        return redisManager;
    }

    @Bean
    public SecureRemoteInvocationExecutor getSecureRemoteInvocationExecutor(SecurityManager securityManager) {
        SecureRemoteInvocationExecutor secureRemoteInvocationExecutor = new SecureRemoteInvocationExecutor();
        secureRemoteInvocationExecutor.setSecurityManager(securityManager);
        return secureRemoteInvocationExecutor;
    }

    @Bean
    public ShiroTagFreeMarkerConfigurer shiroTagFreeMarkerConfigurer() {
        ShiroTagFreeMarkerConfigurer shiroTagFreeMarkerConfigurer = new ShiroTagFreeMarkerConfigurer();
        shiroTagFreeMarkerConfigurer.setTemplateLoaderPath("classpath:/templates/");
        shiroTagFreeMarkerConfigurer.setDefaultEncoding("UTF-8");
        Properties properties = new Properties();
        properties.setProperty("template_update_delay", "0");
        properties.setProperty("default_encoding", "UTF-8");
        properties.setProperty("number_format", "0.##########");
        properties.setProperty("datetime_format", "yyyy-MM-dd HH:mm:ss");
        properties.setProperty("classic_compatible", "true");
        shiroTagFreeMarkerConfigurer.setFreemarkerSettings(properties);
        return shiroTagFreeMarkerConfigurer;
    }

    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        SecurityUtils.setSecurityManager(securityManager);
        shiroFilterFactoryBean.setSuccessUrl("/");
        shiroFilterFactoryBean.setLoginUrl("/login");
        shiroFilterFactoryBean.setUnauthorizedUrl("/error/illegalAccess");
        final Map<String, String> mapConfig = new HashMap(16);
        mapConfig.put("/static/**", "anon");
        mapConfig.put("/logout", "logout");
        mapConfig.put("/login", "anon");
        mapConfig.put("/**", "authc");
        mapConfig.put("/captcha/image", "anon");
        mapConfig.put("/register", "anon");
        mapConfig.put("/system/user/checkName", "anon");
        String anonUrls = this.templateProperties.getAnon();
        if (StringUtils.isNotEmpty(anonUrls)) {
            String[] urls = StringUtils.split(anonUrls, ";");
            if (urls != null && urls.length > 0) {
                Arrays.stream(urls).forEach((item) -> {
                    mapConfig.put(item, "anon");
                });
            }
        }

        //日志拦截器
        mapConfig.put("/**", "logging");

        if(shiroFilterFactoryBean.getFilters() != null){
            shiroFilterFactoryBean.getFilters().put("logging", new LoggingFilter());
        }

        shiroFilterFactoryBean.setFilterChainDefinitionMap(mapConfig);
        return shiroFilterFactoryBean;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

    @Bean
    public DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator daap = new DefaultAdvisorAutoProxyCreator();
        daap.setProxyTargetClass(true);
        return daap;
    }

    @Bean
    public CookieRememberMeManager cookieRememberMeManager() {
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
        simpleCookie.setMaxAge(24 * 60 * 60);
        cookieRememberMeManager.setCookie(simpleCookie);
        cookieRememberMeManager.setCipherKey(Base64.decode("6ZmI6I2j5Y+R5aSn5ZOlAA=="));
        return cookieRememberMeManager;
    }

    @Bean
    public LocaleResolver localeResolver() {
        final CookieLocaleResolver cookieLocaleResolver = new CookieLocaleResolver();
        final String localeStr = this.templateProperties.getLocale();

        Locale locale = Locale.US;
        if (StringUtils.isNotEmpty(localeStr)) {
            try {
                locale = new Locale(localeStr.split("_")[0], localeStr.split("_")[1]);
            } catch (Exception e) {
                locale = Locale.US;
            }
        }

        cookieLocaleResolver.setDefaultLocale(locale);
        cookieLocaleResolver.setCookieHttpOnly(true);
        cookieLocaleResolver.setCookieName("lang");
        cookieLocaleResolver.setCookieMaxAge(24 * 60 * 60);
        return cookieLocaleResolver;
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
        localeChangeInterceptor.setParamName("lang");
        return localeChangeInterceptor;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
        registry.addResourceHandler("/static-gen/**").addResourceLocations("classpath:/static-gen/");
    }

//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(globalInterceptor).addPathPatterns("/**").excludePathPatterns("/static/**");
//        registry.addInterceptor(localeChangeInterceptor()).addPathPatterns("/**").excludePathPatterns("/static/**");
//    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(globalInterceptor).addPathPatterns("/**").excludePathPatterns("/static/**").excludePathPatterns("/static-gen/**");
        registry.addInterceptor(localeChangeInterceptor()).addPathPatterns("/**").excludePathPatterns("/static/**").excludePathPatterns("/static-gen/**");
    }

    @Bean
    public ThreadPoolTaskExecutor serviceTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(256);
        executor.setMaxPoolSize(256);
        executor.setKeepAliveSeconds(120000);
        executor.setQueueCapacity(10000);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }

    @Bean
    public ScheduledExecutorFactoryBean scheduledThreadPoolScheduler() {
        ScheduledExecutorFactoryBean scheduler = new ScheduledExecutorFactoryBean();
        scheduler.setPoolSize(5);
        scheduler.setThreadNamePrefix("scheduled-");
        return scheduler;
    }

    @Bean
    public Gson gson() {
        return new Gson();
    }

    @Bean
    public MessageSource messageSource() {
        final ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        final List<String> bundles = new ArrayList();
        bundles.add("classpath:i18n/internation");

        if (StringUtils.isNotEmpty(templateProperties.getComposeResource())) {
            String[] resources = StringUtils.split(templateProperties.getComposeResource(), ";");
            if (resources != null && resources.length > 0) {
                Arrays.stream(resources).forEach((item) -> {
                    bundles.add("classpath:" + item);
                });
            }
        }
        messageSource.setBasenames(bundles.toArray(new String[0]));
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);

        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericToStringSerializer<>(Object.class));

        template.afterPropertiesSet();
        return template;
    }
}