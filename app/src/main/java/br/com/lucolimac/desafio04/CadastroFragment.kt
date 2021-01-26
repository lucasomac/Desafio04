package br.com.lucolimac.desafio04

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import br.com.lucolimac.desafio04.databinding.FragmentCadastroBinding
import br.com.lucolimac.desafio04.model.Game

class CadastroFragment : Fragment() {
    private lateinit var _binding: FragmentCadastroBinding
    private val binding get() = _binding
    val args: CadastroFragmentArgs by navArgs()
    val viewModel by viewModels<CadastroFragmentViewModel> {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return CadastroFragmentViewModel() as T
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCadastroBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val nameGame = args.nameGame

        if (!nameGame.isEmpty()) {
            viewModel.game.observe(viewLifecycleOwner) {
                binding.include.editTextTextName.setText(it.name)
                binding.include.editTextTextCreatedAt.setText(it.year.toString())
                binding.include.editTextTextDescription.setText(it.overview)
            }
            viewModel.readGame(nameGame)

        }
        binding.include.btnCreateGame.setOnClickListener {
            if (nameGame.isEmpty()) {
                Log.i("TEXTO1", binding.include.editTextTextName.text.toString())
                viewModel.sendGame(
                    viewModel.getGameToInsert(
                        "",
                        binding.include.editTextTextName.text.toString(),
                        Integer.parseInt(binding.include.editTextTextCreatedAt.text.toString()),
                        binding.include.editTextTextDescription.toString()
                    )
                )
            } else {
                Log.i("TEXTO", binding.include.editTextTextName.text.toString())
                viewModel.updateGame(
                    nameGame, viewModel.getGameToInsert(
                        "",
                        binding.include.editTextTextName.text.toString(),
                        Integer.parseInt(binding.include.editTextTextCreatedAt.text.toString()),
                        binding.include.editTextTextDescription.toString()
                    )
                )
            }
            val action =
                CadastroFragmentDirections.actionCadastroFragmentToDetailFragment(binding.include.editTextTextName.text.toString())
            findNavController().navigate(action)
        }
    }
}