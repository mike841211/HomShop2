package com.homlin.app.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;

import com.homlin.utils.Pager;

public interface BaseDao<T, PK extends Serializable> {

	// public Session getSession();

	// -------------------- 基本检索、增加、修改、删除操作 --------------------

	/**
	 * 根据主键获取实体。如果没有相应的实体，返回 null。
	 */
	public T get(PK id);

	// /**
	// * 根据主键获取实体并加锁。如果没有相应的实体，返回 null。
	// */
	// public T getWithLock(PK id, LockMode lock);

	/**
	 * 根据主键获取实体。如果没有相应的实体，抛出异常。
	 */
	public T load(PK id);

	// /**
	// * 根据主键获取实体并加锁。如果没有相应的实体，抛出异常。
	// */
	// public T loadWithLock(PK id, LockMode lock);

	/**
	 * 获取全部实体。
	 */
	public List<T> loadAll();

	// // loadAllWithLock() ?

	/**
	 * 更新实体
	 */
	public void update(T entity);

	// /**
	// * 更新实体并加锁
	// */
	// public void updateWithLock(T entity, LockMode lock);

	/**
	 * 存储实体到数据库
	 */
	public PK save(T entity);

	public void persist(T entity);

	// // saveWithLock()

	/**
	 * 增加或更新实体
	 */
	public void saveOrUpdate(T entity);

	// /**
	// * 增加或更新集合中的全部实体
	// */
	// public void saveOrUpdateAll(Collection<T> entities);

	/**
	 * 删除指定的实体
	 */
	public void delete(T entity);

	// /**
	// * 加锁并删除指定的实体
	// */
	// public void deleteWithLock(T entity, LockMode lock);

	/**
	 * 根据主键删除指定实体
	 */
	public void delete(PK id);

	// /**
	// * 根据主键加锁并删除指定的实体
	// */
	// public void deleteByKeyWithLock(PK id, LockMode lock);
	//
	// /**
	// * 删除集合中的全部实体
	// */
	// public void deleteAll(Collection<T> entities);

	/**
	 * 合并
	 */
	public T merge(T entity);

	// -------------------- HQL -----------------------------

	/**
	 * 使用HSQL语句直接增加、更新、删除实体
	 */
	public int update(String hql);

	/**
	 * 使用带参数的HSQL语句增加、更新、删除实体
	 */
	public int update(String hql, Object... params);

	/**
	 * 使用带参数的SQL语句增加、更新、删除实体
	 */
	public int updateBySql(String sql, Object... params);

	// /**
	// * 使用HSQL语句检索数据
	// */
	// public List<?> find(String hql);

	/**
	 * 使用带参数的HSQL语句检索数据
	 */
	public List<?> find(String hql, Object... params);

	// public Object find(String hql, int maxResults);

	public List<?> findTop(String hql, int maxResults, Object... params);

	/**
	 * 使用带参数的SQL语句检索数据
	 */
	public List<?> findBySql(String sql, Object... params);

	/**
	 * 使用带参数的SQL语句检索数据返回MAP
	 */
	public List<?> findMapBySql(String sql, Object... params);

	/**
	 * return query.uniqueResult();
	 * 
	 * @param hql
	 * @return
	 */
	public Object findOne(String hql);

	/**
	 * return query.uniqueResult();
	 * 
	 * @param hql
	 * @param params
	 * @return
	 */
	public Object findOne(String hql, Object... params);

	public List<T> findByProperty(String[] propertyNames, Object[] values);

	public List<T> findByProperty(String propertyName, Object value);

	/**
	 * return query.uniqueResult();
	 * 
	 * @param propertyNames
	 * @param values
	 * @return
	 */
	public T findOneByProperty(String[] propertyNames, Object[] values);

	/**
	 * return query.uniqueResult();
	 * 
	 * @param propertyName
	 * @param value
	 * @return
	 */
	public T findOneByProperty(String propertyName, Object value);

	// /**
	// * 使用带命名的参数的HSQL语句检索数据
	// */
	// public List findByNamedParam(String queryString, String[] paramNames, Object[] values);
	//
	// /**
	// * 使用命名的HSQL语句检索数据
	// */
	// public List findByNamedQuery(String queryName);
	//
	// /**
	// * 使用带参数的命名HSQL语句检索数据
	// */
	// public List findByNamedQuery(String queryName, Object[] values);
	//
	// /**
	// * 使用带命名参数的命名HSQL语句检索数据
	// */
	// public List findByNamedQueryAndNamedParam(String queryName, String[] paramNames, Object[] values);
	//
	// /**
	// * 使用HSQL语句检索数据，返回 Iterator
	// */
	// public Iterator iterate(String queryString);
	//
	// /**
	// * 使用带参数HSQL语句检索数据，返回 Iterator
	// */
	// public Iterator iterate(String queryString, Object[] values);
	//
	// /**
	// * 关闭检索返回的 Iterator
	// */
	// public void closeIterator(Iterator it);
	//
	// -------------------------------- Criteria ------------------------------

