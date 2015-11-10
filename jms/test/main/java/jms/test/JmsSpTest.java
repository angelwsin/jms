package jms.test;

import javax.jms.Destination;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.jms.bean.Email;
import com.jsm.spring.send.EmailProducerService;
import com.jsm.spring.send.ProducerService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:applicationContext-context.xml")
public class JmsSpTest {
	    @Autowired
	    private ProducerService producerService;
	    @Autowired
	    private EmailProducerService emailProducerService;
	    @Autowired  
	    @Qualifier("sessionAwareQueue")  
	    private Destination sessionAwareQueue;  
	    
	    @Autowired  
	    @Qualifier("queueDestination")  
	    private Destination destination;  
	    @Autowired  
	    @Qualifier("adapterQueue")  
	    private Destination adapterQueue;  
	  // @Test
	    public void send(){
	    	producerService.sendMessage(destination, "发送了");
	    }
	   // @Test
	    public void sendAndResp(){
	    	producerService.sendMessage(sessionAwareQueue, "发送并回复");
	    }
	  //  @Test
	    public void sendAdater(){
	    	producerService.sendMessage(adapterQueue, "发送adapterQueue");
	    }

	    @Test  
	    public void testObjectMessage() {  
	        Email email = new Email("zhangsan@xxx.com", "主题", "内容");  
	        emailProducerService.sendMessage(destination, email);  
	    }  
}
