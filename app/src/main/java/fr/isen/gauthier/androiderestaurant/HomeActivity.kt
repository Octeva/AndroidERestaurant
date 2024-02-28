package fr.isen.gauthier.androiderestaurant

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import fr.isen.gauthier.androiderestaurant.ui.theme.AndroidERestaurantTheme
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.VerticalAlignmentLine
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment


enum class DishType {
    STARTER, MAIN, DESSERT;

    @Composable
    fun title(): String {
        return when(this) {
            STARTER -> stringResource(id = R.string.menu_starter)
            MAIN -> stringResource(id = R.string.menu_main)
            DESSERT -> stringResource(id = R.string.menu_dessert)
        }
    }
}

interface MenuInterface {
    fun dishPressed(dishType: DishType)
}

class HomeActivity : ComponentActivity(), MenuInterface {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            //getString(R.string.menu_starter)
            AndroidERestaurantTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SetupView(this)
                }
            }
        }
    }

    override fun dishPressed(dishType: DishType) {
        //Toast.makeText(this, "Voici mon toast", Toast.LENGTH_LONG).show()
        val intent = Intent(this,MenuActivity::class.java)
        //Associer avec le companion object de MenuActivity
        intent.putExtra(MenuActivity.CATEGORY_EXTRA_KEY, dishType)
        startActivity(intent)
    }

    override fun onPause() {
        //Log.d avant le super lorsque on va faire disparaitre une page par exemple (Ne plus pouvoir rien faire avec)
        Log.d("LifeCycle", "Home Activity - On Pause")
        super.onPause()
    }

    override fun onResume() {
        //On met le log apres le super lorsque l'on fait apparaitre
        super.onResume()
        Log.d("LifeCycle", "Home Activity - On Resume")
    }

    override fun onDestroy() {
        //Log.d = Afficher dans les logs ce que le programme fait
        Log.d("LifeCycle", "Home Activity - On Destroy")
        super.onDestroy()

    }
}

@Composable
fun SetupView(menu: HomeActivity) {
    Surface (
        modifier = Modifier.fillMaxWidth(),
        color = colorResource(id = R.color.background)
    ) {

        //Contenu du haut de la page
        Row {
            Image(
                painter = painterResource(R.drawable.manateelogo),
                contentDescription = "logo picture",
                modifier = Modifier
                    // Espacer a gauche et en haut
                    .padding(top = 10.dp)
                    // Taille de l'image
                    .size(130.dp)
                    // Forme de l'image
                    .clip(CircleShape)
            )

        Column (horizontalAlignment = Alignment.CenterHorizontally){
            Text(
                text = "Le",
                modifier = Modifier
                    //Espace
                    .padding(start = 1.dp, top = 15.dp),
                // Augmenter la taille du texte
                fontSize = 60.sp,
                // Utiliser une police de caractères différente
                fontFamily = FontFamily.Cursive,
                fontWeight = FontWeight.ExtraBold
            )
            Text(
                text = "Lamentin",
                modifier = Modifier
                    //Espace
                    .padding(start = 1.dp),
                // Augmenter la taille du texte
                fontSize = 60.sp,
                // Utiliser une police de caractères différente
                fontFamily = FontFamily.Cursive,
                fontWeight = FontWeight.ExtraBold
            )
        }

        }



Column (horizontalAlignment = Alignment.CenterHorizontally){


        //separation
        Row(
            modifier = Modifier
                .padding(top = 225.dp),
            horizontalArrangement = Arrangement.spacedBy(100.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.etoilenb),
                contentDescription = "etoiles separation",
                modifier = Modifier
                    .size(20.dp)
            )
            Image(
                painter = painterResource(R.drawable.etoilenb),
                contentDescription = "etoiles separation",
                modifier = Modifier
                    .size(20.dp)

            )
            Image(
                painter = painterResource(R.drawable.etoilenb),
                contentDescription = "etoiles separation",
                modifier = Modifier
                    .size(20.dp)
            )
        }

        CustomButton(type = DishType.STARTER, menu)

        //Separation
        Row(
            modifier = Modifier
                .padding(top = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(100.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.etoilenb),
                contentDescription = "etoiles separation",
                modifier = Modifier
                    .size(20.dp)
            )
            Image(
                painter = painterResource(R.drawable.etoilenb),
                contentDescription = "etoiles separation",
                modifier = Modifier
                    .size(20.dp)

            )
            Image(
                painter = painterResource(R.drawable.etoilenb),
                contentDescription = "etoiles separation",
                modifier = Modifier
                    .size(20.dp)
            )
        }
        CustomButton(type = DishType.MAIN, menu)
        Row(
            modifier = Modifier
                .padding(top = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(100.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.etoilenb),
                contentDescription = "etoiles separation",
                modifier = Modifier
                    .size(20.dp)
            )
            Image(
                painter = painterResource(R.drawable.etoilenb),
                contentDescription = "etoiles separation",
                modifier = Modifier
                    .size(20.dp)

            )
            Image(
                painter = painterResource(R.drawable.etoilenb),
                contentDescription = "etoiles separation",
                modifier = Modifier
                    .size(20.dp)
            )
        }
        CustomButton(type = DishType.DESSERT, menu)
    }
    }
}

@Composable fun CustomButton(type: DishType, menu: MenuInterface) {
    TextButton(
        onClick = { menu.dishPressed(type) },
        modifier = Modifier.padding(vertical = 20.dp)
        ) {
        Text(
            text = type.title(),
            fontSize = 45.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AndroidERestaurantTheme {
        SetupView(HomeActivity())
    }
}