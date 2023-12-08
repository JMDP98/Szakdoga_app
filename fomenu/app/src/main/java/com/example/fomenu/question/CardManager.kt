package com.example.activities_tuto

import java.text.FieldPosition

object CardManager {
    private val selectedCards = mutableListOf<Int>()

    fun toggleCardSelection(position: Int){
        if(selectedCards.contains(position)){
            selectedCards.remove(position)
        }
        else{
            selectedCards.add(position)
        }
    }

    fun isCardSelected(position: Int): Boolean{
        return selectedCards.contains(position)
    }

}