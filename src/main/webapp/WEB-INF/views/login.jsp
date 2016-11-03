<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="shortcut icon" href="../../resources/image/DO_favicon.ico">

    <title>Daou Tech</title>

    <!-- Bootstrap core CSS -->
    <link href="../../resources/libs/bootstrap/css/bootstrap.css" rel="stylesheet">
    <link href="../../resources/libs/bootstrap/css/signin.css" rel="stylesheet">
    <link href="../../resources/libs/bootstrap/css/custom.css" rel="stylesheet">

    <!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
      <script src="../../assets/js/html5shiv.js"></script>
      <script src="../../assets/js/respond.min.js"></script>
    <![endif]-->
  </head>
  <body style="margin-top:0px;">
  	<div class="header">
  		<a href="/" class="logo" title="다우기술">다우기술</a>
  	</div>
  	<div class="navBiz">
	  	<div class="btnBox">
			<span class="heading"></span>
		</div>
	</div>
    <div class="content">
      <form class="form-signin" action="/login" method="POST">
        <h3 class="page_title">로그인</h3>        
      	<p class="page_desc">고객과 함께 비상하는 다우기술</p>
        <input type="text" class="form-control" placeholder="Id" autofocus name="loginId">
        <input type="password" class="form-control" placeholder="Password" name="password">        
        <button class="btn btn-lg btn-primary btn-block" type="submit">로그인</button>
      </form>
    </div> <!-- /container -->
    <!-- Bootstrap core JavaScript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
  </body>
</html>