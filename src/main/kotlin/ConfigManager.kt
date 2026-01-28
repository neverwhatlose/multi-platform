package org.neverwhatlose

import org.jetbrains.annotations.Contract
import org.yaml.snakeyaml.DumperOptions
import org.yaml.snakeyaml.Yaml
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.locks.ReentrantLock
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read

// todo: convert ConfigManager from singleton to instantiable class to each instance per config file
// note: maybe it is reasonable to support nullable values as a result of get() or as an argument for put() functions
object ConfigManager {
    val lock = ReentrantReadWriteLock()
    private val yaml = Yaml(
        DumperOptions().apply {
            defaultFlowStyle = DumperOptions.FlowStyle.BLOCK
            isPrettyFlow = true
            indent = 4
        }
    )
    private val _data: ConcurrentHashMap<String, Any> = loadData()
    val data: Map<String, Any> get() = _data.toMap()

    @Throws(IllegalStateException::class)
    inline fun <reified T> get(path: String, default: T? = null): T = lock.read {
        val value = path.split(".").fold(data as? Any) { acc, key -> if (acc is Map<*, *>) acc[key] else null }
        return value as? T ?: default
            ?: throw IllegalStateException("Value at path $path is not of type ${T::class.simpleName} or does not exist!")
    }

    @Synchronized
    @Suppress("UNCHECKED_CAST")
    @Throws(IllegalStateException::class)
    fun <T> put(path: String, value: T) {
        val keys = path.split(".")
        val lastKey = keys.last()

        val targetMap = keys.dropLast(1).fold(_data) { acc, key ->
            when (val next = acc[key]) {
                null -> ConcurrentHashMap<String, Any>().also { acc[key] = it }
                is ConcurrentHashMap<*, *> -> next as ConcurrentHashMap<String, Any>
                else -> throw IllegalStateException("Value at the key `$key` is not a MutableMap")
            }
        }

        targetMap[lastKey] = value as Any
        saveData()
    }

    private fun saveData(file: File = File("config.yaml")) = file.writeText(yaml.dump(_data))

    @Suppress("UNCHECKED_CAST")
    @Throws(IllegalStateException::class)
    private fun loadData(file: File = File("config.yaml")): ConcurrentHashMap<String, Any> {
        if (!file.exists()) {
            when (val resourceStream = this::class.java.getResourceAsStream("config.yaml")) {
                null -> if (!file.createNewFile()) throw IOException("Could not create config.yaml")
                else -> resourceStream.use { Files.copy(it, file.toPath(), StandardCopyOption.REPLACE_EXISTING) }
            }
        }

        val obj = yaml.load<Any>(file.readText()) as? Map<String, Any>
            ?: throw IllegalStateException("Invalid YAML file!")
        return ConcurrentHashMap(obj)
    }
}