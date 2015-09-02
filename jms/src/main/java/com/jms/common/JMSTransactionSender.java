package com.jms.common;

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
 * 事务的jms  sender
 */
public class JMSTransactionSender {
	    private QueueConnection conn;
	    private Queue queue;
	    private QueueSession session;
	    private QueueSender sender;
	    public JMSTransactionSender() {
			// TODO Auto-generated constructor stub
	    	try {
				InitialContext cxt = new InitialContext();
				    QueueConnectionFactory facotry =   (QueueConnectionFactory) cxt.lookup("queueFactory");
				 conn  =facotry.createQueueConnection();
				  queue = (Queue) cxt.lookup("queue1");
				  //第一个参数 是否 使用 事务 
				session =   conn.createQueueSession(true, Session.AUTO_ACKNOWLEDGE);
				   sender =   session.createSender(queue);
				   conn.start();
				  
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	
		}
	    
	    public  void sendMessages(){
	    	       //在同一个事务中发送
	    	      try {
					sendMessage("first message ");
					sendMessage("second message");
					sendMessage("third  message");
					session.commit();
					conn.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					try {
						session.rollback();
					} catch (JMSException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
	    }
       public void sendMessage(String text )throws Exception{
    	                TextMessage msg =          session.createTextMessage();
    	                sender.send(msg);
       }
       
       public static void main(String[] args) {
		        JMSTransactionSender tran = new JMSTransactionSender();
		        tran.sendMessages();
	}
}
