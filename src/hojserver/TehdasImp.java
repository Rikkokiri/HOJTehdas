package hojserver;

import java.rmi.*;
import java.rmi.server.*;
import java.util.Hashtable;
import java.util.UUID;

import hojserver.tehdaskoneet.*;


public class TehdasImp extends UnicastRemoteObject implements Tehdas {

	private Silo[] siilot;
	private Conveyer[] ruuvikuljettimet;
	private Pump[] pumput;
	private Processor[] prosessorit;
	private Tank[] kypsytyssailiot; 
	
	private Hashtable<UUID, String> userIdKeys;
	
	//---------- KONSTRUKTORI -----------------------
	
	public TehdasImp() throws RemoteException {
		super();
		
		siilot = new Silo[4]; //4 siiloa
		ruuvikuljettimet = new Conveyer[3]; //3 kuljetinta
		pumput = new Pump[4]; //4 pumppua
		prosessorit = new Processor[3];
		kypsytyssailiot	  = new Tank[10]; //10 kypsytyssäiliötä
		
		//Kutsutaan koneet luovaa ja koneiden threadit käynnistävää metodia.
		alustaKoneet();
		
		userIdKeys = new Hashtable<UUID, String>();
		
	} //constructor
	
	//-------- Kirjautuminen --------
	
	// SISÄÄNKIRJAUTUMINEN
	public UUID login(String kayttajaNimi) throws RemoteException {
		//Generoidaan käyttäjälle uniikki id
		UUID id = UUID.randomUUID();
		userIdKeys.put(id, kayttajaNimi);
		System.out.println("Käyttäjä " + kayttajaNimi + ", " + id + " kirjautui sisään.");
		return id;
	}
	
	// 
	public String prosessorinKayttaja(int prosessori) throws RemoteException {
		return prosessorit[prosessori].getUser();
	}
	
	// ULOSKIRJOITUS
	public void logout(UUID idKey) throws RemoteException {
		for(int i = 0; i < prosessorit.length; i++){
			if(prosessorit[i].getUserId() != null && prosessorit[i].getUserId().equals(idKey)){
				prosessorit[i].setReserved(false);
			}
		}
		System.out.println("Käyttäjä " + userIdKeys.get(idKey) + ", " + idKey + " kirjautui ulos.");
		
		//Poistetaan käyttäjän id käyttäjien id-listalta
		userIdKeys.remove(idKey);
	}
	
	//-------- Ruuvikuljettimet -------------
	
	// SILO CONVEYERIN KÄYNNISTYS
	public void ruuvihihnanKaynnistys() throws RemoteException {
		System.out.println("Tehdas: Käynnistetään siiloja täyttävä ruuvikuljetin");
		ruuvikuljettimet[0].setRunning(true);
	}
	
	// SILO CONVEYERIN SAMMUTUS
	public void ruuvihihnanKaynnistysVapautus() throws RemoteException {
		System.out.println("Tehdas: Sammutetaan siiloja täyttävä ruuvikuletin.");
		ruuvikuljettimet[0].setRunning(false);
	}
	
	//-------- Siilot --------------
	// SIILON VARAUS
	public void siilonVaraus(int siilonNro) throws RemoteException {
		System.out.println("Tehdas: Varataan siilo nro. " + (siilonNro+1));
		siilot[siilonNro].setReserved(true);
	}
	
	// SIILON VARAUKSEN VAPAUTTAMINEN
	public void siilonVarausVapautus(int siilonNro) throws RemoteException {
		System.out.println("Tehdas: Vapautetaan siilo nro. " + (siilonNro+1));
		siilot[siilonNro].setReserved(false);
	}
	
	//-------- Prosessorit eli keittimet --------
	
	// PROCESSOR CONVEYERING KÄYNNISTYS
	public void prosessorinLataus(int kuljettimenNro, int maara) throws RemoteException {
		if (maara != -1){
			System.out.println("Tehdas: Ruuvikuljetin " + (kuljettimenNro+1) + " alkaa siirtää " + maara + "kg materiaalia prosessoriin");
		}
		else{
			System.out.println("Tehdas: Ruuvikuljetin " + (kuljettimenNro+1) + " alkaa siirtää materiaalia prosessoriin");
		}
		ruuvikuljettimet[kuljettimenNro].setRunning(true);
		ruuvikuljettimet[kuljettimenNro].setLimit(maara);
	}

