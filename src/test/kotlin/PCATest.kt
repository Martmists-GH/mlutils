import com.martmists.mlutils.compat.format.fromCSV
import com.martmists.mlutils.math.pca
import com.martmists.ndarray.simd.F64Array
import com.martmists.ndarray.simd.F64FlatArray
import org.jetbrains.kotlinx.kandy.dsl.plot
import org.jetbrains.kotlinx.kandy.letsplot.export.save
import org.jetbrains.kotlinx.kandy.letsplot.layers.points
import java.io.File
import kotlin.test.Test


class PCATest {
    // Trim vectors to 2D
    private val vectors = F64Array.fromCSV(File(this::class.java.getResource("hw3-data.csv")!!.toURI())).along(0).map { it.flatten() }.toList()

    private fun plot(points: List<F64FlatArray>) = plot {
        points {
            x(points.map { it[0] })
            y(points.map { it[1] })
        }
    }

    @Test
    fun `PCA 2, Vector Data`() {
        val clusters = pca(2, vectors)

        plot(clusters).save("pca-2-vectors.png")
    }
}
