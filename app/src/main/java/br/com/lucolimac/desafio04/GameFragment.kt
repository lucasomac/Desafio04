package br.com.lucolimac.desafio04

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import br.com.lucolimac.desafio04.databinding.FragmentGameListBinding
import br.com.lucolimac.desafio04.model.Game

class GameFragment : Fragment(), GameAdapter.OnClickGame {

    private lateinit var _binding: FragmentGameListBinding
    private val binding get() = _binding

    //    private var adapter = GameAdapter(this)
    private lateinit var adapter: GameAdapter
    private lateinit var items: ArrayList<Game>
    val viewModel by viewModels<GameFragmentViewModel> {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return GameFragmentViewModel() as T
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        items = arrayListOf()
        _binding = FragmentGameListBinding.inflate(inflater, container, false)
        adapter = GameAdapter(this)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.list.adapter = adapter
        binding.list.hasFixedSize()

        viewModel.games.observeForever {
            items = it
            Log.i("ITEMS11", items.toString())
            adapter.addGame(items)
        }
        viewModel.readGames()
//        adapter.addGame(items)
        Log.i("ITEMS12", items.toString())

        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_gameFragment_to_cadastroFragment)
        }
    }

    override fun onClickGame(position: Int) {
        val action =
            GameFragmentDirections.actionGameFragmentToDetailFragment(items[position].name)
        findNavController().navigate(action)
    }

}