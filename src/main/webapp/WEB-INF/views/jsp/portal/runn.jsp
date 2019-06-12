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
<jsp:useBean id="su" class="com.lerx.sys.util.StringUtil" scope="page" ></jsp:useBean>

<jsp:useBean id="portal" class="com.lerx.entities.Portal" scope="request" ></jsp:useBean>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1,user-scalable=no">

<title>网站监控：${requestScope.portal.name} </title>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/scripts/js/jquery/1.9.1/jquery.min.js"></script>
<link rel="stylesheet" href="${pageContext.request.contextPath}/scripts/js/layui/v2/css/layui.css?t=${tu.getCurrTime()}" media="all">
<script src="${pageContext.request.contextPath}/scripts/js/layui/v2/layui.js"></script>
<script src="${pageContext.request.contextPath}/scripts/js/RGraph/libraries/RGraph.common.core.js" ></script>
<script src="${pageContext.request.contextPath}/scripts/js/RGraph/libraries/RGraph.line.js" ></script>
<style type="text/css">
.nopass {color: red;}
.layui-tab-content{margin-left: 10px;}
.layui-box{padding: initial;border-collapse:collapse; border:none;width: 100%;height: 100%}
.layui-box td{word-wrap:break-word;word-break: break-all;}
.built-in-table td{word-wrap:break-word;word-break: break-all;}
</style>
</head>
<body>
<c:if test="${adminLogin}">
<div style="color: red;margin:10px 5px 0 5px;">　　警告：本页面如果泄露给无关人员，可能对您的网站造成灾难性结果！请不要将本页面及页面地址给无关人员！<br />　　只有拥有后台管理权限的人员才能获得本页面地址！</div>
</c:if>
<fieldset class="layui-elem-field layui-field-title" style="margin-top: 10px;">
  <legend>服务器当前状态</legend>
</fieldset>

<table  class="layui-table">
		<thead>
         </thead>
         <tbody>
          <tr> 
            <td>操作系统： </td>
            <td>${requestScope.es.queryHashtable("os.name")}  ${requestScope.es.queryHashtable("os.version")} <c:if test="${level!='unknown'}">${level}</c:if>		
			</td>
          </tr>
          <tr> 
            <td>服务器当前时间</td>
            <td><date:date value ='${tu.getCurrTime() }' fmtstr='yyyy-MM-dd HH:mm:ss' /></td>
          </tr>
          <tr> 
            <td>服务启动时间</td>
            <td><date:date value ='${requestScope.runtime }' fmtstr='yyyy-MM-dd HH:mm:ss' /></td>
          </tr>
          <tr> 
            <td>服务运行时长</td>
            <td>${requestScope.de.day} 天  ${requestScope.de.hour} 小时   ${requestScope.de.minute} 分钟 ${requestScope.de.second} 秒 </td>
          </tr>
          <tr> 
            <td>启动用户及目录：</td>
            <td>${requestScope.es.queryHashtable("user.name")} (${requestScope.es.queryHashtable("user.dir")}) 
			</td>
          </tr>
		  <tr> 
            <td>Java 版本</td>
            <td>${requestScope.es.queryHashtable("java.runtime.name")} ${requestScope.es.queryHashtable("java.runtime.version")}</td>
          </tr>
          
          <tr> 
            <td>java.home</td>
            <td>${requestScope.es.queryHashtable("java.home")}</td>
          </tr>
          
          <tr> 
            <td>java.io.tmpdir</td>
            <td>${requestScope.es.queryHashtable("java.io.tmpdir")}</td>
          </tr>
          
          <tr> 
            <td>内存剩余</td>
            <td>
	            <table class="layui-table">
	            <tr>
	            <td>
		            <div class="layui-progress layui-progress-big" lay-showPercent="yes">
	  					<div class="layui-progress-bar layui-bg-green" lay-percent="${requestScope.percentMemory}%"></div>
					</div>
				</td>
	            </tr>
	            <tr>
	            <td>${requestScope.freeMemory}M / ${requestScope.totalMemory}M</td>
	            </tr>
	            </table>
            	
			</td>
          </tr>
          
          <tr> 
            <td>空间剩余(${requestScope.userDir})</td>
            <td>
	            <table class="layui-table">
	            <tr>
	            <td>
		            <div class="layui-progress layui-progress-big" lay-showPercent="yes">
	  					<div class="layui-progress-bar layui-bg-green" lay-percent="${requestScope.spacePercent}%"></div>
					</div>
				</td>
	            </tr>
	            <tr>
	            <td>${requestScope.spaceFree} ${requestScope.capacityUnit}  / ${requestScope.spaceTotal} ${requestScope.capacityUnit} </td>
	            </tr>
	            </table>
            	
			</td>
          </tr>
          
      </tbody>
