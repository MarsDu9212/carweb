package com.demo.common.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseCars<M extends BaseCars<M>> extends Model<M> implements IBean {

	public void setCid(java.lang.String cid) {
		set("cid", cid);
	}

	public java.lang.String getCid() {
		return get("cid");
	}
	
	public void setUid(java.lang.String uid) {
		set("uid", uid);
	}
	
	public java.lang.String getUid() {
		return get("uid");
	}
	
	public void setUname(java.lang.String uname) {
		set("uname", uname);
	}
	
	public java.lang.String getUname() {
		return get("uname");
	}

	public void setDriver(java.lang.String driver) {
		set("driver", driver);
	}

	public java.lang.String getDriver() {
		return get("driver");
	}

	public void setStartadd(java.lang.String startadd) {
		set("startadd", startadd);
	}

	public java.lang.String getStartadd() {
		return get("startadd");
	}

	public void setTargetadd(java.lang.String targetadd) {
		set("targetadd", targetadd);
	}

	public java.lang.String getTargetadd() {
		return get("targetadd");
	}

	public void setCarmodel(java.lang.String carmodel) {
		set("carmodel", carmodel);
	}

	public java.lang.String getCarmodel() {
		return get("carmodel");
	}

	public void setLoad(java.lang.String load) {
		set("load", load);
	}

	public java.lang.String getLoad() {
		return get("load");
	}

	public void setCnote(java.lang.String cnote) {
		set("cnote", cnote);
	}

	public java.lang.String getCnote() {
		return get("cnote");
	}

	public void setCphone(java.lang.String cphone) {
		set("cphone", cphone);
	}

	public java.lang.String getCphone() {
		return get("cphone");
	}

	public void setPlatenum(java.lang.String platenum) {
		set("platenum", platenum);
	}

	public java.lang.String getPlatenum() {
		return get("platenum");
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
	
	public void setResult(java.lang.String result) {
		set("result", result);
	}
	
	public java.lang.String getResult() {
		return get("result");
	}
	
	public void setCoord(java.lang.String coord) {
		set("coord", coord);
	}
	
	public java.lang.String getCoord() {
		return get("coord");
	}

	public void setRname(java.lang.String rname) {
		set("rname", rname);
	}
	
	public java.lang.String getRname() {
		return get("rname");
	}
}