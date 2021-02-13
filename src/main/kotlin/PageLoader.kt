import org.jsoup.Jsoup
import org.jsoup.nodes.Document

interface IPageLoader {
    fun download(url: String): Document
}

class PageLoader : IPageLoader {
    override fun download(url: String): Document {
        return Jsoup.connect(url).get()
    }
}