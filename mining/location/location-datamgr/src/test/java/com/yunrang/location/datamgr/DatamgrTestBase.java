package com.yunrang.location.datamgr;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.yunrang.location.datamgr.mysql.dao.RepoCellStationDao;
import com.yunrang.location.datamgr.mysql.dao.RepoGeoDao;
import com.yunrang.location.datamgr.mysql.dao.RepoIpRangeDao;
import com.yunrang.location.datamgr.mysql.dao.RepoIpReferDao;
import com.yunrang.location.datamgr.mysql.dao.RepoPoiDao;

import junit.framework.TestCase;

public abstract class DatamgrTestBase extends TestCase {
	protected ApplicationContext applicationContext;
	protected RepoCellStationDao repoCellStationDao;
	protected RepoGeoDao repoGeoDao;
	protected RepoIpRangeDao repoIpRangeDao;
	protected RepoIpReferDao repoIpReferDao;
	protected RepoPoiDao repoPoiDao;
	
	public DatamgrTestBase () {
        this.applicationContext = new ClassPathXmlApplicationContext("spring/snippet_datamgr.xml");
        this.repoCellStationDao = applicationContext.getBean("repoCellStationDao", RepoCellStationDao.class);
        this.repoGeoDao = applicationContext.getBean("repoGeoDao", RepoGeoDao.class);
        this.repoIpRangeDao = applicationContext.getBean("repoIpRangeDao", RepoIpRangeDao.class);
        this.repoIpReferDao = applicationContext.getBean("repoIpReferDao", RepoIpReferDao.class);
        this.repoPoiDao = applicationContext.getBean("repoPoiDao", RepoPoiDao.class);
    	System.out.println("\n#################    TEST OUTPUT    #######################:\n");
    }
}
