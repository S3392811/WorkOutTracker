package s3392811tesside.nikhilsevva.workouttrackerapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WorkOutStarter()
        }
    }
}

@Composable
fun WorkOutStarter() {
    val context = LocalContext.current as Activity
    var showSplash by remember { mutableStateOf(true) }

    DisposableEffect(Unit) {
        val job = CoroutineScope(Dispatchers.Main).launch {
            delay(3000)
            showSplash = false
        }
        onDispose { job.cancel() }
    }

    if (showSplash) {
        SplashScreen()

    } else {


        if (FitnessPrefs.isUserAuthenticated(context)) {
            context.startActivity(Intent(context, WorkoutHomeActivity::class.java))
            context.finish()
        } else {
            context.startActivity(Intent(context, LoginActivity::class.java))
            context.finish()
        }


    }

}

@Composable
fun SplashScreen() {

    Column(
        modifier = Modifier
            .fillMaxSize().padding(WindowInsets.systemBars.asPaddingValues()),
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(color = colorResource(id = R.color.p4))
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "WorkOut Tracking App!",
                textAlign = TextAlign.Center,
                color = colorResource(id = R.color.black),
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(bottom = 4.dp)
            )

            Text(
                text = "Crush Your Limits, Track Your Progress",
                color = colorResource(id = R.color.black),
                style = MaterialTheme.typography.bodyMedium,

                )


            Spacer(modifier = Modifier.height(38.dp))

            Image(
                modifier = Modifier.size(200.dp, 200.dp),
                painter = painterResource(id = R.drawable.workout_icon),
                contentDescription = "Workout Tracker",
            )



            Spacer(modifier = Modifier.weight(1f))

        }

        Image(
            painter = painterResource(id = R.drawable.wt_wave_down),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier.fillMaxWidth()
        )

        Text(
            text = "By",
            color = colorResource(id = R.color.black),
            style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier
                .padding(bottom = 4.dp, end = 12.dp)
                .align(Alignment.End),
            textAlign = TextAlign.Center
        )

        Text(
            text = "Nikhil Sevva",
            color = colorResource(id = R.color.black),
            style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier
                .padding(bottom = 4.dp, end = 12.dp)
                .align(Alignment.End)
        )


        Spacer(modifier = Modifier.height(30.dp))
    }

}


@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    SplashScreen()
}