package no.nav.poao_openam_proxy.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "proxy")
data class ProxyProperties(
	var proxies: List<ProxyTarget> = emptyList()
)

data class ProxyTarget(
	var appName: String = "",
	var appNamespace: String = "",
	var appCluster: String = ""
)
