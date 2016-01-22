package hojclient;

import java.rmi.RemoteException;



/**
 * Luokka, joka päivittää MainWindowin GUI:t 10ms:n välein
 *
 */
public class BackgroundUpdater extends Thread{
	
	MainWindow m;
	
	
	// ---------- KONSTRUKTORI ---------- //
	
	public BackgroundUpdater(MainWindow main){
		super();
		m = main;
	}

	
	// ---------- RUN ---------- //
		
	public void run(){
		while(m.online){
			try{
			// Päivitys
			m.update();
			} catch (RemoteException e) {
				System.out.println(e);
			}
			// Odotus
			synchronized(this){
				try{
					this.wait(10);
				}catch (Exception e){System.out.println(e);}
			}
		}
	
	}
}