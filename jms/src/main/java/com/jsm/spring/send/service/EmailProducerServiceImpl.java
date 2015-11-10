package com.jsm.spring.send.service;

import java.io.Serializable;

import javax.jms.Destination;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import com.jsm.spring.send.EmailProducerService;
@Service
public class EmailProducerServiceImpl implements EmailProducerService{
	@Autowired
	private JmsTemplate jmsTemplate; 
	public void sendMessage(Destination destination, Serializable obj) {
		// TODO Auto-generated method stub
		jmsTemplate.convertAndSend(destination, obj);
	}

}
