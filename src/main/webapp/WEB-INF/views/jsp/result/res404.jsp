<%@ page trimDirectiveWhitespaces="true" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>未找到资源</title>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/scripts/js/jquery/1.9.1/jquery.min.js"></script>
<link rel="stylesheet" href="${pageContext.request.contextPath}/scripts/js/layui/v2/css/layui.css?t=${tu.getCurrTime()}" media="all">
<script src="${pageContext.request.contextPath}/scripts/js/layui/v2/layui.js"></script>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/scripts/js/layui/css/global.css">
</head>
<style>

.site-title {text-align:center;font-size: 20px;font-weight: bold;margin-top: 40px;color: red; }
.layui-btn {margin-top: 30px;}
</style>

<script type="text/javascript">
	layui.use('element', function() {
		var element = layui.element();

		//一些事件监听
		element.on('tab(demo)', function(data) {
			console.log(data);
		});
	});
	
</script>
<body>
<div class="container">
	<p>The requested resource was not found!</p>
	<p>未找到请求的资源</p>
</div>
</body>
</html>