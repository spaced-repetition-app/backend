package org.aibles.spaced_repetition.core.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Data
@Component
@ConfigurationProperties(prefix = "spring.mail")
public class EmailProperties {
    
    private String host;
    private int port = 587;
    private String username;
    private String password;
    private String protocol = "smtp";
    private String defaultEncoding = "UTF-8";
    private boolean testConnection = false;
    
    private Smtp smtp = new Smtp();
    
    private Properties properties = new Properties();
    
    @Data
    public static class Smtp {
        private boolean auth = true;
        private boolean starttlsEnable = true;
        private boolean debug = false;
        private int connectionTimeout = 5000;
        private int timeout = 5000;
        private int writeTimeout = 5000;
    }
    
    public Properties getJavaMailProperties() {
        if (!properties.isEmpty()) {
            return properties;
        }
        
        Properties props = new Properties();
        props.put("mail.transport.protocol", protocol);
        props.put("mail.smtp.auth", smtp.auth);
        props.put("mail.smtp.starttls.enable", smtp.starttlsEnable);
        props.put("mail.debug", smtp.debug);
        props.put("mail.smtp.connectiontimeout", smtp.connectionTimeout);
        props.put("mail.smtp.timeout", smtp.timeout);
        props.put("mail.smtp.writetimeout", smtp.writeTimeout);
        return props;
    }
}