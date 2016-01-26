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
		siloToBeFilled = -1;
	}
	
	// ---------- RUN ---------- //
	
	public void run(){
		
		int oldS; //Temporari muuttuuja, jota käytetääne edellisen kierroksen siilon muistamiseen
		
		while(true){
			while(running){
				
				oldS = siloToBeFilled;
				siloToBeFilled = -1;
				
				// Tarkistetaan ensin mitä siiloa mahdollisesti jo täytetään (jottei tapahtuisi turhia vaihtoja)
				if (oldS != -1 && !siilot[oldS].isFull() 
						&& (siilot[oldS].getTila() == KoneenTila.FREE || siilot[oldS].getTila() == KoneenTila.FILLING) && siilot[oldS].isReserved()
						&& running && siilot[oldS].isReserved()){
					//
					siloToBeFilled = oldS;
				}
				else{
					
					//Etsitään täytettävä siilo
					for (int i = 0; i < 4; i++){
						if (!siilot[i].isFull() 
								&& (siilot[i].getTila() == KoneenTila.FREE || siilot[i].getTila() == KoneenTila.FILLING) && siilot[i].isReserved()
								&& running && siilot[i].isReserved()){
							//
							siloToBeFilled = i;			
							break;
						}//if
					}//for
				}//else
				
				if (siloToBeFilled != -1){
					siilot[siloToBeFilled].setTila(KoneenTila.FILLING);
					siilot[siloToBeFilled].addToSilo(transferAmount);
					
					if (siilot[siloToBeFilled].isFull()){
						siilot[siloToBeFilled].setTila(KoneenTila.FULL);
					} // if full				
				}
				
				// Muutetaan siilojen tilat takaisin FREE:ksi, jos niitä ei enää täytetä
				for (int i = 0; i < 4; i++){
					if (i != siloToBeFilled && siilot[i].getTila() == KoneenTila.FILLING){
						siilot[i].setTila(KoneenTila.FREE);
					}
				}
				
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

