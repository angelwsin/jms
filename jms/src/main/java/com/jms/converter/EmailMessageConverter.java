package com.jms.converter;

import java.io.Serializable;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.springframework.jms.support.converter.MessageConversionException;
import org.springframework.jms.support.converter.MessageConverter;


public class EmailMessageConverter implements MessageConverter{

	public Message toMessage(Object object, Session session) throws JMSException, MessageConversionException {
		// TODO Auto-generated method stub
		return session.createObjectMessage((Serializable)object);
	}

	public Object fromMessage(Message message) throws JMSException, MessageConversionException {
		// TODO Auto-generated method stub
		ObjectMessage objMessage = (ObjectMessage) message; 
		return objMessage.getObject();
	}

	
	

}
