package com.homlin.module.shop.controller.admin;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.homlin.app.annotation.CheckAuthority;
import com.homlin.module.AppConstants;
import com.homlin.module.shop.model.TbShopOrder;
import com.homlin.module.shop.model.TbShopOrder.OrderStatus;
import com.homlin.module.shop.model.TbShopOrder.PaymentStatus;
import com.homlin.module.shop.model.TbShopOrder.ShippingStatus;
import com.homlin.module.shop.model.TbShopOrderItem;
import com.homlin.module.shop.service.OrderService;
import com.homlin.utils.MiniUtil;
import com.homlin.utils.Pager;
import com.homlin.utils.Util;

@Controller("adminOrderController")
@RequestMapping("/admin/order")
public class OrderController extends BaseAdminController {

	private final String RETURN_PATH = "/admin/order";

	@Autowired
	OrderService orderService;

	@CheckAuthority(AppConstants.ORDER)
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list() throws Exception {
		return RETURN_PATH + "/list";
	}

	@CheckAuthority(AppConstants.ORDER)
	@ResponseBody
	@RequestMapping(value = "/datalist", method = RequestMethod.POST)
	public String datalist(@RequestParam(value = "pageIndex", defaultValue = "0") int pageIndex,
			@RequestParam(value = "pageSize", defaultValue = "20") int pageSize) throws Exception {

		Map<String, Object> params = getMiniuiParams("sn", "name", "memberUsername", "paymentCode");

		Pager pager = new Pager(pageIndex + 1, pageSize, params);
		pager = orderService.getPagedList(pager);

		return MiniUtil.getGridJsonData(pager);
	}

	// 查看订单明细
	@CheckAuthority(AppConstants.ORDER)
	@RequestMapping(value = "/view")
	public String orderView(@RequestParam(value = "id", defaultValue = "") String id, Model model) throws Exception {
		TbShopOrder order = orderService.get(id);
		model.addAttribute("order", order);
		return RETURN_PATH + "/view";
	}

	// 修改订单金额
	@CheckAuthority(AppConstants.ORDER)
	@ResponseBody
	@RequestMapping(value = "/updateOrderAmount", method = RequestMethod.POST)
	public String updateOrderAmount(TbShopOrder voOrder) throws Exception {
		TbShopOrder order = orderService.get(voOrder.getId());
		if (order == null) {
			return ajaxJsonErrorMessage("参数错误");
		}
		if (order.getOrderStatus() == OrderStatus.cancelled || order.getOrderStatus() == OrderStatus.completed) {
			return ajaxJsonErrorMessage("当前订单不可编辑！");
		}
		if (order.getPaymentStatus() != PaymentStatus.unpaid) {
			return ajaxJsonErrorMessage("当前订单已付过款！");
		}

		// BigDecimal gainAmount = order.getTotalPrice().add(order.getAdjustAmount())
		// .subtract(order.getCouponAmount()).subtract(order.getUseScoreAmount())
		// .setScale(0, BigDecimal.ROUND_FLOOR);
		// BigDecimal mutiple = order.getGainScore().divide(gainAmount); // 原来获得积分参数 //fixme 0出错,需改为gainScoreMutiple存到数据库

		order.setShippingFee(voOrder.getShippingFee());
		order.setAdjustAmount(voOrder.getAdjustAmount());

		// BigDecimal gainAmount = order.getTotalPrice().add(order.getAdjustAmount())
		// .subtract(order.getCouponAmount()).subtract(order.getUseScoreAmount());
		BigDecimal gainAmount = order.getGainScoreAmount();
		gainAmount = gainAmount.setScale(0, BigDecimal.ROUND_FLOOR);
		BigDecimal mutiple = order.getGainScoreMutiple(); // 原来获得积分参数
		order.setGainScore(gainAmount.multiply(mutiple));

		order.setTotalAmount(order.getTotalPrice().add(order.getAdjustAmount()).add(order.getShippingFee())); // .add(order.getPaymentFee())
		order.setModifyDate(Util.getNowDateTimeString());

		orderService.update(order);
		actionlog("修改订单", "修改订单金额，单号：" + order.getSn());
		return ajaxJsonSuccessMessage();
	}

