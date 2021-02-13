import kotlinx.cli.*

fun main(args: Array<String>) {
    val parser = ArgParser("WebCrawler")
    val url by parser.option(ArgType.String, shortName = "u", description = "URL").required()
    val output by parser.option(ArgType.String, shortName = "o", description = "Output directory name").required()
    val `max-depth` by parser.option(ArgType.Int, shortName = "d", description = "Max depth for crawling")
        .default(Int.MAX_VALUE)
    val `number-threads` by parser.option(ArgType.Int, shortName = "n", description = "Number of threads").default(20)

    parser.parse(args)

    val crawler = Crawler(url, `max-depth`, `number-threads`)
    val urls = crawler.crawl()

    buildSiteMap(url, urls, output)
}
