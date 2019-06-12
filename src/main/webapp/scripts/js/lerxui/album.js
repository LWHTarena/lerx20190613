

/*
 * 用户注册协议
 */
function albumagree(path,gid) {
	var w=parentW();
	//准备协议
	$.get(path+'/action_albgenre/agree/'+gid, function(result){

		w.layer.open({
			formType: 2,
			title: '使用协议',
			btn: ['同意','不同意'],
			yes: function(index){
				w.layer.close(index); 
				var wtitle="空间名称";
				
					
				//注册
				
					w.layer.prompt({
						  formType: 0,
						  value: '',
						  title: '请输入您的'+wtitle
					}, function(value, index2, elem){
						var indexP = w.layer.load(1, {
							  shade: [0.1,'#fff'] //0.1透明度的白色背景
							});	
						//根据名称查找是否存在
						uri =path+'/action_album/findByName?name=' + value;
						var name=value;
						$.ajax({
							url : uri,
							type : 'post',
							success : function(data) {
								//----s
								w.layer.close(indexP); 
								w.layer.close(index2); 
								if (data){
									w.layer.msg("该名称已被占用！",{time: 3000});
								}else{
									
									uri2 =path+'/action_album/apply?name=' + name + '&gid='+gid;
									$.ajax({
										url : uri2,
										type : 'post',
										success : function(data2) {
											//----s
											switch (data2){
											case 0:
												w.layer.msg("申请成功！",{time: 3000});
												break;
											case -2:
												w.layer.msg("您没有申请该专辑的权限！",{time: 3000});
												break;
											case -3:
												w.layer.msg("请登录后再申请！",{time: 3000});
												break;
											case -5:
												w.layer.msg("您没有申请该专辑的权限！",{time: 3000});
												break;
											case -10:
												w.layer.msg("参数错误！错误号："+data2,{time: 3000});
												break;
											case -11:
												w.layer.msg("参数错误！错误号："+data2,{time: 3000});
												break;
											case -12:
												w.layer.msg("名称不能为空！",{time: 3000});
												break;
											case -18:
												w.layer.msg("名称中发现敏感词，操作被拒绝！",{time: 3000});
												break;
											case -20:
												w.layer.msg("相同类型的专辑数量超过限制！",{time: 3000});
												break;
											default:
												w.layer.msg("申请失败！错误号："+data2,{time: 3000});	
												
											}
											/*if (data2==0){
												w.layer.msg("申请成功！",{time: 3000});
												
											}else{
												
												w.layer.msg("申请失败！错误号："+data2,{time: 3000});
											}*/
											
										}
									});
									
//									w.layer.msg("申请成功！",{time: 3000});
								}
								
								
								
								//----e
							}
						});
					   //发送验证码--end
						
					});
				
				
				//reg();
			},
			btn2: function(index){
				w.layer.close(index);
			},
			area: ['800px', '350px'], //自定义文本域宽高
			content:'<textarea class="layui-layer-input" style="width: 740px; height: 200px;line-height: 20px;padding: 6px 10px;display: block;font-family: inherit;font-size: inherit;font-style: inherit;font-weight: inherit;outline: 0;border: 1px solid #e6e6e6;color: #333;">'+result+'</textarea>'
			});
				
		});
	
	
}


