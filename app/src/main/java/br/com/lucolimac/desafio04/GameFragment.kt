package br.com.lucolimac.desafio04

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import br.com.lucolimac.desafio04.databinding.FragmentGameListBinding
import br.com.lucolimac.desafio04.model.Game

class GameFragment : Fragment(), GameAdapter.OnClickGame {

    private lateinit var _binding: FragmentGameListBinding
    private val binding get() = _binding
    private lateinit var adapter: GameAdapter
    private lateinit var items: ArrayList<Game>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameListBinding.inflate(inflater, container, false)
        adapter = GameAdapter(this)
        val gridLayoutManager = GridLayoutManager(context, 3)
        gridLayoutManager.isUsingSpansToEstimateScrollbarDimensions = true
        items = arrayListOf(
            Game("", "GOD", 2015),
            Game("", "GOD", 2015),
            Game("", "GOD", 2015),
            Game("", "GOD", 2015),

            Game("", "GOD", 2015),
            Game("", "GOD", 2015),
            Game("", "GOD", 2015),
            Game("", "GOD", 2015),
            Game("", "GOD", 2015),
            Game("", "GOD", 2015),
            Game("", "GOD", 2015)
        )
        adapter.addComic(items)
        binding.list.adapter = adapter
        binding.list.hasFixedSize()
        adapter = GameAdapter(this)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_gameFragment_to_cadastroFragment)
        }
    }

    override fun onClickGame(position: Int) {
        findNavController().navigate(R.id.action_gameFragment_to_detailFragment)
        Toast.makeText(context, items[position].toString(), Toast.LENGTH_LONG).show()
    }
}