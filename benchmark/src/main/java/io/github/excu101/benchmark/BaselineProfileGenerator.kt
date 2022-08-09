package io.github.excu101.benchmark

import android.graphics.Point
import androidx.benchmark.macro.ExperimentalBaselineProfilesApi
import androidx.benchmark.macro.junit4.BaselineProfileRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.Until
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalBaselineProfilesApi
@RunWith(AndroidJUnit4::class)
class BaselineProfileGenerator {

    companion object {
        const val PACKAGE_NAME = "io.github.excu101.vortexfilemanager"
    }

    @get:Rule
    val baselineProfileRule = BaselineProfileRule()

    private lateinit var device: UiDevice

    @Before
    fun setUp() {
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        device = UiDevice.getInstance(instrumentation)
    }

    @Test
    fun startup() =
        baselineProfileRule.collectBaselineProfile(packageName = PACKAGE_NAME) {
            pressHome()
            startActivityAndWait()
        }
}