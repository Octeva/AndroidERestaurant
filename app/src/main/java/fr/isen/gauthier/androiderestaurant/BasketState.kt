package fr.isen.gauthier.androiderestaurant

import android.content.Context
import androidx.compose.runtime.mutableStateOf

object BasketState {
    val itemCountInBasket = mutableStateOf(0)

    // Fonction pour mettre à jour le nombre d'articles dans le panier
    fun updateItemCountInBasket(context: Context) {
        // Mise à jour de itemCountInBasket avec le nombre actuel d'articles dans le panier
        itemCountInBasket.value = Basket.current(context).items.sumOf { it.count }
    }
}