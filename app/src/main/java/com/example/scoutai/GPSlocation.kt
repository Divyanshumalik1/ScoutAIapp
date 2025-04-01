import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task

fun getCurrentLocation(context: Context, onLocationReceived: (latitude: Double?, longitude: Double?) -> Unit) {
    val fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

    // Check if permission is granted
    if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
        val locationTask: Task<Location> = fusedLocationClient.lastLocation

        locationTask.addOnSuccessListener { location ->
            if (location != null) {
                // Pass latitude and longitude
                onLocationReceived(location.latitude, location.longitude)
            } else {
                onLocationReceived(null, null)
            }
        }
    } else {
        // Request permission if not granted
        ActivityCompat.requestPermissions(context as Activity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
        onLocationReceived(null, null)
    }
}