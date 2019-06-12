<%@ page trimDirectiveWhitespaces="true" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib uri="/tags" prefix="date" %>
<jsp:useBean id="dateValue" class="java.util.Date"/>
<jsp:setProperty name="dateValue" property="time" value="${timestampValue}"/>
<jsp:useBean id="tu" class="com.lerx.sys.util.TimeUtil" scope="page" ></jsp:useBean>
<jsp:useBean id="hq" class="com.lerx.sys.util.HttpUtil" scope="page" ></jsp:useBean>
<jsp:useBean id="albumGenre" class="com.lerx.entities.AlbumGenre" scope="request" ></jsp:useBean>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
<title>增加专辑类型</title>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/scripts/js/jquery/1.9.1/jquery.min.js"></script>
<link rel="stylesheet" href="${pageContext.request.contextPath}/scripts/js/layui/v2/css/layui.css?t=${tu.getCurrTime()}" media="all">
<script src="${pageContext.request.contextPath}/scripts/js/layui/v2/layui.js"></script>
	
<script src="${pageContext.request.contextPath}/scripts/js/miniColors/2.1/jquery.minicolors.js"></script>

<link rel="stylesheet" href="${pageContext.request.contextPath}/scripts/js/miniColors/2.1/jquery.minicolors.css">
<style type="text/css">


.layui-form-label {width: 100px;}
.layui-form-block,.layui-input-block{
	margin-left:130px;margin-right:5px;
}


.toppos {
	margin-top: 20px;
}

.addbtn {
	margin-top: 40px;
	margin-left: 300px;
}

.tips {
	margin-left: 20px;
}
.img-input{
	display:inline;
	width:15%;
}
</style>



</head>
<body>

<p style="text-align: right;"></p>
<div class="runtest">
	<form:form commandName="albumGenre" id="newalbumGenre" 
		action="${pageContext.request.contextPath }/action_albgenre/add"
		method="post" class="layui-form"  accept-charset="UTF-8">
		
		<div class="toppos"></div>

		
			<div class="layui-form-item">
				<label class="layui-form-label">名称：</label>
				<div class="layui-input-block">
					<form:input path="name" required="true"  lay-verify="required" placeholder="" autocomplete="off" class="layui-input" /> <form:errors path="name"></form:errors>

				</div>
			</div>
			
					
			
			<div class="layui-form-item">
				<label class="layui-form-label">描述：</label>
				<div class="layui-input-block">
				<form:textarea path="description" class="layui-textarea" />
				</div>
			</div>
			
			<div class="layui-form-item">
				<label class="layui-form-label">协议：</label>
				<div class="layui-input-block">
				<form:textarea path="agreement" class="layui-textarea" />
				</div>
			</div>
			
			
			<c:if test="${albumGenre.id >0 }">
			<form:hidden path="id" />
			
			
			
			
			<div class="layui-form-item">
				<label class="layui-form-label">参数：</label>
				<div class="layui-input-block">
					<form:checkbox path="status" value="true" lay-skin="primary" title="可用" />
					<form:checkbox path="open" value="true" lay-skin="primary" title="注册" />
					<form:checkbox path="free" value="true" lay-skin="primary" title="自由" />
					<form:checkbox path="poll" value="true" lay-skin="primary" title="调查" />
					<form:checkbox path="comm" value="true" lay-skin="primary" title="评论" />	
				</div>
			</div>
			</c:if>
			
			<div class="layui-form-item">
				<label class="layui-form-label">限额：</label>
				<div class="layui-input-block">
					<form:input path="quota" required="true"  lay-verify="required" placeholder="值为0时不限" autocomplete="off" class="layui-input" /> <form:errors path="quota"></form:errors>

				</div>
			</div>
			
			
			
			
			
			<div class="layui-form-item">
				<label class="layui-form-label">模板：</label>
				<div class="layui-input-block">
				
				<form:select path="templet.id" id="templet" lay-filter="templet">
				
				<c:forEach items="${templets }" var="templet" varStatus="st">
				<form:option label="${templet.name }" value="${templet.id }"   >
				
				</form:option>
				</c:forEach>  
					
              		 
           		</form:select> 
           

				</div>
			</div>
			
			<div class="layui-form-item">
				<div class="layui-input-block addbtn">
				<button class="layui-btn" onclick="commit();" lay-submit lay-filter="formDemo">提交</button>
				</div>
			</div>


		
	</form:form>
	</div>
<script type="text/javascript">

function convertDateToUnix(str){
	
	str = str.replace(/-/g,"/");
	var date = new Date(str);
	var unixDate = new Date(Date.UTC(date.getFullYear(),date.getMonth(),date.getDate(),date.getHours(),date.getMinutes(), date.getSeconds()));   
	return unixDate.getTime();
}

function commit(){
	
	/* $("#startDate").val(convertDateToUnix($("#times").val()));
	$("#expiryDate").val(convertDateToUnix($("#timee").val())); */
	
	$("#newalbumGenre").submit();
	
}


</script>
<script type="text/javascript">


layui.use('form', function(){
	  var form = layui.form; //只有执行了这一步，部分表单元素才会自动修饰成功
	  
	  //……
	  
	  //但是，如果你的HTML是动态生成的，自动渲染就会失效
	  //因此你需要在相应的地方，执行下述方法来手动渲染，跟这类似的还有 element.init();
	  form.render();
	}); 
	
</script>

<script>
layui.use('laydate', function(){
  var laydate = layui.laydate;
  
  //执行一个laydate实例
  laydate.render({
    elem: '#times' //指定元素
  });
  
  laydate.render({
	    elem: '#timee' //指定元素
	  });
});

	
</script>

</body>
</html>