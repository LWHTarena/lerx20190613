<%@ page trimDirectiveWhitespaces="true" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE HTML>
<html >
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>增加成功</title>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/scripts/js/jquery/1.9.1/jquery.min.js"></script>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/scripts/js/layui/v2/css/layui.css?t=${tu.getCurrTime()}" media="all">
  <script src="${pageContext.request.contextPath}/scripts/js/layui/v2/layui.js"></script>
</head>
<script type="text/javascript">

layui.use(['form', 'layer', 'table',  'element'], function(){
	  var $ = layui.jquery  
	  ,form = layui.form
	  ,layer = layui.layer //弹层
	  ,table = layui.table //表格
	  ,element = layui.element; //元素操作
	  
	//一些事件监听
		element.on('tab(demo)', function(data) {
			console.log(data);
		});
	  
});
	
</script>
<body>
<div class="layui-form">
	<div class="layui-form-item">
		<label class="layui-form-label">模块：</label>
		<div class="layui-input-block" style="padding-top:8px">${requestScope.areaTitle}</div>
	</div>
	<div class="layui-form-item">
		<label class="layui-form-label">标签名：</label>
		<div class="layui-input-block" style="padding-top:8px">${requestScope.var}</div>
	</div>
</div>
<form class="layui-form" method="post" action="${pageContext.request.contextPath}/action_album_templet/modifyCode/${requestScope.templetID }/${requestScope.area }/${requestScope.var}">
<div class="layui-form-item">
    <label class="layui-form-label">标签代码：</label>
    <div class="layui-input-block">
    
      <textarea rows="10" cols="40" class="layui-textarea" name="code"  ><c:out value="${requestScope.code }" escapeXml="true"></c:out></textarea>
    </div>
  </div>


<div class="layui-form-item">
    <div class="layui-input-block">
      <input class="layui-btn" name="submit" title="提交" type="submit">
<input class="layui-btn" name="undo" value="取消" onclick="javascript:window.history.back();" type="button">
    </div>
  </div>


</form>

	

</body>
</html>