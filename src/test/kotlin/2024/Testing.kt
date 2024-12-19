@file:Suppress("PackageName")
package `2024`

import TestBase
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Timeout
import java.util.concurrent.TimeUnit

class Testing : TestBase("2024") {
    @Test
    @Timeout(10, unit = TimeUnit.SECONDS)
    fun day1Sample() {
        testSample(1, 11, null)
    }

    @Test
    @Timeout(10, unit = TimeUnit.SECONDS)
    fun day1() {
        test(1, -1, -1)
    }
} 