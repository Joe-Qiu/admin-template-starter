package com.treeview.configurations;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "admin.template")
public class TemplateProperties {
    /** 皮肤 **/
    private String skin;
    private String anon;
    private String locale;
    private String company;
    private String rootContext;
    private String sessionTimeout;
    private String composeResource;
}