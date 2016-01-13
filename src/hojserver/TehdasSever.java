package hojserver;

import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.*;

public class TehdasSever {

	public static void main(String args[]){
		
		try{
			
			//http://docs.oracle.com/javase/6/docs/technotes/guides/rmi/hello/hello-world.html
			
			TehdasImp tehdas = new TehdasImp();
			//Ei tarvita 
			//Tehdas stub = (Tehdas) UnicastRemoteObject.exportObject(tehdas, 0);
			
			Registry registry = LocateRegistry.getRegistry();
			registry.rebind("juomatehdas", tehdas); //TODO Vaihda nimi?
		
		} catch (Exception e) {
			System.out.println("Error: " + e);
		}
	} //main
} // class TehdasServer
