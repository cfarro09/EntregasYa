package com.delycomps.entregasya.data

import java.text.DateFormat
import java.util.Date

/**
 * Data class for Location related data (only takes what's needed from
 * {@link android.location.Location} class).
 */
data class MyLocationEntity(
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val foreground: Boolean = true,
    val date: Date = Date()
) {

    override fun toString(): String {
        val appState = if (foreground) {
            "in app"
        } else {
            "in BG"
        }

        return "$latitude, $longitude $appState on " +
                "${DateFormat.getDateTimeInstance().format(date)}.\n"
    }
}