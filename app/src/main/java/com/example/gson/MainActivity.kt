package com.example.gson

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import okhttp3.OkHttpClient
import timber.log.Timber
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Request

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Timber.plant(Timber.DebugTree())

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val recyclerView: RecyclerView = findViewById(R.id.rView)
                recyclerView.layoutManager = GridLayoutManager(this@MainActivity, 2)

                val photos = fetchPhotos()
                val photoUrls = generatePhotoUrls(photos)

                recyclerView.adapter = PhotoAdapter(this@MainActivity, photoUrls)
            } catch (e: Exception) {
                Timber.e("MainActivity", "Ошибка: ${e.message}")
            }
        }
    }

    fun generatePhotoUrls(photos: ArrayList<Photo>): List<String> {
        return photos.map { photo ->
            "https://farm${photo.farm}.staticflickr.com/${photo.server}/${photo.id}_${photo.secret}_z.jpg"
        }
    }

    fun fetchPhotos(): ArrayList<Photo> {
        val url = "https://api.flickr.com/services/rest/?method=flickr.photos.search&api_key=ff49fcd4d4a08aa6aafb6ea3de826464&tags=cat&format=json&nojsoncallback=1"

        val client = OkHttpClient()

        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw Exception("Ошибка загрузки данных: ${response.code}")

            val jsonResponse = response.body?.string() ?: throw Exception("Пустой ответ от сервера")

            val wrapper: Wrapper = Gson().fromJson(jsonResponse, Wrapper::class.java)

            return wrapper.photos.photo
        }
    }
}