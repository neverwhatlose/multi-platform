package org.neverwhatlose

object Main {
    @JvmStatic
    fun main(args: Array<String>) {
        Thread.setDefaultUncaughtExceptionHandler { t, e ->}
    }
}