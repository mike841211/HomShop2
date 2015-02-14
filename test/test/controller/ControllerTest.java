package test.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import org.junit.Before;
import org.junit.Test;

import test.BaseTest;

import com.homlin.module.AppConstants;

public class ControllerTest extends BaseTest {

	// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
	// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
	// import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
	// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
	// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
	// import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

	@Before
	public void setup() throws Exception {
		// this.mockMvc = webAppContextSetup(this.wac).build();
		this.mockMvc = webAppContextSetup(this.wac).alwaysExpect(status().isOk()).build();
	}

	@Test
	public void testHighcharts() throws Exception {
		this.mockMvc.perform(
				get("/admin/highcharts/list.htm")
						.sessionAttr(AppConstants.SESSION_ADMIN_ISLOGIN, true)
						.sessionAttr(AppConstants.SESSION_ADMIN_ISSUPER, true)
						.sessionAttr(AppConstants.SESSION_ADMIN_USERNAME, "admin")
				// .sessionAttr(AppConstants.SESSION_STATION_ID, "4028808b46b1c7440146b1deebf50001")
				// .param("cid", "4028808b46bc22350146bc30daac0009")
				// .param("pt", "12")
				)
		// .andDo(print())

		;

		trace("OK");
	}

	// @Test
	public void test() throws Exception {
		this.mockMvc.perform(
				get("/test.htm")
						.param("cid", "4028808b46bc22350146bc30daac0009")
						.param("pt", "12")
				)
				.andDo(print());

		trace("OK");
	}

	// @Test
	public void w() throws Exception {
		trace("OK3");
	}

	// @Test
	public void q() throws Exception {
		trace("OK2");
	}

	// [[

	// @Test
	public void login() throws Exception {

		this.mockMvc.perform(
				post("/admin/login")
						.param("username", "admin")
						.param("password", "1")
				)
				.andDo(print());

		trace("OK");
	}

	// @Test
	public void login2() throws Exception {

		this.mockMvc.perform(
				get("/eshop/order_list")
						.param("title", "ceshi")
						.param("DEBUG", "1")
				)
				.andDo(print());

		trace("OK");
	}

	// @Test
	public void datalist() throws Exception {
		this.mockMvc.perform(
				get("/member/register.htm")
						.param("title", "ceshi")
						.param("DEBUG", "1")
				)
				.andDo(print());

		trace("OK");
	}

	// ]]

}