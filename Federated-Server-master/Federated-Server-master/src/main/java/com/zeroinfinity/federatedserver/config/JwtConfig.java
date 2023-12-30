package com.zeroinfinity.federatedserver.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnExpression("${app.api.logging.enable:true}")
public class JwtConfig {

	@Value("${app.api.logging.url-patterns}")
	private String[] urlPatterns;

	@Autowired
	private JwtRequestFilter jwtRequestFilter;

	/* to register the filter and apply it to custom urls */
	@Bean
	public FilterRegistrationBean<JwtRequestFilter> jwtFilter() {
		FilterRegistrationBean<JwtRequestFilter> registrationBean = new FilterRegistrationBean<>();
		registrationBean.setFilter(jwtRequestFilter);
		registrationBean.addUrlPatterns(urlPatterns);
		return registrationBean;
	}

}