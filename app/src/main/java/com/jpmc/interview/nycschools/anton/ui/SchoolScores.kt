package com.jpmc.interview.nycschools.anton.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jpmc.interview.nycschools.anton.R
import com.jpmc.interview.nycschools.anton.schools.AverageScoresUi
import com.jpmc.interview.nycschools.anton.schools.Score
import com.jpmc.interview.nycschools.anton.schools.ScoreUi
import com.jpmc.interview.nycschools.anton.ui.theme.Android20230315AntonSpaansNYCSchoolsTheme

@Composable
fun SchoolScores(
    schoolName: String,
    scores: AverageScoresUi,
    modifier: Modifier = Modifier,
) {
    Card(
        border = BorderStroke(width = 1.dp, color = Color.LightGray),
    ) {

        Column(
            modifier = modifier
                .padding(horizontal = 12.dp, vertical = 8.dp)
                .fillMaxWidth()
                .wrapContentHeight(),
        ) {
            Text(
                text = schoolName,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
            )
            Row(modifier = Modifier.padding(top = 4.dp)) {
                Column {
                    Text(text = stringResource(scores.readingScore.label))
                    Text(text = stringResource(scores.writingScore.label))
                    Text(text = stringResource(scores.mathScore.label))
                }
                Column(modifier = Modifier.padding(start = 16.dp)) {
                    Text(text = scores.readingScore.score.value()?.toString() ?: "s")
                    Text(text = scores.writingScore.score.value()?.toString() ?: "s")
                    Text(text = scores.mathScore.score.value()?.toString() ?: "s")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SchoolScoresPreview() {
    Android20230315AntonSpaansNYCSchoolsTheme {
        SchoolScores(
            schoolName = "Selected School",
            scores = AverageScoresUi(
                readingScore = ScoreUi(label = R.string.sat_reading, score = Score("100")),
                writingScore = ScoreUi(label = R.string.sat_writing, score = Score("100")),
                mathScore = ScoreUi(label = R.string.sat_math, score = Score("s")),
            ),
            modifier = Modifier
        )
    }
}
