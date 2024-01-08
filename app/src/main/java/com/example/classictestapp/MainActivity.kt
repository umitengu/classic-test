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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextField
import androidx.compose.material3.OutlinedButton

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            RecipeApp()
        }
    }

    @Composable
    fun RecipeApp() {
        val (text, setText) = remember { mutableStateOf("") }
        val (response, setResponse) = remember { mutableStateOf("") }
        val coroutineScope = rememberCoroutineScope()
        var showDialog by remember { mutableStateOf(false) }
        Column(modifier = Modifier.padding(16.dp)) {
            BasicTextField(
                value = text,
                onValueChange = setText,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Button(onClick = {
                coroutineScope.launch {
                    // ここで非同期処理を行う
                    val apiKey = SecurePreferences.getApiKey(this@MainActivity)
                    if (apiKey == null) {
                        setResponse("APIキーの取得に失敗しました")
                    } else {
                        val result = NetworkService.getChatGptResponse(
                            text,
                            apiKey
                        )
                        setResponse(result)
                    }
                }
            }) {
                Text("送信")
            }
            Button(onClick = {
                showDialog = true
            }) {
                Text("APIキー登録")
            }
            Text(response)
            if (showDialog) {
                ShowApiKeyDialog { showDialog = false }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun ShowApiKeyDialog(onDismissRequest: () -> Unit) {
        var text by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = onDismissRequest,
            title = { Text("APIキーを入力") },
            text = {
                TextField(
                    value = text,
                    onValueChange = { text = it },
                    label = { Text("APIキー") }
                )
            },
            confirmButton = {
                OutlinedButton(onClick = {
                    SecurePreferences.setApiKey(this, text)
                    onDismissRequest()
                }) {
                    Text("保存")
                }
            }
        )
    }
}



