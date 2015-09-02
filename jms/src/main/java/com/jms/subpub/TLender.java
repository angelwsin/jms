package com.jms.subpub;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.naming.InitialContext;

public class TLender {
	
	  private TopicConnection conn;
	  private TopicSession tSession;
	  private Topic topic;
	
	 public TLender(String topicCF,String topic)throws Exception{
		     InitialContext cxt = new InitialContext();
		   TopicConnectionFactory factory =   (TopicConnectionFactory) cxt.lookup(topicCF);
		   conn =    factory.createTopicConnection();
		   this.topic = (Topic) cxt.lookup(topic);
		   tSession = conn.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
		   conn.start();
	 }
     public void publicRate(double rate){
    	   try {
			TopicPublisher publisher = tSession.createPublisher(topic);
			BytesMessage msg = tSession.createBytesMessage();
			 msg.writeDouble(rate);
			 publisher.publish(msg);
			 
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	   
     }
     
     public void close(){
    	  try {
			conn.close();
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
     }
     
     public static void main(String[] args) {
		  try {
			  BufferedReader reader = new BufferedReader(new  InputStreamReader(System.in));
			TLender lender = new TLender("topicCf", "rateTopic");
			String rete = reader.readLine();
			while(true){
				lender.publicRate(Double.valueOf(rete));
				rete = reader.readLine();
				
			}
			  
			// lender.clone();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
