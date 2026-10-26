package com.soul.neurokaraoke.data.api

import java.net.URI
import java.net.URL

/** Routes only Neuro Karaoke service hosts; preserves signed paths and queries verbatim. */
object EndpointRouter {
    @Volatile var useChinaEndpoints: Boolean = false

    fun route(value: String, china: Boolean = useChinaEndpoints): String {
        val uri = try { URI(value.replace(" ", "%20")) } catch (_: Exception) { return value }
        if (uri.scheme !in listOf("https", "http", "wss", "ws")) return value
        val host = uri.host ?: return value
        val globalSuffix = ".neurokaraoke.com"
        val chinaSuffix = ".neurokaraoke.com.cn"
        val globalHost = if (host.endsWith(chinaSuffix, ignoreCase = true)) host.dropLast(3) else host
        if (!globalHost.endsWith(globalSuffix, ignoreCase = true)) return value
        val service = globalHost.dropLast(globalSuffix.length).lowercase()
        if (service !in setOf("api", "idk", "storage", "images", "socket")) return value
        val target = globalHost + if (china) ".cn" else ""
        if (target == host) return value
        val authority = uri.rawAuthority
        val hostStart = authority.lastIndexOf('@') + 1
        val newAuthority = authority.substring(0, hostStart) + target + authority.substring(hostStart + host.length)
        val start = value.indexOf("://") + 3
        return value.substring(0, start) + newAuthority + value.substring(start + authority.length)
    }

    fun url(value: String): URL = URL(route(value))
}
