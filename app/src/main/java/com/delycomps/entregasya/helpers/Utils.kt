package com.delycomps.entregasya.helpers

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.preference.PreferenceManager
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.FirebaseDatabase
import java.text.DateFormat
import java.util.*

const val KEY_REQUESTING_LOCATION_UPDATES = "requesting_location_updates"


object Utils {
    fun Context.hasPermission(permission: String): Boolean {

        // Background permissions didn't exit prior to Q, so it's approved by default.
        if (permission == Manifest.permission.ACCESS_BACKGROUND_LOCATION &&
            android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.Q) {
            return true
        }

        return ActivityCompat.checkSelfPermission(this, permission) ==
                PackageManager.PERMISSION_GRANTED
    }

    /**
     * Requests permission and if the user denied a previous request, but didn't check
     * "Don't ask again", we provide additional rationale.
     *
     * Note: The Snackbar should have an action to request the permission.
     */
    fun Fragment.requestPermissionWithRationale(
        permission: String,
        requestCode: Int,
        snackbar: Snackbar
    ) {
        val provideRationale = shouldShowRequestPermissionRationale(permission)

        if (provideRationale) {
            snackbar.show()
        } else {
            requestPermissions(arrayOf(permission), requestCode)
        }
    }

    fun requestingLocationUpdates(context: Context?): Boolean {
        return PreferenceManager.getDefaultSharedPreferences(context)
            .getBoolean(KEY_REQUESTING_LOCATION_UPDATES, false)
    }

    /**
     * Stores the location updates state in SharedPreferences.
     * @param requestingLocationUpdates The location updates state.
     */
    fun setRequestingLocationUpdates(context: Context?, requestingLocationUpdates: Boolean) {
        PreferenceManager.getDefaultSharedPreferences(context)
            .edit()
            .putBoolean(KEY_REQUESTING_LOCATION_UPDATES, requestingLocationUpdates)
            .apply()
    }

    /**
     * Returns the `location` object as a human readable string.
     * @param location  The [Location].
     */
    fun getLocationText(location: Location?): CharSequence? {
        return if (location == null) "Unknown location" else "(" + location.latitude + ", " + location.longitude + ")"
    }

    fun getLocationTitle(context: Context?): String? {
        return "actualización time " + DateFormat.getDateTimeInstance().format(Date())
    }

    fun shareLocation(location: Location, driverId: String, plateNumber: String?) {
        val database = FirebaseDatabase.getInstance().reference
        //val locationDTO = Location(location.latitude, location.longitude, plateNumber)
        //database.child("locations").child("driver$driverId").setValue(locationDTO)
    }

    fun removeLocation(driverId: String) {
        val database = FirebaseDatabase.getInstance().reference
        //        LocationDTO locationDTO = new LocationDTO(location.getLatitude(), location.getLongitude());
        database.child("locations").child("driver$driverId").removeValue()
    }
}