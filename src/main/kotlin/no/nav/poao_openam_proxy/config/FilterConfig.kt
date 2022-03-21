package no.nav.poao_openam_proxy.config

import no.nav.common.auth.Constants.OPEN_AM_ID_TOKEN_COOKIE_NAME
import no.nav.common.auth.context.UserRole
import no.nav.common.auth.oidc.filter.OidcAuthenticationFilter
import no.nav.common.auth.oidc.filter.OidcAuthenticator
import no.nav.common.auth.oidc.filter.OidcAuthenticatorConfig
import no.nav.common.auth.utils.UserTokenFinder
import no.nav.common.rest.filter.ConsumerIdComplianceFilter
import no.nav.common.utils.EnvironmentUtils
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class FilterConfig {

	private fun openAmAuthConfig(properties: EnvironmentProperties): OidcAuthenticatorConfig {
		return OidcAuthenticatorConfig()
			.withDiscoveryUrl(properties.openAmDiscoveryUrl)
			.withClientId(properties.veilarbloginOpenAmClientId)
			.withIdTokenCookieName(OPEN_AM_ID_TOKEN_COOKIE_NAME)
			.withIdTokenFinder(UserTokenFinder())
			.withUserRole(UserRole.INTERN)
	}

	@Bean
	fun complianceFilterRegistrationBean(): FilterRegistrationBean<ConsumerIdComplianceFilter> {
		val registration = FilterRegistrationBean<ConsumerIdComplianceFilter>()
		registration.filter = ConsumerIdComplianceFilter(EnvironmentUtils.isDevelopment().orElse(false))
		registration.order = 0
		registration.addUrlPatterns("/proxy/*")
		return registration
	}

	@Bean
	fun authenticationFilterRegistrationBean(properties: EnvironmentProperties): FilterRegistrationBean<OidcAuthenticationFilter> {
		val registration = FilterRegistrationBean<OidcAuthenticationFilter>()
		val authenticationFilter = OidcAuthenticationFilter(
			OidcAuthenticator.fromConfigs(openAmAuthConfig(properties))
		)
		registration.filter = authenticationFilter
		registration.order = 1
		registration.addUrlPatterns("/proxy/*")
		return registration
	}

}
