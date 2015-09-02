package com.jms.subpub;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;
import javax.naming.InitialContext;

 public class TBorrow implements MessageListener{
	 private TopicConnection conn;
	 private  TopicSession session;
	 private  Topic topic;
	
	public TBorrow(String topicCf,String topic)throws Exception{
		   InitialContext cxt = new InitialContext();
		    TopicConnectionFactory factory   = (TopicConnectionFactory) cxt.lookup(topicCf);
		    conn =   factory.createTopicConnection();
		    
		    // 创建持久订阅的时候,必须要设置client,否则会报错:
		    // javax.jms.JMSException: You cannot create a durable subscriber
		    // without specifying a unique clientID on a Connection

		    // 如果clientID重复(已经存在相同id的活动连接),会报错
		    // javax.jms.InvalidClientIDException: Broker: localhost - Client: 1
		    // already connected from tcp://127.0.0.1:2758
		    
		    //持久订阅设置
		    conn.setClientID("1");
		    
		    
		    this.topic = (Topic) cxt.lookup(topic);
		    session = conn.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
		  //消息监听
			  //null表示不过滤 true表示不接受自己发送的
			  //   TopicSubscriber sub =  session.createSubscriber(this.topic, null, true);
		    // 在同一个连接的ClientID下,持久订阅者的名称必须唯一
		    // javax.jms.JMSException: Durable consumer is in use for client: 1 and
		    // subscriptionName: 1
		    TopicSubscriber  sub =  session.createDurableSubscriber(this.topic, "borrow1");
			      sub.setMessageListener(this);
		    conn.start();
		  
		    
		
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
			TBorrow b = new TBorrow("topicCf", "rateTopic");
			
			//b.clone();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}



	public void onMessage(Message message) {
		// TODO Auto-generated method stub
		BytesMessage msg = (BytesMessage) message;
		 try {
			if( msg.readDouble()>30){
				 System.out.println("yes ...");
			 }else{
				 System.out.println("no ....");
			 }
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

}
