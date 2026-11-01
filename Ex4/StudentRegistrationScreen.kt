import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        NavHost(navController = navController, startDestination = "home_screen") {
            composable("home_screen") {
                HomeScreen(
                    onNavigateToDetails = { name, regNo, gender ->
                        val encodedName = Uri.encode(name)
                        val encodedRegNo = Uri.encode(regNo)
                        val encodedGender = Uri.encode(gender)
                        navController.navigate("details_screen/$encodedName/$encodedRegNo/$encodedGender")
                    }
                )
            }
            composable(
                route = "details_screen/{name}/{regNo}/{gender}",
                arguments = listOf(
                    navArgument("name") { type = NavType.StringType },
                    navArgument("regNo") { type = NavType.StringType },
                    navArgument("gender") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val name = Uri.decode(backStackEntry.arguments?.getString("name") ?: "")
                val regNo = Uri.decode(backStackEntry.arguments?.getString("regNo") ?: "")
                val gender = Uri.decode(backStackEntry.arguments?.getString("gender") ?: "")
                DetailsScreen(
                    name = name,
                    regNo = regNo,
                    gender = gender,
                    onBackClick = { navController.popBackStack() }
                )
            }
        }
    }
}

@Composable
fun HomeScreen(onNavigateToDetails: (String, String, String) -> Unit) {
    var name by remember { mutableStateOf("") }
    var regNo by remember { mutableStateOf("") }
    var selectedGender by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val genders = listOf("Male", "Female")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Student Registration",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        OutlinedTextField(
            value = name,
            onValueChange = { name = it; errorMessage = null },
            label = { Text("Student Name") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = regNo,
            onValueChange = { regNo = it; errorMessage = null },
            label = { Text("Register Number") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Gender",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            genders.forEach { gender ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(end = 24.dp)
                ) {
                    RadioButton(
                        selected = selectedGender == gender,
                        onClick = { selectedGender = gender; errorMessage = null }
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = gender, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }

        if (errorMessage != null) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = errorMessage!!,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (name.isBlank() || regNo.isBlank() || selectedGender.isBlank()) {
                    errorMessage = "Please fill in all fields and select gender."
                } else {
                    onNavigateToDetails(name.trim(), regNo.trim(), selectedGender)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Continue")
        }
    }
}

@Composable
fun DetailsScreen(
    name: String,
    regNo: String,
    gender: String,
    onBackClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Student Details",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Name: $name",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = "Register Number: $regNo",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = "Gender: $gender",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onBackClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Back")
        }
    }
}