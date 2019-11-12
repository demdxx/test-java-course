// For test the HTTP need to mock it

package space.harbour.hw5;

import java.net.MalformedURLException;

import org.junit.Assert;
import org.junit.Test;

public class CrawlerTests {
  @Test
  public void parseURLsTest() {
    final String html = "<div class=\"shrubbery\">"
        + "  <h2 class=\"widget-title\"><span aria-hidden=\"true\" class=\"icon-news\"></span>Latest News</h2>"
        + "  <p class=\"give-me-more\"><a href=\"https://blog.python.org\" title=\"More News\">More</a></p>"
        + "  <ul class=\"menu\">" + "    <li>"
        + "      <time datetime=\"2019-11-11T18:04:00+00:00\"><span class=\"say-no-more\">2019-</span>11-11</time>"
        + "      <a href=\"http://feedproxy.google.com/~r/PythonSoftwareFoundationNews/~3/jAMRqiPhWSs/seeking-developers-for-paid-contract.html\">Seeking"
        + "        Developers for Paid Contract Improving pip</a></li>" + "    <li>"
        + "      <time datetime=\"2019-11-02T00:51:00.000005+00:00\"><span class=\"say-no-more\">2019-</span>11-02</time>"
        + "      <a href=\"http://feedproxy.google.com/~r/PythonInsider/~3/D5sAVdxYnzY/python-359-is-released.html\">Python 3.5.9 is"
        + "        released</a></li>" + "    <li>"
        + "      <time datetime=\"2019-10-31T19:24:00.000003+00:00\"><span class=\"say-no-more\">2019-</span>10-31</time>"
        + "      <a href=\"http://feedproxy.google.com/~r/PythonSoftwareFoundationNews/~3/Y4CDqUEnEB8/the-2019-python-developer-survey-is.html\">The"
        + "        2019 Python Developer Survey is here, take a few minutes to complete the survey!</a></li>"
        + "    <li>"
        + "      <time datetime=\"2019-10-30T16:23:00.000002+00:00\"><span class=\"say-no-more\">2019-</span>10-30</time>"
        + "      <a href=\"http://feedproxy.google.com/~r/PythonSoftwareFoundationNews/~3/dQEL0laUUlw/cpython-core-developer-sprint-2019.html\">CPython"
        + "        Core Developer Sprint 2019</a></li>" + "    <li>"
        + "      <time datetime=\"2019-10-29T06:51:00.000001+00:00\"><span class=\"say-no-more\">2019-</span>10-29</time>"
        + "      <a href=\"http://feedproxy.google.com/~r/PythonInsider/~3/Pirbj3v2YiI/python-358-is-now-available.html\">Python"
        + "        3.5.8 is now available</a></li>" + "  </ul>" + "</div>";
    var expected = new String[] { "https://blog.python.org",
        "http://feedproxy.google.com/~r/PythonSoftwareFoundationNews/~3/jAMRqiPhWSs/seeking-developers-for-paid-contract.html",
        "http://feedproxy.google.com/~r/PythonInsider/~3/D5sAVdxYnzY/python-359-is-released.html",
        "http://feedproxy.google.com/~r/PythonSoftwareFoundationNews/~3/Y4CDqUEnEB8/the-2019-python-developer-survey-is.html",
        "http://feedproxy.google.com/~r/PythonSoftwareFoundationNews/~3/dQEL0laUUlw/cpython-core-developer-sprint-2019.html",
        "http://feedproxy.google.com/~r/PythonInsider/~3/Pirbj3v2YiI/python-358-is-now-available.html" };
    var list = Crawler.parseURLs(html);
    Assert.assertNotNull(list);
    if (list != null) {
      Assert.assertArrayEquals("url list", expected, list.toArray());
    }
  }

  @Test
  public void prepareURLTest() {
    try {
      var url = Crawler.prepareURL("ya.net");
      Assert.assertEquals("http://ya.net", url.toString());
      url = Crawler.prepareURL("//google.com");
      Assert.assertEquals("https://google.com", url.toString());
      url = Crawler.prepareURL("http://yahoo.com/news");
      Assert.assertEquals("http://yahoo.com/news", url.toString());
      url = Crawler.prepareURL("https://msn.com/news");
      Assert.assertEquals("https://msn.com/news", url.toString());
    } catch (MalformedURLException e) {
      e.printStackTrace();
      Assert.fail(e.getMessage());
    }
  }
}