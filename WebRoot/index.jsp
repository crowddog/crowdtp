<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<base href="<%=basePath%>">
		<link href="css/bootstrap.min.css" rel="stylesheet">
		<title>CrowdTP</title>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
		<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

		<script type="text/javascript">
	$('#myTab a').click(function(e) {
		e.preventDefault()
		$(this).tab('show')
	})
	function onclickAjax() {
		var progressDiv = document.getElementById("progress");
		progressDiv.style.display = "block";
		var requirement = document.getElementById("requirement");
		var start = document.getElementById("start");
		var scenenum = document.getElementById("scenenum");
		alert(requirement.value);
		var queryString = "requirement=" + requirement.value;
		queryString += "&start=" + start.value
		queryString += "&scenenum=" + scenenum.value;
		var url = "travelplanning";
		if (window.XMLHttpRequest) {
			req = new XMLHttpRequest();
		} else if (window.ActiveXObject) {
			req = new ActiveXObject("Microsoft.XMLHTTP");
		}
		req.open("POST", url, true);
		req.setRequestHeader("Content-Type",
				"application/x-www-form-urlencoded;");
		req.setRequestHeader("RequestType", "ajax");
		req.onreadystatechange = complete;
		req.send(queryString);
	}

	function complete() {
		if (req.readyState == 4) {
			if (req.status == 200) {
				alert("success");
				var result = req.responseText;
				var p = document.getElementById("planningresult");
				p.value = result;
				var progressDiv = document.getElementById("progress");
				progressDiv.style.display = "none";

			}
		}
	}
</script>
	</head>
	<body>
		<ul id="myTab" class="nav nav-tabs">
			<li class="active">
				<a href="#travelplanning" data-toggle="tab">旅游规划</a>
			</li>
			<li>
				<a href="#historicalplanning" data-toggle="tab">历史规划</a>
			</li>
		</ul>
		<div id="myTabContent" class="tab-content">
			<div class="tab-pane fade in active" id="travelplanning">
				<div style="width: 50%; margin: 0 auto; font-size:30px">
					旅游需求:
					<input type="text" name="requirement" id="requirement"
						style="width: 500px; margin: 10px 10px 10px 11px" />
					<br>
					起始景点:
					<input type="text" name="start" id="start"
						style="width: 500px; margin: 10px 10px 10px 11px" />
					<br>
					预期景点数:
					<input type="text" name="scenenum" id="scenenum"
						style="width: 500px; margin: 10px 10px 10px 0px" />
					<br>
					<input type="button" value="提交" style="width: 100px"
						onclick=
	onclickAjax();;
/>
					<br>
					<div style="width: 50%; margin: 0 auto; display: none"
						id="progress">
						<div class="progress">
							<div class="progress-bar progress-bar-striped active"
								role="progressbar" aria-valuenow="100" aria-valuemin="0"
								aria-valuemax="100" style="width: 100%; font-size: 20px">
								正在努力规划中，请耐心等候^0^
							</div>
						</div>
					</div>
					<div class="jumbotron">
						<h1>
							规划结果：
						</h1>
						<br>
						<br>
						<input type="text" value="" id="planningresult" size=50
							style="border: 0px;" disabled>
						<p>
							<a class="btn btn-primary btn-lg" href="#" role="button">Learn
								more</a>
						</p>
					</div>
				</div>
			</div>
			<div class="tab-pane fade" id="historicalplanning">
				<div style="width: 50%; margin: 0 auto">
					<div class="progress">
						<div class="progress-bar progress-bar-striped active"
							role="progressbar" aria-valuenow="100" aria-valuemin="0"
							aria-valuemax="100" style="width: 100%">
							正在努力规划中，请耐心等候^0^
						</div>
					</div>
				</div>
			</div>
			<div class="tab-pane fade" id="dropdown1">
				<p>
					Content of dropdown1
				</p>
			</div>
			<div class="tab-pane fade" id="dropdown2">
				<p>
					Content of dropdown2
				</p>
			</div>
		</div>






		<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
		<script src="http://cdn.bootcss.com/jquery/1.11.1/jquery.min.js"></script>
		<!-- Include all compiled plugins (below), or include individual files as needed -->
		<script src="js/bootstrap.min.js"></script>
	</body>
</html>