	// PROCESSOR CONVEYERIN SAMMUTUS
	public void prosessorinLatausVapautus(int kuljettimenNro) throws RemoteException {
		System.out.println("Tehdas: Sammutetaan ruuvikuljetin nro. " + (kuljettimenNro+1));
		ruuvikuljettimet[kuljettimenNro].setRunning(false);
	}

	/*Tässä metodissa on tavallaan typerää tarkistaa koko käyttäjäroskaa, koska metodia kutsutaan vain,
	kun reserve-painike on ylhäällä, jolloin keittimessä ei voi olla varausta.
	Mutta kun jo kirjoitin, niin antaa olla.
	*/
	// PROSESSORIN VARAUS
	public void prosessorinVaraus(int prosessorinNro, UUID userId) throws RemoteException {
		if(prosessorit[prosessorinNro].getUserId() == null){
			prosessorit[prosessorinNro].setReserved(true);
			prosessorit[prosessorinNro].setUser(userIdKeys.get(userId));
			prosessorit[prosessorinNro].setUserId(userId);
			System.out.println("Tehdas: Käyttäjä " + userIdKeys.get(userId) + ", " + userId + " varaa prosessorin nro. " + (prosessorinNro+1));
		} else {
			System.out.println("Käyttäjä: " + userIdKeys.get(userId) + " yritti varata prosessorin " + (prosessorinNro+1) + ".");
			System.out.println("Tehdas: Ei voida varata prosessoria. Prosessori on käyttäjän " + userIdKeys.get(prosessorit[prosessorinNro].getUserId()) + ", " + prosessorit[prosessorinNro].getUserId() + " hallussa.");
		}
	}
	
	// VARAUKSEN VAPAUTUS PROSESSORISTA
	public void prosessorinVarausVapautus(int prosessorinNro, UUID userId) throws RemoteException {
		if(prosessorit[prosessorinNro].getUserId().equals(userId)){
			prosessorit[prosessorinNro].setReserved(false);
			System.out.println("Tehdas: Vapautetaan prosessori nro. " + (prosessorinNro+1)); //TODO
		} else {
			System.out.println("Käyttäjä: " + userIdKeys.get(userId) + ", " + userId + " yritti vapauttaa prosessorin " + (prosessorinNro+1) + ".");
			System.out.println("Tehdas: Ei voida vapauttaa. Prosessori on käyttäjän " + userIdKeys.get(prosessorit[prosessorinNro].getUserId()) + ", " + userId + " hallussa.");
		}
	}

	// PROSESSORIN KÄYNNISTYS
	public void prosessorinKaynnistys(int prosessorinNro, UUID userId) throws RemoteException {
		if(prosessorit[prosessorinNro].getUserId() != null && prosessorit[prosessorinNro].getUserId().equals(userId)){
			prosessorit[prosessorinNro].setRunning(true);
		} else {
			System.out.println("Käyttäjä: " + userIdKeys.get(userId) + ", " + userId + " yritti käynnistää prosessorin " + (prosessorinNro+1) + ".");
			System.out.println("Tehdas: Ei voida käynnistää. Prosessori on käyttäjän " + userIdKeys.get(userId) + ", " + userId + " hallussa.");
		}
	}
	
	// PROSESSORIN SAMMUTUS
	public void prosessorinKaynnistysVapautus(int prosessorinNro) throws RemoteException {
		System.out.println("Tehdas: Sammutetaan prosessori " + (prosessorinNro+1));
		prosessorit[prosessorinNro].setRunning(false);
	}

	//-------- Tankkeja eli kypsytyssäiliöt ja niitä käsittelevät pumput --------
	
