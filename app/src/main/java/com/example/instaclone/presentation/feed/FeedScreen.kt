package com.example.instaclone.presentation.feed

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.instaclone.DestinationScreen
import com.example.instaclone.R
import com.example.instaclone.common.*
import com.example.instaclone.data.PostData
import com.example.instaclone.presentation.BottomNavigationItem
import com.example.instaclone.presentation.BottomNavigationMenu
import com.example.instaclone.presentation.MainViewModel
import com.google.android.gms.common.internal.service.Common
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun FeedScreen(
    navController: NavController,
    viewModel: MainViewModel
) {
    val userDataLoading = viewModel.inProgress.value
    val userData = viewModel.userData.value
    val personalizatedFeed = viewModel.postsFeed.value
    val personalizedFeedLoading = viewModel.postsFeedProgress.value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(Color.White)
        ) {
            UserImageCard(userImage = userData?.imageUrl)
        }
        PostsList(
            posts = personalizatedFeed,
            modifier = Modifier.weight(1f),
            loading = personalizedFeedLoading or userDataLoading,
            navController = navController,
            viewModel = viewModel,
            currentUserId = userData?.userId ?: ""
        )
        BottomNavigationMenu(
            selectedItem = BottomNavigationItem.FEED,
            navController = navController,
            viewModel
        )
    }
}

@Composable
fun PostsList(
    posts: List<PostData>,
    modifier: Modifier,
    loading: Boolean,
    navController: NavController,
    viewModel: MainViewModel,
    currentUserId: String
) {
    Box(modifier = modifier) {
        LazyColumn {
            items(posts) {
                Post(
                    post = it,
                    currentUserId = currentUserId,
                    viewModel
                ) {
                    navigateTo(navController, DestinationScreen.SinglePost, NavParam("post", it))
                }
            }
        }
        if (loading) {
            CircularProgressSpinner()
        }
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun Post(post: PostData, currentUserId: String, viewModel: MainViewModel, onPostClick: () -> Unit) {
    val likeAnimation = remember { mutableStateOf(false) }
    val dislikeAnimation = remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(corner = CornerSize(4.dp)),
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(top = 4.dp, bottom = 4.dp)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Card(
                    shape = CircleShape, modifier = Modifier
                        .padding(4.dp)
                        .size(32.dp)
                ) {
                    CommonImage(data = post.userImage, contentScale = ContentScale.Crop)
                }
                Text(text = post.username ?: "", modifier = Modifier.padding(4.dp))
            }
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                val modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onDoubleTap = {
                                if (post.likes?.contains(currentUserId) == true) {
                                    dislikeAnimation.value = true
                                } else {
                                    likeAnimation.value = true
                                }
                                viewModel.onLikePost(post)
                            },
                            onTap = {
                                onPostClick.invoke()
                            }
                        )
                    }

                CommonImage(
                    data = post.postImage,
                    modifier = modifier,
                    contentScale = ContentScale.Inside
                )

                if (likeAnimation.value) {
                    CoroutineScope(Dispatchers.Main).launch {
                        delay(1000L)
                        likeAnimation.value = false
                    }
                    LikeAnimation()
                }

                if (dislikeAnimation.value) {
                    CoroutineScope(Dispatchers.Main).launch {
                        delay(1000L)
                        dislikeAnimation.value = false
                    }
                    LikeAnimation(false)
                }
            }
            Row(Modifier.fillMaxWidth()) {
                Icon(painter = painterResource(id = R.drawable.ic_like), contentDescription = "", tint = Color.Red)
                Text(text = "${post.likes?.size}")

            }
        }
    }
}
