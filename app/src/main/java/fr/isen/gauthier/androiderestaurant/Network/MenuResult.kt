package fr.isen.gauthier.androiderestaurant.Network

import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class MenuResult (val data: List<Category>)

//data class MenuResult (@SerializedName("name_fr") val data: List<Category>): Serializable