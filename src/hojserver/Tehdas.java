package hojserver;

import java.rmi.*;

import hojserver.tehdaskoneet.KoneenTila;

public interface Tehdas extends Remote {
	
	//-------- Kirjautuminen--------
	public void login(String kayttajaNimi) throws RemoteException; // K�ytt�j� kirjautuu
	
	public String prosessorinKayttaja(int prosessori) throws RemoteException;
	
	//-------- Ruuvikuljettimet --------
	public void ruuvihihnanKaynnistys() throws RemoteException; //Eli ekan ruuvikuljettimen start nappi
	
	public void ruuvihihnanKaynnistysVapautus() throws RemoteException; 
	
	//-------- Siilot --------
	public void siilonVaraus(int siilonNro) throws RemoteException;
	
	public void siilonVarausVapautus(int siilonNro) throws RemoteException;
	
	//-------- Prosessorit eli keittimet --------
	public void prosessorinLataus(int kuljettimeNro, int maara) throws RemoteException;
	
	public void prosessorinLatausVapautus(int kuljettimeNro) throws RemoteException;
	
	public void prosessorinVaraus(int prosessorinNro, String kayttaja)throws RemoteException;
	
	public void prosessorinVarausVapautus(int prosessorinNro)throws RemoteException;
	
	public void prosessorinKaynnistys(int prosessorinNro) throws RemoteException;
	
	public void prosessorinKaynnistysVapautus(int prosessorinNro) throws RemoteException;
	
	//-------- Tankkeja eli kypsytyssäiliöt ja niitä käsittelevät pumput --------
	public void sailoidenTaytto(int pumpunNro) throws RemoteException; // pumpuilla
	
	public void sailoidenTayttoVapautus(int pumpunNro) throws RemoteException; 
	
	public void sailionVaraus(int sailionNro) throws RemoteException;

	public void sailionVarausVapautus(int sailionNro) throws RemoteException;
	
	//-------- Pumppaaminen pullotukseen --------
	public void pullojenTaytto(int pumpunNro) throws RemoteException;
	
	public void pullojenTayttoVapautus(int pumpunNro) throws RemoteException;
	
	//.-.-.-.-.-. Tietojen kerääminen käyttöliittymän päivittämistä varten .-.-.-.-.-.
	public int[] siilojenAineMaara() throws RemoteException;
	
	public String[] prosessorienTila() throws RemoteException;
	
	public int[] sailioidenJuomanMaara() throws RemoteException;
	
	public String[] prosessorienEdistyminen() throws RemoteException;
	
	//< > < > < > Painikkeiden tilat < > < > < >
	
	public boolean[] nappiRuuvikuljettimet() throws RemoteException;
	
	public boolean[] nappiSiilot() throws RemoteException;
	
	public boolean[] nappiProsessoritReserved() throws RemoteException;
	
	public boolean[] nappiProsessoritStart() throws RemoteException;
	
	public boolean[] nappiPumput() throws RemoteException;
	
	public boolean[] nappiKypsytyssailiot() throws RemoteException;
	
	
}
