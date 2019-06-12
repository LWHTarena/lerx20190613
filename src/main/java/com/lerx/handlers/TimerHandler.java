package com.lerx.handlers;

import java.util.List;

import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.lerx.dao.iface.IGroupDao;
import com.lerx.entities.ArticleGroup;
import com.lerx.sys.util.FileUtil;
import com.lerx.v5.util.GroupUtil;

@Component
public class TimerHandler {

	
	@Autowired
	private IGroupDao groupDaoImpl;
	
	@Autowired
	private ResourceBundleMessageSource messageSource;
	
	
   // @Scheduled(fixedRate = 1000*60*30)   
    public void htmlCreateUpdate(){ 
    	List<ArticleGroup> list=groupDaoImpl.queryChanged();
    	String realPath,strCreateDateTime,fromurl;
    	String charset=messageSource.getMessage("charset", null, "utf-8", null);
    	String htmlRoot=messageSource.getMessage("group.file.static.root", null, "html", null);
    	if (htmlRoot!=null && (htmlRoot.trim().equalsIgnoreCase("WebRoot") || htmlRoot.trim().equalsIgnoreCase("root")) ){
    		htmlRoot="";
    	}
    	String indexFile=messageSource.getMessage("file.html.default", null, "index.html", null);
    	
    	
    	String contextPath= messageSource.getMessage("context.path", null, "http://localhost/", null);
    	
    	strCreateDateTime=messageSource.getMessage("msg.datetime.html.static.create", null, "", null);
    	fromurl=contextPath+"show_portal/index";
    	String index=FileUtil.appPath()+indexFile;
    	
    	FileUtil.htmlBySniff(fromurl , index, strCreateDateTime,  charset);
    	
    	for (ArticleGroup ag:list) {
    		if (GroupUtil.openChk(ag) && (ag.getJumpToUrl()==null || ag.getJumpToUrl().trim().equals(""))) {
    			realPath = FileUtil.appPath()+GroupUtil.htmlFolder(ag,htmlRoot);
        		realPath = FileUtil.repairFilePath(realPath)+indexFile;
        		strCreateDateTime=messageSource.getMessage("msg.datetime.html.static.create", null, "", null);
        		fromurl=contextPath+"show_portal/nav/"+ag.getId();
        		FileUtil.htmlBySniff(fromurl, realPath, strCreateDateTime,  charset);
    		}
    	}
       /* SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd H:m:s");
        System.out.println("timer : "+format.format(new Date()));*/   
    } 
    
    
    
}
