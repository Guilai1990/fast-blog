<!DOCTYPE html>
<html>
<head>
    <title>Posts</title>

    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap-theme.min.css">
</head>

<body>
<div class="container">
    <div class="row">
        <div class="col-xs-12">
            <h1>Listing All Posts</h1>

            <#if post_creation_succeeded??>
                <#if post_creation_succeeded>
                  <p class="bg-success">Post created!</p>
                <#else>
                  <p class="bg-danger">Error creating post! Please check server/console logs for more information.</p>
                </#if>
            </#if>

            <table class="table">
              <tr>
                <th>Title</th>
                <th>Published</th>
                <th>Date created</th>
              </tr>
              <#list posts as post>
                <tr>
                  <th>${post.title}</th>
                  <th>${post.published?c}</th>
                  <th>${post.createdAt?date}</th>
                </tr>
              </#list>
            </table>
        </div>
    </div>
</div>
</body>
</html>