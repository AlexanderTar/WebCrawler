# WebCrawler

This is a simple console application tasked to crawl entire contents of the given website. Application is written in Kotlin.

### Building

WebCrawler uses `gradle` as a build system. To build project locally simply run

```
./gradlew build
```

### Testing

To run unit test suite for a project run

```
./gradlew test
```

### Running

WebCrawler expects following command line arguments:

--url - root URL to crawl (required)
--output - output directory for crawling results (XML sitemap) (required)
--max-depth - maximum depth for crawling (optional, default - unbounded)
--number-threads - number of worker threads for the job (optional, default - 20)

To run application run:

```
./gradlew run --args="--url https://acme.com --output <output-dir>"
```