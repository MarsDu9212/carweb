package com.demo.common.interceptor;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;

public class CarInterceptor implements Interceptor {
	
	private final static String[] agent = { "Android", "iPhone", "iPod","iPad", "Windows Phone", "MQQBrowser" };
	
	public void intercept(Invocation inv) {
		Controller controller = inv.getController();
		ServletRequest request = controller.getRequest();
		String   url  = request.getScheme()+"://"; //请求协议 http 或 https  
//		  url+=((HttpServletRequest) request).getHeader("host");  // 请求服务器  
		  url+=((HttpServletRequest) request).getRequestURI();     // 工程名    
		  if(((HttpServletRequest) request).getQueryString()!=null) //判断请求参数是否为空
		  url+="?"+((HttpServletRequest) request).getQueryString();   // 参数 
//		  System. out.println(url);  
		
		String ua = ((HttpServletRequest) request).getHeader("User-Agent");
//		System.out.println("----------------------------------  Interceptor "+checkAgentIsMobile(ua));
		
		if(!checkAgentIsMobile(ua)){
			controller.setAttr("msg", "非法请求");
			return;
		}
		inv.invoke();
	}
	/**
	* 判断User-Agent 是不是来自于手机
	* @param ua
	* @return
	*/
	public static boolean checkAgentIsMobile(String ua) {
		boolean flag = false;
		if (!ua.contains("Windows NT") || (ua.contains("Windows NT") && ua.contains("compatible; MSIE 9.0;"))) {
			// 排除 苹果桌面系统
			if (!ua.contains("Windows NT") && !ua.contains("Macintosh")) {
				for (String item : agent) {
				if (ua.contains(item)) {
					flag = true;
						break;
					}
				}
			}
		}
		return flag;
	}
	
}
