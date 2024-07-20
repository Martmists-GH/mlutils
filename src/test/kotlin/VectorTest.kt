import com.martmists.mlutils.math.eigen
import com.martmists.ndarray.simd.F64Array
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
