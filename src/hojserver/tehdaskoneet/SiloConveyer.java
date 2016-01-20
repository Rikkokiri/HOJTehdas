package hojserver.tehdaskoneet;

public class SiloConveyer extends Conveyer {

	private Silo[] siilot;
	private boolean reserved; //Onko varattu?

	public SiloConveyer(Silo[] s){
		super();
		siilot = s;
		reserved = false;
	}
	
	public void run(){
		while(true){
			
			
			// Jos ehdot täynnä, niin aletaan täyttämään ylintä mahdollista siiloa
			for (int i = 0; i < 4; i++){
				if (!siilot[i].isFull() 
						&& (siilot[i].getTila() == KoneenTila.FREE || siilot[i].getTila() == KoneenTila.FILLING) && siilot[i].isReserved()
						&& running && siilot[i].isReserved() && !reserved){
					
					reserved = true;
					siilot[i].setTila(KoneenTila.FILLING);
					siilot[i].addToSilo(2);
					
					if (siilot[i].isFull()){
						siilot[i].setTila(KoneenTila.FULL);
					}
																		
				}//if
				
			}//for
			reserved = false;
			
			
			//Odotus
			synchronized(this){
				try{
					this.wait(10);
				}catch (Exception e){System.out.println(e);}
			}
			
		}//while
	}//run
	
	public void setRunning(boolean r){
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

