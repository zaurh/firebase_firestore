package com.example.instaclone.presentation

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.instaclone.common.CircularProgressSpinner
import com.example.instaclone.data.CommentData
import org.w3c.dom.Comment

@Composable
fun CommentsScreen(
    navController: NavController, viewModel: MainViewModel, postId: String
) {
    var commentText by rememberSaveable { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    val comments = viewModel.comments.value
    val commentsProgress = viewModel.commentsProgress.value

    Column(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.weight(1f)) {
            if (commentsProgress) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressSpinner()
                }
            } else if (comments.isEmpty()) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                        Text(text = "No comments yet.")
                }
            }else{
                LazyColumn(modifier = Modifier.weight(1f)){
                    items(comments){ comment ->
                        CommentRow(comment)
                    }
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            TextField(
                value = commentText,
                onValueChange = { commentText = it },
                modifier = Modifier
                    .weight(1f)
                    .border(1.dp, color = Color.LightGray),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent,
                    textColor = Color.Black,
                    focusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )
            Button(onClick = {
                viewModel.createComment(postId, commentText)
                commentText = ""
                focusManager.clearFocus()
            }, modifier = Modifier.padding(start = 8.dp)) {
                Text(text = "Comment")
            }

        }
    }
}

@Composable
fun CommentRow(comment: CommentData) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(8.dp)) {
        Text(text = comment.username ?: "", fontWeight = FontWeight.Bold)
        Text(text = comment.text ?: "", modifier = Modifier.padding(start = 8.dp))
    }
}