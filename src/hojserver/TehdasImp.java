package hojserver;

import java.rmi.*;
import java.rmi.server.*;

import hojserver.tehdaskoneet.*;

public class TehdasImp extends UnicastRemoteObject implements Tehdas {
	
	//Arrayna vai arraylistin? -Array k�y hyvin
	private Silo[] siilot;
	private Conveyer[] ruuvikuljettimet;
	private Pump[] pumput;
	private Processor[] prosessorit;
	private Tank[] kypsytyssailiot;   //Ei ��kk�si� mielell��n nimiksi. Ei oikeen tyk�nnyt niist� kun pullasin p�yt�koneelle
	
	
	
	public TehdasImp() throws RemoteException {
		super();
		
		siilot = new Silo[4]; //4 siiloa
		ruuvikuljettimet = new Conveyer[3]; //3 kuljetinta
		pumput = new Pump[4]; //4 pumppua
		prosessorit = new Processor[3];
		kypsytyssailiot	  = new Tank[10]; //10 kypsytyssäiliötä
		
		alustaKoneet();
		
	} // constructor
		
	//Just for the firt test. //TODO REMOVE!!
	public void testimetodi(int repeat) throws RemoteException {
		for(int i = 0; i < repeat; i++){
			System.out.println("Testing, testing...");
		}
		
		try {
			System.out.println(RemoteServer.getClientHost());
		} catch (ServerNotActiveException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//-------- Kirjautuminen --------
	public void login(String kayttajaNimi) throws RemoteException {
		// TODO Auto-generated method stub
	}
	
	//-------- Ruuvikuljettimet --------
	public void ruuvihihnanKaynnistys() throws RemoteException {
		ruuvikuljettimet[0].setRunning(true);
	}
	
	public void ruuvihihnanKaynnistysVapautus() throws RemoteException {
		ruuvikuljettimet[0].setRunning(false);
	}
	
	//-------- Siilot --------
	public void siilonVaraus(int siilonNro) throws RemoteException {
		siilot[siilonNro].setReserved(true);
	}
	
	public void siilonVarausVapautus(int siilonNro) throws RemoteException {
		siilot[siilonNro].setReserved(false);
	}
	
	//-------- Prosessorit eli keittimet --------
	public void prosessorinLataus(int kuljettimenNro, int maara) throws RemoteException {
		ruuvikuljettimet[kuljettimenNro].setRunning(true);
		ruuvikuljettimet[kuljettimenNro].setLimit(maara);
	}

	public void prosessorinLatausVapautus(int kuljettimeNro) throws RemoteException {
		ruuvikuljettimet[kuljettimeNro].setRunning(false);
	}

	public void prosessorinVaraus(int prosessorinNro, String kayttaja)
			throws RemoteException {
		prosessorit[prosessorinNro].setUser(kayttaja);
		prosessorit[prosessorinNro].setReserved(true);
	}
	
	public void prosessorinVarausVapautus(int prosessorinNro) throws RemoteException {
		prosessorit[prosessorinNro].setReserved(false);
	}

	public void prosessorinKaynnistys(int prosessorinNro)
			throws RemoteException {
		prosessorit[prosessorinNro].setRunning(true);		
	}
	
	public void prosessorinKaynnistysVapautus(int prosessorinNro) throws RemoteException {
		prosessorit[prosessorinNro].setRunning(false);
	}

	//-------- Tankkeja eli kypsytyssäiliöt ja niitä käsittelevät pumput --------
	public void sailoidenTaytto(int pumpunNro) throws RemoteException {
		pumput[pumpunNro].runPump();
	}

	public void sailoidenTayttoVapautus(int pumpunNro) throws RemoteException {
		pumput[pumpunNro].stopPump();
	}
	
	public void sailionVaraus(int sailionNro) throws RemoteException {
		kypsytyssailiot[sailionNro].setReserved(true);		
	}
	
	public void sailionVarausVapautus(int sailionNro) throws RemoteException {
		kypsytyssailiot[sailionNro].setReserved(false);
	}
	
	//-------- Pumppaaminen pullotukseen --------
	public void pullojenTaytto(int pumpunNro) throws RemoteException {
		pumput[pumpunNro].runPump();
	}
	
	public void pullojenTayttoVapautus(int pumpunNro) throws RemoteException {
		pumput[pumpunNro].stopPump();
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
			else if(prosessorit[i].getTila() == KoneenTila.PROSESSING){
				tila[i] = "Processing";
			}
			else if(prosessorit[i].getTila() == KoneenTila.READY){
				tila[i] = "Ready";
			}
			else if(prosessorit[i].getTila() == KoneenTila.FREE){
				tila[i] = "Waiting"; //TODO Change to 'Free' or something else?
			}
		}
		return tila;
	}

	public String[] prosessorienEdistyminen() throws RemoteException {
		String[] progress = new String[prosessorit.length];
		
		for(int i = 0; i < prosessorit.length; i++){
			if(prosessorit[i].getTila() == KoneenTila.EMPTYING || prosessorit[i].getTila() == KoneenTila.FILLING){
				progress[i] = Double.toString(prosessorit[i].getFillPercentage()) + " %";
				System.out.println("TehdasImp: Täytön/tyhjennyksen edistyminen " + progress[i]);
			}
			else if(prosessorit[i].getTila() == KoneenTila.PROSESSING){
				progress[i] = Double.toString(prosessorit[i].getProgress()) + " %";
				System.out.println("TehdasImp: Prosessoinnin edistyminen " + progress[i]);
			}
			else{
				progress[i] = null;
			}
		}
		return progress;
	}

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
		
	//----------------------------------------------------------
	// : METODIT KONEIDEN TILOJEN MUUTTAMISEEN
	

	
	
	
	//----------------------------------------------------------
	
	/**
	 * Metodi, jolla luodaan kaikki tehtaan koneet.
	 */
	public void alustaKoneet(){
		/* 
		 * Olisi varmaan voinut toteuttaa kompaktimmin, mutta oh well...
		 * - Noh, alustaminenhan on kertaluonteinen operaatio, joten sen kompaktiudella ei oikeastaan ole
		 * juurikaan v�li�. Ja minusta t�m� on kyll� ihan kompakti.
		 */
		
		//Siilot, 4 kpl
		for(int i = 0; i < siilot.length; i++){
			siilot[i] = new Silo();
		}
		
		//Juomakeittimet, 3 kpl
		for(int i = 0; i < prosessorit.length; i++){
			prosessorit[i] = new Processor();
			prosessorit[i].start();
		}
		//Alustetaan 3. juomakeitin testaamiseksi
		prosessorit[2].setProductAmount(10000);
		prosessorit[2].setTila(KoneenTila.READY);
		
		//Ruuvikuljettimet, 3 kpl
		for(int i = 0; i < ruuvikuljettimet.length; i++){
			if(i == 0){
				ruuvikuljettimet[i] = new SiloConveyer(siilot);
				ruuvikuljettimet[i].start(); //Start thread
			} else {
				ruuvikuljettimet[i] = new ProcessorConveyer(siilot, prosessorit);
				ruuvikuljettimet[i].start(); //Start thread
			}
		}
		
		//Pumput, 4 kpl
		for (int i = 0; i < pumput.length; i++){
			if(i < 2){
			pumput[i] = new TankPump(prosessorit, kypsytyssailiot);
			} else {
				pumput[i] = new BottlePump(kypsytyssailiot);
				pumput[i].start();
			}			
			
		}
		
		//Kypsytyssäiliöt, 10 kpl
		for(int i = 0; i < kypsytyssailiot.length; i++){
			kypsytyssailiot[i] = new Tank();
			kypsytyssailiot[i].start();
		}
	}//alustaKoneet

}
