package org.example

class Applicative<A, B> (val a: A, val b: B) {
    inline fun <C> map(fn: (A, B) -> C): C = fn(a, b)

    inline fun <C> params(fn: A.(A) -> (B) -> C): C = a.fn(a)(b)

    operator fun component1() = a
    operator fun component2() = b
}