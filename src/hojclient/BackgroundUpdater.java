package hojclient;

import java.rmi.RemoteException;



/**
 * Luokka, joka päivittää MainWindowin GUI:t 10ms:n välein
 *
 */
public class BackgroundUpdater extends Thread{
	
	MainWindow m;
	
	public BackgroundUpdater(MainWindow main){
		super();
		m = main;
	}

	public void run(){
		while(true){
			try{
			m.update();
			} catch (RemoteException e) {
				System.out.println(e);
			}
			synchronized(this){
				try{
					this.wait(10);
				}catch (Exception e){System.out.println(e);}
			}
		}
	
	}
}