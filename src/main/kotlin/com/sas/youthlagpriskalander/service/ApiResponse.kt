package com.sas.youthlagpriskalander.service

import reactor.netty.udp.UdpInbound

data class ApiResponse(
    val outbound: Map<String, PriceInfo>,
    val inbound: Map<String, PriceInfo>,
    val offerId : String
)
