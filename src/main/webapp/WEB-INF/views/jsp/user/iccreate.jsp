<%@ page trimDirectiveWhitespaces="true"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<jsp:useBean id="sutil" class="com.lerx.sys.util.StringUtil"
	scope="page"></jsp:useBean>
<%@ taglib uri="/tags" prefix="date"%>
<jsp:useBean id="dateValue" class="java.util.Date" />
<jsp:setProperty name="dateValue" property="time"
	value="${timestampValue}" />
<jsp:useBean id="hq" class="com.lerx.sys.util.HttpUtil" scope="page"></jsp:useBean>
<jsp:useBean id="tu" class="com.lerx.sys.util.TimeUtil" scope="page"></jsp:useBean>
<jsp:useBean id="su" class="com.lerx.sys.util.StringUtil" scope="page"></jsp:useBean>
<jsp:useBean id="user" class="com.lerx.entities.User" scope="request" ></jsp:useBean>
<!doctype html>
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html;charset=utf-8">
<script type="text/javascript"
	src="${pageContext.request.contextPath}/scripts/js/jquery/1.11.3/jquery-1.11.3.min.js"></script>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/scripts/js/layui/v2/css/layui.css?t=${tu.getCurrTime()}"
	media="all">
<%-- <link href="${pageContext.request.contextPath}/css/connect/${otype}.css" rel="stylesheet"> --%>
<script
	src="${pageContext.request.contextPath}/scripts/js/layui/v2/layui.js"></script>
<script
	src="${pageContext.request.contextPath}/scripts/js/validate/drag.js"
	type="text/javascript" charset="utf-8"></script>
<title>${connectTitle}互联登录设置</title>
<style type="text/css">
body{
background: url("/scripts/css/connect/${otype}.jpg") no-repeat fixed;
height:100%;
width:100%;
overflow: hidden;
background-size:100%;
}

.layui-main {
	margin-top: 100px;
	width: 400px;
	height: 200px;
}

.layui-form-label {
	width: 60px;
}

.sendv {
	width: 100px;
}

.layui-input-block {
	margin-left: 90px;
}

#drag {
	position: relative;
	background-color: #e8e8e8;
	height: 34px;
	line-height: 34px;
	text-align: center;
}

#drag .handler {
	position: absolute;
	top: 0px;
	left: 0px;
	width: 40px;
	height: 32px;
	border: 1px solid #ccc;
	cursor: move;
}

.handler_bg {
	background: #fff
		url("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAA3hpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNS1jMDIxIDc5LjE1NTc3MiwgMjAxNC8wMS8xMy0xOTo0NDowMCAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wTU09Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9tbS8iIHhtbG5zOnN0UmVmPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvc1R5cGUvUmVzb3VyY2VSZWYjIiB4bWxuczp4bXA9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC8iIHhtcE1NOk9yaWdpbmFsRG9jdW1lbnRJRD0ieG1wLmRpZDo0ZDhlNWY5My05NmI0LTRlNWQtOGFjYi03ZTY4OGYyMTU2ZTYiIHhtcE1NOkRvY3VtZW50SUQ9InhtcC5kaWQ6NTEyNTVEMURGMkVFMTFFNEI5NDBCMjQ2M0ExMDQ1OUYiIHhtcE1NOkluc3RhbmNlSUQ9InhtcC5paWQ6NTEyNTVEMUNGMkVFMTFFNEI5NDBCMjQ2M0ExMDQ1OUYiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIDIwMTQgKE1hY2ludG9zaCkiPiA8eG1wTU06RGVyaXZlZEZyb20gc3RSZWY6aW5zdGFuY2VJRD0ieG1wLmlpZDo2MTc5NzNmZS02OTQxLTQyOTYtYTIwNi02NDI2YTNkOWU5YmUiIHN0UmVmOmRvY3VtZW50SUQ9InhtcC5kaWQ6NGQ4ZTVmOTMtOTZiNC00ZTVkLThhY2ItN2U2ODhmMjE1NmU2Ii8+IDwvcmRmOkRlc2NyaXB0aW9uPiA8L3JkZjpSREY+IDwveDp4bXBtZXRhPiA8P3hwYWNrZXQgZW5kPSJyIj8+YiRG4AAAALFJREFUeNpi/P//PwMlgImBQkA9A+bOnfsIiBOxKcInh+yCaCDuByoswaIOpxwjciACFegBqZ1AvBSIS5OTk/8TkmNEjwWgQiUgtQuIjwAxUF3yX3xyGIEIFLwHpKyAWB+I1xGSwxULIGf9A7mQkBwTlhBXAFLHgPgqEAcTkmNCU6AL9d8WII4HOvk3ITkWJAXWUMlOoGQHmsE45ViQ2KuBuASoYC4Wf+OUYxz6mQkgwAAN9mIrUReCXgAAAABJRU5ErkJggg==")
		no-repeat center;
}

