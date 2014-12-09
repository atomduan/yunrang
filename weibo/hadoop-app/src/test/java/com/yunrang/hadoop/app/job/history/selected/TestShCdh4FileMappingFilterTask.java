package com.yunrang.hadoop.app.job.history.selected;

import static org.easymock.EasyMock.*;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.easymock.Capture;
import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import org.easymock.IAnswer;
import org.junit.*;

@SuppressWarnings({"unchecked","rawtypes"})
public class TestShCdh4FileMappingFilterTask extends EasyMockSupport {
	
	private ShCdh4FileMappingFilterTask.InnerMapper innerMapper;
	private Configuration config;
	
	private Mapper.Context mockContext;
	private String sampleFilePath;
	
	@Before
	public void setUp() throws Exception {
		this.sampleFilePath = "weibo_sample.txt";
		this.innerMapper = new ShCdh4FileMappingFilterTask.InnerMapper(){
			public void writeOutRecord(Text key, Text value, String outputFilePath) throws Exception {
				String line = (value == null) ? key.toString() : key + "	" + value;
				System.out.println(line);
		        System.out.println(outputFilePath);
			}
		};
		this.config = new Configuration();
		String inputRootPath = "/tmp/juntaoduan/history_selected_filter_0928";
		this.config.set(ShCdh4FileMappingFilterTask.INPUT_ROOT_PATH_KEY, inputRootPath);
		String outputRootPath = "/tmp/juntaoduan/history_simple_task" + "/" + System.currentTimeMillis();
		this.config.set(ShCdh4FileMappingFilterTask.OUTPUT_ROOT_PATH_KEY, outputRootPath);
		
		// mock context's api:
		this.mockContext = createMock(Mapper.Context.class);
		expect(this.mockContext.getConfiguration()).andStubReturn(this.config);
		// rewrite mock's method : register the write call
		this.mockContext.write(capture(new Capture<Text>()), capture(new Capture<Text>()));
		expectLastCall().andAnswer(new IAnswer<Object>() {
		    public Object answer() throws Throwable {
		        Text key = (Text)(EasyMock.getCurrentArguments()[0]);
		        Text value = (Text)(EasyMock.getCurrentArguments()[1]);
		        String line = (value == null) ? key.toString() : key + "	" + value;
		        System.out.println(line);
		        return null; // required to be null for a void method
		    }
		}).anyTimes();
		String mockInputFilePath =  "/tmp/juntaoduan/history_selected_filter_0928/2104_09-2014_10/part-r-00000";
		FileSplit fsp = new FileSplit(new Path(mockInputFilePath), 0, 0, new String[]{});
		expect(this.mockContext.getInputSplit()).andStubReturn(fsp);
	}
	
	@Test
	public void testInnerMapper() throws Exception {
		replayAll();
		this.innerMapper.setup(this.mockContext);
		this.doMapMethodTest();
		this.innerMapper.cleanup(this.mockContext);
	}
	
	private void doMapMethodTest() throws Exception {
		InputStream is = this.getClass().getClassLoader().getResourceAsStream(sampleFilePath);
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String line = null;
		Long key = 0L;
		while ((line = br.readLine()) != null) {
			this.innerMapper.map(new LongWritable(key), new Text(line), this.mockContext);
			key++;
		}
	}
}
