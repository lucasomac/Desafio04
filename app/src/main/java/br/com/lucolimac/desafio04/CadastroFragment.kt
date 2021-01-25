package br.com.lucolimac.desafio04

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import br.com.lucolimac.desafio04.databinding.FragmentCadastroBinding

class CadastroFragment : Fragment() {
    private lateinit var _binding: FragmentCadastroBinding
    private val binding get() = _binding
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
        binding.include.btnCreateGame.setOnClickListener {
            findNavController().navigate(R.id.action_cadastroFragment_to_detailFragment)
        }
    }
}