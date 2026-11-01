import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.chris.neo.ex3.screen.AnnouncementScreen
import com.chris.neo.ex3.viewmodels.AnnouncementViewModel

class AnnouncementFragment : Fragment() {

    private val viewModel: AnnouncementViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return ComposeView(requireContext()).apply {

            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
            )

            setContent {

                MaterialTheme {

                    val announcements by viewModel.announcements
                        .collectAsStateWithLifecycle()

                    AnnouncementScreen(
                        announcements = announcements
                    )
                }
            }
        }
    }
}