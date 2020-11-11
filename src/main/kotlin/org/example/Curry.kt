package org.example

class Curry<A, B, C>(private val fn: (A, B) -> C) {

    fun with(a: A): (B) -> C = { b: B -> fn(a, b) }
    fun all(): (A) -> (B) -> C = { a: A -> { b: B -> fn(a, b) } }

}
