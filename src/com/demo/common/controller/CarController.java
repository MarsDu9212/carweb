package com.demo.common.controller;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.shiro.crypto.hash.Sha256Hash;

import com.demo.common.model.Address;
import com.demo.common.model.Cargo;
import com.demo.common.model.Cars;
import com.demo.common.model.Carsell;
import com.demo.common.model.Cart;
import com.demo.common.model.Company;
import com.demo.common.model.Device;
import com.demo.common.model.Gcomment;
import com.demo.common.model.Gger;
import com.demo.common.model.Gogo;
import com.demo.common.model.Goods;
import com.demo.common.model.Mycars;
import com.demo.common.model.Order;
import com.demo.common.model.Post;
import com.demo.common.model.Postcomment;
import com.demo.common.model.Service;
import com.demo.common.model.Store;
import com.demo.common.model.Szdetail;
import com.demo.common.model.Trade_car;
import com.demo.common.model.Trade_cargo;
import com.demo.common.model.Types;
import com.demo.common.model.User;
import com.demo.common.model.Version;
import com.demo.common.model.Wares;
import com.demo.common.model.Yuyue;
import com.demo.utils.ABCBankAddr;
import com.demo.utils.ApiUtils;
import com.demo.utils.BankCardBin;
import com.demo.utils.CheckBankCard;
import com.demo.utils.JPush;
import com.demo.utils.SendSMS;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

public class CarController extends Controller {
	

	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static String crttime = sdf.format(new Date());

	public void index() {
	}

	/**
	 * --------------------------
	 * 用户模块
	 * @author 杜腾飞
	 * 		2016年5月23日 10:26:39
	 * --------------------------
	 */
	//上传发布APP
	public void uploadAPP(){
		Db.update("delete from t_version");
		Version version = getModel(Version.class);
		version.setVersion(getPara("version"));
		version.setIospath(getPara("iospath"));
		version.setAndroidpath(getPara("androidpath"));
		version.setContent(getPara("content"));
		version.setUpdatetime(crttime);
		version.save();
		forwardAction("/car/downloadAPP");
	}
	//下载APP
	public void downloadAPP(){
		Record rec = Db.findFirst("select * from t_version");
		setAttr("data", rec);
		renderFreeMarker("/html/download.html");
	}
	//检测设备是否安装APP
	public void appInstalled(){
		Record rec = Db.findFirst("select id from t_device where did='"+getPara("did")+"' ");
		if(rec == null){
			Device dev = getModel(Device.class);
			dev.setId(UUID.randomUUID().toString().replace("-", ""));
			dev.setDid(getPara("did"));
			dev.setDname(getPara("dname"));
			dev.setModel(getPara("model"));
			dev.setType(getPara("type"));
			dev.setVersion(getPara("version"));
			dev.setCrttime(crttime);
			dev.save();
		}else{
			Device dev = getModel(Device.class);
			dev.setId(rec.getStr("id"));
			dev.setDid(getPara("did"));
			dev.setDname(getPara("dname"));
			dev.setModel(getPara("model"));
			dev.setType(getPara("type"));
			dev.setVersion(getPara("version"));
			dev.setCrttime(crttime);
			dev.update();
		}
		setAttr("data", rec);
		renderFreeMarker("/html/download.html");
	}
	//发送验证码
	public void smsCode(){
		String code = SendSMS.sendRegCode(getPara("phone"), getPara("type"));
		setAttr("code", code);
		setAttr("phone", getPara("phone"));
		renderJson();
	}
	//发送验证码
	public void sendPush(){
		Map<String, String> map = new HashMap<String, String>();
		map.put("pageUrl", getPara("pageUrl"));
		Object result = JPush.testSendPush(getPara("alias"), getPara("msgContent"), map);
		setAttr("result", result);
		System.out.println(" [ sendPush ]"+result);
		renderJson();
	}
	//小工具API接口
	public void apiUtils(){
//		Object result = ApiUtils.request("http://apis.baidu.com/apistore/mobilenumber/mobilenumber", getPara("phone"));
		String result = "";
		if("gsd".equals(getPara("method"))){
			result = ApiUtils.getPhoneGSD(getPara("phone"));
		}
		if("shenfen".equals(getPara("method"))){
			result = ApiUtils.getPhoneSFZ(getPara("id"));
		}
		if("gupiao".equals(getPara("method"))){
			result = ApiUtils.getGuPiao(getPara("id"));
		}
		if("lottery".equals(getPara("method"))){
			result = ApiUtils.getDLT(getPara("lotteryType"));
		}
		if("you".equals(getPara("method"))){
			result = ApiUtils.getYouJG(getPara("you"));
		}
		if("xingz".equals(getPara("method"))){
			result = ApiUtils.getYunS(getPara("xingz"));
		}
//		if("xingz".equals(getPara("method"))){
//			
//			result = JuheDemo.getYunS(getPara("xingz"));
//		}
		if("meng".equals(getPara("method"))){
			result = ApiUtils.getMeng(getPara("meng"));
		}
		if("nao".equals(getPara("method"))){
			result = ApiUtils.getNaoJ(getPara("nao"));
		}
		if("ming".equals(getPara("method"))){
			result = ApiUtils.getMingY(getPara("ming"));
		}
		if("zucai".equals(getPara("method"))){
			result = ApiUtils.getZuCai(getPara("zucai"));
		}
		setAttr("data", result);
		renderJson();
	}
	//注册
	public void register(){
		User user = getModel(User.class);
		String id = UUID.randomUUID().toString().replace("-", "");
		String type = getPara("type"), ifpfct = "1";
		if("1".equals(type)){
			ifpfct = "0";
		}
		user.setUserid(id);
		user.setUsername("user_"+id.substring(0, 8));
		user.setUtype(type);
		user.setPhone(getPara("phone"));
		user.setPassword(getPara("password"));
		user.setIfpfct(ifpfct);
		user.setMoney("0");
		user.setCrttime(crttime);
		boolean success = user.save();
		setAttr("success", success);
		setAttr("id", id);
		renderJson();
	}
	//完善资料
	public void wanshanziliao(){
		Company com = getModel(Company.class);
		com.setCid(UUID.randomUUID().toString().replace("-", ""));
		com.setCname(getPara("cname"));
		com.setCtype(getPara("ctype"));
		com.setCimg(getPara("license"));
		com.setIdentity(getPara("card"));
		com.setCinfo(getPara("cinfo"));
		com.setCstatus("0");
		com.setUid(getPara("uid"));
		boolean success = com.save();
		Db.update("update t_user set ifpfct='1' where userid='"+getPara("uid")+"' ");
		setAttr("success", success);
		renderJson();
	}
	//注册时检验手机号是否已注册
	public void checkPhone(){
		String sql = "select count(userid) as unum from t_user where phone='"+getPara("phone")+"' ";
		User user = getModel(User.class);
		user = user.findFirst(sql);
		boolean exist = false;
		if("0".equals(user.get("unum")+"")){
			
		}else{
			exist = true;
		}
		setAttr("exist", exist);
		renderJson();
	}
	//登录检验用户名/手机号是否存在
	public void checkNamePhone(){
		String sql = "select count(userid) as unum from t_user where phone='"+getPara("namePhone")+"' or username='"+getPara("namePhone")+"' ";
		User user = getModel(User.class);
		user = user.findFirst(sql);
		boolean exist = false;
		if("0".equals(user.get("unum")+"")){
			
		}else{
			exist = true;
		}
		setAttr("exist", exist);
		renderJson();
	}
	//登录
	public void login(){
		String sql = "select * from t_user where phone='"+getPara("phone")+"' or username='"+getPara("phone")+"' ";
		User user = getModel(User.class);
		user = user.findFirst(sql);
		if(user == null){
			setAttr("exist", false);
			setAttr("msg", "用户名不存在");
		}else{
			if(new Sha256Hash(getPara("password")).toHex().equals(user.getPassword())){
				setAttr("exist", true);
				setAttr("user", user);
				setSessionAttr("user", user);
			}else{
				setAttr("exist", false);
				setAttr("msg", "密码错误");
			}
		}
		renderJson();
	}
	public void updatePwd(){
		String sql = "update t_user set password='"+getPara("pwd")+"' where phone='"+getPara("phone")+"' ";
		Db.update(sql);
		renderJson();
	}
	//修改用户信息
	public void editUserInfo(){
		String sql = "update t_user set "+getPara("field")+"='"+getPara("val")+"' where userid='"+getPara("userid")+"' ";
		Db.update(sql);
		User user = getModel(User.class);
		user = user.findById(getPara("userid"));
		setAttr("user", user);
		renderJson();
	}
	//获取商家会员信誉等级信息
	public void getSUlv(){
		Record rec = Db.findFirst("select u.*,s.slv,s.goodcom,s.badcom from t_user u,t_store s where u.userid=s.uid and u.userid='"+getPara("uid")+"'");
		setAttr("data", rec);
		System.out.println(rec);
		renderJson();
	}
	//查看个人会员等级
	public void getSUlv2(){
		Record rec = Db.findFirst("select *from t_user where userid='"+getPara("uid")+"'");
		setAttr("data", rec);
		renderJson();
	}
	//查看个人会员好评差评
	public void getPingL(){
		String sql = "select count(t.cid) as Tnum from t_gcomment t left join t_user g on t.uid=g.userid where g.userid='"+getPara("uid")+"' and t.oid is null and t.ctype='1' ";
		String sql1 = "select count(t.cid) as Tnum from t_gcomment t left join t_user g on t.uid=g.userid where g.userid='"+getPara("uid")+"' and t.oid is null and t.ctype='0' ";
		String sql3 = "select count(t.cid) as Tnum from t_gcomment t left join t_user g on t.uid=g.userid where g.userid='"+getPara("uid")+"' and t.oid is null and t.ctype='2' ";
		Gcomment list = Gcomment.dao.findFirst(sql);
		Gcomment list2 = Gcomment.dao.findFirst(sql1);
		Gcomment list3 = Gcomment.dao.findFirst(sql3);
		setAttr("data1",list);
		setAttr("data2",list2);
		setAttr("data3",list3);
		System.out.println("-----------------");
		renderJson();
	}
	//获取企业人会员等级信息
	public void getAUlvs(){
		Record rec = Db.findFirst("select *from t_user where userid='"+getPara("uid")+"'");
		setAttr("data",rec);
		renderJson();
	}
	//查看企业会员好评差评
	public void getPingJL(){
		String sql = "select count(t.cid) as Tnum from t_gcomment t left join t_user g on t.uid=g.userid where g.userid='"+getPara("uid")+"' and t.oid is not null and t.ctype='1' ";
		String sql1 = "select count(t.cid) as Tnum from t_gcomment t left join t_user g on t.uid=g.userid where g.userid='"+getPara("uid")+"' and t.oid is not null and t.ctype='0' ";
		String sql3 = "select count(t.cid) as Tnum from t_gcomment t left join t_user g on t.uid=g.userid where g.userid='"+getPara("uid")+"' and t.oid is null and t.ctype='2' ";
		Gcomment list = Gcomment.dao.findFirst(sql);
		Gcomment list2 = Gcomment.dao.findFirst(sql1);
		Gcomment list3 = Gcomment.dao.findFirst(sql3);
		setAttr("data1",list);
		setAttr("data2",list2);
		setAttr("data3",list3);
		renderJson();
	}
	//根据ID查询用户信息
	public void getUserById(){
		User user = getModel(User.class);
		user = user.findById(getPara("userid"));
		setAttr("user", user);
		renderJson();
	}
	//查询收货地址
	public void getAddrs(){
		List<Record> addrs = Db.find("select * from t_address where uid='"+getPara("userid")+"'");
		setAttr("list", addrs);
		renderJson();
	}
	//根据ID查询收货地址
	public void getAddr(){
		Address addr = getModel(Address.class);
		addr = addr.findById(getPara("id"));
		setAttr("data", addr);
		renderJson();
	}
	//查询默认收货地址
	public void getDefAddr(){
		Address addr = getModel(Address.class);
		String sql = "select * from t_address where uid='"+getPara("uid")+"' ";
		List<Address> list = addr.find(sql);
		if(list.size() > 0){
			sql = "select * from t_address where uid='"+getPara("uid")+"' and isdef='1' ";
			addr = addr.findFirst(sql);
			if(addr == null){
				addr = list.get(0);
			}
			setAttr("flag", "1");
			setAttr("data", addr);
		}else{
			setAttr("flag", "0");
			setAttr("data", addr);
		}
		renderJson();
	}
	//新增收货地址
	public void editAddress(){
		Address addr = getModel(Address.class);
		String id = getPara("id");
		boolean success = false;
		if(id == null || "".equals(id)){
			addr.setId(UUID.randomUUID().toString().replace("-", ""));
			addr.setSex(getPara("sex"));
			addr.setUname(getPara("username"));
			addr.setPhone(getPara("phone"));
			addr.setAddress(getPara("address"));
			addr.setUid(getPara("uid"));
			success = addr.save();
		}else{
			addr.setId(id);
			addr.setSex(getPara("sex"));
			addr.setUname(getPara("username"));
			addr.setPhone(getPara("phone"));
			addr.setAddress(getPara("address"));
			addr.setUid(getPara("uid"));
			success = addr.update();
		}
		setAttr("success", success);
		renderJson();
	}
	//删除收货地址
	public void delAddr(){
		Address addr = getModel(Address.class);
		boolean success = addr.deleteById(getPara("id"));
		setAttr("success", success);
		renderJson();
	}
	//判断地址是否在配送范围
	public void checkAddr(){
		boolean exist = false;
		String sql = "select city from t_area";
		List<Record> list = Db.find(sql);
		for (int i = 0; i < list.size(); i++) {
			String city = list.get(i).getStr("city").toString();
			if(getPara("city").equals(city) || getPara("city").indexOf(city) > -1 || getPara("city").indexOf(city.replace("市", "").replace("区", "")) > -1){
				exist = true;
				setAttr("success", exist);
				renderJson();
			}
		}
		setAttr("success", exist);
		renderJson();
	}
	//查询我的货车
	public void getMyCars(){
		List<Record> cars = Db.find("select * from t_mycars where uid='"+getPara("userid")+"'");
		for (int i = 0; i < cars.size(); i++) {
			Record rec = cars.get(i);
			String carmodel = rec.getStr("carmodel"),
					load = rec.getStr("load");
			rec.set("carmodel", Db.findFirst("select tname from t_types where id='"+carmodel+"' ").get("tname"));
			rec.set("load", Db.findFirst("select tname from t_types where id='"+load+"' ").get("tname"));
		}
		setAttr("list", cars);
		renderJson();
	}
	//查询我的车牌照
	public void getCheP(){
		String sql = "select id,platenum from t_mycars where uid = '"+getPara("uid")+"'";
		List <Mycars> cars =Mycars.dao.find(sql);
		setAttr("list",cars);
		renderJson();
	}
	//根据ID查询收货地址
	public void getMyCarsById(){
		String sql = "select t.*,f.tname from t_mycars t left join t_types f on t.carmodel=f.id where t.id='"+getPara("id")+"'";
		Mycars cars = Mycars.dao.findFirst(sql);
		setAttr("data", cars);
		renderJson();
	}
	//删除收货地址
	public void delCar(){
		Mycars cars = getModel(Mycars.class);
		boolean success = cars.deleteById(getPara("id"));
		setAttr("success", success);
		renderJson();
	}
	//编辑我的货车
	public void editCar(){
		Mycars cars = getModel(Mycars.class);
		String id = getPara("id");
		boolean success = false;
		if(id == null || "".equals(id)){
			cars.setId(UUID.randomUUID().toString().replace("-", ""));
			cars.setUid(getPara("uid"));
			cars.setUname(getPara("username"));
			cars.setCphone(getPara("phone"));
			cars.setCarmodel(getPara("chexing"));
			cars.setLoad(getPara("dunwei"));
			cars.setPlatenum(getPara("chepaihao"));
			cars.setImgs(getPara("imgs"));
			cars.setCrttime(crttime);
			success = cars.save();
		}else{
			cars.setId(id);
			cars.setUid(getPara("uid"));
			cars.setUname(getPara("username"));
			cars.setCphone(getPara("phone"));
			cars.setCarmodel(getPara("chexing"));
			cars.setLoad(getPara("dunwei"));
			cars.setPlatenum(getPara("chepaihao"));
			cars.setImgs(getPara("imgs"));
			cars.setCrttime(crttime);
			success = cars.update();
		}
		setAttr("success", success);
		renderJson();
	}
	

