import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

/**
 * API実行を行うオブジェクト
 */
object NetworkService {
    private const val apiUrl = "https://api.openai.com/v1/chat/completions"

    /**
     * Chat Completions APIを実行するクラス
     *  param1 input - ChatGPTへの送信文字列
     *  param2 apiKey - API KEY
     *  return ChatGPTの返答文字列
     */
    suspend fun getChatGptResponse(input: String, apiKey: String): String = withContext(Dispatchers.IO) {
        val url = URL(apiUrl)
        val connection = url.openConnection() as HttpURLConnection
        try {
            connection.apply {
                requestMethod = "POST"
                setRequestProperty("Authorization", "Bearer $apiKey")
                setRequestProperty("Content-Type", "application/json")
                doOutput = true

                outputStream.use { os ->
                    val body = """
                        {
                            "model": "gpt-3.5-turbo",
                            "messages": [{"role": "system", "content": "You are a helpful assistant."}, 
                                         {"role": "user", "content": "$input"}]
                        }
                    """.trimIndent()
                    os.write(body.toByteArray())
                }

                val response = inputStream.bufferedReader().use { it.readText() }
                val jsonResponse = JSONObject(response)
                val choices = jsonResponse.getJSONArray("choices")
                if (choices.length() > 0) {
                    val firstChoice = choices.getJSONObject(0)
                    val message = firstChoice.getJSONObject("message")
                    return@withContext message.getString("content")
                } else {
                    return@withContext "No response received"
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
            return@withContext "Error: ${e.message}"
        } finally {
            connection.disconnect()
        }.toString()
    }
}
