package hojserver;

import java.rmi.*;

public interface Tehdas extends Remote {
	
	// Metodien nimet mielekk��mmiksi?
	// Onko liikaa?
	
	public void testimetodi(int repeat) throws RemoteException;
	
	public Object getData() throws RemoteException;  //Mink�k�h�n laisina paketteina tuo data clientille l�hetet��n?
	
	public void login(String kayttajaNimi) throws RemoteException; // K�ytt�j� kirjautuu
	
	public void siilonLataus() throws RemoteException; //Eli ekan ruuvikuljettimen start nappi
	
	public void siilonVaraus(int siilonNro) throws RemoteException;
	
	public void prosessorinLataus(int kuljettimeNro, int maara) throws RemoteException;
	
	public void prosessorinVaraus(int prosessorinNro, String kayttaja)throws RemoteException;
	
	public void prosessorinKaynnistys(int prosessorinNro) throws RemoteException;
	
	public void sailoidenTaytto(int pumpunNro) throws RemoteException; // pumpuilla
	
	public void sailionVaraus(int sailionNro) throws RemoteException;
	
	public void pullojenTaytto(int pumpunNro) throws RemoteException;
	
	
	public int[] siilojenAineMaara() throws RemoteException;
	
	public int[] prosessorienTila() throws RemoteException;
	
	public int[] sailioidenJuomanMaara() throws RemoteException;
	
	public boolean[] nappiRuuvikuljettimet() throws RemoteException;
	
	public boolean[] nappiSiilot() throws RemoteException;
	
	public boolean[] nappiProsessoritReserved() throws RemoteException;
	
	public boolean[] nappiProsessoritStart() throws RemoteException;
	
	public boolean[] nappiPumput() throws RemoteException;
	
	public boolean[] nappiKypsytyssailiot() throws RemoteException;
}