	// 修改发货信息
	@CheckAuthority(AppConstants.ORDER)
	@ResponseBody
	@RequestMapping(value = "/updateShippingInfo", method = RequestMethod.POST)
	public String updateShippingInfo(TbShopOrder voOrder) throws Exception {
		TbShopOrder order = orderService.get(voOrder.getId());
		if (order == null) {
			return ajaxJsonErrorMessage("参数错误");
		}
		if (order.getOrderStatus() == OrderStatus.cancelled || order.getOrderStatus() == OrderStatus.completed) {
			return ajaxJsonErrorMessage("当前订单不可编辑！");
		}
		if (order.getShippingStatus() != ShippingStatus.unshipped) {
			return ajaxJsonErrorMessage("当前订单已发货！");
		}

		order.setName(voOrder.getName());
		order.setMobile(voOrder.getMobile());
		order.setPhone(voOrder.getPhone());
		order.setAddress(voOrder.getAddress()); // areaCode???
		order.setShippingCompany(voOrder.getShippingCompany());
		order.setShippingCode(voOrder.getShippingCode());
		order.setModifyDate(Util.getNowDateTimeString());

		orderService.update(order);
		actionlog("修改订单", "修改发货信息，单号：" + order.getSn());
		return ajaxJsonSuccessMessage();
	}

	// 修改卖家备注
	@CheckAuthority(AppConstants.ORDER)
	@ResponseBody
	@RequestMapping(value = "/updateSalerRemark", method = RequestMethod.POST)
	public String updateSalerRemark(TbShopOrder voOrder) throws Exception {
		TbShopOrder order = orderService.get(voOrder.getId());
		if (order == null) {
			return ajaxJsonErrorMessage("参数错误");
		}

		order.setSalerRemark(voOrder.getSalerRemark());
		order.setModifyDate(Util.getNowDateTimeString());

		orderService.update(order);
		actionlog("修改订单", "修改卖家备注，单号：" + order.getSn());
		return ajaxJsonSuccessMessage();
	}

	// 确认订单信息
	@CheckAuthority(AppConstants.ORDER)
	@ResponseBody
	@RequestMapping(value = "/confirmed", method = RequestMethod.POST)
	public String confirmed(String id, OrderStatus status) throws Exception {
		if (StringUtils.isBlank(id)) {
			return ajaxJsonErrorMessage("参数错误");
		}

		TbShopOrder order = orderService.get(id);
		if (order == null) {
			return ajaxJsonErrorMessage("参数错误");
		}
		if (order.getOrderStatus() != OrderStatus.unconfirmed) {
			return ajaxJsonErrorMessage("当前订单已处理过！");
		}
		orderService.confirmed(order);
		actionlog("修改订单", "确认订单，单号：" + order.getSn());
		return ajaxJsonSuccessMessage();
	}

	// 取消订单
	@CheckAuthority(AppConstants.ORDER)
	@ResponseBody
	@RequestMapping(value = "/cancelled", method = RequestMethod.POST)
	public String cancelled(String id, OrderStatus status) throws Exception {
		if (StringUtils.isBlank(id)) {
			return ajaxJsonErrorMessage("参数错误");
		}

		TbShopOrder order = orderService.get(id);
		if (order == null) {
			return ajaxJsonErrorMessage("参数错误");
		}
		if (order.getOrderStatus() != OrderStatus.unconfirmed) {
			return ajaxJsonErrorMessage("当前订单已处理过！");
		}

		orderService.cancelled(order);
		actionlog("修改订单", "取消订单，单号：" + order.getSn());
		return ajaxJsonSuccessMessage();
	}

