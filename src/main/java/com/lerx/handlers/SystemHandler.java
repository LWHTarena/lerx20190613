package com.lerx.handlers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.lerx.sys.util.StringUtil;

@RequestMapping("/action_system")
@Controller
public class SystemHandler {
	
	@RequestMapping("/random")
	@ResponseBody
	public String findTemplate() throws Exception{
		
		return StringUtil.uuidStr();
	}

}
