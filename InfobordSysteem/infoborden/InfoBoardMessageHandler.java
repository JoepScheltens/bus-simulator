package infoborden;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
public class InfoBoardMessageHandler {

	private final HashMap<String,Integer> laatsteBericht = new HashMap<String,Integer>();
	private final HashMap<String, JSONMessage> infoBoardTextLabelToMessage = new HashMap<String, JSONMessage>();
	private int hashValue;
	private boolean refresh;
	private String[] infoBoardTextLabels;

	private int[] currentArrivalTimes;
	private String[] currentInfoBoardTextLabels;

	
	public void nieuwBericht(String incoming) {
		try {
			JSONMessage bericht = new ObjectMapper().readValue(incoming, JSONMessage.class);
	    	String busID = bericht.getBusID();
	    	Integer tijd = bericht.getTijd();
	    	if (!laatsteBericht.containsKey(busID) || laatsteBericht.get(busID)<=tijd){
	    		laatsteBericht.put(busID, tijd);
	    		if (bericht.getArrivalTime()==0){
	    			infoBoardTextLabelToMessage.remove(busID);
	    		} else {
	    			infoBoardTextLabelToMessage.put(busID, bericht);
	    		}
	    	}
	    	setInfoBoardText();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void setInfoBoardText(){
		currentInfoBoardTextLabels = new String[]{"--1--", "--2--", "--3--", "--4--", "leeg"};
		currentArrivalTimes = new int[5];
		int amountOfBusStops = 0;
		if(!infoBoardTextLabelToMessage.isEmpty()){
			for(String busID: infoBoardTextLabelToMessage.keySet()){
				JSONMessage message = infoBoardTextLabelToMessage.get(busID);
				int indexOfBusStop = amountOfBusStops;
				updateCurrentInfoBoardValues(indexOfBusStop, message);

				if(amountOfBusStops<4){
					amountOfBusStops++;
				}
			}
		}
		refresh = checkRepaint(amountOfBusStops, currentArrivalTimes);
		infoBoardTextLabels = currentInfoBoardTextLabels;
	}

	private void updateCurrentInfoBoardValues(int indexOfBusStop, JSONMessage message){
		while(indexOfBusStop > 0 && message.getArrivalTime() < currentArrivalTimes[indexOfBusStop - 1]){
			currentArrivalTimes[indexOfBusStop] = currentArrivalTimes[indexOfBusStop - 1];
			currentInfoBoardTextLabels[indexOfBusStop] = currentInfoBoardTextLabels[indexOfBusStop - 1];
			indexOfBusStop--;
		}
		indexOfBusStop--;

		currentArrivalTimes[indexOfBusStop] = message.getArrivalTime();
		currentInfoBoardTextLabels[indexOfBusStop] = message.getInfoTextLine();
	}

	private boolean checkRepaint(int aantalRegels, int[] aankomsttijden){
		int totaalTijden=0;
		for(int i=0; i<aantalRegels;i++){
			totaalTijden+=aankomsttijden[i];
		}
		if(hashValue!=totaalTijden){
			hashValue=totaalTijden;
			return true;
		}
		return false;
	}
	
	public boolean boardReceivedNewMessage() {
		return refresh;
	}
	
	public String[] repaintInfoBoardValues(){
		return infoBoardTextLabels;
	}
}
