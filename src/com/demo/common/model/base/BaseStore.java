package com.demo.common.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseStore<M extends BaseStore<M>> extends Model<M> implements IBean {

	public void setStoreid(java.lang.String storeid) {
		set("storeid", storeid);
	}

	public java.lang.String getStoreid() {
		return get("storeid");
	}

	public void setSname(java.lang.String sname) {
		set("sname", sname);
	}

	public java.lang.String getSname() {
		return get("sname");
	}

	public void setSaddress(java.lang.String saddress) {
		set("saddress", saddress);
	}

	public java.lang.String getSaddress() {
		return get("saddress");
	}

	public void setSabout(java.lang.String sabout) {
		set("sabout", sabout);
	}

	public java.lang.String getSabout() {
		return get("sabout");
	}

	public void setSimgs(java.lang.String simgs) {
		set("simgs", simgs);
	}

	public java.lang.String getSimgs() {
		return get("simgs");
	}

	public void setSlv(java.lang.String slv) {
		set("slv", slv);
	}

	public java.lang.String getSlv() {
		return get("slv");
	}

	public void setType(java.lang.String type) {
		set("type", type);
	}

	public java.lang.String getType() {
		return get("type");
	}

	public void setIfsale(java.lang.String ifsale) {
		set("ifsale", ifsale);
	}

	public java.lang.String getIfsale() {
		return get("ifsale");
	}

	public void setSphone(java.lang.String sphone) {
		set("sphone", sphone);
	}

	public java.lang.String getSphone() {
		return get("sphone");
	}

	public void setComid(java.lang.String comid) {
		set("comid", comid);
	}

	public java.lang.String getComid() {
		return get("comid");
	}

	public void setGoodcom(java.lang.String goodcom) {
		set("goodcom", goodcom);
	}

	public java.lang.String getGoodcom() {
		return get("goodcom");
	}

}
