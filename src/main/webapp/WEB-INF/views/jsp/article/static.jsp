<%@ page trimDirectiveWhitespaces="true" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!doctype html>
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
		<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html;charset=utf-8">
<script type="text/javascript"
	src="${pageContext.request.contextPath}/scripts/js/jquery/1.9.1/jquery.min.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/scripts/js/jquery/jquery.timers-1.2.js"></script>
<link rel="stylesheet" href="${pageContext.request.contextPath}/scripts/js/layui/v2/css/layui.css?t=${tu.getCurrTime()}" media="all">
<script src="${pageContext.request.contextPath}/scripts/js/layui/v2/layui.js"></script>
<title>Insert title here</title>
<style type="text/css">
.main{width: 90%;text-align: center;margin: 40px 20px 0 20px; }
.layui-progress {margin-top: 20px;margin-bottom: 40px;}
.txt{margin-top: 15px;}
.site-demo-button{margin-top: 60px;}
</style>
</head>
<body>
<div class="main">
<div class="layui-progress layui-progress-big" lay-showpercent="true" lay-filter="demo">
  <div class="layui-progress-bar layui-bg-red" lay-percent="0%"></div>
</div>		  
<div class="txt" id="aa"> 提醒：根据文章数量多少，会占用不同的时间。请耐心等待。</div>
<div class="txt">需处理的总数：<span id="total">尚未检测</span></div>
<div class="txt">已完成：<span id="finished">尚未开始</span></div>
<div class="site-demo-button" style="margin-top: 20px; margin-bottom: 0;">
  <button id="dostart" class="layui-btn site-demo-active" data-type="setPercent">开始处理</button>
  <button id="dofinish" class="layui-btn layui-hide" data-type="setPercent">处理完成！</button>
</div>

<script>
layui.use('element', function(){
  var $ = layui.jquery
  ,element = layui.element; //Tab的切换功能，切换事件监听等，需要依赖element模块
  
  //触发事件
  var active = {
    setPercent: function(){
    	//$('.site-demo-active').text('正在处理，请耐心等待……');
    	$('.site-demo-active').addClass("layui-hide");
    	$("#dofinish").text('正在处理，请耐心等待……');
    	$("#dofinish").removeClass("layui-hide");
    	
    	element.progress('demo', '1%');
      //设置进度
      var url='${pageContext.request.contextPath}/action_static/art';
      var lastid=0,finished=0,total=0,tag=0;

      $('body').everyTime('5s',function(){

		  	var uri = url + '/'+lastid+'/'+finished+'/'+total;
		  	if (finished == total && tag==1){
		  		$('body').stopTime ();
		  		$("#dofinish").text('处理完成！');
		  		parent.layer.alert ('生成结束，成功：' +finished + '篇。全部：'+total + '篇。');
		  		
		  	}else{
		  		$('#cc').html('uri:'+uri);
		  		$.ajax({
		  			url: uri,
		  			type: 'post',
		  			//async: false,
		  			success: function(data) {
		  				lastid=data.lastID;
		  				finished=data.finished;
		  				total=data.total;
		  				tag=data.tag;
		  				$('#finished').html(''+finished);
		  				$('#total').html(''+total);
		  				$('#aa').html('正在处理:'+data.msg);
		  				//$('#dd').html(finished*100 / total + '%');
		  				
		  				element.progress('demo', Math.round(finished*100 / total )+ '%');
		  				
		  				//element.progress('test1', finished*100 / total + '%');
		  				
		  			}
		  		});
		  		
		  	}

		  });
      
      //设置进度
    }
    
  };
  
  $('.site-demo-active').on('click', function(){
    var othis = $(this), type = $(this).data('type');
    active[type] ? active[type].call(this, othis) : '';
  });
});
</script>
</div>
</body>
</html>