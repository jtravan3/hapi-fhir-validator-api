package com.jtravan.config;

import io.sentry.Sentry;
import io.sentry.SentryClient;
import io.sentry.spring.SentryExceptionResolver;
import io.sentry.spring.SentryServletContextInitializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;

@Configuration
public class SentryConfig {

    @Value("${SENTRY_DSN:}")
    private String sentryDsn;

    @Bean
    public SentryClient sentryClient() {
        return Sentry.init(sentryDsn);
    }

    @Bean
    @DependsOn("sentryClient")
    public HandlerExceptionResolver sentryExceptionResolver() {
        return new SentryExceptionResolver() {
            @Override
            public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

                Sentry.capture(ex);

                ModelAndView view = new ModelAndView(new MappingJackson2JsonView());
                view.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
                view.addObject("successful", false);
                view.addObject("errors", Collections.emptyList());
                view.addObject("warnings", Collections.emptyList());
                view.addObject("info", Collections.emptyList());

                if ("Unexpected failure while validating resource".equals(ex.getMessage())) {
                    view.addObject("exception", ex.getCause().getMessage());
                } else {
                    view.addObject("exception", ex.getMessage());
                }

                return view;
            }
            @Override
            public int getOrder() {
                // Ensures that our custom Exception Handlers will be called first
                // and duplicates won't be logged in Sentry
                return 100;
            }
        };
    }

    @Bean
    @DependsOn("sentryClient")
    public ServletContextInitializer sentryServletContextInitializer() {
        return new SentryServletContextInitializer();
    }
}