.handler_ok_bg {
	background: #fff
		url("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAA3hpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNS1jMDIxIDc5LjE1NTc3MiwgMjAxNC8wMS8xMy0xOTo0NDowMCAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wTU09Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9tbS8iIHhtbG5zOnN0UmVmPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvc1R5cGUvUmVzb3VyY2VSZWYjIiB4bWxuczp4bXA9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC8iIHhtcE1NOk9yaWdpbmFsRG9jdW1lbnRJRD0ieG1wLmRpZDo0ZDhlNWY5My05NmI0LTRlNWQtOGFjYi03ZTY4OGYyMTU2ZTYiIHhtcE1NOkRvY3VtZW50SUQ9InhtcC5kaWQ6NDlBRDI3NjVGMkQ2MTFFNEI5NDBCMjQ2M0ExMDQ1OUYiIHhtcE1NOkluc3RhbmNlSUQ9InhtcC5paWQ6NDlBRDI3NjRGMkQ2MTFFNEI5NDBCMjQ2M0ExMDQ1OUYiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIDIwMTQgKE1hY2ludG9zaCkiPiA8eG1wTU06RGVyaXZlZEZyb20gc3RSZWY6aW5zdGFuY2VJRD0ieG1wLmlpZDphNWEzMWNhMC1hYmViLTQxNWEtYTEwZS04Y2U5NzRlN2Q4YTEiIHN0UmVmOmRvY3VtZW50SUQ9InhtcC5kaWQ6NGQ4ZTVmOTMtOTZiNC00ZTVkLThhY2ItN2U2ODhmMjE1NmU2Ii8+IDwvcmRmOkRlc2NyaXB0aW9uPiA8L3JkZjpSREY+IDwveDp4bXBtZXRhPiA8P3hwYWNrZXQgZW5kPSJyIj8+k+sHwwAAASZJREFUeNpi/P//PwMyKD8uZw+kUoDYEYgloMIvgHg/EM/ptHx0EFk9I8wAoEZ+IDUPiIMY8IN1QJwENOgj3ACo5gNAbMBAHLgAxA4gQ5igAnNJ0MwAVTsX7IKyY7L2UNuJAf+AmAmJ78AEDTBiwGYg5gbifCSxFCZoaBMCy4A4GOjnH0D6DpK4IxNSVIHAfSDOAeLraJrjgJp/AwPbHMhejiQnwYRmUzNQ4VQgDQqXK0ia/0I17wJiPmQNTNBEAgMlQIWiQA2vgWw7QppBekGxsAjIiEUSBNnsBDWEAY9mEFgMMgBk00E0iZtA7AHEctDQ58MRuA6wlLgGFMoMpIG1QFeGwAIxGZo8GUhIysmwQGSAZgwHaEZhICIzOaBkJkqyM0CAAQDGx279Jf50AAAAAABJRU5ErkJggg==")
		no-repeat center;
}

#drag .drag_bg {
	background-color: #7ac23c;
	height: 34px;
	width: 0px;
}

#drag .drag_text {
	position: absolute;
	top: 0px;
	width: 400px;
	-moz-user-select: none;
	-webkit-user-select: none;
	user-select: none;
	-o-user-select: none;
	-ms-user-select: none;
}

