package org.example

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ResultTests {

    @Test
    internal fun `GIVEN ok WHEN i request the result THEN i get the ok value`() {
        val result = Result.Ok("banana")
        val resultValue: String = result.ifError("error")

        assertThat(resultValue).isEqualTo("banana")
    }

    @Test
    internal fun `GIVEN Error WHEN i request the result THEN i get the Error value`() {
        val result = Result.Error<String>("banana")
        val resultValue: String = result.ifError("error")

        assertThat(resultValue).isEqualTo("error")
    }

    @Test
    internal fun `GIVEN Ok WHEN i request the result or handle the error THEN i get ok value`() {
        val result = Result.Ok("banana")
        val resultValue: String = result.ifError { it.description }

        assertThat(resultValue).isEqualTo("banana")
    }

    @Test
    internal fun `GIVEN Ok WHEN i map the value and THEN request the result or handle the error THEN i get mapped value`() {
        val result = Result.Ok("0")
        val resultValue = result
            .map { it.toInt() }
            .ifError(-1)

        assertThat(resultValue).isEqualTo(0)
    }

    @Test
    internal fun `GIVEN Error WHEN i map the value and THEN request the result or handle the error THEN i get error value`() {
        val result = Result.Error<String>("0")
        val resultValue = result
            .map { it.toInt() }
            .ifError(-1)

        assertThat(resultValue).isEqualTo(-1)
    }

    @Test
    internal fun `GIVEN Error WHEN i request the result or handle the error THEN i get the handled value`() {
        val result = Result.Error<String>("error")
        val resultValue: String = result.ifError { it.description }

        assertThat(resultValue).isEqualTo("error")
    }

    @Test
    internal fun `GIVEN ok WHEN i flatMap the value and THEN request the result or handle the error THEN i the ok value`() {
        val result = Result.Ok("0")
        val resultValue = result
            .flatMap { Result.Ok(it.toInt()) }
            .ifError(-1)

        assertThat(resultValue).isEqualTo(0)
    }

    @Test
    internal fun `GIVEN Error WHEN i flatMap the value and THEN request the result or handle the error THEN i get the handled value`() {
        val result = Result.Error<String>("error")
        val resultValue = result
            .flatMap { Result.Ok(it.toInt()) }
            .ifError(-1)

        assertThat(resultValue).isEqualTo(-1)
    }

    @Test
    internal fun `GIVEN 2 Oks WHEN i combine and apply a function them THEN i get ok value`() {
        val resultValue = map2(Result.Ok(1), Result.Ok("2")){ x, s ->
            x + s.toInt()
        }.ifError(0)

        assertThat(resultValue).isEqualTo(3)
    }

    @Test
    internal fun `GIVEN 2 Errors WHEN i combine and apply a function them THEN i get the error value`() {
        val resultValue = map2(Result.Ok(1), Result.Error<String>("some error")){ x, s ->
            x + s.toInt()
        }.ifError(0)

        assertThat(resultValue).isEqualTo(0)
    }

    @Test
    internal fun `GIVEN a function inside a Result and an ok Result WHEN i apply the fn THEN i get the ok value`() {
        val resultFn: Result<(Int) -> String> = Result.pure { x: Int -> (x + 1).toString() }

        val x = Result.pure(1)
        val y = x.ap(resultFn).ifError("")

        assertThat(y).isEqualTo("2")
    }

    @Test
    internal fun `GIVEN a fn and two ok results WHEN i apply the fn THEN i get the ok value`() {
        val fn = { x: Int, y: String -> (x + y.toInt()).toString() }
        val x = Result.Ok(1)
        val y = Result.Ok("2")

        val z = y.ap(x.map { Curry(fn).with(it) }).ifError("")

        assertThat(z).isEqualTo("3")
    }
}