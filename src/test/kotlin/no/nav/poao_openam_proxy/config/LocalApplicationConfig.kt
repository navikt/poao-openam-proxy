package no.nav.poao_openam_proxy.config

import no.nav.common.auth.context.AuthContextHolder
import no.nav.common.auth.context.AuthContextHolderThreadLocal
import no.nav.common.sts.ServiceToServiceTokenProvider
import no.nav.poao_openam_proxy.config.properties.EnvironmentProperties
import no.nav.poao_openam_proxy.config.properties.ProxyProperties
import no.nav.poao_openam_proxy.proxy_filter.LogRequestFilter
import no.nav.poao_openam_proxy.proxy_filter.TokenExchangeFilter
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.cloud.netflix.zuul.EnableZuulProxy
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableZuulProxy
@EnableConfigurationProperties(EnvironmentProperties::class, ProxyProperties::class)
class LocalApplicationConfig {

	@Bean
	fun postRequestZuulFilter(): LogRequestFilter {
		return LogRequestFilter()
	}

	@Bean
	fun preRequestZuulFilter(
		authContextHolder: AuthContextHolder,
		serviceToServiceTokenProvider: ServiceToServiceTokenProvider,
		proxyProperties: ProxyProperties
	): TokenExchangeFilter {
		return TokenExchangeFilter("/proxy", authContextHolder, serviceToServiceTokenProvider, proxyProperties)
	}

	@Bean
	fun authContextHolder(): AuthContextHolder {
		return AuthContextHolderThreadLocal.instance()
	}

	@Bean
	fun serviceToServiceTokenProvider(): ServiceToServiceTokenProvider {
		return ServiceToServiceTokenProvider { serviceName, namespace, cluster -> "token" }
	}

}
