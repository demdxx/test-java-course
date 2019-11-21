<!doctype html>
<html lang="en">
  <head>
    <title>${title}</title>
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <link rel="stylesheet" href="/bootstrap.min.css" />
  </head>
  <body>
    <div class="container">
      <div class="row">
        <div class="col-7">
          <h1>${title}</h1>
          <ul>
            <#list movies as movie>
              <li>${movie.getId()} - ${movie.getTitle()}</li>
            </#list>
          </ul>
        </div>
        <div class="col-5">
          <h3>Get movie</h3>
          <code><pre>
          curl -X GET http://0.0.0.0:4567/api/movies
          curl -X GET http://0.0.0.0:4567/api/movies/5dd5ad1ea8109047a6a3e479
          </pre></code>
          <h3>Create movie</h3>
          <code><pre>
          curl -X POST \
            http://0.0.0.0:4567/api/movies \
            -H 'Content-Type: application/json' \
            -d '{
              "title": "Movie name",
              "poster": "http://cdn.shopify.com/s/files/1/1148/8924/products/MPW-102952-a_1024x1024.jpg?v=1556255275"
            }'
          </pre></code>
          <h3>Update movie</h3>
          <code><pre>
          curl -X PUT \
            http://0.0.0.0:4567/api/movies/5dd5ad1ea8109047a6a3e479 \
            -H 'Content-Type: application/json' \
            -d '{
              "title": "Movie name",
              "poster": "http://cdn.shopify.com/s/files/1/1148/8924/products/MPW-102952-a_1024x1024.jpg?v=1556255275"
            }'
          </pre></code>
          <h3>Delete movie</h3>
          <code><pre>
          curl -X DELETE http://0.0.0.0:4567/api/movies/5dd5ad1ea8109047a6a3e479
          </pre></code>
        </div>
      </div>
    </div>
    <footer class="container">Java Programming - Harbour.Space University</footer>
  </body>
</html>