package com.martmists.mlutils.compat.format

import com.sksamuel.scrimage.ImmutableImage
import com.sksamuel.scrimage.nio.JpegWriter
import com.sksamuel.scrimage.nio.PngWriter
import com.sksamuel.scrimage.webp.WebpWriter
import org.jetbrains.bio.viktor.F64Array
import java.io.File

/**
 * Reads an image file into an [F64Array].
 *
 * @param file The image file to read.
 * @return The [F64Array] read from the image file.
 */
fun F64Array.Companion.fromImage(file: File): F64Array {
    val img = ImmutableImage.loader().fromFile(file)
    val w = img.width
    val h = img.height
    val arr = F64Array(w, h, 4)
    for (y in 0 until h) {
        for (x in 0 until w) {
            val px = img.pixel(x, y)
            arr[x, y, 0] = px.red().toDouble() / 255
            arr[x, y, 1] = px.green().toDouble() / 255
            arr[x, y, 2] = px.blue().toDouble() / 255
            arr[x, y, 3] = px.alpha().toDouble() / 255
        }
    }
    return arr
}

/**
 * Writes an [F64Array] to an image file.
 *
 * @param file The image file to write to. Valid file extensions are `.png`, `.jpg`, and `.webp`.
 */
fun F64Array.toImage(file: File) {
    require(shape.size == 3) { "Expected 3D array, got ${shape.size}D" }
    require(shape[2] == 3 || shape[2] == 4) { "Expected 3 or 4 channels, got ${shape[2]} channels" }
    val w = shape[0]
    val h = shape[1]
    val img = ImmutableImage.create(w, h)
    for (y in 0 until h) {
        for (x in 0 until w) {
            var px = img.pixel(x, y)
            px = px.red((this[x, y, 0] * 255).toInt())
            px = px.green((this[x, y, 1] * 255).toInt())
            px = px.blue((this[x, y, 2] * 255).toInt())
            if (shape[2] == 4) {
                px = px.alpha((this[x, y, 3] * 255).toInt())
            } else {
                px = px.alpha(255)
            }
            img.setPixel(px)
        }
    }

    val writer = when (file.extension.lowercase()) {
        "png" -> PngWriter.MinCompression
        "jpg", "jpeg" -> JpegWriter.Default
        "webp" -> WebpWriter.DEFAULT
        else -> throw IllegalArgumentException("Unsupported image format: ${file.extension}")
    }

    file.writeBytes(img.bytes(writer))
}
