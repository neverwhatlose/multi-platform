package org.nwtls.app

import java.util.concurrent.atomic.AtomicReference
import javax.swing.text.DefaultCaret

val configRef = AtomicReference<Map<String, Any>>(
    mapOf(
        "a" to mapOf("b" to 1)
    )
) // "a": { "b": 1 }

fun main() {
    val oldConf = configRef.get() // snapshot: { "a": { "b": 1 }}
    val oldA = oldConf["a"] as Map<*, *> // { "b": 1 }
    println(oldA) // > {b=1}
    val newA = oldA + ("b" to 2) // { "b": 2 }
    println(newA) // > {b=2}
    val newConfig = oldConf + ("a" to newA) // { "a": { "b": 2 }}
    configRef.compareAndSet(oldConf, newConfig) // { "a": { "b": 2 }}
    println(oldConf["a"]) // > {b=1}
    println(configRef.get()["a"]) // > {b=2}

    // test #2: getting value by provided path

//    println(get<Int>("a.b"))
//    println(get<String>("a.b"))
//    println(get<Int>("a.c"))
//    println(getOrFail<Int>("a.b"))
//    println(getOrFail<Int>("a.c"))
//    println(getOrFail<String>("a.b"))
}

inline fun <reified T> get(path: String, default: T? = null): T? {
    val oldConf = configRef.get()
    val value = path.split(".").fold(oldConf as? Any) { acc, key ->
        if (acc is Map<*, *>) return@fold acc[key] else null
    }
    return value as? T ?: default
}

@Throws(IllegalStateException::class)
inline fun <reified T> getOrFail(path: String, default: T? = null): T = get<T>(path, default)
        ?: throw IllegalArgumentException("Value at path '$path' is missing or is not of type ${T::class.simpleName}")
