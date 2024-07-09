package com.treeview.configurations;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "admin.template")
public class TemplateProperties {
    private String env;
    private String skin;
    private String anonUrls;
    private String systemName;
    private String shortName;
}