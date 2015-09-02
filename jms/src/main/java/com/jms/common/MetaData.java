package com.jms.common;

import java.util.Enumeration;

import javax.jms.Connection;
import javax.jms.ConnectionMetaData;
import javax.jms.QueueConnectionFactory;
import javax.naming.InitialContext;

/*
 * jms元数据  如 版本 提供者等
 */
public class MetaData {
	
	  public static void main(String[] args) throws Exception{
		   InitialContext cxt = new InitialContext();
		 QueueConnectionFactory connfactory = (QueueConnectionFactory) cxt.lookup("queueFactory");
		 Connection conn =   connfactory.createConnection();
		 ConnectionMetaData m = conn.getMetaData();
		 System.out.println("版本号:"+m.getJMSVersion());
		 Enumeration  e =  m.getJMSXPropertyNames();
		 while(e.hasMoreElements()){
			   System.out.println(e.nextElement());
		 }
		 conn.close();
	}

}
