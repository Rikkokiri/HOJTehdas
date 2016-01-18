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
	
	
	public void login(String kayttajaNimi) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	public void ruuvihihnanKaynnistys() throws RemoteException {
		ruuvikuljettimet[0].setRunning(true);
	}
	
	public void siilonVaraus(int siilonNro) throws RemoteException {
		siilot[siilonNro].setReserved(true);
	}
	public void prosessorinLataus(int kuljettimenNro, int maara) throws RemoteException {
		ruuvikuljettimet[kuljettimenNro].setRunning(true);
		ruuvikuljettimet[kuljettimenNro].setLimit(maara);
	}

	public void prosessorinVaraus(int prosessorinNro, String kayttaja)
			throws RemoteException {
		juomakeittimet[prosessorinNro].setUser(kayttaja);
		juomakeittimet[prosessorinNro].setReserved(true);
	}

	public void prosessorinKaynnistys(int prosessorinNro)
			throws RemoteException {
		juomakeittimet[prosessorinNro].setRunning(true);		
	}

	public void sailoidenTaytto(int pumpunNro) throws RemoteException {
		pumput[pumpunNro].runPump();
	}


	public void sailionVaraus(int sailionNro) throws RemoteException {
		kypsytyssailiot[sailionNro].setReserved(true);		
	}

	public void pullojenTaytto(int pumpunNro) throws RemoteException {
		pumput[pumpunNro].runPump();
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
			napit[i] = juomakeittimet[i].isReserved();
		}
		return napit;
	}

	public boolean[] nappiProsessoritStart() throws RemoteException {
		boolean[] napit = new boolean[3];
		for (int i = 0; i < 3; i++){
			napit[i] = juomakeittimet[i].isRunning();
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

	public void ruuvihihnanKaynnistysVapautus() throws RemoteException {
		ruuvikuljettimet[0].setRunning(false);
	}

	public void siilonVarausVapautus(int siilonNro) throws RemoteException {
		siilot[siilonNro].setReserved(false);
	}

	public void prosessorinLatausVapautus(int kuljettimeNro)
			throws RemoteException {
		ruuvikuljettimet[kuljettimeNro].setRunning(false);
	}

	public void prosessorinVarausVapautus(int prosessorinNro)
			throws RemoteException {
		juomakeittimet[prosessorinNro].setReserved(false);
	}

	public void prosessorinKaynnistysVapautus(int prosessorinNro)
			throws RemoteException {
		juomakeittimet[prosessorinNro].setRunning(false);
	}

	public void sailoidenTayttoVapautus(int pumpunNro) throws RemoteException {
		pumput[pumpunNro].stopPump();
	}

	public void sailionVarausVapautus(int sailionNro) throws RemoteException {
		kypsytyssailiot[sailionNro].setReserved(false);
	}

	public void pullojenTayttoVapautus(int pumpunNro) throws RemoteException {
		pumput[pumpunNro].stopPump();
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
		for (int i = 0; i < pumput.length; i++){
			pumput[i] = new Pump();
		}
		
		//Juomakeittimet, 3 kpl
		for(int i = 0; i < juomakeittimet.length; i++){
			juomakeittimet[i] = new Processor();
		}
		
		//Kypsytyssäiliöt, 10 kpl
		for(int i = 0; i < kypsytyssailiot.length; i++){
			kypsytyssailiot[i] = new Tank();
			kypsytyssailiot[i].start();
		}

	}
}
