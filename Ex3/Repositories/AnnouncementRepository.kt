import com.chris.ex3.models.Announcement

class AnnouncementRepository {
    fun getAnnouncements(): List<Announcement> {
        return listOf(
            Announcement.TextNotice(
                id = 1,
                title = "Office Maintenance",
                message = "The office will undergo maintenance this Saturday."
            ),

            Announcement.UrgentAlert(
                id = 2,
                title = "Security Alert",
                message = "Please reset your company password immediately."
            ),

            Announcement.ImageAnnouncement(
                id = 3,
                title = "Annual Sports Day",
                imageUrl = "https://example.com/sports-day.jpg",
                description = "Join us for the annual employee sports day."
            ),

            Announcement.ActionItem(
                id = 4,
                title = "Complete Employee Survey",
                message = "Please complete the employee satisfaction survey.",
                actionText = "Complete Survey"
            )
        )
    }
}