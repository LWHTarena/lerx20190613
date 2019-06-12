<%@ page trimDirectiveWhitespaces="true"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<%@ taglib uri="/tags" prefix="date"%>
<jsp:useBean id="tu" class="com.lerx.sys.util.TimeUtil" scope="page"></jsp:useBean>
<!doctype html>
<html>
<head>
<meta charset="utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport" content="width=device-width, initial-scale=1">
<script type="text/javascript"
	src="${pageContext.request.contextPath}/scripts/js/jquery/1.11.3/jquery-1.11.3.min.js"></script>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/scripts/bootstrap-3.3.7-dist/css/bootstrap.min.css" />
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/scripts/js/layui/v2/css/layui.css?t=${tu.getCurrTime()}"
	media="all">
<!--[if lt IE 9]><script src="${pageContext.request.contextPath}/scripts/js/ie8-responsive-file-warning.js"></script><![endif]-->
<%-- <link href="${pageContext.request.contextPath}/scripts/css/connect/${otype}.css" rel="stylesheet"> --%>
<script src="${pageContext.request.contextPath}/scripts/js/ie-emulation-modes-warning.js"></script>

<!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
<!--[if lt IE 9]>
		<script src="${pageContext.request.contextPath}/scripts/js/html5shiv.min.js"></script>
		<script src="${pageContext.request.contextPath}/scripts/js/respond.min.js"></script>
		<![endif]-->

<script
	src="${pageContext.request.contextPath}/scripts/js/layui/v2/layui.js"></script>
<script type="text/javascript"
	src='${pageContext.request.contextPath}/scripts/js/jquery/jquery.qrcode.min.js'></script>
