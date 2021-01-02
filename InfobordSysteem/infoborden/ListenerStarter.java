package infoborden;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public  class ListenerStarter implements Runnable, ExceptionListener {
	private String selector="";
	private InfoBoard infoBoard;
	private InfoBoardMessageHandler infoBoardMessageHandler;
	
	public ListenerStarter() {
	}
	
	public ListenerStarter(String selector, InfoBoard infoBoard, InfoBoardMessageHandler infoBoardMessageHandler) {
		this.selector=selector;
		this.infoBoard = infoBoard;
		this.infoBoardMessageHandler = infoBoardMessageHandler;
	}

	public void run() {
        try {
            ActiveMQConnectionFactory connectionFactory =
            		new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_BROKER_URL);
            Connection connection = connectionFactory.createConnection();
            connection.start();
            connection.setExceptionListener(this);
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination destination = session.createQueue("QUEUE2");
            MessageConsumer consumer = session.createConsumer(destination, selector);
            System.out.println("Produce, wait, consume "+ selector);
            consumer.setMessageListener(new QueueListener(selector, infoBoard, infoBoardMessageHandler));
        } catch (Exception e) {
            System.out.println("Caught: " + e);
            e.printStackTrace();
        }
    }

    public synchronized void onException(JMSException ex) {
        System.out.println("JMS Exception occured. Shutting down client.");
    }
}