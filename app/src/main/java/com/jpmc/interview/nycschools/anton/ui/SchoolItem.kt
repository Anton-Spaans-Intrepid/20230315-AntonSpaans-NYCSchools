package com.jpmc.interview.nycschools.anton.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jpmc.interview.nycschools.anton.R
import com.jpmc.interview.nycschools.anton.ui.theme.Android20230315AntonSpaansNYCSchoolsTheme

/**
 * Shows a school-item with an icon, a [name] and shows the item as selected based on the value
 * of the boolean [isSelected].
 */
@Composable
fun SchoolItem(name: String, isSelected: Boolean, modifier: Modifier = Modifier) {
    Card(
        colors = if (isSelected) CardDefaults.elevatedCardColors() else CardDefaults.cardColors(),
        elevation = if (isSelected) CardDefaults.elevatedCardElevation() else CardDefaults.cardElevation(),
        modifier = modifier,
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 6.dp, vertical = 4.dp)
                .height(48.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_school),
                contentDescription = "",
                modifier = Modifier.wrapContentSize(),
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = name,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                maxLines = 2,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SchoolItemPreview() {
    Android20230315AntonSpaansNYCSchoolsTheme {
        SchoolItem("Large school name fdsajlf dsajlfd sajfsajl fdsaij;lfsad fds", false)
    }
}

@Preview(showBackground = true)
@Composable
fun SelectedSchoolItemPreview() {
    Android20230315AntonSpaansNYCSchoolsTheme {
        SchoolItem("Some other school", true)
    }
}
