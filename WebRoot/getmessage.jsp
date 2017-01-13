<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>选择你的需求</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

  </head>
  
  <body>
  	<h1 align = "center"> 请选择你的需求</h1><br>
    <form action = "/test/webget">
    地点 : <input type = "text" name = "endpoint">
    类型 : <select name = "scenictype">
    	<option>自然风景</option>
    	<option>娱乐</option>
    	<option>文化</option>
    	<option>都市</option>
    	<option>休闲</option>
    	</select>
    <br>
    出发点 : <input type = "text" name = "startpoint">
    <br>
    
    <input type = "submit" value = "提交">&nbsp;&nbsp;&nbsp;
    <input type = "reset" value = "重置">
    <br>
    
    </form>
  </body>
</html>
