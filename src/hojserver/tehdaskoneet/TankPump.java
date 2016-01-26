package hojserver.tehdaskoneet;

/*
- Siirtää juomaa 500 litraa sekunnissa
- Pumpulle annetaan parametrina siirrettävä määrä tai se voidaan asettaa tyhjentämään koko yksikkö
- Säiliöitä ei saa ylitäyttää
- Tyhjästä säiliöstä ei voi pumpata pullotukseen
- Pullotukseen oletetaan mahtuvan aina niin paljon kuin säiliössä on juomaa
*/

/*
 * Luokka pumpuille, jotka tyhjentää prosessoreita ja täyttävätkysytyssäiliöitä
 */

public class TankPump extends Pump {
	
	private Processor[] processors;
	private Tank[] tanks;
	
	private boolean reserved; //Onko pumppu jo käytössä?
	private int processorToBeEmptied;
	private int tankToBeFilled;
	
	private final int transferAmount = 5;
	private final int wait = 10;
	
	private int identity;
	
	
	// ---------- KONSTRUKTORIT ---------- //
	
	public TankPump(Processor[] processors, Tank[] tanks, int id){
		super();
		this.processors = processors;
		this.tanks = tanks;
		identity = id;
		tankToBeFilled = -1;
		processorToBeEmptied = -1;
	}
	
	
	// ---------- FIND METODIT ---------- //
	
	// Etsitään tyhjennetävä prosessori
	private void findProcessor(){
		for(int i = 0; i < 3; i++){
			if((processors[i].getTila() == KoneenTila.READY || processors[i].getTila() == KoneenTila.EMPTYING) &&
			!processors[i].isEmpty() && processors[i].isReserved()
				&& (processors[i].getPump() == -1 || processors[i].getPump() == identity)){
				processorToBeEmptied = i;
				processors[i].setPump(identity);
				break;
			}//if
		} //for processors
	}// findProcessor
	
	private void findTank(){
		for (int i = 0; i < 10; i++){
			if ((tanks[i].getTila() == KoneenTila.FREE || tanks[i].getTila() == KoneenTila.FILLING) &&
					!tanks[i].isFull() && tanks[i].isReserved() 
					&& (tanks[i].getPump() == -1 || tanks[i].getPump() == identity )){
				tankToBeFilled = i;
				tanks[i].setPump(identity);
				break;
			}//if
		}// for tanks
	}//findTank
	
	// ---------- RUN ----------//
	
	public void run(){
		
		int oldP;
		int oldT;
		
		while(true){
			while(isRunning()){
				
				oldP = processorToBeEmptied;
				oldT = tankToBeFilled;
				
				processorToBeEmptied = -1;
				tankToBeFilled = -1;
				
				if (oldP != -1 && processors[oldP].getPump() == identity && (processors[oldP].getTila() == KoneenTila.READY 
						|| processors[oldP].getTila() == KoneenTila.EMPTYING) && !processors[oldP].isEmpty() && processors[oldP].isReserved()){
					//
					processorToBeEmptied = oldP;
				}
				else{
				findProcessor();
				}
				
				if (oldT != -1 && tanks[oldT].getPump() == identity && (tanks[oldT].getTila() == KoneenTila.FREE 
						|| tanks[oldT].getTila() == KoneenTila.FILLING) && !tanks[oldT].isFull() && tanks[oldT].isReserved()){
					//
					tankToBeFilled = oldT;
				}
				else{
					findTank();
				}
				
				// poistaminen / lisääminen
				if (tankToBeFilled != -1 && processorToBeEmptied != -1){
					
					//tilojen muutokset
					tanks[tankToBeFilled].setTila(KoneenTila.FILLING);
					processors[processorToBeEmptied].setTila(KoneenTila.EMPTYING);
					
					// Jos prosessorissa vähemmän jäljellä, kuin siirrettävä määrä
					if (processors[processorToBeEmptied].getProductAmount() - transferAmount
							<= 0){
						tanks[tankToBeFilled].addLiquid(processors[processorToBeEmptied].getProductAmount());
						processors[processorToBeEmptied].emptyProcessor();
						tanks[tankToBeFilled].setTila(KoneenTila.FREE);
						processors[processorToBeEmptied].setTila(KoneenTila.FREE);
						processors[processorToBeEmptied].setPump(-1);
						tanks[tankToBeFilled].setPump(-1);
					}
					
					// Jos säiliöön ei enä mahdu siirrettävää määrää
					else if (tanks[tankToBeFilled].getAmountOfLiquid() + transferAmount >= 10000 ){
						processors[processorToBeEmptied].removeProduct(10000 - tanks[tankToBeFilled].getAmountOfLiquid());
						tanks[tankToBeFilled].addLiquid(10000 - tanks[tankToBeFilled].getAmountOfLiquid());
						processors[processorToBeEmptied].setTila(KoneenTila.FREE);
						processors[processorToBeEmptied].setPump(-1);
					}
					// Normaali tilanne
					else{
						processors[processorToBeEmptied].removeProduct(transferAmount);
						tanks[tankToBeFilled].addLiquid(transferAmount);
					}// else
					
					// Tilamuutokset jos tyhjä / täynnä
					
					if (processors[processorToBeEmptied].isEmpty()){
						processors[processorToBeEmptied].setTila(KoneenTila.FREE);
						processors[processorToBeEmptied].setPump(-1);
					}
					if (tanks[tankToBeFilled].isFull()){
						tanks[tankToBeFilled].setTila(KoneenTila.FULL);
					}
					
				}// if täyttö / lisäys
				
				//odotus
				synchronized(this){
					try{
						this.wait(wait);
					}catch (Exception e){System.out.println(e);}
				}
			} //while is running
			synchronized(this){
				try{
					this.wait(wait);
				}catch (Exception e){System.out.println(e);}
			}
		} // whiel true
	} //run
	
	// ---------- GETTERIT / SETTERIT ---------- //
	
	public void setRunning(boolean r){
		running = r;
	}
	
	public void stopPump(){
		if (tankToBeFilled != -1 && tanks[tankToBeFilled].getTila() == KoneenTila.FILLING){
			tanks[tankToBeFilled].setTila(KoneenTila.FREE);
		}
		running = false;
	}
	
	
}
