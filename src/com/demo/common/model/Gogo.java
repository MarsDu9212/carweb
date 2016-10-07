package com.demo.common.model;

import com.demo.common.model.base.BaseGogo;
import com.jfinal.plugin.activerecord.Page;
import com.mysql.jdbc.StringUtils;

public class Gogo extends BaseGogo<Gogo>{
	public static final Gogo dao = new Gogo();

	public Page<Gogo> searchGoodsPage1(int pageNum, int pageSize, String content, String orderCond) {
		if (pageNum < 1) {
			pageNum = 1;
		}

		StringBuilder sql = new StringBuilder();
		sql.append(" from t_service g left join t_gogo t on g.id=t.storeid where t.storeid in ");
		sql.append(" (select storeid from t_gogo where loss like '%" + content + "%')");
//		if (!StringUtils.isEmptyOrWhitespaceOnly(orderCond)) {
//			sql.append(" order by "+orderCond+"");
//		}
		Page<Gogo> page = Gogo.dao.paginate(pageNum, pageSize, "select g.type ,t.* ", sql.toString());
		int totalPage = page.getTotalPage();
		if (totalPage < 1) {
			totalPage = 1;
		}
		if (pageNum > totalPage) {
			pageNum = totalPage;
		}

		return page;
	}

}
