
<!DOCTYPE html>
<html>
<head>
    <title>Fast Blog!</title>

    <!-- Latest compiled and minified CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css">

    <!-- Optional theme -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap-theme.min.css">

    <!-- Latest compiled and minified JavaScript -->
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>

    <link href='http://fonts.googleapis.com/css?family=Open+Sans' rel='stylesheet' type='text/css'>

    <style type="text/css">
      body {
        font-family: 'Open Sans', sans-serif;
      }
    </style>
</head>

<body>
    <div class="container">
        <div class="row">
            <div class="col-xs-12">
                <#if isAdmin>
                    <p>You're an admin!</p>
                <#else>
                    <p>You're a normal user! Thanks for visiting!</p>
                </#if>

                <#list posts as post>
                    <h1>${post.title}</h1>
                    <p><em>${post.createdAt?number_to_date}</em></p>

                    <h4><em>${post.summary}</em></h4>

                    <p>${post.body}</p>
                </#list>
            </div>
        </div>
    </div>
</body>
</html>