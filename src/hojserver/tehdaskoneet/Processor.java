package hojserver.tehdaskoneet;
 
 /*
 - kenelle varattu
 - tilavuus 10000 litraa vettä ja 2000 kiloa raaka-ainetta
 - prosessoi juomaa 20 sekuntia
  */
 
 public class Processor extends Thread {
 
 	//Muuttujien nimet ovat jotenkin älyttömän typerän kuuluisia, mutta ne nyt tulivat mieleen.
 	//Voi - ja varmaan kannattaa - vaihtaa myöhemmin.
 	private final int waterAmountVolume = 10000; //litraa
 	private final int materialAmountVolume = 2000; //kiloa
 	private final double processingtime = 20000; //millisekuntia
 	
 	private int waterAmount;
 	private int materialAmount;
 	private int productAmount;
 	private double progress;
 	
 	//Prosessorin tilaan liittyvät
 	private boolean running;
 	private boolean reserved;
 	private KoneenTila tila;
 	
 	//Prosessoria käyttävä conveyer
 	private int conveyer;
 	private int pumppu;
 	
 	//Prosessoria käyttävä asiakas
 	private String user; //String vai jokin muu?
 	
 	public Processor(){
  		//Oletuksena keittimessä ei ole vettä eikä raaka-ainetta
 		waterAmount = 0;
 		materialAmount = 0;
 		
 		//tilat
 		tila = KoneenTila.FREE;
 		reserved = false;
 		running = false;
 		
 		user = null; // ???
 		conveyer = -1;
 		pumppu = -1;
 		
 	} //konstruktori
 
 
 	//----------- RUN-METODI --------------
  	
  	public void run(){
  		//Keittimen run-metodissa ei tapahdu muuta kuin juoman keittäminen 20 sekuntia.
  		//TODO Tällä hetkellä juoman keittäminen on asetettu kestämään vain >>> 5 sekuntia <<<.

  		int timespent = 0; //For console printing //TODO Can be removed
  		
		while(true){
			
			while(running && tila == KoneenTila.PROSESSING && getProductAmount() == 0){
 				synchronized (this) {
 					try {
 						this.wait(250); //puoli sekunti
 					} catch (InterruptedException e) {
 						System.out.println("Juoman keittäminen keskeytyi keittimessä " + this);
 						e.printStackTrace();
 					}
 				}

 				addProgress(5); //TODO Change?
 				timespent += 500;
 				
 				System.out.println("Time spent processing: " + timespent + " milliseconds. Progress " + getProgress() + " %");
 				
 				if(progress == 100){ //Progress reaches 100 %, product ready
 					running = false;
 					makeProduct();
 					this.setTila(KoneenTila.READY);
 					System.out.println("Juoma valmis keittimessä " + this + ", juomaa " + productAmount + " litraa.");
 				}
 			}//while(running...)
				synchronized (this) {
 					try {
 						this.wait(100); //puoli sekunti
 					} catch (InterruptedException e) {}
 					}
 			resetProgress();
 		}//while(true)
  	}//run
  		
 	//------------ USER (CLIENT) ---------------------
 	
 	public void setUser(String k){
 		user = k;
 	}
 	
 	public String getUser(){
 		return user;
 	}
 	
 	//------------ CONVEYER -------------------
 	
 	public void setConveyer(int c){
 		if(c == -1 || c == 1 || c == 2 ){
 			conveyer = c;
 		}
 	}
 	
 	public int getConveyer(){
 		return conveyer;
 	}
 	
 	//--------- SET RESERVED (reserve-painike) --------------
 	
 	/**
 	 * setReserved
 	 * @param r
 	 */
 	public void setReserved(boolean r){
 		//>>> Reserve-painike vapautetaan...
 		//Ok, kunhan prosessointi ei ole käynnissä
 		if(r == false && tila != KoneenTila.PROSESSING){
 			reserved = r;
 			System.out.println("Prosessoidaan, ei voi vapauttaa");
 		}
 		//Reserve-painikkeen painaminen pohjaan on aina ok
 		else if(r == true){
 			reserved = r;
 			System.out.println("Vapautetaan prosessori!" + reserved);
 			
 			if(getProductAmount() != 0){
 				setTila(KoneenTila.READY);
 			} else {
 				setTila(KoneenTila.FREE);
 			}
 			//Asetetaan vielä prosessorin tila kuntoon
 			if(isFull()){
 				setTila(KoneenTila.FULL); //TODO Turha?
 			}
 		}
 	}
 
 	//------------ SET RUNNING (start-painike) --------------------
 	
 	public void setRunning(boolean r){
 		
 		//Start-painike painetaan pohjaan...
 		if(r == true){
 			/*...kun
 			- keitintä ei täytetä eikä tyhjennetä
 			- keittimessä on jotain mitä prosessoida
 			- keitin ei ole tilassa READY (eli saanut juomaa valmiiksi)
 			 */
 			if(tila != KoneenTila.EMPTYING && tila != KoneenTila.FILLING && tila != KoneenTila.READY && !isEmpty()){
 				running = r;
 				setTila(KoneenTila.PROSESSING);
 				System.out.println("Prosessin pitäisi käynnistyä. Prosessing tila " + getTila() + ", varaus: " + isReserved() + ", running: " + isRunning()); //TODO Remove
 			} else {
 				System.out.println("Prosessorin " + this + " start-painiketta ei voi painaa.");
 				System.out.println("Prosessori tilassa: " + getTila());
 			}
 		}
 		
 		//Start-painike vapautetaan....
 		else if(r == false){
 			//Keitin on täynnä tai vapaa ja ei tyhjä
 			if(tila == KoneenTila.READY || tila == KoneenTila.FULL || tila == KoneenTila.FREE){
 				running = false;
 				if(isFull()){
 					setTila(KoneenTila.FULL);
 				} else {
 					setTila(KoneenTila.FREE);
 				}
 			}
 		}//if
 		System.out.println("Eihän päästä tänne? Tila: " + getTila() + " varaus: " + isReserved() + ", running " + isRunning()); //TODO Remove
 	}//setRunning
 

 	// ------------- MATERIAL AND WATER ----------------------

 	public int getWaterAmount(){	//This method isn't actually used
 		return waterAmount;			//as the amount of water doesn't matter in the assigment.
 	}
 	
 	public int getMaterialAmount(){
 		return materialAmount;
 	}
 	
 	public void addMaterial(int maara){
 		//System.out.println("Processor: Lisätään prosessoriin " + maara);			//TODO remove
 		materialAmount += maara;
 		if(isFull()){
 			setTila(KoneenTila.FULL);
 		}
 	}
 	
 	public void emptyProcessor(){
 		waterAmount = 0;
 		materialAmount = 0;
 		productAmount = 0;
 	}
 	 
 	public int getMaterialAmountVolume(){
 		return materialAmountVolume;
 	}
 	
 	//----------- PRODUCT ---------------------
 	
 	/**
 	 * Method the amount of product that can be produced from material and 'makes the product'.
 	 */
 	public void makeProduct(){
 		productAmount = 5 * materialAmount;
 	}
 	
 	/**
 	 * Takes asked amount of product from the processor.
 	 * @param amount The amount to be taken from the processor.
 	 */
 	public void removeProduct(int amount){
 		productAmount = productAmount - amount;
 	}
 	
 	public void setProductAmount(int amount){
 		productAmount = amount;
 	}
 	
 	public int getProductAmount(){
 		return productAmount;
 	}
 		
 	//------------ PROGRESS -------------------
 	
 	public double getProgress(){
 		return progress;
 	}
 	
 	public void addProgress(double amount){
 		progress += amount;
 	}
 	
 	public void resetProgress(){
 		progress = 0;
 	}
 	
 	//----------- PROSENTIT ------------------
 	//
 	
 	public int getFillPercentage(){
 		//System.out.println("Lasketaan täytön/tyhjennyksen edistyminen: " + materialAmount / materialAmountVolume);
 		return (int)(100 * ((double)materialAmount / (double)materialAmountVolume) ); //prosentteina
 	}
 	
 	public int getProductPercentage(){
 		return (int) (100 * ((double)productAmount / (double)waterAmountVolume));
 	}
 	
 	//---------- PROSESSORI TILA YMS. ----------
 	
	public void setTila(KoneenTila t){
 		if(t == KoneenTila.FREE || t == KoneenTila.FILLING || t == KoneenTila.PROSESSING){ //TODO Valmis vielä?
 			progress = 0;
 		}
 		tila = t;
 	}
 	
 	public KoneenTila getTila(){
 		return tila;
 	}
 	
 	public boolean isFull(){
 		if (materialAmount >= materialAmountVolume){
 			return true;
 		}
 		else{
 			return false;
 		}
 	}
 	 	
 	public boolean isEmpty(){
 		return (materialAmount == 0 && waterAmount == 0 && productAmount == 0);
 	}
 	
 	public boolean isReserved(){
 		return reserved;
 	}
 	
 	public boolean isRunning(){
 		return running;
 	}
 	 	 	
 }