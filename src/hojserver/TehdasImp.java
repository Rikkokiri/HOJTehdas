package hojserver;

import java.rmi.*;
import java.rmi.server.*;

public class TehdasImp extends UnicastRemoteObject implements Tehdas {
	
	
	
	public TehdasImp() throws RemoteException {
		
	}
		
	//Just for the firt test.
	public void testimetodi(int repeat) throws RemoteException {
		for(int i = 0; i < repeat; i++){
			System.out.println("Testing, testing...");
		}
	}
	
	
	
}
