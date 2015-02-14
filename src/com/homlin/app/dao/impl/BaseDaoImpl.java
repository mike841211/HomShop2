package com.homlin.app.dao.impl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.hibernate4.SessionFactoryUtils;
import org.springframework.util.Assert;

import com.homlin.app.dao.BaseDao;
import com.homlin.utils.Pager;

/**
 * 通用Dao实现
 * 
 * Date:2014-06-26 15:00
 * 
 * @version 1.0.0
 * 
 * @author wuduanpiao
 * 
 * @param <T>
 * @param <PK>
 */
@SuppressWarnings("unchecked")
public abstract class BaseDaoImpl<T, PK extends Serializable> implements BaseDao<T, PK> {

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	private final Class<T> entityClass;

	// private String pkName = null;

	public BaseDaoImpl() {
		this.entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		// Field[] fields = this.entityClass.getDeclaredFields();
		// for (Field f : fields) {
		// if (f.isAnnotationPresent(Id.class)) {
		// this.pkName = f.getName();
		// }
		// }
	}

	@Autowired
	private SessionFactory sessionFactory;

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public Session getSession() {
		// 事务必须是开启的(Required)，否则获取不到
		return sessionFactory.getCurrentSession();
	}

	// [[ JdbcTemplate ]]
	// ( extends JdbcDaoSupport 注解失败，只能XML配置DAO？)

	public DataSource getDataSource() {
		return SessionFactoryUtils.getDataSource(getSessionFactory());
	}

	public Connection getConnection() throws SQLException {
		return getDataSource().getConnection();
	}

	// 在需要的时候再初始化
	// @Autowired
	private JdbcTemplate jdbcTemplate;

	public JdbcTemplate getJdbcTemplate() {
		if (null == jdbcTemplate) {
			// System.out.println("new JdbcTemplate");
			jdbcTemplate = new JdbcTemplate(getDataSource());
		}
		return jdbcTemplate;
	}

	// ]]

	// -------------------- 基本检索、增加、修改、删除操作 --------------------

	@Override
	public T get(PK id) {
		return (T) getSession().get(entityClass, id);
	}

	@Override
	public T load(PK id) {
		return (T) getSession().load(entityClass, id);
	}

	@Override
	public List<T> loadAll() {
		Criteria criteria = getSession().createCriteria(entityClass);
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}

	@Override
	public void update(T entity) {
		getSession().update(entity);
	}

	@Override
	public PK save(T entity) {
		return (PK) getSession().save(entity);
	}

	@Override
	public void persist(T entity) {
		getSession().persist(entity);
	}

	@Override
	public void saveOrUpdate(T entity) {
		getSession().saveOrUpdate(entity);
	}

	@Override
	public void delete(T entity) {
		getSession().delete(entity);
	}

	@Override
	public void delete(PK id) {
		getSession().delete(load(id));
	}

	@Override
	public T merge(T entity) {
		return (T) getSession().merge(entity);
	}

	// -------------------- HQL -----------------------------

	@Override
	public int update(String hql) {
		return update(hql, (Object[]) null);
	}

	@Override
	public int update(String hql, Object... params) {
		Query query = getSession().createQuery(hql);
		setParameters(query, params);
		return Integer.valueOf(query.executeUpdate());
	}

	@Override
	public int updateBySql(String sql, Object... params) {
		Query query = getSession().createSQLQuery(sql);
		setParameters(query, params);
		return Integer.valueOf(query.executeUpdate());
	}

	// @Override
	// public List<?> find(String hql) {
	// return find(hql, (Object[]) null);
	// }

	@Override
	public List<?> find(String hql, Object... params) {
		Query query = getSession().createQuery(hql);
		setParameters(query, params);
		return query.list();
	}

	// @Override
	// public List<?> find(String hql, int maxResults) {
	// return find(hql, maxResults, (Object[]) null);
	// }

	@Override
	public List<?> findTop(String hql, int maxResults, Object... params) {
		Query query = getSession().createQuery(hql);
		setParameters(query, params);
		return query.setMaxResults(maxResults).list();
	}

	@Override
	public List<?> findBySql(String sql, Object... params) {
		Query query = getSession().createSQLQuery(sql);
		setParameters(query, params);
		return query.list();
	}

	@Override
	public List<?> findMapBySql(String sql, Object... params) {
		Query query = getSession().createSQLQuery(sql);
		setParameters(query, params);
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		return query.list();
	}

	@Override
	public Object findOne(String hql) {
		return findOne(hql, (Object[]) null);
	}