</table>


<fieldset class="layui-elem-field layui-field-title" style="margin-top: 20px;">
  <legend>时刻流量表</legend>
</fieldset>
 
<div class="layui-tab layui-tab-card cvs-card">
  <ul class="layui-tab-title">
    <li class="layui-this" id="tab11" >今日</li>
    <li id="tab22">昨日</li>
    <li id="tab33">平均</li>
   
  </ul>
  <div class="layui-tab-content">
    <div class="layui-tab-item layui-show">

		<canvas id="cvsToday" width="352px" class="cvs" >[No canvas support]</canvas>
	</div>
    <div class="layui-tab-item">
    	<canvas id="cvsYesterday" width="352px" class="cvs" >[No canvas support]</canvas>
    </div>
    <div class="layui-tab-item">
		<canvas id="cvsAvg" width="352px" class="cvs" >[No canvas support]</canvas>
	</div>
    

   
  </div>
</div>

<script>
        window.onload = function ()
        {
            var lineToday = new RGraph.Line({
                id: 'cvsToday',
                data: [[${ip24HourToday}],[${vs24HourToday}]],
                options: {
                    textAccessible: true,
                    hmargin: 5,
                    tickmarks: 'endcircle',
                    colors: [['#099'],['#F00']],
                    labels: ['','1','','3','','5','','7','','9','','11','','13','','15','','17','','19','','21','','23']
                }
            }).draw();
           
            
           
           $("#tab22").click();
            var lineYesterday = new RGraph.Line({
                id: 'cvsYesterday',
                data: [[${ip24HourYesterday}],[${vs24HourYesterday}]],
                options: {
                    textAccessible: true,
                    hmargin: 5,
                    tickmarks: 'endcircle',
                    colors: [['#099'],['#F00']],
                    labels: ['','1','','3','','5','','7','','9','','11','','13','','15','','17','','19','','21','','23']
                }
            }).draw();
            
            $("#tab33").click();
            
            var lineAvg = new RGraph.Line({
                id: 'cvsAvg',
                data: [[${ip24HourAvg}],[${vs24HourAvg}]],
                options: {
                    textAccessible: true,
                    hmargin: 5,
                    tickmarks: 'endcircle',
                    colors: [['#099'],['#F00']],
                    labels: ['','1','','3','','5','','7','','9','','11','','13','','15','','17','','19','','21','','23']
                }
            }).draw();
           
            $("#tab11").click();
            
        };
    </script>

<fieldset class="layui-elem-field layui-field-title" style="margin-top: 20px;">
  <legend>数据统计</legend>
</fieldset>

<table  class="layui-table">
		<thead>
         </thead>
         <tbody>
          <tr> 
            <td>访问总数 (PV/IP)： </td>
            <td>${requestScope.psi.views} / ${requestScope.psi.ips}	</td>
          </tr>
          <tr> 
            <td>今日访问总数 (PV/IP)： </td>
            <td>${requestScope.vaToday.views} / ${requestScope.vaToday.ips}	</td>
          </tr>
          <tr> 
            <td>昨日访问总数 (PV/IP)： </td>
            <td>${requestScope.vaYesterday.views} / ${requestScope.vaYesterday.ips}	</td>
          </tr>
          <tr> 
            <td>文章总数 (发布/已核)</td>
            <td>${requestScope.psi.artsTotal} / ${requestScope.psi.artsPassed}	</td>
          </tr>
          <tr> 
            <td>会员总数 (注册/通过)</td>
            <td>${requestScope.psi.userTotal } / ${requestScope.psi.userPassed }</td>
          </tr>
          
      </tbody>
</table>


<div class="layui-tab layui-tab-card cvs-card">
  <ul class="layui-tab-title">
    <li class="layui-this" id="art1" >最新发布</li>
    <li id="art2">最近访问</li>
    <li id="art3">最热文章</li>
    <li id="art4">最新评论</li>
   
  </ul>
  <div class="layui-tab-content">
    <div class="layui-tab-item layui-show">

		<table  class="layui-table">
			<thead>
	          <tr > 
	            <th >序号</th>
	            <th >文章</th>
	            <th >会员</th>
	            <th >发布时间</th>
	           
	          </tr>
	         </thead>
	         <tbody>
	         <c:forEach items="${requestScope.newarts }" var="art" varStatus="st">
			 
