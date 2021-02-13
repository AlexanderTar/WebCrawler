import org.junit.jupiter.api.Test
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.ForkJoinPool
import java.util.concurrent.TimeUnit
import kotlin.test.assertEquals

val siteMap = mapOf(
    "https://acme.com" to setOf("https://acme.com/help", "https://acme.com/about", "https://acme.com/careers"),
    "https://acme.com/help" to setOf(
        "https://acme.com/help/faq",
        "https://acme.com/help/service",
        "https://acme.com/help/chat"
    ),
    "https://acme.com/about" to setOf("https://acme.com/about/mission", "https://acme.com/about/team"),
    "https://acme.com/careers" to setOf("https://acme.com/careers/openings"),
    "https://acme.com/help/faq" to setOf("https://acme.com/help/faq/banking", "https://acme.com/help/faq/savings")
)

class TestPageCrawler(
    override val url: String, override val rootUrl: String
) : IPageCrawler {
    override fun crawl() = siteMap[url]!!
}

class TestPageCrawlerFactory(override val rootUrl: String) : IPageCrawlerFactory {
    override fun create(url: String) = TestPageCrawler(url, url)
}

class CrawlerWorkerTest {
    @Test
    fun `CrawlerWorker should return all links with unbounded depth`() {
        // GIVEN
        val rootUrl = "https://acme.com"
        val urls = mutableMapOf(rootUrl to 0)
        val executorService = Executors.newWorkStealingPool(5) as ForkJoinPool
        val worker = CrawlerWorker(TestPageCrawlerFactory(rootUrl), rootUrl, executorService, urls)

        // WHEN
        executorService.submit(worker)
        executorService.awaitQuiescence(1000, TimeUnit.MILLISECONDS)

        // THEN
        assertEquals(siteMap.keys + siteMap.values.flatten().toHashSet(), urls.keys)
    }

    @Test
    fun `CrawlerWorker should return links up to specified depth`() {
        // GIVEN
        val rootUrl = "https://acme.com"
        val urls = mutableMapOf(rootUrl to 0)
        val executorService = Executors.newWorkStealingPool(5) as ForkJoinPool
        val worker = CrawlerWorker(TestPageCrawlerFactory(rootUrl), rootUrl, executorService, urls, 1)

        // WHEN
        executorService.submit(worker)
        executorService.awaitQuiescence(1000, TimeUnit.MILLISECONDS)

        // THEN
        assertEquals(setOf(rootUrl) + siteMap[rootUrl]!!, urls.keys)
    }
}