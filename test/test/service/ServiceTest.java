package test.service;

import org.junit.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import test.BaseTest;

@Transactional
public class ServiceTest extends BaseTest {

	// @Autowired
	// WjwPolicemanServiceImpl impl;

	// @Autowired
	// TbWjwPolicemanDaoImpl daoImpl;

	// @Autowired
	// WjwStationService impl;

	@Test
	@Rollback(false)
	public void test1() throws Exception {
		// daoImpl.test();
		// impl.get("4028808b46b1c7440146b1deebf50001").getTbWjwFeedbacks().size();
		// TbWjwStation station = impl.get("4028808b46b1c7440146b1deebf50001");
		// station.setAddress("1700s");
		// impl.update(station);
		// Set set = impl.get("4028808b46b1c7440146b1deebf50001").getTbWjwFeedbacks();
		// System.out.println(set.size());
	}

	/*
	public void test() {
		new TransactionTemplate(SpringContextHolder.getBean(HibernateTransactionManager.class)).execute(new TransactionCallback<Object>() {
			@Override
			public Object doInTransaction(TransactionStatus status) {
				getJdbcTemplate().update("update tb_wjw_policeman set displayorder=6");
				getJdbcTemplate().update("update tb_wjw_policeman2 set displayorder=1");
				return null;
			}
		});
	}
	*/

	protected void trace(Object... objects) {
		for (Object object : objects) {
			System.out.println(object);
		}
	}

	@Test
	public void q() throws Exception {
		trace("OK2");
	}
}
