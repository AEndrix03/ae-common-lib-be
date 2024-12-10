package it.aredegalli.commons.config;

import it.aredegalli.commons.config.properties.CommonProperties;
import org.springframework.stereotype.Component;

@Component
public class ConfigPrinter {
    public ConfigPrinter(CommonProperties commonProperties) {
        System.out.println("LoginType: " + commonProperties.getAuth().getLoginType());
        System.out.println("Auth Datasource URL: " + commonProperties.getAuth().getDatasource().getUrl());
    }
}
