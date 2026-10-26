package com.soul.neurokaraoke

import com.soul.neurokaraoke.data.api.EndpointRouter
import org.junit.Assert.assertEquals
import org.junit.Test

class EndpointRouterTest {
    @Test fun routesServicesWithoutChangingSignedPaths() {
        for (service in listOf("api", "idk", "storage", "images", "socket")) {
            val source = "https://$service.neurokaraoke.com:8443/a%20b%2Fc?sig=a%2Bb#part"
            val target = source.replace(".com:", ".com.cn:")
            assertEquals(target, EndpointRouter.route(source, true))
            assertEquals(target, EndpointRouter.route(target, true))
            assertEquals(source, EndpointRouter.route(target, false))
        }
        assertEquals("wss://socket.neurokaraoke.com.cn/live", EndpointRouter.route("wss://socket.neurokaraoke.com/live", true))
    }

    @Test fun preservesUnescapedSpacesInMediaUrls() {
        assertEquals("https://storage.neurokaraoke.com.cn/audio/a b.mp3", EndpointRouter.route("https://storage.neurokaraoke.com/audio/a b.mp3", true))
    }

    @Test fun leavesOtherHostsAndLocalFilesAlone() {
        for (url in listOf("https://neurokaraoke.com/", "https://cn.neurokaraoke.com/", "https://discord.com/", "https://api.neurokaraoke.com.evil.test/", "file:///audio.mp3", "not a URL")) {
            assertEquals(url, EndpointRouter.route(url, true))
        }
    }
}
