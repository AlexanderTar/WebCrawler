interface IPageCrawler {
    val rootUrl: String
    val url: String

    fun crawl(): Set<String>
}

class PageCrawler(
    private val loader: IPageLoader,
    override val rootUrl: String,
    override val url: String
) : IPageCrawler {

    private val regex = "$rootUrl/.*".toRegex()

    override fun crawl(): Set<String> = loader
        .download(url)
        .select("a[href]")
        .map { it.attr("href") }
        .map { absoluteUrl(rootUrl, it) }
        .filter { regex.matches(it) && it != url }
        .toHashSet()
}
