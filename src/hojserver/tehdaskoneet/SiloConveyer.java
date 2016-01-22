package hojserver.tehdaskoneet;
/*
 * Siiloja täyttävän ruuvikuljettimen luokka
 */
public class SiloConveyer extends Conveyer {

	private Silo[] siilot;
	private boolean reserved; //Onko varattu?
	private int siloToBeFilled;

	// ---------- KONSTRUKTROIT ---------- //
	
	public SiloConveyer(Silo[] s){
		super();
		siilot = s;
		reserved = false;
	}
	
	// ---------- RUN ---------- //
	
	public void run(){
		while(true){
			while(running){
				siloToBeFilled = -1;
				
				// Jos ehdot täyttyvät, niin aletaan täyttämään ylintä mahdollista siiloa
				for (int i = 0; i < 4; i++){
					if (!siilot[i].isFull() 
							&& (siilot[i].getTila() == KoneenTila.FREE || siilot[i].getTila() == KoneenTila.FILLING) && siilot[i].isReserved()
							&& running && siilot[i].isReserved() && !reserved){
						
						reserved = true;
						siloToBeFilled = i;
						siilot[i].setTila(KoneenTila.FILLING);
						siilot[i].addToSilo(transferAmount);
						
						if (siilot[i].isFull()){
							siilot[i].setTila(KoneenTila.FULL);
						}
																			
					}//if
					
				}//for
				
				// Muutetaan siilojen tilat takaisin FREE:ksi, jos niitä ei enää täytetä
				for (int i = 0; i < 4; i++){
					if (i != siloToBeFilled && siilot[i].getTila() == KoneenTila.FILLING){
						siilot[i].setTila(KoneenTila.FREE);
					}
				}
				
				reserved = false;
				
				
				//Odotus
				synchronized(this){
					try{
						this.wait(waitTime);
					}catch (Exception e){System.out.println(e);}
				}
			}//while running
			synchronized(this){
				try{
					this.wait(waitTime);
				}catch (Exception e){System.out.println(e);}
			}
		}//while
	}//run
	
	
	// ---------- GETTERIT / SETTERIT ---------- //
	
	public void setRunning(boolean r){
		//Sammutettaessa muutetaan siilojen tilaksi FREE (Mikäli sitä siis täytetään)
		if (!r){
			for (int i = 0; i < 4; i++){
				if (siilot[i].getTila() == KoneenTila.FILLING){
					siilot[i].setTila(KoneenTila.FREE);
				}
			}
		}
		running = r;
	}
}

