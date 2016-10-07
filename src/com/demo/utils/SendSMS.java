package com.demo.utils;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class SendSMS {
//	private static String Url = "http://api.isms.ihuyi.com/webservice/isms.php?method=Submit";
	private static String Url = "https://106.ihuyi.com/webservice/sms.php?method=Submit";
//	private static String Url = "https://106.ihuyi.cn/webservice/sms.php?method=Submit";
	
	public static String sendRegCode(String phone, String type) {
		
		HttpClient client = new HttpClient(); 
		PostMethod method = new PostMethod(Url); 
			
		//client.getParams().setContentCharset("GBK");
		client.getParams().setContentCharset("UTF-8");
		method.setRequestHeader("ContentType","application/x-www-form-urlencoded;charset=UTF-8");

		
		int mobile_code = (int)((Math.random()*9+1)*100000);

		//System.out.println(mobile);
//	    String content = new String("您正在注册鸡毛信DQB会员，验证码：" + mobile_code +"，请您在15分钟内完成验证，勿将验证码泄露给他人。");
//	    String content = new String("您正在找回鸡毛信DQB密码，验证码：" + mobile_code +"，请您在15分钟内完成验证，如非本人操作请忽略本条信息，勿将验证码泄露给他人。" + mobile_code);
		String content = "";
		if("reg".equals(type)){
			content = new String("您正在注册车业通会员，验证码：" + mobile_code +"，请您在15分钟内完成验证，勿将验证码泄露给他人。");
		}else{
		    content = new String("您正在找回车业通密码，验证码：" + mobile_code +"，请您在15分钟内完成验证，如非本人操作请忽略本条信息，勿将验证码泄露给他人。");
		}

		NameValuePair[] data = {//提交短信
//			    new NameValuePair("account", "cf_wzjmx"),
//			    new NameValuePair("password", "899d9f4e85f49d162176f5d6faa96af0"),//密码为APIKEY（可以登录用户中心查看）
			    new NameValuePair("account", "cf_cyt"),
			    new NameValuePair("password", "cb44b8ec5237bf8a191d35f11c0ec2e3"),//密码为APIKEY（可以登录用户中心查看）
			    new NameValuePair("mobile", phone), 
			    new NameValuePair("content", content),
		};
		
		method.setRequestBody(data);	
		System.out.println(mobile_code);
//		
		try {
			client.executeMethod(method);	
			
			String SubmitResult =method.getResponseBodyAsString();
					
			//System.out.println(SubmitResult);

			Document doc = DocumentHelper.parseText(SubmitResult); 
			Element root = doc.getRootElement();


			String code = root.elementText("code");	
			String msg = root.elementText("msg");	
			String ismsid = root.elementText("smsid");	
			
			
			System.out.println(code);
			System.out.println(msg);
			System.out.println(ismsid);
						
			 if("2".equals(code)){
				System.out.println("短信提交成功");
			}
			
		} catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		return 	mobile_code+"";
	}
	
}
