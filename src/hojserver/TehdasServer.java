package hojserver;

import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.*;
import java.util.ArrayList;
import java.util.UUID;


public class TehdasServer {

	private ArrayList<UUID> userIdKeys;
	
	public static void main(String args[]){
		
		try{

			TehdasImp tehdas = new TehdasImp();
			
			Registry registry = LocateRegistry.createRegistry(2020);			
			System.out.println(registry);
		
			Naming.rebind("//127.0.0.1:2020/tehdas", tehdas);

		} catch (Exception e) {
			System.out.println("Error: " + e);
		}
	} //main
} // class TehdasServer