<script
	src="${pageContext.request.contextPath}/scripts/js/lerxui/gen.js"></script>
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/scripts/css/iconfont/iconfont.css">
<style>
.user-center{padding:20px 0;}
			.user-center .user-image{ text-align: center; margin-top: 10px; }
			.user-center .user-image img{ width:120px; height: 120px; border-radius: 50%; background:#fff; padding:2px; border:1px solid #ccc;}
			.user-center .user-name{text-align: center; margin-top:10px; font-size: 18px;}
			.edit-btn{margin-top: 20px; text-align: center;}
			.user-center .user-2dcode{ text-align: center;  }
			.user-center .user-2dcode canvas{margin-top: 20px;}
			.user-center .btn{ width: 180px; text-align: left;}
			.user-center .col-sm-5 .row{ margin: 5px 0;}
			.user-info-hl{ background: #ddd; padding: 8px;}
			.user-info .row{ height: 30px; border-bottom: 1px #ccc dotted; margin:5px 15px;}
			.user-info .col-xs-4{ font-weight: bold;}
			.user-info .col-xs-5{ font-size: 12px;}
			
/* .user-card .logo {
	text-align: center;
	margin-top: 20px;
}

.card-header {
	background: url(../images/002.jpg) center;
	height: 200px
}

.logo img {
	width: 100px;
	height: 100px;
	border-radius: 50%;
	border: 2px #fff solid;
	margin-top: 30px;
}

.user-card .user-image {
	text-align: center;
	margin-top: 10px;
}

.user-card .user-name {
	margin-top: 5px;
	font-size: 18px;
	color: #333;
	margin: 10px 0 15px 0;
}

.user-info-hl {
	background: #ddd;
	padding: 8px;
	margin: 10px 0;
}

.user-card .row {
	height: 30px;
	border-bottom: 1px #ccc dotted;
	padding: 5px 15px;
}

.user-card .col-xs-4 {
	font-weight: bold;
}

.user-card .col-xs-5 {
	font-size: 12px;
}

.change-user-info {
	margin: 10px 0;
}

.iconfont {
	font-size: 14px;
}

.icon {
	width: 1em;
	height: 1em;
	vertical-align: -0.15em;
	fill: currentColor;
	overflow: hidden;
} */
</style>
<title>${user.username }的网络名片</title>
</head>
<body>
<c:if test="${own}">
<c:set var="tmpSuxBind" value="，点此绑定" />
<c:set var="tmpSuxFree" value="，点此解除" />
<c:set var="tmpPws" value="修改密码" />
</c:if>
<c:if test="${own==false}">
<c:set var="tmpSuxBind" value="" />
<c:set var="tmpSuxFree" value="" />
<c:set var="tmpPws" value="密码保护中" />
</c:if>
<div class="user-card">
			<div class="container user-center">
				<div class="row">
					<div class="col-sm-3">
						<div class="user-image"><img id="avatar" alt="" /></div>
						<div class="user-name">${user.username }</div>
					</div>
					<div class="col-sm-3 edit-btn">
							<div class="col-xs-12"><a class="btn btn-default" href="javascript:chgmobile('${pageContext.request.contextPath}',${user.id });" role="button"><span class="glyphicon glyphicon-phone" <c:if test='${not empty user.mobile}'> style="color: #79c8e2;"  </c:if>  ></span>　<span id="user-mobile"></span></a></div>
							<div class="col-xs-12"><a class="btn btn-default" href="javascript:chgemail('${pageContext.request.contextPath}',${user.id });" role="button"><span class="glyphicon glyphicon-envelope" <c:if test='${not empty user.email}'> style="color: #584fb0;" </c:if>></span>　${user.email }<c:if test='${empty user.email}'>未绑定${tmpSuxBind}</c:if></a></div>
							<div class="col-xs-12"><a class="btn btn-default" href="javascript:pws('${pageContext.request.contextPath}',${user.id });" role="button"><span class="glyphicon glyphicon-lock" style="color: #a62341;"></span>　${tmpPws}</a></div>
					</div>
					<div class="col-sm-3 edit-btn">
							<div class="col-xs-12"><a class="btn btn-default" href="javascript:bind(1,${!bindWeChat});" role="button"><img src="${pageContext.request.contextPath}/images/wechat-${unBindWeChat}login.png"/>　<c:if test="${bindWeChat}">已绑定${tmpSuxFree}</c:if><c:if test="${!bindWeChat}">未绑定${tmpSuxBind}</c:if></a></div>
							<div class="col-xs-12"><a class="btn btn-default" href="javascript:bind(2,${!bindWeibo});" role="button"><img src="${pageContext.request.contextPath}/images/webo-${unBindWeibo}login.png"/>　<c:if test="${bindWeibo}">已绑定${tmpSuxFree}</c:if><c:if test="${!bindWeibo}">未绑定${tmpSuxBind}</c:if></a></div>
							<div class="col-xs-12"><a class="btn btn-default" href="javascript:bind(0,${!bindQQ});" role="button"><img src="${pageContext.request.contextPath}/images/qq-${unBindQQ}login.png"/>　<c:if test="${bindQQ}">已绑定${tmpSuxFree}</c:if><c:if test="${!bindQQ}">未绑定${tmpSuxBind}</c:if></a></div>
					</div>
					<div class="col-sm-3 user-2dcode" id="qrcode">
					</div>
					
					<script type="text/javascript">
					//var bbb='${unBindQQ}';
					//var ccc='${bindStatus}';
					
					var uri = window.location.href;
					$('#qrcode').qrcode({
						width : 100,
						height : 100,
						text : uri
					});
				</script>
				</div>
				
			</div>
			<div class="container user-info">
				<h4 class="user-info-hl">用户信息</h4>
				<div class="row">
					<div class="col-xs-4">UID</div>
					<div class="col-xs-8">${user.id }</div>
				</div>
				<div class="row">
					<div class="col-xs-4">积分</div>
					<div class="col-xs-8">--</div>
				</div>
				<div class="row">
					<div class="col-xs-4">注册IP</div>
					<div class="col-xs-8">${user.createIP }</div>
				</div>
				<div class="row">
					<div class="col-xs-4">注册时间</div>
					<div class="col-xs-8"><date:date value='${user.createTime }'
								fmtstr='yyyy-MM-dd HH:mm:ss' /></div>
				</div>
				<div class="row">
					<div class="col-xs-4">上次IP</div>
					<div class="col-xs-8">${user.lastLoginIP }</div>
				</div>
				<div class="row">
					<div class="col-xs-4">上次时间</div>
					<div class="col-xs-8"><date:date value='${user.lastLoginTime }'
								fmtstr='yyyy-MM-dd HH:mm:ss' /></div>
				</div>
				<c:if test="${isadmin || own}">
				<div class="row">
					<div class="col-xs-4">真实姓名</div>
					<div class="col-xs-8">${user.truename }</div>
				</div>
				<div class="row">
					<div class="col-xs-4">审核人</div>
					<div class="col-xs-8">---</div>
				</div>
				</c:if>
				<h4 class="user-info-hl">用户专辑列表</h4>
				<div class="row">
					<div class="col-xs-7"><a href="javascript:;">即将出现……</a></div>
					<div class="col-xs-5">博客</div>
				</div>
				
			</div>
		</div>

		<script
			src="${pageContext.request.contextPath}/scripts/bootstrap-3.3.7-dist/js/bootstrap.min.js"></script>
		<script type="text/javascript">
			var con = false;
			$
					.ajax({
						url : '${pageContext.request.contextPath}/action_user/match/${user.id}',
						type : 'post',
						async : false,
						success : function(data) {
							if (data == 0) {
								$('#pws').show();
								con = true;
							} else {
								$('#pws').hide();
							}
						}
					});
			layui
					.use(
							[ 'element', 'upload' ],
							function() {
								var element = layui.element, upload = layui.upload;

								if (con) {
									//执行实例
									var uploadInst = upload
											.render({
												elem : '#avatar' //绑定元素
												,
												url : '${pageContext.request.contextPath}/action_file/upload' //上传接口
												/* ,accept: 'file'
												,exts: 'jpg' */
												,
												done : function(res) {

													if (res.code == 0) {
														var avatarUrl = res.data.src;

														$
																.ajax({
																	url : '${pageContext.request.contextPath}/action_user/avatar?uid=${user.id }&avatar='
																			+ res.data.src,
																	type : 'post',
																	async : false,
																	success : function(
																			data) {
																		if (data == 0) {
																			$(
																					"#avatar")
																					.attr(
																							'src',
																							avatarUrl);
																			layer
																					.msg("头像更新成功！页面刷新以后才能正确显示。");
																		} else {
																			layer
																					.msg("头像更新失败！");
																		}
																	}
																});

														//location.reload();
													} else {
														layer.alert("上传失败！");
													}
													//上传完毕回调
												},
												error : function() {
													//请求异常回调
												}
											});

								}

								//form.render();
							});

			var mobile = "${user.mobile }";
			if (mobile==""){
				mobile="未绑定${tmpSuxBind}";
			}else{
				mobile = mobile.replace(/(\d{4})\d{4}(\d{3})/, '$1****$2');
			}
			
			$("#user-mobile").html(mobile);
			var avatarUrl = "${user.avatarUrl }";

			if (avatarUrl.length < 5) {
				avatarUrl = '${requestScope.avatarNull }';
			}

			$("#avatar").attr('src', avatarUrl);
			
			function bind(otype,state) {
				var targetApp;
				switch (otype){
				case 1:
					targetApp="微信";
					break;
				case 2:
					targetApp="微博";
					break;
					default:
						targetApp="QQ";
				}
				
				if (state==0){
					parent.layer.confirm('您确定解除'+targetApp+'互联 吗？ 删除后将不能得利用'+targetApp+'互联登录！',{title:'提醒'}, function(index){
						  
						  var uri="${pageContext.request.contextPath}/action_user/connecter/ubind/"+otype;
						//layer.alert(uri);
							$.ajax({
								url: uri,
								type: 'post',
								success: function(data) {
									if (data==0){
										parent.layer.msg('解除成功！');
										location.reload(); 
									}else{
										parent.layer.msg('解除失败！错误号：'+data);
									}
									
								}
							});
						
							parent.layer.close(index);
						});
				}else{
					var w=parentW();
					var url;
					var href=w.location.href;
					url="${pageContext.request.contextPath}/action_user/connecter/auth/"+otype+"?referer="+href;
					w.location.href=url;

					
				}
				
				
			}
			
			if (${own}==false){
				$("a").removeAttr("href");
			}
		</script>
</body>
</html>
