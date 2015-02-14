package com.homlin.module.shop.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.homlin.app.service.impl.BaseServiceImpl;
import com.homlin.module.shop.dao.TbShopProductCommentDao;
import com.homlin.module.shop.model.TbShopProduct;
import com.homlin.module.shop.model.TbShopProductComment;
import com.homlin.module.shop.service.ProductCommentService;
import com.homlin.utils.Pager;

@Service
public class ProductCommentServiceImpl extends BaseServiceImpl<TbShopProductComment, String> implements ProductCommentService {

	@Autowired
	public void setBaseDao(TbShopProductCommentDao baseDao) {
		super.setBaseDao(baseDao);
	}

	@Autowired
	TbShopProductCommentDao tbShopProductCommentDao;

	@Override
	public Pager getPagedList(Pager pager) {
		return tbShopProductCommentDao.getPagedList(pager);
	}

	@Override
	public TbShopProductComment getByOrderItemId(String item_id) {
		return tbShopProductCommentDao.findOneByProperty("tbShopOrderItem.id", item_id);
	}

	@Override
	public String save(TbShopProductComment comment) throws Exception {
		TbShopProduct product = comment.getTbShopProduct();
		if (null != product) {
			Integer scoreCount = product.getScoreCount();
			scoreCount++;
			BigDecimal scoreTotal = product.getScoreTotal();
			scoreTotal = scoreTotal.add(new BigDecimal(comment.getScore()));
			BigDecimal score = scoreTotal.divide(new BigDecimal(scoreCount), 1, RoundingMode.CEILING);
			product.setScoreTotal(scoreTotal);
			product.setScoreCount(scoreCount);
			product.setScore(score);
		}
		return super.save(comment);
	}

	@Override
	public void setBoolean(String[] ids, String prop, String bool) {
		tbShopProductCommentDao.setBoolean(ids, prop, bool);

	}
}