	@Override
	public Object findOne(String hql, Object... params) {
		Query query = getSession().createQuery(hql);
		setParameters(query, params);
		return query.setMaxResults(1).uniqueResult();
	}

	// -------------------------------- Criteria ------------------------------

	@Override
	public DetachedCriteria createDetachedCriteria() {
		return DetachedCriteria.forClass(this.entityClass);
	}

	@Override
	public Criteria createCriteria() {
		return this.createDetachedCriteria().getExecutableCriteria(this.getSession());
	}

	@Override
	public List<T> findByCriteria(DetachedCriteria criteria) {
		return findByCriteria(criteria, -1, -1);
	}

	@Override
	public List<T> findByCriteria(DetachedCriteria criteria, int firstResult, int maxResults) {
		Criteria executableCriteria = createCriteria();
		if (firstResult >= 0) {
			executableCriteria.setFirstResult(firstResult);
		}
		if (maxResults > 0) {
			executableCriteria.setMaxResults(maxResults);
		}
		return executableCriteria.list();
	}

	@Override
	public List<T> findEqualByEntity(T entity, String[] propertyNames) {
		Criteria criteria = this.createCriteria();
		Example exam = Example.create(entity);
		exam.excludeZeroes();
		String[] defPropertys = getSessionFactory().getClassMetadata(entityClass).getPropertyNames();
		for (String defProperty : defPropertys) {
			int ii = 0;
			for (ii = 0; ii < propertyNames.length; ++ii) {
				if (defProperty.equals(propertyNames[ii])) {
					criteria.addOrder(Order.asc(defProperty));
					break;
				}
			}
			if (ii == propertyNames.length) {
				exam.excludeProperty(defProperty);
			}
		}
		criteria.add(exam);
		return (List<T>) criteria.list();
	}

	@Override
	public List<T> findLikeByEntity(T entity, String[] propertyNames) {
		Criteria criteria = this.createCriteria();
		for (String property : propertyNames) {
			try {
				Object value = PropertyUtils.getProperty(entity, property);
				if (value instanceof String) {
					criteria.add(Restrictions.like(property, (String) value, MatchMode.ANYWHERE));
					criteria.addOrder(Order.asc(property));
				} else {
					criteria.add(Restrictions.eq(property, value));
					criteria.addOrder(Order.asc(property));
				}
			} catch (Exception ex) {
				// 忽略无效的检索参考数据。
			}
		}
		return (List<T>) criteria.list();
	}

	@Override
	public Integer getRowCount(DetachedCriteria criteria) {
		criteria.setProjection(Projections.rowCount());
		Integer result = (Integer) criteria.getExecutableCriteria(getSession()).uniqueResult();
		// List<?> list = this.findByCriteria(criteria, 0, 1);
		// return (Integer) list.get(0);
		return result == null ? 0 : ((Integer) result).intValue();
	}

	@Override
	public Object getStatValue(DetachedCriteria criteria, String propertyName, String StatName) {
		if (StatName.toLowerCase().equals("max")) {
			criteria.setProjection(Projections.max(propertyName));
		} else if (StatName.toLowerCase().equals("min")) {
			criteria.setProjection(Projections.min(propertyName));
		} else if (StatName.toLowerCase().equals("avg")) {
			criteria.setProjection(Projections.avg(propertyName));
		} else if (StatName.toLowerCase().equals("sum")) {
			criteria.setProjection(Projections.sum(propertyName));
		} else {
			return null;
		}
		List<?> list = this.findByCriteria(criteria, 0, 1);
		return list.get(0);
	}

	@Override
	public List<T> findByProperty(String[] propertyNames, Object[] values) {
		Criteria criteria = createCriteria();
		for (int i = 0; i < propertyNames.length; i++) {
			criteria.add(Restrictions.eq(propertyNames[i], values[i]));
		}
		return criteria.list();
	}

	@Override
	public List<T> findByProperty(String propertyName, Object value) {
		Criteria criteria = createCriteria();
		criteria.add(Restrictions.eq(propertyName, value));
		return criteria.list();
	}

	@Override
	public T findOneByProperty(String[] propertyNames, Object[] values) {
		Criteria criteria = createCriteria();
		for (int i = 0; i < propertyNames.length; i++) {
			criteria.add(Restrictions.eq(propertyNames[i], values[i]));
		}
		return (T) criteria.setMaxResults(1).uniqueResult();
	}

	@Override
	public T findOneByProperty(String propertyName, Object value) {
		Criteria criteria = createCriteria();
		criteria.add(Restrictions.eq(propertyName, value));
		return (T) criteria.setMaxResults(1).uniqueResult();
	}

