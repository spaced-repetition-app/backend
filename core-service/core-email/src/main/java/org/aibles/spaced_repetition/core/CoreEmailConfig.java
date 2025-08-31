package org.aibles.spaced_repetition.core;

import org.aibles.spaced_repetition.core.config.EmailProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "org.aibles.spaced_repetition.core.email")
@ComponentScan(basePackages = "org.aibles.spaced_repetition.core.config")
@EnableConfigurationProperties(EmailProperties.class)
public class CoreEmailConfig {
}