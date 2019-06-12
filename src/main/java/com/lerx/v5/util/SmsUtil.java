package com.lerx.v5.util;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.json.JSONException;
import org.springframework.context.support.ResourceBundleMessageSource;

import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.github.qcloudsms.httpclient.HTTPException;
import com.lerx.portal.obj.Sms;
import com.lerx.portal.obj.SmsReturn;

import org.dom4j.Document;   
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;   
import org.dom4j.Element;

public class SmsUtil {

	public static Sms build(ResourceBundleMessageSource messageSource) {
		Sms sms = new Sms();
		sms.setAccount(messageSource.getMessage("sms.account", null, "", null));
		sms.setUrl(messageSource.getMessage("sms.url", null, "", null));
		sms.setPwd(messageSource.getMessage("sms.pwd", null, "", null));
		sms.setAppid(Integer.valueOf(messageSource.getMessage("sms.appid", null, "0", null)));
		sms.setCharset(messageSource.getMessage("charset", null, "UTF-8", null));
		if (sms.getPwd() == null || sms.getPwd().trim().equals("")) {
			return null;
		}
		return sms;
	}
	
	
	/*
	 * 互亿无线、秒赛
	 */
	public static SmsReturn sendOnIhuyi(Sms sms, String mobile, String content) {
		SmsReturn sr = new SmsReturn();
		if (sms == null) {
			sr.setResult(-11);
			return sr;
		}

		if (mobile == null || mobile.trim().equals("")) {
			sr.setResult(-13);
			return sr;
		}

		if (content == null || content.trim().equals("")) {
			sr.setResult(-12);
			return sr;
		}
		
		HttpClient client = new HttpClient(); 
		PostMethod method = new PostMethod(sms.getUrl());
		client.getParams().setContentCharset(sms.getCharset());
		method.setRequestHeader("ContentType","application/x-www-form-urlencoded;charset="+sms.getCharset());
		
		NameValuePair[] data = {//提交短信
			    new NameValuePair("account", sms.getAccount()), 
			    new NameValuePair("password", sms.getPwd()), //查看密码请登录用户中心->验证码、通知短信->帐户及签名设置->APIKEY
			    //new NameValuePair("password", util.StringUtil.MD5Encode("密码")),
			    new NameValuePair("mobile", mobile), 
			    new NameValuePair("content", content),
		};
		method.setRequestBody(data);
		
		try {
			client.executeMethod(method);
			
			String SubmitResult =method.getResponseBodyAsString();

			//System.out.println(SubmitResult);

			Document doc = DocumentHelper.parseText(SubmitResult);
			Element root = doc.getRootElement();

			String code = root.elementText("code");
			/*String msg = root.elementText("msg");
			String smsid = root.elementText("smsid");

			System.out.println(code);
			System.out.println(msg);
			System.out.println(smsid);*/

			 if("2".equals(code)){
//				System.out.println("短信提交成功");
				sr.setResult(0);
			}

		} catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			sr.setResult(-14);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			sr.setResult(-16);
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			sr.setResult(-15);
		}	
		
		return sr;
		
	}
	
	
	
	/*
	 * 腾讯云短信
	 */
	public static SmsReturn sendOnTencent(Sms sms, String mobile, String content) {
		SmsReturn sr = new SmsReturn();
		if (sms == null) {
			sr.setResult(-11);
			return sr;
		}

		if (mobile == null || mobile.trim().equals("")) {
			sr.setResult(-13);
			return sr;
		}

		if (content == null || content.trim().equals("")) {
			sr.setResult(-12);
			return sr;
		}
		
		try {
			
		    SmsSingleSender ssender = new SmsSingleSender(sms.getAppid(), sms.getPwd());
		    SmsSingleSenderResult result = ssender.send(0, "86", mobile,
		    		content, "", "");
		    sr.setResult(0 - result.result);
		    sr.setMsg(result.errMsg);
		    sr.setFee(result.fee);
		} catch (HTTPException e) {
		    // HTTP响应码错误
		    e.printStackTrace();
		    sr.setResult(-14);
		} catch (JSONException e) {
		    // json解析错误
		    e.printStackTrace();
		    sr.setResult(-15);
		} catch (IOException e) {
		    // 网络IO错误
		    e.printStackTrace();
		    sr.setResult(-16);
		}
		
		return sr;

	}

}
