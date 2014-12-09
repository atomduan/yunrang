package com.yunrang.hadoop.app.easymock;

import static org.easymock.EasyMock.*;

import org.easymock.Capture;
import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import org.easymock.IAnswer;

import org.junit.*;

public class TestEasyMockExample extends EasyMockSupport {
	
	@Test
	public void testInnerMapper() throws Exception {
		TargetClass mock = this.createMock(TargetClass.class);
		expect(mock.foo()).andStubReturn("return_foo");
		mock.bar(EasyMock.capture(new Capture<String>()) , EasyMock.capture(new Capture<String>()));
		expectLastCall().andAnswer(new IAnswer<Object>() {
		    public Object answer() throws Throwable {
		        String key = (String)(EasyMock.getCurrentArguments()[0]);
		        String value = (String)(EasyMock.getCurrentArguments()[1]);
		        System.out.println(key+"	"+value);
		        return null; // required to be null for a void method
		    }
		}).anyTimes();
		replayAll();
		String r = mock.foo();
		mock.bar("param_foo", "param_bar");
		System.out.println(r);
	}
	
	public static class TargetClass {
		public String foo() {
			return null;
		}
		public void bar(String p, String k) {
			
		}
	}
}