	// ---------------------------------------------------------------

	@Override
	public void flush() {
		getSession().flush();
	}

	@Override
	public void clear() {
		getSession().clear();
	}

	@Override
	public Pager findByPage(Pager pager) {
		Assert.notNull(pager.getHql(), "pager.getHql() 不能为null，没有指定查询语句");
		Integer totalCount = pager.getTotalCount();
		if (totalCount == 0) { // 某些查询不需要重复查询总记录数，可在第一次查询总数后保存到pager
			totalCount = getCount(pager.getHql(), pager.getQueryParams());
		}
		// Integer totalCount = 50;
		pager.setTotalCount(totalCount);
		if (totalCount > 0) {
			Query query = getSession().createQuery(pager.getHql()).setFirstResult(pager.getFirstIndex()).setMaxResults(pager.getPageSize());
			setParameters(query, pager.getQueryParams());
			pager.setDataList(query.list());
		}
		return pager;
	}

	@Override
	public Pager findByPageSql(Pager pager) {
		Integer totalCount = pager.getTotalCount();
		if (totalCount == 0) { // 某些查询不需要重复查询总记录数，可在第一次查询总数后保存到pager
			totalCount = getCountBySql(pager.getSql(), pager.getQueryParams());
		}
		pager.setTotalCount(totalCount);
		if (totalCount > 0) {
			Query query = getSession().createSQLQuery(pager.getSql()).setFirstResult(pager.getFirstIndex()).setMaxResults(
					pager.getPageSize());
			setParameters(query, pager.getQueryParams());
			query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			pager.setDataList(query.list());
		}
		return pager;
	}

	@Override
	public boolean isUnique(String propertyName, Object oldValue, Object newValue) {
		// Assert.hasText(propertyName, "propertyName must not be empty");
		// Assert.notNull(newValue, "newValue is required");
		if (newValue == oldValue || newValue.equals(oldValue)) {
			return true;
		}
		if (newValue instanceof String) {
			if (oldValue != null && StringUtils.equalsIgnoreCase((String) oldValue, (String) newValue)) {
				return true;
			}
		}
		return !exists(propertyName, newValue);
	}

	@Override
	public boolean isUnique(String[] propertyName, Object[] value, Object oldValue, Object newValue) {
		if (newValue == oldValue || newValue.equals(oldValue)) {
			return true;
		}
		if (newValue instanceof String) {
			if (oldValue != null && StringUtils.equalsIgnoreCase((String) oldValue, (String) newValue)) {
				return true;
			}
		}
		return !exists(propertyName, value);
	}

	@Override
	public boolean exists(String propertyName, Object value) {
		// Assert.hasText(propertyName, "propertyName must not be empty");
		// Assert.notNull(value, "value is required");
		return findOneByProperty(propertyName, value) != null;
	}

	@Override
	public boolean exists(String[] propertyName, Object[] value) {
		// Assert.hasText(propertyName, "propertyName must not be empty");
		// Assert.notNull(value, "value is required");
		return findOneByProperty(propertyName, value) != null;
	}

	// @Override
	// public int getCount(final String hql) {
	// return getCount(hql, (Object[]) null);
	// }

	// @Override
	public int getCount(final String hql, final Object... values) {

		String queryString = hql;
		try {
			// 1.优化查询总数，去除结果字段及排序
			int index_from = StringUtils.indexOfIgnoreCase(queryString, "from ");
			if (index_from < 0) {
				index_from = 0;
			}
			int lastindex_order_by = StringUtils.lastIndexOfIgnoreCase(queryString, " order by ");
			if (lastindex_order_by < 0 || queryString.lastIndexOf(")") > lastindex_order_by || queryString.lastIndexOf("'") > lastindex_order_by) { // 确认是查询结果的排序
				lastindex_order_by = queryString.length();
			}
			queryString = "select count(*) " + queryString.substring(index_from, lastindex_order_by);

			Query query = getSession().createQuery(queryString);
			setParameters(query, values);
			return Integer.valueOf(query.uniqueResult().toString());
		} catch (Exception e) {
			logger.error("优化查询总数失败:\n\t优化前：" + hql + "\n\t优化后：" + queryString);

			// 2.直接查询一次
			Query query = getSession().createQuery(hql);
			setParameters(query, values);
			return query.list().size();
		}
	}

