package com.homlin.module.shop.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.homlin.app.dao.impl.BaseDaoImpl;
import com.homlin.module.shop.dao.TbWxMsgDao;
import com.homlin.module.shop.model.TbWxMsg;
import com.homlin.utils.HqlHelper;
import com.homlin.utils.Pager;

@Repository
public class TbWxMsgDaoImpl extends BaseDaoImpl<TbWxMsg, String> implements TbWxMsgDao {

	@Override
	public Pager getPagedList(Pager pager) {
		String sql = "select ";
		sql += HqlHelper
				.mapping("a.content,a.createTime,a.description,a.format,a.fromUserName,a.label,a.location_X as locationX,a.location_Y as locationY,a.mediaId,a.msgId,a.msgType,a.picUrl,a.recognition,a.scale,a.thumbMediaId,a.title,a.toUserName,a.url,a.lastreply", "a");
		sql += ",b.nickname as fromUserNameText";
		sql += " from Tb_Wx_Msg as a left join Tb_Wx_User as b on a.fromUserName=b.openid ";
		// 条件
		Map<String, Object> params = pager.getParams();
		if (pager.hasParams()) {
			sql += " where 1=1";
			List<Object> queryParams = new ArrayList<Object>();
			String key, value;
			key = "nickname";
			if (params.containsKey(key)) {
				value = params.get(key).toString();
				sql += " and b.nickname like ?";
				queryParams.add("%" + value + "%");
			}
			key = "content";
			if (params.containsKey(key)) {
				value = params.get(key).toString();
				sql += " and a.content like ?";
				queryParams.add("%" + value + "%");
			}
			pager.setQueryParams(queryParams.toArray());
		}
		sql += " order by a.createTime desc";

		pager.setSql(sql);
		return findByPageSql(pager);
	}

}
