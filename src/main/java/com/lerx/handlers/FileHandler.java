package com.lerx.handlers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.lerx.annotation.Token;
import com.lerx.dao.iface.IArticleAttaDao;
import com.lerx.dao.iface.IArticleDao;
import com.lerx.dao.iface.IUploadedFileDao;
import com.lerx.dao.iface.IUserDao;
import com.lerx.entities.ArticleAtta;
import com.lerx.entities.UploadedFile;
import com.lerx.portal.obj.UploadArgs;
import com.lerx.portal.obj.UploadResultForWangEditor;
import com.lerx.sys.obj.FileInf;
import com.lerx.sys.obj.LayerUploadReturnData;
import com.lerx.sys.obj.LayerUploadedReturn;
import com.lerx.sys.util.FileUtil;
import com.lerx.sys.util.HttpUtil;
import com.lerx.sys.util.StringUtil;
import com.lerx.sys.util.SysUtil;
import com.lerx.v5.util.UploadUtil;

@RequestMapping("/action_file")
@Controller
public class FileHandler {
	
	private static final String LOGINPAGE = "jsp/user/login";
	
	@Autowired
	private ResourceBundleMessageSource messageSource;
	
	@Autowired
	private IUserDao userDaoImpl;
	
	@Autowired
	private IUploadedFileDao uploadedFileDaoImpl;
	
	@Autowired
	private IArticleDao articleDaoImpl;
	
	@Autowired
	private IArticleAttaDao articleAttaDaoImpl;
	
	@RequestMapping(value="/uploadAtta",method=RequestMethod.POST)
	@ResponseBody
	@Token(ajax = true,log=true,loginOrAdmin=true,mark="file--<upload>",failedPage = LOGINPAGE,msgKey="fail.nologin")
	public long uploadAtta(@RequestParam(value="file",required=true) MultipartFile file,@RequestParam(value = "targetFile", required = false) String targetFile,@RequestParam(value = "title", required = false) String title,@RequestParam(value = "intact", required = false) Boolean intact,@RequestParam(value = "cw", required = false) Integer cw,@RequestParam(value = "ch", required = false) Integer ch,@RequestParam(value = "aid", required = true) long aid,@RequestParam(value = "tid", required = false) long tid,HttpServletRequest request,HttpSession session) throws IOException{
		if (intact==null) {
			intact=false;
		}
		if (cw==null) {
			cw=0;
		}
		if (ch==null) {
			ch=0;
		}
		String uidStr=HttpUtil.getCookie(messageSource, request, "uid_lerx");
		long uid;
		if (uidStr!=null && !uidStr.trim().equals("") && StringUtil.isNumber(uidStr)) {
			uid = Long.valueOf(uidStr);
		}else {
			uid = 0L;
		}
		
//		System.out.println("uid:"+uid);
		targetFile = SysUtil.uploadTarget(messageSource, file.getOriginalFilename(), targetFile,uid);
		if (intact) {
			cw=0;
			ch=0;
		}else {
			if (cw==0) {
				cw=Integer.valueOf(messageSource.getMessage("img.resize.cut.default.width", null, "0", null));
			}
			if (ch==0) {
				ch=Integer.valueOf(messageSource.getMessage("img.resize.cut.default.height", null, "0", null));
			}
		}
		if (title.trim().equals("")) {
			title=null;
		}else{
			title = StringUtil.escapeUrl(title, 0);
		}
		UploadArgs ua = new UploadArgs();
		ua.setCh(ch);
		ua.setCw(cw);
		ua.setFile(file);
		ua.setIntact(intact);
		ua.setMessageSource(messageSource);
		ua.setRequest(request);
		ua.setSession(session);
		ua.setTargetFile(targetFile);
		ua.setUploadedFileDaoImpl(uploadedFileDaoImpl);
		ua.setUserDaoImpl(userDaoImpl);
		ua.setUid(uid);
		UploadedFile uf=upload(ua);
		if (uf==null){
			return -1;
		}
		ArticleAtta aa = new ArticleAtta();
		if (aid>0L) {
			aa.setArticle(articleDaoImpl.findByID(aid));
		}
		aa.setTmpID(tid);
		aa.setUf(uf);
		aa.setTitle(title);
		
		return articleAttaDaoImpl.add(aa);
		/*if (aa!=null) {
			return aa.getId();
		}else {
			return -1;
		}*/
		
	}
	
