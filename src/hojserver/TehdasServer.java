package hojserver;

import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.*;

public class TehdasServer {

	public static void main(String args[]){
		
		try{
			
			System.setProperty("java.security.policy", "server.policy");
			
			//http://docs.oracle.com/javase/6/docs/technotes/guides/rmi/hello/hello-world.html
			
			TehdasImp tehdas = new TehdasImp();
			
			Registry registry = LocateRegistry.createRegistry(2020);
			//Registry registry = LocateRegistry.getRegistry();
			
			System.out.println(registry); //for testing
			
			//registry.rebind("juomatehdas", tehdas); //TODO Vaihda nimi?
		
			Naming.rebind("//127.0.0.1:2020/tehdas", tehdas);
			
			
		} catch (Exception e) {
			System.out.println("Error: " + e);
		}
	} //main
} // class TehdasServer
