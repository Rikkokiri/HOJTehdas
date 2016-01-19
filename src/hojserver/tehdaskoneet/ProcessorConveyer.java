package hojserver.tehdaskoneet;

public class ProcessorConveyer extends Conveyer {
	
	private Silo[] silos;
	private Processor[] processors;
	private boolean reserved;
	private boolean kaikkiTaynna;
	private int siloToBeEmptied;
	private int processorToBeFilled;
	
	public ProcessorConveyer(Silo[] silos, Processor[] processors){
		super();
		this.silos = silos;
		this.processors = processors;
	}
	
	public void run(){
		
while(true){
	
			siloToBeEmptied = -1;			//Temporariset muuttujat tyhjennettäville/
			processorToBeFilled = -1;		//täytettäville contanereille
	
			// Katsotaan mistä siilosta otetaan
			for (int i = 0; i < 4; i++){
				if (!silos[i].isEmpty() && 
						( silos[i].getTila() == KoneenTila.FREE || silos[i].getTila() == KoneenTila.EMPTYING
						|| silos[i].getTila() == KoneenTila.FULL ) && running && silos[i].isReserved() && reserved){
					siloToBeEmptied = i;
					reserved = true;
					
				}
			}
			
			reserved = false;
			
			// Katsotaan mihin prosessoriin lisätään
			for (int i = 0; i < 3; i++){
				if (!processors[i].isFull() 
						&& (processors[i].getTila() == KoneenTila.FREE || processors[i].getTila() == KoneenTila.FILLING)
						&& running && processors[i].isReserved() && !reserved){										
					processorToBeFilled = i;
					reserved = true;
				}//if	
			}//for
			System.out.println("Siilo: " + siloToBeEmptied + " Prosessori: " + processorToBeFilled);
			
			if (siloToBeEmptied != -1 && processorToBeFilled != -1){
				reserved = true;
				processors[processorToBeFilled].setTila(KoneenTila.FILLING);
				processors[processorToBeFilled].addMaterial(200);
				
				silos[siloToBeEmptied].setTila(KoneenTila.EMPTYING);
				silos[siloToBeEmptied].removeFromSilo(200);
				
				if (processors[processorToBeFilled].isFull()){
					processors[processorToBeFilled].setTila(KoneenTila.FULL);
				}
			}
			reserved = false;
			
			//Jos kaikki prosessorit täynnä
			//Ei välttämättä tarvita?
			
			kaikkiTaynna = true;
			for (int i = 0; i < 3; i++){
				if (!processors[i].isFull()){
					kaikkiTaynna = false;
				}
			}
			if (kaikkiTaynna){
				this.setRunning(false);
			}
			if (siloToBeEmptied != -1 && processorToBeFilled != -1){
				System.out.println("Siilo " + siloToBeEmptied + 1 + ", " + silos[siloToBeEmptied].getDegreeOfFilling());
				System.out.println("Prosessori " + processorToBeFilled + 1 + ", " + processors[processorToBeFilled].getMaterialAmount());
			}
			//Odotus
			synchronized(this){
				try{
					this.wait(1000);
				}catch (Exception e){System.out.println(e);}
			}
			
		}//while
		
	}
	
}
