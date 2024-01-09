package com.clean.mvvm.utils

import java.net.URLEncoder
import java.nio.charset.StandardCharsets

fun encodedString(str: String): String = URLEncoder.encode(str, StandardCharsets.UTF_8.toString())
