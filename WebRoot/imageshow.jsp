<%@ page language="java" import="java.util.*" contentType="text/html; charset=utf-8" %>
<%
request.setCharacterEncoding("utf-8");
response.setCharacterEncoding("utf-8");
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String imagePath ="";
%>

<!DOCTYPE HTML>
<html lang="zh-CN" xmlns="http://www.w3.org/1999/xhtml">
<head>
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
	left:210px;
	top:26px;
	width:649px;
	height:32px;
	z-index:1;
}
#Layer2 {
	position:absolute;
	left:29px;
	top:82px;
	width:648px;
	height:602px;
	z-index:2;
}
#Layer3 {
	position:absolute;
	left:28px;
	top:697px;
	width:652px;
	height:67px;
	z-index:3;
}
-->
.tiezi_layer {
	border-radius: 10px;
	background: rgba(66, 139, 202, 0.2);
	margin: 10px;
	padding: 10px;
}
.zf_tiezi_layer {
	border-radius: 10px;
	background: rgba(94, 165, 226, 0.45);
	margin: 20px;
	padding: 12px;
}
.image_layer {
	margin: 3px;
}
.image_group_layer {
	margin: 5px;
}
em{font-style:normal}
</style>
</head>

<body>
<%
	String currentQuery=(String) request.getAttribute("currentQuery");
	int currentPage=(Integer) request.getAttribute("currentPage");
%>
<div class="container" id="Layer1">
	<form class="form-inline" role="form" method="get" action="ImageServer">
	  <div class="row">
	      <div class="form-group">
	        <label class="sr-only" for="searchInput">Sina~Sina~</label>
	      	<input class="form-control" id="searchInput" value="<%=currentQuery%>" name="query" type="text" size="55" />
	      </div>
		  <input type="submit" class="btn btn-primary" name="wantto" value="千度找贴">
		  <input type="submit" class="btn btn-info" name="wantto" value="千度找人">
	  </div>
	</form>
</div>
<div class="container" id="Layer2" style="top: 82px; height: 585px; left: 200px;">
  <div id="imagediv">
  <Table style="left: 0px; width: 500px;">
  <% 
  	String[] nickNames = (String[]) request.getAttribute("nickNames");
  	String[] personlinks = (String[]) request.getAttribute("personlinks");
  	String[] tieziTexts = (String[]) request.getAttribute("tieziTexts");
  	String[] favorites = (String[]) request.getAttribute("favorites");
  	String[] forwardings = (String[]) request.getAttribute("forwardings");
  	String[] comments = (String[]) request.getAttribute("comments");
  	String[] thumbups = (String[]) request.getAttribute("thumbups");
  	String[] images = (String[]) request.getAttribute("images");
  	String[] zf_images = (String[]) request.getAttribute("zf_images");
  	if(nickNames!=null && nickNames.length>0){
  		for(int i=0;i<nickNames.length;i++){
  			String tmp = tieziTexts[i];
  			String[] textsplit = tmp.split("%%%");
  			String[] imagesplit = images[i].split(";");
  			String[] zf_imagesplit = zf_images[i].split(";");
  			%>
  		<div class="tiezi_layer">
	  		<a href="<%=personlinks[i] %>"><h4><font color="black">@<%=nickNames[i] %></font></h4></a>
	  		<p><%=textsplit[0]%></p>
	  		<%
	  		if(imagesplit.length > 0) {
	  			%>
 				<div class="image_group_layer">
 				<%
	  			int cnt = 0;
	  			for(int j=0; j<imagesplit.length; j++) {
	  				if(cnt % 3 == 0 && cnt != 0) {
	  					%> <br> <%
	  				}
	  				cnt++;
	  			%>
	  				<img src="<%=imagesplit[j]%>" class="image_layer">
	  			<% 
	  			}
	  			%> 
  				</div>
  				<%
	  		}
	  		%>
	  		<% 
	  		if(textsplit.length > 1) {
	  			%>
	  			<div class="zf_tiezi_layer">
	  			<p>【<strong>转</strong>】 <%=textsplit[1]%></p>
	  			<%
	  			if(zf_imagesplit.length > 0) {
	  				%>
	  				<div class="image_group_layer">
	  				<%
	  				int zf_cnt = 0;
	  				for(int j=0; j<zf_imagesplit.length; j++) {
	  					if(zf_cnt % 3 == 0 && zf_cnt != 0) {
	  						%> <br> <%
	  					}
	  					zf_cnt++;
	  					%>
	  						<img src="<%=zf_imagesplit[j]%>" class="image_layer">
	  					<%
	  				}
	  				%> 
	  				</div>
	  				<%
	  			}%>
	  			</div>
	  			<%
	  		}
	  		 %>
	  		 
	  		 <div class="bs-docs-grid extra_info_layer">
	  			<div class="row show-grid">
	  			  <div class="col-md-3">
	  			  	<span>
	  			  		<em><span class="glyphicon glyphicon-star-empty"></span></em>
	  			  		<em>收藏&nbsp;<%=favorites[i]%></em>
	  			  	</span>
	  			  </div>
  				  <div class="col-md-3">
  				  	<span>
	  			  		<em><span class="glyphicon glyphicon-share"></span></em>
	  			  		<em>转发&nbsp;<%=forwardings[i]%></em>
	  			  	</span>
  				  </div>
				  <div class="col-md-3">
				  	<span>
	  			  		<em><span class="glyphicon glyphicon-comment"></span></em>
	  			  		<em>评论&nbsp;<%=comments[i]%></em>
	  			  	</span>
				  </div>
				  <div class="col-md-3">
				  	<span>
	  			  		<em><span class="glyphicon glyphicon-thumbs-up"></span></em>
	  			  		<em>点赞&nbsp;<%=thumbups[i]%></em>
	  			  	</span>
				  </div>
	  			</div>
	  		</div>
	  		<br>	
  		</div>
  		<%}; %>
  	<%}else{ %>
  		<p><tr><h3>抱歉，未搜索到相关结果</h3></tr></p>
  	<%}; %>
  </Table>
  </div>
  <div class="container" class="pagedivider">
  	<nav aria-label="Page navigation">
  		<ul class="pagination">
			<%if(currentPage>1){ %>
				<li>
				<a aria-label="Previous" href="ImageServer?query=<%=currentQuery%>&page=<%=currentPage-1%>&wantto=千度找贴">
					<span aria-hidden="true">&laquo;</span>
				</a>
				</li>
			<%}; %> 
			<%for (int i=Math.min(Math.max(1,currentPage-4), 15-9);i<currentPage;i++){%>
				<li>
				<a href="ImageServer?query=<%=currentQuery%>&page=<%=i%>&wantto=千度找贴">
					<%=i%>
				</a>
				</li>
			<%}; %>
			<li class="active">
		    	<span><%=currentPage%><span class="sr-only">(current)</span></span>
		    </li>
			<%for (int i=currentPage+1;i<=Math.min(Math.max(currentPage+5, 10), 15);i++){ %>
				<li>
				<a href="ImageServer?query=<%=currentQuery%>&page=<%=i%>&wantto=千度找贴">
					<%=i%>
				</a>
				</li>
			<%} if(currentPage < 15) {%>
			<li>
			<a aria-label="Next" href="ImageServer?query=<%=currentQuery%>&page=<%=currentPage+1%>&wantto=千度找贴">
				<span aria-hidden="true">&raquo;</span>
			</a>
			</li>
			<% }; %>
		</ul>
	</nav>
  </div>
</div>
<div id="Layer3" style="top: 839px; left: 27px;">
	
</div>
<div>
</div>
</body>
