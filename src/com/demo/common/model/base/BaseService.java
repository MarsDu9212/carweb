package com.demo.common.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseService<M extends BaseService<M>> extends Model<M> implements IBean {

	public void setId(java.lang.String id) {
		set("id", id);
	}

	public java.lang.String getId() {
		return get("id");
	}

	public void setSname(java.lang.String sname) {
		set("sname", sname);
	}

	public java.lang.String getSname() {
		return get("sname");
	}

	public void setAddress(java.lang.String address) {
		set("address", address);
	}

	public java.lang.String getAddress() {
		return get("address");
	}

	public void setCoord(java.lang.String coord) {
		set("coord", coord);
	}

	public java.lang.String getCoord() {
		return get("coord");
	}

	public void setSlv(java.lang.String slv) {
		set("slv", slv);
	}

	public java.lang.String getSlv() {
		return get("slv");
	}

	public void setPhone(java.lang.String phone) {
		set("phone", phone);
	}

	public java.lang.String getPhone() {
		return get("phone");
	}

	public void setDesc(java.lang.String desc) {
		set("desc", desc);
	}

	public java.lang.String getDesc() {
		return get("desc");
	}

	public void setCont(java.lang.String cont) {
		set("cont", cont);
	}

	public java.lang.String getCont() {
		return get("cont");
	}

	public void setType(java.lang.String type) {
		set("type", type);
	}

	public java.lang.String getType() {
		return get("type");
	}

	public void setGgtype(java.lang.String ggtype) {
		set("ggtype", ggtype);
	}

	public java.lang.String getGgtype() {
		return get("ggtype");
	}

	public void setZytype(java.lang.String zytype) {
		set("zytype", zytype);
	}

	public java.lang.String getZytype() {
		return get("zytype");
	}

}
