package com.example.webtooninfo

import android.content.Intent
import android.os.Bundle
import android.transition.TransitionManager
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.webtooninfo.recyclerView.WebtoonList
import com.example.webtooninfo.recyclerView.WebtoonListAdapter
import kotlinx.coroutines.launch

class HomeScreen : AppCompatActivity(), OnItemClickListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var webtoonList: List<WebtoonList>
    private lateinit var adapter: WebtoonListAdapter
    private lateinit var database: WebtoonDatabase

    private lateinit var constraintLayout: ConstraintLayout
    private lateinit var originalConstraintSet: ConstraintSet

    private lateinit var featuredImage: ImageView
    private lateinit var featuredTitle: TextView
    private lateinit var featuredRating: TextView
    private lateinit var favoriteButton: ImageView
    private lateinit var searchWebtoon: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home_screen)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        favoriteButton = findViewById(R.id.homeFavoriteButton)
        searchWebtoon = findViewById(R.id.searchView)
        constraintLayout = findViewById(R.id.main)

        originalConstraintSet = ConstraintSet()
        originalConstraintSet.clone(constraintLayout)

        recyclerView = findViewById(R.id.recyclerListOfManhwa)
        webtoonList = ArrayList()
        (webtoonList as ArrayList<WebtoonList>).add(WebtoonList(1, R.drawable.tower_of_god, "Tower of God", "Fantasy", 4.0, getString(R.string.tower_description)))
        (webtoonList as ArrayList<WebtoonList>).add(WebtoonList(2, R.drawable.noblesse, "Noblesse", "Fantasy", 3.9, getString(R.string.noblesse_description)))
        (webtoonList as ArrayList<WebtoonList>).add(WebtoonList(3, R.drawable.second_life_ranker, "Second Life Ranker", "Fantasy", 4.2, getString(R.string.second_description)))

        database = WebtoonDatabase.getDatabase(this)
        val webtoonDao = database.webtoonDao()

        adapter = WebtoonListAdapter(webtoonList, this, webtoonDao)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        favoriteButton.setOnClickListener {
            val intent = Intent(this, FavoriteScreen::class.java)
            startActivity(intent)
        }

        searchWebtoon.setOnSearchClickListener {
            moveTextViewDown()
        }

        searchWebtoon.setOnCloseListener {
            moveTextViewUp()
            false
        }

        featuredRating = findViewById(R.id.mainImageRating)
        featuredTitle = findViewById(R.id.mainImageTitle)
        featuredImage = findViewById(R.id.featuredImage)
        featuredImage.setOnClickListener {
            val intent = Intent(this, WebtoonDetail::class.java)

            intent.putExtra("title", featuredTitle.text)
            intent.putExtra("rating", featuredRating.text.toString().toDoubleOrNull() ?: 0.0)
            intent.putExtra("genre", "Fantasy")

            startActivity(intent)
        }
    }

    private fun moveTextViewDown() {
        val constraintSet = ConstraintSet()
        constraintSet.clone(constraintLayout)

        constraintSet.clear(R.id.featuredTextView, ConstraintSet.TOP)
        constraintSet.connect(R.id.featuredTextView, ConstraintSet.TOP, R.id.searchView, ConstraintSet.BOTTOM, 20)

        TransitionManager.beginDelayedTransition(constraintLayout)
        constraintSet.applyTo(constraintLayout)
    }

    private fun moveTextViewUp() {
        TransitionManager.beginDelayedTransition(constraintLayout)
        originalConstraintSet.applyTo(constraintLayout)
    }

    override fun onItemClick(position: Int) {
        val clickedWebtoon = webtoonList[position]
        val intent = Intent(this, WebtoonDetail::class.java)

        intent.putExtra("title", clickedWebtoon.itemTitle)
        intent.putExtra("genre", clickedWebtoon.itemGenre)
        intent.putExtra("rating", clickedWebtoon.itemRating)
        intent.putExtra("image", clickedWebtoon.itemImage)

        startActivity(intent)
    }

    override fun onFavoriteClick(position: Int) {
        val webtoon = webtoonList[position]
        lifecycleScope.launch {
            database.webtoonDao().addToFavorites(webtoon)
        }
    }
}