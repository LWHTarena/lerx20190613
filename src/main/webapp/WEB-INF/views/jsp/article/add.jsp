<%@ page trimDirectiveWhitespaces="true" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="/tags" prefix="date" %>
<jsp:useBean id="dateValue" class="java.util.Date"/>
<jsp:useBean id="sutil" class="com.lerx.sys.util.StringUtil"
	scope="page"></jsp:useBean>
<jsp:useBean id="hq" class="com.lerx.sys.util.HttpUtil" scope="request"></jsp:useBean>
<jsp:useBean id="tu" class="com.lerx.sys.util.TimeUtil" scope="page" ></jsp:useBean>
<jsp:useBean id="article" class="com.lerx.entities.Article"
	scope="request"></jsp:useBean>
	<c:if test="${empty token}">
	  <c:set var="token" value="${hq.buildToken()}" scope="request"></c:set>
	</c:if>

<!doctype html>
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html;charset=utf-8">
<title>编辑</title>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/scripts/js/jquery/1.9.1/jquery.min.js"></script>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/scripts/js/layui/v2/css/layui.css?t=${tu.getCurrTime()}"
	media="all">
<script src="${pageContext.request.contextPath}/scripts/js/layui/v2/layui.js"></script>
<script src="${pageContext.request.contextPath}/scripts/js/lerxui/gen.js"></script>
<script src="${pageContext.request.contextPath}/scripts/js/wangEditor/3.1.1/wangEditor.min.js"></script>

<style type="text/css">

.runtest {
margin-top: 5px;}

.layui-form-label {width: 120px;}
.layui-form-block,.layui-input-block{
	margin-left:150px;margin-right:50px;
}

.toppos {
	margin-top: 20px;
}

.addbtn {
	position:absolute;
	bottom:40px;
	margin-top:0;
	margin-left:0px;
	width:100%;
}
.addbtn .layui-btn{
margin:0 0 0 300px;
}

.tips {
	margin-left: 20px;
}
.layui-input-block{
	margin-left:150px;
}
#test1 .layui-icon,#test2 .layui-icon{
	display:inline-block;
	margin-top:8px ;
	font-size:24px;
}
.addtime{padding: 9px 15px;}

.uploadFile{ width:70% }
.layui-form-select{z-index:10002;}
.layui-icon-search{margin-right: 0px;cursor: pointer;float:right}
</style>
</head>
<body  class="layui-layout-body">
	<div class="runtest">
		<form:form commandName="article" id="articleedit" htmlEscape="false"
			action="${pageContext.request.contextPath}/action_article/add"
			class="layui-form" method="post">
			<input type="hidden" name="token" value="${token}" />
			