	/**
	 * 创建与会话无关的检索标准对象
	 */
	public DetachedCriteria createDetachedCriteria();

	/**
	 * 创建与会话绑定的检索标准对象
	 */
	public Criteria createCriteria();

	/**
	 * 使用指定的检索标准检索数据
	 */
	public List<T> findByCriteria(DetachedCriteria criteria);

	/**
	 * 使用指定的检索标准检索数据，返回部分记录
	 */
	public List<T> findByCriteria(DetachedCriteria criteria, int firstResult, int maxResults);

	/**
	 * 使用指定的实体及属性检索（满足除主键外属性＝实体值）数据
	 */
	public List<T> findEqualByEntity(T entity, String[] propertyNames);

	/**
	 * 使用指定的实体及属性(非主键)检索（满足属性 like 串实体值）数据
	 */
	public List<T> findLikeByEntity(T entity, String[] propertyNames);

	/**
	 * 使用指定的检索标准检索数据，返回指定范围的记录
	 */
	public Integer getRowCount(DetachedCriteria criteria);

	/**
	 * 使用指定的检索标准检索数据，返回指定统计值
	 */
	public Object getStatValue(DetachedCriteria criteria, String propertyName, String StatName);

	// // -------------------------------- Others --------------------------------
	//
	// /**
	// * 加锁指定的实体
	// */
	// public void lock(T entity, LockMode lockMode);
	//
	// /**
	// * 强制初始化指定的实体
	// */
	// public void initialize(Object proxy);
	//

	/**
	 * 强制立即更新缓冲数据到数据库（否则仅在事务提交时才更新）
	 */
	public void flush();

	public void clear();

	// // -------------------------------- my --------------------------------
	//
	// /**
	// * 强制初始化指定的实体
	// */
	// public void initialize(Object... proxy);
	//
	// /**
	// * 根据主键获取实体。如果没有相应的实体，返回 null。
	// */
	// public T findById(PK id);
	//
	// /**
	// * 根据主键删除指定实体，同deleteByKey()
	// */
	// public void deleteById(PK id);
	//
	// /**
	// * 查询字段属性最大值
	// */
	// public Object findMaxProperty(String propertyName);
	//
	// /**
	// * 根据属性获取实体列表。
	// */
	// public List findByProperty(String propertyName, Object value);
	//
	// /**
	// * 根据多属性属性获取实体列表。
	// */
	// public List findByProperty(String[] propertyNames, Object[] values);
	//
	// /**
	// * 加载全部数据，同loadAll()
	// *
	// * @return
	// */
	// public List<T> findAll();
	//
	// /**
	// * 使用hql语句进行分页查询
	// *
	// * @param queryString
	// * 需要查询的hql语句
	// * @param offset
	// * 第一条记录索引
	// * @param pageSize
	// * 每页需要显示的记录数
	// * @return 当前页的所有记录
	// */
	// TO DO 使用 Pager
	// public List<?> findByPage(final String queryString, final int offset, final int pageSize);

	public Pager findByPage(Pager pager);

	//
	// /**
	// * 使用hql语句进行分页查询
	// *
	// * @param queryString
	// * 需要查询的hql语句
	// * @param value
	// * 如果hql有一个参数需要传入，value就是传入hql语句的参数
	// * @param offset
	// * 第一条记录索引
	// * @param pageSize
	// * 每页需要显示的记录数
	// * @return 当前页的所有记录
	// */
	// public List findByPage(final String queryString, final Object value, final int offset, final int pageSize);
	//
	// /**
	// * 使用hql语句进行分页查询
	// *
	// * @param queryString
	// * 需要查询的hql语句
	// * @param values
	// * 如果hql有多个个参数需要传入，values就是传入hql的参数数组
	// * @param offset
	// * 第一条记录索引
	// * @param pageSize
	// * 每页需要显示的记录数
	// * @return 当前页的所有记录
	// */
	// public List findByPage(final String queryString, final Object[] values, final int offset, final int pageSize);

	Pager findByPageSql(Pager pager);

	/**
	 * 根据属性名、修改前后属性值判断在数据库中是否唯一(若新修改的值与原来值相等则直接返回true).
	 * 
	 * @param propertyName
	 *            属性名称
	 * @param oldValue
	 *            修改前的属性值
	 * @param newValue
	 *            修改后的属性值
	 * @return boolean
	 */
	public boolean isUnique(String propertyName, Object oldValue, Object newValue);

	public boolean isUnique(String[] propertyName, Object[] value, Object oldValue, Object newValue);

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

	public boolean exists(String[] propertyName, Object[] value);

	// public int getCount(final String hql);

	// public int getCount(final String hql, final Object[] values);

	// getByProcedure
	public List<Map<String, Object>> execProc(final String proc, final Object... params);

	// public void runProcedure(String proc, Object[] values);

	// public void runProcedure(String proc);
	// ====
	void fixIdIndex(String model, String parent, String id, String idIndex) throws Exception;
}