import com.martmists.mlutils.compat.format.fromCSV
import com.martmists.mlutils.compat.format.fromImage
import com.martmists.mlutils.compat.jvm.pixels
import com.martmists.mlutils.compat.jvm.rows
import com.martmists.mlutils.convert.hsvToRgb
import com.martmists.mlutils.convert.rgbToHsv
import com.martmists.mlutils.math.Cluster
import com.martmists.mlutils.math.clusterKMeans
import com.martmists.mlutils.math.pca
import org.jetbrains.bio.viktor.F64Array
import org.jetbrains.bio.viktor.F64FlatArray
import org.jetbrains.kotlinx.kandy.dsl.categorical
import org.jetbrains.kotlinx.kandy.dsl.plot
import org.jetbrains.kotlinx.kandy.ir.Plot
import org.jetbrains.kotlinx.kandy.letsplot.export.save
import org.jetbrains.kotlinx.kandy.letsplot.feature.Position
import org.jetbrains.kotlinx.kandy.letsplot.feature.layout
import org.jetbrains.kotlinx.kandy.letsplot.feature.position
import org.jetbrains.kotlinx.kandy.letsplot.layers.bars
import org.jetbrains.kotlinx.kandy.letsplot.layers.points
import org.jetbrains.kotlinx.kandy.letsplot.settings.Symbol
import org.jetbrains.kotlinx.kandy.letsplot.style.Style
import org.jetbrains.kotlinx.kandy.util.color.Color
import java.io.File
import kotlin.math.roundToInt
import kotlin.test.Test


class PCATest {
    // Trim vectors to 2D
    private val vectors = F64Array.fromCSV(File(this::class.java.getResource("hw3-data.csv")!!.toURI())).rows()

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
