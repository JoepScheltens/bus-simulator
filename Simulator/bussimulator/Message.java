package bussimulator;

import java.util.ArrayList;

public class Message {
	private final String lijnNaam;
	private String eindpunt;
	private final String bedrijf;
	private final String busID;
	private final int tijd;
	private final ArrayList<ETA> ETAs;
	
	Message(String lijnNaam, String bedrijf, String busID, int tijd){
		this.lijnNaam=lijnNaam;
		this.bedrijf=bedrijf;
		this.eindpunt="";
		this.busID=busID;
		this.tijd=tijd;
		this.ETAs=new ArrayList<ETA>();
	}

	public void addETA(ETA eta){
		ETAs.add(eta);
	}
	public void setEindpunt(String eindpunt){
		this.eindpunt = eindpunt;
	}
}
