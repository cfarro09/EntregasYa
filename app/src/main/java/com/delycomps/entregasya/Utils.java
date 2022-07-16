/**
 * Copyright 2017 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.delycomps.entregasya;


import android.content.Context;
import android.location.Location;
import android.preference.PreferenceManager;

import com.delycomps.entregasya.model.LocationDTO;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.ktx.Firebase;

import java.text.DateFormat;
import java.util.Date;

class Utils {

    static final String KEY_REQUESTING_LOCATION_UPDATES = "requesting_location_updates";

    /**
     * Returns true if requesting location updates, otherwise returns false.
     *
     * @param context The {@link Context}.
     */
    static boolean requestingLocationUpdates(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(KEY_REQUESTING_LOCATION_UPDATES, false);
    }

    /**
     * Stores the location updates state in SharedPreferences.
     * @param requestingLocationUpdates The location updates state.
     */
    static void setRequestingLocationUpdates(Context context, boolean requestingLocationUpdates) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(KEY_REQUESTING_LOCATION_UPDATES, requestingLocationUpdates)
                .apply();
    }

    /**
     * Returns the {@code location} object as a human readable string.
     * @param location  The {@link Location}.
     */
    static String getLocationText(Location location) {
        return location == null ? "Unknown location" :
                "(" + location.getLatitude() + ", " + location.getLongitude() + ")";
    }

    static String getLocationTitle(Context context) {
        return "actualización time " + DateFormat.getDateTimeInstance().format(new Date());
    }
    static void shareLocation (Location location, String driverId, String plateNumber) {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        LocationDTO locationDTO = new LocationDTO(location.getLatitude(), location.getLongitude(), plateNumber);

        database.child("locations").child("driver" + driverId).setValue(locationDTO);
    }
    static void removeLocation (String driverId) {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
//        LocationDTO locationDTO = new LocationDTO(location.getLatitude(), location.getLongitude());

        database.child("locations").child("driver" + driverId).removeValue();
    }
}