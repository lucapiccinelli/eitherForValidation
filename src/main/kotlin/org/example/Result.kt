package org.example

import java.lang.RuntimeException

sealed class Result<T>{
    data class Ok<T>(val value: T): Result<T>()
    data class Error<T>(val description: String): Result<T>()

    inline fun <R> map(body: (T) -> R): Result<R> = when(this){
        is Ok -> Ok(body(this.value))
        is Error -> Error(this.description)
    }

    inline fun <R> flatMap(body: (T) -> Result<R>): Result<R> = when(this){
        is Ok -> body(this.value)
        is Error -> Error(this.description)
    }

    inline fun ifError(errorHandler: (Error<T>) -> T): T = when(this){
        is Ok -> value
        is Error -> errorHandler(this)
    }

    fun ifError(errorValue: T): T = ifError { errorValue }

    fun get(): T = ifError{ error ->  throw ResultException(error) }
}

fun <X,Y,R> map2(r1: Result<X>, r2: Result<Y>, body: (X, Y) -> R): Result<R> =
    r1.flatMap { x1 -> r2.map { x2-> body(x1, x2) } }

fun <X,Y,Z,R> map3(r1: Result<X>, r2: Result<Y>, r3: Result<Z>, body: (X, Y, Z) -> R): Result<R> =
    r1.flatMap { x1 -> r2.flatMap { x2 -> r3.map { x3 -> body(x1, x2, x3) } } }

fun <X,Y,Z,K,R> map4(r1: Result<X>, r2: Result<Y>, r3: Result<Z>, r4: Result<K>, body: (X, Y, Z, K) -> R): Result<R> =
    r1.flatMap { x1 -> r2.flatMap { x2 -> r3.flatMap { x3 -> r4.map { x4 -> body(x1, x2, x3, x4) } } } }

class ResultException(error: Result.Error<*>) : RuntimeException(error.description)
