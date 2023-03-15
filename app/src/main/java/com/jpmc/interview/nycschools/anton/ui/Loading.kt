package com.jpmc.interview.nycschools.anton.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jpmc.interview.nycschools.anton.R
import com.jpmc.interview.nycschools.anton.ui.theme.Android20230315AntonSpaansNYCSchoolsTheme

/** Shows a loading spinner with a [label][labelId] */
@Composable
fun Loading(@StringRes labelId: Int, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .wrapContentHeight()
            .wrapContentWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CircularProgressIndicator()
        Text(stringResource(labelId), modifier = Modifier.padding(start = 4.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun LoadingPreview() {
    Android20230315AntonSpaansNYCSchoolsTheme {
        Loading(labelId = R.string.loading_schools)
    }
}
