package xyz.iterus.meband.extension

@ExperimentalUnsignedTypes // just to make it clear that the experimental unsigned types are used
fun ByteArray.toHexString() = asUByteArray().joinToString(" ") { "0x" + it.toString(16).uppercase().padStart(2, '0') }
