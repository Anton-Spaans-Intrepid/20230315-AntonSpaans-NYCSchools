package com.jpmc.interview.nycschools.anton

import android.app.Application
import com.jpmc.interview.nycschools.anton.schools.SchoolDomain
import com.jpmc.interview.nycschools.anton.schools.SchoolService
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

/**
 * Application that has one screen that shows a list of NYC area schools and allows the user
 * to select a school, which causes the average SAT scores (reading, writing, math) to
 * be shown.
 *
 * This has been done:
 *    - Simple one activity/screen UI.
 *    - Happy paths only on UI so far.
 *    - Unit tests for SchoolDomain
 *
 * To be done:
 *    - User rail-road programming (Either) to handle errors.
 *    - Error paths on UI.
 *    - Add Unit tests for SchoolViewModel
 *
 * To be done optionally:
 *     - Add an Espresso/Compose test for at least one scenario.
 */
class SchoolApplication : Application() {

    /** The companion object of the app-context will handle dependency injection
     * bindings/provisions (manually).
     */
    companion object {

        /** Manually provide a singleton [SchoolDomain]. */
        val schoolDomain: SchoolDomain by lazy {
            SchoolDomain(schoolService)
        }

        private const val BaseUrl = "https://data.cityofnewyork.us/resource/"

        /** Manually provide a singleton [Converter.Factory]. */
        private val GsonFactory: Converter.Factory by lazy {
            GsonConverterFactory.create()
        }

        /** Manually create a provider/factory for [Retrofit] instance (for the [SchoolService]). */
        private val RetrofitFactory: (String, Converter.Factory) -> Retrofit = { baseUrl, factory ->
            Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(factory)
                .build()
        }

        /** Manually provide a singleton [SchoolService]. */
        private val schoolService : SchoolService by lazy {
            RetrofitFactory(BaseUrl, GsonFactory).create()
        }
    }
}
