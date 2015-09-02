package com.jms.p2p;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
/*
 * 异步消息监听器
 */
public class QLender  implements MessageListener{
	       private  QueueConnection conn;
	       private  QueueSession queueSession;
	       private Queue   queueRequest;
	
	
	   public QLender(String queueFactory,String requestQ)throws Exception{
		     InitialContext cxt = new InitialContext();
		       QueueConnectionFactory factory=   (QueueConnectionFactory) cxt.lookup(queueFactory);
		         conn = factory.createQueueConnection();
		       queueRequest = (Queue) cxt.lookup(requestQ);
		       queueSession= conn.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);

		       conn.start();
		       QueueReceiver receiver =  queueSession.createReceiver(queueRequest);
		       receiver.setMessageListener(this);
		   
	   }

	public void onMessage(Message message) {
		// TODO Auto-generated metho	stub
		 boolean accep = false;
		 MapMessage  mapMsg = (MapMessage)message;
		
		 try {
			double salary = mapMsg.getDouble("Salary");
			double amount = mapMsg.getDouble("LoanAmount");
			if(amount >20000){
				accep = true;
			}
			
		   Queue reponseQ = (Queue) mapMsg.getJMSReplyTo();
		   TextMessage response = queueSession.createTextMessage();
		   response.setJMSCorrelationID(mapMsg.getJMSMessageID());
		   response.setText(accep?"accep":"disAccep");
		   QueueSender sender =   queueSession.createSender(reponseQ);
		   sender.send(response);
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		
	}
	public static void main(String[] args) {
		try {
			QLender qLender = new QLender("queueFactory", "requestQueue");
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			  reader.readLine();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
