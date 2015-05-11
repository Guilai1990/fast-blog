<!DOCTYPE html>
<html>
<head>
    <title>Create New Post</title>

    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap-theme.min.css">
</head>

<body>
<div class="container">
    <div class="row">
        <div class="col-xs-12">
            <h1>Listing All Posts</h1>

            <ul>
                <#list posts as post>
                    <li>${post.title}: ${post.id}, ${post.body}</li>
                </#list>
            </ul>

        </div>
    </div>
</div>
</body>
</html>