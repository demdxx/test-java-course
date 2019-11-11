0) Use the same project template archive as for the previous assignment. Don't forget to update project name in pom.xml

1) Implement a simple multi-threaded web crawler:
a. Start with a queue of URLs to web pages that that should be visited (toVisit queue).
b. Each URL should be visited by exactly one thread.
c. There should be a set of URLs that are already visited (alreadyVisited set).
d. When a thread visits a web page it should add all URLs (starting with "http://" or "https://") on that page to the toVisit queue, unless URL is already visited (check if alreadyVisited set contains the URL)
e. Use an ExecutorService to manage threads.
f. Don't forget to add a bunch of jUnit tests for the assignment (please do at least 3 tests). Check if they are executed in Travis CI, so you don't lose points because of configuration issues.
g. You can use this method to retrieve contents of a web page:

```java
public static String getContentOfWebPage(URL url) {
  final StringBuilder content = new StringBuilder();

  try (
    InputStream is = url.openConnection().getInputStream();
    InputStreamReader in = new InputStreamReader(is, "UTF-8");
    BufferedReader br = new BufferedReader(in);
  ) {
    String inputLine;
    while ((inputLine = br.readLine()) != null)
      content.append(inputLine);
  } catch (IOException e) {
    System.out.println("Failed to retrieve content of " + url.toString());
    e.printStackTrace();
  }
  return content.toString();
}
```

2) The code and tests should run in Travis CI and be green (pass successfully). Submit the link.