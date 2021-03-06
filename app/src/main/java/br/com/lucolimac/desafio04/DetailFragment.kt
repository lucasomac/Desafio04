package br.com.lucolimac.desafio04

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import br.com.lucolimac.desafio04.databinding.FragmentCadastroBinding
import br.com.lucolimac.desafio04.databinding.FragmentDetailBinding
import br.com.lucolimac.desafio04.databinding.FragmentGameListBinding
import com.squareup.picasso.Picasso

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class DetailFragment : Fragment() {
    private lateinit var _binding: FragmentDetailBinding
    private val binding get() = _binding
    val args: DetailFragmentArgs by navArgs()
    val viewModel by viewModels<DetailFragmentViewModel> {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return DetailFragmentViewModel() as T
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val nameGame = args.nameGame
        viewModel.game.observe(viewLifecycleOwner) {
            Picasso.get().load(it.imageUrl)
                .placeholder(R.drawable.progress_animation).into(binding.imageView);
            binding.tvYear.text = it.year.toString()
            binding.tvName.text = it.name
            binding.tvOverview.text = it.overview
        }
        viewModel.readGame(nameGame)
        binding.btnGoBack.setOnClickListener {
            findNavController().navigate(R.id.action_detailFragment_to_gameFragment)
        }
        binding.btnEdit.setOnClickListener {
            val action =
                DetailFragmentDirections.actionDetailFragmentToCadastroFragment(binding.tvName.text.toString())
            findNavController().navigate(action)
        }
    }
}