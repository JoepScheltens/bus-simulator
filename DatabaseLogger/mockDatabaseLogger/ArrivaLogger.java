package mockDatabaseLogger;

import com.thoughtworks.xstream.XStream;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class ArrivaLogger implements Runnable{
	private boolean isReceivingNewMessages = true;
	private int currentAmountOfMessages = 0;
	private int currentAmountOfETAs = 0;
	private MessageConsumer consumer;


	@Override
	public void run() {
		try {
			runArrivaLogger();
		} catch (Exception e) {
			System.out.println("Caught: " + e);
			e.printStackTrace();
		}
	}

	private void runArrivaLogger() throws JMSException {
		setupConnection();
		System.out.println(currentAmountOfMessages + " berichten met " + currentAmountOfETAs + " ETAs verwerkt.");
	}

	private void setupConnection() throws JMSException {
		ActiveMQConnectionFactory connectionFactory =
				new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_BROKER_URL);
		Connection connection = connectionFactory.createConnection();
		connection.start();
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Destination destination = session.createQueue("ARRIVALOGGER");
		consumer = session.createConsumer(destination);

		handleNewMessages();

		consumer.close();
		session.close();
		connection.close();
	}

	private void handleNewMessages() throws JMSException {
		while (isReceivingNewMessages) {
			Message message = consumer.receive(2000);
			isReceivingNewMessages = false;
			if (message instanceof TextMessage) {
				messageFromXMLToBericht((TextMessage) message);
			} else {
				System.out.println("Received: " + message);
			}
		}
	}

	private void messageFromXMLToBericht(TextMessage message) throws JMSException {
		String text = message.getText();
		String textWithoutVersionNumber = text.substring(text.indexOf("?>") + 3);
		isReceivingNewMessages = true;
		XStream xstream = new XStream();
		xstream.alias("Bericht", Bericht.class);
		xstream.alias("ETA", ETA.class);
		Bericht bericht = (Bericht) xstream.fromXML(textWithoutVersionNumber);
		currentAmountOfMessages++;
		currentAmountOfETAs+=bericht.ETAs.size();
	}
}
