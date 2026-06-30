package com.example.springweb.config;

import com.example.springweb.filter.RequestLoggingFilter;
import com.example.springweb.servlet.InfoServlet;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Registers servlet-layer components.
 *
 * <p>Note: Spring Boot auto-configures the {@code DispatcherServlet} (the MVC front
 * controller) mapped to {@code /}. We therefore do not declare a
 * {@code WebApplicationInitializer} / manual {@code DispatcherServlet} — doing so
 * would conflict with the auto-configuration. Instead this class demonstrates the
 * registration mechanisms: a filter and an additional plain servlet.
 */
@Configuration
public class ServletConfig {

    @Bean
    public FilterRegistrationBean<RequestLoggingFilter> requestLoggingFilter() {
        FilterRegistrationBean<RequestLoggingFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new RequestLoggingFilter());
        registration.addUrlPatterns("/api/*");
        registration.setName("requestLoggingFilter");
        registration.setOrder(1);
        return registration;
    }

    @Bean
    public ServletRegistrationBean<InfoServlet> infoServlet() {
        ServletRegistrationBean<InfoServlet> registration =
                new ServletRegistrationBean<>(new InfoServlet(), "/servlet/info");
        registration.setName("infoServlet");
        registration.setLoadOnStartup(1);
        return registration;
    }
}
