package com.lerx.v5.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.support.ResourceBundleMessageSource;

import com.lerx.sys.util.FileUtil;
import com.lerx.sys.util.StringUtil;

public class ConfigUtil {
	
	public static boolean mailChk(ResourceBundleMessageSource messageSource,String mail){
		
		if (mail==null || mail.trim().equals("") || !StringUtil.emailTest(mail)){
			return false;
		}
		
		String[] mailtmp = mail.trim().split("@");
		
		String mailSuffixes = mailtmp[1];
		
		
		String suffixes = messageSource.getMessage("mail.send.allow.suffix", null,"", null);
		
		if (suffixes==null || suffixes.trim().equals("") || suffixes.trim().equals("null")){
			return true;
		}else{
			String[] suffix = suffixes.trim().split(",");
			String tmp;
			for (int i = 0; i < suffix.length; i++) {
				
				tmp = suffix[i];
				if (tmp==null){
					tmp="";
				}
				tmp=tmp.trim();
				if (mailSuffixes.equalsIgnoreCase(tmp)){
					return true;
				}
			}
		}
		return false;
		
	}
	
	
	public static String configContentsByComma(String filename,String charset){
		
		String classesPath = FileUtil.classesPath();
		String file = classesPath + filename;
		File f = new File(file);
		String tempString = null,str;
		if (f.exists()) {
			tempString="";
			String strn="\n";
			BufferedReader reader;
			try {
				FileInputStream fis=new FileInputStream(f);
				InputStreamReader isr = new InputStreamReader(fis,charset);
				reader = new BufferedReader(isr);
				/*reader = new BufferedReader(new InputStreamReader(new FileInputStream(
				        f), charset));*/
				while ((str = reader.readLine()) != null) {
					tempString+=str+strn;
				}
//							tempString = reader.readLine();
				fis.close();
				isr.close();
				reader.close();
				fis=null;
				isr=null;
				reader=null;
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (tempString!=null){
			tempString=tempString.trim();
			tempString=StringUtil.strReplace(tempString, "\n", ",");
			tempString=StringUtil.strReplace(tempString, ",,", ",");
		}
		return tempString;
		
	}
	
	public static String getAdminFolder(ResourceBundleMessageSource messageSource){
		String folder=messageSource.getMessage("admin.folder", null, "admin", null);
		return folder;
	}
	
	public static String getAvatarNullFile(ResourceBundleMessageSource messageSource,HttpServletRequest request) {
		String contextPath=request.getContextPath();
		if (contextPath==null || contextPath.trim().equals("")) {
			contextPath="/";
		}else {
			if (contextPath.endsWith("/")) {
				contextPath+="/";
			}
		}
		return contextPath+messageSource.getMessage("img.avatar.null", null, "images/login/noavatar_small.gif", null);
		
	}
	
	
	public static String strFilter(ResourceBundleMessageSource messageSource,String str) {
		String charset=messageSource.getMessage("charset", null, "UTF-8", null);
		String filterWords=ConfigUtil.configContentsByComma("filterWords", charset);
		String repTarget="";
		boolean rep=false;
		if (messageSource.getMessage("filter.replace", null, "true", null).trim().equals("true")) {
			rep=true;
			repTarget=messageSource.getMessage("filter.replace.target", null, "*", null);
		}
		str=StringUtil.filterByWords(str, filterWords, rep, repTarget);
		
		return str;
	}
	
}
