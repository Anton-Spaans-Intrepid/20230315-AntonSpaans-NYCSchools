package com.jpmc.interview.nycschools.anton.schools

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SchoolDomainTest {

    private val fakeService = object : SchoolService {
        override suspend fun getSchools(): List<School> = EXPECTED_SCHOOLS
        override suspend fun getSatScores(): List<AverageScores> = EXPECTED_SCORES
    }

    private val schoolDomain = SchoolDomain(fakeService)

    @Test
    fun `getSchools should return the shools obtained from the SchoolService`() = runTest {
        val results = schoolDomain.getSchools()

        assertEquals(EXPECTED_SCHOOLS, results)
    }

    @Test
    fun `getAverageScores should return the averages scores of the provided school`() = runTest {
        val results = schoolDomain.getAverageScores(SchoolId("97fdsaj"))

        assertEquals(EXPECTED_SCORES[2], results)
    }

    companion object {
        private val EXPECTED_SCHOOLS = listOf(
            School(SchoolId("34218fdsa9"), "Z School 1"),
            School(SchoolId("97fdsaj"), "S School 2"),
            School(SchoolId("w3"), "A School 3"),
        )

        private val EXPECTED_SCORES = listOf(
            AverageScores(
                id = SchoolId("34218fdsa9"),
                math = Score("233"),
                reading = Score("400"),
                writing = Score("34")
            ),
            AverageScores(
                id = SchoolId("w3"),
                math = Score("s"),
                reading = Score("s"),
                writing = Score("s")
            ),
            AverageScores(
                id = SchoolId("97fdsaj"),
                math = Score("124"),
                reading = Score("23"),
                writing = Score("333")
            )
        )
    }
}
