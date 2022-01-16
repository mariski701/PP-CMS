package com.cms.pp.cms.pp;


import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeRequests().antMatchers("/").permitAll().and()
                .authorizeRequests().antMatchers("/console/**").permitAll().and();
        httpSecurity.csrf().disable();
        //httpSecurity.cors();
        httpSecurity.headers().frameOptions().disable();
        httpSecurity.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                .maximumSessions(1).sessionRegistry(sessionRegistry()).and().sessionFixation();
        /*httpSecurity.httpBasic()
                .and()
                .authorizeRequests().antMatchers("/api/user/cms/register").hasAuthority("ADD_CMS_USER").and()
                .authorizeRequests().antMatchers("/api/user/delete/{id}").hasAuthority("REMOVE_USER").and()
                .authorizeRequests().antMatchers("/api/user/edit/changeMail").hasAuthority("EDIT_CMS_USER").and()
                .authorizeRequests().antMatchers("/api/user/edit/changeNickname").hasAuthority("EDIT_CMS_USER").and()
                .authorizeRequests().antMatchers("/api/user/cms/users").hasAuthority("READ_CMS_USERS").and()
                .authorizeRequests().antMatchers("/api/articles/add").hasAuthority("ADD_ARTICLE").and()
                .authorizeRequests().antMatchers("/api/articles/delete/{id}").hasAuthority("REMOVE_ARTICLE").and()
                .authorizeRequests().antMatchers("/api/articles/edit").hasAnyAuthority("EDIT_ADMINS_ARTICLE", "EDIT_MODERATORS_ARTICLE", "EDIT_EDITORS_ARTICLE").and()
                .authorizeRequests().antMatchers("/api/articles/publish").hasAuthority("PUBLISH_ARTICLE").and()
                .authorizeRequests().antMatchers("/api/articles/{id}/allowcomments/{bool}").hasAuthority("EDIT_ARTICLE").and()
                .authorizeRequests().antMatchers("/api/tag/add").hasAuthority("ADD_TAGS_CMS").and()
                .authorizeRequests().antMatchers("/api/tag/remove/{id}").hasAuthority("REMOVE_TAGS").and()
                .authorizeRequests().antMatchers("/api/tag/modify/{id}").hasAuthority("EDIT_TAG").and()
                .authorizeRequests().antMatchers("/api/language/remove/{id}").hasAuthority("REMOVE_LANGUAGE").and()
                .authorizeRequests().antMatchers("/api/language/add").hasAuthority("ADD_LANGUAGE").and()
                .authorizeRequests().antMatchers("/api/language/edit").hasAuthority("EDIT_LANGUAGE").and()
                .authorizeRequests().antMatchers("/api/comments/add").hasAuthority("WRITE_COMMENT").and()
                .authorizeRequests().antMatchers("/api/comments/edit").hasAuthority("EDIT_OWN_COMMENT").and()
                .authorizeRequests().antMatchers("/api/comments/cms/edit/{id}").hasAuthority("EDIT_COMMENT").and()
                .authorizeRequests().antMatchers("/api/cms/admin/config/comments/{boolean}").hasAuthority("MANAGE_CONFIG_FLAGS").and()
                .authorizeRequests().antMatchers("/api/cms/admin/config/register/{boolean}").hasAuthority("MANAGE_CONFIG_FLAGS").and()
                .authorizeRequests().antMatchers("/api/cms/admin/config/login/{boolean}").hasAuthority("MANAGE_CONFIG_FLAGS").and()
                .authorizeRequests().antMatchers("/api/user/edit/role").hasAuthority("EDIT_USER_ROLE").and()
                .authorizeRequests().antMatchers("/api/cms/role/create").hasAuthority("ADD_ROLE").and()
                .authorizeRequests().antMatchers("/api/cms/role/edit/{id}").hasAuthority("EDIT_ROLE").and()
                .authorizeRequests().antMatchers("/api/cms/role/remove/{id}").hasAuthority("REMOVE_ROLE").and()
                .authorizeRequests().antMatchers("/api/alerts/add").hasAuthority("ADD_TRANSLATION").and()
                .authorizeRequests().antMatchers("/api/user/edit/mail").hasAuthority("EDIT_USER").and()
                .authorizeRequests().antMatchers("/api/user/edit/password").hasAuthority("EDIT_USER").and()
                .authorizeRequests().antMatchers("/api/user/edit/username").hasAuthority("EDIT_USER").and()
                .formLogin();*/
        //httpSecurity.authorizeRequests().antMatchers("/").authenticated().and().httpBasic().and().csrf().disable();


    }

    @Bean
    protected CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
        return source;
    }

    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }


}