package com.zdari.darijuegos.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.FileProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.File
import java.net.HttpURLConnection
import java.net.URL

class UpdateManager(private val context: Context) {

    private val repoUrl = "https://api.github.com/repos/eldarioqueprograma/darijuegos/releases/latest"

    /**
     * Compara la versión actual con la de GitHub Releases.
     * Devuelve la URL de descarga del APK si hay una actualización.
     */
    suspend fun checkForUpdates(currentVersion: String): String? {
        return withContext(Dispatchers.IO) {
            try {
                val url = URL(repoUrl)
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                connection.setRequestProperty("Accept", "application/vnd.github.v3+json")

                if (connection.responseCode == 200) {
                    val json = connection.inputStream.bufferedReader().readText()
                    val release = JSONObject(json)
                    val latestTag = release.getString("tag_name")

                    // Solo devolvemos URL si la versión de GitHub es distinta a la actual
                    if (latestTag != currentVersion) {
                        val assets = release.getJSONArray("assets")
                        for (i in 0 until assets.length()) {
                            val asset = assets.getJSONObject(i)
                            if (asset.getString("name").endsWith(".apk")) {
                                return@withContext asset.getString("browser_download_url")
                            }
                        }
                    }
                }
                null
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    /**
     * Descarga el APK en la caché y lanza el instalador.
     */
    suspend fun downloadAndInstall(downloadUrl: String) {
        withContext(Dispatchers.IO) {
            try {
                val url = URL(downloadUrl)
                val connection = url.openConnection() as HttpURLConnection
                val apkFile = File(context.cacheDir, "update.apk")

                url.openStream().use { input ->
                    apkFile.outputStream().use { output ->
                        input.copyTo(output)
                    }
                }

                withContext(Dispatchers.Main) {
                    installApk(apkFile)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun installApk(file: File) {
        val uri: Uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "application/vnd.android.package-archive")
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(intent)
    }
}
