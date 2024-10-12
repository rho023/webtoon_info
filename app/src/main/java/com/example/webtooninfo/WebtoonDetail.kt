package com.example.webtooninfo

import android.content.Context
import android.media.Image
import android.media.Rating
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.webtooninfo.recyclerView.WebtoonList

class WebtoonDetail : AppCompatActivity() {
    private lateinit var detailTitle: TextView
    private lateinit var detailRating: TextView
    private lateinit var detailGenre: TextView
    private lateinit var ratingBar: RatingBar
    private lateinit var detailImage: ImageView
    private lateinit var detailImage2: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_webtoon_detail)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val title = intent.getStringExtra("title") ?: "Unknown"
        val genre = intent.getStringExtra("genre")
        val rating = intent.getDoubleExtra("rating", 0.0)

        detailTitle = findViewById(R.id.detailTitle)
        detailRating = findViewById(R.id.detailRating)
        detailGenre = findViewById(R.id.detailGenre)
        detailImage = findViewById(R.id.detailImage)
        detailImage2 = findViewById(R.id.detailImage2)

        detailTitle.text = title
        detailRating.text = rating.toString()
        detailGenre.text = genre

        if(title == "Solo Leveling") {
            detailImage.setImageResource(R.drawable.solo_detail)
            detailImage2.setImageResource(R.drawable.solo_detail2)
        }else if(title == "Noblesse") {
            detailImage.setImageResource(R.drawable.noblesse_detail)
            detailImage2.setImageResource(R.drawable.noblesse_detail2)
        }else if(title == "Second Life Ranker") {
            detailImage.setImageResource(R.drawable.second_detail)
            detailImage2.setImageResource(R.drawable.second_detail2)
        }

        ratingBar = findViewById(R.id.detailRatingBar)

        loadSavedRating(title)

        ratingBar.setOnRatingBarChangeListener { _, rating, _ ->
            val ratingGiven = ratingBar.rating
            saveRating(title, ratingGiven)
            Toast.makeText(this, "$ratingGiven stars", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveRating(title: String, ratingGiven: Float) {
        val sharedPreferences = getSharedPreferences("WebtoonRatings", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putFloat(title, ratingGiven)
        editor.apply()
    }

    private fun loadSavedRating(title: String) {
        val sharedPreferences = getSharedPreferences("WebtoonRatings", Context.MODE_PRIVATE)

        val savedRating = sharedPreferences.getFloat(title, 0.0f)

        ratingBar.rating = savedRating
    }
}