	// TANK PUMP KÄYNNISTYS
	public void sailoidenTaytto(int pumpunNro) throws RemoteException {
		pumput[pumpunNro].runPump();
		System.out.println("Tehdas: Käynnistetään pumppu nro. " + (pumpunNro+1) + " prosessoreista -> kypsytyssäiliöihin.");
	}

	// TANK PUMP SAMMUTUS
	public void sailoidenTayttoVapautus(int pumpunNro) throws RemoteException {
		pumput[pumpunNro].stopPump();
		System.out.println("Tehdas: Sammutetaan kypsytyssäiliöiden pumppu nro. " + (pumpunNro+1));
	}
	
	// TANKIN VARAUS
	public void sailionVaraus(int sailionNro) throws RemoteException {
		kypsytyssailiot[sailionNro].setReserved(true);		
		System.out.println("Tehdas: Varataan kypsytyssäiliö nro. " + (sailionNro+1));
	}
	
	// TANKIN VARAUKSEN VAPAUTUS
	public void sailionVarausVapautus(int sailionNro) throws RemoteException {
		kypsytyssailiot[sailionNro].setReserved(false);
		System.out.print("Tehdas: Vapautetaan kypsytyssäiliö nro. " + (sailionNro+1));
	}
	
	//-------- Pumppaaminen pullotukseen --------
	
	// BOTTLE PUMP KÄYNNISTYS
	public void pullojenTaytto(int pumpunNro) throws RemoteException {
		pumput[pumpunNro].runPump();
		System.out.println("Tehdas: Käynnistetään pullotuspumppu nro. " + (pumpunNro+1));
	}
	
	public void pullojenTayttoVapautus(int pumpunNro) throws RemoteException {
		pumput[pumpunNro].stopPump();
		System.out.println("Tehdas: Sammutetaan pullotuspumppu nro. " + (pumpunNro+1));
	}

	//.-.-.-.-.-. Tietojen kerääminen käyttöliittymän päivittämistä varten .-.-.-.-.-.
	
	public int[] siilojenAineMaara() throws RemoteException {
		int[] sam = new int[4];
		
		for(int i = 0; i < siilot.length; i++){
			sam[i] = siilot[i].getDegreeOfFilling();
		}
		return sam;
	}

	// prosentteina
	public String[] prosessorienTila() throws RemoteException {
		String[] tila = new String[prosessorit.length];
		for(int i = 0; i < prosessorit.length; i++){
			if(prosessorit[i].getTila() == KoneenTila.EMPTYING){
				tila[i] = "Emptying";
			}
			else if(prosessorit[i].getTila() == KoneenTila.FILLING){
				tila[i] = "Filling";
			}
			else if(prosessorit[i].getTila() == KoneenTila.PROCESSING){
				tila[i] = "Processing";
			}
			else if(prosessorit[i].getTila() == KoneenTila.READY){
				tila[i] = "Ready";
			}
			else if(prosessorit[i].getTila() == KoneenTila.FREE){
				tila[i] = "Waiting";
			}
			else if (prosessorit[i].getTila() == KoneenTila.FULL){
				tila[i] = "Full";
			}
		}
		return tila;
	}

	/**
	 * Metodi kerää tiedot prosessorien tiloista prosentteina. Prosessorin tilasta riippuen käyttöliittymälle kerrotaan joko
	 * a) valmiin tuotteen prosentti (kapasiteetista)
	 * b) kuinka suuri osuus raaka-ainekapasiteetista on täytetty
	 * c) juoman keittämisen edistyminen prosentteina
	 */
	public String[] prosessorienEdistyminen() throws RemoteException {
		String[] progress = new String[prosessorit.length];
		
		for(int i = 0; i < prosessorit.length; i++){
			if (prosessorit[i].getTila() == KoneenTila.EMPTYING || prosessorit[i].getTila() == KoneenTila.READY){
				progress[i] = Integer.toString(prosessorit[i].getProductPercentage()) + " %";
			}
			else if(prosessorit[i].getTila() != KoneenTila.PROCESSING || prosessorit[i].getTila() == KoneenTila.FULL || prosessorit[i].getTila() == KoneenTila.FREE){
				progress[i] = Integer.toString(prosessorit[i].getFillPercentage()) + " %";
				//System.out.println("TehdasImp: Täytön/tyhjennyksen edistyminen " + progress[i]);
			}
			else if(prosessorit[i].getTila() == KoneenTila.PROCESSING){
				progress[i] = Double.toString(prosessorit[i].getProgress()) + " %";
				//System.out.println("TehdasImp: Prosessoinnin edistyminen " + progress[i]);
			}
			else{
				progress[i] = null;
			}
		}
		return progress;
	}

