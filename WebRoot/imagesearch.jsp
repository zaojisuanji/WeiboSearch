<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
request.setCharacterEncoding("utf-8");
System.out.println(request.getCharacterEncoding());
response.setCharacterEncoding("utf-8");
System.out.println(response.getCharacterEncoding());
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
System.out.println(path);
System.out.println(basePath);
%>


<!DOCTYPE HTML>
<html lang="zh-CN" xmlns="http://www.w3.org/1999/xhtml">
<head>
<base href="<%=basePath%>"></base>
<link rel="stylesheet" href="//netdna.bootstrapcdn.com/bootstrap/3.0.3/css/bootstrap.min.css">
<link rel="stylesheet" href="//netdna.bootstrapcdn.com/bootstrap/3.0.3/css/bootstrap-theme.min.css">
<script type="text/javascript" src="http://cdn.bootcss.com/jquery/1.11.1/jquery.min.js"></script>
<script src="//netdna.bootstrapcdn.com/bootstrap/3.0.3/js/bootstrap.min.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>新浪微博-清华-搜索</title>
<style type="text/css">
<!--
#Layer1 {
	position:absolute;
	left:40%;
	top:60%;
	width:404px;
	height:29px;
	z-index:1;
}
#Layer2 {
	position:absolute;
	left:480px;
	top:68px;
	width:446px;
	height:152px;
	z-index:2;
}
.mylayer {
	position:absolute;
	top:   0px;
  	left:  0px;
  	width: 300px; 
  	height:200px;
}
.logo {MARGIN-TOP:90px; MARGIN-LEFT:525px; width:350px; height:150px;}
-->
</style>
</head>
<body>
<img src="/imgpath/1.png" class="logo">
<div class="container" id="Layer1" style="top: 300px; left: 380px; width: 710px;">
	<form class="form-inline" role="form" method="get" action="servlet/ImageServer">
	  <div class="row">
	      <div class="form-group">
	        <label class="sr-only" for="searchInput">Sina</label>
	      	<input class="form-control" id="searchInput" name="query" type="text" size="60" />
	      </div>
		  <input type="submit" class="btn btn-primary" name="wantto" value="千度找贴">
		  <input type="submit" class="btn btn-info" name="wantto" value="千度找人">
	  </div>
	</form>
</div>
</body>
</html>
