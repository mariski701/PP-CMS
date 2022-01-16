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


@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeRequests().antMatchers("/").permitAll().and()
                .authorizeRequests().antMatchers("/console/**").permitAll().and();
        httpSecurity.csrf().disable();
        httpSecurity.headers().frameOptions().disable();
        httpSecurity.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                .maximumSessions(1).sessionRegistry(sessionRegistry()).and().sessionFixation();
        httpSecurity.httpBasic()
                .and()
                .authorizeRequests().antMatchers("/api/user/cms/register").hasAuthority("ADD_CMS_USER").and().csrf().disable()
                .authorizeRequests().antMatchers("/api/user/delete/{id}").hasAuthority("REMOVE_USER").and().csrf().disable()
                .authorizeRequests().antMatchers("/api/user/edit/changeMail").hasAuthority("EDIT_CMS_USER").and().csrf().disable()
                .authorizeRequests().antMatchers("/api/user/edit/changeNickname").hasAuthority("EDIT_CMS_USER").and().csrf().disable()
                .authorizeRequests().antMatchers("/api/user/cms/users").hasAuthority("READ_CMS_USERS").and().csrf().disable()
                .authorizeRequests().antMatchers("/api/articles/add").hasAuthority("ADD_ARTICLE").and().csrf().disable()
                .authorizeRequests().antMatchers("/api/articles/delete/{id}").hasAuthority("REMOVE_ARTICLE").and().csrf().disable()
                .authorizeRequests().antMatchers("/api/articles/edit").hasAnyAuthority("EDIT_ADMINS_ARTICLE", "EDIT_MODERATORS_ARTICLE", "EDIT_EDITORS_ARTICLE").and().csrf().disable()
                .authorizeRequests().antMatchers("/api/articles/publish").hasAuthority("PUBLISH_ARTICLE").and().csrf().disable()
                .authorizeRequests().antMatchers("/api/articles/{id}/allowcomments/{bool}").hasAuthority("EDIT_ARTICLE").and().csrf().disable()
                .authorizeRequests().antMatchers("/api/tag/add").hasAuthority("ADD_TAGS_CMS").and().csrf().disable()
                .authorizeRequests().antMatchers("/api/tag/remove/{id}").hasAuthority("REMOVE_TAGS").and().csrf().disable()
                .authorizeRequests().antMatchers("/api/tag/modify/{id}").hasAuthority("EDIT_TAG").and().csrf().disable()
                .authorizeRequests().antMatchers("/api/language/remove/{id}").hasAuthority("REMOVE_LANGUAGE").and().csrf().disable()
                .authorizeRequests().antMatchers("/api/language/add").hasAuthority("ADD_LANGUAGE").and().csrf().disable()
                .authorizeRequests().antMatchers("/api/language/edit").hasAuthority("EDIT_LANGUAGE").and().csrf().disable()
                .authorizeRequests().antMatchers("/api/comments/add").hasAuthority("WRITE_COMMENT").and().csrf().disable()
                .authorizeRequests().antMatchers("/api/comments/edit").hasAuthority("EDIT_OWN_COMMENT").and().csrf().disable()
                .authorizeRequests().antMatchers("/api/comments/cms/edit/{id}").hasAuthority("EDIT_COMMENT").and().csrf().disable()
                .authorizeRequests().antMatchers("/api/cms/admin/config/comments/{boolean}").hasAuthority("MANAGE_CONFIG_FLAGS").and().csrf().disable()
                .authorizeRequests().antMatchers("/api/cms/admin/config/register/{boolean}").hasAuthority("MANAGE_CONFIG_FLAGS").and().csrf().disable()
                .authorizeRequests().antMatchers("/api/cms/admin/config/login/{boolean}").hasAuthority("MANAGE_CONFIG_FLAGS").and().csrf().disable()
                .authorizeRequests().antMatchers("/api/user/edit/role").hasAuthority("EDIT_USER_ROLE").and().csrf().disable()
                .authorizeRequests().antMatchers("/api/cms/role/create").hasAuthority("ADD_ROLE").and().csrf().disable()
                .authorizeRequests().antMatchers("/api/cms/role/edit/{id}").hasAuthority("EDIT_ROLE").and().csrf().disable()
                .authorizeRequests().antMatchers("/api/cms/role/remove/{id}").hasAuthority("REMOVE_ROLE").and().csrf().disable()
                .authorizeRequests().antMatchers("/api/alerts/add").hasAuthority("ADD_TRANSLATION").and().csrf().disable()
                .authorizeRequests().antMatchers("/api/user/edit/mail").hasAuthority("EDIT_USER").and().csrf().disable()
                .authorizeRequests().antMatchers("/api/user/edit/password").hasAuthority("EDIT_USER").and().csrf().disable()
                .authorizeRequests().antMatchers("/api/user/edit/username").hasAuthority("EDIT_USER").and().csrf().disable()
                .formLogin();

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