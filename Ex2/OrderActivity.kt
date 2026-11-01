import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

class OrderActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Receive data from MainActivity
        val foodName =
            intent.getStringExtra("food_name")
                ?: "Unknown Food"

        val foodPrice =
            intent.getStringExtra("food_price")
                ?: "₹0"

        setContent {

            MaterialTheme {

                OrderConfirmationScreen(
                    foodName = foodName,
                    foodPrice = foodPrice,
                    onBackClick = {
                        finish()
                    }
                )
            }
        }
    }
}


@Composable
fun OrderConfirmationScreen(
    foodName: String,
    foodPrice: String,
    onBackClick: () -> Unit
) {

    // Animation states
    var showIcon by remember {
        mutableStateOf(false)
    }

    var showTitle by remember {
        mutableStateOf(false)
    }

    var showDetails by remember {
        mutableStateOf(false)
    }

    var showButton by remember {
        mutableStateOf(false)
    }


    // Start animations one after another
    LaunchedEffect(Unit) {

        delay(200)
        showIcon = true

        delay(500)
        showTitle = true

        delay(500)
        showDetails = true

        delay(700)
        showButton = true
    }


    Column(
        modifier = Modifier.fillMaxSize(),

        horizontalAlignment =
            Alignment.CenterHorizontally,

        verticalArrangement =
            Arrangement.Center
    ) {


        // --------------------------------
        // 1. SUCCESS ICON ANIMATION
        // --------------------------------

        AnimatedVisibility(

            visible = showIcon,

            enter =
                scaleIn(
                    animationSpec =
                        tween(
                            durationMillis = 600,
                            easing =
                                FastOutSlowInEasing
                        )
                ) +
                        fadeIn(
                            animationSpec =
                                tween(600)
                        )
        ) {

            AnimatedSuccessIcon()
        }


        Spacer(
            modifier =
                Modifier.height(20.dp)
        )


        // --------------------------------
        // 2. TITLE ANIMATION
        // --------------------------------

        AnimatedVisibility(

            visible = showTitle,

            enter =
                fadeIn(
                    animationSpec =
                        tween(600)
                ) +
                        slideInVertically(
                            animationSpec =
                                tween(600)
                        )
        ) {

            Text(

                text =
                    "ORDER CONFIRMED!",

                fontSize = 28.sp
            )
        }


        Spacer(
            modifier =
                Modifier.height(20.dp)
        )


        // --------------------------------
        // 3. DETAILS ANIMATION
        // --------------------------------

        AnimatedVisibility(

            visible = showDetails,

            enter =
                fadeIn(
                    animationSpec =
                        tween(700)
                ) +
                        slideInVertically(
                            animationSpec =
                                tween(700)
                        )
        ) {

            Column(

                horizontalAlignment =
                    Alignment.CenterHorizontally
            ) {

                Text(

                    text =
                        "Food: $foodName",

                    fontSize = 20.sp
                )

                Spacer(
                    modifier =
                        Modifier.height(10.dp)
                )

                Text(

                    text =
                        "Price: $foodPrice",

                    fontSize = 20.sp
                )

                Spacer(
                    modifier =
                        Modifier.height(15.dp)
                )

                Text(

                    text =
                        "Thank you for your order!",

                    fontSize = 18.sp
                )
            }
        }


        Spacer(
            modifier =
                Modifier.height(30.dp)
        )


        // --------------------------------
        // 4. BUTTON ANIMATION
        // --------------------------------

        AnimatedVisibility(

            visible = showButton,

            enter =
                fadeIn(
                    animationSpec =
                        tween(700)
                ) +
                        scaleIn(
                            animationSpec =
                                tween(700)
                        )
        ) {

            Button(

                onClick = onBackClick
            ) {

                Text(
                    text =
                        "BACK TO MENU"
                )
            }
        }
    }
}


@Composable
fun AnimatedSuccessIcon() {

    var startAnimation by remember {
        mutableStateOf(false)
    }

    val scale by animateFloatAsState(

        targetValue =
            if (startAnimation) 1f
            else 0.3f,

        animationSpec =
            tween(
                durationMillis = 700,
                easing =
                    FastOutSlowInEasing
            ),

        label = "Success Icon Scale"
    )


    LaunchedEffect(Unit) {

        startAnimation = true
    }


    Text(

        text = "🎉",

        fontSize = 70.sp,

        modifier =
            Modifier.graphicsLayer {

                scaleX = scale
                scaleY = scale
            }
    )
}


@Preview(
    showBackground = true
)
@Composable
fun OrderConfirmationPreview() {

    MaterialTheme {

        OrderConfirmationScreen(

            foodName = "Pizza",

            foodPrice = "₹180",

            onBackClick = {}
        )
    }
}