package com.demo.utils;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.jpush.api.JPushClient;
import cn.jpush.api.common.resp.APIConnectionException;
import cn.jpush.api.common.resp.APIRequestException;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.audience.AudienceTarget;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;

public class JPush {

    protected static final Logger LOG = LoggerFactory.getLogger(JPush.class); 
    
	private static final String appKey = "e263c5bee9d7aeee2e31722f";
	private static final String masterSecret = "d96c98b6e25761e02ab1ae35";
    public static final String ALERT = "测试内容2016年7月23日18:21:27";  
	
    public static JPushClient jpushClient=null; 
	
	//sendCustomMessageWithAlias

	int sendNo = 1;
//	static String alias = "user_26c4d29029734b469cdbbed9e04c6ac4";	//Alias字符串 别名
//	static String alias2 = "tag1, tag2";	//Alias字符串 别名
	static String msgTitle = "测试标题";
	static String msgContent = "测试内容";
	
    public static Object testSendPush(String alias, String msgContent, Map<String, String> map) {  
    	jpushClient = new JPushClient(masterSecret, appKey);
        
    	PushPayload payload = null;
    	if("all".equals(alias)){
    		payload = buildPushObject_all_alias_alert(msgContent, map);  
    	}else{
    		payload = buildPushObject_alias_alert(alias, msgContent, map);  
    	}
    	PushResult result = null;
        try {  
            System.out.println(payload.toString());  
            result = jpushClient.sendPush(payload);  
            System.out.println(result+"................................");  
              
            LOG.info("Got result - " + result);  
              
        } catch (APIConnectionException e) {  
            LOG.error("Connection error. Should retry later. ", e);  
              
        } catch (APIRequestException e) {  
            LOG.error("Error response from JPush server. Should review and fix it. ", e);  
            LOG.info("HTTP Status: " + e.getStatus());  
            LOG.info("Error Code: " + e.getErrorCode());  
            LOG.info("Error Message: " + e.getErrorMessage());  
            LOG.info("Msg ID: " + e.getMsgId());  
        }  
    	return result;
    }
	
	public static PushPayload buildPushObject_all_alias_alert(String msgContent, Map<String, String> map) {  
		return PushPayload.newBuilder()  
			.setPlatform(Platform.all())//设置接受的平台  
			.setAudience(Audience.all())//Audience设置为all，说明采用广播方式推送，所有用户都可以接收到  
			.setNotification(Notification.newBuilder()  
                    .addPlatformNotification(IosNotification.newBuilder()  
                            .setAlert(msgContent)  
                            .setBadge(+1)  
                            .addExtra("from", "JPush")  
                            .addExtras(map)
                            .build())  
                    .build())    
            .setOptions(Options.newBuilder()  
                    .setApnsProduction(true)  
                    .build())  
			.build();  
	} 
	public static PushPayload buildPushObject_alias_alert(String alias, String msgContent, Map<String, String> map) {  
		return PushPayload.newBuilder()  
				.setPlatform(Platform.all())//设置接受的平台  
				.setAudience(Audience.alias(alias)) //指定某个用户接收
				.setNotification(Notification.newBuilder()  
                        .addPlatformNotification(IosNotification.newBuilder()  
                                .setAlert(msgContent)  
                                .setBadge(+1)  
                                .addExtra("from", "JPush")  
                                .addExtras(map)
                                .build())  
                        .build())  
				.setOptions(Options.newBuilder()  
						.setApnsProduction(true)  
						.build())  
				.build();  
	} 
	public static PushPayload buildPushObject_all_all_alert() {  
        return PushPayload.alertAll(ALERT);  
    }  
      
    public static PushPayload buildPushObject_android_tag_alertWithTitle() {  
        return PushPayload.newBuilder()  
                .setPlatform(Platform.android())  
                .setAudience(Audience.all())  
                .setNotification(Notification.android(ALERT, msgTitle, null))  
                .build();  
    }  
      
    public static PushPayload buildPushObject_android_and_ios() {  
        return PushPayload.newBuilder()  
                .setPlatform(Platform.android_ios())  
                .setAudience(Audience.tag("tag1"))  
                .setNotification(Notification.newBuilder()  
                        .setAlert("alert content")  
                        .addPlatformNotification(AndroidNotification.newBuilder()  
                                .setTitle("Android Title").build())  
                        .addPlatformNotification(IosNotification.newBuilder()  
                                .incrBadge(1)  
                                .addExtra("extra_key", "extra_value").build())  
                        .build())  
                .build();  
    }  
      
    public static PushPayload buildPushObject_ios_tagAnd_alertWithExtrasAndMessage() {  
        return PushPayload.newBuilder()  
                .setPlatform(Platform.ios())  
                .setAudience(Audience.tag_and("tag1", "tag_all"))  
                .setNotification(Notification.newBuilder()  
                        .addPlatformNotification(IosNotification.newBuilder()  
                                .setAlert(ALERT)  
                                .setBadge(5)  
                                .setSound("happy")  
                                .addExtra("from", "JPush")  
                                .build())  
                        .build())  
                 .setMessage(Message.content(msgContent))  
                 .setOptions(Options.newBuilder()  
                         .setApnsProduction(true)  
                         .build())  
                 .build();  
    }  
      
    public static PushPayload buildPushObject_ios_audienceMore_messageWithExtras() {  
        return PushPayload.newBuilder()  
                .setPlatform(Platform.android_ios())  
                .setAudience(Audience.newBuilder()  
                        .addAudienceTarget(AudienceTarget.tag("tag1", "tag2"))  
                        .addAudienceTarget(AudienceTarget.alias("user_26c4d29029734b469cdbbed9e04c6ac4", "alias2"))  
                        .build())  
                .setMessage(Message.newBuilder()  
                        .setMsgContent(msgContent)  
                        .addExtra("from", "JPush")  
                        .build())  
                .build();  
    }  
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String crttime = sdf.format(new Date());
		testSendPush("user_26c4d29029734b469cdbbed9e04c6ac4", "Hi:"+crttime, null);

//		float a = 90.20f;
//		float b = 9.80f;
//		
//		float price=89.89f;
//		int itemNum=3;
//		float totalPrice=price*itemNum;
//		float num=(float)(Math.round(totalPrice*1000)/1000);//如果要求精确4位就*10000然后/10000
//		
//		float p = 90.218f;
//		DecimalFormat decimalFormat=new DecimalFormat(".00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
//		String p2 = decimalFormat.format(p);//format 返回的是字符串
//		
//		System.out.println(a+b);
//		System.out.println(num);
//		System.out.println(p2);
//		System.out.println(Float.parseFloat("12.310"));
	}

}
