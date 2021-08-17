package cz.kochchy.subsampling_image_compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import cz.kochchy.subsampling_image_compose.ui.theme.Subsampling_image_composeTheme
import io.github.kochchy.SubsamplingImage

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Subsampling_image_composeTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    SubsamplingImage(modifier = Modifier.fillMaxSize(), data = "https://burzovnisvet.cz/wp-content/uploads/2021/06/pes.jpeg")
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Subsampling_image_composeTheme {
        Greeting("Android")
    }
}