import androidx.lifecycle.ViewModel
import com.chris.ex3.models.Announcement
import com.chris.neo.ex3.repositories.AnnouncementRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AnnouncementViewModel : ViewModel() {
    private val repository = AnnouncementRepository()
    private val _announcements = MutableStateFlow<List<Announcement>>(emptyList())
    val announcements: StateFlow<List<Announcement>> = _announcements.asStateFlow()

    init {
        loadAnnouncements()
    }

    private fun loadAnnouncements() {
        _announcements.value = repository.getAnnouncements()
    }
}