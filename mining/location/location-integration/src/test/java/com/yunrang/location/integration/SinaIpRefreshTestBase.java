package com.yunrang.location.integration;

import com.yunrang.location.common.context.ContextHttpClient;
import com.yunrang.location.common.context.ContextPoiQuery;
import com.yunrang.location.integration.support.api.ip.LocationIpApiSinaIPRange;

import junit.framework.TestCase;

public class SinaIpRefreshTestBase extends TestCase {
    //initialize context
    protected ContextHttpClient httpClientContext;
    protected ContextPoiQuery poiQueryingContext;
    protected LocationIpApiSinaIPRange locationAPISinaIPRange;
}
