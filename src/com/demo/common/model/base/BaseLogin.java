package com.demo.common.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseLogin<M extends BaseLogin<M>> extends Model<M> implements IBean {

	public void setUid(java.lang.String uid) {
		set("uid", uid);
	}

	public java.lang.String getUid() {
		return get("uid");
	}

	public void setToken(java.lang.String token) {
		set("token", token);
	}

	public java.lang.String getToken() {
		return get("token");
	}

	public void setLogintime(java.util.Date logintime) {
		set("logintime", logintime);
	}

	public java.util.Date getLogintime() {
		return get("logintime");
	}

	public void setImei(java.lang.String imei) {
		set("imei", imei);
	}

	public java.lang.String getImei() {
		return get("imei");
	}

}
