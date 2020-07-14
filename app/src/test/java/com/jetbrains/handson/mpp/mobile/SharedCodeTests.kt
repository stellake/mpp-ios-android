package com.jetbrains.handson.mpp.mobile

import kotlinx.coroutines.runBlocking
import org.junit.Test

import org.junit.Assert.*
class SharedCodeTests {
    @Test
    fun APITest(){
        val presenter=ApplicationPresenter()
        runBlocking{presenter.callOnTrainPage("CBG","KGX")}
    }
}