	/**
	 * Palauttaa tiedot, paljonko kussakin kypsytyssäiliössä (Tank-luokka) on juomaa.
	 */
	public int[] sailioidenJuomanMaara() throws RemoteException {
		int[] sjm = new int[10];
		for (int i = 0; i < 10; i++){
			sjm[i] = kypsytyssailiot[i].getAmountOfLiquid();
		}
		return sjm;
	}
	
	//< > < > < > Painikkeiden tilat < > < > < >
	
	public boolean[] nappiRuuvikuljettimet() throws RemoteException {
		boolean[] napit = new boolean[3];
		for (int i = 0; i < 3; i++){
			napit[i] = ruuvikuljettimet[i].isRunning();
		}
		return napit;
	}

	public boolean[] nappiSiilot() throws RemoteException {
		boolean[] napit = new boolean[4];
		for (int i = 0; i < 4; i++){
			napit[i] = siilot[i].isReserved();
		}
		return napit;
	}

	public boolean[] nappiProsessoritReserved() throws RemoteException {
		boolean[] napit = new boolean[3];
		for (int i = 0; i < 3; i++){
			napit[i] = prosessorit[i].isReserved();
		}
		return napit;
	}

	public boolean[] nappiProsessoritStart() throws RemoteException {
		boolean[] napit = new boolean[3];
		for (int i = 0; i < 3; i++){
			napit[i] = prosessorit[i].isRunning();
		}
		return napit;
	}

	public boolean[] nappiPumput() throws RemoteException {
		boolean[] napit = new boolean[4];
		for (int i = 0; i < 4; i++){
			napit[i] = pumput[i].isRunning();
		}
		return napit;
	}

	public boolean[] nappiKypsytyssailiot() throws RemoteException {
		boolean[] napit = new boolean[10];
		for (int i = 0; i < 10; i++){
			napit[i] = kypsytyssailiot[i].isReserved();
		}
		return napit;
	}
		
	/**
	 * Alustaa kaikki tehtaan koneet ja sitä mukaa kuin luo koneet, käynnistää niiden Threadit.
	 */
	public void alustaKoneet(){

		//Siilot, 4 kpl
		for(int i = 0; i < siilot.length; i++){
			siilot[i] = new Silo();
		}
		
		//Juomakeittimet, 3 kpl
		for(int i = 0; i < prosessorit.length; i++){
			prosessorit[i] = new Processor();
			prosessorit[i].start();
		}

		//Ruuvikuljettimet, 3 kpl
		for(int i = 0; i < ruuvikuljettimet.length; i++){
			if(i == 0){
				ruuvikuljettimet[i] = new SiloConveyer(siilot);
				ruuvikuljettimet[i].start(); //Start thread
			} else {
				ruuvikuljettimet[i] = new ProcessorConveyer(siilot, prosessorit, i);
				ruuvikuljettimet[i].start(); //Start thread
			}
		}
		
		//Pumput, 4 kpl
		for (int i = 0; i < pumput.length; i++){
			if(i < 2){
			pumput[i] = new TankPump(prosessorit, kypsytyssailiot, i);
			} else {
				pumput[i] = new BottlePump(kypsytyssailiot, i);
			}
			pumput[i].start();
		}
		
		//Kypsytyssäiliöt, 10 kpl
		for(int i = 0; i < kypsytyssailiot.length; i++){
			kypsytyssailiot[i] = new Tank();
			kypsytyssailiot[i].start();
		}
	}//alustaKoneet
	
} //TehdasImp
