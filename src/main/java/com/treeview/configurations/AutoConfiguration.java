package com.treeview.configurations;

import com.baomidou.dynamic.datasource.plugin.MasterSlaveAutoRoutingPlugin;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.treeview.interceptor.GlobalInterceptor;
import com.treeview.shiro.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import javax.annotation.Resource;
import java.util.*;

@Slf4j
@Configuration
@EnableCaching
@EnableConfigurationProperties({TemplateProperties.class})
public class AutoConfiguration implements WebMvcConfigurer {
    @Lazy
    @Resource
    private SaasRealm saasRealm;

    @Resource
    @Lazy
    private ShiroSessionDao shiroSessionDao;
    @Resource

    private TemplateProperties templateProperties;

    @Resource
    private SaasSessionListener saasSessionListener;

    @Resource
    private SaasSessionValidationScheduler saasSessionValidationScheduler;

    @Resource
    private GlobalInterceptor globalInterceptor;

    @Bean(name = {"shiroCache"})
    public CacheManager cacheManager() {
        EhCacheManager cacheManager = new EhCacheManager();
        cacheManager.setCacheManagerConfigFile("classpath:cache/shiroCache.xml");
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
        defaultWebSessionManager.setGlobalSessionTimeout(1800000L);
        defaultWebSessionManager.setDeleteInvalidSessions(true);
        defaultWebSessionManager.setSessionIdCookieEnabled(true);
        defaultWebSessionManager.setSessionIdUrlRewritingEnabled(false);
        defaultWebSessionManager.setSessionIdCookie(new SimpleCookie("KSESSIONID"));

        String configEnv = System.getProperty("config.env");
        if (configEnv != null && !"test".equals(configEnv)) {
            defaultWebSessionManager.setSessionDAO(this.shiroSessionDao);
        } else {
            defaultWebSessionManager.setSessionDAO(new MemorySessionDAO());
        }
        defaultWebSessionManager.setSessionListeners(Arrays.asList(saasSessionListener));

        //定义要使用的无效的Session定时调度器
        defaultWebSessionManager.setSessionValidationScheduler(saasSessionValidationScheduler);
        //是否定时检查session
        defaultWebSessionManager.setSessionValidationSchedulerEnabled(true);

        return defaultWebSessionManager;
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
        Map<String, String> mapConfig = new HashMap(16);
        mapConfig.put("/static/**", "anon");
        mapConfig.put("/logout", "logout");
        mapConfig.put("/login", "anon");
        mapConfig.put("/**", "authc");
        mapConfig.put("/captcha/image", "anon");
        mapConfig.put("/register", "anon");
        mapConfig.put("/system/user/checkName", "anon");
        String anonUrls = this.templateProperties.getAnonUrls();
        if (StringUtils.isNotEmpty(anonUrls)) {
            String[] urls = StringUtils.split(anonUrls, ";");
            if (urls != null && urls.length > 0) {
                Arrays.stream(urls).forEach((item) -> {
                    mapConfig.put(item, "anon");
                });
            }
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
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor(DbType.MYSQL);
        paginationInnerInterceptor.setOverflow(false);
        interceptor.addInnerInterceptor(paginationInnerInterceptor);
        return interceptor;
    }

    @Bean
    public CookieRememberMeManager cookieRememberMeManager() {
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
        simpleCookie.setMaxAge(259200000);
        cookieRememberMeManager.setCookie(simpleCookie);
        cookieRememberMeManager.setCipherKey(Base64.decode("6ZmI6I2j5Y+R5aSn5ZOlAA=="));
        return cookieRememberMeManager;
    }

    @Bean
    public LocaleResolver localeResolver() {
        final CookieLocaleResolver cookieLocaleResolver = new CookieLocaleResolver();
        cookieLocaleResolver.setDefaultLocale(Locale.US);
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

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(globalInterceptor).addPathPatterns("/**").excludePathPatterns("/static/**");
        registry.addInterceptor(localeChangeInterceptor()).addPathPatterns("/**").excludePathPatterns("/static/**");
    }

    @Bean
    public MasterSlaveAutoRoutingPlugin masterSlaveAutoRoutingPlugin() {
        return new MasterSlaveAutoRoutingPlugin();
    }
}
