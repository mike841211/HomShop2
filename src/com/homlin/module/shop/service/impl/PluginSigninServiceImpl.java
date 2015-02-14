package com.homlin.module.shop.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.homlin.app.service.impl.BaseServiceImpl;
import com.homlin.module.shop.constants.CacheConfigKeys;
import com.homlin.module.shop.dao.TbPluginSigninDao;
import com.homlin.module.shop.execption.SignedException;
import com.homlin.module.shop.model.TbPluginSignin;
import com.homlin.module.shop.model.TbPluginSigninLog;
import com.homlin.module.shop.model.TbShopMember;
import com.homlin.module.shop.model.TbShopMemberScoreLog;
import com.homlin.module.shop.model.TbShopMemberScoreLog.Valtype;
import com.homlin.module.shop.service.PluginSigninService;
import com.homlin.module.shop.util.CacheUtil;
import com.homlin.utils.Util;

@Service
public class PluginSigninServiceImpl extends BaseServiceImpl<TbPluginSignin, String> implements PluginSigninService {

	@Autowired
	public void setBaseDao(TbPluginSigninDao baseDao) {
		super.setBaseDao(baseDao);
	}

	@Autowired
	TbPluginSigninDao tbPluginSigninDao;

	@Override
	public Map<String, Object> signin(TbShopMember tbShopMember) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		BigDecimal _score;
		BigDecimal baseScore = new BigDecimal(CacheUtil.getConfig(CacheConfigKeys.PLUGIN_SIGNIN_BASESCORE));
		BigDecimal dates = new BigDecimal(CacheUtil.getConfig(CacheConfigKeys.PLUGIN_SIGNIN_DATES));
		String datetime = Util.getNowDateTimeString();
		TbPluginSignin tbPluginSignin = tbShopMember.getTbPluginSignin();
		if (tbPluginSignin == null) {
			_score = baseScore;
			tbPluginSignin = new TbPluginSignin();
			tbPluginSignin.setCreateDate(datetime);
			tbPluginSignin.setModifyDate(datetime);
			tbPluginSignin.setDates(1);
			tbPluginSignin.setDatesAddup(1);
			tbPluginSignin.setScoreAddup(baseScore);
			tbPluginSignin.setTbShopMember(tbShopMember);
			tbShopMember.setTbPluginSignin(tbPluginSignin);
		} else {
			// 是否连续签到
			String _lastSigninDateString = tbPluginSignin.getModifyDate().substring(0, 10); // 上次签到日期
			String _yestodayString = Util.getDateString(DateUtils.addDays(new Date(), -1));
			int i = _lastSigninDateString.compareTo(_yestodayString);
			if (i < 0) { // 至少昨天没签到，重头计算
				tbPluginSignin.setDates(1);
				_score = baseScore;
			} else if (i == 0) { // 昨天有签到，连续签到
				Integer _dates = tbPluginSignin.getDates();
				_dates++;
				if (_dates <= dates.intValue()) {
					_score = baseScore.multiply(new BigDecimal(_dates));
				} else {
					_score = baseScore.multiply(new BigDecimal(dates.intValue()));
				}
				tbPluginSignin.setDates(_dates);
			} else { // 已签到
				throw new SignedException();
				// throw new MessageException("今日已签到过！");
			}
			tbPluginSignin.setDatesAddup(tbPluginSignin.getDatesAddup() + 1);
			tbPluginSignin.setScoreAddup(tbPluginSignin.getScoreAddup().add(_score));
			tbPluginSignin.setModifyDate(datetime);
		}

		// 签到记录
		TbPluginSigninLog tbPluginSigninLog = new TbPluginSigninLog();
		tbPluginSigninLog.setScore(_score);
		tbPluginSigninLog.setSignDate(datetime);
		tbPluginSigninLog.setTbShopMember(tbShopMember);
		tbShopMember.getTbPluginSigninLogs().add(tbPluginSigninLog);

		// 会员积分
		tbShopMember.setScore(tbShopMember.getScore().add(_score));
		tbShopMember.setScoreAddup(tbShopMember.getScoreAddup().add(_score));

		// 会员积分记录
		TbShopMemberScoreLog tbShopMemberScoreLog = new TbShopMemberScoreLog();
		tbShopMemberScoreLog.setCreateDate(datetime);
		tbShopMemberScoreLog.setModifyDate(datetime);
		tbShopMemberScoreLog.setTbShopMember(tbShopMember);
		tbShopMemberScoreLog.setVal(_score);
		tbShopMemberScoreLog.setValtype(Valtype.signin);
		tbShopMemberScoreLog.setRemark("签到积分");
		tbShopMember.getTbShopMemberScoreLogs().add(tbShopMemberScoreLog);

		// 返回
		map.put("score", _score);
		return map;
	}

}
