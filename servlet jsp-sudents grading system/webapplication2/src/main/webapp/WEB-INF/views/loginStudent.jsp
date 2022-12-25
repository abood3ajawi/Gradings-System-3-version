<html>
<head>
<title>Yahoo!!</title>
</head>
<body>
<h1>Student Login </h1>
    <p><font color="red">${errorMessage}</font></p>
    <form action="/login-student" method="POST">
        Name : <input name="name" type="text" /> Password : <input name="password" type="password" /> <input type="submit" />
    </form>
</body>
</html>