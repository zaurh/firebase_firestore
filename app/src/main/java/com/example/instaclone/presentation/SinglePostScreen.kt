package com.example.instaclone.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.instaclone.DestinationScreen
import com.example.instaclone.R
import com.example.instaclone.common.CommonDivider
import com.example.instaclone.common.CommonImage
import com.example.instaclone.common.navigateTo
import com.example.instaclone.data.PostData
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun SinglePostScreen(
    navController: NavController,
    viewModel: MainViewModel,
    postData: PostData
) {
    val comments = viewModel.comments.value

    LaunchedEffect(key1 = Unit) {
        viewModel.getComments(postData.postId)
    }
    postData.userId?.let {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(8.dp)
        )
        {
            Text(text = "Back", modifier = Modifier.clickable { navController.popBackStack() })
            CommonDivider()
            SinglePostDisplay(
                navController = navController,
                viewModel = viewModel,
                postData = postData,
                numberComments = comments.size
            )
        }

    }
}

@Composable
fun SinglePostDisplay(
    navController: NavController, viewModel: MainViewModel, postData: PostData, numberComments: Int
) {
    val userData = viewModel.userData.value
    val scale = remember { mutableStateOf(1f) }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Card(
                shape = CircleShape, modifier = Modifier
                    .padding(8.dp)
                    .size(32.dp)
            ) {
                Image(
                    contentScale = ContentScale.Crop,
                    painter = rememberImagePainter(data = postData.userImage),
                    contentDescription = "",
                    modifier = Modifier.clickable { }
                )
            }

            Text(text = postData.username ?: "", modifier = Modifier.clickable { })

            if (userData?.userId == postData.userId) {
                // Current user's posts
            } else if (userData?.following?.contains(postData.userId) == true) {
                Text(
                    text = " · Following",
                    color = Color.Gray,
                    modifier = Modifier.clickable { viewModel.onFollowClick(postData.userId!!) })
            } else {
                Text(
                    text = " · Follow",
                    color = Color.Blue,
                    modifier = Modifier.clickable { viewModel.onFollowClick(postData.userId!!) })
            }
        }
    }
    Box(
        modifier = Modifier.pointerInput(Unit) {
            detectTransformGestures { centroid, pan, zoom, rotation ->
                scale.value *= zoom
            }
        }
    ) {
        val modifier = Modifier
            .align(Alignment.Center)
            .fillMaxWidth()
            .height(500.dp)
            .graphicsLayer(
                // adding some zoom limits (min 50%, max 200%)
                scaleX = maxOf(.7f, minOf(8f, scale.value)),
                scaleY = maxOf(.7f, minOf(8f, scale.value)),
            )
        CommonImage(
            data = postData.postImage,
            modifier = modifier,
            contentScale = ContentScale.Inside
        )
    }
    Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
        Image(
            painter = painterResource(id = R.drawable.ic_like),
            contentDescription = "",
            modifier = Modifier.size(24.dp),
            colorFilter = ColorFilter.tint(Color.Red)
        )
        Text(
            text = " ${postData.likes?.size ?: 0} likes",
            modifier = Modifier.padding(start = 0.dp)
        )
    }

    Row(modifier = Modifier.padding(8.dp)) {
        Text(
            text = postData.username ?: "",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.clickable {

            })
        Text(
            text = postData.postDescription ?: "No description.",
            modifier = Modifier.padding(start = 8.dp)
        )
    }

    Row(modifier = Modifier.padding(8.dp)) {
        Text(text = "$numberComments comments", color = Color.Gray, modifier = Modifier
            .padding(start = 8.dp)
            .clickable {
                postData.postId?.let {
                    navController.navigate(DestinationScreen.CommentsScreen.createRoute(it))
                }
            })
    }


    Row(modifier = Modifier.padding(8.dp)) {
        Text(text = postData.time.toString())
    }




}