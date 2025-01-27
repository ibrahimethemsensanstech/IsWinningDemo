package com.sanstech.iswinning

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform