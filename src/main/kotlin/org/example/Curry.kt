package org.example

class Curry<A, B, C>(private val fn: (A, B) -> C) {

    fun to(a: A): (B) -> C = { b: B -> fn(a, b) }
    fun to(): (A) -> (B) -> C = { a: A -> { b: B -> fn(a, b) } }

}
