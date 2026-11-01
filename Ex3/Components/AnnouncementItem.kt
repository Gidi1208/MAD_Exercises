import androidx.compose.runtime.Composable
import com.chris.ex3.models.Announcement

@Composable
fun AnnouncementItem(
    announcement: Announcement
) {
    when (announcement) {

        is Announcement.TextNotice -> {
            TextNoticeCard(announcement)
        }

        is Announcement.UrgentAlert -> {
            UrgentAlertCard(announcement)
        }

        is Announcement.ImageAnnouncement -> {
            ImageAnnouncementCard(announcement)
        }

        is Announcement.ActionItem -> {
            ActionItemCard(announcement)
        }
    }
}