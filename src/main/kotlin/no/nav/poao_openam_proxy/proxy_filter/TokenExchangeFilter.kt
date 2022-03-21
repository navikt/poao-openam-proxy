package no.nav.poao_openam_proxy.proxy_filter

import com.netflix.zuul.ZuulFilter
import com.netflix.zuul.context.RequestContext
import com.netflix.zuul.exception.ZuulException
import no.nav.common.auth.context.AuthContextHolder
import no.nav.common.sts.ServiceToServiceTokenProvider
import no.nav.poao_openam_proxy.config.properties.ProxyProperties
import no.nav.poao_openam_proxy.utils.UrlUtils
import org.springframework.http.HttpHeaders

class TokenExchangeFilter(
	private val contextPath: String,
	private val authContextHolder: AuthContextHolder,
	private val serviceToServiceTokenProvider: ServiceToServiceTokenProvider,
	private val proxyProperties: ProxyProperties
) : ZuulFilter() {

	companion object {
		const val NAV_IDENT_HEADER = "nav-ident"
	}

	override fun shouldFilter() = true

	override fun filterType() = "pre"

	override fun filterOrder() = 5

	override fun run(): Any? {
		val ctx = RequestContext.getCurrentContext()
		val request = ctx.request

		val pathWithoutContext: String = UrlUtils.stripStartPath(contextPath, request.requestURI)
		val appName: String = UrlUtils.getFirstSegment(pathWithoutContext)

		val proxy = proxyProperties.proxies.find { it.appName == appName }
			?: throw ZuulException("Proxy Mapping Not Found", 404, "Could not find a proxy config for this endpoint")

		val token = serviceToServiceTokenProvider.getServiceToken(
			proxy.appName, proxy.appNamespace, proxy.appCluster
		)

		val veilederIdent = authContextHolder.requireNavIdent()

		ctx.addZuulRequestHeader(NAV_IDENT_HEADER, veilederIdent.get())
		ctx.addZuulRequestHeader(HttpHeaders.AUTHORIZATION, "Bearer $token")

		return null
	}

}
