package cz.kochchy.subsampling_image_compose

import RatingWidget
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import cz.kochchy.subsampling_image_compose.ui.theme.SampleTheme
import io.github.kochchy.SubsamplingImage
import kotlinx.coroutines.launch
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalPagerApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SampleTheme {
                Surface(color = MaterialTheme.colors.background) {
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState())
                    ) {
                        SubsamplingImageComposeWidgetSample()
                        RatingBarComposeWidgetSample()
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalPagerApi::class)
    @Composable
    private fun SubsamplingImageComposeWidgetSample() {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
            Text(
                modifier = Modifier.padding(top = 16.dp, start = 16.dp, bottom = 4.dp, end = 16.dp),
                text = "subsampling-image-compose",
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Light,
                fontSize = 18.sp,
                fontStyle = FontStyle.Normal,
                color = Color.Black
            )

            val items = (0..1000).map { "https://burzovnisvet.cz/wp-content/uploads/2021/06/pes.jpeg" }
            val pagerState = rememberPagerState(pageCount = items.count())

            val scope = rememberCoroutineScope()

            HorizontalPager(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp), state = pagerState
            ) {
                Box(contentAlignment = Alignment.Center) {
                    SubsamplingImage(
                        modifier = Modifier.fillMaxWidth().height(180.dp),
                        data = items.getOrNull(it),
                        maxScale = 10f,
                    )
                    Text(
                        text = "index: $it",
                        color = Color.White,
                        fontSize = 18.sp
                    )
                }
            }

            Button(modifier = Modifier.padding(16.dp),onClick = {
                scope.launch {
                    pagerState.animateScrollToPage(Random.nextInt(items.count()))
                }
            }) {
                Text(text = "scroll to random page")
            }
        }
    }

    @Composable
    private fun RatingBarComposeWidgetSample() {
        val ratingState = remember {
            mutableStateOf(0f)
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
            Text(
                modifier = Modifier.padding(top = 16.dp, start = 16.dp, bottom = 4.dp, end = 16.dp),
                text = "rating-bar-compose",
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Light,
                fontSize = 18.sp,
                fontStyle = FontStyle.Normal,
                color = Color.Black
            )

            Text(text = "input rating bar")

            RatingWidget(
                initialValue = 0f,
                step = 0.5f,
                minRating = 0.5f,
                max = 6,
                spacing = 6.dp,
                size = 24.dp,
                painterFullImage = painterResource(id = R.drawable.ic_star_full),
                painterEmptyImage = painterResource(id = R.drawable.ic_star_empty),
                ratingChanged = {
                    ratingState.value = it
                }
            )

            Text(text = "only display rating bar")

            RatingWidget(
                rating = ratingState.value,
                spacing = 0.dp,
                max = 6,
                size = 36.dp
            )
        }
    }
}