package hojserver;

import java.rmi.*;
import java.rmi.server.*;

public class TehdasSever {

	public static void main(String args[]){
		
		try{
			
			TehdasImp tehdas = new TehdasImp();
			Naming.rebind("juomatehdas", tehdas); //TODO Vaihda nimi?
		
		} catch (Exception e) {
			System.out.println("Error: " + e);
		}
	} //main
} // class TehdasServer
