package com.example.webtooninfo

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.webtooninfo.recyclerView.WebtoonList
import com.example.webtooninfo.recyclerView.WebtoonListAdapter
import kotlinx.coroutines.launch

class FavoriteScreen : AppCompatActivity() {
    private lateinit var favoriteRecycler: RecyclerView
    private lateinit var adapter: WebtoonListAdapter
    private lateinit var database: WebtoonDatabase
    private lateinit var backButton: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_favorite_screen)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        favoriteRecycler = findViewById(R.id.favoritesRecyclerView)
        database = WebtoonDatabase.getDatabase(this)

        lifecycleScope.launch {
            val favoritesList = database.webtoonDao().getAllFavorites()
            val webtoonDao = database.webtoonDao()
            adapter = WebtoonListAdapter(favoritesList, object : OnItemClickListener {
                override fun onItemClick(position: Int) {
                    // Handle item click in favorites
                }

                override fun onFavoriteClick(position: Int) {
                    // Handle favorite button in the favorites screen
                }
            }, webtoonDao)
            favoriteRecycler.layoutManager = LinearLayoutManager(this@FavoriteScreen)
            favoriteRecycler.adapter = adapter

            backButton = findViewById(R.id.fav_back_button)
            backButton.setOnClickListener {
                intent = Intent(this@FavoriteScreen, HomeScreen::class.java)
                startActivity(intent)
            }
        }
    }
}