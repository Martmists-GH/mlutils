import com.martmists.mlutils.compat.format.fromCSV
import com.martmists.mlutils.compat.format.fromImage
import com.martmists.mlutils.compat.jvm.ofRows
import com.martmists.mlutils.compat.jvm.pixels
import com.martmists.mlutils.compat.jvm.rows
import com.martmists.mlutils.convert.hsvToRgb
import com.martmists.mlutils.convert.rgbToHsv
import com.martmists.mlutils.math.Cluster
import com.martmists.mlutils.math.clusterKMeans
import com.martmists.mlutils.math.eigen
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
import kotlin.test.assertEquals


class VectorTest {
    @Test
    fun `Test EigenValues and EigenVectors`() {
        val sample = F64Array.ofRows(listOf(
            doubleArrayOf(4.0, 8.0),
            doubleArrayOf(10.0, 2.0),
        ))

        val (eigenValues, eigenVectors) = sample.eigen()

        println(eigenValues)
        println(eigenVectors)

        assertEquals(12.0, eigenValues[0], 0.01)
        assertEquals(-6.0, eigenValues[1], 0.01)
        assertEquals(0.707, eigenVectors[0, 0], 0.01)
        assertEquals(-0.707, eigenVectors[0, 1], 0.01)
        assertEquals(0.707, eigenVectors[1, 0], 0.01)
        assertEquals(0.707, eigenVectors[1, 1], 0.01)
    }
}
