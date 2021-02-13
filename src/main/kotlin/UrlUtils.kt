import java.net.URI

fun absoluteUrl(rootUrl: String, url: String): String {
    val uri = URI(url)
    if (!uri.isAbsolute) {
        return URI(rootUrl + url).toString()
    }
    return url
}
