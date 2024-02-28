package fr.isen.gauthier.androiderestaurant

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.GsonBuilder
import fr.isen.gauthier.androiderestaurant.BasketState.updateItemCountInBasket
import fr.isen.gauthier.androiderestaurant.Network.Category
import fr.isen.gauthier.androiderestaurant.Network.Dish
import fr.isen.gauthier.androiderestaurant.Network.MenuResult
import fr.isen.gauthier.androiderestaurant.ui.theme.AndroidERestaurantTheme
import org.json.JSONObject
import fr.isen.gauthier.androiderestaurant.Network.NetworkConstants.Companion as NetworkConstants

class MenuActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Recuperer la clef mis dans les extras
        //as? = cast optionnel
        val type = (intent.getSerializableExtra(CATEGORY_EXTRA_KEY) as? DishType) ?: DishType.STARTER //Si ca crash on va sur la page entree


        setContent {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                MenuView(type)
            }
        }
        Log.d("LifeCycle","Menu Activity - On Create")
    }

    override fun onPause(){
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    //Pour relier avec le HomeActivity en fonction du plat selectionner
    //N'appartient qu'a MenuActivity
    companion object {
        val CATEGORY_EXTRA_KEY = "CATEGORY_EXTRA_KEY"
    }
}

@Composable
//Type entre () permet de se refere au DishType
fun MenuView(type: DishType){
    val context = LocalContext.current
    val panier = Color(0xFF783201).copy(alpha = 0.7f)
    val numberBasketItem = remember {
        mutableIntStateOf(1)
    }

    Surface (
        modifier = Modifier.fillMaxWidth(),
        color = colorResource(id = R.color.background)) {
        val category = remember {
            //<Category?> Elle va gere l'affichage de ma categorie
            mutableStateOf<Category?>(null)
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp)
        ) {//etirer la colonne a la largeur max


            Row(
                modifier = Modifier.padding(end = 15.dp),
                verticalAlignment = Alignment.CenterVertically
            )
            {
                Column(horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)) {

                //Title fait reference a la fonction dans HomeActivity
                Text(
                    text = type.title(),
                    fontSize = 60.sp,
                    fontFamily = FontFamily.Cursive,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .wrapContentWidth(Alignment.CenterHorizontally)

                )}
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


        //Lazy column = gere les grands nombres comparer a column
        LazyColumn{
            category.value?.let {
                items(it.items){
                    dishRow(it)
                }
            }
        }
    }
    postData(type, category)
}}

@Composable
fun dishRow(dish: Dish) {
    val context = LocalContext.current
    val cardColor = Color(0xFF783201).copy(alpha = 0.7f)
    Card (

        modifier = Modifier
            .padding(start = 10.dp, end = 10.dp)
            .padding(vertical = 15.dp)
            .clickable {
                Log.d("click", "click")
                val intent = Intent(context, DetailActivity::class.java)
                intent.putExtra(DetailActivity.DISH_EXTRA_KEY, dish)
                context.startActivity(intent)
            },
        shape = RoundedCornerShape(30.dp)

    )
    {
        Row(modifier = Modifier
            .background(cardColor)
            .fillMaxWidth() // Pour occuper toute la largeur disponible
        ) {
        Text(
            text = dish.name,
            fontFamily = FontFamily.Serif,
            fontSize = 25.sp,
            color = Color.White,
            modifier = Modifier
                .padding(start = 20.dp, top = 10.dp, bottom = 15.dp)

        )
    }

        Row(modifier = Modifier
            .background(cardColor) // Couleur de fond de la carte
            .fillMaxWidth() // Pour occuper toute la largeur disponible
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(dish.images.first())
                    .build(),
                null,
                placeholder = painterResource(R.drawable.ic_launcher_foreground),
                error = painterResource(R.drawable.ic_launcher_background),
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    //.padding(start)
                    .width(1000.dp)
                    .height(100.dp)
                    .clip(RoundedCornerShape(10))
                    .padding(bottom = 20.dp)
                    //.padding(top = 8.dp)

            )

        }

        Row(horizontalArrangement = Arrangement.Center, // Centrer horizontalement les éléments du Row
            modifier = Modifier
                .fillMaxWidth()
                .background(cardColor) // Couleur de fond de la carte
                ){
            Text(
                text = "Plus d'informations : ",
                fontSize = 12.sp,
                fontFamily = FontFamily.SansSerif,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Light,
                color = Color.White,
                modifier = Modifier
                    .padding(bottom = 10.dp)
            )

            Image(
                painter = painterResource(R.drawable.information),
                contentDescription = "Information",
                modifier = Modifier
                    // Taille de l'image
                    .size(20.dp)
                    // Forme de l'image
                    .clip(CircleShape)
            )
        }


    }

}


@Composable
fun postData(type: DishType, category: MutableState<Category?>){
    val currentCategory = type.title()
    val context = LocalContext.current
    val queue = Volley.newRequestQueue(context)
    val params = JSONObject()
    params.put(NetworkConstants.ID_SHOP, "1")

    val request = JsonObjectRequest(
        Request.Method.POST,
        NetworkConstants.URL,
        params,
        {response ->
            Log.d("request", response.toString(2))
            val result = GsonBuilder().create().fromJson(response.toString(), MenuResult::class.java)
            val filteredResult = result.data.first { category -> category.name == currentCategory}
            category.value = filteredResult

        },
        {
            Log.e("request", it.toString())
        }
    )

    queue.add(request)
}


