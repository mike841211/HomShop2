package test;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

/**
 * BaseServiceImpl 开启事务：No Session found for current thread<br>
 * AbstractJUnit4SpringContextTests，直接修改数据库，lazyload 会抛出 LazyInitializationException: could not initialize proxy - no Session<br>
 * AbstractTransactionalJUnit4SpringContextTests，自动回滚，不修改数据库，如果不让它回滚，只需要在test方法上添加@Rollback(false)，可以lazyload
 * 统一使用 AbstractTransactionalJUnit4SpringContextTests
 */
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:/applicationContext.xml", "classpath*:/applicationContext-mvc.xml" })
public class BaseTest extends AbstractTransactionalJUnit4SpringContextTests {

	protected void trace(Object... objects) {
		for (Object object : objects) {
			System.out.println(object);
		}
	}

	@Autowired
	protected WebApplicationContext wac;

	@Autowired
	protected RequestMappingHandlerAdapter handlerAdapter;

	protected MockHttpServletRequest request = new MockHttpServletRequest();
	protected MockHttpServletResponse response = new MockHttpServletResponse();

	protected MockMvc mockMvc;

	@Before
	public void before() {
		trace("\n============ TEST START ============\n");
	}

	@After
	public void after() {
		trace("\n============= TEST END =============\n");
	}

}