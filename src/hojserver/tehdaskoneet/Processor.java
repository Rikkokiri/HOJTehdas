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
 	
 	private boolean running; //Turha?
 	private boolean reserved;
 	
 	private KoneenTila tila;
 	
 	private String user; //String vai jokin muu?
 	
 	public Processor(){
 		user = null; // ???
 		//Oletuksena keittimessä ei ole vettä eikä raaka-ainetta
 		waterAmount = 0;
 		materialAmount = 0;
 		
 		//tilat
 		tila = KoneenTila.FREE;
 		reserved = false;
 		running = false;
 		
 	} //konstruktori
 
 
 	//>>>> RUN-METODI <<<<<
  	
  	public void run(){
  		//Keittimen run-metodissa ei tapahdu muuta kuin juoman keittäminen 20 sekuntia

  		int timespent = 0;
  		
		while(true){
			//System.out.println("Nyt pitäisi päästä run-metodiin" + this.tila); //TODO Remove
			
			while(running && tila == KoneenTila.PROSESSING){ 		//Tuplaehto turhaan?
 				synchronized (this) {
 					try {
 						this.wait(500); //puoli sekunti
 					} catch (InterruptedException e) {
 						System.out.println("Juoman keittäminen keskeytyi keittimessä " + this);
 						e.printStackTrace();
 					}
 				}
 				
 				//this.addProgress(2.5);
 				addProgress(10); //TODO Change
 				timespent += 500;
 				
 				System.out.println("Time spent processing: " + timespent + "milliseconds. Progress " + getProgress() + " %");
 				
 				if(progress == 100){ //Kun on odotettu prosessointiajan verran, juoma valmis
 					running = false;
 					calculateProduct(); //
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
  	
  	
 	// <<<< TILA >>>>
 	
 	public void setTila(KoneenTila t){
 		if(t == KoneenTila.FREE || t == KoneenTila.FILLING || t == KoneenTila.PROSESSING){ //TODO Valmis vielä?
 			progress = 0;
 		}
 		tila = t;
 	}
 	
 	// <<<< USER >>>>
 	
 	public void setUser(String k){
 		user = k;
 	}
 	
 	public String getUser(){
 		return user;
 	}
 	
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
 			setTila(KoneenTila.FREE);
 			
 			//Asetetaan vielä prosessorin tila kuntoon
 			if(isFull()){
 				setTila(KoneenTila.FULL); //TODO Turha?
 			}
 		}
 	}
 
 	//Start-painike
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
 
 	// <<<< VESI, RAAKA-AINE JA TUOTE >>>>
 	
 	// --- Getterit ---
 	public int getWaterAmount(){
 		return waterAmount;
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
 	
 	public void removeProduct(int amount){
 		productAmount = productAmount - amount;
 	}
 	
 	public int calculateProduct(){
 		return productAmount = 5 * materialAmount;
 	}
 	
 	// <<<< Tila jne. >>>>
 	
 	
 	//--------- PROGRESS ------------
 	
 	public double getProgress(){
 		return progress;
 	}
 	
 	public void addProgress(double amount){
 		progress += amount;
 	}
 	
 	public void resetProgress(){
 		progress = 0;
 	}
 	
 	//--------------------------------
 	
 	//What percentage of processor is filled
 	public int getFillPercentage(){
 		//System.out.println("Lasketaan täytön/tyhjennyksen edistyminen: " + materialAmount / materialAmountVolume);
 		return (int)(100 * ((double)materialAmount / (double)materialAmountVolume) ); //prosentteina
 	}
 	
 	public int getProductPercentage(){
 		return (int) (100 * ((double)productAmount / (double)waterAmountVolume));
 	}
 	
 	public int getMaterialAmountVolume(){
 		return materialAmountVolume;
 	}
 	
 	public boolean isReserved(){
 		return reserved;
 	}
 	
 	public boolean isRunning(){
 		return running;
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
 	
 	public void setProductAmount(int amount){
 		productAmount = amount;
 	}
 	
 	public int getProductAmount(){
 		return productAmount;
 	}
 	
 	
 	/**
 	 * Metodi, jolla tyhjennetään keitin, kun juoma on valmista.
 	 */
 	//JÄRKEVÄ????
 	public void emptyProcessor(){
 		waterAmount = 0;
 		materialAmount = 0;
 		productAmount = 0;
 	}
 	
 }