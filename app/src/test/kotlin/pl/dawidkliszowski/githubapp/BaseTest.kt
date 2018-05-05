package pl.dawidkliszowski.githubapp

import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Ignore
import java.util.concurrent.TimeUnit

@Ignore
open class BaseTest {

    companion object {
        protected val testSchedulersInitializer = RxTestSchedulersInitializer()

        @BeforeClass
        @JvmStatic
        fun setUpClass() {
            testSchedulersInitializer.initTestSchedulers()
        }

        @AfterClass
        @JvmStatic
        fun finishClass() {
            testSchedulersInitializer.reset()
        }
    }

    protected fun advanceRxTime(timeMillis: Long) {
        testSchedulersInitializer.testScheduler
                .advanceTimeBy(timeMillis, TimeUnit.MILLISECONDS)
    }
}