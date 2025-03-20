package com.example.workouttracker

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

class RegistrationActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RegistrationScreen()
        }
    }
}

@Composable
fun RegistrationScreen() {

    var userName by remember { mutableStateOf("") }

    var userAge by remember { mutableStateOf("") }

    var userWeight by remember { mutableStateOf("") }

    var useremail by remember { mutableStateOf("") }
    var userpassword by remember { mutableStateOf("") }

    val context = LocalContext.current as Activity

    Column(
        modifier = Modifier
            .fillMaxSize(),
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = colorResource(id = R.color.p4))
                .padding(16.dp),
//            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(100.dp))


            Row(
                verticalAlignment = Alignment.CenterVertically,
            )
            {
                Column() {

                    Text(
                        text = "Register!",
                        color = colorResource(id = R.color.black),
                        style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(bottom = 4.dp)
                    )

                    Text(
                        text = "Sign up to be Fit",
                        color = colorResource(id = R.color.black),
                        style = MaterialTheme.typography.bodyMedium,

                        )
                }

                Spacer(modifier = Modifier.weight(1f))

                Image(
                    modifier = Modifier.size(100.dp),
                    painter = painterResource(id = R.drawable.ic_workout_two),
                    contentDescription = "Grocery Store Manager",
                )

            }

            Spacer(modifier = Modifier.height(82.dp))

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
                    .background(color = colorResource(id = R.color.white)),
                value = useremail,
                onValueChange = { useremail = it },
                placeholder = { Text("Email") },

                leadingIcon = {
                    Image(
                        modifier = Modifier.size(36.dp),
                        painter = painterResource(id = R.drawable.ic_email),
                        contentDescription = "email",
                    )
                },

                )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
                    .background(color = colorResource(id = R.color.white)),
                value = userName,
                onValueChange = { userName = it },
                placeholder = { Text("UserName") },
                leadingIcon = {
                    Image(
                        modifier = Modifier.size(36.dp),
                        painter = painterResource(id = R.drawable.ic_user),
                        contentDescription = "userName",
                    )
                },

                )
            Spacer(modifier = Modifier.height(12.dp))

            Row {

                OutlinedTextField(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 12.dp)
                        .background(color = colorResource(id = R.color.white)),
                    value = userAge,
                    onValueChange = { userAge = it },
                    placeholder = { Text("Age") },
                    leadingIcon = {
                        Image(
                            modifier = Modifier.size(36.dp),
                            painter = painterResource(id = R.drawable.ic_age),
                            contentDescription = "age",
                        )
                    },

                    )

                OutlinedTextField(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 12.dp)
                        .background(color = colorResource(id = R.color.white)),
                    value = userWeight,
                    onValueChange = { userWeight = it },
                    placeholder = { Text("Weight") },
                    leadingIcon = {
                        Image(
                            modifier = Modifier.size(36.dp),
                            painter = painterResource(id = R.drawable.ic_weight),
                            contentDescription = "weight",
                        )
                    },

                    )
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
                    .background(color = colorResource(id = R.color.white)),
                value = userpassword,
                onValueChange = { userpassword = it },
                placeholder = { Text("Password") },
                leadingIcon = {
                    Image(
                        modifier = Modifier.size(36.dp),
                        painter = painterResource(id = R.drawable.ic_password),
                        contentDescription = "password",
                    )
                },

                )

            Spacer(modifier = Modifier.height(32.dp))

            Image(
                modifier = Modifier
                    .size(50.dp)
                    .align(Alignment.CenterHorizontally),
                painter = painterResource(id = R.drawable.ic_right_arrow),
                contentDescription = "Grocery Store Manager",
            )

            Spacer(modifier = Modifier.height(12.dp))

        }

        Image(
            painter = painterResource(id = R.drawable.wt_wave_down), // Replace with your actual SVG drawable
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier.align(alignment = Alignment.CenterHorizontally)
        ) {
            Text(
                text = "Already member? ",
                color = colorResource(id = R.color.black),
                style = MaterialTheme.typography.bodyLarge,
            )

            Text(
                text = "Start Tracking",
                color = colorResource(id = R.color.p3),
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Black),
                modifier = Modifier.clickable {
                        context.startActivity(Intent(context, LoginActivity::class.java))
                        context.finish()
                }
            )

            Spacer(modifier = Modifier.height(36.dp))


        }
    }

}

@Preview(showBackground = true)
@Composable
fun RegistrationScreenPreview() {
    RegistrationScreen()
}