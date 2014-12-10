package com.yunrang.location.datamgr.mysql.util;

import java.io.Reader;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import com.yunrang.location.common.util.UtilClasspathResource;

public class UtilMybatisTemplate {
	private SqlSessionFactory sqlSessionFactory;
	
	public UtilMybatisTemplate() {
		Reader reader = UtilClasspathResource.getReader("conf/mybatis/mybatis_config.xml");  
		sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
	}
	
	@SuppressWarnings("unchecked")
	public void execute(Class<?> mapperClass, Callback callback) throws Exception {
		SqlSession sqlSession = null;
		try {
			sqlSession = sqlSessionFactory.openSession();
			callback.execute(sqlSession.getMapper(mapperClass));
		} finally {
			if (sqlSession != null) {
				sqlSession.close();
			}
		}
	}
	
	abstract public static class Callback<T> {
		abstract public void execute(T mapper) throws Exception;
	}
}
