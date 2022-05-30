package com.jamesclark.android.androidlibraryexample

import java.io.InputStreamReader

class MockResponseFileReader(path: String) {
    val content: String

    init {
        InputStreamReader(this.javaClass.classLoader!!.getResourceAsStream(path)).use { reader ->
            content = reader.readText()
        }
    }
}