package no.nav.poao_openam_proxy.config

import no.nav.common.auth.context.AuthContextHolder
import no.nav.common.auth.context.AuthContextHolderThreadLocal
import no.nav.poao_openam_proxy.proxy_filter.PostRequestZuulFilter
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.cloud.netflix.zuul.EnableZuulProxy
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

@Profile("default")
@Configuration
@EnableZuulProxy
@EnableConfigurationProperties
class ApplicationConfig {

	@Bean
	fun postRequestZuulFilter(): PostRequestZuulFilter {
		return PostRequestZuulFilter()
	}

	@Bean
	fun authContextHolder(): AuthContextHolder {
		return AuthContextHolderThreadLocal.instance()
	}

}
