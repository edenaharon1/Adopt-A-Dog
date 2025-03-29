package com.example.adoptadog

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import org.json.JSONException

class VolleyRequestManager(private val context: Context) {

    fun fetchNearbyAnimalShelters(
        location: LatLng,
        map: GoogleMap,
        onComplete: () -> Unit
    ) {
        val radius = 5000 // 5 ק"מ
        val apiKey = "AIzaSyBnR0E0Y8qreE0vGNvYfg4MEqRIillPhNQ"

        val url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?" +
                "location=${location.latitude},${location.longitude}" +
                "&radius=$radius" +
                "&keyword=עמותת חיות" +
                "&type=establishment" +
                "&key=$apiKey"

        val queue = Volley.newRequestQueue(context)

        val originalBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.ic_dog_marker)
        val resizedBitmap = Bitmap.createScaledBitmap(originalBitmap, 80, 80, false)
        val dogIcon = BitmapDescriptorFactory.fromBitmap(resizedBitmap)

        val request = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                try {
                    val results = response.getJSONArray("results")

                    for (i in 0 until results.length()) {
                        val place = results.getJSONObject(i)
                        val name = place.getString("name")
                        val placeId = place.getString("place_id")
                        val lat = place.getJSONObject("geometry").getJSONObject("location").getDouble("lat")
                        val lng = place.getJSONObject("geometry").getJSONObject("location").getDouble("lng")
                        val position = LatLng(lat, lng)

                        fetchPhoneNumber(placeId, name, position, map, dogIcon, apiKey)
                    }
                } catch (e: JSONException) {
                    Log.e("VolleyError", "Parsing error: ${e.message}")
                }
                onComplete()
            },
            { error ->
                Log.e("VolleyError", "Nearby search failed: ${error.message}")
                onComplete()
            }
        )

        queue.add(request)
    }

    private fun fetchPhoneNumber(
        placeId: String,
        name: String,
        position: LatLng,
        map: GoogleMap,
        icon: BitmapDescriptor,
        apiKey: String
    ) {
        val detailsUrl = "https://maps.googleapis.com/maps/api/place/details/json?" +
                "place_id=$placeId&fields=name,formatted_phone_number&key=$apiKey"

        val request = JsonObjectRequest(
            Request.Method.GET, detailsUrl, null,
            { response ->
                try {
                    val result = response.getJSONObject("result")
                    val phone = result.optString("formatted_phone_number", "טלפון לא זמין")

                    val markerOptions = MarkerOptions()
                        .position(position)
                        .title(name)
                        .snippet(phone)
                        .icon(icon)

                    map.addMarker(markerOptions)
                } catch (e: JSONException) {
                    Log.e("VolleyError", "Details parse error: ${e.message}")
                }
            },
            { error ->
                Log.e("VolleyError", "Details request failed: ${error.message}")
            }
        )

        Volley.newRequestQueue(context).add(request)
    }
}
