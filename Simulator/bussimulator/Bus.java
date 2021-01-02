package bussimulator;

import com.thoughtworks.xstream.XStream;
import bussimulator.Halte.Positie;

public class Bus{

	private Bedrijven bedrijf;
	private Lijnen busLine;
	private int busStopNumber;
	private int totVolgendeHalte;
	private int direction;
	private boolean isAtBusStop;
	private String busID;
	
	Bus(Lijnen BusLine, Bedrijven bedrijf, int direction){
		this.busLine = BusLine;
		this.bedrijf=bedrijf;
		this.direction = direction;
		this.busStopNumber = -1;
		this.totVolgendeHalte = 0;
		this.isAtBusStop = false;
		this.busID = "Niet gestart";
	}
	
	public void setbusID(int starttijd){
		this.busID=starttijd+ busLine.name()+ direction;
	}
	
	public void naarVolgendeHalte(){
		Positie volgendeHalte = busLine.getHalte(busStopNumber + direction).getPositie();
		totVolgendeHalte = busLine.getHalte(busStopNumber).afstand(volgendeHalte);
	}

	public boolean endPointReached(){
		if ((busStopNumber + direction >= busLine.getLengte()-1) || (busStopNumber == 0)) {
			System.out.printf("Bus %s heeft eindpunt (halte %s, richting %d) bereikt.%n", 
					busLine.name(), busLine.getHalte(busStopNumber), busLine.getRichting(busStopNumber)* direction);
			return true;
		}
		else {
			System.out.printf("Bus %s heeft halte %s, richting %d bereikt.%n", 
					busLine.name(), busLine.getHalte(busStopNumber), busLine.getRichting(busStopNumber) * direction);
			return false;
		}
	}
	
	public void start() {
		busStopNumber = (direction ==1) ? 0 : busLine.getLengte()-1;
		System.out.printf("Bus %s is vertrokken van halte %s in richting %d.%n", 
				busLine.name(), busLine.getHalte(busStopNumber), busLine.getRichting(busStopNumber)* direction);
		naarVolgendeHalte();
	}
	
	public boolean move(){
		boolean eindpuntBereikt = false;
		isAtBusStop =false;

		if (busStopNumber == -1) {
			start();
		}
		else {
			totVolgendeHalte--;
			if (totVolgendeHalte == 0){
				busStopNumber += direction;
				isAtBusStop = true;
				eindpuntBereikt = endPointReached();
			}
		}
		return eindpuntBereikt;
	}
	
	public void sendETAs(int nu){
		int i=0;
		Message message = new Message(busLine.name(),bedrijf.name(),busID,nu);
		if (isAtBusStop) {
			ETA eta = new ETA(busLine.getHalte(busStopNumber).name(), busLine.getRichting(busStopNumber)* direction,0);
			message.addETA(eta);
		}
		Positie eerstVolgende= busLine.getHalte(busStopNumber + direction).getPositie();
		int tijdNaarHalte=totVolgendeHalte+nu;
		for (i = busStopNumber + direction; !(i>= busLine.getLengte()) && !(i < 0); i=i+ direction){
			tijdNaarHalte+= busLine.getHalte(i).afstand(eerstVolgende);
			ETA eta = new ETA(busLine.getHalte(i).name(), busLine.getRichting(i)* direction,tijdNaarHalte);
			message.addETA(eta);
			eerstVolgende= busLine.getHalte(i).getPositie();
		}
		message.setEindpunt(busLine.getHalte(i- direction).name());
		sendBericht(message);
	}
	
	public void sendLastETA(int nu){
		Message message = new Message(busLine.name(),bedrijf.name(),busID,nu);
		String eindpunt = busLine.getHalte(busStopNumber).name();
		ETA eta = new ETA(eindpunt, busLine.getRichting(busStopNumber)* direction,0);
		message.addETA(eta);
		message.setEindpunt(eindpunt);
		sendBericht(message);
	}

	public void sendBericht(Message message){
    	XStream xstream = new XStream();
		xstream.alias("Bericht", Message.class);
    	xstream.alias("ETA", ETA.class);
		String berichtXML = xstream.toXML(message);
    	Producer producer = new Producer();
    	producer.sendBericht(berichtXML);
	}
}
