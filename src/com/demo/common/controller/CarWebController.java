package com.demo.common.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.demo.common.model.Carsell;
import com.demo.common.model.Gogo;
import com.demo.common.model.Goods;
import com.demo.common.model.Service;
import com.demo.common.model.Store;
import com.demo.common.model.Wares;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;

public class CarWebController extends Controller {
	

	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	private static String crttime = sdf.format(new Date());

	public void index() {
	}
	

	//检测设备是否安装APP
	public void appInstalled_web(){
		String IMEI = System.getProperty("IMEI"); 
		String IMEI2 = System.getProperty("phone.IMEI"); 
		String IMEI3 = System.getProperty("com.siemens.IMEI"); 
		String IMEI4 = System.getProperty("com.nokia.mid.imei"); 
		String IMEI5 = System.getProperty("com.sonyericsson.imei"); 
		System.out.println("-----------------------------------"+IMEI);
		System.out.println("-----------------------------------"+IMEI2);
		System.out.println("-----------------------------------"+IMEI3);
		System.out.println("-----------------------------------"+IMEI4);
		System.out.println("-----------------------------------"+IMEI5);
		renderJson();
	}

	//商品详情 根据ID查询商品
	public void getGoodsById_web(){
		String sql = "select * from t_goods where goodid='"+getPara("id")+"' ";
		Goods goods = Goods.dao.findFirst(sql);
		setAttr("data", goods);
		renderFreeMarker("/html/car/1-findPJ-cpxq.html");
	}
	//修理  根据修理ID 查商家
	public void getXiuLiById_web(){
		String sql = "select * from t_service where id = '"+getPara("id")+"'";
		setAttr("data", Db.findFirst(sql));
		renderFreeMarker("/html/car/7-xiuli-shangjia.html");
	}
	//生活服务 根据商家ID 查商家
	public void getServiceByID_web(){
		String sql = "select * from t_wares where id = '"+getPara("id")+"'";
		Wares wares = Wares.dao.findFirst(sql);
		setAttr("data", wares);
		renderFreeMarker("/html/car/9-service-shangjia.html");
	}
	//保险 根据保险ID 查商家
	public void getBaoXianById_web(){
		String sql = "select * from t_service where id = '"+getPara("id")+"'";
		Service goods = Service.dao.findFirst(sql);
		setAttr("data",goods);
		renderFreeMarker("/html/car/11-baoxian-shangjia.html");
	}
	//保险 根据保险商品ID 查商品
	public void getBaoXianSById_web(){
		String sql = "select * from t_gogo where id = '"+getPara("id")+"'";
		Gogo goods = Gogo.dao.findFirst(sql);
		setAttr("data",goods);
		renderFreeMarker("/html/car/13-baoxian-xi.html");
	}
	//保险 根据汽贸ID 查商家
	public void getQiMaoById_web(){
		String sql = "select * from t_wares where id = '"+getPara("id")+"'";
		Wares wares = Wares.dao.findFirst(sql);
		setAttr("data", wares);
		renderFreeMarker("/html/car/13-qimao-shangjia.html");
	}	
	//保险 根据汽贸商品ID 查商品
	public void getQiMaoSById_web(){
		String sql = "select * from t_carsell where carsellid = '"+getPara("id")+"'";
		Carsell goods = Carsell.dao.findFirst(sql);
		setAttr("data",goods);
		renderFreeMarker("/html/car/13-qimao-xiangqing.html");
	}
	//保险 根据旧车ID 查商家
	public void getJiuCheById_web(){
		String sql = "select * from t_wares where id = '"+getPara("id")+"'";
		Wares wares = Wares.dao.findFirst(sql);
		setAttr("data", wares);
		renderFreeMarker("/html/car/14-jiuche-shangjia.html");
	}
	//保险 根据旧车商品ID 查商品
	public void getJiuCheSById_web(){
		String sql = "select * from t_carsell where carsellid = '"+getPara("id")+"'";
		Carsell goods = Carsell.dao.findFirst(sql);
		setAttr("data",goods);
		renderFreeMarker("/html/car/14-jiuche-xaingqing.html");
	}
	//保险 根据公估ID 查公估
	public void getGongGuById_web(){
		String sql = "select * from t_service where id = '"+getPara("id")+"'";
		Service goods = Service.dao.findFirst(sql);
		setAttr("data",goods);
		renderFreeMarker("/html/car/6-gonggu-shangjia.html");
	}
	//保险 根据油气ID 查油气
	public void getYouQiById_web(){
		String sql = "select * from t_service where id = '"+getPara("id")+"'";
		Service goods = Service.dao.findFirst(sql);
		setAttr("data",goods);
		renderFreeMarker("/html/car/8-youqi-shangjia.html");
	}
	//保险 根据油气商品ID 查商品
	public void getYouQiSById_web(){
		String sql = "select * from t_gogo where id = '"+getPara("id")+"'";
		Gogo goods = Gogo.dao.findFirst(sql);
		setAttr("data",goods);
		renderFreeMarker("/html/car/13-youqi-xi.html");
	}
	//保险 根据金融商品ID 查金融商品
	public void getJinRongSById_web(){
		String sql = "select * from t_gogo where id = '"+getPara("id")+"'";
		Gogo goods = Gogo.dao.findFirst(sql);
		setAttr("data",goods);
		renderFreeMarker("/html/car/13-jinrong-xi.html");
	}
	//保险 根据油气商品ID 查商品
	public void getXiuLiSById_web(){
		String sql = "select * from t_gogo where id = '"+getPara("id")+"'";
		Gogo goods = Gogo.dao.findFirst(sql);
		setAttr("data",goods);
		renderFreeMarker("/html/car/6-gonggu-shangjia-chanpin.html");
	}
	//保险 根据金融商家ID 查商家
	public void getJinRongById_web(){
		String sql = "select * from t_service where id = '"+getPara("id")+"'";
		Service goods = Service.dao.findFirst(sql);
		setAttr("data",goods);
		renderFreeMarker("/html/car/12-jinrong-shangjia.html");
	}
	//保险 根据商家ID 查商家店铺
	public void getDianPById_web(){
		String sql = "select * from t_store where storeid = '"+getPara("id")+"'";
		Store goods = Store.dao.findFirst(sql);
		setAttr("data",goods);
		renderFreeMarker("/html/car/1-sjdianpu.html");
	}
}