.area-return{
margin-top: 240px;
text-align: center;
}
</style>
</head>
<body>
	<div class="layui-main" id="connecter">
		<div class="layui-clear"></div>
		<fieldset class="layui-elem-field layui-field-title"
			style="margin-top: 20px;">
			<legend>
			<c:if test="${otype==0 }">
				<i class="layui-icon layui-icon-login-qq"
					style="font-size: 30px; color: #1E9FFF;"></i>
			</c:if>
			<c:if test="${otype==1 }">
				<i class="layui-icon layui-icon-login-wechat"
					style="font-size: 30px; color: #44b549;"></i>
			</c:if>
			<c:if test="${otype==2 }">
				<i class="layui-icon layui-icon-login-weibo"
					style="font-size: 30px; color: #E6162D;"></i>
			</c:if>
			 ${connectTitle}互联登录设置
			</legend>
		</fieldset>

		<div style="padding: 20px; background-color: #F2F2F2;">
			<div class="layui-row layui-col-space15">

				<div class="layui-col-md12">

					<div class="layui-card">
						<div class="layui-card-header">帐号验证</div>
						<div class="layui-card-body">


							<div class="layui-form-item">
								<label class="layui-form-label" id="labeld">手机号：</label>
								<div class="layui-input-block">
									<input type="text" id="inputv" name="inputv" required
										lay-verify="required|phone|number" placeholder="请输入手机号"
										autocomplete="off" class="layui-input">
								</div>
							</div>

							<div class="layui-form-item">
								<div class="layui-inline">
									<label class="layui-form-label">验证码：</label>
									<div class="layui-input-inline" style="width: 130px;">
										<input class="layui-input"
											maxlength='<spring:message code="vcode.sendchar.length"></spring:message>'
											type="text" id="vcode" name="vcode" required
											lay-verify="required" placeholder="请输入验证码" autocomplete="off">
									</div>
									<div class="layui-input-inline" style="width: 50px;">
										<input type="button"
											class="layui-btn sendv layui-btn-disabled" id="btn"
											value="获 取">
									</div>
								</div>

							</div>

							<div class="layui-form-item">
								<label class="layui-form-label"></label> <input type="button"
									id="next" class="layui-btn layui-btn-disabled" id="btn"
									value="下一步">
							</div>

						</div>
						<div class="layui-card-body">提醒：会覆盖帐号下绑定的其它${connectTitle}互联帐号！</div>
						<div class="layui-card-body">
							<div id="drag" class="demo2"></div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<script>
		var openID = "${qui.openID}";
		
		var inputlabel = "";
		var inputtype = 0;
		$(function() {
			var uri = '${pageContext.request.contextPath}/action_portal/regtest';
			$.ajax({
				url : uri,
				type : 'post',
				success : function(data) {
					var smod = data;

					if (smod == 0) {
						inputlabel = "邮箱";
						$("#labeld").html("邮　箱：");
						$('#inputv').attr('placeholder', "请输入邮箱地址");

					} else {
						inputlabel = "手机号";
						inputtype = 1;
						$("#labeld").html("手机号：");
						$('#inputv').attr('placeholder', "请输入手机号码");
					}
				}
			});
		});

		/* layui.use([ 'layer', 'element' ], function() {
			var layer = layui.layer //弹层
			, element = layui.element; //元素操作

			//监听Tab切换
			element.on('tab(demo)', function(data) {
				layer.msg('切换了：' + this.innerHTML);
				console.log(data);
			});
			
			if (openID == "") {
				$('#drag').hide();
				layer.alert("非法请求！" ,function(
						index) {
					
					//window.location.href = document.referrer;
					layer.close(index);
				});
			}

		}); */

		
		
		function isPhoneNo(value) {
			var pattern = /^1[34578]\d{9}$/;
			return pattern.test(value);
		}

		function isEmailNo(value) {
			var pattern = /^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/;
			return pattern.test(value);
		}

		$("#btn").attr("disabled", true);
		$('#drag').drag();

		$(function() {
			$('#btn')
					.click(
							function() {
								$("#btn").val("正在发送…");
								$("#btn").attr(
										"disabled",
										true);
								$("#btn")
										.addClass(
												"layui-btn-disabled");
								var passv;
								if (inputtype == 0) {
									passv = isEmailNo($("#inputv").val());
								} else {
									passv = isPhoneNo($("#inputv").val());
								}
								if (passv) {

									var uri = '${pageContext.request.contextPath}/action_captcha/send?lookup=0&target='
											+ $("#inputv").val();
									$
											.ajax({
												url : uri,
												type : 'post',
												success : function(data) {
													switch (data) {
													case 0:
														layer.msg("验证码已成功发送！");

														$("#vcode").val('');
														$("#next")
																.removeClass(
																		"layui-btn-disabled");

														var count = 60;
														var countdown = setInterval(
																CountDown, 500);
														function CountDown() {
															$("#btn").attr(
																	"disabled",
																	true);
															$("#btn")
																	.addClass(
																			"layui-btn-disabled");
															if (count > 56) {
																$("#btn")
																		.val(
																				"正在发送…");
															} else {
																$("#btn")
																		.val(
																				count
																						+ "秒后重新发送 ");
															}

															if (count == 0) {
																$("#btn")
																		.val(
																				"获 取")
																		.removeAttr(
																				"disabled");
																$("#btn")
																		.removeClass(
																				"layui-btn-disabled");
																clearInterval(countdown);
															}
															count--;
														}

														break;
													case -1:
														layer
																.msg(
																		"服务器小弟说他大哥不理他的请求，您能联系一下管理员看看是不是配置错误或者欠费啦？",
																		{
																			time : 5000
																		});
														break;
													case -5:
														layer
																.msg(
																		"服务器小弟说他只能向他熟悉的目标发送，您的请求他无法完成啊！",
																		{
																			time : 5000
																		});
														break;
													case -8:
														layer
																.msg(
																		"验证码已经发送过了，请让服务器小弟歇会儿喝口浓茶，好吗？",
																		{
																			time : 5000
																		});
														break;
													case -11:
														layer
																.msg(
																		"服务器小弟说您输入错误，您信吗？",
																		{
																			time : 3000
																		});
														break;
													default:
														layer.msg(
																"服务器小弟处理不了您这复杂的请求啦！错误号："
																		+ data,
																{
																	time : 3000
																});

													}
												}

											});

								} else {

									layer.msg("请输入正确的" + inputlabel + "！");
								}

								//$("#btn").val("获取");

							});
			$('#next')
					.click(
							function() {

								var uri = '${pageContext.request.contextPath}/action_user/bindConnecter?vcode='
										+ $("#vcode").val()
										+ "&otype="+${otype}+"&target="
										+ $("#inputv").val()
										+ "&openID="
										+ openID + "&channel="+inputtype;
								$
										.ajax({
											url : uri,
											type : 'post',
											success : function(data) {
												if (data.status < 0) {
													layer
															.msg(
																	"验证失败了！请检查您输入的验证码是否正确定！",
																	{
																		time : 3000
																	});
												} else {
													if (data.uid > 0) {
														layer
																.alert(
																		"验证成功！已绑定用户："
																				+ data.username+"<br />即将返回："+data.referer,
																		function(
																				index) {
																			//do something
																			window.location.href = data.referer;
																			layer
																					.close(index);
																		});
													} else {
														$("#referer").val(data.referer);
														layer
																.alert("验证成功！需新注册用户");
														$("#connecter").hide();
														$("#userreg").show();
														
													}

												}
											}
										});

							});

		});
	</script>
	
	
	
	
	
	<form:form commandName="user" id="userreg" class="layui-form layui-hide"
		action="${pageContext.request.contextPath }/action_user/regByOpenID"
		method="post" accept-charset="UTF-8">
		
		<div class="toppos"></div>

		<div class="runtest">
		 
		 <input type="hidden" name="sendTarget" value="${sessionScope.sendTarget}" />
		 <input type="hidden" name="targetMode" value="${sessionScope.targetMode}" />
		 <input type="hidden" name="openID" value="${qui.openID}" />
		 <input type="hidden" name="otype" value="${otype}" />
		 <input type="hidden" id="referer" name="referer" value="" />
		 
			
			<div class="layui-form-item">
				<label class="layui-form-label">请输入用户名</label>
				<div class="layui-input-block">
					<form:input path="username"  id="username" lay-verify="username" 
						placeholder="登录名(会员号)" autocomplete="off" class="layui-input" /> <form:errors path="username"></form:errors>

				</div>
			</div>
			
			<div class="layui-form-item">
				<label class="layui-form-label">请输入密码</label>
				<div class="layui-input-block">
					<form:input path="password"  required="true" lay-verify="required"
						placeholder="密码" autocomplete="off" class="layui-input" /> <form:errors path="password"></form:errors>

				</div>
			</div>	
			<c:if test="${sessionScope.targetMode==1}">
			<div class="layui-form-item">
				<label class="layui-form-label">请输入您的邮箱</label>
				<div class="layui-input-block">
					<form:input path="email"  required="true" lay-verify="required|email|lemail"
						placeholder="邮箱" autocomplete="off" class="layui-input" /> <form:errors path="email"></form:errors>

				</div>
			</div>
			</c:if>
			<c:if test="${sessionScope.targetMode==0}">
			<div class="layui-form-item">
				<label class="layui-form-label">请输入手机号码</label>
				<div class="layui-input-block">
					<form:input path="mobile"  required="true" lay-verify="required|phone|lmobile"
						placeholder="邮箱" autocomplete="off" class="layui-input" /> <form:errors path="mobile"></form:errors>

				</div>
			</div>
			</c:if>
			
			<div class="layui-form-item">
				<div class="layui-input-block addbtn">
				<button class="layui-btn" lay-submit="" lay-filter="formDemo">立即提交</button>
				</div>
			</div>


		</div>
	</form:form>
	<div class="layui-main area-return">
	<a href="${pageContext.request.contextPath}/" class="layui-btn">返回首页</a>
	</div>
	
	
