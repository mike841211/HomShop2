package com.homlin.app.service;

import java.io.Serializable;
import java.util.List;

/**
 * Service接口 - Service接口基类
 */
public interface BaseService<T, PK extends Serializable> {

	/**
	 * 获取所有实体对象集合.
	 * 
	 * @return 实体对象集合
	 */
	public List<T> findAll();

	/**
	 * 根据属性名判断数据是否已存在.
	 * 
	 * @param propertyName
	 *            属性名称
	 * @param value
	 *            值
	 * @return boolean
	 */
	public boolean exists(String propertyName, Object value);

	/**
	 * 保存实体对象.
	 * 
	 * @param entity
	 *            对象
	 * @return ID
	 * @throws Exception
	 */
	public PK save(T entity) throws Exception;

	/**
	 * 根据ID获取实体对象.
	 * 
	 * @param id
	 *            记录ID
	 * @return 实体对象
	 */
	public T get(PK id);

	/**
	 * 根据属性名、修改前后属性值判断在数据库中是否唯一(若新修改的值与原来值相等则直接返回true).
	 * 
	 * @param propertyName
	 *            属性名称
	 * @param oldValue
	 *            修改前的属性值
	 * @param oldValue
	 *            修改后的属性值
	 * @return boolean
	 */
	public boolean isUnique(String propertyName, Object oldValue, Object newValue);

	/**
	 * 合并实体对象.
	 * 
	 * @param entity
	 *            对象
	 * @throws Exception
	 */
	public void merge(T entity) throws Exception;

	/**
	 * 更新实体对象.
	 * 
	 * @param entity
	 *            对象
	 * @throws Exception
	 */
	public void update(T entity) throws Exception;

	/**
	 * 删除实体对象.
	 * 
	 * @param entity
	 *            对象
	 * @return
	 * @throws Exception
	 */
	public void delete(T entity) throws Exception;

	/**
	 * 根据ID删除实体对象.
	 * 
	 * @param id
	 *            记录ID
	 * @throws Exception
	 */
	public void delete(PK id) throws Exception;

	/**
	 * 根据ID数组删除实体对象.
	 * 
	 * @param ids
	 *            ID数组
	 * @throws Exception
	 */
	public void delete(PK[] ids) throws Exception;

}