	// 确认已收款
	@CheckAuthority(AppConstants.ORDER)
	@ResponseBody
	@RequestMapping(value = "/paid", method = RequestMethod.POST)
	public String paid(String id, PaymentStatus status) throws Exception {
		if (StringUtils.isBlank(id)) {
			return ajaxJsonErrorMessage("参数错误");
		}

		TbShopOrder order = orderService.get(id);
		if (order == null) {
			return ajaxJsonErrorMessage("参数错误");
		}
		if (order.getOrderStatus() == OrderStatus.cancelled || order.getOrderStatus() == OrderStatus.completed) {
			return ajaxJsonErrorMessage("当前订单不可编辑！");
		}
		if (order.getPaymentStatus() != PaymentStatus.unpaid) {
			return ajaxJsonErrorMessage("当前订单已付过款！");
		}

		orderService.paid(order);
		actionlog("修改订单", "确认收到货款，单号：" + order.getSn());
		return ajaxJsonSuccessMessage();
	}

	// 确认已发货
	@CheckAuthority(AppConstants.ORDER)
	@ResponseBody
	@RequestMapping(value = "/shipped", method = RequestMethod.POST)
	public String shipped(String id, ShippingStatus status) throws Exception {
		if (StringUtils.isBlank(id)) {
			return ajaxJsonErrorMessage("参数错误");
		}

		TbShopOrder order = orderService.get(id);
		if (order == null) {
			return ajaxJsonErrorMessage("参数错误");
		}
		if (order.getOrderStatus() == OrderStatus.cancelled || order.getOrderStatus() == OrderStatus.completed) {
			return ajaxJsonErrorMessage("当前订单不可编辑！");
		}
		// todo 是否要求付款才发货
		// if (order.getPaymentStatus() != PaymentStatus.paid) {
		// return ajaxJsonErrorMessage("当前订单未付款！");
		// }
		if (order.getShippingStatus() != ShippingStatus.unshipped) {
			return ajaxJsonErrorMessage("当前订单已发过货！");
		}

		// todo 是否发货前判断库存

		orderService.shipped(order);
		actionlog("修改订单", "确认订单已发货，单号：" + order.getSn());
		return ajaxJsonSuccessMessage();
	}

	// 确认订单已完成
	@CheckAuthority(AppConstants.ORDER)
	@ResponseBody
	@RequestMapping(value = "/completed", method = RequestMethod.POST)
	public String completed(String id, OrderStatus status) throws Exception {
		if (StringUtils.isBlank(id)) {
			return ajaxJsonErrorMessage("参数错误");
		}

		TbShopOrder order = orderService.get(id);
		if (order == null) {
			return ajaxJsonErrorMessage("参数错误");
		}
		if (order.getOrderStatus() == OrderStatus.cancelled || order.getOrderStatus() == OrderStatus.completed) {
			return ajaxJsonErrorMessage("当前订单不可编辑！");
		}
		if (order.getPaymentStatus() == PaymentStatus.unpaid || order.getPaymentStatus() == PaymentStatus.partialPayment) {
			return ajaxJsonErrorMessage("当前订单有未付款！");
		}
		if (order.getShippingStatus() == ShippingStatus.unshipped || order.getShippingStatus() == ShippingStatus.partialShipment) {
			return ajaxJsonErrorMessage("当前订单未全部发货！");
		}

		orderService.completed(order);
		actionlog("修改订单", "确认订单完成，单号：" + order.getSn());
		return ajaxJsonSuccessMessage();
	}

