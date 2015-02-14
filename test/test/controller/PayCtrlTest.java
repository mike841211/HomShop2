package test.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import test.BaseTest;

import com.homlin.module.AppConstants;

public class PayCtrlTest extends BaseTest {

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
	public void testPay() throws Exception {
		this.mockMvc.perform(
				MockMvcRequestBuilders.get("/payment/upmp.do?type=order&id=4028808b45871c3b0145887da02a0021")
						.sessionAttr(AppConstants.SESSION_MEMBER_ISLOGIN, true)
						.sessionAttr(AppConstants.SESSION_MEMBER_ID, "4028808b45455a8d01454560aa0d0005")
				// .sessionAttr(AppConstants.SESSION_MEMBER_USERNAME, "test")
				// .sessionAttr(AppConstants.SESSION_MEMBER_NAME, "admin")
				// .sessionAttr(AppConstants.SESSION_STATION_ID, "4028808b46b1c7440146b1deebf50001")
				// .param("cid", "4028808b46bc22350146bc30daac0009")
				// .param("pt", "12")
				)
		// .andDo(print())

		;

		trace("OK");
	}

}