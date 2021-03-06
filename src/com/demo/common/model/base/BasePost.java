package com.demo.common.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BasePost<M extends BasePost<M>> extends Model<M> implements IBean {

	public void setPid(java.lang.String pid) {
		set("pid", pid);
	}
	public java.lang.String getPid() {
		return get("pid");
	}
	
	public void setType(java.lang.String type) {
		set("type", type);
	}
	public java.lang.String getType() {
		return get("type");
	}
	
	public void setStype(java.lang.String stype) {
		set("stype", stype);
	}
	public java.lang.String getStype() {
		return get("stype");
	}

	public void setUid(java.lang.String uid) {
		set("uid", uid);
	}

	public java.lang.String getUid() {
		return get("uid");
	}
	
	public void setCrttime(java.lang.String crttime) {
		set("crttime", crttime);
	}
	
	public java.lang.String getCrttime() {
		return get("crttime");
	}

	public void setTitle(java.lang.String title) {
		set("title", title);
	}

	public java.lang.String getTitle() {
		return get("title");
	}

	public void setContent(java.lang.String content) {
		set("content", content);
	}

	public java.lang.String getContent() {
		return get("content");
	}
	
	public void setImgs(java.lang.String imgs) {
		set("imgs", imgs);
	}
	
	public java.lang.String getImgs() {
		return get("imgs");
	}

	public void setPraisenum(java.lang.String praisenum) {
		set("praisenum", praisenum);
	}

	public java.lang.String getPraisenum() {
		return get("praisenum");
	}

	public void setCommentnum(java.lang.String commentnum) {
		set("commentnum", commentnum);
	}

	public java.lang.String getCommentnum() {
		return get("commentnum");
	}

}
