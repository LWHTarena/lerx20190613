package com.lerx.v5.util;

import java.net.MalformedURLException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class TestUtil {

/*public static void main(String[] args)  {
	
	
	String str="AccessToken [accessToken=2.00CT2O_B0xYjee25cb994eeccbHATC, expireIn=157679999, refreshToken=,uid=1200302332]";
	
	String rgex = "\\[(.*?)\\]";
	Pattern pattern = Pattern.compile(rgex); 
	Matcher matcher = pattern.matcher(str);
	String tmp1;
	while(matcher.find()) {
		tmp1=matcher.group(1);
		System.out.println("tmp1:"+tmp1);
		
	}
	
	}*/


	 public static void main(String[] args) {
		 
		 java.net.URL refurl;
		try {
			refurl = new java.net.URL("http://www.lerx.com/html/g1/2019/03-16/220825654-101.html");
			String viprhost = refurl.getHost();// 获取主机名
			System.out.println(viprhost);
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
			
//		 String name="lzh900u000334";
		/* for (int i=1;i<100;i++) {
			 nametmp=name;
				nametmp += StringUtil.covIntToStr(i,4);
				System.out.println("nametmp:"+nametmp);
			}*/
		 
//		 System.out.println(name.replaceAll("\\d+$",""));
		 
		/* String tmp="{\"access_token\":\"19_rd3DB0sPwKDWg2stmyFKwdk5x1oa9KMyo2APXf6gNeG6hpTLKjdxyxCDbLlCh5Kkjugo-d9_-5OqP-N9xLJCrEfjMPDnVu580fqIjAVlwBY\",\"expires_in\":7200,\"refresh_token\":\"19_NyjOrWnWmUssjA6vZiOD5CFF74ZEdG7qLPSIaa0PX-ddTM7Z1JLRq1YiG0_qVfyWVgPxxu_cHDvHzoJbW8p6kJdrXUur8bdsNLyVsWn-3z8\",\"openid\":\"oPjYM6Ipcr30pE7rUWM5vdu9EFdc\",\"scope\":\"snsapi_base\"}";
	        JsonObject  jsonObject = (JsonObject) new JsonParser().parse(tmp).getAsJsonObject();
	        String openid = jsonObject.get("openid").getAsString();
	        System.out.println("openid:"+openid);*/
	    }


public static String toPrettyFormat(String json) {
    JsonParser jsonParser = new JsonParser();
    JsonObject jsonObject = jsonParser.parse(json).getAsJsonObject();
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    return gson.toJson(jsonObject);
}


}
