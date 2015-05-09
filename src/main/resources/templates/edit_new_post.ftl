<!DOCTYPE html>
<html>
    <head>
        <title>Create New Post</title>

        <!-- Latest compiled and minified CSS -->
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css">

        <!-- Optional theme -->
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap-theme.min.css">

        <!-- Latest compiled and minified JavaScript -->
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>
    </head>

    <body>
        <h1>New Post</h1>

        <#if post_creation_succeeded??>
            <#if post_creation_succeeded>
                <p class="bg-success">Post created!</p>
            <#else>
                <p class="bg-danger">Error creating post! Please check server/console logs for more information.</p>
            </#if>
        </#if>

        <form action="/posts/create" method="post">
            <div class="form-group">
                <textarea name="body" cols="120" rows="30"></textarea>
            </div>
            <input type="hidden"
                   name="${_csrf.parameterName}"
                   value="${_csrf.token}"/>
            <button type="submit" class="btn btn-default">Submit</button>
        </form>

        <form action="/logout" method="post">
            <input type="hidden"
                   name="${_csrf.parameterName}"
                   value="${_csrf.token}"/>
            <button type="submit" class="btn btn-default">Logout</button>
        </form>
    </body>
</html>