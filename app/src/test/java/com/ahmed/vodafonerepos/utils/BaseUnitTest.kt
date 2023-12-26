package com.ahmed.vodafonerepos.utils

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Rule

open class BaseUnitTest {
    @get:Rule
    var mMainCoroutineScopeRule = MainCoroutineScopeRule()

    @get:Rule
    var mInstantTaskExecutorRule = InstantTaskExecutorRule()
}