package org.example

class Ap<A, B> (val a: A, val b: B) {
    inline fun <C> map(body: (A, B) -> C): C = body(a, b)

    inline fun <C> params(fn: A.(A) -> (B) -> C): C = a.fn(a)(b)

    operator fun component1() = a
    operator fun component2() = b
}