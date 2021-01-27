package br.com.lucolimac.desafio04

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import br.com.lucolimac.desafio04.databinding.FragmentCadastroBinding
import br.com.lucolimac.desafio04.model.Game
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import java.util.*

class CadastroFragment : Fragment() {
    private lateinit var _binding: FragmentCadastroBinding
    private val binding get() = _binding
    private val CODE_IMAGE = 1000
    var url = ""
    var firebaseStorage = FirebaseStorage.getInstance()
    var gameImageRef: StorageReference = firebaseStorage.getReference(Date().toInstant().toString())
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
        binding.ivCamera.setOnClickListener { setIntent() }
        viewModel.photo.observe(viewLifecycleOwner) {
            url = it
        }
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
                viewModel.sendGame(
                    viewModel.getGameToInsert(
                        url,
                        binding.include.editTextTextName.text.toString(),
                        binding.include.editTextTextCreatedAt.text.toString().toInt(),
                        binding.include.editTextTextDescription.text.toString()
                    )
                )
            } else {
                viewModel.updateGame(
                    nameGame, viewModel.getGameToInsert(
                        url,
                        binding.include.editTextTextName.text.toString(),
                        binding.include.editTextTextCreatedAt.text.toString().toInt(),
                        binding.include.editTextTextDescription.text.toString()
                    )
                )
            }
            val action =
                CadastroFragmentDirections.actionCadastroFragmentToDetailFragment(binding.include.editTextTextName.text.toString())
            findNavController().navigate(action)
        }
    }

    fun setIntent() {
        val intent = Intent()
        intent.type = "image/"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(
            Intent.createChooser(intent, "Captura Imagem"),
            CODE_IMAGE
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CODE_IMAGE) {
            val uploadTask = gameImageRef.putFile(data!!.data!!)
            uploadTask.continueWith { task ->
                if (task.isSuccessful) {
                    Toast.makeText(context, "Enviando imagem", Toast.LENGTH_LONG).show()
                }
                gameImageRef.downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
//                    val downloadUri = gameImageRef.downloadUrl
                    val downloadUri = task.result
                    Log.i("TEXTO1", downloadUri.toString())
                    viewModel.setUrlImage(downloadUri.toString())
                    Picasso.get().load(downloadUri.toString()).into(binding.ivCamera)
                }
            }
        }
    }
}