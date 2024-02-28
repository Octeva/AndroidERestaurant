package fr.isen.gauthier.androiderestaurant


import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton

import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import fr.isen.gauthier.androiderestaurant.Network.Dish
import kotlin.math.max


class DetailActivity : ComponentActivity() {

    @OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dish = intent.getSerializableExtra(DISH_EXTRA_KEY) as? Dish

        setContent {

            val context = LocalContext.current
            val count = remember {
                mutableIntStateOf(1)
            }
            Surface(
                color = colorResource(id = R.color.background),
                modifier = Modifier.fillMaxWidth()
            ) {

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Row {


                        Image(
                            painter = painterResource(R.drawable.manateelogo),
                            contentDescription = "logo picture",
                            modifier = Modifier
                                // Espacer a gauche et en haut
                                .padding(top = 5.dp)
                                // Taille de l'image
                                .size(100.dp)
                                // Forme de l'image
                                .clip(CircleShape)
                        )
                        Spacer(modifier = Modifier.weight(1f))


//                        Text(
//                            text = "Nombre d'articles dans le panier : ${BasketState.itemCountInBasket.value}",
//                            modifier = Modifier
//                                .padding(end = 15.dp)
//                                .align(Alignment.CenterVertically)
                        //)
                        Box(modifier = Modifier
                            .wrapContentSize(Alignment.TopEnd)
                            .padding(end = 5.dp)
                        ) {
                            Button(
                                onClick = {
                                    val intent = Intent(context, BasketActivity::class.java)
                                    context.startActivity(intent)
                                },
                                modifier = Modifier
                                    .size(80.dp)
                                    .background(color = Color.Transparent, shape = CircleShape)
                            ) {
                                Image(
                                    painter = painterResource(R.drawable.panier),
                                    contentDescription = "logo picture",
                                    modifier = Modifier
                                        .size(70.dp)
                                        .background(color = Color.Transparent, shape = CircleShape)
                                )
                            }
                            // Vignette ronde
                            Text(
                                text = BasketState.itemCountInBasket.value.toString(),
                                color = Color.White,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .background(color = Color.Red, shape = CircleShape)
                                    .padding(4.dp)
                                    .align(Alignment.TopEnd)
                            )
                        }


                    }
                    //TopAppBar({ Text(dish?.name ?: "") })
                    dish?.let {
                        Text(
                            text = it.name,
                            fontFamily = FontFamily.Cursive,
                            fontSize = 40.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .padding(start = 15.dp)
                        )
                    }



                    Box(
                        modifier = Modifier
                            .padding(top = 10.dp, start = 40.dp, end = 40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        HorizontalPager(state = rememberPagerState {
                            dish?.images?.count() ?: 0
                        }) { caroussel ->
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(dish?.images?.get(caroussel))
                                    .build(),
                                null,
                                placeholder = painterResource(R.drawable.ic_launcher_foreground),
                                error = painterResource(R.drawable.ic_launcher_foreground),
                                contentScale = ContentScale.Fit,
                                modifier = Modifier
                                    .width(400.dp)
                                    .clip(RoundedCornerShape(10))

                            )
                        }

                    }


                    Text(
                        text = "Ingrédients : ",
                        fontSize = 20.sp,
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(top = 20.dp)

                    )


                    val ingredient = dish?.ingredient?.map { it.name }?.joinToString(", ") ?: ""

                    Text(
                        text = ingredient,
                        fontSize = 15.sp,
                        fontFamily = FontFamily.Serif,
                        fontStyle = FontStyle.Italic
                    )


                    Row(
                        modifier = Modifier
                            .padding(top = 30.dp)
                    ) {

                        //Bouton -
                        OutlinedButton(onClick = {
                            count.value = max(1, count.value - 1)
                            Toast.makeText(context, "- 1 Article", Toast.LENGTH_LONG).show()
                        })
                        {
                            Text(
                                text = "-",
                                fontSize = 20.sp
                            )
                        }

                        //Quantite
                        Text(
                            text = count.value.toString(),
                            fontSize = 30.sp,
                            fontFamily = FontFamily.Monospace,
                            modifier = Modifier.padding(horizontal = 24.dp)
                        )

                        //Bouton +
                        OutlinedButton(onClick = {
                            count.value = count.value + 1
                            Toast.makeText(context, "+ 1 article", Toast.LENGTH_LONG).show()
                        })
                        {
                            Text(
                                text = "+",
                                fontSize = 20.sp
                            )
                        }


                    }

                    Button(onClick = {
                        if (dish != null) {
                            Basket.current(context).add(dish, count.value, context)
                            // Mise à jour immédiate de l'état pour refléter le changement
                            BasketState.updateItemCountInBasket(context)
                        }
                    }) {
                        Text(
                            text = "Ajouter au panier",
                            fontSize = 20.sp
                        )
                    }
                    Text(
                        text = dish?.prices?.first()?.price + " €",
                        fontFamily = FontFamily.Serif,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Thin,
                        fontStyle = FontStyle.Italic,
                        modifier = Modifier
                            .padding(top = 15.dp)
                    )


                }
            }
        }
    }

    @Composable
    fun updateBasketCount(basketItems: MutableList<BasketItem>): MutableIntState {
        val itemCountInBasket = remember { mutableIntStateOf(countItems(basketItems)) }
        return itemCountInBasket

    }


    companion object {
        val DISH_EXTRA_KEY = "DISH_EXTRA_KEY"
    }
}

