package hojserver;

import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.*;

public class TehdasServer {

	public static void main(String args[]){
		
		try{

			TehdasImp tehdas = new TehdasImp();
			
			//Luodaan RMI-rekisteri porttiin 2020
			Registry registry = LocateRegistry.createRegistry(2020);
			System.out.println(registry);
		
			Naming.rebind("//127.0.0.1:2020/tehdas", tehdas);

		} catch (Exception e) {
			System.out.println("Error: " + e);
		}
		
	} //main
} // class TehdasServer
