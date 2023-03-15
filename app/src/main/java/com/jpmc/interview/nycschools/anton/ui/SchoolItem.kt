package com.jpmc.interview.nycschools.anton.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
    Surface(color = Color.Transparent, modifier = modifier) {
        Card(
            // Selected school-item has a blue border. Otherwise it has a gray border.
            border = BorderStroke(
                1.dp,
                color = if (isSelected) Color.Blue else Color.LightGray,
            ),
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
