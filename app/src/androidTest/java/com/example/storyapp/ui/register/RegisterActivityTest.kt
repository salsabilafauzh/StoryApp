package com.example.storyapp.ui.register

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.idling.CountingIdlingResource
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.hasErrorText
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class RegisterActivityTest {
    private lateinit var countingIdlingResource: CountingIdlingResource

    @get:Rule
    var activityScenarioRule = ActivityScenarioRule(RegisterActivity::class.java)


    @Before
    fun setUp() {
        countingIdlingResource = CountingIdlingResource("RegisterIdlingResource")
        IdlingRegistry.getInstance().register(countingIdlingResource)
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(countingIdlingResource)
    }


    @Test
    fun registerInvalidInput() {
        val invalidEmail = "invalidemail"
        val invalidPassword = "123"
        val username = "iniusername"

        val editTextUsername = com.example.storyapp.R.id.ed_register_name
        val editTextEmail = com.example.storyapp.R.id.ed_register_email
        val editTextPassword = com.example.storyapp.R.id.ed_register_password
        val errorTextEmail = ApplicationProvider.getApplicationContext<Context>()
            .getString(com.example.storyapp.R.string.invalid_email)
        val errorTextPassword = ApplicationProvider.getApplicationContext<Context>()
            .getString(com.example.storyapp.R.string.invalid_password)

        clearClipboard()
        onView(withId(editTextUsername))
            .perform(typeText(username))
        hideKeyboard()
        onView(withId(editTextEmail))
            .perform(typeText(invalidEmail))
        hideKeyboard()
        onView(withId(editTextPassword))
            .perform(typeText(invalidPassword))
        hideKeyboard()

        onView(withId(editTextEmail))
            .check(matches(hasErrorText(errorTextEmail)))
        onView(withId(editTextPassword))
            .check(matches(hasErrorText(errorTextPassword)))
    }

    private fun clearClipboard() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val clipboard = appContext.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboard.setPrimaryClip(ClipData.newPlainText(null, ""))
    }

    private fun hideKeyboard() {
        onView(ViewMatchers.isRoot()).perform(ViewActions.closeSoftKeyboard())
    }
}

