package cn.pwc.javamail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/** 
* SendEmail Tester. 
* 
* @author <Authors name> 
* @since <pre>04/25/2017</pre>
* @version 1.0 
*/ 
public class SendEmailTest { 

@Before
public void before() throws Exception { 
} 

@After
public void after() throws Exception { 
} 

/** 
* 
* Method: send() 
* 
*/ 
@Test
public void testSend() throws Exception {

    for (int i = 500; i < 1500; i++) {
        SendEmail.getSendEmail().send("liuwang@liuw53.top","smtp test" + i,"内容" + i);
    }

}

@Test
public void testAttachments() throws Exception {
    List<String> add = new ArrayList<String>();
    add.add("xxx@163.com");
    add.add("xxx@139.com");
    List<String> file = new ArrayList<String>();
    file.add("E:\\doc\\xxx.doc");
    SendEmail.getSendEmail().attachments(add,"主题","内容",file);
}


    /**
* 
* Method: getProp() 
* 
*/ 
@Test
public void testGetProp() throws Exception { 
//TODO: Test goes here... 
/* 
try { 
   Method method = SendEmail.getClass().getMethod("getProp"); 
   method.setAccessible(true); 
   method.invoke(<Object>, <Parameters>); 
} catch(NoSuchMethodException e) { 
} catch(IllegalAccessException e) { 
} catch(InvocationTargetException e) { 
} 
*/ 
} 

} 
