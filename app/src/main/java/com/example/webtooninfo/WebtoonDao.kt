package com.example.webtooninfo

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.webtooninfo.recyclerView.WebtoonList

@Dao
interface WebtoonDao {

    @Insert
    suspend fun addToFavorites(webtoon: WebtoonList)

    @Query("SELECT * FROM favorites")
    suspend fun getAllFavorites(): List<WebtoonList>

    @Query("SELECT * FROM favorites WHERE id = :id")
    suspend fun getFavoriteById(id: Int): WebtoonList?

    @Query("DELETE FROM favorites WHERE id = :id")
    suspend fun removeFromFavorites(id: Int)

}