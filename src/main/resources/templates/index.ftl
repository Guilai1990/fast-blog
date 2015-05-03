
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
</head>

<body>
    <p>Hi there!</p>

    <p>Admin? ${isAdmin?c}</p>

    <#if isAdmin>
        <p>You're an admin!</p>
    <#else>
        <p>You're a normal user! Thanks for visiting!</p>
    </#if>
</body>
</html>