<c:if test="${su.isEmpty(art.jumpUrl) == false }">
<c:set var="href" value="${art.jumpUrl }"></c:set>
</c:if>
<c:if test="${su.isEmpty(art.jumpUrl) && art.hfs.status }">
<c:set var="href" value="${art.hfs.url }"></c:set>
</c:if>
<c:if test="${su.isEmpty(art.jumpUrl) && art.hfs.status == false }">
<c:set var="href" value="${pageContext.request.contextPath}/show_portal/art/${art.id}"></c:set>
</c:if>

			 
			 
	         <tr> 
	            <td>${st.index+1} </td>
	            <td ><a <c:if test="${art.status == false }">class="nopass"</c:if> href="${href}" target="_blank">${art.subject} </a></td>
	            <td>${art.user.username } </td>
	            <td><date:date value ='${art.creationTime }' fmtstr='yyyy-MM-dd HH:mm:ss' /></td>
	          </tr>
	         </c:forEach>
	         
	          
	      </tbody>
	</table>
		
	</div>
    <div class="layui-tab-item">
    <table class="layui-table">
	  <thead>
	    <tr>
			<th>序号</th>
			<th>IP及访问信息</th>
	    </tr>
	   </thead> 
	   <tbody>
	    <c:forEach items="${requestScope.visitRs.list }" var="vip" varStatus="st">
	    
	
						<tr>
							<td>${(rs.page-1)*rs.pageSize+st.index+1}</td>
							<td class="layui-box">
							<table class="layui-table built-in-table">
							<tbody>
							<thead>
	          <tr > 
	            <th >${vip.ip } <c:if test="${vip.totalIn6Hours>1 }"> (${vip.totalIn6Hours})</c:if></th>
	           
	          </tr>
	         </thead>
							<tr><td>${vip.ipfrom}</td></tr>
							<tr><td><a target="_blank" href="${vip.visitUrl}">${vip.visitUrl}</a></td></tr>
							<tr><td><a target="_blank" href="${vip.reffer}">${vip.reffer}</a></td></tr>
							<tr><td><date:date value ='${vip.visitDatetime }' fmtstr='yyyy-MM-dd HH:mm:ss' /> </td></tr>
							</tbody>
							</table>
							</td>
							
						</tr>
		</c:forEach>
	  </tbody>
	</table>
    </div>
    
    <div class="layui-tab-item">
    
    
    <table  class="layui-table">
			<thead>
	          <tr > 
	            <th >序号</th>
	            <th >文章</th>
	            <th >IP数</th>
	            <th >发布时间</th>
	           
	          </tr>
	         </thead>
	         <tbody>
	         <c:forEach items="${requestScope.hotRs }" var="art" varStatus="st">
			 <c:if test="${art.hfs.status == false }">
			 <c:set var="href" value="${pageContext.request.contextPath}/show_portal/art/${art.id}"></c:set>
			 </c:if>
			 <c:if test="${art.hfs.status }">
			 <c:set var="href" value="${art.hfs.url }"></c:set>
			 </c:if>
	         <tr> 
	            <td>${st.index+1} </td>
	            <td ><a <c:if test="${art.status == false }">class="nopass"</c:if> href="${href}" target="_blank">${art.subject} </a></td>
	            <td>${art.vbook.ipOwn } </td>
	            <td><date:date value ='${art.creationTime }' fmtstr='yyyy-MM-dd HH:mm:ss' /></td>
	          </tr>
	         </c:forEach>
	         
	          
	      </tbody>
	</table>
	
    
	</div>
	
	<div class="layui-tab-item">
    
    
    <table class="layui-table">
	  <thead>
	    <tr>
			<th>序号</th>
			<th>最新评论信息</th>
	    </tr>
	   </thead> 
	   <tbody>
	    <c:forEach items="${requestScope.listComments }" var="cth" varStatus="st">
	    
	
						<tr>
							<td>${st.index+1}</td>
							<td class="layui-box">
							<table class="layui-table built-in-table">
							<tbody>
							<thead>
	          <tr > 
	            <th >出处：<a href="${cth.href}" target="_blank">${cth.subject}</a> </th>
	           
	          </tr>
	         </thead>
							<tr><td>${cth.ip} ${cth.ipfrom}</td></tr>
							<tr><td>评论内容：${cth.content}</td></tr>
							<tr><td><date:date value ='${cth.occurDatetime }' fmtstr='yyyy-MM-dd HH:mm:ss' /> </td></tr>
							</tbody>
							</table>
							</td>
							
						</tr>
		</c:forEach>
	  </tbody>
	</table>
	
    
	</div>
    

   
  </div>
</div>




<script>
layui.use(['element', 'layer'], function(){
  var element = layui.element;
  var layer = layui.layer;
  
  //监听折叠
  element.on('collapse(test)', function(data){
    layer.msg('展开状态：'+ data.show);
  });
  
  element.on('tab(demo)', function(data){
	    console.log(data);
	  });
  
});
</script>


</body>
</html>