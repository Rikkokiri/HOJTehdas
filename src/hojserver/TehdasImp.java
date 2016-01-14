package hojserver;

import java.rmi.*;
import java.rmi.server.*;

import hojserver.tehdaskoneet.*;

public class TehdasImp extends UnicastRemoteObject implements Tehdas {
	
	//Arrayna vai arraylistin? -Array k�y hyvin
	private Siilo[] siilot;
	private Ruuvikuljetin[] ruuvikuljettimet;
	private Pumppu[] pumput;
	private Juomakeitin[] juomakeittimet;
	private Kypsytys[] kypsytyssailiot;   //Ei ��kk�si� mielell��n nimiksi. Ei oikeen tyk�nnyt niist� kun pullasin p�yt�koneelle
	
	public TehdasImp() throws RemoteException {
		super();
		
		siilot = new Siilo[4]; //4 siiloa
		ruuvikuljettimet = new Ruuvikuljetin[3]; //3 kuljetinta
		pumput = new Pumppu[4]; //4 pumppua
		juomakeittimet = new Juomakeitin[3];
		kypsytyssailiot	  = new Kypsytys[10]; //10 kypsytyssäiliötä
		
		alustaKoneet();
		
	}
		
	//Just for the firt test.
	public void testimetodi(int repeat) throws RemoteException {
		for(int i = 0; i < repeat; i++){
			System.out.println("Testing, testing...");
		}
	}
	
	// Metdoti joka antaa kaiken tiedon tehtaan tilasta
	// Tosin mink�laisina paketteina?
	public Object getData() throws RemoteException{
		Object a = new Object();;
		return a;
	}
	
	public void alustaKoneet(){
		/* 
		 * Olisi varmaan voinut toteuttaa kompaktimmin, mutta oh well...
		 * - Noh, alustaminenhan on kertaluonteinen operaatio, joten sen kompaktiudella ei oikeastaan ole
		 * juurikaan v�li�. Ja minusta t�m� on kyll� ihan kompakti.
		 */
		
		//Siilot, 4 kpl
		for(int i = 0; i < siilot.length; i++){
			siilot[i] = new Siilo();
		}
		
		//Ruuvikuljettimet, 3 kpl
		for(int i = 0; i < ruuvikuljettimet.length; i++){
			ruuvikuljettimet[i] = new Ruuvikuljetin();
		}
		
		//Pumput, 4 kpl
		//Pitäisikö pullotukseen vievät pumput ja keittimistä kypsytykseen siirtävät pumput eritellä?
		//Jopa kirjoittaa erilliset luokat? Onko järkeä?
		//Kannattaa minusta erikseen. Niill� on kuitenkin eri teht�v�t.
		
		
		//Juomakeittimet, 3 kpl
		for(int i = 0; i < juomakeittimet.length; i++){
			juomakeittimet[i] = new Juomakeitin();
		}
		
		//Kypsytyssäiliöt, 10 kpl
		for(int i = 0; i < kypsytyssailiot.length; i++){
			kypsytyssailiot[i] = new Kypsytys();
		}
	
	}
	
	
}
