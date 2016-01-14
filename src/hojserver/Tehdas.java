package hojserver;

import java.rmi.*;

public interface Tehdas extends Remote {
	
	public void testimetodi(int repeat) throws RemoteException;
	
	public Object getData() throws RemoteException;  //Minkäköhän laisina paketteina tuo data clientille lähetetään?
	
	
}
