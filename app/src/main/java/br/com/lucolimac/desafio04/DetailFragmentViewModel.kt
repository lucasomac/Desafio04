package br.com.lucolimac.desafio04

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.lucolimac.desafio04.model.Game
import com.google.firebase.firestore.FirebaseFirestore

class DetailFragmentViewModel : ViewModel() {
    var dbFireStore = FirebaseFirestore.getInstance()
    var crFireStorage = dbFireStore.collection("games")
    val game = MutableLiveData<Game>()
    fun readGame(name: String) {
        crFireStorage.document(name).get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    game.value = Game(
                        task.result!!.data!!["image"] as String,
                        task.result!!.data!!["name"] as String,
                        task.result!!.data!!["year"] as String,
                        task.result!!.data!!["overview"] as String
                    )
                } else {
                    Log.w("TA G", "Error getting documents.", task.exception)
                }
            }
    }

}