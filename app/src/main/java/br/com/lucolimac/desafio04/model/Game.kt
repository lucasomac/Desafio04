package br.com.lucolimac.desafio04.model

import java.io.Serializable

data class Game(var imageUrl: String, var name: String, var year: String, var overview: String) :
    Serializable {
    override fun toString(): String {
        return "Game(imageUrl='$imageUrl', name='$name', year='$year', overview='$overview')"
    }
}