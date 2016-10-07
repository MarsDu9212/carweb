package com.demo.common.model;

import com.jfinal.plugin.activerecord.ActiveRecordPlugin;

/**
 * Generated by JFinal, do not modify this file.
 * <pre>
 * Example:
 * public void configPlugin(Plugins me) {
 *     ActiveRecordPlugin arp = new ActiveRecordPlugin(...);
 *     _MappingKit.mapping(arp);
 *     me.add(arp);
 * }
 * </pre>
 */
public class _MappingKit {

	public static void mapping(ActiveRecordPlugin arp) {
		arp.addMapping("t_address", "id", Address.class);
		arp.addMapping("t_cargo", "cgid", Cargo.class);
//		arp.addMapping("t_carpre", "", Carpre.class);
		arp.addMapping("t_cars", "cid", Cars.class);
		arp.addMapping("t_carsell", "carsellid", Carsell.class);
		arp.addMapping("t_cart", "cartid", Cart.class);
		arp.addMapping("t_company", "cartid", Company.class);
		arp.addMapping("t_post", "pid", Post.class);
		arp.addMapping("t_postcomment", "id", Postcomment.class);
		arp.addMapping("t_device", "id", Device.class);
		arp.addMapping("t_gcomment", "cid", Gcomment.class);
		arp.addMapping("t_gger", "id", Gger.class);
		arp.addMapping("t_goods", "goodid", Goods.class);
//		arp.addMapping("t_login", "", Login.class);
		arp.addMapping("t_mycars", "id", Mycars.class);
		arp.addMapping("t_order", "id", Order.class);
//		arp.addMapping("t_sale", "", Sale.class);
		arp.addMapping("t_service", "id", Service.class);
		arp.addMapping("t_store", "storeid", Store.class);
		arp.addMapping("t_szdetail", "id", Szdetail.class);
		arp.addMapping("t_trade_car", "tid", Trade_car.class);
		arp.addMapping("t_trade_cargo", "tid", Trade_cargo.class);
		arp.addMapping("t_types", "id", Types.class);
		arp.addMapping("t_user", "userid", User.class);
		arp.addMapping("t_version", "version", Version.class);
		arp.addMapping("t_wares", "id", Wares.class);
		arp.addMapping("t_yuyue", "id", Yuyue.class);
		arp.addMapping("t_gogo","id", Gogo.class);
	}
}
