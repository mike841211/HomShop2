package test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import org.junit.Before;
import org.junit.Test;

import test.BaseTest;

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
	public void xxxxx() throws Exception {
		this.mockMvc.perform(
				get("/xxxxx.htm")
						.param("title", "ceshi")
						.param("DEBUG", "1")
				)
				.andDo(print());

		trace("OK");
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