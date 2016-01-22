package hojserver.tehdaskoneet;
/*
 * Siilosta prosessorille raaka-ainetta kuljettavan ruuvikuljettimen luokka
 */
public class ProcessorConveyer extends Conveyer {
	
	private Silo[] silos;
	private Processor[] processors;
	
	private boolean reserved; // Onko Kuljetin jo jonkin käytössä
	private int siloToBeEmptied;
	private int processorToBeFilled;
	private int limit; // Siirrettävä raaka-aineen määrä, jonka käyttäjä antanut (-1 jos ei käyttäjä antanut mitään)
	
	private final int identity; //Tunniste, jolla tunnistetaan mikä kuljetin kyseesä
	
	private final int transferAmount = 2; // Yhdessä syklissä siirrettävä määrä
	private final int waitTime = 10; // yhden syklin kesto millisekunneissa
	
	
	// -------- KONSTRUKTORI --------
	
	public ProcessorConveyer(Silo[] silos, Processor[] processors, int id){
		super();
		this.silos = silos;
		this.processors = processors;
		this.identity = id;
		limit = -1;
	}
	
	// -------- RUN --------
	
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
				
				// poistetaan siilosta ja lisätään prosessoriin (jos voidaan)
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
						// Jos prosessorissa vähemmän tilaa kuin mitä tranferAmount
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
					}// else (limit käytössä?)
					
					// Tilamuutokset, jos prosessori tuli täytee / siilo tyhjeni
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
						this.wait(waitTime);
					}catch (Exception e){System.out.println(e);}
				}
		}//while
		
	}// run
	
	// -------- GETTERIT / SETTERIT -------- //
	
	public void setRunning(boolean r){
		//Jos siiloa tyhjennetään ja ProcessorConveyer pysäytetään, niin kyseisen siilon tilaksi asetetaan FREE
		for (int i = 0; i < 4; i++){
			if (silos[i].getTila() == KoneenTila.EMPTYING){
				silos[i].setTila(KoneenTila.FREE);
			}
		}
		// Vastaavasti sama jos prosessoria täytetään ja ProcesorConveyer pysäytetään, niin asetetaan tilaksi FREE
		for (int i = 0; i < 3; i++){
			if (processors[i].getTila() == KoneenTila.FILLING){
				processors[i].setTila(KoneenTila.FREE);
			}
		}
		
		running = r;
	}// setRunning
	
	public void setLimit(int l){
		limit = l;
	}
}

