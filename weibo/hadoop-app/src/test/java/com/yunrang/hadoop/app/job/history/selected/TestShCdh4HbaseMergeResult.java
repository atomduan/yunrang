package com.yunrang.hadoop.app.job.history.selected;

import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.Reducer.Context;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;
import org.easymock.Capture;
import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import org.easymock.IAnswer;
import org.junit.Before;
import org.junit.Test;

import com.yunrang.hadoop.app.common.config.HBaseWeiboColumns;
import com.yunrang.hadoop.app.job.history.recompute.ShCdh4HbaseMergeResult;
import com.yunrang.hadoop.app.job.history.recompute.utils.WeiboHbaseAccesser;
import com.yunrang.hadoop.app.utils.SAWParser;


@SuppressWarnings({"unchecked", "rawtypes", "unused"})
public class TestShCdh4HbaseMergeResult extends EasyMockSupport {
	private static final String OUTPUT_ROOT = "/tmp/merge/result";
	private static final String QI_VALID = "11,1";
	
	private Configuration mockConfig;
	private Reducer.Context mockContext;
	private WeiboHbaseAccesser mockHbaseAccessor;
	private MultipleOutputs mockMultipleOutputs;
	
	private ShCdh4HbaseMergeResult.InnerMapper innerMapper;
	private ShCdh4HbaseMergeResult.InnerReducer innerReducer;
	
