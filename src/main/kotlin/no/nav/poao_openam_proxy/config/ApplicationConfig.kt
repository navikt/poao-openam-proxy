package no.nav.poao_openam_proxy.config

import no.nav.common.auth.context.AuthContextHolder
import no.nav.common.auth.context.AuthContextHolderThreadLocal
import no.nav.common.sts.ServiceToServiceTokenProvider
import no.nav.common.sts.utils.AzureAdServiceTokenProviderBuilder
import no.nav.poao_openam_proxy.config.properties.EnvironmentProperties
import no.nav.poao_openam_proxy.config.properties.ProxyProperties
import no.nav.poao_openam_proxy.proxy_filter.LogRequestFilter
import no.nav.poao_openam_proxy.proxy_filter.TokenExchangeFilter
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.cloud.netflix.zuul.EnableZuulProxy
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

@Profile("default")
@Configuration
@EnableZuulProxy
@EnableConfigurationProperties(EnvironmentProperties::class, ProxyProperties::class)
class ApplicationConfig {

	@Bean
	fun postRequestZuulFilter(): LogRequestFilter {
		return LogRequestFilter()
	}

	@Bean
	fun preRequestZuulFilter(
		@Value("\${server.servlet.context-path:}")
		servletContextPath: String,
		authContextHolder: AuthContextHolder,
		serviceToServiceTokenProvider: ServiceToServiceTokenProvider,
		proxyProperties: ProxyProperties
	): TokenExchangeFilter {
		return TokenExchangeFilter("$servletContextPath/proxy", authContextHolder, serviceToServiceTokenProvider, proxyProperties)
	}

	@Bean
	fun authContextHolder(): AuthContextHolder {
		return AuthContextHolderThreadLocal.instance()
	}

	@Bean
	fun serviceToServiceTokenProvider(): ServiceToServiceTokenProvider {
		return AzureAdServiceTokenProviderBuilder.builder()
			.withEnvironmentDefaults()
			.build()
	}

}
