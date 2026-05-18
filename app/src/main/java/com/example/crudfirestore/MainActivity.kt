package com.example.crudfirestore

import android.os.Bundle import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.crudfirestore.ui.theme.CRUDFirestoreTheme
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CRUDFirestoreTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    FirestoreCRUDApp(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun FirestoreCRUDApp(modifier: Modifier = Modifier) {
    val db = FirebaseFirestore.getInstance()
    val context = LocalContext.current
    
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var users by remember { mutableStateOf(listOf<User>()) }
    var editingUser by remember { mutableStateOf<User?>(null) }

    // Fetch users
    LaunchedEffect(Unit) {
        db.collection("users").addSnapshotListener { value, error ->
            if (error != null) {
                Toast.makeText(context, "Error fetching: ${error.message}", Toast.LENGTH_SHORT).show()
                return@addSnapshotListener
            }
            if (value != null) {
                val userList = value.documents.map { doc ->
                    User(
                        id = doc.id,
                        name = doc.getString("name") ?: "",
                        email = doc.getString("email") ?: ""
                    )
                }
                users = userList
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Firestore CRUD", style = MaterialTheme.typography.headlineMedium)
        
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (name.isNotEmpty() && email.isNotEmpty()) {
                    val userData = hashMapOf(
                        "name" to name,
                        "email" to email
                    )
                    
                    if (editingUser == null) {
                        // Create
                        db.collection("users")
                            .add(userData)
                            .addOnSuccessListener {
                                Toast.makeText(context, "User Added", Toast.LENGTH_SHORT).show()
                                name = ""
                                email = ""
                            }
                    } else {
                        // Update
                        db.collection("users")
                            .document(editingUser!!.id)
                            .set(userData)
                            .addOnSuccessListener {
                                Toast.makeText(context, "User Updated", Toast.LENGTH_SHORT).show()
                                name = ""
                                email = ""
                                editingUser = null
                            }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (editingUser == null) "Add User" else "Update User")
        }

        if (editingUser != null) {
            TextButton(onClick = {
                editingUser = null
                name = ""
                email = ""
            }) {
                Text("Cancel Edit")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(users) { user ->
                UserItem(
                    user = user,
                    onEdit = {
                        editingUser = user
                        name = user.name
                        email = user.email
                    },
                    onDelete = {
                        db.collection("users").document(user.id).delete()
                            .addOnSuccessListener {
                                Toast.makeText(context, "User Deleted", Toast.LENGTH_SHORT).show()
                            }
                    }
                )
            }
        }
    }
}

@Composable
fun UserItem(user: User, onEdit: () -> Unit, onDelete: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = user.name, style = MaterialTheme.typography.titleMedium)
                Text(text = user.email, style = MaterialTheme.typography.bodySmall)
            }
            Row {
                IconButton(onClick = onEdit) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit")
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete")
                }
            }
        }
    }
}
