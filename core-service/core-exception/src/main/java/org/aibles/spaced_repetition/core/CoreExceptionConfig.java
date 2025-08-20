package org.aibles.spaced_repetition.core;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "org.aibles.spaced_repetition.core.exception")
@ComponentScan(basePackages = "org.aibles.spaced_repetition.core.i18n")
@ComponentScan(basePackages = "org.aibles.spaced_repetition.core.config")
public class CoreExceptionConfig {
}