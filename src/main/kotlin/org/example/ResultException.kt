package org.example

import java.lang.RuntimeException

class ResultException(error: Result.Error<*>) : RuntimeException(error.description)