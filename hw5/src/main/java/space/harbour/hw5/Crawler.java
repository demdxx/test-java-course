package space.harbour.hw5;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class Crawler {
  private ExecutorService executor = Executors.newFixedThreadPool(5);
  public Set<String> visited = Collections.synchronizedSet(new HashSet<String>());

  public Set<String> load(String... urls) throws MalformedURLException {
    var futures = new ArrayList<Future<List<String>>>();
    var result = new HashSet<String>();
    for (var url : urls) {
      final var newURL = prepareURL(url);
      if (!visited.add(newURL.toString())) {
        continue;
      }
      futures.add(executor.submit(() -> {
        System.out.println("Load URL: " + newURL.toString());
        return parseURLs(getContentOfWebPage(newURL));
      }));
    }
    for (var f : futures) {
      try {
        var links = f.get();
        if (links != null)
          result.addAll(links);
      } catch (InterruptedException | ExecutionException e) {
        e.printStackTrace();
      }
    }
    return result;
  }

  public static List<String> parseURLs(String htmlText) {
    var result = new ArrayList<String>();
    Document document = Jsoup.parse(htmlText);
    document.select("a").forEach((elem) -> {
      String href = elem.attr("href");
      if (href.startsWith("http://") || href.startsWith("https://")) {
        result.add(href);
      }
    });
    return result;
  }

  public static String getContentOfWebPage(URL url) {
    final StringBuilder content = new StringBuilder();
    try (var is = url.openConnection().getInputStream();
        var in = new InputStreamReader(is, "UTF-8");
        var br = new BufferedReader(in);) {
      String inputLine;
      while ((inputLine = br.readLine()) != null)
        content.append(inputLine);
    } catch (IOException e) {
      System.out.println("Failed to retrieve content of " + url.toString());
      e.printStackTrace();
    }
    return content.toString();
  }

  public static URL prepareURL(String ustr) throws MalformedURLException {
    if (!ustr.startsWith("http://") && !ustr.startsWith("https://")) {
      if (ustr.startsWith("//")) {
        return new URL("http:" + ustr);
      }
      return new URL("http://" + ustr);
    }
    return new URL(ustr);
  }

  public static void main(String[] args) {
    var crawler = new Crawler();
    var list = Set.of("https://www.oracle.com/corporate/features/jsoup-html-parsing-library.html", "https://github.com");
    while (true) {
      try {
        list = crawler.load(list.toArray(new String[list.size()]));
        if (list == null || list.size() < 1) {
          break;
        }
      } catch (MalformedURLException e) {
        e.printStackTrace();
        break;
      }
      if (crawler.visited.size() >= 50) {
        break;
      }
    }
    System.out.println("VISITED");
    var i = 1;
    for (var s : crawler.visited) {
      System.out.printf("%d: %s\n", i, s);
      i++;
    }
    System.out.println("FINISH");
  }
}
