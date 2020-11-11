package org.example

object Curry {

    inline fun <A, B, C> with(crossinline fn: (A, B) -> C, a: A): (B) -> C = { b: B -> fn(a, b) }
    inline fun <A, B, C> all(crossinline fn: (A, B) -> C): (A) -> (B) -> C = { a: A -> { b: B -> fn(a, b) } }

}
