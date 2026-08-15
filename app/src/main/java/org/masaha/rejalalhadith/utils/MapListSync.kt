package org.masaha.rejalalhadith.utils

import android.content.Context
import android.util.Log
import org.json.JSONObject
import java.io.File
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

object MapListSync {
    private const val TAG = "MapListSync"
    private const val FILE_NAME = "maplist.json"
    private const val UPDATE_URL = "https://www.masaha.org/api/rejal/map-list/latest-update"
    private const val LIST_URL = "https://www.masaha.org/api/rejal/map-list"

    fun cacheFile(context: Context): File {
        return File(context.filesDir, FILE_NAME)
    }

    fun readJson(context: Context): String {
        val cached = cacheFile(context)
        if (cached.exists() && cached.length() > 0) {
            return cached.readText()
        }
        return context.assets.open(FILE_NAME).bufferedReader().use { it.readText() }
    }

    fun sync(context: Context) {
        val appContext = context.applicationContext
        try {
            val latest = fetchLatestDate()
            val prefs = PrefManager(appContext)
            val saved = prefs.getMapListUpdatedAt()

            if (saved.isEmpty()) {
                prefs.saveMapListUpdatedAt(latest)
                Log.d(TAG, "Saved initial map-list date: $latest")
                return
            }

            if (saved == latest) {
                return
            }

            val json = fetchMapList()
            JSONObject(json).getJSONObject("data").getJSONArray("records")
            cacheFile(appContext).writeText(json)
            prefs.saveMapListUpdatedAt(latest)
            RejalHierarchy.reload(appContext)
            Log.d(TAG, "Updated map-list from internet. New date: $latest")
        } catch (error: Exception) {
            Log.e(TAG, "Unable to sync map-list", error)
        }
    }

    private fun fetchLatestDate(): String {
        val json = JSONObject(httpGet(UPDATE_URL))
        if (!json.optBoolean("success", false)) {
            throw IOException("latest-update request failed")
        }
        return json.getString("data")
    }

    private fun fetchMapList(): String {
        val jsonText = httpGet(LIST_URL)
        val json = JSONObject(jsonText)
        if (!json.optBoolean("success", false)) {
            throw IOException("map-list request failed")
        }
        return jsonText
    }

    private fun httpGet(url: String): String {
        val connection = URL(url).openConnection() as HttpURLConnection
        connection.connectTimeout = 15000
        connection.readTimeout = 30000
        connection.requestMethod = "GET"
        connection.setRequestProperty("Accept", "application/json")
        try {
            val code = connection.responseCode
            if (code !in 200..299) {
                throw IOException("HTTP $code for $url")
            }
            return connection.inputStream.bufferedReader().use { it.readText() }
        } finally {
            connection.disconnect()
        }
    }
}
