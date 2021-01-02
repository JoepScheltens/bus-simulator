package infoborden;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

public class QueueListener implements MessageListener {
	private String consumerName;
	private InfoBoard infoBoard;
	private InfoBoardMessageHandler infoBoardMessageHandler;
	
	public QueueListener(String consumerName, InfoBoard infoBoard, InfoBoardMessageHandler infoBoardMessageHandler) {
		this.consumerName = consumerName;
		this.infoBoard = infoBoard;
		this.infoBoardMessageHandler = infoBoardMessageHandler;
	}

	public void onMessage(Message message) {
		try {
			if (message instanceof TextMessage) {
	            TextMessage textMessage = (TextMessage) message;
				String text = textMessage.getText();
				System.out.println("Consumer("+consumerName+")");
				infoBoardMessageHandler.nieuwBericht(text);
				infoBoard.updateBord();
			} else {
	            System.out.println("Consumer("+consumerName+"): Received: " + message);
	        }
		} 	        catch (JMSException e) {
			e.printStackTrace();
    	}
	}
}

