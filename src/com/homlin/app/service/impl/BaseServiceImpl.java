package com.homlin.app.service.impl;

import java.io.File;
import java.io.Serializable;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.homlin.app.dao.BaseDao;
import com.homlin.app.service.BaseService;

/**
 * Service实现类 - Service实现类基类
 * 
 * Date:2014 6 26 17:13:43
 * 
 * @version 1.0.0
 * 
 * @author wuduanpiao
 * 
 * @param <T>
 * @param <PK>
 */
/* 
* 事务默认情况下如果方法抛出unchecked异常，则事务回滚，如果抛出的是checked异常，则事务不回滚 
* 如果想要让方法抛出checked异常时也回，则可以按照下面的方法 
* @Transactional(rollbackFor=Exception.class) 
* 也可以指定unchecked异常不进行回滚 
* @Transactional(noRollbackFor=RuntimeException.class) 
*/
@Transactional(rollbackFor = Exception.class)
public abstract class BaseServiceImpl<T, PK extends Serializable> implements BaseService<T, PK> {

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected BaseDao<T, PK> baseDao;

	public BaseDao<T, PK> getBaseDao() {
		if (baseDao == null) {
			throw new IllegalArgumentException("baseDao不能为null >>> " + getClass());
		}
		return baseDao;
	}

	public void setBaseDao(BaseDao<T, PK> baseDao) {
		this.baseDao = baseDao;
	}

	// ===================

	@Override
	public List<T> findAll() {
		return getBaseDao().loadAll();
	}

	@Override
	public boolean exists(String propertyName, Object value) {
		return getBaseDao().exists(propertyName, value);
	}

	@Override
	public PK save(T entity) throws Exception {
		return (PK) getBaseDao().save(entity);
	}

	@Override
	public T get(PK id) {
		return (T) getBaseDao().get(id);
	}

	@Override
	public boolean isUnique(String propertyName, Object oldValue, Object newValue) {
		return getBaseDao().isUnique(propertyName, oldValue, newValue);
	}

	@Override
	public void update(T entity) throws Exception {
		getBaseDao().merge(entity);
	}

	@Override
	public void merge(T entity) throws Exception {
		getBaseDao().merge(entity);
	}

	@Override
	public void delete(T entity) throws Exception {
		getBaseDao().delete(entity);
	}

	@Override
	public void delete(PK id) throws Exception {
		getBaseDao().delete(id);
	}

	@Override
	public void delete(PK[] ids) throws Exception {
		if (ids != null) {
			for (PK id : ids) {
				delete(id);
			}
		}
	}

	// ===================

	@Autowired
	private HttpServletRequest request;

	// @Autowired
	// SystemDao systemDao;

	// 获取Request
	public HttpServletRequest getRequest() {
		return this.request;
	}

	protected void deleteUploadFile(String filepath) {
		if (StringUtils.isBlank(filepath)) {
			return;
		}
		String fullPath = getRequest().getSession().getServletContext().getRealPath(filepath);
		File file = new File(fullPath);
		// Util.trace(filepath, fullPath, file.exists(), file.isFile());
		if (file.exists() && file.isFile()) {
			file.delete();
		}
	}

	protected void deleteUploadFile(String[] filepaths) {
		for (String filepath : filepaths) {
			deleteUploadFile(filepath);
		}
	}

	protected void trace(Object... objects) {
		for (Object object : objects) {
			System.out.println(object);
		}
	}

}