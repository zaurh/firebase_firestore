package com.example.instaclone

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.instaclone.common.NotificationMessage
import com.example.instaclone.data.PostData
import com.example.instaclone.presentation.CommentsScreen
import com.example.instaclone.presentation.MainViewModel
import com.example.instaclone.presentation.SinglePostScreen
import com.example.instaclone.presentation.login.LoginScreen
import com.example.instaclone.presentation.feed.FeedScreen
import com.example.instaclone.presentation.profile.EditProfileScreen
import com.example.instaclone.presentation.profile.MyPostsScreen
import com.example.instaclone.presentation.profile.NewPostScreen
import com.example.instaclone.presentation.search.SearchScreen
import com.example.instaclone.presentation.sign_up.SignUpScreen
import com.example.instaclone.ui.theme.InstaCloneTheme
import dagger.hilt.android.AndroidEntryPoint
import org.w3c.dom.Comment

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            InstaCloneTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Navig()
                }
            }
        }
    }
}

sealed class DestinationScreen(val route: String) {
    object SignUp : DestinationScreen("signup")
    object Login : DestinationScreen("login")
    object Feed : DestinationScreen("feed")
    object Search : DestinationScreen("search")
    object Posts : DestinationScreen("posts")
    object Profile : DestinationScreen("profile")
    object NewPost : DestinationScreen("newpost/{imageUri}") {
        fun createRoute(uri: String) = "newpost/$uri"
    }
    object SinglePost : DestinationScreen("singlepost")
    object CommentsScreen: DestinationScreen("comments/{postId}"){
        fun createRoute(postId: String) = "comments/$postId"
    }
}

@Composable
fun Navig() {

    val viewModel = hiltViewModel<MainViewModel>()

    NotificationMessage(viewModel = viewModel)

    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = DestinationScreen.Login.route) {
        composable(DestinationScreen.SignUp.route) {
            SignUpScreen(navController = navController, viewModel)
        }
        composable(DestinationScreen.Login.route) {
            LoginScreen(navController = navController, viewModel)
        }
        composable(DestinationScreen.Feed.route) {
            FeedScreen(navController = navController, viewModel)
        }
        composable(DestinationScreen.Search.route) {
            SearchScreen(navController = navController, viewModel)
        }
        composable(DestinationScreen.Posts.route) {
            MyPostsScreen(navController = navController, viewModel)
        }
        composable(DestinationScreen.Profile.route) {
            EditProfileScreen(navController = navController, viewModel)
        }
        composable(DestinationScreen.NewPost.route) { navBackStackEntry ->
            val imageUri = navBackStackEntry.arguments?.getString("imageUri")
            imageUri?.let {
                NewPostScreen(navController = navController, viewModel = viewModel, encodedUri = it)
            }
        }

        composable(DestinationScreen.SinglePost.route) {
            val postData = navController
                .previousBackStackEntry
                ?.arguments
                ?.getParcelable<PostData>("post")

            postData?.let { SinglePostScreen(
                navController = navController,
                viewModel = viewModel,
                postData = postData
            ) }
        }

        composable(DestinationScreen.CommentsScreen.route){ navBackStackEntry ->
            val postId = navBackStackEntry.arguments?.getString("postId")
            postId?.let {
                CommentsScreen(navController = navController, viewModel = viewModel, postId = it)
            }
        }


    }
}