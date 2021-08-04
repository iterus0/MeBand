package xyz.iterus.meband

class Greeting {
    fun greeting(): String {
        return "Hello, ${Platform().platform}!"
    }
}