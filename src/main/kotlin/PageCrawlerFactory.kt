interface IPageCrawlerFactory {
    val rootUrl: String

    fun create(url: String): IPageCrawler
}

class PageCrawlerFactory(override val rootUrl: String) : IPageCrawlerFactory {
    override fun create(url: String): IPageCrawler =
        PageCrawler(PageLoader(), rootUrl, url)
}
