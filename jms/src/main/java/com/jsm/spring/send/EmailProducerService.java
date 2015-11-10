package com.jsm.spring.send;

import java.io.Serializable;

import javax.jms.Destination;

public interface EmailProducerService {
	public void sendMessage(Destination destination, final Serializable obj) ;
}
