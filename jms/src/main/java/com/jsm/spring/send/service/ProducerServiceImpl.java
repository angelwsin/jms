package com.jsm.spring.send.service;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.jsm.spring.send.ProducerService;

@Service
public class ProducerServiceImpl implements ProducerService {
	@Autowired
	private JmsTemplate jmsTemplate;  
	   @Autowired  
	    @Qualifier("responseQueue")  
	    private Destination responseQueue;  
	public void sendMessage(Destination destination, final String message) {
		// TODO Auto-generated method stub
		System.out.println("---------------生产者发送消息-----------------");  
        System.out.println("---------------生产者发了一个消息：" + message);  
        jmsTemplate.send(destination, new MessageCreator() {  
            public Message createMessage(Session session) throws JMSException {  
                 TextMessage msg = session.createTextMessage(message);  
                 msg.setJMSReplyTo(responseQueue);
                 return msg;
            }  
        });  
		
	}

}
