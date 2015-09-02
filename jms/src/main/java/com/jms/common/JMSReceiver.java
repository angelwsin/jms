package com.jms.common;

import java.util.ArrayList;
import java.util.List;

import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;

public class JMSReceiver  implements MessageListener ,ExceptionListener{
	     private  List<String> bufferMessage = new ArrayList<String>();
	
	  public JMSReceiver() throws Exception{
		// TODO Auto-generated constructor stub
		  InitialContext   cxt  = new InitialContext();
		    QueueConnectionFactory factory =   (QueueConnectionFactory) cxt.lookup("queueFactory");
		      Queue queue = (Queue) cxt.lookup("queue1");
		       QueueConnection conn  =        factory.createQueueConnection();
		     QueueSession session =   conn.createQueueSession(false, Session.CLIENT_ACKNOWLEDGE);
		     QueueReceiver receiver =    session.createReceiver(queue);
		     receiver.setMessageListener(this);
		     conn.start();
	}

	public void onMessage(Message message) {
		// TODO Auto-generated method stub
		  try {
			if(message.propertyExists("squeueMarker")){
				  String marker = message.getStringProperty("squeueMarker");
				  //正在启动消息组清空
				  if("sequeue_start".equals(marker)){
					  //由于消息是以组为单位传送的 ，出现故障的话 第一个消息会标记为从新传送，无需关心其他的消息
					   if(message.getJMSRedelivered()){
						   bufferMessage.clear();
					   }
					   bufferMessage.clear();
				  }
				  
				  //结束消息标识 对整组消息 确认
				  if("sequeue_end".equals(marker)){
					    System.out.print("Message:");
					    for(String msg :bufferMessage){
					    	System.out.print(msg+"   ");
					    }
					    //确认收到所有消息
					    message.acknowledge();
				  }
			  }
			  if(message instanceof TextMessage){
				    bufferMessage.add(((TextMessage) message).getText());
			  }
			  System.out.println("waiting next message ... ");
			
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  
		 
	}
   public static void main(String[] args) {
	    try {
			new JMSReceiver();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
}

public void onException(JMSException arg0) {
	// TODO Auto-generated method stub
	//监听 用于从新 连接
}
}
