package com.jms.common;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;

/*
 * 消息的确认方式 
 * Session.CLIENT_ACKNOWLEDGE   使用分组的方式发送
 */
public class JMSSender {
	  private  QueueConnection conn;
	  private  QueueSession session;
	  private  Queue queue;
	  private QueueSender sender;
	
	public static void main(String[] args) {
		  try {
			JMSSender send = new JMSSender();
			send.sendGroup();
			send.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public JMSSender() throws Exception{
		      InitialContext cxt = new InitialContext();
		      QueueConnectionFactory factory =   (QueueConnectionFactory) cxt.lookup("queueFactory");
		      conn =     factory.createQueueConnection();
		      queue = (Queue) cxt.lookup("queue1");
		      session = conn.createQueueSession(false, Session.CLIENT_ACKNOWLEDGE);
		      sender = session.createSender(queue);
		      conn.start();
	}
	
	  public void sendGroup(){
		     sendSequeueMarker("sequeue_start");
		     sendMessage("mumber1");
		     sendMessage("nubmer2");
		     sendMessage("nubmer3");
		     sendSequeueMarker("sequeue_end");
		   
	  }
	
	public void sendMessage(String text){
		 
		       try {
		    	   TextMessage msg = session.createTextMessage();
				     msg.setText(text);
				     msg.setStringProperty("JMSXGroupID", "GROUP1");
				     sender.send(msg);
			} catch (JMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	}
	
	public void sendSequeueMarker(String marker){
		    try {
				BytesMessage msg =   session.createBytesMessage();
				msg.setStringProperty("squeueMarker", marker);
				 msg.setStringProperty("JMSXGroupID", "GROUP1");
				 sender.send(msg);
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

}
