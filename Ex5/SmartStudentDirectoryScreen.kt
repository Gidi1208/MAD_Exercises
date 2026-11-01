import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

data class Student(
    val name: String,
    val registerNumber: String,
    val department: String,
    val gender: String
)

@Composable
fun StudentDirectoryApp() {
    val navController = rememberNavController()
    val students = remember {
        listOf(
            Student("Alice Smith", "REG001", "Computer Science", "Female"),
            Student("Bob Jones", "REG002", "Information Technology", "Male"),
            Student("Charlie Brown", "REG003", "Electronics", "Male"),
            Student("Diana Prince", "REG004", "Mechanical", "Female"),
            Student("Ethan Hunt", "REG005", "Civil", "Male"),
            Student("Fiona Gallagher", "REG006", "Electrical", "Female"),
            Student("George Clark", "REG007", "Computer Science", "Male"),
            Student("Hannah Abbott", "REG008", "Information Technology", "Female")
        )
    }

    NavHost(navController = navController, startDestination = "student_list") {
        composable("student_list") {
            StudentListScreen(
                students = students,
                onStudentClick = { student ->
                    val encodedName = Uri.encode(student.name)
                    val encodedRegNo = Uri.encode(student.registerNumber)
                    val encodedDept = Uri.encode(student.department)
                    val encodedGender = Uri.encode(student.gender)
                    navController.navigate("student_details/$encodedName/$encodedRegNo/$encodedDept/$encodedGender")
                }
            )
        }
        composable(
            route = "student_details/{name}/{registerNumber}/{department}/{gender}",
            arguments = listOf(
                navArgument("name") { type = NavType.StringType },
                navArgument("registerNumber") { type = NavType.StringType },
                navArgument("department") { type = NavType.StringType },
                navArgument("gender") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val name = Uri.decode(backStackEntry.arguments?.getString("name") ?: "")
            val registerNumber = Uri.decode(backStackEntry.arguments?.getString("registerNumber") ?: "")
            val department = Uri.decode(backStackEntry.arguments?.getString("department") ?: "")
            val gender = Uri.decode(backStackEntry.arguments?.getString("gender") ?: "")
            StudentDetailsScreen(
                name = name,
                registerNumber = registerNumber,
                department = department,
                gender = gender,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}

@Composable
fun StudentListScreen(
    students: List<Student>,
    onStudentClick: (Student) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Smart Student Directory",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(students) { student ->
                StudentItem(student = student, onClick = { onStudentClick(student) })
            }
        }
    }
}

@Composable
fun StudentItem(
    student: Student,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = student.name,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Reg No: ${student.registerNumber}",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "Department: ${student.department}",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun StudentDetailsScreen(
    name: String,
    registerNumber: String,
    department: String,
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
                    .padding(20.dp)
            ) {
                Text(
                    text = "Name: $name",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = "Register Number: $registerNumber",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = "Department: $department",
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