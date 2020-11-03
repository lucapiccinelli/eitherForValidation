package org.example

sealed class Result<T>{
    data class Ok<T>(val value: T): Result<T>() {
        override fun <R> map(body: (T) -> R): Result<R> = Ok(body(value))
        override fun <R> flatMap(body: (T) -> Result<R>): Result<R> = body(value)
        override fun ifError(errorValue: T): T = value
        override fun ifError(errorHandler: (Error<T>) -> T): T = value
    }

    data class Error<T>(val description: String): Result<T>() {
        override fun <R> map(body: (T) -> R): Result<R> = Error(description)
        override fun <R> flatMap(body: (T) -> Result<R>): Result<R> = Error(description)
        override fun ifError(errorValue: T): T = errorValue
        override fun ifError(errorHandler: (Error<T>) -> T): T = errorHandler(this)
    }

    abstract fun <R> map(body: (T) -> R): Result<R>
    abstract fun <R> flatMap(body: (T) -> Result<R>): Result<R>
    abstract fun ifError(errorValue: T): T
    abstract fun ifError(errorHandler: (Error<T>) -> T): T
}