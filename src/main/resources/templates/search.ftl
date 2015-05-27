
<!DOCTYPE html>
<html>
<head>
    <title>Fast Blog - Search ${query}</title>

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
            <h1>Showing search results for <kbd>${query}</kbd></h1>

            <#list posts as post>
                <h2>${post.title}</h2>
                <p><em>${post.createdAt?number_to_date}</em></p>

                <h4><em>${post.summary}</em></h4>

                <p>${post.body}</p>
            </#list>
        </div>
    </div>
</div>
</body>
</html>