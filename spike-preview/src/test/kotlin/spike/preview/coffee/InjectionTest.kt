package spike.preview.coffee

import spike.preview.coffee.dagger.DaggerShelf
import kotlin.test.Test
import kotlin.test.assertContentEquals

class InjectionTest {

    @Test
    fun `spike matches dagger output`() {
        // ====
        // Neither dagger, nor spike, will show "pumping" as factory instancing is unbiased towards entrypoint/component
        // To show pumping "heater" would have had to be instanced as singleton
        // ====
        //val expected = listOf(
        //    "~ ~ ~ heating ~ ~ ~",
        //    "=> => pumping => =>",
        //    " [_]P coffee! [_]P "
        //)
        val spike = with(Shelf()) {
            coffeeMaker.brew()
            logger().collect()
        }
        val dagger = with(DaggerShelf.builder().build()) {
            coffeeMaker.brew()
            logger().collect()
        }
        assertContentEquals(dagger, spike)
    }

}