<script type="text/javascript">

layui.use(['form', 'layer',  'element'], function(){
	  var form = layui.form 
	  ,layer = layui.layer 
	  ,element = layui.element;
	  
	  
	  if (openID == "") {
			$('#drag').hide();
			layer.alert("非法请求！" ,function(
					index) {
				
				//window.location.href = document.referrer;
				layer.close(index);
			});
		}
	  
	//自定义验证规则  
	  form.verify({  
		  
		
		  username: function(value){ 
			  
	          if(value.length < 3){  
	            return '用户名至少得3个字符啊';  
	          }else{
	        	  var uri="${pageContext.request.contextPath}/action_user/findname?name="+value;
	        	  var r;
		          $.ajax({
						url: uri,
						type: 'post',
						async: false,
						success: function(data) {
							
							if (data>0){
								r= '该用户名已被占用！请重新输入！';
							}
						}
					});
		          return r;
	          }
	          
	          
		  }
	  
	  	,lemail:function(value){ 
			  
	  		var uri="${pageContext.request.contextPath}/action_user/findemail?email="+value;
      	  	var r;
	          $.ajax({
					url: uri,
					type: 'post',
					async: false,
					success: function(data) {
						
						if (data>0){
							r= '该邮箱已被占用！请重新输入！';
						}
					}
				});
	          return r;
	          
	          
		  }
	  	,lmobile:function(value){ 
			  
	  		var uri="${pageContext.request.contextPath}/action_user/findmobile?mobile="+value;
      	  	var r;
	          $.ajax({
					url: uri,
					type: 'post',
					async: false,
					success: function(data) {
						
						if (data>0){
							r= '该手机号已被占用！请重新输入！';
						}
					}
				});
	          return r;
	          
	          
		 }
	  
	  });
	  
	  
	//监听提交  
	  form.on('submit(formDemo)', function(data){
				  
	    /* layer.alert(JSON.stringify(data.field), {  
	      title: '最终的提交信息'  
	    })  
	    return false;   */
	  }); 
	
	  form.render(); // 更新全部
	
});

</script>	
</body>
</html>