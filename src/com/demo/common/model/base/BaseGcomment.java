package com.demo.common.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseGcomment<M extends BaseGcomment<M>> extends Model<M> implements IBean {

	public void setCid(java.lang.String cid) {
		set("cid", cid);
	}

	public java.lang.String getCid() {
		return get("cid");
	}

	public void setCcontent(java.lang.String ccontent) {
		set("ccontent", ccontent);
	}

	public java.lang.String getCcontent() {
		return get("ccontent");
	}

	public void setCtype(java.lang.String ctype) {
		set("ctype", ctype);
	}

	public java.lang.String getCtype() {
		return get("ctype");
	}

	public void setType(java.lang.String type) {
		set("type", type);
	}

	public java.lang.String getType() {
		return get("type");
	}

	public void setImgs(java.lang.String imgs) {
		set("imgs", imgs);
	}

	public java.lang.String getImgs() {
		return get("imgs");
	}

	public void setCrttime(java.lang.String crttime) {
		set("crttime", crttime);
	}

	public java.lang.String getCrttime() {
		return get("crttime");
	}

	public void setClv(java.lang.String clv) {
		set("clv", clv);
	}

	public java.lang.String getClv() {
		return get("clv");
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
	public void setOid(java.lang.String oid) {
		set("oid", oid);
	}
	
	public java.lang.String getOid() {
		return get("oid");
	}
	
	public void setReid(java.lang.String reid) {
		set("reid", reid);
	}
	
	public java.lang.String getReid() {
		return get("reid");
	}
	public void setGcommenttype(java.lang.String gcommenttype) {
		set("gcommenttype", gcommenttype);
	}
	
	public java.lang.String getGcommenttype() {
		return get("gcommenttype");
	}
}
