package com.demo.common.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseCarpre<M extends BaseCarpre<M>> extends Model<M> implements IBean {

	public void setPreid(java.lang.String preid) {
		set("preid", preid);
	}

	public java.lang.String getPreid() {
		return get("preid");
	}

	public void setName(java.lang.String name) {
		set("name", name);
	}

	public java.lang.String getName() {
		return get("name");
	}

	public void setPhone(java.lang.String phone) {
		set("phone", phone);
	}

	public java.lang.String getPhone() {
		return get("phone");
	}

	public void setGoodid(java.lang.String goodid) {
		set("goodid", goodid);
	}

	public java.lang.String getGoodid() {
		return get("goodid");
	}

	public void setUid(java.lang.String uid) {
		set("uid", uid);
	}

	public java.lang.String getUid() {
		return get("uid");
	}

}