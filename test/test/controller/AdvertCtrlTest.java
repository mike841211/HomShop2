package test.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import org.junit.Before;
import org.junit.Test;
import org.springframework.test.annotation.Repeat;
import org.springframework.test.annotation.Rollback;

import test.BaseTest;

import com.homlin.module.AppConstants;

public class AdvertCtrlTest extends BaseTest {

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

	@Repeat(4)
	@Rollback(false)
	@Test
	public void save() throws Exception {

		this.mockMvc.perform(
			post("/admin/ad/save")
					.sessionAttr(AppConstants.SESSION_ADMIN_ISLOGIN, true)
					.sessionAttr(AppConstants.SESSION_ADMIN_ISSUPER, true)
					.sessionAttr(AppConstants.SESSION_ADMIN_USERNAME, "admin")
					// .sessionAttr(AppConstants.SESSION_STATION_ID, "4028808b46b1c7440146b1deebf50001")
					.param("keyword", "bottom")
					.param("title", "ceshi")
					.param("imgpath", "")
					.param("link", "###")
					.param("inuse", "1")
					.param("displayorder", "1")
					.param("body", "sadfasfsafsdfafdsfafsa1")
					.param("remark", "sss")
				)
				.andDo(print());

		trace("OK");
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