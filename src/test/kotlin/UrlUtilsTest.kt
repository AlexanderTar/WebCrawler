import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class UrlUtilsTest {
    @Test
    fun `absoluteUrl returns URL as is if it's absolute`() {
        // GIVEN
        val rootUrl = "https://acme.com"
        val url = "https://acme.com/about"

        // WHEN
        val absolute = absoluteUrl(rootUrl, url)

        // THEN
        assertEquals(url, absolute)
    }

    @Test
    fun `absoluteUrl returns absolute URL if relative was provided`() {
        // GIVEN
        val rootUrl = "https://acme.com"
        val url = "/about"

        // WHEN
        val absolute = absoluteUrl(rootUrl, url)

        // THEN
        assertEquals(rootUrl + url, absolute)
    }
}