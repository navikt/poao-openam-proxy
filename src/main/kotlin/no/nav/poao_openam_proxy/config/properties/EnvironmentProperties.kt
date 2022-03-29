package no.nav.poao_openam_proxy.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "app.env")
data class EnvironmentProperties (
	var mulighetsrommetApiUrl: String = "",
	var openAmDiscoveryUrl: String = "",
	var veilarbloginOpenAmClientId: String = "",
)
