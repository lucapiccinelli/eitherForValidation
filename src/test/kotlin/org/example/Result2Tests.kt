import org.assertj.core.api.Assertions.assertThat
import org.example.Result2
import org.example.ap
import org.example.curry
import org.example.map
import org.junit.jupiter.api.Test

class Result2Tests {

    @Test
    internal fun name() {
        val errors = {x: String, y: String, z: Int, h: Int -> }.curry()
            .map(Result2.Error("error1"))
            .ap(Result2.Error("error2"))
            .ap(Result2.Ok(1))
            .ap(Result2.Error("error4"))

        assertThat(errors.toString()).isEqualTo("error1,error2,error4")
    }
}