package hojserver;

import java.rmi.*;
import java.rmi.server.*;

import hojserver.tehdaskoneet.*;

public class TehdasImp extends UnicastRemoteObject implements Tehdas {
	
	//Arrayna vai arraylistin? -Array k�y hyvin
	private Silo[] siilot;
	private Conveyer[] ruuvikuljettimet;
	private Pump[] pumput;
	private Processor[] juomakeittimet;
	private Tank[] kypsytyssailiot;   //Ei ��kk�si� mielell��n nimiksi. Ei oikeen tyk�nnyt niist� kun pullasin p�yt�koneelle
	
	
	
	public TehdasImp() throws RemoteException {
		super();
		
		siilot = new Silo[4]; //4 siiloa
		ruuvikuljettimet = new Conveyer[3]; //3 kuljetinta
		pumput = new Pump[4]; //4 pumppua
		juomakeittimet = new Processor[3];
		kypsytyssailiot	  = new Tank[10]; //10 kypsytyssäiliötä
		
		alustaKoneet();
		
	}
		
	//Just for the firt test.
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
	
	// Metdoti joka antaa kaiken tiedon tehtaan tilasta
	// Tosin mink�laisina paketteina?
	public Object getData() throws RemoteException{
		Object a = new Object();;
		return a;
	}
	
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
		
		//Ruuvikuljettimet, 3 kpl
		for(int i = 0; i < ruuvikuljettimet.length; i++){
			ruuvikuljettimet[i] = new Conveyer();
		}
		
		//Pumput, 4 kpl
		//Pitäisikö pullotukseen vievät pumput ja keittimistä kypsytykseen siirtävät pumput eritellä?
		//Jopa kirjoittaa erilliset luokat? Onko järkeä?
		// - Kannattaa minusta erikseen. Niill� on kuitenkin eri teht�v�t.
		// - Sama kysymys her�si my�s ruuvikuljettimista
		
		
		//Juomakeittimet, 3 kpl
		for(int i = 0; i < juomakeittimet.length; i++){
			juomakeittimet[i] = new Processor();
		}
		
		//Kypsytyssäiliöt, 10 kpl
		for(int i = 0; i < kypsytyssailiot.length; i++){
			kypsytyssailiot[i] = new Tank();
		}

	}

	public void login(String kayttajaNimi) throws RemoteException {
		// TODO Auto-generated method stub
		
	}


	public void siilonLataus() throws RemoteException {
		// TODO Auto-generated method stub
		
	}


	public void siilonVaraus(int siilonNro) throws RemoteException {
		// TODO Auto-generated method stub
		
	}


	public void prosessorinLataus(int kuljettimenNro, int maara) throws RemoteException {
		// TODO Auto-generated method stub
		
	}


	public void prosessorinVaraus(int prosessorinNro, String kayttaja)
			throws RemoteException {
		// TODO Auto-generated method stub
		
	}


	public void prosessorinKaynnistys(int prosessorinNro)
			throws RemoteException {
		// TODO Auto-generated method stub
		
	}


	public void sailoidenTaytto(int pumpunNro) throws RemoteException {
		// TODO Auto-generated method stub
		
	}


	public void sailionVaraus(int sailionNro) throws RemoteException {
		// TODO Auto-generated method stub
		
	}


	public void pullojenTaytto(int pumpunNro) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	
	public int[] siilojenAineMaara() throws RemoteException {
		int[] sam = new int[4];
		sam[0] = siilot[0].getDegreeOfFilling();
		sam[1] = siilot[1].getDegreeOfFilling();
		sam[2] = siilot[2].getDegreeOfFilling();
		sam[3] = siilot[3].getDegreeOfFilling();
		return sam;
	}

	// prosentteina
	public int[] prosessorienTila() throws RemoteException {
		int[] tila = new int[3];
		tila[0] = (juomakeittimet[0].getProgress() / 20000) * 100;
		tila[1] = (juomakeittimet[1].getProgress() / 20000) * 100;
		tila[2] = (juomakeittimet[2].getProgress() / 20000) * 100;
		return tila;
	}

	
	public int[] sailioidenJuomanMaara() throws RemoteException {
		int[] sjm = new int[10];
		for (int i = 0; i < 10; i++){
			sjm[i] = kypsytyssailiot[i].getNestemäärä();
		}
		return sjm;
	}

	
	public boolean[] nappienTila() throws RemoteException {
		int[] tilat = new int[1];
		return null;
	}
	
}