	/**
	 * --------------------------------------
	 * 商城模块
	 * @author 杜腾飞
	 * 		2016年5月25日 15:31:25
	 * --------------------------------------
	 */
	//搜索商品
	public void searchGoods(){
		int pageNum = getParaToInt("pageNum", 1);
		int pageSize = getParaToInt("pageSize", 10);
		Page<Goods> goods = Goods.dao.searchGoodsPage(pageNum, pageSize, getPara("content"), getPara("orderCond"));
		Page<Gogo> goods1 = Gogo.dao.searchGoodsPage1(pageNum, pageSize, getPara("content"), getPara("orderCond"));
		Page<Carsell> goods2 = Carsell.dao.searchGoodsPage2(pageNum, pageSize, getPara("content"), getPara("orderCond"));
		setAttr("list", goods.getList());
		setAttr("list1",goods1.getList());
		setAttr("list2",goods2.getList());
		renderJson();
	}
	//找轮胎
	public void geGoods_LT(){
		int pageNumber = getParaToInt("pageNumber", Integer.parseInt(getPara("pageNumber")));
		int pageSize = getParaToInt("pageSizes", Integer.parseInt(getPara("pageSize")));
		Page<Record> goods = Goods.dao.getGoodsLTpage(pageNumber, pageSize, getPara("type"), getPara("content"), getPara("orderCond"), getPara("brand"));
		List<Record> list = goods.getList();
		for (int i = 0; i < list.size(); i++) {
			Record rec = list.get(i);
			String sql = "select count(g.cid) as num from  t_gcomment g left join t_order o on g.oid=o.id left join t_goods t on o.goodid=t.goodid left join  t_user u on g.uid=u.userid "
				 + "where g.uid=u.userid and g.oid=o.id and g.goodid  = '"+rec.getStr("goodid")+"' and g.oid is not null";
			System.out.println("-------------------"+Db.findFirst(sql).getLong("num")+"");
			rec.set("goodcom", Db.findFirst(sql).getLong("num")+"");
		}
		int nono = goods.getTotalRow();
		setAttr("data1", nono);
		setAttr("pageNumber", goods.getPageNumber());
		setAttr("data", list);
		renderJson();
	}
	//找轮胎商店
	public void getStore_LT(){
		int pageNumber = getParaToInt("pageNumber", Integer.parseInt(getPara("pageNumber")));
		int pageSize = getParaToInt("pageSizes", Integer.parseInt(getPara("pageSize")));
		Page<Store> goods = Store.dao.getStoreLTpage(pageNumber, pageSize, getPara("content"));
		int nono = goods.getTotalRow();
		setAttr("data1",nono);
		setAttr("pageNumber",goods.getPageNumber());
		setAttr("data",goods.getList());
		System.out.println(goods);
		renderJson();
	}
	//找配件商店
	public void getStore_PJ(){
		int pageNumber = getParaToInt("pageNumber", Integer.parseInt(getPara("pageNumber")));
		int pageSize = getParaToInt("pageSizes", Integer.parseInt(getPara("pageSize")));
		Page<Store> goods = Store.dao.getStorePJpage(pageNumber, pageSize, getPara("content"));
		int nono = goods.getTotalRow();
		setAttr("data1",nono);
		setAttr("pageNumber",goods.getPageNumber());
		setAttr("data",goods.getList());
		System.out.println(goods);
		renderJson();
	}
	//找配件商店
	public void getStore_JY(){
		int pageNumber = getParaToInt("pageNumber", Integer.parseInt(getPara("pageNumber")));
		int pageSize = getParaToInt("pageSizes", Integer.parseInt(getPara("pageSize")));
		Page<Store> goods = Store.dao.getStoreJYpage(pageNumber, pageSize, getPara("content"));
		int nono = goods.getTotalRow();
		setAttr("data1",nono);
		setAttr("pageNumber",goods.getPageNumber());
		setAttr("data",goods.getList());
		System.out.println(goods);
		renderJson();
	}
	//找全部商家
	public void getStore_All(){
		int pageNum = getParaToInt("pageNum", 1);
		int pageSize = getParaToInt("pageSize", 10);
		Page<Store> goods = Store.dao.getStoreAllpage(pageNum, pageSize, getPara("content"));
		Page<Service> goods1 =Service.dao.getStoreAllpage1(pageNum, pageSize, getPara("content"));
		Page<Wares> goods2 =Wares.dao.getStoreAllpage2(pageNum, pageSize, getPara("content"));
		
		setAttr("data",goods.getList());
		setAttr("data1",goods1.getList());
		setAttr("data2",goods2.getList());
		renderJson();
	}
	//找汽贸 新车
	public void getqimao_SPALL(){
		int pageNumber = getParaToInt("pageNumber", Integer.parseInt(getPara("pageNumber")));
		int pageSize = getParaToInt("pageSizes", Integer.parseInt(getPara("pageSize")));
		Page<Carsell> goods = Carsell.dao.getStoreAllpage(pageNumber, pageSize, getPara("conmentQM"),getPara("orderCondQM"),getPara("brand"));
		int nono = goods.getTotalRow();
		setAttr("data1",nono);
		setAttr("pageNumber",goods.getPageNumber());
		setAttr("data",goods.getList());
		System.out.println(goods);
		renderJson();
	}
	//找汽贸 新车
	public void getjiuche_SPALL(){
		int pageNumber = getParaToInt("pageNumber", Integer.parseInt(getPara("pageNumber")));
		int pageSize = getParaToInt("pageSizes", Integer.parseInt(getPara("pageSize")));
		Page<Carsell> goods = Carsell.dao.getjiucheAllpage(pageNumber, pageSize, getPara("conmentES"),getPara("orderCondES"),getPara("brand"));
		int nono = goods.getTotalRow();
		setAttr("data1",nono);
		setAttr("pageNumber",goods.getPageNumber());
		setAttr("data",goods.getList());
		renderJson();
	}
	
	
	/**
	 * 找机油getershou_SP
	 */
	public void geGoods_JY(){
		int pageNumber = getParaToInt("pageNumber", Integer.parseInt(getPara("pageNumber")));
		int pageSize = getParaToInt("pageSizes", Integer.parseInt(getPara("pageSize")));
		Page<Goods> goods = Goods.dao.getGoodsJYpage(pageNumber, pageSize, getPara("typeJY"), getPara("conmentJY"), getPara("orderCondJY"), getPara("brand"));
		int nono = goods.getTotalRow();
		setAttr("data1",nono);
		setAttr("pageNumber",goods.getPageNumber());
		setAttr("data", goods.getList());
		renderJson();
	}
	/**
	 * 找配件
	 */
	public void geGoods_PJ(){
		int pageNumber = getParaToInt("pageNumber", Integer.parseInt(getPara("pageNumber")));
		int pageSize = getParaToInt("pageSizes", Integer.parseInt(getPara("pageSize")));
		Page<Goods> goods = Goods.dao.getGoodsPJpage(pageNumber, pageSize, getPara("typePJ"), getPara("contentPJ"), getPara("orderCondPJ"), getPara("brand"));
		int nono = goods.getTotalRow();
		setAttr("data1",nono);
		setAttr("pageNumber",goods.getPageNumber());
		setAttr("data", goods.getList());
		renderJson();
	}
    /**
     * 分页查询智能 修理厂 修理技师
     */
	public void getXLJS(){
		int pageNumber = getParaToInt("pageNumber", Integer.parseInt(getPara("pageNumber")));
		int pageSize = getParaToInt("pageSizes", Integer.parseInt(getPara("pageSize")));
		
		if("XL".equals(getPara("t"))){
			String sql = "select * from t_service where type='xl' order by "+getPara("orderCond")+" ";
			Page<Service> cars = Service.dao.paginate(pageNumber, pageSize, sql);
			int nono = cars.getTotalRow();
			setAttr("data",nono);
			setAttr("pageNumber",cars.getPageNumber());
			setAttr("list", cars.getList());
		}else{
			String sql = "select g.*,s.zytype,s.address from t_gger g,t_service s where g.serviceid=s.id and g.type='js' order by "+getPara("orderCond")+" ";
			Page<Service> cargo = Service.dao.paginate(pageNumber, pageSize, sql);
			int nono = cargo.getTotalRow();
			setAttr("data",nono);
			setAttr("pageNumber",cargo.getPageNumber());
			setAttr("list", cargo.getList());
		}
		renderJson();
	}
	/**
	 * 分页查询加油站 加气站
	 */
	public void getJYJQ(){
		int pageNumber = getParaToInt("pageNumber", Integer.parseInt(getPara("pageNumber")));
		int pageSize = getParaToInt("pageSizes", Integer.parseInt(getPara("pageSize")));
		
		if("JY".equals(getPara("t"))){
			String sql = "select * from t_service where type='jy' order by "+getPara("orderCond")+" ";
			Page<Service> cars = Service.dao.paginate(pageNumber, pageSize, sql);
			int nono  = cars.getTotalRow();
			setAttr("data1",nono);
			setAttr("pageNumber",cars.getPageNumber());
			setAttr("list", cars.getList());
		}else{
			String sql = "select * from t_service where type='jq' order by "+getPara("orderCond")+" ";
			Page<Service> cargo = Service.dao.paginate(pageNumber, pageSize, sql);
			int nono  = cargo.getTotalRow();
			setAttr("data1",nono);
			setAttr("pageNumber",cargo.getPageNumber());
			setAttr("list", cargo.getList());
		}
		renderJson();
	}
	//查询所有修理厂、加油加气站 以便在地图上显示
	public void getAllXLJYJQ(){
		List<Record> list = Db.find("select s.id,s.sname as title,s.desc as subTitle,s.coord from t_service s where type in ("+getPara("type")+") ");
		setAttr("list", list);
		renderJson();
	}
	/**
	 * 分页查询--信贷产品--金融理财
	 */
	public void getXDJR(){
		int pageNumber = getParaToInt("pageNumber", Integer.parseInt(getPara("pageNumber")));
		int pageSize = getParaToInt("pageSizes", Integer.parseInt(getPara("pageSize")));
		
		if("XD".equals(getPara("t"))){
			String sql = "select * from t_service where type='xd' order by "+getPara("orderCond")+" ";
			Page<Service> cars = Service.dao.paginate(pageNumber, pageSize, sql);
			int nono = cars.getTotalRow();
			setAttr("data1",nono);
			setAttr("pageNumber",cars.getPageNumber());
			setAttr("list", cars.getList());
		}else{
			String sql = "select * from t_service where type='jr' order by "+getPara("orderCond")+" ";
			Page<Service> cargo = Service.dao.paginate(pageNumber, pageSize, sql);
			int nono = cargo.getTotalRow();
			setAttr("data1",nono);
			setAttr("pageNumber",cargo.getPageNumber());
			setAttr("list", cargo.getList());
		}
		renderJson();
	}
	/**
	 * 查询-加油-加气-商家详情
	 */
	public void getServiceById_J(){
		String sql = "select t.*,f.points from t_service t left join t_user f on t.uid=f.userid where id = '"+getPara("id")+"' ";
		Service goods = Service.dao.findFirst(sql);
		setAttr("data",goods);
		renderJson();
	}
	/**
	 * 查询 -- 信贷 -- 金融 -- 详情
	 */
	public void getServiceById_XD(){
		String sql = "select t.*,f.points from t_service t left join t_user f on t.uid=f.userid where id = '"+getPara("id")+"' ";
		Service goods = Service.dao.findFirst(sql);
		setAttr("data",goods);
		renderJson();
	}
	/**
	 * 查询 -- 信贷 -- 理财 --产品 --1条
	 */
	public void getLCai(){
		String sql ="select * from t_gogo where storeid='"+getPara("id")+"' limit 1";
		List<Gogo> goods = Gogo.dao.find(sql);
		setAttr("data",goods);
		renderJson();
	}
	/**
	 * 查询 -- 信贷 -- 理财 --产品 --全部
	 */
	public void getLCaiAll(){
		String sql ="select * from t_gogo where storeid='"+getPara("id")+"' ";
		List<Gogo> goods = Gogo.dao.find(sql);
		setAttr("data",goods);
		System.out.println(goods);
		renderJson();
	}
	/**
	 * 分页查询信誉度查询
	 */
	public void getXJ(){
		int pageNum = getParaToInt("pageNum", 1);
		int pageSize = getParaToInt("pageSize", 10);
		
		if("XL".equals(getPara("t"))){
			Page<Service> cars = Service.dao.getService2Paginate(pageNum, pageSize);
			setAttr("list", cars.getList());
		}else{
			String sql = "select * from t_service";
			Page<Service> cargo = Service.dao.paginate(pageNum, pageSize, sql);
			setAttr("list", cargo.getList());
		}
		renderJson();
	}
	/**
	 * 查询公估
	 */
	public void getGG(){
		int pageNumber = getParaToInt("pageNumber", Integer.parseInt(getPara("pageNumber")));
		int pageSize = getParaToInt("pageSizes", Integer.parseInt(getPara("pageSize")));
		Page<Service> goods = Service.dao.getGGLTpage(pageNumber, pageSize);
		int nono = goods.getTotalRow();
		setAttr("data1",nono);
		setAttr("pageNumber",goods.getPageNumber());
		setAttr("data",goods.getList());
		renderJson();
	}
	/**
	 * 查公估---信誉度
	 */
	public void getXL(){
		int pageNumber = getParaToInt("pageNumber", Integer.parseInt(getPara("pageNumber")));
		int pageSize = getParaToInt("pageSizes", Integer.parseInt(getPara("pageSize")));
		Page<Service> goods = Service.dao.getXLLTpage(pageNumber, pageSize);
		int nono = goods.getTotalRow();
		setAttr("data1",nono);
		setAttr("pageNumber",goods.getPageNumber());
		setAttr("data",goods.getList());
		renderJson();
	}
	public void getFirstGG(){
		String sql = "select  * from t_gger where serviceid='"+getPara("storeid")+"' and type = 'gg' limit 1";
		Gger goods = Gger.dao.findFirst(sql);
		setAttr("data",goods);
		renderJson();
	}
	public void getAllGG(){
		String sql = "select  * from t_gger where serviceid='"+getPara("storeid")+"' and type = 'gg' ";
		List<Gger> goods = Gger.dao.find(sql);
		setAttr("data",goods);
		renderJson();
	}
	public void getFirstJS(){
		String sql = "select  * from t_gger where serviceid='"+getPara("storeid")+"' and type = 'js' limit 1";
		Gger goods = Gger.dao.findFirst(sql);
		setAttr("data",goods);
		renderJson();
	}
	public void getAllJS(){
		String sql = "select  * from t_gger where serviceid='"+getPara("storeid")+"' and type = 'js' ";
		List<Gger> goods = Gger.dao.find(sql);
		setAttr("data",goods);
		renderJson();
	}
	// 查公估--预约
	public void getGGYU(){
		String sql = "select g.type,g.id ,u.loss,u.id,u.price,u.img from t_service g,t_gogo u where g.id=u.storeid and g.id='"+getPara("storeid")+"' and g.type ='gg' limit 1";
		List<Service> goods = Service.dao.find(sql);
		setAttr("data",goods);
		renderJson();
	}
	//查公估--预约All
	public void getGGYUAll(){
		String sql = "select g.type,g.id ,u.loss,u.id,u.price,u.img from t_service g,t_gogo u where g.id=u.storeid and g.id='"+getPara("storeid")+"' and g.type ='gg'";
		List<Service> goods = Service.dao.find(sql);
		setAttr("data",goods);
		System.out.println(goods);
		renderJson();
	}
	/**
	 * 查询一条 -- 保险信息
	 */
	public void getBXYT(){
		String sql = "select u.img,u.id, u.loss,u.price,u.oldprice,u.storeid from t_service g,t_gogo u where g.id=u.storeid and g.id='"+getPara("storeid")+"' and g.type='dm' limit 1";
		List<Gogo> goods = Gogo.dao.find(sql);
		setAttr("data",goods);
		renderJson();
	}
	/**
	 * 查询 -- 多条 -- 保险信息
	 */
	public void getBXZL(){
		String sql = "select u.img,u.id, u.loss,u.price,u.oldprice,u.storeid from t_service g,t_gogo u where g.id=u.storeid and g.id='"+getPara("storeid")+"' and g.type='dm' ";
		List<Gogo> goods = Gogo.dao.find(sql);
		setAttr("data",goods);
		renderJson();
	}
	/**
	 * 查 -- 一条 -- 油气优惠 
	 */
	public void getBXYT_J(){
		String sql = "select u.img,u.id, u.loss,u.price,u.oldprice,u.storeid,g.type from t_service g,t_gogo u where g.id=u.storeid and g.id='"+getPara("storeid")+"' limit 1";
		List<Gogo> goods = Gogo.dao.find(sql);
		setAttr("data",goods);
		System.out.println(goods);
		renderJson();
	}
	/**
	 * 查询 --  多条 -- 油气优惠
	 */
	public void getBXZL_J(){
		String sql = "select u.img,u.id, u.loss,u.price,u.oldprice,u.storeid,g.type from t_service g,t_gogo u where g.id=u.storeid and g.id='"+getPara("storeid")+"' ";
		List<Gogo> goods = Gogo.dao.find(sql);
		setAttr("data",goods);
		System.out.println(goods);
		renderJson();
	}
	/**
	 * 根据ID查公估 - 保险-信息
	 */
	public void getServiceById(){
		String sql = "select t.*,f.points from t_service t left join t_user f on t.uid=f.userid where id='"+getPara("id")+"' ";
		Service goods = Service.dao.findFirst(sql);
		setAttr("data",goods);
		renderJson();
		
	}
	/**
	 * 查修理报价
	 */
	public void getBaoJ(){
		String sql ="select * from t_gogo where storeid ='"+getPara("id")+"'limit 1";
		List<Gogo> goods = Gogo.dao.find(sql);
		setAttr("data",goods);
		renderJson();
	}
	/**
	 * 查看其它修理报价
	 */
	public void getOtherBao(){
		String sql = "select * from t_gogo where storeid ='"+getPara("id")+"'";
		List<Gogo> goods = Gogo.dao.find(sql);
		setAttr("data",goods);
		renderJson();
	}
	/**
	 * 查保险--智能
	 */
	public void getBXZ(){
		int pageNumber = getParaToInt("pageNumber", Integer.parseInt(getPara("pageNumber")));
		int pageSize = getParaToInt("pageSizes", Integer.parseInt(getPara("pageSize")));
		Page<Service> goods = Service.dao.getBXZpage( pageNumber, pageSize);
		int nono = goods.getTotalRow();
		setAttr("data1",nono);
		setAttr("pageNumber",goods.getPageNumber());
		setAttr("data",goods.getList());
		renderJson();
	}
	/**
	 * 查保险--信誉度
	 */
	public void getBXX(){
		int pageNumber = getParaToInt("pageNumber", Integer.parseInt(getPara("pageNumber")));
		int pageSize = getParaToInt("pageSizes", Integer.parseInt(getPara("pageSize")));
		Page<Service> goods = Service.dao.getBXXpage(pageNumber,pageSize);
		int nono = goods.getTotalRow();
		setAttr("data1",nono);
		setAttr("pageNumber",goods.getPageNumber());
		setAttr("data",goods.getList());
		renderJson();
	}
	/**
	 * 查汽贸  
	 */
	public void getQM(){
		int pageNumber = getParaToInt("pageNumber", Integer.parseInt(getPara("pageNumber")));
		int pageSize = getParaToInt("pageSizes", Integer.parseInt(getPara("pageSize")));
		Page<Wares> goods = Wares.dao.getQMpage(pageNumber, pageSize, getPara("typeQM"), getPara("conmentQM"), getPara("orderCondQM"),getPara("brand"));
		int nono = goods.getTotalRow();
		setAttr("data1",nono);
		setAttr("pageNumber",goods.getPageNumber());
		setAttr("data", goods.getList());
		renderJson();
		
	}
	/**
	 * 查二手车
	 */
	public void getES(){
		int pageNumber = getParaToInt("pageNumber", Integer.parseInt(getPara("pageNumber")));
		int pageSize = getParaToInt("pageSizes", Integer.parseInt(getPara("pageSize")));
		Page<Wares> goods = Wares.dao.getESpage(pageNumber,pageSize,getPara("typeES"),getPara("conmentES"),getPara("orderCondES"),getPara("brand"));
		int nono = goods.getTotalRow();
		setAttr("data1",nono);
		setAttr("pageNumber",goods.getPageNumber());
		setAttr("data", goods.getList());
	    renderJson();
	}
    /**
     * 查服务
     */
	public void getFW(){
		int pageNumber = getParaToInt("pageNumber", Integer.parseInt(getPara("pageNumber")));
		int pageSize = getParaToInt("pageSizes", Integer.parseInt(getPara("pageSize")));
		Page<Wares> goods = Wares.dao.getFWpage(pageNumber,pageSize,getPara("typeFW"),getPara("commentFW"),getPara("orderCondFW"),getPara("orderCondFL"));
		int nono = goods.getTotalRow();
		setAttr("data1",nono);
		setAttr("pageNumber",goods.getPageNumber());
		setAttr("data",goods.getList());
		renderJson();
	}
	/**
	 * 根据ID查-服务
	 */
	public void getWareById(){
		String sql = "select t.*,f.points from t_wares t left join t_user f on t.uid=f.userid where id = '"+ getPara("id")+"' ";
		Wares goods = Wares.dao.findFirst(sql);
		setAttr("data",goods);
		renderJson();
	}

