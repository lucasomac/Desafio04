package br.com.lucolimac.desafio04

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.lucolimac.desafio04.model.Game
import com.squareup.picasso.Picasso


class GameAdapter(
    val listener: OnClickGame,
) : RecyclerView.Adapter<GameAdapter.GameViewHolder>() {
    var games = arrayListOf<Game>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.game_item, parent, false)
        return GameViewHolder(view)
    }

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        val item = games[position]
        if (item.imageUrl.isNotEmpty())
            Picasso.get().load(item.imageUrl).placeholder(R.drawable.progress_animation)
                .into(holder.ivCapa)
        holder.tvName.text = item.name
        holder.tvAno.text = item.year
    }

    override fun getItemCount(): Int = games.size
    fun addGame(list: ArrayList<Game>) {
        games = list
        notifyDataSetChanged()
    }

    inner class GameViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        init {
            itemView.setOnClickListener(this)
        }

        val ivCapa: ImageView = view.findViewById(R.id.ivCapa)
        val tvName: TextView = view.findViewById(R.id.tvName)
        val tvAno: TextView = view.findViewById(R.id.tvAno)

        override fun toString(): String {
            return super.toString() + " '" + tvName.text + "'"
        }

        override fun onClick(v: View?) {
            if (adapterPosition != RecyclerView.NO_POSITION) {
                listener.onClickGame(adapterPosition)
            }
        }
    }

    interface OnClickGame {
        fun onClickGame(position: Int)
    }
}