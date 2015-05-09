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
                    <h1>New Post</h1>

                    <#if post_creation_succeeded??>
                        <#if post_creation_succeeded>
                            <p class="bg-success">Post created!</p>
                        <#else>
                            <p class="bg-danger">Error creating post! Please check server/console logs for more information.</p>
                        </#if>
                    </#if>

                    <form action="/posts/create" method="post">
                        <input type="hidden"
                               name="${_csrf.parameterName}"
                               value="${_csrf.token}"/>

                        <div class="form-group">
                            <label for="title">Title</label>
                            <input type="text" name="title" id="title" class="form-control">
                        </div>

                        <div class="form-group">
                            <label for="summary">Summary</label>
                            <textarea name="summary" id="summary" cols="80" rows="5" class="form-control"></textarea>
                        </div>

                        <div class="form-group">
                            <textarea name="body" cols="80" rows="30" class="form-control"></textarea>
                        </div>

                        <button type="submit" class="btn btn-default">Submit</button>
                    </form>

                    <form action="/logout" method="post">
                        <input type="hidden"
                               name="${_csrf.parameterName}"
                               value="${_csrf.token}"/>
                        <button type="submit" class="btn btn-default">Logout</button>
                    </form>
                </div>
            </div>
        </div>
    </body>
</html>