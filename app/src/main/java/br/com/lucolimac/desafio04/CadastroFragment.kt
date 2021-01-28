package br.com.lucolimac.desafio04

import android.app.AlertDialog
import android.content.Intent
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
import androidx.navigation.fragment.navArgs
import br.com.lucolimac.desafio04.databinding.FragmentCadastroBinding
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import dmax.dialog.SpotsDialog

class CadastroFragment : Fragment() {
    private lateinit var _binding: FragmentCadastroBinding
    private val binding get() = _binding
    private val CODE_IMAGE = 1000
    var url = ""
    private lateinit var alertDialog: AlertDialog
    private lateinit var firebaseStorage: FirebaseStorage
    private lateinit var firebaseStorageReference: StorageReference
    private lateinit var firebaseFirestore: FirebaseFirestore
    private lateinit var firebaseFirestoreReference: CollectionReference
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
        config()
        return binding.root
    }

    fun config() {
        alertDialog = SpotsDialog.Builder().setContext(context).build()
        firebaseStorage = FirebaseStorage.getInstance()
        firebaseStorageReference = firebaseStorage.getReference("games.jpg")
        firebaseFirestore = FirebaseFirestore.getInstance()
        firebaseFirestoreReference = firebaseFirestore.collection("games")
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
//                Picasso.get().load(it.imageUrl).resize(50, 50).into(binding.ivCamera)
                binding.include.editTextTextName.setText(it.name)
                binding.include.editTextTextCreatedAt.setText(it.year.toString())
                binding.include.editTextTextDescription.setText(it.overview)
            }
            viewModel.readGame(nameGame)

        }
        binding.include.btnCreateGame.setOnClickListener {
            Log.i("LOH", url)
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
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(
            Intent.createChooser(intent, "Captura Imagem"), CODE_IMAGE
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CODE_IMAGE) {
            alertDialog.show()
            val uploadTask = firebaseStorageReference.putFile(data!!.data!!)
            uploadTask.continueWith { task ->
                if (task.isSuccessful) {
                    Toast.makeText(context, "Imagem enviada", Toast.LENGTH_LONG).show()
                }
                firebaseStorageReference.downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUri = task.result
                    val url = downloadUri!!.toString()
//                        .substring(0, downloadUri.toString().indexOf("&token"))
                    Log.i("URKO2", url)
                    viewModel.setUrlImage(url)
                    alertDialog.dismiss()
                    Picasso.get().load(url).resize(50, 50).into(binding.ivCamera)
                }
            }
        }
    }
}