package com.utsman.jokenorris.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.utsman.jokenorris.R
import com.utsman.jokenorris.domain.entity.Joke

class JokeAdapter : RecyclerView.Adapter<JokeViewHolder>() {
    private val _list: MutableList<Joke> = mutableListOf()
    private val list: List<Joke>
        get() = _list

    fun updateList(list: List<Joke>) {
        _list.clear()
        _list.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JokeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_joke, parent, false)
        return JokeViewHolder(view)
    }

    override fun onBindViewHolder(holder: JokeViewHolder, position: Int) {
        val joke = list[position]
        holder.bind(joke)
    }

    override fun getItemCount(): Int {
        return list.size
    }
}