	// 删除订单
	@CheckAuthority(AppConstants.ORDER)
	@ResponseBody
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public String delete(String ids) throws Exception {

		// 暂时只单个删除

		if (StringUtils.isBlank(ids)) {
			return ajaxJsonErrorMessage("参数ID错误");
		}
		TbShopOrder order = orderService.get(ids);
		if (order == null) {
			return ajaxJsonErrorMessage("参数错误");
		}
		if (order.getOrderStatus() != OrderStatus.cancelled) {
			return ajaxJsonErrorMessage("只能删除已取消的订单！");
		}

		orderService.delete(order);
		actionlog("删除订单", "单号：" + order.getSn());
		return ajaxJsonSuccessMessage();
	}

	/**
	 * 导出订单
	 * 
	 * @throws Exception
	 */
	@RequestMapping(value = "/export")
	public String export(HttpServletResponse response) throws Exception {

		Map<String, Object> params = getMiniuiParams("sn", "name", "memberUsername", "createDate1", "createDate2", "sn1", "sn2");

		if (params.size() == 0) {
			addActionError("没有指定条件");
			return PAGE_ERROR;
		}

		List<TbShopOrder> list = orderService.getExportDataList(params);

		if (list == null || list.size() == 0) {
			addActionError("没有找到数据");
			return PAGE_ERROR;
		}

		// 输出文件
		try {
			File file = new File(System.getProperty("java.io.tmpdir") + "/" + UUID.randomUUID() + ".xls");
			// 打开文件
			WritableWorkbook book = Workbook.createWorkbook(file);
			// 生成名为“第一页”的工作表，参数0表示这是第一页
			WritableSheet sheet = book.createSheet("Sheet1", 0);

			WritableFont font1 = new WritableFont(WritableFont.createFont("宋体"), 20, WritableFont.BOLD);
			jxl.write.WritableCellFormat titleFormat1 = new jxl.write.WritableCellFormat(font1);
			titleFormat1.setAlignment(Alignment.CENTRE);
			titleFormat1.setVerticalAlignment(VerticalAlignment.CENTRE);
			titleFormat1.setBorder(Border.TOP, BorderLineStyle.THICK);

			// 标题样式
			jxl.write.WritableCellFormat titleFormat = new jxl.write.WritableCellFormat();
			titleFormat.setBorder(Border.ALL, BorderLineStyle.THIN); // 线条
			titleFormat.setVerticalAlignment(VerticalAlignment.CENTRE); // 垂直对齐
			titleFormat.setAlignment(Alignment.CENTRE); // 水平对齐
			titleFormat.setWrap(true); // 是否换行
			titleFormat.setBackground(Colour.GRAY_25);// 背景色暗灰-25%

			int r = 0;
			for (TbShopOrder order : list) {
				sheet.setRowView(r, 600);
				sheet.mergeCells(0, r, 6, r);
				sheet.addCell(new Label(0, r, "销售订单", titleFormat1));

				r++;
				sheet.setRowView(r, 300);
				sheet.addCell(new Label(0, r, "订单号"));
				sheet.addCell(new Label(1, r, order.getSn()));
				if (StringUtils.isNotEmpty(order.getMemberUsername())) {
					sheet.addCell(new Label(2, r, "会员号"));
					sheet.addCell(new Label(3, r, order.getMemberUsername()));
				}
				sheet.addCell(new Label(4, r, "下单时间"));
				sheet.mergeCells(5, r, 6, r);
				sheet.addCell(new Label(5, r, order.getCreateDate()));

				r++;
				sheet.setRowView(r, 300);
				sheet.mergeCells(0, r, 6, r);
				sheet.addCell(new Label(0, r, "配送信息", titleFormat));

				r++;
				sheet.setRowView(r, 300);
				sheet.mergeCells(0, r, 6, r);
				sheet.addCell(new Label(0, r, order.getName() + " " + order.getMobile() + " " + order.getPhone()));
				// sheet.addCell(new Label(1, r, order.getMobile()));
				// sheet.addCell(new Label(2, r, order.getPhone()));

				r++;
				sheet.setRowView(r, 300);
				sheet.mergeCells(0, r, 6, r);
				sheet.addCell(new Label(0, r, order.getAddress()));

				r++;
				sheet.setRowView(r, 300);
				sheet.mergeCells(0, r, 6, r);
				sheet.addCell(new Label(0, r, "备注：" + order.getBuyerRemark()));

				r++;
				sheet.setRowView(r, 300);
				sheet.addCell(new Label(0, r, "序号", titleFormat));
				sheet.addCell(new Label(1, r, "货号", titleFormat));
				sheet.addCell(new Label(2, r, "品名", titleFormat));
				sheet.addCell(new Label(3, r, "规格", titleFormat));
				sheet.addCell(new Label(4, r, "单价", titleFormat));
				sheet.addCell(new Label(5, r, "数量", titleFormat));
				sheet.addCell(new Label(6, r, "金额", titleFormat));

				sheet.setColumnView(1, 10);
				sheet.setColumnView(2, 30);
				sheet.setColumnView(3, 15);
				Set<TbShopOrderItem> items = order.getTbShopOrderItems();
				int index = 1;
				for (TbShopOrderItem item : items) {
					r++;
					sheet.setRowView(r, 300);
					sheet.addCell(new Label(0, r, String.valueOf(index++)));
					sheet.addCell(new Label(1, r, item.getSn()));
					sheet.addCell(new Label(2, r, item.getName()));
					sheet.addCell(new Label(3, r, item.getSpecifications()));
					// sheet.addCell(new Label(4, r, item.getPrice().toString()));
					sheet.addCell(new jxl.write.Number(4, r, item.getPrice().doubleValue()));
					// sheet.addCell(new Label(5, r, item.getQuantity().toString()));
					sheet.addCell(new jxl.write.Number(5, r, item.getQuantity()));
					// sheet.addCell(new Label(6, r, item.getPrice().multiply(new BigDecimal(item.getQuantity())).toString()));
					sheet.addCell(new jxl.write.Number(6, r, item.getPrice().multiply(new BigDecimal(item.getQuantity())).doubleValue()));
				}

				r++;
				sheet.setRowView(r, 300);
				sheet.addCell(new Label(5, r, "商品合计"));
				// sheet.addCell(new Label(6, r, order.getTotalAmount().subtract(order.getShippingFee()).toString()));
				sheet.addCell(new jxl.write.Number(6, r, order.getTotalAmount().subtract(order.getShippingFee()).doubleValue()));

				r++;
				sheet.setRowView(r, 300);
				sheet.addCell(new Label(0, r, "配送方式"));
				sheet.addCell(new Label(1, r, order.getShippingMethod()));
				sheet.addCell(new Label(5, r, "运费"));
				// sheet.addCell(new Label(6, r, order.getShippingFee().toString()));
				sheet.addCell(new jxl.write.Number(6, r, order.getShippingFee().doubleValue()));

				r++;
				sheet.setRowView(r, 300);
				sheet.addCell(new Label(5, r, "订单总额"));
				// sheet.addCell(new Label(6, r, order.getTotalAmount().toString()));
				sheet.addCell(new jxl.write.Number(6, r, order.getTotalAmount().doubleValue()));

				r++;
				r++;
				r++;
			}

			// 写入数据并关闭文件
			book.write();
			book.close();

			// --------
			// 下载文件
			String fileName = "商品档案" + Calendar.getInstance().getTimeInMillis() + ".xls"; // 文件的默认保存名
			fileName = new String(fileName.getBytes("gbk"), "iso8859-1"); // 中文问题
			// 读到流中
			InputStream inStream = new FileInputStream(file);
			// 设置输出的格式
			response.reset();
			response.setContentType("bin");
			response.addHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
			// 循环取出流中的数据
			byte[] b = new byte[4096];
			int len;
			try {
				while ((len = inStream.read(b)) > 0) {
					response.getOutputStream().write(b, 0, len);
				}
				inStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			// --- 删除临时文件
			file.delete();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
