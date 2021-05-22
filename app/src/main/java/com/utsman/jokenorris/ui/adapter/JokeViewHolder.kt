package com.utsman.jokenorris.ui.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.utsman.jokenorris.domain.entity.Joke
import com.utsman.jokenorris.databinding.ItemJokeBinding

class JokeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    fun bind(joke: Joke) = ItemJokeBinding.bind(itemView).run {
        tvItem.text = joke.joke
    }
}