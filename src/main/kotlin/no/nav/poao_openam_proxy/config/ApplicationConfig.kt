package no.nav.poao_openam_proxy.config

import no.nav.common.rest.filter.ConsumerIdComplianceFilter
import no.nav.common.utils.EnvironmentUtils
import no.nav.poao_openam_proxy.proxy_filter.PostRequestZuulFilter
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.cloud.netflix.zuul.EnableZuulProxy
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

@Profile("default")
@Configuration
@EnableZuulProxy
class ApplicationConfig {

	@Bean
	fun postRequestZuulFilter(): PostRequestZuulFilter {
		return PostRequestZuulFilter()
	}

	@Bean
	fun complianceFilterRegistrationBean(): FilterRegistrationBean<ConsumerIdComplianceFilter> {
		val registration = FilterRegistrationBean<ConsumerIdComplianceFilter>()
		registration.filter = ConsumerIdComplianceFilter(EnvironmentUtils.isDevelopment().orElse(false))
		registration.order = 0
		registration.addUrlPatterns("/proxy/*")
		return registration
	}

}
