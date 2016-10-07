package com.demo.common.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseDevice<M extends BaseDevice<M>> extends Model<M> implements IBean {

	public void setId(java.lang.String id) {
		set("id", id);
	}
	
	public java.lang.String getId() {
		return get("id");
	}
	
	public void setDid(java.lang.String did) {
		set("did", did);
	}

	public java.lang.String getDid() {
		return get("did");
	}

	public void setVersion(java.lang.String version) {
		set("version", version);
	}

	public java.lang.String getVersion() {
		return get("version");
	}

	public void setDname(java.lang.String dname) {
		set("dname", dname);
	}

	public java.lang.String getDname() {
		return get("dname");
	}
	
	public void setModel(java.lang.String model) {
		set("model", model);
	}
	
	public java.lang.String getModel() {
		return get("model");
	}

	public void setType(java.lang.String type) {
		set("type", type);
	}

	public java.lang.String getType() {
		return get("type");
	}
	
	public void setCrttime(java.lang.String crttime) {
		set("crttime", crttime);
	}
	
	public java.lang.String getCrttime() {
		return get("crttime");
	}

}