	@RequestMapping(value="/uploadForWangEditor",method=RequestMethod.POST)
	@ResponseBody
	@Token(ajax = false,log=true,loginOrAdmin=true,mark="file--<upload>",failedPage = LOGINPAGE,msgKey="fail.nologin")
	public UploadResultForWangEditor uploadForWangEditor(@RequestParam(value="files",required=true) MultipartFile[] files,@RequestParam(value = "intact", required = false) Boolean intact,@RequestParam(value = "cw", required = false) Integer cw,@RequestParam(value = "ch", required = false) Integer ch,HttpServletRequest request,HttpSession session) throws IOException{
		UploadResultForWangEditor urfwe=new UploadResultForWangEditor();
		if (files.length<1) {
			urfwe.setErrno(-1);
			return urfwe;
		}
		if (intact==null) {
			intact=false;
		}
		if (cw==null) {
			cw=0;
		}
		if (ch==null) {
			ch=0;
		}
		String uidStr=HttpUtil.getCookie(messageSource, request, "uid_lerx");
		long uid;
		if (uidStr!=null && !uidStr.trim().equals("") && StringUtil.isNumber(uidStr)) {
			uid = Long.valueOf(uidStr);
		}else {
			uid = 0L;
		}
		
		if (intact) {
			cw=0;
			ch=0;
		}else {
			if (cw==0) {
				cw=Integer.valueOf(messageSource.getMessage("img.resize.cut.default.width", null, "0", null));
			}
			if (ch==0) {
				ch=Integer.valueOf(messageSource.getMessage("img.resize.cut.default.height", null, "0", null));
			}
		}
		
		String targetFile;
		List<String> data= new ArrayList<String>();
		for (int i=0;i<files.length;i++) {
			MultipartFile file=files[i];
			targetFile=null;
			targetFile = SysUtil.uploadTarget(messageSource, file.getOriginalFilename(), targetFile,uid);
			
			UploadArgs ua = new UploadArgs();
			ua.setCh(ch);
			ua.setCw(cw);
			ua.setFile(file);
			ua.setIntact(intact);
			ua.setMessageSource(messageSource);
			ua.setRequest(request);
			ua.setSession(session);
			ua.setTargetFile(targetFile);
			ua.setUploadedFileDaoImpl(uploadedFileDaoImpl);
			ua.setUserDaoImpl(userDaoImpl);
			ua.setUid(uid);
			
			UploadedFile ue=upload(ua);
			data.add(HttpUtil.currRequest().getContextPath()+"/"+ue.getUrl());
			
		}
		
		
		urfwe.setData(data);
		urfwe.setErrno(0);
		return urfwe;
		
		
	}
	
	
	@RequestMapping(value="/upload",method=RequestMethod.POST)
	@ResponseBody
	@Token(ajax = false,log=true,loginOrAdmin=true,mark="file--<upload>",failedPage = LOGINPAGE,msgKey="fail.nologin")
	public LayerUploadedReturn upload(@RequestParam(value="file",required=true) MultipartFile file,@RequestParam(value = "targetFile", required = false) String targetFile,@RequestParam(value = "title", required = false) String title,@RequestParam(value = "intact", required = false) Boolean intact,@RequestParam(value = "cw", required = false) Integer cw,@RequestParam(value = "ch", required = false) Integer ch,HttpServletRequest request,HttpSession session) throws IOException{
		if (intact==null) {
			intact=false;
		}
		if (cw==null) {
			cw=0;
		}
		if (ch==null) {
			ch=0;
		}
		String uidStr=HttpUtil.getCookie(messageSource, request, "uid_lerx");
		long uid;
		if (uidStr!=null && !uidStr.trim().equals("") && StringUtil.isNumber(uidStr)) {
			uid = Long.valueOf(uidStr);
		}else {
			uid = 0L;
		}
		
//		System.out.println("uid:"+uid);
		targetFile = SysUtil.uploadTarget(messageSource, file.getOriginalFilename(), targetFile,uid);
		if (intact) {
			cw=0;
			ch=0;
		}else {
			if (cw==0) {
				cw=Integer.valueOf(messageSource.getMessage("img.resize.cut.default.width", null, "0", null));
			}
			if (ch==0) {
				ch=Integer.valueOf(messageSource.getMessage("img.resize.cut.default.height", null, "0", null));
			}
		}
		
		UploadArgs ua = new UploadArgs();
		ua.setCh(ch);
		ua.setCw(cw);
		ua.setFile(file);
		ua.setIntact(intact);
		ua.setMessageSource(messageSource);
		ua.setRequest(request);
		ua.setSession(session);
		ua.setTargetFile(targetFile);
		ua.setTitle(title);
		ua.setUploadedFileDaoImpl(uploadedFileDaoImpl);
		ua.setUserDaoImpl(userDaoImpl);
		ua.setUid(uid);
		
		UploadedFile ue=upload(ua);
		
		LayerUploadReturnData rd= new LayerUploadReturnData();
		LayerUploadedReturn lur=new LayerUploadedReturn();
		rd.setSrc(HttpUtil.currRequest().getContextPath()+"/"+ue.getUrl());
		lur.setData(rd);
		lur.setCode(0);
		lur.setMsg(ua.getMessageSource().getMessage("success.upload", null, "Upload success!",null));
		
		return lur;
		
	}
	
	private static UploadedFile upload(UploadArgs ua)  throws IOException{
		FileInf fi=FileUtil.upload(ua.getFile(),ua.getTargetFile(),ua.getCw(),ua.getCh());
		String filenameNoExt=fi.getNameNoExt();
		fi.setNameNoExt(filenameNoExt);
		UploadedFile fu = UploadUtil.coverFromFi(fi);
		fu.setUser(ua.getUserDaoImpl().findByID(ua.getUid()));
		fu.setRemark(ua.getTitle());
		UploadedFile existu=ua.getUploadedFileDaoImpl().findByMD5(fu.getMd5(), fu.getId());
		UploadedFile ue;
		if (existu!=null){
			FileUtil.delete(fu.getFullPath());
			ue=existu;
		}else{
			ue=fu;
			ua.getUploadedFileDaoImpl().add(ue);
		}
		
		/*LayerUploadReturnData rd= new LayerUploadReturnData();
		LayerUploadedReturn lur=new LayerUploadedReturn();
		rd.setSrc(HttpUtil.currRequest().getContextPath()+"/"+ue.getUrl());
		lur.setData(rd);
		lur.setCode(0);
		lur.setMsg(ua.getMessageSource().getMessage("success.upload", null, "Upload success!",null));*/
		return ue;
	}

}
