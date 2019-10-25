package com.wang.base.config;

import com.wang.base.config.security.*;
import com.wang.base.config.security.filters.JwtAuthenticationTokenFilter;
import com.wang.base.config.security.sms.SmsCodeAuthenticationSecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/***
 * @ClassName: A
 * @Description:
 * @Auther: wjx zhijiu
 * @Date: 2019/10/24 10:29
 */
@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {


    @Autowired
    MyAuthenticationEntryPoint authenticationEntryPoint;//未登陆时返回

    @Autowired
    MyAuthenticationSuccessHandler authenticationSuccessHandler; //登录成功返回

    @Autowired
    MyAuthenticationFailureHandler authenticationFailureHandler; //登录失败返回

    @Autowired
    MyLogoutSuccessHandler logoutSuccessHandler;//注销成功返回

    @Autowired
    MyAccessDeniedHandler accessDeniedHandler;//无权访问返回

    @Autowired
    SecurityUserService userDetailsService; // 自定义user

    @Autowired
    JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter; // JWT 拦截器

    @Autowired
    private SmsCodeAuthenticationSecurityConfig smsCodeAuthenticationSecurityConfig;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //TODO 加密相关
        auth.userDetailsService(userDetailsService).passwordEncoder(NoOpPasswordEncoder.getInstance());
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.apply(smsCodeAuthenticationSecurityConfig);
        // 去掉 CSRF
        http.csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 使用 JWT，关闭token
                .and().httpBasic().authenticationEntryPoint(authenticationEntryPoint)
                .and().formLogin().successHandler(authenticationSuccessHandler).failureHandler(authenticationFailureHandler).permitAll()
                .and().logout().logoutUrl("/logout").logoutSuccessHandler(logoutSuccessHandler).permitAll()
                .and().authorizeRequests().anyRequest().authenticated()
                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                    public <O extends FilterSecurityInterceptor> O postProcess(O fsi) {
                        fsi.setAccessDecisionManager(accessDecisionManager());
                        fsi.setSecurityMetadataSource(securityMetadataSource());
                        return fsi;
                    }
                });
        http.rememberMe().rememberMeParameter("remember-me")
                .userDetailsService(userDetailsService).tokenValiditySeconds(1000);
        http.exceptionHandling().accessDeniedHandler(accessDeniedHandler); // 无权访问 JSON 格式的数据
        http.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class); // JWT Filter

    }

    @Bean
    public AccessDecisionManager accessDecisionManager(){
        return new MyAccessDecisionManager();
    }

    @Bean
    public FilterInvocationSecurityMetadataSource securityMetadataSource() {
        return new MySecurityMetadataSource();
    }
}
