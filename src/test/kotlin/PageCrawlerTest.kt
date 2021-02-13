import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class TestPageLoader(urls: Collection<String>) : IPageLoader {
    private val page = urls.joinToString("\n") { "<a href=\"$it\" />" }

    override fun download(url: String): Document {
        return Jsoup.parse(page)
    }
}

class PageCrawlerTest {
    @Test
    fun `PageCrawler should return only links matching pattern`() {
        // GIVEN
        val urls = hashSetOf(
            "https://google.com",
            "https://acme.com/help",
            "https://acme.com/about",
            "https://acme.com/careers",
            "/about/",
            "/help/faq"
        )
        val expectedUrls = hashSetOf(
            "https://acme.com/help",
            "https://acme.com/about",
            "https://acme.com/careers",
            "https://acme.com/about/",
            "https://acme.com/help/faq"
        )
        val loader = TestPageLoader(urls)
        val crawler = PageCrawler(loader, "https://acme.com", "https://acme.com")

        // WHEN
        val links = crawler.crawl()

        // THEN
        assertEquals(expectedUrls, links)
    }

    @Test
    fun `PageCrawler should not return self`() {
        // GIVEN
        val urls = hashSetOf(
            "https://acme.com",
            "https://acme.com/help",
            "https://acme.com/about",
            "https://acme.com/careers"
        )
        val expectedUrls = hashSetOf("https://acme.com/help", "https://acme.com/about", "https://acme.com/careers")
        val loader = TestPageLoader(urls)
        val crawler = PageCrawler(loader, "https://acme.com", "https://acme.com")

        // WHEN
        val links = crawler.crawl()

        // THEN
        assertEquals(expectedUrls, links)
    }
}