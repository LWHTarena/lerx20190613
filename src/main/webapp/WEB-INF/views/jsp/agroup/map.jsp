<%@ page trimDirectiveWhitespaces="true"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<jsp:useBean id="sutil" class="com.lerx.sys.util.StringUtil"
	scope="page"></jsp:useBean>
<jsp:useBean id="hq" class="com.lerx.sys.util.HttpUtil" scope="page"></jsp:useBean>
<jsp:useBean id="tu" class="com.lerx.sys.util.TimeUtil" scope="page"></jsp:useBean>
<!doctype html>
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html;charset=utf-8">
<title>网站地图 - ${requestScope.portal.name }</title>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/scripts/js/jquery/1.9.1/jquery.min.js"></script>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/scripts/js/layui/v2/css/layui.css?t=${tu.getCurrTime()}"
	media="all">
<script
	src="${pageContext.request.contextPath}/scripts/js/layui/v2/layui.js"></script>
</head>
<style>
.plant_mian{width: 800px;margin:0 auto;}

a:visited {
	text-decoration: none;
	color: #2c374b;
}

a:hover {
	text-decoration: none;
	color: #c20;
}

a:active {
	text-decoration: none;
	color: #2c374b;
}



.col_center {
	text-align: center;
}

.map-table {
	width: 800px;
	padding: 15px;
	
}

.layui-table th {
	text-align: center;
}


/* table {  
		table-layout: fixed;  word-break:break-all;  
		word-wrap:break-word;  
	} */
</style>
<body>
	<div class="plant_mian" style="padding: 15px;">

		<!-- 内容主体区域 -->
			
			<table class="layui-table map-table" align="center">
				<colgroup>
					<col width="150">
					<col>
				</colgroup>
				<thead>
					<tr>
						<th colspan="2">${portal.fullName } 网站地图<c:if test="${empty requestScope.preListAll }"> (没有任何栏目)</c:if></th>

					</tr>
				</thead>
				<tbody>
					<c:forEach items="${requestScope.preListAll }" var="g"
						varStatus="st">
						<c:if test="${g.id==0 }">
						<tr>
								<td class="col_center_80px"></td>
								<td><a href="${pageContext.request.contextPath}/">首页</a></td>

							</tr>
						</c:if>
						<c:if test="${g.id >0 }">
							<tr>
								<td class="col_center_80px">${st.index}</td>
								<td><a href="${g.url}">${g.title}</a></td>

							</tr>
						</c:if>
					</c:forEach>
				</tbody>
			</table>
	</div>

</body>
</html>