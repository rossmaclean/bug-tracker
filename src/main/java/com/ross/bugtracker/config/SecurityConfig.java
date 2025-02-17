package com.ross.bugtracker.config;

import com.ross.bugtracker.service.CustomAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomAuthenticationProvider authProvider;


    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authProvider);
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/css/**", "/js/**", "/img/**", "/error/**", "/webjars/**");
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();

        http.authorizeRequests().antMatchers("/", "/login", "/logout", "/error").permitAll();
        http.authorizeRequests().antMatchers("/delete_bug").access("hasAuthority('ROLE_DEVELOPER')");
        http.authorizeRequests().antMatchers("/update_status").access("hasAnyAuthority('ROLE_DEVELOPER', 'ROLE_TESTER')");
        http.authorizeRequests().antMatchers("/add_comment").access("hasAnyAuthority('ROLE_DEVELOPER', 'ROLE_TESTER')");

        http.authorizeRequests().antMatchers("/bugs").authenticated();

        http.authorizeRequests().and().formLogin()
                .loginProcessingUrl("/login_check")
                .loginPage("/login")
                .defaultSuccessUrl("/bugs")
                .failureUrl("/login?error=true")
                .usernameParameter("username")
                .passwordParameter("password")
                .and().logout().logoutUrl("/logout").logoutSuccessUrl("/login?logout=true");
    }
}