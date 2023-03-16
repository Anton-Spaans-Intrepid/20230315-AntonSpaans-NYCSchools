package com.jpmc.interview.nycschools.anton.schools

import androidx.annotation.StringRes
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.Either
import arrow.core.continuations.either
import arrow.core.getOrHandle
import com.jpmc.interview.nycschools.anton.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * This is the ViewModel for the SchoolActivity.
 *
 * @property schoolDomain The domain that will be injected from Activity
 * @property savedState State that has been saved after a config change or a process death
 */
class SchoolViewModel(
    private val schoolDomain: SchoolDomain,
    private val savedState: SavedStateHandle = SavedStateHandle()
) : ViewModel() {

    private val _schools: MutableStateFlow<SchoolUi> = MutableStateFlow(SchoolUi.Loading())

    /**
     * Stream of [SchoolUi] instances describing on how the UI should be rendered.
     * Collect from this [StateFlow] to receive ongoing UI changes.
     */
    val schoolUi: StateFlow<SchoolUi> = _schools

    /** Optimization: Caches [Schools][School] loaded from the domain and remote service. */
    private var loadedSchools: List<School>? = null

    init {
        showScreen()
    }

    /**
     * Causes the [schoolUi] to emit a new [SchoolUi] instance based on the provided [school].
     *
     * Call this method when the user has selected a school whose average SAT scores should be
     * shown next.
     */
    fun onSchoolSelected(school: SchoolItemUi) {
        savedState[KEY_ID] = school.id.id
        savedState[KEY_NAME] = school.name
        showScreen()
    }

    /** Causes [schoolUi] to emit the list of schools and optionally the average SAT scores. */
    private fun showScreen() {
        if (savedState.contains(KEY_ID) && savedState.contains(KEY_NAME)) {
            showScores(id = SchoolId(savedState[KEY_ID]!!), name = savedState[KEY_NAME]!!)
        } else {
            showSchools()
        }
    }

    /** Causes [schoolUi] to include the list of schools. */
    private fun showSchools() {
        viewModelScope.launch {
            delay(3000) // fake a delay in loading data...
            _schools.value = either {
                SchoolUi.SchoolListUi(schools = loadSchools().bind())
            }.getOrHandle(SchoolError::toErrorUi)
        }
    }

    /** Causes [schoolUi] to include the average SAT scores. */
    private fun showScores(id: SchoolId, name: String) {
        viewModelScope.launch {
            _schools.value = either {
                val schoolList = loadSchools().bind()

                _schools.value = SchoolUi.SchoolListUi(
                    schools = schoolList,
                    message = R.string.loading_scores
                )

                delay(1000) // fake a delay in loading data...

                loadScores(id, name).getOrHandle { error ->
                    SchoolUi.SchoolListUi(
                        schools = schoolList,
                        message = error.getMessage()
                    )
                }
            }.getOrHandle(SchoolError::toErrorUi)
        }
    }

    /** Returns a list of [SchoolItemUi], each representing a NYC area school. */
    private suspend fun loadSchools(): Either<SchoolError, List<SchoolItemUi>> {
        return either {
            val schools = loadedSchools ?: schoolDomain.getSchools().bind()
                .sortedBy(School::name)
                .also { loadedSchools = it }

            val id = savedState.get<String>(KEY_ID)?.let(::SchoolId)
            schools.map {
                SchoolItemUi(
                    id = it.id,
                    name = it.name,
                    isSelected = it.id == id
                )
            }
        }
    }

    /** Returns the average SAT scores for the school with the provided [id]. */
    private suspend fun loadScores(
        id: SchoolId,
        name: String
    ): Either<SchoolError, SchoolUi.SchoolWithScoresUi> {
        return either {
            schoolDomain.getAverageScores(id).bind().let {
                SchoolUi.SchoolWithScoresUi(
                    schools = loadSchools().bind(),
                    schoolName = name,
                    scores = AverageScoresUi(
                        readingScore = ScoreUi(R.string.sat_reading, it.reading),
                        writingScore = ScoreUi(R.string.sat_writing, it.writing),
                        mathScore = ScoreUi(R.string.sat_math, it.math),
                    )
                )
            }
        }
    }

    companion object {
        private const val KEY_ID = "id"
        private const val KEY_NAME = "name"
    }
}

private fun SchoolError.toErrorUi(): SchoolUi.ErrorUi = SchoolUi.ErrorUi(getMessage())

private fun SchoolError.getMessage() = when (this) {
    is SchoolError.ServiceError -> R.string.service_error
    else -> R.string.network_error
}

/**
 * Represents a [School] on the UI.
 *
 * @property id The id of the school entity
 * @property name The name of the school
 * @property isSelected Determines if the user has this school selected in the list of schools.
 */
data class SchoolItemUi(
    val id: SchoolId,
    val name: String,
    val isSelected: Boolean,
)

/**
 * Represents a [Score] on the UI.
 *
 * @property label Describes the type of score (reading, writing or math)
 * @property score The score-value
 */
data class ScoreUi(
    @StringRes val label: Int,
    val score: Score,
)

/**
 * Represents the 3 average SAT scores for a school.
 *
 * @property readingScore The average reading score
 * @property writingScore The average writing score
 * @property mathScore The average math score
 */
data class AverageScoresUi(
    val readingScore: ScoreUi,
    val writingScore: ScoreUi,
    val mathScore: ScoreUi,
)

/** This class defines the entire UI state that is emitted by this view-model. */
sealed class SchoolUi {

    /** State: The list of available schools is still being loaded. */
    data class Loading(@StringRes val message: Int = R.string.loading_schools) : SchoolUi()

    /**
     * State: The List of [schools] is to be shown. The list has a [label], and a [message] is
     * shown below the list.
     */
    data class SchoolListUi(
        @StringRes val label: Int = R.string.schools,
        val schools: List<SchoolItemUi>,
        @StringRes val message: Int = R.string.no_school_selected
    ) : SchoolUi()

    /**
     * State: The List of [schools] is to be shown. The list has a [label], and below the list
     * the average SAT [scores] will be shown for the schools with name [schoolName].
     */
    data class SchoolWithScoresUi(
        @StringRes val label: Int = R.string.schools,
        val schools: List<SchoolItemUi>,
        @StringRes val scoresLabel: Int = R.string.average_scores,
        val schoolName: String,
        val scores: AverageScoresUi
    ) : SchoolUi()

    /** State: Show just an error message on the screen. */
    data class ErrorUi(@StringRes val message: Int = R.string.network_error) : SchoolUi()
}