	/**
	 * 查询--汽贸商品--车详情---多个
	 */
	public void getqimao_SP(){
		String sql = "select * from t_carsell where id='"+ getPara("id")+"' limit 2 ";
		List<Carsell> goods = Carsell.dao.find(sql);
		setAttr("data", goods);
		renderJson();	
	}
	/**
	 * 查询 -- 汽贸商品 -- 多车 
	 */
	public void getqimaoAllSP(){
		String sql = "select * from t_carsell where id='"+ getPara("id")+"' ";
		List<Carsell> goods = Carsell.dao.find(sql);
		setAttr("data", goods);
		renderJson();	
	}
	/**
	 *  查询 -- 二手车 --车详情 --多个
	 */
	public void getershou_SP(){
		String sql ="select * from t_carsell where id='"+ getPara("id")+"' limit 2";
		List<Carsell> goods = Carsell.dao.find(sql);
		setAttr("data",goods);
		renderJson();
		
	}
	/**
	 * 查询全部 -- 二手车 -- 车详情
	 */
	public void getershouAllSP(){
		String sql ="select * from t_carsell where id='"+getPara("id")+"' ";
		List<Carsell> goods = Carsell.dao.find(sql);
		setAttr("data",goods);
		renderJson();
	}
	/**
	 * 查促销--
	 */
	 public void get_CX(){
		 String sql = "select * from t_goods where ifsale=1 ";
		 List<Goods> goods = Goods.dao.find(sql);
		 setAttr("data",goods);
		 renderJson();
		 
	 }
	 /**
	  * 查前三个促销
	  */
	 public void get_CX3(){
		 String sql = "select * from t_goods where ifsale=1 order by gsailnum desc limit 3  ";
		 List<Goods> goods = Goods.dao.find(sql);
		 setAttr("data",goods);
		 renderJson();
	 }
	 /**
	  * 查询剩下的信息
	  */
	 public void get_CXv(){
		 String sql = "select * from t_goods where ifsale=1 order by gsailnum desc ";
		 int pageNumber = getParaToInt("pageNumber", Integer.parseInt(getPara("pageNumber")));
		 int pageSize = getParaToInt("pageSizes", Integer.parseInt(getPara("pageSize")));
		 Page<Goods> goods = Goods.dao.paginate(pageNumber, pageSize, sql);
		 setAttr("data", goods.getList());
		 //查出有多少 条数据
		 setAttr("data2", goods.getTotalRow());
		 System.out.println(goods.getTotalRow());
		 setAttr("pageNumber", goods.getPageNumber());
		 setAttr("pageSize", goods.getPageSize());
		 renderJson();
	 }
	 /**
	  * 查新车 -- 详情-- 具体信息--单个
	  */
	 public void getXCPZ(){
		 String sql ="select * from t_carsell where carsellid = '"+getPara("carsellid")+"' ";
		 Carsell goods = Carsell.dao.findFirst(sql);
		 setAttr("data",goods);
		 renderJson();
	 }
	 /**
	  * 查询 gogo 表信息
	  */
	 public void getChanP(){
		 String sql = "select * from t_gogo where id = '"+getPara("id")+"'";
		 Gogo goods = Gogo.dao.findFirst(sql);
		 setAttr("data",goods);
		 renderJson();
	 }
	 //查询商家的商品 
	 public void getGoodsByStoreId(){
		 //热销单品
		 String sql = "select * from t_goods where storeid='"+getPara("storeid")+"' order by gsailnum desc limit 2 ";
		 //促销商品
		 String sql2 = "select * from t_goods where storeid='"+getPara("storeid")+"' and ifsale='1' order by gsailnum desc limit 2 ";
		 //新品推荐
		 String sql3 = "select * from t_goods where storeid='"+getPara("storeid")+"' order by gsailnum asc limit 2 ";
		 //全部商品
		 String sql4 = "select * from t_goods where storeid='"+getPara("storeid")+"' order by gsailnum desc ";
		 List<Goods> list = Goods.dao.find(sql);
		 List<Goods> list2 = Goods.dao.find(sql2);
		 List<Goods> list3 = Goods.dao.find(sql3);
		 List<Goods> list4 = Goods.dao.find(sql4);
		 setAttr("list", list);
		 setAttr("list2", list2);
		 setAttr("list3", list3);
		 setAttr("list4", list4);
		 renderJson();
	 }
	//热销单品
	 public void getRxiao(){
		 String sql = "select * from t_goods where storeid='"+getPara("storeid")+"' order by gsailnum desc ";
		 List<Goods> list = Goods.dao.find(sql);
		 setAttr("list", list);
		 renderJson();
	 }
	 //促销商品
	 public void getCxiao(){
		 String sql2 = "select * from t_goods where storeid='"+getPara("storeid")+"' and ifsale='1' order by gsailnum desc";
		 List<Goods> list2 = Goods.dao.find(sql2);
		 setAttr("list", list2);
		 renderJson();
	 }
	 //新品推荐
	 public void getTjian(){
		 String sql3 = "select * from t_goods where storeid='"+getPara("storeid")+"' order by gsailnum asc ";
		 List<Goods> list3 = Goods.dao.find(sql3);
		 setAttr("list", list3);
		 renderJson();
	 }
	 //查询商家下所有商品并排序
	 public void getSGoodsOrder(){
		 String sql = "select * from t_goods g where g.storeid='"+getPara("storeid")+"' and g.gbrand like '%"+getPara("leixing")+"%' order by " + getPara("orderCond");
		 List<Goods> list = Goods.dao.find(sql);
		 setAttr("list", list);
		 renderJson();
	 }
	//商品详情 根据ID查询商品
	public void getGoodsById(){
		String sql = "select * from t_goods where goodid='"+getPara("id")+"' ";
		Goods goods = Goods.dao.findFirst(sql);
		setAttr("data", goods);
		renderJson();
	}
	//商品详情页显示商家信息
	public void getStroeInfo(){
		String sql = "select s.storeid,s.sname,s.sphone,s.saddress,s.location,s.simgs,s.slv,s.sabout,f.points,count(g.goodid) as bbs,sum(g.gsailnum) as totalsn from t_goods g left join t_store s on g.storeid=s.storeid left join t_user f on s.uid=f.userid where g.storeid='"+getPara("storeid")+"' ";
		Record rec = Db.findFirst(sql);
		setAttr("data", rec);
		renderJson();
	}
	//获取商家等级 以及积分
	public void getFenShu(){
		String sql = "select slv from t_store where storeid='"+getPara("storeid")+"'";
		String sql1 =" select count(g.cid) as gcNum from t_gcomment g  left join t_goods t on g.goodid=t.goodid where t.goodid in  (select s.goodid from t_goods s where s.storeid = '"+getPara("storeid")+"') and g.ctype='1' and g.oid is not null "; 
		String sql2 =" select count(g.cid) as gcNum from t_gcomment g  left join t_goods t on g.goodid=t.goodid where t.goodid in  (select s.goodid from t_goods s where s.storeid = '"+getPara("storeid")+"') and g.ctype='0' and g.oid is not null ";
		Store goods = Store.dao.findFirst(sql);
		Gcomment goods1 = Gcomment.dao.findFirst(sql1);
		Gcomment goods2 = Gcomment.dao.findFirst(sql2);
		setAttr("data",goods);
		setAttr("data1",goods1);
		setAttr("data2",goods2);
		renderJson();
	}
	//添加到购物车
	public void addCart(){
		Cart cart = getModel(Cart.class);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String crttime = sdf.format(new Date());
		
		boolean success = false;
		Record record = Db.findFirst("select * from t_cart where sid='"+getPara("sid")+"' and goodid='"+getPara("goodid")+"' and gattr='"+getPara("gattr")+"' and uid='"+getPara("uid")+"' ");
		if(record == null){
			cart.setCartid(UUID.randomUUID().toString().replace("-", ""));
			cart.setSname(getPara("sname"));
			cart.setGname(getPara("gname"));
			cart.setGattr(getPara("gattr"));
			cart.setGimg(getPara("gimg"));
			cart.setPrice(getPara("price"));
			cart.setNum(getPara("num"));
			cart.setSid(getPara("sid"));
			cart.setGoodid(getPara("goodid"));
			cart.setUid(getPara("uid"));
			cart.setGtype(getPara("gtype"));
			cart.setCrttime(crttime);
			success = cart.save();
		}else{
			int r = Db.update("update t_cart set num=num+"+getPara("num")+" where sid='"+getPara("sid")+"' and goodid='"+getPara("goodid")+"' and gattr='"+getPara("gattr")+"' and uid='"+getPara("uid")+"' ");
			if(r > 0){
				success = true;
			}
		}
		
		setAttr("success", success);
		renderJson();
	}
	//查询购物车信息 可根据用户、编号
	public void getCarts(){
		String sql = "select * from t_cart where "+getPara("field")+" in ("+getPara("id")+") order by crttime desc ";
		List<Cart> carts = Cart.dao.find(sql);
		setAttr("list", carts);
		renderJson();
	}
	//删除购物车信息
	public void delCart(){
		setAttr("success", Db.update("delete from t_cart where cartid in ("+getPara("ids")+")"));
		renderJson();
	}
	//立即购买查询商品及商家信息
	public void getGoodsToOrder(){
		String sql = "select g.gname,g. from t_goods g,t_store s where g.storeid=s.storeid and "+getPara("field")+" ='"+getPara("id")+"' ";
		List<Cart> carts = Cart.dao.find(sql);
		setAttr("list", carts);
		renderJson();
	}
	//下订单
	public void setAnOrder(){
		boolean success = true;
		int totalMoney = 0, num = 0;
		float price = 0;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String crttime = sdf.format(new Date());
		
		Order order = getModel(Order.class);

		String params = getPara("order");
		String uid = getPara("uid"),
				addrid = getPara("addrid"),
				cartid = "",
				id = UUID.randomUUID().toString().replace("-", ""),
				str = params.replaceAll("\\{", "").replaceAll("\"", "").replaceAll("\\}", "");
		String[] ar = str.split(",");
		order.setId(id);
		order.setUid(uid);
		order.setAddrid(addrid);
		for (int j = 0; j < ar.length; j++) {
			String[] a = ar[j].split(":");
			if("sid".equals(a[0])){ order.setSid(a[1]);}
			if("goodid".equals(a[0])){ order.setGoodid(a[1]);}
			if("gname".equals(a[0])){ order.setGname(a[1]); }
			if("gattr".equals(a[0])){ order.setGattr(a[1]);}
			if("gtype".equals(a[0])){ order.setGtype(a[1]);}
			if("gimg".equals(a[0])){ order.setGimg("http:" + a[2]);}
			if("price".equals(a[0])){
				
				String as = a[1];
				float p = Float.parseFloat(as);
				DecimalFormat decimalFormat=new DecimalFormat(".00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
				String p2 = decimalFormat.format(p);//format 返回的是字符串
				
				order.setGprice(p2);
				price = p;
			}
			if("num".equals(a[0])){
				order.setGnum(a[1]);
				num = Integer.parseInt(a[1]);
			}
			if("cartid".equals(a[0])){ cartid = a[1];}
		}
		totalMoney += price * num;
		order.setCrttime(crttime);
		order.setStatus("0");
		success = order.save();
		
//		Db.update("delete from t_cart where cartid = '" + cartid + "'");
		
		setAttr("totalMoney", totalMoney);
		setAttr("cartid", "cartid");
		setAttr("ids", "'"+id+"'");
		setAttr("success", success);
		renderJson();
	}
	public void setOrders(){
		boolean success = true;
		int totalMoney = 0;
		String ids = "", cartid = "",gname = "";
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String crttime = sdf.format(new Date());
		
		String params = getPara("order");
		String uid = getPara("uid"), addrid = getPara("addrid");
		String[] arr = params.split("\\|");
		for (int i = 0; i < arr.length; i++) {
			String str = arr[i].replaceAll("\\{", "").replaceAll("\"", "").replaceAll("\\}", "");
			System.out.println(arr[i]);
			String[] ar = str.split(",");

			Order order = getModel(Order.class);
			String id = UUID.randomUUID().toString().replace("-", "");
			float price = 0;
			int num = 0;
			ids += "'" + id + "',";
			order.setId(id);
			order.setUid(uid);
			order.setAddrid(addrid);
			for (int j = 0; j < ar.length; j++) {
				String[] a = ar[j].split(":");
				if("sid".equals(a[0])){ order.setSid(a[1]);}
				if("goodid".equals(a[0])){ order.setGoodid(a[1]);}
				if("gname".equals(a[0])){ order.setGname(a[1]); }
				if("gattr".equals(a[0])){ order.setGattr(a[1]);}
				if("gtype".equals(a[0])){ order.setGtype(a[1]);}
				if("gimg".equals(a[0])){ order.setGimg("http:" + a[2]);}
				if("price".equals(a[0])){

					String as = a[1];
					float p = Float.parseFloat(as);
					DecimalFormat decimalFormat=new DecimalFormat(".00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
					String p2 = decimalFormat.format(p);//format 返回的是字符串
					
					order.setGprice(p2);
					price = p;
				}
				if("num".equals(a[0])){
					order.setGnum(a[1]);
					num = Integer.parseInt(a[1]);
				}
				if("cartid".equals(a[0])){ cartid += "'" + a[1] + "',";}
			}
			totalMoney += price * num;
			order.setCrttime(crttime);
			order.setStatus("0");
			success = order.save();
		}
		ids = ids.substring(0, ids.length()-1);
		cartid = cartid.substring(0, cartid.length()-1);
		
//		Db.update("delete from t_cart where cartid in ("+cartid+")");

		DecimalFormat decimalFormat=new DecimalFormat(".00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
		String finalMoney = decimalFormat.format(totalMoney);//format 返回的是字符串
		setAttr("totalMoney", finalMoney);
		setAttr("cartid", cartid);
		setAttr("ids", ids);
		setAttr("success", success);
		renderJson();
	}
	/**
	 *修理下单 
	 */
	public void setXiuLi(){
		boolean success = true;
		int totalMoney = 0;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String crttime = sdf.format(new Date());
		Order order = getModel(Order.class);
		String id = UUID.randomUUID().toString().replace("-", "");
		order.setId(id);
		order.setUid(getPara("uid"));
		order.setCrttime(crttime);
		order.setGprice(getPara("price1"));
		order.setGimg(getPara("img"));
		order.setGname(getPara("loss"));
		order.setGoodid(getPara("id"));
		order.setSid(getPara("storeid"));
		order.setGtype(getPara("gtype"));
		order.setGnum(getPara("gnum"));
		order.setPricetype(getPara("pricetype"));
		order.setStatus(getPara("status"));
		success = order.save();
		setAttr("totalMoney", totalMoney);
		setAttr("id", id);
		setAttr("success", success);
		renderJson();
	}
	/**
	 *油气下单 
	 */
	public void setYouqi(){
		boolean success = true;
		int totalMoney = 0;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String crttime = sdf.format(new Date());
		Order order = getModel(Order.class);
		String id = UUID.randomUUID().toString().replace("-", "");
		order.setId(id);
		order.setUid(getPara("uid"));
		order.setCrttime(crttime);
		order.setGname(getPara("uname"));
		order.setGprice(getPara("price1"));
		order.setGimg(getPara("img"));
		order.setGoodid(getPara("id"));
		order.setSid(getPara("storeid"));
		order.setGtype(getPara("gtype"));
		order.setGnum(getPara("gnum"));
		order.setPricetype(getPara("pricetype"));
		order.setStatus(getPara("status"));
		success = order.save();
		setAttr("totalMoney", totalMoney);
		setAttr("id", id);
		setAttr("success", success);
		renderJson();
	}
	/**
	 * 删除订单
	 */
	public void toDele(){
		Order order = getModel(Order.class);
		boolean success = order.deleteById(getPara("id"));
		setAttr("success", success);
		renderJson();
	}
	//修改支付状态--下单
	public void setPayOrderss(){
		String sql3 = "select goodid,gnum from t_order where id in ("+getPara("ids")+") ";
		List <Order> order = Order.dao.find(sql3);
			for(int i=0;i<order.size();i++){
				String updateSql = "update t_goods set gtotalnum = gtotalnum-"+(order.get(i).getStr("gnum"))+", gsailnum = gsailnum + "+(order.get(i).getStr("gnum"))+" where goodid='"+(order.get(i).getStr("goodid"))+"' ";
				Db.update(updateSql);
			}
		String pricetype = "1";
		if("wxpay".equals(getPara("payWay"))){
			pricetype = "2";
		}else if("alipay".equals(getPara("payWay"))){
			pricetype = "3";
		}else if("unionpay".equals(getPara("payWay"))){
			pricetype = "4";
		}
		String sql = "update t_order set status='1',pricetype='"+pricetype+"' where id in ("+getPara("ids")+") ";
		int success = Db.update(sql);
		Db.update("delete from t_cart where cartid = '" + getPara("delCartIDs") + "'");
		setAttr("success", success);
		renderJson();
	}
	//修改支付状态 -- 修理油气
	public void setPayXLYQ(){
		String sql ="update t_order set status='1',pricetype='1' where id in ("+getPara("ids")+") ";
		String sql2 = "update t_user set money = money+"+getPara("totalMoney")+"  where userid in (select g.uid from t_order t left join t_service g on t.sid=g.id where t.id in ("+getPara("ids")+") )";
		int success = Db.update(sql);
		int success2 = Db.update(sql2);
		setAttr("success2",success2);
		setAttr("success",success);
		renderJson();
	}
	//修改支付状态 -- 公估
	public void setPayGG(){
		String sql ="update t_yuyue set conmen='1',pricetype='1' where id in ("+getPara("ids")+") ";
		String sql2 = "update t_user set money = money+"+getPara("totalMoney")+"  where userid in (select g.uid from t_yuyue t left join t_service g on t.nstore=g.id where t.id in ("+getPara("ids")+") )";
		int success = Db.update(sql);
		int success2 = Db.update(sql2);
		setAttr("success2",success2);
		setAttr("success",success);
		renderJson();
	}
	//修改支付状态 -- 货物
	public void setPayHC(){
		String sql ="update t_trade_cargo set status='3',typemoney='1' where id in ("+getPara("ids")+") ";
		String sql2 = "update t_user set money = money+"+getPara("totalMoney")+"  where userid in (select uid from t_trade_cargo  where id in ("+getPara("ids")+") )";
		int success = Db.update(sql);
		int success2 = Db.update(sql2);
		setAttr("success2",success2);
		setAttr("success",success);
		renderJson();
	}
	public void refuseCar() {
		String flag = getPara("flag");
		String tid = getPara("tid");		
		Db.update("update t_trade_cargo set status='"+ flag +"' where tid='"+ tid +"'");		
		renderJson();
	}
	
	//修改支付状态 -- 预约
	public void setPayYuYue(){
		String sql = "update t_yuyue set conmen='1' where id in ("+getPara("ids")+") ";
		int success = Db.update(sql);
		setAttr("success", success);
		renderJson();
	}
	//修改支付状态以及支付金额 -- 现金 --货车预约
	public void get_XianJ(){
		String sql = "update t_trade_cargo set typemoney='0',status='3', rmoney='"+getPara("jine")+"',imgs='"+getPara("img")+"' where tid = '"+getPara("id")+"' ";
		int success = Db.update(sql);
		setAttr("success",success);
		renderJson();
	}
	//修改支付状态以及支付金额 -- 线上 --货车预约
	public void get_PingT(){
		String sql = "update t_trade_cargo set typemoney='1',status='2', rmoney='"+getPara("jine")+"',imgs='"+getPara("img")+"' where tid ='"+getPara("id")+"' ";
		int success = Db.update(sql);
		setAttr("success",success);
		renderJson();
	}
	//修改支付状态以及支付金额 -- 现金 --货物预约
	public void get_XianJHC(){
		String sql ="update t_trade_car set typemoney='0',status='3', rmoney='"+getPara("jine")+"',imgs='"+getPara("img")+"' where tid = '"+getPara("id")+"' ";
		int success = Db.update(sql);
		setAttr("success",success);
		renderJson();
	}
	//修改支付状态以及支付金额 -- 线上 --货车预约
	public void get_PingTHC(){
		String sql = "update t_trade_car set typemoney='1',status='2', rmoney='"+getPara("jine")+"',imgs='"+getPara("img")+"' where tid ='"+getPara("id")+"' ";
		int success = Db.update(sql);
		setAttr("success",success);
		renderJson();
	}
	//查询待确认订单数量
	public void getPerCenNum(){
		//订单
		String sql_HW = "select count(t.tid) as cgonum from t_trade_cargo t left join t_cargo g on t.cid=g.cgid left join t_user f on g.uid=f.userid where (userid ='"+getPara("uid")+"' or g.uid ='"+getPara("uid")+"')  and t.status in ('0','1','2') order by ttime desc";
		String sql_HC = "select count(t.tid) as carnum from t_trade_car t left join t_cars g on t.cid=g.cid left join t_user f on t.uid=f.userid where (userid ='"+getPara("uid")+"' or g.uid ='"+getPara("uid")+"') and t.status in ('0','1','2') order by ttime desc ";
		String sql = "select count(id) as num from t_order where uid='"+getPara("uid")+"' and status in ('0') order by crttime desc";
		int a = Integer.parseInt(Db.findFirst(sql_HW).getLong("cgonum").toString());
		int b = Integer.parseInt(Db.findFirst(sql_HC).getLong("carnum").toString());
		int c = Integer.parseInt(Db.findFirst(sql).getLong("num").toString());
		
		//物流
		String wl_HW = "select count(c.tid) as hwnum from t_trade_cargo c left join t_cargo g on c.cid=g.cgid where g.result='0' and g.uid='"+getPara("uid")+"' ";
		String wl_HC = "select count(c.tid) as hcnum from t_trade_car c left join t_cars g on c.cid=g.cid where g.result='0'  and g.uid='"+getPara("uid")+"' ";
		int d = Integer.parseInt(Db.findFirst(wl_HW).getLong("hwnum").toString());
		int e = Integer.parseInt(Db.findFirst(wl_HC).getLong("hcnum").toString());
		
		//帖子
		String sql_tz = "select count(p.id) as tznum from t_postcomment p left join t_post t on p.pid=t.pid left join t_user u on t.uid=u.userid"
				+ " where p.pid='"+getPara("uid")+"' and p.ifnew='0' and (p.replay is null or p.replay='') and (p.msgid is null or p.msgid='')";
		int f = Integer.parseInt(Db.findFirst(sql_tz).getLong("tznum").toString());
		
		//评价
		String sql_pj = "select count(g.cid) as pjnum from t_gcomment g,t_user u,t_order o "
				+ " where g.uid=u.userid and g.oid=o.id and g.uid='"+getPara("uid")+"' and ctype in ('0','1','2') order by g.crttime desc";
		int g = Integer.parseInt(Db.findFirst(sql_pj).getLong("pjnum").toString());
		
		//预约
		String sql_yy = "select count(c.id) as yynum from t_yuyue c,t_gogo t where t.id=c.sid and c.uid='"+getPara("uid")+"' and c.w_type = 'gg' ";
		int h = Integer.parseInt(Db.findFirst(sql_yy).getLong("yynum").toString());
		setAttr("ddnum", a + b + c);
		setAttr("wlnum", d + e);
		setAttr("tznum", f);
		setAttr("pjnum", g);
		setAttr("yynum", h);
		renderJson();
	}
	//查询我的订单
	public void getOrders(){
		String sql = "select * from t_order where uid='"+getPara("uid")+"' and status in ("+getPara("status")+") and gtype = '"+getPara("gtype")+"' order by crttime desc";
		if("".equals(getPara("gtype"))){
			sql = "select * from t_order where uid='"+getPara("uid")+"' and status in ("+getPara("status")+") order by crttime desc";
		}
		List<Record> list = Db.find(sql);
		for (int i = 0; i < list.size(); i++) {
			Record rec = list.get(i);
			String gtype = rec.getStr("gtype");
			if("carLT".equals(gtype) || "carJY".equals(gtype) || "carPJ".equals(gtype)){
				rec.set("sname", Db.findFirst("select sname from t_store where storeid='"+rec.getStr("sid")+"' ").getStr("sname"));
			}
		}
		setAttr("list", list);
		renderJson();
	}
	//发表商品评价
	public void pubCom(){
		int integral = 0;
		if("1".equals(getPara("ctype"))){
			integral = 1;
		}else if("0".equals(getPara("ctype"))){
			integral = -2;
		}

		if("gg".equals(getPara("t"))){ //公估
			String sql2 = "UPDATE t_yuyue SET replay = 1 WHERE id='"+getPara("oid")+"'";
			Db.update(sql2);
		}else if("BDZHC".equals(getPara("t"))){
			String sql = "update t_trade_cargo SET status = '4' where tid = '"+getPara("oid")+"' ";
			Db.update(sql);
		}else if("ZDYYHC".equals(getPara("t"))){
			String sql = "update t_trade_car set status = '4' where tid = '"+getPara("oid")+"'";
			Db.update(sql);
		}else if("carLT".equals(getPara("t")) || "carJY".equals(getPara("t")) || "carPJ".equals(getPara("t"))){ //商城
			String updateSql1 = "update t_user set points = points+"+integral+" where userid in (select ur.userid from "
					+" (select distinct t.userid from t_store s left join t_user t on  t.userid=s.uid left join t_goods g on g.storeid=s.storeid where g.storeid in " 
					+"(select storeid from t_goods g where g.goodid = '"+getPara("goodid")+"')) ur)";
			String sql = "update t_order SET conmen = '"+getPara("conmen")+"' where id = '"+getPara("oid")+"'";
			Db.update(updateSql1);
			Db.update(sql);
		}else{ //修理、油气 
			String updateSql1 = "update t_user set points = points+"+integral+" where userid in (select ur.userid from "
					+" (select distinct t.userid from t_service s left join t_user t on  t.userid=s.uid left join t_gogo g on g.storeid=s.id where g.storeid in"
					+" (select storeid from t_gogo g where g.id = '"+getPara("goodid")+"')) ur)";
			String sql = "update t_order SET conmen = '"+getPara("conmen")+"' where id = '"+getPara("oid")+"'";
			Db.update(updateSql1);
			Db.update(sql);
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String crttime = sdf.format(new Date());
		
		Gcomment gc = getModel(Gcomment.class);
		gc.setCid(UUID.randomUUID().toString().replace("-", ""));
		gc.setCcontent(getPara("ccontent"));
		gc.setCtype(getPara("ctype"));
		gc.setType(getPara("type"));
		gc.setImgs(getPara("imgs"));
		gc.setGoodid(getPara("goodid"));
		gc.setUid(getPara("uid"));
		gc.setOid(getPara("oid"));
		gc.setGcommenttype(getPara("t"));
		gc.setCrttime(crttime);
		boolean success = gc.save();
		setAttr("success", success);
		renderJson();
	}
	//回复商品评价
	public void pubComSS(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String crttime = sdf.format(new Date());
		int integral = 0;
		if("1".equals(getPara("ctype"))){
			integral = 3;
		}else{
			integral = 1;
		}
		Gcomment gc = getModel(Gcomment.class);
		gc.setCid(UUID.randomUUID().toString().replace("-", ""));
		gc.setCcontent(getPara("ccontent"));
		gc.setCtype(getPara("ctype"));
		gc.setType(getPara("type"));
		gc.setImgs(getPara("imgs"));
		gc.setGoodid(getPara("goodid"));
		gc.setUid(getPara("uid"));
		gc.setReid(getPara("reid"));
		gc.setCrttime(crttime);
		boolean success = gc.save();
		if(success){
			String updateSql = "update t_user set points = points+"+integral+" where userid = '"+getPara("uid")+"'";
			int succes1 = Db.update(updateSql);
			setAttr("succes1",succes1);
		}
		setAttr("success", success);
		renderJson();
	}
	//商品详情页显示一条评论及评论数
	public void getFirstCom(){
		Gcomment gc = getModel(Gcomment.class);
		String sql = "select g.*,u.username,u.img,o.gattr,o.gnum, t.goodid from  t_gcomment g left join t_order o on g.oid=o.id left join t_goods t on o.goodid=t.goodid left join  t_user u on g.uid=u.userid "
			 + "where g.uid=u.userid and g.oid=o.id and g.goodid  = '"+getPara("goodid")+"' and g.oid is not null order by g.crttime desc";
		gc = gc.findFirst(sql);
		String gcNum = Db.find(sql).size()+"";
		setAttr("data", gc);
		setAttr("gcNum", gcNum);
		renderJson();
	}
	/**
	 * 商家详情页显示一条评论及评论数
	 */
	public void getFirstStore(){
		String sql = "select g.*,u.username,u.img,o.gattr,o.gnum, t.goodid from  t_gcomment g,t_user u,t_order o ,t_goods t "
			+"where g.uid=u.userid and g.oid=o.id and g.goodid=t.goodid and t.goodid in(select t.goodid from t_goods where t.storeid = '"+getPara("storeid")+"' ) order by g.crttime desc";
		String sql2 = "select count(cid) as gcNum from t_gcomment where goodid in (select goodid from t_goods where storeid = '"+getPara("storeid")+"' )";
		Gcomment gc = Gcomment.dao.findFirst(sql);
		Gcomment gc2 = Gcomment.dao.findFirst(sql2);
		Long gcNum = gc2.get("gcNum");
		setAttr("data", gc);
		setAttr("gcNum", gcNum);
		renderJson();
	}
	/**
	 * 查询商家所有评价
	 */
	public void getFirstStore2(){
		String sql1 = "select sum(case when g.ctype=0 then 1 else 0 end) as cps,sum(case when g.ctype=1 then 1 else 0 end) as hps,sum(case when g.ctype=2 then 1 else 0 end) as zps,sum(case when g.ctype in ('0','1','2') then 1 else 0 end) as allps from  t_gcomment g left join t_order o on g.oid=o.id left join t_goods t on o.goodid=t.goodid left join  t_user u on g.uid=u.userid" 
			+" where g.uid=u.userid and g.oid=o.id and t.goodid in(select t.goodid from t_goods where t.storeid = '"+getPara("storeid")+"' ) order by g.crttime desc";
		String sql = "select g.*,u.username,u.img,o.gattr,o.gnum, t.goodid from  t_gcomment g left join t_order o on g.oid=o.id left join t_goods t on o.goodid=t.goodid left join  t_user u on g.uid=u.userid" 
			+" where g.uid=u.userid and g.oid=o.id and t.goodid in(select t.goodid from t_goods where t.storeid = '"+getPara("storeid")+"' ) and g.ctype in ("+getPara("ctype")+") order by g.crttime desc";
		List<Gcomment> Gco = Gcomment.dao.find(sql);
		List<Gcomment> list1 = Gcomment.dao.find(sql1);
		setAttr("data", Gco);
		setAttr("list1", list1);
		renderJson();
	}
	//服务--详情页显示一条评论及评论数
	public void getFirstCom_FW(){
		Gcomment gc = getModel(Gcomment.class);
		Gcomment gc2 = new Gcomment();
		String sql ="select g.*,u.username,u.img,o.cont from t_gcomment g,t_user u,t_service o "
				+ "where g.uid=u.userid and g.goodid=o.id and g.goodid='"+getPara("goodid")+"' order by g.crttime desc";
		String sql2 = "select count(cid) as gcNum from t_gcomment where goodid='"+getPara("goodid")+"' ";
		gc = gc.findFirst(sql);
		gc2 = gc2.findFirst(sql2);
		Long gcNum = gc2.get("gcNum");
		setAttr("data", gc);
		setAttr("gcNum", gcNum);
		renderJson();
	}
	//汽贸 -- 生活服务--详情页显示一条评论及评论数
	public void getFirstCom_QM(){
		Gcomment gc = getModel(Gcomment.class);
		Gcomment gc2 = new Gcomment();
		String sql ="select g.*,u.username,u.img,o.cont from t_gcomment g,t_user u,t_wares o "
				+ "where g.uid=u.userid and g.goodid=o.id and g.goodid='"+getPara("goodid")+"' order by g.crttime desc";
		String sql2 = "select count(cid) as gcNum from t_gcomment where goodid='"+getPara("goodid")+"' ";
		System.out.println(getPara("goodid"));
		gc = gc.findFirst(sql);
		gc2 = gc2.findFirst(sql2);
		Long gcNum = gc2.get("gcNum");
		setAttr("data", gc);
		setAttr("gcNum", gcNum);
		renderJson();
	}
	/**
	 * 获取 加油--加气--第一条评论
	 */
	public void getFirstCom_FW_J(){
		Gcomment gc = getModel(Gcomment.class);
		Gcomment gc2 = new Gcomment();
		String sql ="select g.*,u.username,u.img,o.cont from t_gcomment g,t_user u,t_service o "
				+ "where g.uid=u.userid and g.goodid=o.id and g.goodid='"+getPara("goodid")+"' order by g.crttime desc";
		String sql2 = "select count(cid) as gcNum from t_gcomment where goodid='"+getPara("goodid")+"' ";
		System.out.println(getPara("goodid"));
		gc = gc.findFirst(sql);
		gc2 = gc2.findFirst(sql2);
		Long gcNum = gc2.get("gcNum");
		setAttr("data", gc);
	System.out.println(gc);
		setAttr("gcNum", gcNum);
		renderJson();
	}
	//查询商品的评论
	public void getCommsByGid(){
		Gcomment gc = getModel(Gcomment.class);
		String sql1 = "select sum(case when g.ctype=0 then 1 else 0 end) as cps,sum(case when g.ctype=1 then 1 else 0 end) as hps,sum(case when g.ctype=2 then 1 else 0 end) as zps,sum(case when g.ctype in ('0','1','2') then 1 else 0 end) as allps from  t_gcomment g left join t_order o on g.oid=o.id left join t_goods t on o.goodid=t.goodid left join  t_user u on g.uid=u.userid" 
			 + " where g.uid=u.userid and g.oid=o.id and t.goodid = '"+getPara("goodid")+"' and g.oid is not null order by g.crttime desc;";
		String sql = "select g.*,u.username,u.img,o.gattr,o.gnum, t.goodid from  t_gcomment g left join t_order o on g.oid=o.id left join t_goods t on o.goodid=t.goodid left join  t_user u on g.uid=u.userid" 
			+" where g.uid=u.userid and g.oid=o.id and t.goodid = '"+getPara("goodid")+"' and g.ctype in ("+getPara("ctype")+") and g.oid is not null order by g.crttime desc";
		List<Gcomment> list = gc.find(sql);
		List<Gcomment> list1 = gc.find(sql1);
		setAttr("list", list);
		setAttr("list1", list1);
		renderJson();
	}
	public void getCommsByGid2(){
		Gcomment gc = getModel(Gcomment.class);
		String sql1 ="select sum(case when g.ctype=0 then 1 else 0 end) as cps,sum(case when g.ctype=1 then 1 else 0 end) as hps,sum(case when g.ctype=2 then 1 else 0 end) as zps,sum(case when g.ctype in ('0','1','2') then 1 else 0 end) as allps from t_gcomment g,t_user u " 
				+ "where g.uid=u.userid and g.goodid in (select id from t_gogo where storeid='"+getPara("goodid")+"')  order by g.crttime desc";
		String sql = "select g.*,u.username,u.img from t_gcomment g,t_user u "
				+ "where g.uid=u.userid and g.goodid in (select id from t_gogo where storeid='"+getPara("goodid")+"') and ctype in ("+getPara("ctype")+") order by g.crttime desc";
		List<Gcomment> list = gc.find(sql);
		List<Gcomment> list1 = gc.find(sql1);
		setAttr("list", list);
		setAttr("list1", list1);
		renderJson();
	}
	//查询货主收到的全部评价
	public void getCommsUserid(){
		Gcomment gc = getModel(Gcomment.class);
		String sql = "select t.*,f.platenum,h.username from t_mycars f right join  t_gcomment t on t.goodid=f.id  left join t_user k on "
				+ "f.uid=k.userid left join t_user h on t.uid=h.userid "
				+"where k.userid='"+getPara("uid")+"' and ctype in ("+getPara("type")+") order by f.crttime desc";
		List<Gcomment> list = gc.find(sql);
		setAttr("list", list);
		renderJson();
	}
	//查询用户历史评论
	public void getCommsByUid(){
		Gcomment gc = getModel(Gcomment.class);
		String sql1 = "select sum(case when g.ctype=0 then 1 else 0 end) as cps,sum(case when g.ctype=1 then 1 else 0 end) as hps,sum(case when g.ctype=2 then 1 else 0 end) as zps,sum(case when g.ctype in ('0','1','2') then 1 else 0 end) as allps from t_gcomment g,t_user u,t_order o "
				+ "where g.uid=u.userid and g.oid=o.id and g.uid='"+getPara("uid")+"' order by g.crttime desc";
		String sql = "select distinct g.*,u.userid,u.username,u.phone,u.img,o.gattr,o.gnum,o.gname,g.gcommenttype from t_gcomment g,t_user u,t_order o "
				+ "where g.uid=u.userid and g.oid=o.id and g.uid='"+getPara("uid")+"' and ctype in ("+getPara("ctype")+") order by g.crttime desc";
		List<Gcomment> list = gc.find(sql);
		Record record = Db.findFirst(sql1);
		setAttr("list", list);
		setAttr("data", record);
		renderJson();
	}
	//查询商家历史评论
	public void getCommsByUid2(){
		Gcomment gc = getModel(Gcomment.class);
		String sql1 = "select sum(case when g.ctype=0 then 1 else 0 end) as cps,sum(case when g.ctype=1 then 1 else 0 end) as hps,sum(case when g.ctype=2 then 1 else 0 end) as zps,sum(case when g.ctype in ('0','1','2') then 1 else 0 end) as allps from t_gcomment g,t_user u "
				+ " where g.reid=u.userid and g.reid='"+getPara("uid")+"' order by g.crttime desc";
		String sql = "select distinct g.*,u.userid,u.username,u.phone,u.img, g.gcommenttype from t_gcomment g,t_user u "
				+ " where g.reid=u.userid and g.reid='"+getPara("uid")+"' and ctype in ("+getPara("ctype")+") order by g.crttime desc";
		List<Gcomment> list = gc.find(sql);
		Record record = Db.findFirst(sql1);
		setAttr("list", list);
		setAttr("data", record);
		renderJson();
	}
	/**
	 *  查询下单修理信息
	 */
	public void getXiuLiXD(){
		String sql = "select id, loss,price,storeid,img,oldprice from t_gogo where id='"+getPara("id")+"'";
		Gogo masege = Gogo.dao.findFirst(sql);
		setAttr("masege",masege);
		System.out.println(masege);
		renderJson();
	}
	/**
	 * 查询修理商家userid
	 */
	public void getUserid(){
		String sql ="select t.userid from t_gogo f left join  t_service g on f.storeid=g.id left join t_user t on g.uid=t.userid where f.id='"+getPara("id")+"'";
		User order = User.dao.findFirst(sql);
		setAttr("data",order);
		renderJson();
	}
	/**
	 *  查询下单油气信息
	 */
	public void getYouqiXD(){
		String sql = "select id, loss,price,storeid,img,oldprice from t_gogo where id='"+getPara("id")+"'";
		Gogo masege = Gogo.dao.findFirst(sql);
		setAttr("masege",masege);
		System.out.println(masege);
		renderJson();
	}
	
	/**
	 * 获取类型(字典表)
	 */
	public void getTypes() {
		List<Types> types = Types.dao.find("select * from t_types where type='"+getPara("t")+"' order by time");
		setAttr("list", types);
		renderJson();
	}
	/**
	 * 获取车辆吨位
	 */
	public void getGoodDun(){
		List<Types> types = Types.dao.find("select * from t_types where type = '"+getPara("t")+"' ");
		setAttr("dun",types);
		renderJson();
	}
	/**
	 * 发布货车、货源信息
	 */
	public void publishHCHY() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String crttime = sdf.format(new Date());
		boolean success = false;
		if("HC".equals(getPara("t"))){
			Cars cars = getModel(Cars.class);
			cars.setCid(UUID.randomUUID().toString().replace("-", ""));
			cars.setCphone("");
			cars.setUid(getPara("uid"));
			cars.setUname(getPara("uname"));
			cars.setCphone(getPara("phone"));
			cars.setImgs(getPara("imgs"));
			cars.setStartadd(getPara("qidian"));
			cars.setTargetadd(getPara("zhongdian"));
			cars.setCarmodel(getPara("chexing"));
			cars.setLoad(getPara("dunwei"));
			cars.setPlatenum(getPara("chepaihao"));
			cars.setRname(getPara("mingcheng"));
			cars.setCnote(getPara("beizhu"));
			cars.setCrttime(crttime);
			cars.setResult(getPara("result"));
			cars.setCoord(getPara("carLocation"));
			success = cars.save();
		}else{
			Cargo cargo = getModel(Cargo.class);
			cargo.setCgid(UUID.randomUUID().toString().replace("-", ""));
			cargo.setCtime(getPara("gdate"));
			cargo.setUid(getPara("uid"));
			cargo.setUname(getPara("uname"));
			cargo.setCphone(getPara("phone"));
			cargo.setStartadd(getPara("gqidian"));
			cargo.setTargetadd(getPara("gzhongdian"));
			cargo.setRname(getPara("mingcheng2"));
			cargo.setCinfo(getPara("gname")+" "+getPara("gdunwei"));
			cargo.setPrice(getPara("gprice"));
			cargo.setCrequire("车型:"+getPara("gctype")+" 车长(米):"+getPara("gcarcd"));
			cargo.setCnote(getPara("gbeizhu"));
			cargo.setCrttime(crttime);
			cargo.setResult(getPara("gresult"));
			success = cargo.save();
		}
		setAttr("success", success);
		renderJson();
	}
	/**
	 *  公估预约--
	 */
	public void publishGG(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String crttime = sdf.format(new Date());
		
		Yuyue yuyue = getModel(Yuyue.class);
		String id = UUID.randomUUID().toString().replace("-", "");
		yuyue.setId(id);
		yuyue.setPhone(getPara("shoujihao"));
		yuyue.setName(getPara("xingming"));
		yuyue.setCard(getPara("shenfenzheng"));
		yuyue.setModels(getPara("chelx"));
		yuyue.setCrttime(crttime);
		yuyue.setUid(getPara("uid"));
		yuyue.setW_type(getPara("type"));
		yuyue.setNstore(getPara("nstore"));
		yuyue.setSid(getPara("sid"));
		yuyue.setConmen(getPara("conmen"));
		yuyue.setReconment(getPara("reconment"));
		yuyue.setReplay(getPara("replay"));
		yuyue.setPricetype(getPara("pricetype"));
		yuyue.setGnum(getPara("gnum"));
		yuyue.setPrice(getPara("price"));

		yuyue.setPolicy(getPara("imgUrls"));
		yuyue.setLicense(getPara("imgUrls_cx"));
		yuyue.setDamaged(getPara("imgUrls_js"));
		yuyue.setDriver(getPara("imgUrls_ss"));
		boolean success = yuyue.save();
		setAttr("id",id);
		setAttr("success", success);
		renderJson();
	}
	/**
	 *	修改预约下单方式 
	 */
	public void setGongGuX(){
		String sql = "update t_yuyue set pricetype = '"+getPara("pricetype")+"',conmen = '"+getPara("conmen")+"',price = '"+getPara("price")+"' where id in ("+getPara("id")+") ";
		int success = Db.update(sql);
		setAttr("success", success);
		renderJson();
	}
	/**
	 *	修改预约下单方式 
	 */
	public void setGongGu(){
		String sql = "update t_yuyue set pricetype = '"+getPara("pricetype")+"',price = '"+getPara("price")+"' where id in ("+getPara("id")+") ";
		int success = Db.update(sql);
		setAttr("success", success);
		renderJson();
	}
	/**
	 * 查询 预约下单 的价格
	 */
	public void getPrice(){
		String sql = "select price from t_yuyue where id = "+getPara("id")+" ";
		Yuyue tist = Yuyue.dao.findFirst(sql);
		setAttr("tist",tist);
		renderJson();
	}
	/**
	 * 油气预约
	 */
	public void publishYQ(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String crttime = sdf.format(new Date());
		Yuyue yuyue = getModel(Yuyue.class);
		yuyue.setId(UUID.randomUUID().toString().replace("-", ""));
		yuyue.setPhone(getPara("phone"));
		yuyue.setAddress(getPara("didian"));
		yuyue.setService(getPara("neirong"));
		yuyue.setUid(getPara("uid"));
		yuyue.setW_type(getPara("type"));
		yuyue.setCrttime(crttime);
		yuyue.setNstore(getPara("sname"));
		yuyue.setSid(getPara("sid"));
		boolean success = yuyue.save();
		setAttr("success", success);
		renderJson();
	}
	/**
	 * 判断是否已预约
	 */
	public void getYuyue(){
		String sql = "select * from t_yuyue where uid = '"+getPara("uid")+"' and sid = '"+getPara("cid")+"'";
		List <Yuyue> yuyue = Yuyue.dao.find(sql);
		setAttr("yuyue", yuyue);
		renderJson();
	}
	/**
	 * 判断是否已预约
	 */
	public void getYuyueY(){
		String sql = "select * from t_yuyue where uid = '"+getPara("uid")+"' and sid = '"+getPara("sid")+"'";
		List <Yuyue> yuyue = Yuyue.dao.find(sql);
		setAttr("yuyue", yuyue);
		renderJson();
	}
	/**
	 * 汽贸预约
	 */
	public void publishQM(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String crttime = sdf.format(new Date());
		Yuyue yuyue = getModel(Yuyue.class);
		yuyue.setId(UUID.randomUUID().toString().replace("-", ""));
		yuyue.setPhone(getPara("phone"));
		yuyue.setName(getPara("xingming"));
		yuyue.setUid(getPara("uid"));
		yuyue.setW_type(getPara("type"));
		yuyue.setSid(getPara("cid"));
		yuyue.setCrttime(crttime);
		boolean success = yuyue.save();
		setAttr("success", success);
		renderJson();
	}
	/**
	 * 分页查询货车、货物
	 */
	public void getHCHY(){
		int pageNumber = getParaToInt("pageNumber", Integer.parseInt(getPara("pageNumber")));
		int pageSize = getParaToInt("pageSizes", Integer.parseInt(getPara("pageSize")));
		
		if("HC".equals(getPara("t"))){
			Page<Cars> cars = Cars.dao.getCarsPaginate(pageNumber, pageSize, getPara("qidian"), getPara("zhongdian"), getPara("ctype"), getPara("dunwei"));
			int nono = cars.getTotalRow();
			setAttr("list", cars.getList());
			setAttr("pageNumber", cars.getPageNumber());
			setAttr("data", nono);
		}else{
			Page<Cargo> cargo = Cargo.dao.getHwsPaginate(pageNumber, pageSize, getPara("qidian"), getPara("zhongdian"), getPara("ctype"), getPara("jiage"));
			int nono = cargo.getTotalRow();
			setAttr("list", cargo.getList());
			setAttr("pageNumber", cargo.getPageNumber());
			setAttr("data",nono);
		}
		renderJson();
	}
	/**
	 * 分页查询 自己发布的货物货车
	 */
	public void getHCHY_MY(){
		int pageNumber = getParaToInt("pageNumber", 1);
		int pageSize = getParaToInt("pageSize", 10);
		
		if("HC".equals(getPara("t"))){
			Page<Cars> cars = Cars.dao.getCarsP(pageNumber, pageSize, getPara("uid"));
			setAttr("list", cars.getList());
		}else{
			Page<Cargo> cargo = Cargo.dao.getHwsP(pageNumber, pageSize, getPara("uid"));
			setAttr("list", cargo.getList());
		}
		renderJson();
	}
	/**
	 * 查看自己发布的货物
	 */
	public void getHCHY_MY_X(){
		String sql ="select * from t_cargo where cgid = '"+getPara("cgid")+"' order by crttime desc";
		Cargo goods = Cargo.dao.findFirst(sql);
		setAttr("goods", goods);
		renderJson();
	}
	/**
	 * 查看货车下单的货车用户
	 */
	public void getHC_DD(){
		String sql = "select t.*,u.phone,u.username from t_trade_car t left join t_user u on t.uid=u.userid where t.cid = '"+getPara("cid")+"' order by t.ttime desc ";
		List<Record> list = Db.find(sql);
		setAttr("list", list);
		renderJson();
	}
	/**
	 * 查看货车下单的货车用户详细信息
	 */
	public void getHC_DDD(){
		String sql = "select t.*,u.phone,u.username,u.userid from t_trade_car t left join t_user u on t.uid=u.userid where t.tid = '"+getPara("tid")+"' order by t.ttime desc ";
		Trade_car list = Trade_car.dao.findFirst(sql);
		System.out.println(list);
		setAttr("list", list);
		renderJson();
	}
	/**
	 * 货车 给货物 下单 -- 更改用户数据
	 */
	public void getHW_FD(){
		String sql1 = "update t_trade_car set status = 1 where tid = '"+getPara("tid")+"' ";
		Db.update(sql1);
		setAttr("msg", "添加成功");
		renderJson();
	}
	/**
	 * 货物 给货车 下单 -- 更改用户数据
	 */
	public void getHC_FD(){
		String sql1 = "update t_trade_cargo set status = 1 where tid = '"+getPara("id")+"' ";
		Db.update(sql1);
		setAttr("msg", "添加成功");
		renderJson();
	}
	/**
	 * 查询我预约的货车
	 */
	public void getDingD_HC(){
		String sql_HC_z = "select t.tid,t.uname,t.status,t.rmoney,g.*,f.points from t_trade_cargo t left join t_cargo g on t.cid=g.cgid left join t_user f on g.uid=f.userid where t.uid ='"+getPara("uid")+"' and t.status in ("+getPara("status")+") order by ttime desc";
		String sql_HC_b = "select t.*,g.startadd,g.targetadd,k.tname,f.points from t_trade_cargo t left join t_cargo g on t.cid=g.cgid left join t_user f on g.uid=f.userid left join t_types k on t.cartype=k.id where g.uid ='"+getPara("uid")+"'  and t.status in ("+getPara("status")+") order by ttime desc";
		List<Record> list1 = Db.find(sql_HC_z);
		List<Record> list2 = Db.find(sql_HC_b);
		List<Record> list = new ArrayList<Record>();
		for (int i = 0; i < list1.size(); i++) {
			Record rec = list1.get(i);
			rec.set("role", "zd");
			list.add(rec);
		}
		for (int i = 0; i < list2.size(); i++) {
			Record rec = list2.get(i);
			rec.set("role", "bd");
			list.add(rec);
		}
		setAttr("list", list);
		renderJson();
	}
	/**
	 * 查询我预约的货物
	 */
	public void getDingD_HW(){
		String sql_HC_z = "select t.tid,t.uname,t.status,t.rmoney,g.*,f.points,f.userid from t_trade_car t left join t_cars g on t.cid=g.cid left join t_user f on g.uid=f.userid where t.uid ='"+getPara("uid")+"' and t.status in ("+getPara("status")+") order by ttime desc";
		String sql_HC_b = "select t.*,f.points from t_trade_car t left join t_cars g on t.cid=g.cid left join t_user f on g.uid=f.userid where g.uid ='"+getPara("uid")+"'  and t.status in ("+getPara("status")+") order by ttime desc";
		List<Record> list1 = Db.find(sql_HC_z);
		List<Record> list2 = Db.find(sql_HC_b);
		List<Record> list = new ArrayList<Record>();
		for (int i = 0; i < list1.size(); i++) {
			Record rec = list1.get(i);
			rec.set("role", "zd");
			list.add(rec);
		}
		for (int i = 0; i < list2.size(); i++) {
			Record rec = list2.get(i);
			rec.set("role", "bd");
			list.add(rec);
		}
		setAttr("list", list);
		renderJson();
	}
	/**
	 * 查看货物下单的货车用户
	 */
	public void getHW_DD(){
		String sql = "select t.*,u.phone,u.username,u.points,u.userid from t_trade_cargo t left join t_user u on t.uid=u.userid where t.cid ='"+getPara("cid")+"' order by t.ttime desc";
		List<Record> list = Db.find(sql);
		setAttr("list", list);
		System.out.println(list);
		renderJson();
	}
	/**
	 * 查看自己发布的货车
	 */
	public void getHCHY_MY_Y(){
		String sql ="select t.*,f.points from t_cars t left join t_user f on t.uid=f.userid where t.cid = '"+getPara("cid")+"' order by crttime desc";
		Cars goods = Cars.dao.findFirst(sql);
		setAttr("goods", goods);
		renderJson();
	}
	/**
	 * 查看抢单 的货车详情
	 */
	/**
	 * 查看抢单 的货物用户详细信息
	 */
	public void getXinXi(){
		String sql = "select t.*,u.phone,u.username,u.points,s.tname,u.userid from t_trade_cargo t left join t_user u on t.uid=u.userid left join t_types s on t.cartype=s.id where t.tid = '"+getPara("id")+"' ";
		Trade_cargo list = Trade_cargo.dao.findFirst(sql);
//		List<Record> list = Db.find(sql);
		System.out.println(list);
		setAttr("list", list);
		renderJson();
	}
	/**
	 * 查询货物抢单人数
	 */
	public void getHWY(){
		String sql = "select count(*) as num from t_trade_car where cid = '"+getPara("cid")+"'";
		Trade_car rest = getModel(Trade_car.class);
		rest  = rest.findFirst(sql);
		setAttr("rest", rest.getLong("num"));
		renderJson();
	}
	/**
	 * 查询货车抢单人数
	 */
	public void getHWQ(){
		String sql = "select count(*) as num from t_trade_cargo where cid = '"+getPara("cgid")+"'";
		Trade_cargo rest = getModel(Trade_cargo.class);
		rest  = rest.findFirst(sql);
		setAttr("rest", rest.getLong("num"));
		renderJson();
	}
	/**
	 * 终止货物抢单
	 */
	public void stopQDHY(){
		String sql = "update t_cargo set result = 1 where cgid = '"+getPara("cgid")+"' ";
		Db.update(sql);
		setAttr("msg", "添加成功");
		renderJson();
	}
	/**
	 * 终止货物抢单
	 */
	public void startQDHY(){
		String sql = "update t_cargo set result = 0 where cgid = '"+getPara("cgid")+"' ";
		Db.update(sql);
		setAttr("msg", "添加成功");
		renderJson();
	}
	/**
	 * 终止货车抢单
	 */
	public void stopQDHW(){
		String sql = "update t_cars set result = 1  where cid = '"+getPara("cid")+"' ";
		Db.update(sql);
		setAttr("msg", "添加成功");
		renderJson();
	}
	/**
	 * 终止开始抢单
	 */
	public void startQDHW(){
		String sql = "update t_cars set result = 0  where cid = '"+getPara("cid")+"' ";
		Db.update(sql);
		setAttr("msg", "添加成功");
		renderJson();
	}
	/**
	 * 根据ID查询单个货车、货物
	 */
	public void getHCHYById(){
		if("HC".equals(getPara("t"))){
			String sql = "select t.*,f.points from t_cars t left join t_user f on t.uid=f.userid where t.cid='"+getPara("id")+"' ";
			Cars cars = Cars.dao.findFirst(sql);
			setAttr("data", cars);
		}else{
			String sql = "select * from t_cargo where cgid='"+getPara("id")+"' ";
			Cargo cargo = Cargo.dao.findFirst(sql);
			setAttr("data", cargo);
		}
		renderJson();
	}
	//货车货物下单
	public void setOrder_HWHC(){
		boolean success = false;
		String tid = UUID.randomUUID().toString().replace("-", "");
		if("HW".equals(getPara("t"))){
			Trade_cargo cargo = getModel(Trade_cargo.class);
			cargo.setTid(tid);
			cargo.setCid(getPara("id"));
			cargo.setUid(getPara("uid"));
			cargo.setStatus("0");
			cargo.setCarnum(getPara("chepaihao"));
			cargo.setNum(getPara("shuliang"));
			cargo.setUname(getPara("mingzi"));
			cargo.setUphone(getPara("phone"));
			cargo.setCartype(getPara("chexing"));
			cargo.setAddress(getPara("weizhi"));
			cargo.setTtime(getPara("shijian"));
			success = cargo.save();
		}else{
			Trade_car car = getModel(Trade_car.class);
			car.setTid(tid);
			car.setCid(getPara("id"));
			car.setUid(getPara("uid"));
			car.setStatus("0");
			car.setStartadd(getPara("qidian"));
			car.setTargetadd(getPara("zhongdian"));
			car.setPrice(getPara("price"));
			car.setUname(getPara("mingzi"));
			car.setDunwei(getPara("dunwei"));
			car.setTtime(getPara("zctime"));
			success = car.save();
		}
		setAttr("carIntid", tid);
		setAttr("success", success);
		renderJson();
	}
	//验证 是否已预约货车
	public void yanZ_HWHC(){
		String sql = "select count(tid) as gNum from t_trade_car where cid='"+getPara("id")+"' and uid='"+getPara("uid")+"' ";
		String sql2 = "select uid from t_cars where cid='"+getPara("id")+"' ";
		Trade_car goods = Trade_car.dao.findFirst(sql);
		Record rec = Db.findFirst(sql2);
		setAttr("data", goods);
		setAttr("rec", rec);
		renderJson();
	}
	//验证 是否已预约货物
	public void yanZH_HWHC(){
		String sql = "select count(tid) as gNum from t_trade_cargo where cid='"+getPara("id")+"' and uid='"+getPara("uid")+"' ";
		String sql2 = "select uid from t_cargo where cgid='"+getPara("id")+"' ";
		Trade_cargo goods = Trade_cargo.dao.findFirst(sql);
		Record rec = Db.findFirst(sql2);
		setAttr("data",goods);
		setAttr("rec", rec);
		renderJson();
	}
	//查询所有预约记录
	public void queryTrades(){
		String type = getPara("t"), uid = getPara("uid");
		List<Trade_cargo> list_HW = null;
		List<Trade_car> list_HC = null;
		List<Yuyue> list_GG = null;
		List<Order> list_YQ = null;
		List<Order> list_XL = null;
		List<Yuyue> list_QM = null;
		List<Yuyue> list_JC = null;
		
		Trade_cargo cargo = getModel(Trade_cargo.class);
		Trade_car car = getModel(Trade_car.class);
		Order order = getModel(Order.class);
		Yuyue yuyue = getModel(Yuyue.class);
		
		String sql_HW = "select c.* ,t.status,t.tid from t_trade_cargo t,t_cargo c where t.cid=c.cgid and t.uid='"+uid+"' order by crttime desc";
		String sql_HC = "select c.* ,t.status,t.tid from t_trade_car t,t_cars c where t.cid=c.cid and t.uid='"+uid+"' order by crttime desc";
		String sql_GG = "select c.*, t.loss,t.img from t_yuyue c,t_gogo t where t.id=c.sid and c.uid='"+uid+"' and c.w_type = 'gg' order by c.crttime desc ";
		String sql_YQ = "select g.*,nu.phone,nu.sname,nu.address,t.status,t.id,t.sid from t_gogo g left join t_service nu on g.storeid=nu.id left join t_order t on t.goodid=g.id left join t_user f on t.uid=f.userid where f.userid='"+uid+"' and gtype = 'carGoYQ' order by crttime desc";
		String sql_XL = "select g.*,nu.phone,nu.sname,nu.address,t.status,t.id,t.sid from t_gogo g left join t_service nu on g.storeid=nu.id left join t_order t on t.goodid=g.id left join t_user f on t.uid=f.userid where f.userid='"+uid+"' and gtype = 'carGoXL' order by crttime desc";
		String sql_QM = "select c.* ,t.style from t_yuyue c,t_carsell t where t.carsellid=c.sid and c.uid='"+uid+"' and c.w_type = 'qm' order by c.crttime desc";
		String sql_JC = "select c.* ,t.style from t_yuyue c,t_carsell t where t.carsellid=c.sid and c.uid='"+uid+"' and c.w_type = 'es' order by c.crttime desc"; 
		
		if("HW".equals(type)){
			list_HW = cargo.find(sql_HW);
		}else if("HC".equals(type)){
			list_HC = car.find(sql_HC);
		}else if("GG".equals(type)){
			list_GG = yuyue.find(sql_GG);
		}else if("JQ".equals(type)){
			list_YQ = order.find(sql_YQ);
		}else if("XL".equals(type)){
			list_XL = order.find(sql_XL);
		}else if("QM".equals(type)){
			list_QM = yuyue.find(sql_QM);
		}else if("JC".equals(type)){
			list_JC = yuyue.find(sql_JC);
		}else if("".equals(type)){
			list_HW = cargo.find(sql_HW);
			list_HC = car.find(sql_HC);
			list_GG = yuyue.find(sql_GG);
			list_YQ = order.find(sql_YQ);
			list_XL = order.find(sql_XL);
			list_QM = yuyue.find(sql_QM);
			list_JC = yuyue.find(sql_JC);
		}
		setAttr("list_HW", list_HW);
		setAttr("list_HC", list_HC);
		setAttr("list_GG", list_GG);
		setAttr("list_YQ", list_YQ);
		setAttr("list_XL", list_XL);
		setAttr("list_QM", list_QM);
		setAttr("list_JC", list_JC);
		renderJson();
	}
	//查询我的预约--公估
	public void getYuyueById_GG(){
		String sql = "select policy,license,damaged from t_yuyue where id='"+getPara("id")+"' ";
		Record data = Db.findFirst(sql);
		setAttr("data", data);
		renderJson();
	}
	/**
	 * ---------------------------------
	 * 论坛模块
	 * @author 杜腾飞
	 * 		2016年6月1日 16:40:59
	 * ---------------------------------
	 */
	//发表帖子
	public void addPost(){
		Post post = getModel(Post.class);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String crttime = sdf.format(new Date());
		post.setPid(UUID.randomUUID().toString().replace("-", ""));
		post.setType(getPara("type"));
		post.setStype(getPara("stype"));
		post.setUid(getPara("uid"));
		post.setCrttime(crttime);
		post.setTitle(getPara("title"));
		post.setContent(getPara("content"));
		post.setImgs(getPara("imgs"));
		post.setPraisenum("0");
		post.setCommentnum("0");
		boolean success = post.save();
		setAttr("success", success);
		renderJson();
	}
	//查询帖子
	public void getPosts(){
		Post post = getModel(Post.class);
		String sql = "";
		if("".equals(getPara("type")) || getPara("type") == null){
			sql = "select p.*,u.img as uImg,u.username as uName from t_post p left join t_user u on p.uid=u.userid order by '"+getPara("orderCond")+"' limit 5 ";
		}else{
			sql = "select p.*,u.img as uImg,u.username as uName from t_post p left join t_user u on p.uid=u.userid where p.type='"+getPara("type")+"' order by '"+getPara("orderCond")+"' ";
		}
		List<Post> list = post.find(sql);
		setAttr("list", list);
		renderJson();
	}	
	//查询我的帖子
	public void getMyPosts(){
		Post post = getModel(Post.class);
		String sql = "select p.*,u.img as uImg,u.username as uName from t_post p,t_user u where p.uid=u.userid and p.uid='"+getPara("uid")+"' order by '"+getPara("orderCond")+"' limit 5 ";
		List<Post> list = post.find(sql);
		setAttr("list", list);
		renderJson();
	}	
	//查询帖子和回复
	public void getPostAdHF(){
		Postcomment pcs = getModel(Postcomment.class);
		String sql = "select p.pid,p.title from t_post p,t_user u where p.uid=u.userid and p.uid='"+getPara("uid")+"' order by p.crttime desc ";
		List<Record> list = Db.find(sql);
		List<Record> rsList = new ArrayList<Record>(); 
		for (int i = 0; i < list.size(); i++) {
			Record p = list.get(i);
			if(p != null){
				String sql2 = "select p.content,p.crttime,u.img,u.username from t_postcomment p,t_user u where p.uid=u.userid and p.pid='"+p.getStr("pid")+"' order by p.crttime desc ";
				Postcomment pc = pcs.findFirst(sql2);
				if(pc != null){
					p.set("comment", pc.getContent());
					p.set("crttime", pc.getCrttime());
					p.set("img", pc.get("img"));
					p.set("username", pc.get("username"));
					rsList.add(p);
				}
			}
		}
		setAttr("list", rsList);
		renderJson();
	}
	//查询帖子详情
	public void getPostById(){
		Post post = getModel(Post.class);
		String sql = "select p.*,u.userid,u.img as uImg,u.username as uName from t_post p,t_user u where p.uid=u.userid and p.pid='"+getPara("pid")+"' ";
		post = post.findFirst(sql);
		Postcomment pc = getModel(Postcomment.class);
		String sql2 = "select pc.*,u.username,u.img from t_postcomment pc,t_user u where pc.uid=u.userid and pc.pid='"+getPara("pid")+"' order by pc.msgid,pc.crttime desc";
		List<Postcomment> list = pc.find(sql2);
		setAttr("data", post);
		setAttr("list", list);
		renderJson();
	}	
	//点赞
	public void addZan(){
		String sql = "select * from t_zan where uid='"+getPara("uid")+"' and pid='"+getPara("pid")+"' ";
		Record rec = Db.findFirst(sql);
		if(rec == null){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			String crttime = sdf.format(new Date());
			String sql2 = "insert into t_zan (id, uid, pid, crttime) values"
					+ "('"+UUID.randomUUID().toString().replace("-", "")+"',"
					+ " '"+getPara("uid")+"', '"+getPara("pid")+"',"
					+ " '"+crttime+"')";
			Post post = getModel(Post.class);
			String sql3 = "select praisenum from t_post where pid='"+getPara("pid")+"' ";
			int praisenum = Integer.parseInt(post.findFirst(sql3).get("praisenum")+"");
			post.setPid(getPara("pid"));
			post.setPraisenum((praisenum+1)+"");
			post.update();
			Db.update(sql2);
			setAttr("praisenum", post.getPraisenum());
		}
		renderJson();
	}
	//评论帖子
	public void addGCom(){
		Postcomment pc = getModel(Postcomment.class);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String crttime = sdf.format(new Date());
		pc.setId(UUID.randomUUID().toString().replace("-", ""));
		pc.setUid(getPara("uid"));
		pc.setPid(getPara("pid"));
		pc.setContent(getPara("content"));
		pc.setCrttime(crttime);
		pc.setReplay(getPara("replyUid"));
		pc.setMsgid(getPara("msgid"));
		pc.setIfnew("0");
		boolean success = pc.save();

		Post post = getModel(Post.class);
		String sql = "select commentnum from t_post where pid='"+getPara("pid")+"' ";
		int commentnum = Integer.parseInt(post.findFirst(sql).get("commentnum")+"");
//		String sql2 = "update t_post set commentnum='"+(commentnum + 1)+"' where pid='"+getPara("pid")+"' ";
		post.setPid(getPara("pid"));
		post.setCommentnum((commentnum+1)+"");
		post.update();
		
		setAttr("commentnum", post.getCommentnum());
		setAttr("success", success);
		renderJson();
	}
	//查询帖子类型
	public void getPostTypes(){
		String uid = getPara("uid"), sql = "";
		if("all".equals(getPara("type"))){
			sql = "select * from t_types where type='post' ";
			String sql2 = "select types from t_posttype where uid='"+uid+"'";
			Record r = Db.findFirst(sql2);
			if(r != null){
				String rs = r.get("types");
				setAttr("list2", rs);
			}else{
				setAttr("list2", "");
				
			}
		}else{
			sql = "select * from t_posttype where uid='"+uid+"' ";
			Record rec = Db.findFirst(sql);
			if(rec != null){
				String str = rec.getStr("types"), types = "";
				String[] _types = str.split(",");
				for (int i = 0; i < _types.length; i++) {
					System.out.println("_types[i]:("+_types[i]+")");
					if(!"".equals(_types[i])){
						types += "'" + _types[i] + "',";
					}
				}
				types = types.substring(0, types.length()-1);
				sql = "select * from t_types where id in ("+types+") ";
			}
		}
		List<Record> list = Db.find(sql);
		setAttr("list", list);
		renderJson();
	}
	//添加个人帖子类型
	public void addPostType(){
		String sql = "select types from t_posttype where uid='"+getPara("uid")+"' ", sql2 = "";
		Record rec = Db.findFirst(sql);
		if(rec == null){
			sql2 = "insert into t_posttype (id, uid, types) values"
				 + "('"+UUID.randomUUID().toString().replace("-", "")+"', '"+getPara("uid")+"', '"+getPara("type")+",')";
			Db.update(sql2);
			setAttr("msg", "添加成功");
		}else{
			if((rec.getStr("types")+"").indexOf(getPara("type")) > -1){
				setAttr("msg", "该功能已添加");
			}else{
				String types = rec.getStr("types") + getPara("type") + ",";
				sql2 = "update t_posttype set types='"+types+"' where uid='"+getPara("uid")+"' ";
				Db.update(sql2);
				setAttr("msg", "添加成功");
			}
		}
		renderJson();
	}
	//提交用户反馈信息
	public void feedback(){
		String sql = "insert into t_feedback (id, uid, content, reply, crttime) values("
				+ " '"+UUID.randomUUID().toString().replace("-", "")+"',"
				+ " '"+getPara("uid")+"',"
				+ " '"+getPara("content")+"', '', '"+crttime+"')";
		Db.update(sql);
		renderJson();
	}
	//充值
	public void recharge(){
		Szdetail szdetail = getModel(Szdetail.class);
		szdetail.setId(UUID.randomUUID().toString().replace("-", ""));
		szdetail.setType("1");
		szdetail.setNote(getPara("note"));
		szdetail.setMoney(getPara("money"));
		szdetail.setUid(getPara("uid"));
		szdetail.setTime(crttime);
		szdetail.save();
		Db.update("update t_user set money=money+"+getPara("money")+" where userid='"+getPara("uid")+"' ");
		renderJson();
	}
	//添加消费记录
	public void consRecord(){
		Szdetail szdetail = getModel(Szdetail.class);
		szdetail.setId(UUID.randomUUID().toString().replace("-", ""));
		szdetail.setType("0");
		szdetail.setNote(getPara("note"));
		szdetail.setMoney(getPara("money"));
		szdetail.setUid(getPara("uid"));
		szdetail.setTime(crttime);
		szdetail.save();
		renderJson();
	}
	//查询我的账户【收支明细】
	public void getMoneyDetail(){
		//查询明细
		String _pageNumber = getPara("pagenumber"), _pageSize = getPara("pagesize");
		if("".equals(_pageNumber) || _pageNumber == null || Integer.parseInt(_pageNumber) < 1){
			_pageNumber = "1";
		}
		if("".equals(_pageSize) || _pageSize == null){
			_pageSize = "10";
		}
		int pageNumber = getParaToInt(_pageNumber, Integer.parseInt(_pageNumber)), pageSize = getParaToInt(_pageSize, Integer.parseInt(_pageSize));
		Page<Szdetail> page = Szdetail.dao.paginate(pageNumber, pageSize, "select * from t_szdetail where uid= '"+getPara("uid")+"'");
		//查询余额
		User rec = User.dao.findFirst("select money from t_user where userid= '"+getPara("uid")+"'");
		setAttr("page", page);
		setAttr("data", rec);
		renderJson();
	}
	//新增提现记录
	public void addDCash(){
		//插入提现记录表
		String sql = "insert into t_dcash (id, uid, money, cardno, cardname, cardtype, status, crttime) values ("
				+ " '"+UUID.randomUUID().toString().replace("-", "")+"',"
				+ " '"+getPara("uid")+"',"
				+ " '"+getPara("money")+"',"
				+ " '"+getPara("cardno")+"',"
				+ " '"+getPara("cardname")+"',"
				+ " '"+getPara("cardtype")+"',"
				+ " '0',"
				+ " '"+crttime+"'"
				+ " )";
		int success = Db.update(sql);
		//修改用户余额
		String sql2 = "update t_user set money=money-"+getPara("money")+" where userid='"+getPara("uid")+"' ";
		success += Db.update(sql2);
		setAttr("success", success);
		renderJson();
	}
	//验证银行卡号
	public void checkBankNo(){
		String cardNumber = getPara("cardno"), msg = "", name = "";// 卡号
        cardNumber = cardNumber.replaceAll(" ", "");  
        boolean status = true;
          
        //位数校验  
        if (cardNumber.length() == 16 || cardNumber.length() == 19) {  
  
        } else {  
        	msg = "卡号位数无效";
            status = false;
        }  
          
        //校验  
        if (CheckBankCard.checkBankCard(cardNumber) == true) {  
  
        } else {  
        	msg = "卡号校验失败";
            status = false;
        }  
  
        //判断是不是银联，老的卡号都是服务电话开头，比如农行是95599  
        // http://cn.unionpay.com/cardCommend/gyylbzk/gaishu/file_6330036.html
        if(status){
	        if (cardNumber.startsWith("62")) {  
	        	msg = "中国银联卡";
	            status = true;
	        } else {  
	        	msg = "国外的卡，或者旧银行卡，暂时没有收录";
	            status = false;
	        }  
	  
	        name = BankCardBin.getNameOfBank(cardNumber.substring(0, 6), 0);// 获取银行卡的信息  
	        System.out.println(name);  
        }
        System.out.println(msg);  
          
        //归属地每个银行不一样，这里只收录农行少数地区代码  
//        if (name.startsWith("农业银行") == true) {  
//            if (cardNumber.length() == 19) {  
//                //4大银行的16位是信用卡  
//                //注意：信用卡没有开户地之说，归总行信用卡部。唯独中国银行的长城信用卡有我的地盘这个属性  
//                name = ABCBankAddr.getAddrOfBank(cardNumber.substring(6, 10));  
//                System.out.println("开户地:" + name);  
//            }  
//        }
        setAttr("status", status);
        setAttr("msg", msg);
        setAttr("name", name);
		renderJson();
	}
	//新增银行卡
	public void addMyCards(){
		String cardno = getPara("cardno");
		System.out.println(cardno);
		System.out.println(cardno.length());
		String sql = "insert into t_mycards (id, uid, cardno, cardname, cardtype, endnum, ifdef) values ("
				+ " '"+UUID.randomUUID().toString().replace("-", "")+"',"
				+ " '"+getPara("uid")+"',"
				+ " '"+cardno+"',"
				+ " '"+getPara("cardname")+"',"
				+ " '"+getPara("cardtype")+"',"
				+ " '"+cardno.substring(cardno.length()-4, cardno.length())+"',"
				+ " '0'"
				+ " )";
		int success = Db.update(sql);
		setAttr("success", success);
		renderJson();
	}
	//查询我的银行卡
	public void getMyCards(){
		List<Record> list = Db.find("select * from t_mycards where uid='"+getPara("uid")+"' order by ifdef desc ");
		setAttr("list", list);
		renderJson();
	}
}