<div class="layui-tab layui-tab-brief" lay-filter="docDemoTabBrief">
  <ul class="layui-tab-title">
    <li class="layui-this">基本内容</li>
    <li>图片与视频</li>
    <li>扩展</li>
    <li>作者</li>
    <li>附件</li>
  </ul>
  <div class="layui-tab-content" style="height: 100px;">
    <div class="layui-tab-item layui-show">
    <!-- 第1个标签 -->
			<div class="layui-form-item">
				<label class="layui-form-label">栏目 ：</label>
				<div class="layui-input-block">
				
				<form:select path="agroup.id" id="agroup" lay-filter="agroup">
				
				<c:forEach items="${prelist }" var="ag" varStatus="st">
				<form:option label="${ag.title }" disabled="${!ag.clogging }" value="${ag.id }"   >
				
				</form:option>
				</c:forEach>  
					
              		 
           		</form:select> 
           

				</div>
			</div>
			
			<div class="layui-form-item">
				<label class="layui-form-label">完整标题 ：</label>
				<div class="layui-input-block">
					<form:input path="subject" id="subject" required="true" lay-verify="required"
						placeholder="请输入文章标题" autocomplete="off" class="layui-input" />
					<form:errors path="subject"></form:errors>

				</div>
			</div>
			
			
			<div class="layui-form-item">
				<label class="layui-form-label">正文：</label>
				<div class="layui-input-block">
				<div id="wang">
				
				</div>
				<form:textarea path="content" id="content" htmlEscape="true" style="display: none;"  />
				</div>
			</div>
			

    </div>
    <div class="layui-tab-item">
    <!-- 第2个标签 -->
    		<div class="layui-form-item">
				<label class="layui-form-label">缩略图 ：</label>
				<div class="layui-input-block">
					<form:input path="thumbnail" id="thumbnail" placeholder="缩略图URL（可选）" autocomplete="off" class="layui-input layui-input-inline uploadFile" />
					<form:errors path="thumbnail"></form:errors><a id="test1" title="上传"  style="cursor:pointer;">　<i class="layui-icon">&#xe681;</i> 上传　</a>

				</div>
			</div>
			
			<div class="layui-form-item">
							<label class="layui-form-label">标题图 ：</label>
							<div class="layui-input-block">
								<form:input path="titleImg" id="titleImg" placeholder="标题图URL（可选）" autocomplete="off" class="layui-input layui-input-inline" />
								<form:errors path="titleImg"></form:errors><a id="test2" title="上传"  style="cursor:pointer;">　<i class="layui-icon">&#xe681;</i> 上传　</a>

							</div>
			</div>
			
			<div class="layui-form-item">
							<label class="layui-form-label">视频文件 ：</label>
							<div class="layui-input-block">
								<form:input path="mediaUrl" id="mediaUrl" placeholder="媒体文件URL（可选），请使用mp4文件" autocomplete="off" class="layui-input" />
								<form:errors path="mediaUrl"></form:errors>

							</div>
			</div>
			


	</div>
    <div class="layui-tab-item">
	<!-- 第3个标签 -->
			<div class="layui-form-item">
							<label class="layui-form-label">精简标题 ：</label>
							<div class="layui-input-block">
								<form:input path="subjectShort"  placeholder="请输入文章精简标题（可选，请精简后填写，不精简无须填写）" autocomplete="off" class="layui-input" />
								<form:errors path="subjectShort"></form:errors>

							</div>
			</div>
			
			<div class="layui-form-item">
							<label class="layui-form-label">附加标题 ：</label>
							<div class="layui-input-block">
								<form:input path="extra" placeholder="请输入一个附加标题（可选）" autocomplete="off" class="layui-input" />
								<form:errors path="extra"></form:errors>

							</div>
			</div>
			<div class="layui-form-item">
							<label class="layui-form-label">专题名称 ：</label>
							<div class="layui-input-block">
								<form:input path="toppic" placeholder="请输入一个专题名称（可选）" autocomplete="off" class="layui-input" />
								<form:errors path="toppic"></form:errors>

							</div>
			</div>
			
			<div class="layui-form-item">
							<label class="layui-form-label">强制跳转Url：</label>
							<div class="layui-input-block">
								<form:input path="jumpUrl" placeholder="若需要强制跳，请输入有效网址" autocomplete="off" class="layui-input" />
								<form:errors path="jumpUrl"></form:errors>

							</div>
			</div>
			
			<div class="layui-form-item">
							<label class="layui-form-label">文章简介：</label>
							<div class="layui-input-block">
							<form:textarea path="synopsis" id="synopsis" htmlEscape="true" class="layui-textarea" />
								<form:errors path="synopsis"></form:errors>

							</div>
			</div>
			
			<c:if test="${article.id >0 }">
				<form:hidden path="id" />
			<div class="layui-form-item">
				<label class="layui-form-label">状态：</label>
				<div class="layui-input-block">
				<c:if test="${admin }">
				<form:checkbox path="status" value="true" lay-skin="primary" title="审核通过" />
				</c:if>
					
					<form:checkbox path="poll.status" value="true" lay-skin="primary" title="允许调查" />
					<form:checkbox path="cb.status" value="true" lay-skin="primary" title="允许评论" />
				</div>
			</div>
			<c:if test="${admin }">
			
			<div class="layui-form-item">
				<label class="layui-form-label">发布时间：</label>
				<div class="addtime">
				<date:date value ='${article.creationTime }' fmtstr='yyyy-MM-dd HH:mm:ss' /> <a title="修改" href="javascript:chgAddtime(${article.id},'${article.subject}','<date:date value ='${article.creationTime }' fmtstr='yyyy-MM-dd HH:mm:ss' />');"> <i class="layui-icon">&#xe642;</i> </a>
				</div>
			</div>
			
			</c:if>
			</c:if>
						

	</div>
    <div class="layui-tab-item">
	<!-- 第4个标签 -->
		<div class="layui-form-item">
				<label class="layui-form-label">作者 ：</label>
				<div class="layui-input-block">
					<form:input path="author" 
						placeholder="请输入文章作者" autocomplete="off" class="layui-input" />
					<form:errors path="author"></form:errors>

				</div>
		</div>
		<div class="layui-form-item">
				<label class="layui-form-label">单位 ：</label>
				<div class="layui-input-block">
					<form:input path="authorDept" 
						placeholder="请输入文章作者单位" autocomplete="off" class="layui-input" />
					<form:errors path="authorDept"></form:errors>

				</div>
		</div>
		<div class="layui-form-item">
				<label class="layui-form-label">Email ：</label>
				<div class="layui-input-block">
					<form:input path="authorEmail" 
						placeholder="请输入文章作者的Email" autocomplete="off" class="layui-input" />
					<form:errors path="authorEmail"></form:errors>

				</div>
		</div>
		<div class="layui-form-item">
				<label class="layui-form-label">URL ：</label>
				<div class="layui-input-block">
					<form:input path="authorUrl" 
						placeholder="请输入文章作者的站点" autocomplete="off" class="layui-input" />
					<form:errors path="authorUrl"></form:errors>

				</div>
		</div>
	</div>
    <div class="layui-tab-item">
    
    <!-- 第4个标签 -->
    
    
    <table width="100%" height="100%">
  <colgroup>
    <col>
    <col width="200">
  </colgroup>
  <tbody>
    <tr>
      <td>
      <!-- 左侧附件区域 start -->

      <table class="layui-table">
      <colgroup>
        <col width="90">
        <col>
        <col width="90">
      </colgroup>

      <tbody id="attas">
        
      </tbody>
      </table>

      <!-- 左侧附件区域 end -->
      </td>
      <td height="100%" align="center">

      <!-- 右侧上传按钮区域 start -->

      <a id="test3" title="上传" class="layui-btn"  style="cursor:pointer;">　<i class="layui-icon layui-icon-upload-drag"></i> 点这里上传　</a>
      <div id="upbtn_lerx" class="layui-hide">隐藏的上传</div>
      <input id="aid" class="layui-hide" value="${article.id}" />
      <input id="attasStr" name="attasStr" class="layui-hide" value="">

      <!-- 右侧上传按钮区域 end -->

      </td>
    </tr>
   
  </tbody>
