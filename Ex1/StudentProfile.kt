import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
fun StudentProfile() {

    var name by remember {
        mutableStateOf("")
    }

    var registerNumber by remember {
        mutableStateOf("")
    }

    var department by remember {
        mutableStateOf("")
    }

    var result by remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),

        horizontalAlignment = Alignment.CenterHorizontally,

        verticalArrangement = Arrangement.Center
    ) {

        // Title
        Text(
            text = "Student Profile",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        // Name
        OutlinedTextField(
            value = name,

            onValueChange = {
                name = it
            },

            label = {
                Text("Name")
            },

            placeholder = {
                Text("Enter your name")
            },

            modifier = Modifier.fillMaxWidth()
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        // Register Number
        OutlinedTextField(
            value = registerNumber,

            onValueChange = {
                registerNumber = it
            },

            label = {
                Text("Register Number")
            },

            placeholder = {
                Text("Enter register number")
            },

            modifier = Modifier.fillMaxWidth()
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        // Department
        OutlinedTextField(
            value = department,

            onValueChange = {
                department = it
            },

            label = {
                Text("Department")
            },

            placeholder = {
                Text("Enter department")
            },

            modifier = Modifier.fillMaxWidth()
        )

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        // Submit Button
        Button(
            onClick = {

                result =
                    "Student Details\n\n" +
                            "Name: $name\n" +
                            "Register Number: $registerNumber\n" +
                            "Department: $department"
            },

            modifier = Modifier.fillMaxWidth()
        ) {

            Text("SUBMIT")
        }

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        // Display Result
        if (result.isNotEmpty()) {

            Text(
                text = result,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}


// Preview
@Preview(
    showBackground = true,
    //showSystemUi = true
)
@Preview(showBackground = true)
@Composable
fun StudentProfilePreview() {
    MaterialTheme {
        StudentProfile()
    }
}