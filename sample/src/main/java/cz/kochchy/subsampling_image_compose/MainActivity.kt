package cz.kochchy.subsampling_image_compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
                    val items = (0..1000).map { "https://burzovnisvet.cz/wp-content/uploads/2021/06/pes.jpeg" }
                    val pagerState = rememberPagerState(pageCount = items.count())

                    val scope = rememberCoroutineScope()

                    Column() {
                        BoxWithConstraints(modifier = Modifier) {
                            val maxWidth = this.maxWidth

                            HorizontalPager(
                                modifier = Modifier
                                    .width(maxWidth)
                                    .height(maxHeight - 80.dp), state = pagerState
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    SubsamplingImage(
                                        modifier = Modifier.width(maxWidth),
                                        data = items.getOrNull(it),
                                        maxScale = 10f
                                    )
                                    Text(
                                        text = "index: $it",
                                        color = Color.White,
                                        fontSize = 18.sp
                                    )
                                }
                            }
                        }
                        Button(onClick = {
                            scope.launch {
                                pagerState.animateScrollToPage(Random.nextInt(items.count()))
                            }
                        }) {
                            Text(text = "scroll to random page")
                        }
                    }
                }
            }
        }
    }
}