</table>


	</div>
  </div>
</div> 

<div class="layui-form-item addbtn">
				<div class="layui-input-block">
				<button  class="layui-btn" lay-submit 
						lay-filter="add" >提交</button>
				 <c:if test="${requestScope.admin }"><i onclick="javascript:editByID();" style="padding:12px 8px 0 0;" class="layui-icon layui-icon-search">搜ID编辑</i></c:if>
				</div>
</div>
		</form:form>
				
		<script type="text/javascript">
		
		var tmpID=${currTime};
		var E = window.wangEditor;
        var editor = new E('#wang');
        var $text1 = $('#content');
        editor.customConfig.customAlert = function (info) {
            parent.layer.alert('自定义提示：' + info);
        }
        
        editor.customConfig.uploadFileName = 'files';
        editor.customConfig.uploadImgServer = '${pageContext.request.contextPath}/action_file/uploadForWangEditor';
        editor.create();
        editor.txt.html($('#content').val()); 
		
			layui.use(['form','element','upload'], function() {
				var form = layui.form,
				element = layui.element,
				upload = layui.upload;
				
				var eindex=null;
				var content=null;
				
				
				/* layedit.set({
					  uploadImage: {
					    url: '${pageContext.request.contextPath}/action_file/upload' //接口url
					    ,type: 'post' //默认post
					  }
				}); */
		        
		       // eindex = layedit.build('content'); //建立编辑器

		       		       
				form.on('submit(add)', function(data){
					$text1.val(editor.txt.html());
					var attas = new Array();
					var i=0;
					var attasStr="";
					$('#attas').find('tr').each(function() {
						attas[i]=$(this).find('#attaID').text();
						if (i>0){
							attasStr += ','+attas[i];
						}else{
							attasStr += attas[i];
						}
						i++;
						//alert("id:"+$(this).find('#attaID').text());
		            })
		            
		            $('#attasStr').val(attasStr);
		            
					var gid=$("#agroup").val();
					if(!gid){
						parent.layer.alert("您必须选择文章所属的栏目后才能发布");
						return false;
					}
					/* if ($("#agroup").val() == 0  ){
						parent.layer.alert("您必须选择文章所属的栏目后才能发布");
						return false;
					} */
				});
				
				form.on('select(agroup)', function(data){
					
					/* if (eindex!=null){
						content= layedit.getContent(eindex);
					} */
					
					
					var w=0,h=0,imgIntact=0;
			    	var uri='${pageContext.request.contextPath}/action_agroup/smartWH/'+$('#agroup').val();
			         $.ajax({
							url: uri,
							type: 'post',
							async: false,
							success: function(data) {
								w=data.width;
								h=data.height;
								imgIntact=data.intact;
							}
					});
			        var euri;
			        if (imgIntact==1){
			        	euri='${pageContext.request.contextPath}/action_file/uploadForWangEditor?intact=true'; 
			        }else{
			        	euri='${pageContext.request.contextPath}/action_file/uploadForWangEditor?cw='+w+'&ch='+h; 
			        }
			        
			        
			        editor.customConfig.uploadImgServer = euri;
			        
			        //alert("euri:"+euri);
			        /* layedit.set({
						  uploadImage: {
						    url: euri //接口url
						    ,type: 'post' //默认post
						  }
					});
			        
			        eindex = layedit.build('content'); //建立编辑器
			        
			        if (content!=null){
			        	layedit.setContent(eindex,content);
			        } */
			         
				});
				
				
				//执行实例
				  var uploadInst = upload.render({
				    elem: '#test1' //绑定元素
				    ,url: '${pageContext.request.contextPath}/action_file/upload?cw=0&ch=0' //上传接口
				    /* ,accept: 'file'
				    ,exts: 'jpg' */
				    ,before: function(obj){ //obj参数包含的信息，跟 choose回调完全一致，可参见上文。
				    	//上传之前修改宽和高
				    	var w=0,h=0,imgIntact=0;
				    	var uri='${pageContext.request.contextPath}/action_agroup/smartWH/'+$('#agroup').val();
				         $.ajax({
								url: uri,
								type: 'post',
								async: false,
								success: function(data) {
									w=data.width;
									h=data.height;
									imgIntact=data.intact;
								}
						});
				         
				         var euri;
					        if (imgIntact==1){
					        	this.url='${pageContext.request.contextPath}/action_file/upload?intact=true'; 
					        }else{
					        	this.url='${pageContext.request.contextPath}/action_file/upload?cw='+w+'&ch='+h; 
					        }
				        
				      }
				    ,done: function(res){
				    	if (res.code==0){
				    		$("#thumbnail").val(res.data.src);
				    		//layer.msg("导入成功！");
				    		//location.reload();
				    	}else{
				    		layer.alert("上传失败！");
				    	}
				      //上传完毕回调
				    }
				    ,error: function(){
				      //请求异常回调
				    }
				  });
				
				
				  var uploadInst2 = upload.render({
					    elem: '#test2' //绑定元素
					    ,url: '${pageContext.request.contextPath}/action_file/upload?intact=true' //上传接口
					    /* ,accept: 'file'
					    ,exts: 'jpg' */
					    ,done: function(res){
					    	if (res.code==0){
					    		$("#titleImg").val(res.data.src);
					    		//layer.msg("导入成功！");
					    		//location.reload();
					    	}else{
					    		layer.alert("上传失败！");
					    	}
					      //上传完毕回调
					    }
					    ,error: function(){
					      //请求异常回调
					    }
					  });
				  
				  
				//……
				var uuri;
				var uploadInst3 = upload.render({
					    elem: '#test3' //绑定元素
					    ,url: '${pageContext.request.contextPath}/action_file/uploadAtta?aid='+$('#aid').val()+'&tid=${currTime}&intact=true&cw=0&ch=0' //上传接口
					    ,accept: 'file'
					    ,bindAction: '#upbtn_lerx'
					    ,auto: false //选择文件后不自动上传layui-layer-btn0
					    /*,exts: 'jpg' */
					    ,choose: function(obj){ //obj参数包含的信息，跟 choose回调完全一致，可参见上文。
					    	var uri;
					    	var uploadRander=this;
					    	layer.prompt({
								  formType: 0,
								  value: '无说明',
								  title: '请输入附件说明',
								  async: false,
								  area: ['800px', '350px'] //自定义文本域宽高
								}, function(value, index, elem){
									//value=encodeURI($.trim(value));
									if (value=="无说明"){
										value="";
									}
									value=encodel(value);
									uri='${pageContext.request.contextPath}/action_file/uploadAtta?aid='+$('#aid').val()+'&tid='+tmpID+'&intact=true&cw=0&ch=0&title='+value;
									uuri=uri;
									
									$("#upbtn_lerx").click();
									layer.close(index);
									
							});
					      }
				 		,before: function(obj){ //obj参数包含的信息，跟 choose回调完全一致，可参见上文。
				         
				    		this.url=uuri;
				 			//$("#urlshow").html(this.url);
				        
				      	}
					    ,done: function(res){
					    	if (res>0){
					    		
					    		layer.msg("上传成功！");
					    		artattasReload('${pageContext.request.contextPath}',$('#aid').val(),tmpID,'attas');
					    		//location.reload();
					    	}else{
					    		var mesdd="";
					    		switch(res){
					    		case -8:
					    			mesdd="错误消息：该附件已存在！";
					    			break;
					    		case -13:
					    			mesdd="错误消息：无附属目标！";
					    			break;
					    		default:
					    			mesdd="错误号："+res;
					    		
					    		}
					    		layer.alert("上传失败！"+mesdd);
					    	}
					      //上传完毕回调
					    }
					    ,error: function(){
					      //请求异常回调
					    }
					  });

				//但是，如果你的HTML是动态生成的，自动渲染就会失效
				//因此你需要在相应的地方，执行下述方法来手动渲染，跟这类似的还有 element.init();
				form.render();
			});
			
    		
			artattasReload('${pageContext.request.contextPath}',$('#aid').val(),tmpID,'attas');
			
			function editByID() {
				
				parent.layer.prompt({
					  formType: 0,
					  title: '请输入ID',
					  area: ['800px', '350px'] //自定义文本域宽高
					}, function(value, index, elem){
						if(!isNaN(value)){
							location.href="${pageContext.request.contextPath}/action_article/edit/"+value;
							
							
							parent.layer.close(index);
							
						}else{
							parent.layer.alert('请输入正确的ID号');
						}
							
						 
					  
					});
				
				
				
				
			}
			
			
			function chgAddtime(id,title,addtime){
				parent.layer.prompt({
					  formType: 0,
					  value: addtime,
					  title: '修改文章： “'+title+'” 的发布时间',
					  area: ['800px', '350px'] //自定义文本域宽高
					}, function(value, index, elem){
						if (value!=addtime){
							uri ='${pageContext.request.contextPath}/action_article/addtime/'+id+"?addtimeStr=" + escape(value);
							  $.ajax({
									url : uri,
									type : 'post',
									success : function(data) {
										
										if (data== 0){
											parent.layer.close(index);
											parent.layer.msg('<i class="layui-icon">&#xe618;</i> 修改成功！');
											//alert("修改成功！");
											location.reload(); 
										}else{
											parent.layer.msg('<i class="layui-icon">&#x1007;</i> 操作失败,！错误号：'+data+'。请检查您输入的格式是否有误！');
											//alert("修改时发生错误！");
										}
									}
								}); 
						}
						
					  
					});
				
				}  
			
			
			
		</script>
	</div>
<c:if test="${article.id >0 }">
<script type="text/javascript">
	$(window).load(function(){
		
		var index2 = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
		parent.layer.title('修改文章： “'+$("#subject").val()+'” 的内容',index2)  //再改变当前层的标题
		
	});
</script>
</c:if>
</body>
</html>