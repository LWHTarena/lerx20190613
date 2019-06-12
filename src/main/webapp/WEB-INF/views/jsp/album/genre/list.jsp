<%@ page trimDirectiveWhitespaces="true" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="/tags" prefix="date" %>
<jsp:useBean id="dateValue" class="java.util.Date"/>
<jsp:setProperty name="dateValue" property="time" value="${timestampValue}"/>
<jsp:useBean id="tu" class="com.lerx.sys.util.TimeUtil" scope="page" ></jsp:useBean>
<jsp:useBean id="cu" class="com.lerx.sys.util.ContextUtil" scope="page" ></jsp:useBean>


<!DOCTYPE html>
<html>
<head>
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
  <meta name="renderer" content="webkit">
  <meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
  <title>专辑类型列表</title>
  <script type="text/javascript"
	src="${pageContext.request.contextPath}/scripts/js/jquery/1.9.1/jquery.min.js"></script>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/scripts/js/layui/v2/css/layui.css?t=${tu.getCurrTime()}" media="all">
  <script src="${pageContext.request.contextPath}/scripts/js/layui/v2/layui.js"></script>

  <style>
  
    .demo-carousel{height: 200px; line-height: 200px; text-align: center;}
    .addarea{
    position: fixed; /*or前面的是absolute就可以用*/ 
    /* top: 550px;  */
    margin-top: 20px;
    }
    .nullSoftware{height: 100px;text-align: center;margin-top: 30px;}
    .pagestr{text-align: center;margin-top: 10px;}
    .addarea,.addbuttom{
    	a:link{text-decoration:none;color:#2c374b;} 
		a:visited{text-decoration:none;color:#2c374b;} 
		a:hover{text-decoration:underline;color:#c00;} 
		a:active{text-decoration:none;color:#2c374b;}
    }
    .layui-btn{font-style: italic;    }
    .logout{float: right;
    padding-right: 10px;
    position: fixed; /*or前面的是absolute就可以用*/ 
    bottom: 5px;
    left: 10px;
    }
    
    .ver{float: right;
    padding-right: 10px;
    position: fixed; /*or前面的是absolute就可以用*/  
       bottom: 5px;
       right: 10px;}
    .layui-icon {font-size: 20px;}
    /* table {  
		table-layout: fixed;  word-break:break-all;  
		word-wrap:break-word;  
	} */
    </style>   
</head>
<body class="layui-layout-body">
<div class="layui-layout layui-layout-admin">

  
  
  

</div>

  <div class="plant_mian">
    <!-- 内容主体区域 -->
    <div style="padding: 15px;" id="main">

<!-- 开始 -->

<blockquote class="layui-elem-quote layui-quote-nm"> >> <i class="layui-icon layui-icon-theme"></i> 专辑类型列表　　<a title="增加" href="javascript:add();"> <i class="layui-icon" style="font-size: 18px;">&#xe61f;</i> </a></blockquote>
 <c:if test="${empty requestScope.rs.list }">
 <div class="nullSoftware">没有任何专辑类型。</div>
		
	</c:if>
<c:if test="${!empty requestScope.rs.list }">	
<table class="layui-table" lay-filter="demo">
  <thead>
    <tr>
		<th>序号</th>
		<th>ID</th>
		<th>名称</th>
		<th>状态</th>
		<th>自由注册</th>
		<th>内容审核</th>
		<th>评论</th>
		<th>调查</th>
		<th>创建时间</th>
		<th>建立用户</th>
		<th>操作</th>
    </tr>
   </thead> 
   <tbody>
    <c:forEach items="${requestScope.rs.list }" var="genre" varStatus="st">
    

					<tr>
						<td>${(rs.page-1)*rs.pageSize+st.index+1}</td>
						<td>${genre.id }</td>
						<td><a href="${pageContext.request.contextPath}/show_albgenre/index/${genre.id}" target="_blank">${genre.name }</a></td>
						<td><c:if test="${genre.status }">正常</c:if><c:if test="${genre.status==false }"><font color="red">禁用</font></c:if></td>
						<td><c:if test="${genre.open }">是</c:if><c:if test="${genre.open==false }"><font color="red">否</font></c:if></td>
						<td><c:if test="${genre.free }">自由</c:if><c:if test="${genre.free==false }"><font color="red">管控</font></c:if></td>
						<td><c:if test="${genre.comm }">允许</c:if><c:if test="${genre.comm==false }"><font color="red">禁止</font></c:if></td>
						<td><c:if test="${genre.poll }">允许</c:if><c:if test="${genre.poll==false }"><font color="red">禁止</font></c:if></td>
						<td><date:date value ='${genre.createTime }' fmtstr='yyyy-MM-dd HH:mm:ss' /></td>
						<td>${genre.creator.username }</td>
						<td><a title="编辑" href="javascript:details(${genre.id },'${genre.name }')"> <i class="layui-icon">&#xe642;</i> </a>　 <a title="删除" class="delete" href="javascript:del(${genre.id },'${genre.name }')"> <i class="layui-icon">&#xe640;</i> </a></td>
					</tr>
	</c:forEach>
  </tbody>
</table>
 </c:if>
 
 <div class="pagestr" id="pagestr"></div>
 <script lang='javascript' type='text/javascript'> 
	
	/*
	
	■★▲◆  url 中的person url: '${pageContext.request.contextPath}/action_person/list'
	
	
	
			$(function() {
				
				$(".pagestr").pagefmt( {
					fmt:0,
					pageList:true,
					showall:true,
					showEnds:true,
					cur: ${requestScope.rs.page},
					pagesize: ${requestScope.rs.pageSize},
					pagecount: ${requestScope.rs.pageCount},
					url: '${pageContext.request.contextPath}${pageUrl}'
				} );
			});
	*/		
			function getTime(/** timestamp=0 **/) {  
			    var ts = arguments[0] || 0;  
			    var t,y,m,d,h,i,s;  
			    t = ts ? new Date(ts*1000) : new Date();  
			    y = t.getFullYear();  
			    m = t.getMonth()+1;  
			    d = t.getDate();  
			    h = t.getHours();  
			    i = t.getMinutes();  
			    s = t.getSeconds();  
			    // 可根据需要在这里定义时间格式  
			    return y+'-'+(m<10?'0'+m:m)+'-'+(d<10?'0'+d:d)+' '+(h<10?'0'+h:h)+':'+(i<10?'0'+i:i)+':'+(s<10?'0'+s:s);  
			}

			
			function add() {
				parent.layer.open({
				    type: 2,
				    skin: 'layui-layer-rim', //加上边框
				title: '新增：请输入专辑类型信息',
				    area : ['700px' , '500px'],
				    content: '${pageContext.request.contextPath}/action_albgenre/beforeAdd',
				    success: function(layero, index){
				    },
				    end: function(){
				    	location.reload();
				        
				      }

				});
				
			}

			function details(id,name) {
				parent.layer.open({
				    type: 2,
				    skin: 'layui-layer-rim', //加上边框
				    title: '修改：请输入专辑类型 “'+name+'” 的信息',
				    area : ['700px' , '600px'],
				    content: '${pageContext.request.contextPath}/action_albgenre/edit?id='+id,
				    success: function(layero, index){
				    },
				    end: function(){
				    	location.reload();
				        
				      }

				});
				
			}

			function del(id,name){
				parent.layer.confirm('您确定删除专辑类型  “'+name+'” 吗？ 删除后将不可恢复！',{title:'警告'}, function(index){
					  
					  var uri="${pageContext.request.contextPath}/action_albgenre/del?id="+id;
					
						$.ajax({
							url: uri,
							type: 'post',
							success: function(data) {
								if (data==0){
									parent.layer.msg('删除成功！');
								}else{
									parent.layer.msg('删除失败！错误号：'+data);
								}
								location.reload(); 
							}
						});
					
						parent.layer.close(index);
					});
			}
			
			
			function newAlb() {
				parent.layer.open({
				    type: 2,
				    skin: 'layui-layer-rim', //加上边框
				title: '新增：请输入专辑类型信息',
				    area : ['700px' , '500px'],
				    content: '${pageContext.request.contextPath}/action_albgenre/beforeAdd',
				    success: function(layero, index){
				    },
				    end: function(){
				    	location.reload();
				        
				      }

				});
				
			}
			
</script>

<script type="text/javascript">

layui.use(['laypage', 'layer', 'table', 'element'], function(){
  var laypage = layui.laypage //分页
  ,layer = layui.layer //弹层
  ,table = layui.table //表格
  ,element = layui.element; //元素操作
  
  //分页
  laypage.render({
    elem: 'pagestr' //分页容器的id
    ,count: ${rs.count} //总页数
    ,limit:${rs.pageSize}
    ,curr:${rs.page}
    ,skin: '#1E9FFF' //自定义选中色值
    //,skip: true //开启跳页
    ,jump: function(obj, first){
      if(!first){
        
        var jurl='${pageContext.request.contextPath}${pageUrl}';
        if (jurl.indexOf('?') != -1){
        	jurl+='&page='+obj.curr+'&pageSize=${rs.pageSize}';
        }else{
        	jurl+='?page='+obj.curr+'&pageSize=${rs.pageSize}';
        }
        //layer.msg('第'+ obj.curr +'页 ' + jurl);
        window.location.href=jurl;
        
      }
    }
  });
  

  
});


</script>

<!-- 结束 -->

</div>
  </div>
  
  
</body>
</html>