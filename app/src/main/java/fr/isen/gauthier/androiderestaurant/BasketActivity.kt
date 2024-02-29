package fr.isen.gauthier.androiderestaurant

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ButtonDefaults

import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import kotlin.math.max

class BasketActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var totalPrice = 0
        val basketItems = Basket.current(this).items.toMutableList()
        BasketState.updateItemCountInBasket(this)
        totalPrice = totalPriceUpdate(totalPrice, basketItems)


        //Appel de la fonction pour compter le nombre d'element dans le panier
        val countItem = mutableStateOf(countItems(basketItems))
        countItem.value = countItems(basketItems)

        setContent {
            BasketView(totalPrice, BasketState.itemCountInBasket.value)
        }
    }


}

//Fonction countItems pour compter le nombre d'element dans le panier
public fun countItems(basketItems: MutableList<BasketItem>): Int {
    var count = 0
    for (item in basketItems) {
        count += item.count
    }
    return count
}


fun totalPriceUpdate(initialTotalPrice: Int, basketItems: MutableList<BasketItem>): Int {
    var totalPrice = initialTotalPrice
    basketItems.map { totalPrice += (it.dish.prices.first().price.toInt() * it.count) }

    return totalPrice
}


@Composable
fun BasketView(initialTotalPrice: Int, countItem: Int) {
    val context = LocalContext.current
    val basketItems = remember { mutableStateListOf<BasketItem>() }
    val totalPrice = remember { mutableIntStateOf(initialTotalPrice) }


    Surface(
        modifier = Modifier.fillMaxSize(),
        color = colorResource(id = R.color.background)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Votre panier",
                fontSize = 30.sp,
                fontFamily = FontFamily.Cursive,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp, horizontal = 8.dp)
            )
            LazyColumn {
                items(basketItems) {
                    BasketItemView(it, basketItems, totalPrice)
                }
            }
            basketItems.addAll(Basket.current(context).items)

            Box {
                OutlinedButton(
                    onClick = {
                        Toast.makeText(
                            context,
                            "Commande validée en preparation",
                            Toast.LENGTH_LONG
                        ).show()
                    },
                    modifier = Modifier
                        .padding(10.dp),

                    ) {
                    Text(
                        text = "Panier : ${totalPrice.intValue} €",
                        color = Color.Black
                    ) // Utilisez la valeur de totalPrice
                }
            }
        }
    }
}


@Composable
fun BasketItemView(
    item: BasketItem,
    basketItems: MutableList<BasketItem>,
    totalPrice: MutableState<Int>
) {
    Card {

        val context = LocalContext.current
        val count = remember {
            mutableIntStateOf(item.count)
        }
        Card(
            border = BorderStroke(1.dp, Color.Black),
            modifier = Modifier
                .padding(start = 10.dp, end = 10.dp)
                .padding(vertical = 15.dp)
        ) {
            Row(Modifier.padding(8.dp)) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(item.dish.images.first())
                        .build(),
                    null,
                    placeholder = painterResource(R.drawable.ic_launcher_foreground),
                    error = painterResource(R.drawable.ic_launcher_foreground),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .width(80.dp)
                        .height(80.dp)
                        .clip(RoundedCornerShape(10))
                        .padding(8.dp)
                )
                Column(
                    Modifier
                        .align(alignment = Alignment.CenterVertically)
                        .padding(8.dp)
                ) {
                    Text(item.dish.name)
                    Text("${item.dish.prices.firstOrNull()?.price} €")
                }

                Spacer(Modifier.weight(1f))

                Column(
                    Modifier
                        .align(alignment = Alignment.CenterVertically)
                        .padding(8.dp)
                ) {


//                    Spacer(Modifier.weight(1f))
                    Row {
                        OutlinedButton(
                            onClick = {
                                if (count.value > 1) {

                                    count.value = max(1, count.value - 1)

                                    totalPrice.value -= item.dish.prices.firstOrNull()?.price?.toInt()
                                        ?: 0
                                    BasketState.updateItemCountInBasket(context)
                                    Toast.makeText(
                                        context,
                                        "Suppression d'un produit",
                                        Toast.LENGTH_LONG
                                    )
                                        .show()
                                    //updateItemQuantityInSharedPreferences(context, item, count.value)
                                }
                            },
                            Modifier.size(25.dp)
                        )
                        {
                            Image(
                                painter = painterResource(R.drawable.moins),
                                contentDescription = "-",
                                modifier = Modifier
                                    .size(20.dp)
                                    .background(color = Color.Transparent, shape = CircleShape)
                            )

                        }
                        Text(
                            count.value.toString(),
                            modifier = Modifier
                                .padding(start = 8.dp, end = 8.dp),
                            fontSize = 20.sp
                        )



                        OutlinedButton(
                            onClick = {
                                count.value = count.value + 1
                                totalPrice.value += (item.dish.prices.firstOrNull()?.price?.toInt()
                                    ?: 0)
                                BasketState.updateItemCountInBasket(context)
                                Toast.makeText(context, "Rajout d'un produit", Toast.LENGTH_LONG)
                                    .show()
                            },
                            Modifier
                                .size(25.dp)
                        )
                        {
                            Image(
                                painter = painterResource(R.drawable.plus),
                                contentDescription = "+",
                                modifier = Modifier
                                    .size(70.dp)
                                    .background(color = Color.Transparent, shape = CircleShape)
                            )


                        }
                    }
                }

                Spacer(Modifier.weight(1f))

                OutlinedButton(
                    onClick = {
                        Basket.current(context).delete(item, context)
                        basketItems.clear()
                        basketItems.addAll(Basket.current(context).items)
                        totalPrice.value = totalPriceUpdate(0, basketItems)
                    },
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red), // Changez la couleur du texte ici
                    border = BorderStroke(0.dp, Color.Transparent)
                ) {
                    Text(
                        "X",
                        fontSize = 15.sp,
                        color = Color.Red
                    )
                }
            }
        }
    }
}