package hojserver;

import java.rmi.*;

public interface Tehdas extends Remote {
	
	public void testimetodi(int repeat) throws RemoteException;
	
	public Object getData() throws RemoteException;  //Mink�k�h�n laisina paketteina tuo data clientille l�hetet��n?
	
	
}
