package com.jpmc.interview.nycschools.anton.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jpmc.interview.nycschools.anton.R
import com.jpmc.interview.nycschools.anton.schools.SchoolId
import com.jpmc.interview.nycschools.anton.schools.SchoolItemUi
import com.jpmc.interview.nycschools.anton.ui.theme.Android20230315AntonSpaansNYCSchoolsTheme

/**
 * Shows a vertical list of [SchoolItem]s with a label on top whose text is set to [labelId].
 *
 * The lambda provided to [onSchoolClicked] will be called when the user clicks on one of
 * the [SchoolItem]s.
 */
@Composable
fun SchoolSelection(
    @StringRes labelId: Int,
    schools: List<SchoolItemUi>,
    modifier: Modifier = Modifier,
    onSchoolClicked: (SchoolItemUi) -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(),
    ) {
        Text(
            text = stringResource(labelId),
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth(),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
        )
        SchoolList(
            schools = schools,
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(),
            onSchoolClicked = onSchoolClicked,
        )
    }
}

@Composable
private fun SchoolList(
    schools: List<SchoolItemUi>,
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    onSchoolClicked: (SchoolItemUi) -> Unit = {}
) {
    LazyColumn(
        modifier = modifier,
        state = state,
    ) {
        itemsIndexed(schools) { index, item ->
            if (index != 0) {
                Divider(thickness = 6.dp, color = Color.Transparent)
            }
            SchoolItem(
                name = item.name,
                isSelected = item.isSelected,
                modifier = Modifier.clickable { onSchoolClicked(item) },
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SchoolSelectionPreview() {
    val schoolsData = List(10) {
        SchoolItemUi(SchoolId(it.toString()), "School number $it", it == 3)
    }

    Android20230315AntonSpaansNYCSchoolsTheme {
        SchoolSelection(
            labelId = R.string.schools,
            schools = schoolsData,
            modifier = Modifier.height(320.dp),
        )
    }
}
