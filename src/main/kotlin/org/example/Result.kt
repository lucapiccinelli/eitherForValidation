package org.example

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

    fun ifError(errorValue: T): T = when(this){
        is Ok -> value
        is Error -> errorValue
    }

    inline fun ifError(errorHandler: (Error<T>) -> T): T = when(this){
        is Ok -> value
        is Error -> errorHandler(this)
    }
}