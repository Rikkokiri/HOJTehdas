package hojserver;

import java.rmi.*;

public interface Tehdas extends Remote {
	
	// Metodien nimet mielekk��mmiksi?
	// Onko liikaa?
	
	public void testimetodi(int repeat) throws RemoteException;
	
	public void login(String kayttajaNimi) throws RemoteException; // K�ytt�j� kirjautuu
	
	public void ruuvihihnanKaynnistys() throws RemoteException; //Eli ekan ruuvikuljettimen start nappi
	
	public void ruuvihihnanKaynnistysVapautus() throws RemoteException; 
	
	public void siilonVaraus(int siilonNro) throws RemoteException;
	
	public void siilonVarausVapautus(int siilonNro) throws RemoteException;
	
	public void prosessorinLataus(int kuljettimeNro, int maara) throws RemoteException;
	
	public void prosessorinLatausVapautus(int kuljettimeNro, int maara) throws RemoteException;
	
	public void prosessorinVaraus(int prosessorinNro, String kayttaja)throws RemoteException;
	
	public void prosessorinVarausVapautus(int prosessorinNro, String kayttaja)throws RemoteException;
	
	public void prosessorinKaynnistys(int prosessorinNro) throws RemoteException;
	
	public void prosessorinKaynnistysVapautus(int prosessorinNro) throws RemoteException;
	
	public void sailoidenTaytto(int pumpunNro) throws RemoteException; // pumpuilla
	
	public void sailoidenTayttoVapautus(int pumpunNro) throws RemoteException; 
	
	public void sailionVaraus(int sailionNro) throws RemoteException;

	public void sailionVarausVapautus(int sailionNro) throws RemoteException;
	
	public void pullojenTaytto(int pumpunNro) throws RemoteException;
	
	public void pullojenTayttoVapautus(int pumpunNro) throws RemoteException;
	
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
