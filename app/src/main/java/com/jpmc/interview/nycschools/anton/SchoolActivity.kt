package com.jpmc.interview.nycschools.anton

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.jpmc.interview.nycschools.anton.schools.SchoolUi
import com.jpmc.interview.nycschools.anton.schools.SchoolViewModel
import com.jpmc.interview.nycschools.anton.ui.Loading
import com.jpmc.interview.nycschools.anton.ui.SchoolScores
import com.jpmc.interview.nycschools.anton.ui.SchoolSelection
import com.jpmc.interview.nycschools.anton.ui.theme.Android20230315AntonSpaansNYCSchoolsTheme

class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<SchoolViewModel> { ViewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Android20230315AntonSpaansNYCSchoolsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SchoolScreen(viewModel)
                }
            }
        }
    }

    companion object {
        private val ViewModelFactory = viewModelFactory {
            initializer {
                // Inject the app's [SchoolDomain] and the activity's [SavedStateHandle] in
                // the new [SchoolViewModel].
                SchoolViewModel(SchoolApplication.schoolDomain, createSavedStateHandle())
            }
        }
    }
}

@Composable
fun SchoolScreen(viewModel: SchoolViewModel) {
    val uiState = viewModel.schoolUi.collectAsState()
    val onClickListener = viewModel::onSchoolSelected

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 4.dp, vertical = 4.dp),
    ) {
        when (val ui = uiState.value) {
            is SchoolUi.Loading -> {
                Loading(labelId = ui.message, modifier = Modifier.fillMaxSize())
            }

            is SchoolUi.SchoolListUi -> {
                SchoolSelection(
                    labelId = ui.label,
                    schools = ui.schools,
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(fraction = 0.67f),
                    onSchoolClicked = onClickListener,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(ui.message),
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    textAlign = TextAlign.Center,
                )
            }

            is SchoolUi.SchoolWithScoresUi -> {
                SchoolSelection(
                    labelId = ui.label,
                    schools = ui.schools,
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(fraction = 0.67f),
                    onSchoolClicked = onClickListener,
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(ui.scoresLabel),
                    modifier = Modifier
                        .wrapContentHeight()
                        .fillMaxWidth(),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                )
                SchoolScores(
                    schoolName = ui.schoolName,
                    scores = ui.scores,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                )
            }
        }
    }
}
