package hojserver.tehdaskoneet;

public class ProcessorConveyer extends Conveyer {
	
	private Silo[] silos;
	private Processor[] processors;
	
	private boolean reserved;
	private int siloToBeEmptied;
	private int processorToBeFilled;
	
	private int identity;
	
	private final int transferAmount = 2;
	
	
	public ProcessorConveyer(Silo[] silos, Processor[] processors, int id){
		super();
		this.silos = silos;
		this.processors = processors;
		this.identity = id;
	}
	
	public void run(){
		
		while(true){
				
				if (limit == 0){
					running = false;
				}
		
				siloToBeEmptied = -1;			//Temporariset muuttujat tyhjennettäville/
				processorToBeFilled = -1;		//täytettäville siiloille / prosesseille
				
				reserved = false;
		
				// Katsotaan mistä siilosta otetaan
				for(int i = 0; i < 4; i++){
					if (!silos[i].isEmpty() && 
							( silos[i].getTila() == KoneenTila.FREE || silos[i].getTila() == KoneenTila.EMPTYING
							|| silos[i].getTila() == KoneenTila.FULL ) && running && silos[i].isReserved() && !reserved
							&& (silos[i].getConveyer() == -1 || silos[i].getConveyer() == identity)){
						siloToBeEmptied = i;
						reserved = true;
						silos[i].setConveyer(identity);
						
					} // if
				} // for
				
				reserved = false;
				
				// Katsotaan mihin prosessoriin lisätään
				for (int i = 0; i < 3; i++){
					if (!processors[i].isFull() 
							&& (processors[i].getTila() == KoneenTila.FREE || processors[i].getTila() == KoneenTila.FILLING)
							&& running && processors[i].isReserved() && !reserved && processors[i].getProductAmount() == 0
							&& (processors[i].getConveyer() == -1 || processors[i].getConveyer() == identity)){										
						processors[i].setConveyer(identity);
						processorToBeFilled = i;
						reserved = true;
					}//if
				}//for
				
				// poistetaan siilosta ja lisätään prosessoriin
				if (siloToBeEmptied != -1 && processorToBeFilled != -1){
					
					// Tilojen muutokset
					silos[siloToBeEmptied].setTila(KoneenTila.EMPTYING);
					processors[processorToBeFilled].setTila(KoneenTila.FILLING);
					
					// Jos limit < transferAmount, niin siirretäänvain limitin verran, muuten normaalisti transferAmountin verran
					if (limit < transferAmount && limit != -1){
						processors[processorToBeFilled].addMaterial(limit);			
						silos[siloToBeEmptied].removeFromSilo(limit);
						limit = -1;
						running = false;
						silos[siloToBeEmptied].setTila(KoneenTila.FREE);
						silos[siloToBeEmptied].setConveyer(-1);
						processors[processorToBeFilled].setTila(KoneenTila.FREE);
						processors[processorToBeFilled].setConveyer(-1);
					}// if (l < t)
					else{
						// Jos prosessorissa vähemmän tilaa kuin mitä tranferAmount (Hyi!)
						if(processors[processorToBeFilled].getMaterialAmountVolume() - processors[processorToBeFilled].getMaterialAmount() < transferAmount){
							silos[siloToBeEmptied].removeFromSilo(processors[processorToBeFilled].getMaterialAmountVolume() 
									- processors[processorToBeFilled].getMaterialAmount());
							processors[processorToBeFilled].addMaterial(processors[processorToBeFilled].getMaterialAmountVolume() 
									- processors[processorToBeFilled].getMaterialAmount());
						// Jos taas siilossa vähemmän jäljellä kuin tranferAmount
						}else if (silos[siloToBeEmptied].getDegreeOfFilling() < transferAmount){
								processors[processorToBeFilled].addMaterial(silos[siloToBeEmptied].getDegreeOfFilling());			
								silos[siloToBeEmptied].emptySilo();
							}
						// Ja ihan normi käytäntö
						else {
							processors[processorToBeFilled].addMaterial(transferAmount);			
							silos[siloToBeEmptied].removeFromSilo(transferAmount);
						}
						
					// Jos limit käytössä niin vähennetään sitä
					if (limit != -1)
						limit = limit - transferAmount;
					}// else
					
					if (processors[processorToBeFilled].isFull()){
						processors[processorToBeFilled].setTila(KoneenTila.FULL);
						silos[siloToBeEmptied].setTila(KoneenTila.FREE);
						silos[siloToBeEmptied].setConveyer(-1);
						processors[processorToBeFilled].setConveyer(-1);
					}
					if (silos[siloToBeEmptied].isEmpty()){
						silos[siloToBeEmptied].setTila(KoneenTila.FREE); //?
						processors[processorToBeFilled].setTila(KoneenTila.FREE); //Tarvitaanko?
						silos[siloToBeEmptied].setConveyer(-1);
						processors[processorToBeFilled].setConveyer(-1);
					}
				} // if (jos siirretään)
				
				
				if (siloToBeEmptied == -1 && processorToBeFilled != -1){
					processors[processorToBeFilled].setTila(KoneenTila.FREE);
					processors[processorToBeFilled].setConveyer(-1);
				}
				if (siloToBeEmptied != -1 && processorToBeFilled == -1 && silos[siloToBeEmptied].getTila() == KoneenTila.EMPTYING){
					silos[siloToBeEmptied].setTila(KoneenTila.FREE);
					silos[siloToBeEmptied].setConveyer(-1);
				}
				
				//Odotus
				synchronized(this){
					try{
						this.wait(10);
					}catch (Exception e){System.out.println(e);}
				}
		}//while
		
	}
	
	public void setRunning(boolean r){
		//Jos siiloa tyhjennetään ja ProcessorConveyer pysäytetään, niin kyseisen siilon tilaksi asetetaan FREE
		for (int i = 0; i < 4; i++){
			if (silos[i].getTila() == KoneenTila.EMPTYING){
				silos[i].setTila(KoneenTila.FREE);
			}
		}
		
		for (int i = 0; i < 3; i++){
			if (processors[i].getTila() == KoneenTila.FILLING){
				processors[i].setTila(KoneenTila.FREE);
			}
		}
		
		running = r;
	}
}

