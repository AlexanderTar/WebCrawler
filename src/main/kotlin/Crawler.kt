import org.slf4j.LoggerFactory
import java.util.*
import java.util.concurrent.*
import kotlin.system.measureTimeMillis

class Crawler(private val rootUrl: String, private val maxDepth: Int = Int.MAX_VALUE, private val nThreads: Int = 20) {
    private val logger = LoggerFactory.getLogger(javaClass)

    private val executorService = Executors.newWorkStealingPool(nThreads) as ForkJoinPool

    fun crawl(): Set<String> {
        logger.info("Starting crawling job for $rootUrl. Number of threads: $nThreads")
        val urls = mutableMapOf(rootUrl to 0)
        val crawlerFactory = PageCrawlerFactory(rootUrl)

        val elapsed = measureTimeMillis {
            executorService.execute(CrawlerWorker(crawlerFactory, rootUrl, executorService, urls, maxDepth))
            executorService.awaitQuiescence(Long.MAX_VALUE, TimeUnit.NANOSECONDS)
        }

        logger.info("Finished crawling job for $rootUrl. Total time taken: $elapsed ms")

        return urls.keys
    }
}