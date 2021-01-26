package br.com.lucolimac.desafio04

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.lucolimac.desafio04.model.Game
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


class CadastroFragmentViewModel : ViewModel() {
    // Create a storage reference from the app
    var firebaseStorage = FirebaseStorage.getInstance()
    var dbFireStorage = FirebaseFirestore.getInstance()
    var crFireStorage = dbFireStorage.collection("games")
    var gameImageRef: StorageReference = firebaseStorage.getReference("images")
    val games = MutableLiveData<List<Game>>()
    val game = MutableLiveData<Game>()

    fun getGameToInsert(
        image: String,
        name: String,
        year: Int,
        overview: String
    ): MutableMap<String, Any> {
        val game: MutableMap<String, Any> = HashMap()
        game["image"] = image
        game["name"] = name
        game["year"] = year
        game["overview"] = overview
        return game
    }

    fun sendGame(game: MutableMap<String, Any>) {
        val nome = game["name"]
        Log.i("TEXTO1", game["name"].toString())
        crFireStorage.document(nome.toString()).set(game).addOnSuccessListener {
//            Log.i("STORE", it.toString())
        }.addOnFailureListener {
//            Log.i("STORE", it.toString())
        }
    }

    fun updateGame(name: String, game: MutableMap<String, Any>) {
        crFireStorage.document(name).update(game)

    }

    fun deleteProd(name: String) {
        crFireStorage.document(name).delete().addOnSuccessListener {

        }
    }

    fun readGame(name: String) {
        crFireStorage.document(name).get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    game.value = Game(
                        task.result!!.data!!["image"] as Uri,
                        task.result!!.data!!["name"] as String,
                        task.result!!.data!!["year"] as Int,
                        task.result!!.data!!["overview"] as String
                    )

                } else {
                    Log.w("TA G", "Error getting documents.", task.exception)
                }
            }
    }

    fun readGames() {
        crFireStorage.get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val list = mutableListOf<Game>()
                    for (document in task.result!!) {
                        list.add(
                            Game(
                                document["image"] as Uri,
                                document["name"] as String,
                                document["year"] as Int,
                                document["overview"] as String
                            )
                        )
                    }
                    games.value = list
                } else {
                    Log.w("TA G", "Error getting documents.", task.exception)
                }
            }
    }
}