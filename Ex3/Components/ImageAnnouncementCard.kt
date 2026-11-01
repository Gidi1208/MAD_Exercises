package com.tanvik.neo.ex3.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.chris.neo.R
import com.chris.neo.ex3.models.Announcement

@Composable
fun ImageAnnouncementCard(
    announcement: Announcement.ImageAnnouncement
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {

        Column {

            Image(
                painter = painterResource(R.drawable.pic),
                contentDescription = "Announcement",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier.padding(16.dp)
            ) {

                Text(
                    text = announcement.title,
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                Text(
                    text = announcement.description
                )
            }
        }
    }
}