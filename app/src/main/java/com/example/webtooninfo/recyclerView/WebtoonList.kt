package com.example.webtooninfo.recyclerView

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class WebtoonList(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val itemImage: Int,
    val itemTitle: String,
    val itemGenre: String,
    val itemRating: Double,
    val itemDescription: String
)