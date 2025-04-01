import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import com.example.scoutai.CapturedImage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CameraViewModel : ViewModel() {
    // A list to hold the captured images
    val imagesList: SnapshotStateList<CapturedImage> = mutableStateListOf()
    private var imageCount = 0

    // Permissions and configurations as StateFlow
    private val _imagesPermission = MutableStateFlow(false)
    val imagesPermission: StateFlow<Boolean> = _imagesPermission

    private val _gpsPermission = MutableStateFlow(false)
    val gpsPermission: StateFlow<Boolean> = _gpsPermission

    private val _orientationPermission = MutableStateFlow(false)
    val orientationPermission: StateFlow<Boolean> = _orientationPermission

    private val _recordTimestamps = MutableStateFlow(false)
    val recordTimestamps: StateFlow<Boolean> = _recordTimestamps

    private val _captureInterval = MutableStateFlow(1000L)  // Default value for capture interval
    val captureInterval: StateFlow<Long> = _captureInterval

    // Method to add captured images to the list
    fun addImage(uri: String, name: String, orientation: String, latitude: Double?, longitude: Double?) {
        imageCount++

        val capturedImage = CapturedImage(
            name = name,
            uri = uri,
            orientation = orientation,
            latitude = latitude,
            longitude = longitude
        )

        imagesList.add(capturedImage)
    }

    // Method to update permission states
    fun updatePermissions(imagesPermission: Boolean, gpsPermission: Boolean, orientationPermission: Boolean) {
        _imagesPermission.value = imagesPermission
        _gpsPermission.value = gpsPermission
        _orientationPermission.value = orientationPermission
    }

    // Method to update record timestamps state
    fun updateRecordTimestamps(recordTimestamps: Boolean) {
        _recordTimestamps.value = recordTimestamps
    }

    // Method to update capture interval
    fun updateCaptureInterval(interval: Long) {
        _captureInterval.value = interval
    }
}
