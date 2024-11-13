package com.ames.fr

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform