import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.util.regex.Pattern

// Mock convertLatexToImage for testing without network calls
fun convertLatexToImage(latex: String): String {
    return "https://api.hk.jmstrand.cn/latex_to_image?latex_code=${URLEncoder.encode(latex, StandardCharsets.UTF_8.toString())}"
}

fun processMessageWithLatex(message: String): String {
    // Regex pattern to match %%...%% wrapped LaTeX expressions
    val latexPattern = Pattern.compile("%%(.*?)%%", Pattern.DOTALL)
    val matcher = latexPattern.matcher(message)

    val processedMessage = StringBuffer()
    while (matcher.find()) {
        val latexCode = matcher.group(1) // Extract the LaTeX code inside %%
        val imageUrl = convertLatexToImage(latexCode)
        val imgTag = "<img src=\"$imageUrl\" alt=\"$latexCode\"/>"
        matcher.appendReplacement(processedMessage, imgTag)
    }
    matcher.appendTail(processedMessage)

    return processedMessage.toString()
}

fun main() {
    val testMessages = listOf(
        "Here is an equation in brackets %%\\[E=mc^2\\]%%",
        "And here is one in parentheses %%\\(E=mc^2\\)%%",
        "Lastly, here is a block equation %%\\begin{equation}E=mc^2\\end{equation}%%"
    )

    for (message in testMessages) {
        val processedMessage = processMessageWithLatex(message)
        println("Original: $message")
        println("Processed: $processedMessage")
        println()
    }
}