	@Before
	public void setUp() throws Exception {
		//mock config
		mockConfig = createMock(Configuration.class);
		expect(mockConfig.get(ShCdh4HbaseMergeResult.QI_VALID)).andStubReturn(QI_VALID);
		expect(mockConfig.get(ShCdh4HbaseMergeResult.OUTPUT_ROOT)).andStubReturn(OUTPUT_ROOT);
		
		//mock context
		mockContext = createMock(Reducer.Context.class);
		mockContext.write(capture(new Capture<Text>()), capture(new Capture<Text>()));
		expectLastCall().andAnswer(new IAnswer<Object>() {
		    public Object answer() throws Throwable {
		        Text key = (Text)(EasyMock.getCurrentArguments()[0]);
		        Text value = (Text)(EasyMock.getCurrentArguments()[1]);
		        String line = (value == null) ? key.toString() : key + "	" + value;
		        System.out.println(line);
		        return null; // required to be null for a void method
		    }
		}).anyTimes();
		expect(mockContext.getConfiguration()).andStubReturn(mockConfig);
		
		//mock hbase accessor
		mockHbaseAccessor = createMock(WeiboHbaseAccesser.class);
		mockHbaseAccessor.get(capture(new Capture<String>()), capture(new Capture<String>()));
		expectLastCall().andAnswer(new IAnswer<Map<String, String>>(){
			public Map<String, String> answer() throws Throwable {
				String mid = (String)(EasyMock.getCurrentArguments()[0]);
				String date = (String)(EasyMock.getCurrentArguments()[1]);
				Map<String, String> r = new HashMap<String, String>();
				r.put(HBaseWeiboColumns.ID.getName(), mid);
				r.put(HBaseWeiboColumns.TIME.getName(), date);
				r.put(HBaseWeiboColumns.QI.getName(), "112234");
				r.put(HBaseWeiboColumns.TOPIC_WORDS.getName(), "kkkkk");
				r.put(HBaseWeiboColumns.LIKENUM.getName(), "123");
				r.put(HBaseWeiboColumns.DATA.getName(), "234gf1ds3er123");
				r.put(HBaseWeiboColumns.USERTEXT.getName(), "dddd,@ddd");
				return r;
			}
		}).anyTimes();
		
		//mock multiple output
		mockMultipleOutputs = createMock(MultipleOutputs.class);
		mockMultipleOutputs.write(capture(new Capture<String>()), capture(new Capture<String>()), 
				capture(new Capture<Object>()), capture(new Capture<String>()));
		expectLastCall().andAnswer(new IAnswer<Object>(){
			public Object answer() throws Throwable {
				String nameOutput = (String)(EasyMock.getCurrentArguments()[0]);
				String key = (String)(EasyMock.getCurrentArguments()[1]);
				Object value = (Object)(EasyMock.getCurrentArguments()[2]);
				String baseOutputPath = (String)(EasyMock.getCurrentArguments()[3]);
				System.out.println("----------------------------------------------------");
				System.out.println("nameOutput :["+nameOutput+"]");
				System.out.println("key :["+key+"]");
				System.out.println("value :["+value+"]");
				System.out.println("baseOutputPath :["+baseOutputPath+"]");
				return null;
			}
		}).anyTimes();
		//finalize for useing.
		final Reducer.Context finalMockContext = mockContext;
		final WeiboHbaseAccesser finalMockHbaseAccessor = mockHbaseAccessor;
		final MultipleOutputs finalMockMultipleOutputs = mockMultipleOutputs;
		
		this.innerReducer = new ShCdh4HbaseMergeResult.InnerReducer() {
			public void setup(Context context) {
				String qiValidStr = finalMockContext.getConfiguration().get(ShCdh4HbaseMergeResult.QI_VALID);
				qiValidStr = StringUtils.trimToNull(qiValidStr);
				if (qiValidStr == null) {
					throw new RuntimeException("can not get qiValidStr in reducer, abandon this task");
				}
				String[] params = qiValidStr.split(",");
				for (String p : params) {
					p = StringUtils.trimToNull(p);
					if (p != null) {
						qiValidList.add(Integer.parseInt(p));
					}
				}
				outputRoot = finalMockContext.getConfiguration().get(ShCdh4HbaseMergeResult.OUTPUT_ROOT);
				if (StringUtils.trimToNull(outputRoot) == null) {
					throw new RuntimeException("the runtime output root is empty.....");
				}
				
				syncBackBaseName = outputRoot+"/"+"syncBack"+"/part";
				mergedAttrBaseName = outputRoot+"/"+"mergedAttr"+"/part";
				mergedIndexBaseName = outputRoot+"/"+"mergedIndex"+"/part";
				
				mos = finalMockMultipleOutputs;
				try {
					hbaseAccessor = finalMockHbaseAccessor;
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		};
	}
	
	public void testInnerReducer() throws Exception {
		replayAll();
		Set<Text> set = new HashSet<Text>();
		Map<String, String> r = new HashMap<String, String>();
		r.put(HBaseWeiboColumns.ID.getName(), "3752679707158406");
		r.put(HBaseWeiboColumns.TIME.getName(), "2014-09-09 00:00:26");
		r.put(HBaseWeiboColumns.QI.getName(), "12234");
//		r.put(ShCdh4HbaseMergeResult.QI_FROM_HBASE, "112234");
		r.put(HBaseWeiboColumns.TOPIC_WORDS.getName(), "");
//		r.put(ShCdh4HbaseMergeResult.TOPIC_FROM_HBASE, null);
		r.put(HBaseWeiboColumns.NON_TOPIC_WORDS.getName(), "b");
//		r.put(ShCdh4HbaseMergeResult.NONTOPIC_FROM_HBASE, null);
		r.put(HBaseWeiboColumns.USERTEXT.getName(), "jjj");
//		r.put(ShCdh4HbaseMergeResult.USERTEXT_FROM_HBASE, "ddd");
		
		set.add(new Text(SAWParser.linageRecorde(r)));
		this.innerReducer.setup(this.mockContext);
		this.innerReducer.reduce(new Text("3752679707158406"), set, this.mockContext);
	}
	
	@Test
	public void testQIModify() {
		Integer h = 1042;
		System.out.println("HBASE : " + Integer.toBinaryString(h));
		Integer r = 3090;
		System.out.println("RECOP : " + Integer.toBinaryString(r));
//		Integer f = 114282;
//		System.out.println("MERGE : " + Integer.toBinaryString(f));
	}
}
