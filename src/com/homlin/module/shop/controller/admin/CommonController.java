package com.homlin.module.shop.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.homlin.app.SpringContextHolder;
import com.homlin.module.shop.dao.TbShopChinaAreaDao;

@Controller
@RequestMapping("/admin/common")
public class CommonController extends BaseAdminController {

	private final String RETURN_PATH = "/admin/common";

	@RequestMapping(value = "/uploadImage", method = RequestMethod.GET)
	public String uploadImages() throws Exception {
		return RETURN_PATH + "/uploadImage";
	}

	@RequestMapping(value = "/uploadImages", method = RequestMethod.GET)
	public String uploadImage() throws Exception {
		return RETURN_PATH + "/uploadImages";
	}


	/**
	 * 修复表级别索引：idIndex
	 * 
	 * @param model
	 *            表对应实体名
	 * @param parent
	 *            父对象属性
	 * @param id
	 *            ：id属性
	 * @param idIndex
	 *            ：id索引属性
	 * 
	 * <br>
	 *            CODE:<code>fixIdIndex("TbCategory","tbCategory","id","idIndex")</code>
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/fixIdIndex", method = RequestMethod.POST)
	public String fixIdIndex(@RequestParam(value = "model", required = true) String model,
			@RequestParam(value = "parent", required = true) String parent, @RequestParam(value = "id", required = true) String id,
			@RequestParam(value = "idIndex", required = true) String idIndex) throws Exception {
		// 任意DAO
		SpringContextHolder.getBean(TbShopChinaAreaDao.class).fixIdIndex(model, parent, id, idIndex);
		return ajaxJsonSuccessMessage("操作成功");
	}

}
