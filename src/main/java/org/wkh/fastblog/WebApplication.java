package org.wkh.fastblog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@SpringBootApplication
public class WebApplication {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(WebApplication.class, args);
    }

    @Bean
    public ApplicationSecurity applicationSecurity() {
        return new ApplicationSecurity();
    }


    @Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
    protected static class ApplicationSecurity extends WebSecurityConfigurerAdapter {
        @Override
        public void configure(WebSecurity web) throws Exception {
            web
                    .ignoring()
                    .antMatchers("/resources/**");
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.authorizeRequests()
                    .antMatchers("/").permitAll()
                    .anyRequest().authenticated().and().httpBasic();
        }
    }
}
