package com.example.webtooninfo.recyclerView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.webtooninfo.OnItemClickListener
import com.example.webtooninfo.R
import com.example.webtooninfo.WebtoonDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WebtoonListAdapter(private val webtoonList: List<WebtoonList>,
                         private val listener: OnItemClickListener,
                         private val webtoonDao: WebtoonDao) : RecyclerView.Adapter<WebtoonListAdapter.WebtoonViewHolder>() {

    inner class WebtoonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title : TextView = itemView.findViewById(R.id.item_title)
        val description : TextView = itemView.findViewById(R.id.item_description)
        val genre : TextView = itemView.findViewById(R.id.item_genre)
        val rating : TextView = itemView.findViewById(R.id.item_rating)
        val image : ImageView = itemView.findViewById(R.id.item_image)
        val favoriteButton : ImageButton = itemView.findViewById(R.id.add_to_favorite_button)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if(position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(position)
                }
            }

            favoriteButton.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.onFavoriteClick(position)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WebtoonViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_list_item, parent, false)

        return WebtoonViewHolder(view)
    }

    override fun onBindViewHolder(holder: WebtoonViewHolder, position: Int) {
        val currentItem = webtoonList[position]
        holder.title.text = currentItem.itemTitle
        holder.description.text = currentItem.itemDescription
        holder.genre.text = currentItem.itemGenre
        holder.rating.text = currentItem.itemRating.toString()
        holder.image.setImageResource(currentItem.itemImage)

        val webtoon = webtoonList[position]

        CoroutineScope(Dispatchers.IO).launch {
            val isFavorite = webtoonDao.getFavoriteById(currentItem.id) != null

            CoroutineScope(Dispatchers.Main).launch {
                holder.favoriteButton.setImageResource(
                    if (isFavorite) R.drawable.baseline_favorite_20 else R.drawable.baseline_favorite_border_20
                )

                holder.favoriteButton.setOnClickListener {
                    CoroutineScope(Dispatchers.IO).launch {
                        if (isFavorite) {

                            // Remove from favorites
                            webtoonDao.removeFromFavorites(webtoon.id)

                            CoroutineScope(Dispatchers.Main).launch {
                                holder.favoriteButton.setImageResource(R.drawable.baseline_favorite_border_20)
                                Toast.makeText(holder.itemView.context, "Removed from favorites", Toast.LENGTH_SHORT).show()
                            }

                        } else {

                            // Add to favorites
                            webtoonDao.addToFavorites(webtoon)

                            CoroutineScope(Dispatchers.Main).launch {
                                holder.favoriteButton.setImageResource(R.drawable.baseline_favorite_20)
                                Toast.makeText(holder.itemView.context, "Added to favorites", Toast.LENGTH_SHORT).show()
                            }

                        }
                    }
                }
            }
        }

    }

    override fun getItemCount(): Int {
        return webtoonList.size
    }
}