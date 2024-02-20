package com.cms.pp.cms.pp.configuration;

import com.cms.pp.cms.pp.enums.PrivilegeName;
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

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.authorizeRequests()
			.antMatchers("/")
			.permitAll()
			.and()
			.authorizeRequests()
			.antMatchers("/console/**")
			.permitAll()
			.and();
		httpSecurity.csrf().disable();
		httpSecurity.cors();
		httpSecurity.headers().frameOptions().disable();
		httpSecurity.sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
			.maximumSessions(1)
			.sessionRegistry(sessionRegistry())
			.and()
			.sessionFixation();
		httpSecurity.httpBasic()
			.and()
			.authorizeRequests()
			.antMatchers("/api/user/cms/register")
			.hasAuthority(PrivilegeName.ADD_CMS_USER.getPrivilegeName())
			.and()
			.authorizeRequests()
			.antMatchers("/api/user/delete/{id}")
			.hasAuthority(PrivilegeName.REMOVE_USER.getPrivilegeName())
			.and()
			.authorizeRequests()
			.antMatchers("/api/user/edit/changeMail")
			.hasAuthority(PrivilegeName.EDIT_CMS_USER.getPrivilegeName())
			.and()
			.authorizeRequests()
			.antMatchers("/api/user/edit/changeNickname")
			.hasAuthority(PrivilegeName.EDIT_CMS_USER.getPrivilegeName())
			.and()
			.authorizeRequests()
			.antMatchers("/api/user/cms/users")
			.hasAuthority(PrivilegeName.READ_CMS_USERS.getPrivilegeName())
			.and()
			.authorizeRequests()
			.antMatchers("/api/articles/add")
			.hasAuthority(PrivilegeName.ADD_ARTICLE.getPrivilegeName())
			.and()
			.authorizeRequests()
			.antMatchers("/api/articles/delete/{id}")
			.hasAuthority(PrivilegeName.REMOVE_ARTICLE.getPrivilegeName())
			.and()
			.authorizeRequests()
			.antMatchers("/api/articles/edit")
			.hasAnyAuthority(PrivilegeName.EDIT_ADMINS_ARTICLE.getPrivilegeName(),
					PrivilegeName.EDIT_MODERATORS_ARTICLE.getPrivilegeName(),
					PrivilegeName.EDIT_EDITORS_ARTICLE.getPrivilegeName())
			.and()
			.authorizeRequests()
			.antMatchers("/api/articles/publish")
			.hasAuthority(PrivilegeName.PUBLISH_ARTICLE.getPrivilegeName())
			.and()
			.authorizeRequests()
			.antMatchers("/api/articles/{id}/allowcomments/{bool}")
			.hasAuthority(PrivilegeName.EDIT_ARTICLE.getPrivilegeName())
			.and()
			.authorizeRequests()
			.antMatchers("/api/tag/add")
			.hasAuthority(PrivilegeName.ADD_TAGS_CMS.getPrivilegeName())
			.and()
			.authorizeRequests()
			.antMatchers("/api/tag/remove/{id}")
			.hasAuthority(PrivilegeName.REMOVE_TAGS.getPrivilegeName())
			.and()
			.authorizeRequests()
			.antMatchers("/api/tag/modify/{id}")
			.hasAuthority(PrivilegeName.EDIT_TAG.getPrivilegeName())
			.and()
			.authorizeRequests()
			.antMatchers("/api/language/remove/{id}")
			.hasAuthority(PrivilegeName.REMOVE_LANGUAGE.getPrivilegeName())
			.and()
			.authorizeRequests()
			.antMatchers("/api/language/add")
			.hasAuthority(PrivilegeName.ADD_LANGUAGE.getPrivilegeName())
			.and()
			.authorizeRequests()
			.antMatchers("/api/language/edit")
			.hasAuthority(PrivilegeName.EDIT_LANGUAGE.getPrivilegeName())
			.and()
			.authorizeRequests()
			.antMatchers("/api/comments/add")
			.hasAuthority(PrivilegeName.WRITE_COMMENT.getPrivilegeName())
			.and()
			.authorizeRequests()
			.antMatchers("/api/comments/edit")
			.hasAnyAuthority(PrivilegeName.EDIT_OWN_COMMENT.getPrivilegeName(),
					PrivilegeName.EDIT_COMMENT.getPrivilegeName())
			.and()
			.authorizeRequests()
			.antMatchers("/api/comments/remove/{id}")
			.hasAnyAuthority(PrivilegeName.EDIT_COMMENT.getPrivilegeName(),
					PrivilegeName.EDIT_OWN_COMMENT.getPrivilegeName())
			.and()
			.authorizeRequests()
			.antMatchers("/api/comments/cms/edit/{id}")
			.hasAuthority(PrivilegeName.EDIT_COMMENT.getPrivilegeName())
			.and()
			.authorizeRequests()
			.antMatchers("/api/cms/admin/config/comments/{boolean}")
			.hasAuthority(PrivilegeName.MANAGE_CONFIG_FLAGS.getPrivilegeName())
			.and()
			.authorizeRequests()
			.antMatchers("/api/cms/admin/config/register/{boolean}")
			.hasAuthority(PrivilegeName.MANAGE_CONFIG_FLAGS.getPrivilegeName())
			.and()
			.authorizeRequests()
			.antMatchers("/api/cms/admin/config/login/{boolean}")
			.hasAuthority(PrivilegeName.MANAGE_CONFIG_FLAGS.getPrivilegeName())
			.and()
			.authorizeRequests()
			.antMatchers("/api/user/edit/role")
			.hasAuthority(PrivilegeName.EDIT_USER_ROLE.getPrivilegeName())
			.and()
			.authorizeRequests()
			.antMatchers("/api/cms/role/create")
			.hasAuthority(PrivilegeName.ADD_ROLE.getPrivilegeName())
			.and()
			.authorizeRequests()
			.antMatchers("/api/cms/role/edit/{id}")
			.hasAuthority(PrivilegeName.EDIT_ROLE.getPrivilegeName())
			.and()
			.authorizeRequests()
			.antMatchers("/api/cms/role/remove/{id}")
			.hasAuthority(PrivilegeName.REMOVE_ROLE.getPrivilegeName())
			.and()
			.authorizeRequests()
			.antMatchers("/api/alerts/add")
			.hasAuthority(PrivilegeName.ADD_TRANSLATION.getPrivilegeName())
			.and()
			.authorizeRequests()
			.antMatchers("/api/alerts/edit")
			.hasAuthority(PrivilegeName.EDIT_TRANSLATION.getPrivilegeName())
			.and()
			.authorizeRequests()
			.antMatchers("/api/original/alerts/add")
			.hasAuthority(PrivilegeName.ADD_TRANSLATION.getPrivilegeName())
			.and()
			.authorizeRequests()
			.antMatchers("/api/original/alerts/edit")
			.hasAuthority(PrivilegeName.EDIT_TRANSLATION.getPrivilegeName())
			.and()
			.authorizeRequests()
			.antMatchers("/api/original/alerts/remove/{id}")
			.hasAuthority(PrivilegeName.REMOVE_TRANSLATION.getPrivilegeName())
			.and()
			.authorizeRequests()
			.antMatchers("/api/user/edit/mail")
			.hasAuthority(PrivilegeName.EDIT_USER.getPrivilegeName())
			.and()
			.authorizeRequests()
			.antMatchers("/api/user/edit/password")
			.hasAuthority(PrivilegeName.EDIT_USER.getPrivilegeName())
			.and()
			.authorizeRequests()
			.antMatchers("/api/user/edit/username")
			.hasAuthority(PrivilegeName.EDIT_USER.getPrivilegeName())
			.and()
			.authorizeRequests()
			.antMatchers("/api/articles/cms/findall")
			.hasAuthority(PrivilegeName.READ_CMS_USERS.getPrivilegeName())
			.and()
			.authorizeRequests()
			.antMatchers("/api/articles/cms/findbyuser")
			.hasAuthority(PrivilegeName.ADD_ARTICLE.getPrivilegeName())
			.and()
			.addFilterBefore(new CorsCustomFilter(), BasicAuthenticationFilter.class)
			.formLogin();

		httpSecurity.httpBasic().authenticationEntryPoint(new NoPopupBasicAuthenticationEntryPoint());
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