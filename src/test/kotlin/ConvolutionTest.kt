import com.martmists.mlutils.compat.format.fromImage
import com.martmists.mlutils.compat.format.toImage
import com.martmists.mlutils.math.convolve
import com.martmists.ndarray.simd.F64Array
import java.io.File
import kotlin.test.Test

class ConvolutionTest {
    private val image = F64Array.fromImage(File(this::class.java.getResource("plush.png")!!.toURI())).slice(0, 3, axis=2)

    private fun F64Array.normalize(): F64Array {
        val range = this.min() to this.max()
        this -= range.first
        this /= range.second - range.first
        return this
    }

    @Test
    fun `Vertical Edge Detection, 5x5 kernel`() {
        val kernel = F64Array.ofRows(listOf(
            doubleArrayOf(-1.0, -1.0, -1.0, -1.0, -1.0),
            doubleArrayOf(-1.0, -1.0, -1.0, -1.0, -1.0),
            doubleArrayOf(0.0, 0.0, 0.0, 0.0, 0.0),
            doubleArrayOf(1.0, 1.0, 1.0, 1.0, 1.0),
            doubleArrayOf(1.0, 1.0, 1.0, 1.0, 1.0),
        ))

        val result = image.convolve(kernel).normalize()

        result.toImage(File("lets-plot-images/convolution-5x5-vertical.png"))
    }

    @Test
    fun `Horizontal Edge Detection, 5x5 kernel`() {
        val kernel = F64Array.ofRows(listOf(
            doubleArrayOf(-1.0, -1.0, 0.0, 1.0, 1.0),
            doubleArrayOf(-1.0, -1.0, 0.0, 1.0, 1.0),
            doubleArrayOf(-1.0, -1.0, 0.0, 1.0, 1.0),
            doubleArrayOf(-1.0, -1.0, 0.0, 1.0, 1.0),
            doubleArrayOf(-1.0, -1.0, 0.0, 1.0, 1.0),
        ))

        val result = image.convolve(kernel).normalize()

        result.toImage(File("lets-plot-images/convolution-5x5-horizontal.png"))
    }

    @Test
    fun `Blur, 5x5 kernel`() {
        val kernel = F64Array.ofRows(listOf(
            doubleArrayOf(1.0, 4.0, 6.0, 4.0, 1.0),
            doubleArrayOf(4.0, 16.0, 24.0, 16.0, 4.0),
            doubleArrayOf(6.0, 24.0, 36.0, 24.0, 6.0),
            doubleArrayOf(4.0, 16.0, 24.0, 16.0, 4.0),
            doubleArrayOf(1.0, 4.0, 6.0, 4.0, 1.0),
        ))

        val result = image.convolve(kernel).normalize()

        result.toImage(File("lets-plot-images/convolution-5x5-blur.png"))
    }
}
