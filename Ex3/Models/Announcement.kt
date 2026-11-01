sealed class Announcement {
    abstract val id: Int
    abstract val title: String

    data class TextNotice(
        override val id: Int,
        override  val title: String,
        val message: String
    ) : Announcement()

    data class UrgentAlert(
        override val id: Int,
        override val title: String,
        val message: String
    ) : Announcement()

    data class ImageAnnouncement(
        override val id: Int,
        override val title: String,
        val imageUrl: String,
        val description: String
    ) : Announcement()

    data class ActionItem(
        override val id: Int,
        override val title: String,
        val message: String,
        val actionText: String
    ) : Announcement()
}