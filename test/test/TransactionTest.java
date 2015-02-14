package test;

import org.junit.Test;

public class TransactionTest extends BaseTest {

	// @Autowired
	// WjwPolicemanServiceImpl impl;

	// @Autowired
	// TbWjwPolicemanDaoImpl daoImpl;

	@Test
	public void testDaoTransaction() throws Exception {
		// daoImpl.test();
		// 不通过service直接调用Dao的事物处理
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
	}

	@Test
	public void testServiceTransaction() throws Exception {
		// 当前框架service已配置事物，可直接调用
		// serviceImpl.test();
	}

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
