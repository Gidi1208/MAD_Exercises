import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun FoodMenuScreen(
    onOrderClick: (String, String) -> Unit
) {

    var selectedFood by remember {
        mutableStateOf("")
    }

    var selectedPrice by remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),

        horizontalAlignment =
            Alignment.CenterHorizontally,

        verticalArrangement =
            Arrangement.Center
    ) {

        Text(
            text = "FOOD ORDERING APP",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        Text(
            text = "Select Your Food",
            fontSize = 20.sp
        )

        Spacer(
            modifier = Modifier.height(24.dp)
        )


        FoodItem(
            foodName = "Burger",
            price = "₹120",
            selected = selectedFood == "Burger",
            onClick = {
                selectedFood = "Burger"
                selectedPrice = "₹120"
            }
        )


        FoodItem(
            foodName = "Pizza",
            price = "₹180",
            selected = selectedFood == "Pizza",
            onClick = {
                selectedFood = "Pizza"
                selectedPrice = "₹180"
            }
        )


        FoodItem(
            foodName = "Sandwich",
            price = "₹100",
            selected = selectedFood == "Sandwich",
            onClick = {
                selectedFood = "Sandwich"
                selectedPrice = "₹100"
            }
        )


        FoodItem(
            foodName = "Pasta",
            price = "₹150",
            selected = selectedFood == "Pasta",
            onClick = {
                selectedFood = "Pasta"
                selectedPrice = "₹150"
            }
        )


        Spacer(
            modifier = Modifier.height(24.dp)
        )


        Button(
            onClick = {

                onOrderClick(
                    selectedFood,
                    selectedPrice
                )

            },

            enabled = selectedFood.isNotEmpty(),

            modifier = Modifier.fillMaxWidth()
        ) {

            Text(
                text = "ORDER NOW"
            )
        }
    }
}


@Composable
fun FoodItem(
    foodName: String,
    price: String,
    selected: Boolean,
    onClick: () -> Unit
) {

    Row(
        modifier = Modifier.fillMaxWidth(),

        verticalAlignment =
            Alignment.CenterVertically
    ) {

        RadioButton(
            selected = selected,
            onClick = onClick
        )

        Text(
            text = "$foodName - $price",
            fontSize = 18.sp
        )
    }
}


@Preview(showBackground = true)
@Composable
fun FoodMenuPreview() {

    MaterialTheme {

        FoodMenuScreen(
            onOrderClick = { _, _ -> }
        )
    }
}