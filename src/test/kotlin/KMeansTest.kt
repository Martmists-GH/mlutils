import com.martmists.mlutils.compat.format.fromCSV
import com.martmists.mlutils.compat.format.fromImage
import com.martmists.mlutils.compat.jvm.pixels
import com.martmists.mlutils.compat.jvm.rows
import com.martmists.mlutils.convert.hsvToRgb
import com.martmists.mlutils.convert.rgbToHsv
import com.martmists.mlutils.math.Cluster
import com.martmists.mlutils.math.clusterKMeans
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


class KMeansTest {
    // Trim vectors to 2D
    private val vectors = F64Array.fromCSV(File(this::class.java.getResource("hw3-data.csv")!!.toURI())).slice(0, 2, axis = 1).rows()
    // Drop last column, which is the alpha channel
    private val image = F64Array.fromImage(File(this::class.java.getResource("plush.png")!!.toURI())).slice(0, 3, axis=2).pixels().map { it.rgbToHsv() }

    private fun plot(clusters: List<Cluster>) = plot {
        points {
            x(clusters.flatMap { it.map { vec -> vec[0] } })
            y(clusters.flatMap { it.map { vec -> vec[1] } })
            color(clusters.flatMapIndexed { i, cluster -> cluster.map { i } })
            symbol(clusters.flatMapIndexed { i, cluster -> cluster.map { i } })
        }
    }

    private fun plot(colors: Map<F64FlatArray, Int>) = plot {
        bars {
            x((0 until colors.size))
            y(colors.values.toList())
            borderLine.color = Color.BLACK
            fillColor((0 until colors.size)) {
                scale = categorical(colors.keys.map {
                    val r = (it[0] * 255).roundToInt()
                    val g = (it[1] * 255).roundToInt()
                    val b = (it[2] * 255).roundToInt()
                    Color.rgb(r, g, b)
                })
            }
        }
    }

    @Test
    fun `K = 3, Vector Data`() {
        val clusters = clusterKMeans(3, vectors)

        plot(clusters.values.toList()).save("kmeans-3-vectors.png")
    }

    @Test
    fun `K = 5, Vector Data`() {
        val clusters = clusterKMeans(5, vectors)
        plot(clusters.values.toList()).save("kmeans-5-vectors.png")
    }

    @Test
    fun `K = 5, Image Data`() {
        val clusters = clusterKMeans(5, image)
        val meanColors = clusters.entries.associate { (hsv, cluster) -> hsv.hsvToRgb() to cluster.size }
        plot(meanColors).save("kmeans-5-image.png")
    }

    @Test
    fun `K = 10, Image Data`() {
        val clusters = clusterKMeans(10, image)
        val meanColors = clusters.entries.associate { (hsv, cluster) -> hsv.hsvToRgb() to cluster.size }
        plot(meanColors).save("kmeans-10-image.png")
    }
}
