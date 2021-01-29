package br.com.lucolimac.desafio04

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.lucolimac.desafio04.model.Game
import com.google.firebase.firestore.FirebaseFirestore

class GameFragmentViewModel : ViewModel() {
    var dbFireStore = FirebaseFirestore.getInstance()
    var crFireStore = dbFireStore.collection("games")
    val games = MutableLiveData<ArrayList<Game>>()
    fun readGames() {
        crFireStore.get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val list = arrayListOf<Game>()
                    for (document in task.result!!) {
                        list.add(
                            Game(
                                document["image"] as String,
                                document["name"] as String,
                                document["year"] as String,
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

//        items = arrayListOf(
//            Game("", "GOD", 2015, "TESTE"),
//            Game("", "GOD", 2015, "TESTE"),
//            Game("", "GOD", 2015, "TESTE"),
//            Game("", "GOD", 2015, "TESTE"),
//        )