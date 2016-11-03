package com.daou.deploy.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.daou.deploy.security.AccountUserDetailsService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private AccountUserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        //        http.authorizeRequests()
        //        .antMatchers("/resources/**").permitAll()
        //        .antMatchers("/upgrade/**").permitAll()
        //        .antMatchers("/check/**").permitAll()
        //        .antMatchers("/appr/**").permitAll()
        //        .antMatchers("/guest/**").hasAnyRole("GUEST", "DEV", "ADMIN")
        //        .antMatchers("/dev/**").hasAnyRole("DEV", "ADMIN")
        //        .antMatchers("/adm/**").hasAnyRole("ADMIN")
        //        .antMatchers("/customer/**").hasRole("ADMIN")
        //        .antMatchers("/ad/appr/**").hasRole("ADMIN")
        //        .anyRequest().authenticated().and().formLogin().loginPage("/")
        //        .usernameParameter("loginId").passwordParameter("password").loginProcessingUrl("/login")
        //        .defaultSuccessUrl("/home").failureUrl("/").permitAll().and().logout().permitAll();
        http.authorizeRequests().antMatchers("/resources/**").permitAll().antMatchers("/guest/**")
                .hasAnyRole("GUEST", "DEVELOPER", "ADMIN").antMatchers("/dev/**").hasAnyRole("DEVELOPER", "ADMIN")
                .antMatchers("/adm/**").hasAnyRole("ADMIN").anyRequest().authenticated().and().formLogin()
                .loginPage("/").usernameParameter("loginId").passwordParameter("password").loginProcessingUrl("/login")
                .defaultSuccessUrl("/home").failureUrl("/").permitAll().and().logout().permitAll();

        //        http.authorizeRequests().antMatchers("/resources/**").permitAll()
        //        .antMatchers("/upgrade/**").permitAll()
        //        .antMatchers("/check/**").permitAll()
        //        .antMatchers("/appr/**").permitAll()
        //        .antMatchers("/user/**").hasRole("GUEST")
        //        .antMatchers("/customer/**").hasRole("ADMIN")
        //        .antMatchers("/ad/appr/**").hasRole("ADMIN")
        //        .anyRequest().authenticated()
        //        .and().formLogin().loginPage("/")
        //        .usernameParameter("loginId").passwordParameter("password").loginProcessingUrl("/login")
        //        .defaultSuccessUrl("/home").failureUrl("/").permitAll()
        //        .and().logout().permitAll();
    }
}
