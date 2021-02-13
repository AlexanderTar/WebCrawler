import org.slf4j.LoggerFactory
import java.lang.Exception
import java.util.concurrent.*

class CrawlerWorker(
    private val crawlerFactory: IPageCrawlerFactory,
    private val url: String,
    private val executorService: ExecutorService,
    private val urls: MutableMap<String, Int>,
    private val maxDepth: Int = Int.MAX_VALUE
) : Runnable {
    private val logger = LoggerFactory.getLogger(javaClass)

    override fun run() = try {
        logger.debug("Attempting crawling $url")
        val parsedUrls = crawlerFactory.create(url).crawl()
        synchronized(urls) {
            val depth = urls[url]!!
            if (depth >= maxDepth) return
            parsedUrls.filter { !urls.contains(it) }.forEach { it ->
                urls[it] = depth + 1
                executorService.execute(CrawlerWorker(crawlerFactory, it, executorService, urls, maxDepth))
            }
        }
        logger.debug("Finished crawling $url")
    } catch (e: Exception) {
        logger.warn("Exception occurred while crawling $url: $e")
    }
}