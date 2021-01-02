package bussimulator;
import tijdtools.SimulatorTimeFunctions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

import static bussimulator.Bedrijven.*;
import static bussimulator.Lijnen.*;

public class Runner implements Runnable {

	private static HashMap<Integer,ArrayList<Bus>> busStart = new HashMap<Integer,ArrayList<Bus>>();
	private static ArrayList<Bus> actieveBussen = new ArrayList<Bus>();
	private static int interval=1000;
	private static int syncInterval=5;

	private static void addBus(int starttijd, Bus bus){
		ArrayList<Bus> bussen = new ArrayList<Bus>();
		if (busStart.containsKey(starttijd)) {
			bussen = busStart.get(starttijd);
		}
		bussen.add(bus);
		busStart.put(starttijd,bussen);
		bus.setbusID(starttijd);
	}

	private static int startBussen(int tijd){
		for (Bus bus : busStart.get(tijd)){
			actieveBussen.add(bus);
		}
		busStart.remove(tijd);
		return (!busStart.isEmpty()) ? Collections.min(busStart.keySet()) : -1;
	}

	public static void moveBussen(int nu){
		Iterator<Bus> itr = actieveBussen.iterator();
		while (itr.hasNext()) {
			Bus bus = itr.next();
			boolean eindpuntBereikt = bus.move();
			if (eindpuntBereikt) {
				bus.naarVolgendeHalte();
				bus.sendLastETA(nu);
				itr.remove();
			}
		}		
	}

	public static void sendETAs(int nu){
		Iterator<Bus> itr = actieveBussen.iterator();
		while (itr.hasNext()) {
			Bus bus = itr.next();
			bus.sendETAs(nu);
		}				
	}

	public static int initBussen(){
		int[] starttijden = new int[]{3,5,4,6,3,5,4,6,12,10,3,5,14,16,13};
		Lijnen[] lijnen = new Lijnen[]{LIJN1, LIJN4, LIJN3, LIJN4, LIJN6, LIJN7, LIJN1, LIJN4, LIJN5, LIJN8,
				LIJN8, LIJN8, LIJN3, LIJN4, LIJN5};
		Bedrijven[] bedrijven = new Bedrijven[]{ARRIVA, ARRIVA, ARRIVA,ARRIVA,FLIXBUS,QBUZZ,QBUZZ,ARRIVA,ARRIVA,
				FLIXBUS,QBUZZ,QBUZZ,ARRIVA,ARRIVA,FLIXBUS};
		for(int richting = -1; richting <= 1; richting += 2){
			for(int i = 0; i <= 15; i++){
				addBus(starttijden[i], new Bus(lijnen[i], bedrijven[i], richting));
			}
		}
		return Collections.min(busStart.keySet());
	}

	@Override
	public void run() {
		int tijd=0;
		int counter=0;
		SimulatorTimeFunctions simulatorTimeFunctions = new SimulatorTimeFunctions();
		simulatorTimeFunctions.initSimulatorTijden(interval,syncInterval);
		int volgende = initBussen();
		while ((volgende>=0) || !actieveBussen.isEmpty()) {
			counter= simulatorTimeFunctions.getCounter();
			tijd= simulatorTimeFunctions.getTijdCounter();
			System.out.println("De tijd is:" + simulatorTimeFunctions.getSimulatorWeergaveTijd());
			volgende = (counter==volgende) ? startBussen(counter) : volgende;
			moveBussen(tijd);
			sendETAs(tijd);
			try {
				simulatorTimeFunctions.simulatorStep();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
