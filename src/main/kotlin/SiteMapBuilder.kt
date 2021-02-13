import com.redfin.sitemapgenerator.WebSitemapGenerator
import org.slf4j.LoggerFactory
import java.io.File

fun buildSiteMap(rootUrl: String, urls: Set<String>, output: String) {
    val logger = LoggerFactory.getLogger("buildSiteMap")
    logger.info("Saving crawling results to $output")
    val outputDir = File(output)
    if (outputDir.exists()) {
        outputDir.delete()
    }
    outputDir.mkdirs()
    val wsg = WebSitemapGenerator(rootUrl, outputDir)
    urls.forEach { wsg.addUrl(it) }
    wsg.write()
    val indexFile = wsg.writeSitemapsWithIndex()
    logger.info("Index file created: ${indexFile.absolutePath}")
}
