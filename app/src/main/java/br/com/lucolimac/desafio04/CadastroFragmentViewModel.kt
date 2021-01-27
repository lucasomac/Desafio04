package br.com.lucolimac.desafio04

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.lucolimac.desafio04.model.Game
import com.google.firebase.firestore.FirebaseFirestore


class CadastroFragmentViewModel : ViewModel() {
    // Create a storage reference from the app
    var dbFireStore = FirebaseFirestore.getInstance()
    var crFireStorage = dbFireStore.collection("games")
    val games = MutableLiveData<List<Game>>()
    val game = MutableLiveData<Game>()
    val photo = MutableLiveData<String>()
    fun getGameToInsert(
        image: String,
        name: String,
        year: Int,
        overview: String
    ): MutableMap<String, String> {
        val game: MutableMap<String, String> = HashMap()
        game["image"] = image
        game["name"] = name
        game["year"] = year.toString()
        game["overview"] = overview
        return game
    }

    fun sendGame(game: MutableMap<String, String>) {
        val nome = game["name"]
        crFireStorage.document(nome.toString()).set(game).addOnSuccessListener {
//            Log.i("STORE", it.toString())
        }.addOnFailureListener {
//            Log.i("STORE", it.toString())
        }
    }

    fun updateGame(name: String, game: MutableMap<String, String>) {
        crFireStorage.document(name).update(game as Map<String, Any>)

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

    fun setUrlImage(image: String) {
        photo.value = image
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
    //Configura a imagem para


}