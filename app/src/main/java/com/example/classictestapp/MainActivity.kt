package com.example.classictestapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.classictestapp.ui.theme.ClassicTestAppTheme
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            RecipeApp()
        }
    }
}


@Composable
fun RecipeApp() {
    val apiKey = "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"
    val (text, setText) = remember { mutableStateOf("") }
    val (response, setResponse) = remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    Column(modifier = Modifier.padding(16.dp)) {
        BasicTextField(
            value = text,
            onValueChange = setText,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Button(onClick = {
            coroutineScope.launch {
                // ここで非同期処理を行う
                val result = NetworkService.getChatGptResponse(
                    text,
                    apiKey
                )
                setResponse(result)
            }
        }) {
            Text("送信")
        }
        Text(response)
    }
}
