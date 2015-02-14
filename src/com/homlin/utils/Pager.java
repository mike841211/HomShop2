package com.homlin.utils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.Assert;

public class Pager implements Serializable {

	private static final long serialVersionUID = 1L;

	// //排序方式
	// public enum OrderType {
	// ASC, DESC
	// }

	public static final Integer MAX_PAGE_SIZE = 500;// 每页最大记录数限制

	private Integer pageIndex = 1; // 当前页码
	private Integer pageSize = 20; // 每页记录数
	private Integer totalCount = 0; // 总记录数
	private Integer pageCount = 0; // 总页数
	// private String property; // 查找属性名称
	// private String keyword; // 查找关键字
	// private String orderBy = "createDate"; // 排序字段
	// private OrderType orderType = OrderType.DESC; // 排序方式
	private List<?> dataList; // 数据List

	private String sql; // 原始查询语句
	private String pagingSql; // 分页查询语句
	private String hql; // HQL查询语句

	// private boolean fastMode = true; // 优化统计 totalCount 语句，复杂的 order by 建议设为false

	private Map<String, Object> params = new HashMap<String, Object>(); // 传参变量，保存传递参数
	private Object[] queryParams = null; // (Object[]) null // 最终查询参数集，按顺序

	// -----

	public Pager() {
	}

	// 不好区分SQL\HQL，setSql链式操作
	// public Pager(Integer pageIndex, Integer pageSize, String sql, Map<String, String> params) {
	// setPageIndex(pageIndex);
	// setPageSize(pageSize);
	// setSql(sql);
	// setParams(params);
	// }
	//
	// public Pager(Integer pageIndex, Integer pageSize, String sql) {
	// setPageIndex(pageIndex);
	// setPageSize(pageSize);
	// setSql(sql);
	// }

	public Pager(Integer pageIndex, Integer pageSize, Map<String, Object> params) {
		setPageIndex(pageIndex);
		setPageSize(pageSize);
		setParams(params);
	}

	public Pager(Integer pageIndex, Integer pageSize) {
		setPageIndex(pageIndex);
		setPageSize(pageSize);
	}

	// -----

	public Integer getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(Integer pageIndex) {
		if (pageIndex < 1) {
			pageIndex = 1;
		}
		this.pageIndex = pageIndex;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		if (pageSize < 1) {
			pageSize = 1;
		} else if (pageSize > MAX_PAGE_SIZE) {
			pageSize = MAX_PAGE_SIZE;
		}
		this.pageSize = pageSize;
	}

	public Integer getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

	public Integer getPageCount() {
		pageCount = totalCount / pageSize;
		if (totalCount % pageSize > 0) {
			pageCount++;
		}
		return pageCount;
	}

	public void setPageCount(Integer pageCount) {
		this.pageCount = pageCount;
	}

	public List<?> getDataList() {
		return dataList;
	}

	public void setDataList(List<?> dataList) {
		this.dataList = dataList;
	}

	public Pager setSql(String sql) {
		this.sql = sql;
		setPagingSql(sql);
		return this;
	}

	/**
	 * 获取原始SQL，注意区分分页pagingSql
	 * 
	 * @return sql
	 */
	public String getSql() {
		return sql;
	}

	/**
	 * MSSQL2005以上，建议通过setSql赋值
	 * 
	 * @param sql
	 * @throws Exception
	 */
	public void setPagingSql(String sql) {
		int order_by_index = sql.toUpperCase().lastIndexOf(" ORDER BY ");
		if (order_by_index < 0 || sql.lastIndexOf(")") > order_by_index || sql.lastIndexOf("'") > order_by_index) { // 确认是查询结果的排序
			order_by_index = -1;
		}
		Assert.isTrue(order_by_index > -1, "分页查询必需指定 ORDER BY 排序条件");
		String orderby = sql.substring(order_by_index + 11);
		// String pagingSql = "SELECT * FROM (SELECT TOP " + getPageSize() * getPage() + " row_number() over(ORDER BY " + orderby
		// + ") RowNumber," + sql.substring(7) + ") #T WHERE RowNumber>" + getPageSize() * (getPage() - 1);
		String pagingSql = "SELECT * FROM (SELECT TOP %s row_number() over(ORDER BY %s) RowNumber,%s) #_T_ WHERE RowNumber>%s ORDER BY RowNumber";
		pagingSql = String.format(pagingSql, getPageSize() * getPageIndex(), orderby, sql.substring(7), getFirstIndex());
		this.pagingSql = pagingSql;
	}

	/**
	 * 获取分页SQL，注意区分原始sql，原始sql将加载全部数据
	 * 
	 * @return pagingSql
	 */
	public String getPagingSql() {
		return pagingSql;
	}

	public Pager setHql(String hql) {
		this.hql = hql;
		return this;
	}

	public String getHql() {
		return hql;
	}

	// public void setFastMode(boolean fastMode) {
	// this.fastMode = fastMode;
	// }
	//
	// public boolean isFastMode() {
	// return fastMode;
	// }

	public void setParams(Map<String, Object> params) {
		this.params = params;
	}

	public Map<String, Object> getParams() {
		return params;
	}

	public void setQueryParams(Object[] queryParams) {
		this.queryParams = queryParams;
	}

	public Object[] getQueryParams() {
		return queryParams;
	}

	/**
	 * 分页开始位置
	 * 
	 * @return
	 */
	public Integer getFirstIndex() {
		return pageSize * (pageIndex - 1);
	}

	/**
	 * 分页结束位置
	 * 
	 * @return
	 */
	public Integer getLastIndex() {
		return pageSize * pageIndex;
	}

	// -----

	/**
	 * 是否有参数
	 */
	public boolean hasParams() {
		return null != this.params && !this.params.isEmpty();
	}

	/**
	 * 指定参数是否存在非NULL值
	 */
	public boolean hasParam(String key) {
		return this.hasParams() && null != this.params.get(key);
	}

	/**
	 * 取得传递参数值
	 * 
	 * @param key
	 * @return
	 */
	public Object getParam(String key) {
		return this.hasParams() ? this.params.get(key) : null;
	}

	// /**
	// * 取得传递参数值，不存在key则返回defaultValue
	// *
	// * @param key
	// * @param defaultValue
	// * @return
	// */
	// public String getStringParam(String key, String defaultValue) {
	// if (this.hasParams()) {
	// Object object = this.params.get(key);
	// if (object != null) {
	// return String.valueOf(object);
	// }
	// }
	// return defaultValue;
	// }
	//
	// /**
	// * 取得传递参数字符串，null返回""
	// *
	// * @param key
	// * @return
	// */
	// public String getStringParam(String key) {
	// return getStringParam(key, "");
	// }
}
