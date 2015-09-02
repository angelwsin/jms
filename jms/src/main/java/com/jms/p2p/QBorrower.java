package com.jms.p2p;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;

public class QBorrower {
	   private QueueConnection queueConn;
	   private QueueSession queueSession;
	   private Queue requestQueue;
	   private Queue responseQueue;
	
	   public QBorrower(String queueFactory,String requestQueue,String responseQueue)throws Exception{
		        InitialContext cxt = new InitialContext();
		        QueueConnectionFactory  qFactory =(QueueConnectionFactory)  cxt.lookup(queueFactory);
		        queueConn =  (QueueConnection)qFactory.createConnection();
		        //参数一  表示 是否为事务性 true 是  只有调用 commit才会把消息传给接受者
		        //参二   确认模式
		        
		        this.queueSession = queueConn.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
		        this.requestQueue = (Queue) cxt.lookup(requestQueue);
		        this.responseQueue = (Queue) cxt.lookup(responseQueue);
		        queueConn.start();
	        
	   }
	   /**
	    * 异步的响应/应答
	    * @param salary
	    * @param loanAmt
	    */
	   
	   private void sendRequest(double salary,double loanAmt){
		          try {
					MapMessage msg =  queueSession.createMapMessage();
					msg.setDouble("Salary", salary);
					msg.setDouble("LoanAmount", loanAmt);
					//消息头 设定 响应队列
					msg.setJMSReplyTo(responseQueue);
				QueueSender sender = queueSession.createSender(requestQueue);
				sender.send(msg);
				//查看贷款被接受或拒绝
				//消息关联
				String  filter = "JMSCorrelationID= '"+msg.getJMSMessageID()+"'";
				  QueueReceiver receiver =   queueSession.createReceiver(responseQueue, filter);
				  //设置合理的延时  不然程序一直会阻塞
				    TextMessage  rMsg =  (TextMessage) receiver.receive(3000);
				    if(rMsg==null){
				    	System.out.println(" 没有 响应");
				    }else{
				    	System.out.println("响应的结果:"+rMsg.getText());
				    }
				} catch (JMSException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    
	   }
	
	public static void main(String[] args) {
		QBorrower b  = null;
		   try {
			 b = new QBorrower("queueFactory", "requestQueue", "responseQueue");
			  b.sendRequest(8000000, 200000000);
			  
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
		   try {
			b.queueConn.close();
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
	}

}