	// @Override
	public int getCountBySql(final String sql, final Object[] values) {

		String queryString = sql;
		try {
			// 1.优化查询总数，去除结果字段及排序
			// String _s=";with _table as (select * from Tb_Sync_Order); select * from _table;";
			// 去除前后“;”
			int index_semicolon = queryString.indexOf(";");
			while (index_semicolon == 0) {
				queryString = queryString.substring(index_semicolon);
				index_semicolon = queryString.indexOf(";");
			}
			int lastindex_semicolon = queryString.lastIndexOf(";");
			while (lastindex_semicolon == queryString.length() - 1) {
				queryString = queryString.substring(0, lastindex_semicolon);
				lastindex_semicolon = queryString.lastIndexOf(";");
			}
			if (queryString.indexOf(";") > -1) { // 还包含“;”
				throw new Exception("查询语句中间包含分号");
			}

			// ---

			int index_from = StringUtils.indexOfIgnoreCase(queryString, "from ");
			if (index_from < 0) {
				index_from = 0;
			}
			int lastindex_order_by = StringUtils.lastIndexOfIgnoreCase(queryString, " order by ");
			if (lastindex_order_by < 0 || queryString.lastIndexOf(")") > lastindex_order_by || queryString.lastIndexOf("'") > lastindex_order_by) { // 确认是查询结果的排序
				lastindex_order_by = queryString.length();
			}
			queryString = "select count(*) " + queryString.substring(index_from, lastindex_order_by);

			Query query = getSession().createSQLQuery(queryString);
			setParameters(query, values);
			return Integer.valueOf(query.uniqueResult().toString());
		} catch (Exception e) {
			logger.error("优化查询总数失败:\n\t优化前：" + sql + "\n\t优化后：" + queryString);

			// 2.直接查询一次
			Query query = getSession().createSQLQuery(sql);
			setParameters(query, values);
			return query.list().size();
		}
	}

	@Override
	public List<Map<String, Object>> execProc(final String proc, final Object... params) {
		String sql = proc.trim();
		if (sql.indexOf(" ") == -1) {
			sql = "exec " + sql;
			int len = params.length;
			if (len > 0) {
				sql += " ?";
				for (int i = 2; i <= len; i++) {
					sql += ",?";
				}
			}
		}
		SQLQuery sqlQuery = (SQLQuery) getSession().createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		setParameters(sqlQuery, params);
		return sqlQuery.list();
	}

	// ------- utils -------

	protected void trace(Object... objects) {
		for (Object object : objects) {
			System.out.println(object);
		}
	}

	protected Query setParameters(Query query, Object... params) {
		if (params != null) {
			for (int i = 0; i < params.length; i++) {
				// if (params[i].getClass().isArray()) {
				// query.setParameterList(":xxx", (Object[]) params[i]);
				// query.setTimestamp(i, (Date) params[i]);
				// } else if (params[i] instanceof Date) {
				// // 正-难道这是bug 使用setParameter不行？？
				// query.setTimestamp(i, (Date) params[i]);
				// } else {
				query.setParameter(i, params[i]);
				// }
			}
		}
		return query;
	}

	// ===========

	public void fixIdIndex(String model, String parent, String id, String idIndex) throws Exception {
		String hqlString = String
				.format("select new map(%3$s as id,%2$s.%3$s as pid, %4$s as idIndex) from %1$s where %2$s is null", model, parent, id, idIndex);
		List<Map<String, Object>> list = (List<Map<String, Object>>) find(hqlString);
		String updateHql = String.format("update %s set %s=? where %s=?", model, idIndex, id);
		for (Map<String, Object> map : list) {
			map.put("idIndex", map.get("id"));
			update(updateHql, (String) map.get("idIndex"), (String) map.get("id"));
			fixSubIdIndex(model, parent, id, idIndex, map);
		}
	}

	private void fixSubIdIndex(String model, String parent, String id, String idIndex, Map<String, Object> pmap) throws Exception {
		String hqlString = String
				.format("select new map(%3$s as id,%2$s.%3$s as pid, %4$s as idIndex) from %1$s where %2$s.%3$s=?", model, parent, id, idIndex);
		List<Map<String, Object>> list = (List<Map<String, Object>>) find(hqlString, (String) pmap.get("id"));
		String updateHql = String.format("update %s set %s=? where %s=?", model, idIndex, id);
		for (Map<String, Object> map : list) {
			map.put("idIndex", ((String) pmap.get("idIndex")) + "," + (String) map.get("id"));
			update(updateHql, (String) map.get("idIndex"), (String) map.get("id"));
			fixSubIdIndex(model, parent, id, idIndex, map);
		}
	}

}