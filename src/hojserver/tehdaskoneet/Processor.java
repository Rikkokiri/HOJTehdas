package hojserver.tehdaskoneet;

import java.util.UUID;

/*
 - Tilavuus 10000 litraa vettä ja 2000 kiloa raaka-ainetta
 - Prosessoi juomaa 20 sekuntia
  */
 
 public class Processor extends Thread {
 
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
 	//Prosessoria tyhjentävä pumppu
 	private int pump;
 	
 	//Prosessoria käyttävä asiakas
 	private String user;
 	private UUID userId;
 	
 	public Processor(){
  		//Oletuksena keittimessä ei ole vettä eikä raaka-ainetta
 		waterAmount = 0;
 		materialAmount = 0;
 		
 		//Tilat
 		tila = KoneenTila.FREE;
 		reserved = false;
 		running = false;
 		
 		//Oletuksena prosessorilla ei ole vielä käyttäjää
 		user = null;
 		userId = null;
 		//Eikä yksikään ruuvikuljetin tai pumppu myöskään vielä käytä sitä
 		conveyer = -1;
 		pump = -1;
 		
 	} //konstruktori
 
 
 	//----------- RUN-METODI --------------
  	
  	public void run(){
  		//Keittimen run-metodissa ei tapahdu muuta kuin juoman keittäminen 20 sekuntia.
  		//Tällä hetkellä juoman keittäminen on asetettu kestämään vain >>> 5 sekuntia <<<.

  		int timespent = 0; //Prosessointiin käytetty aika konsoliin tulostamista varten
  		
		while(true){
			
			while(running && tila == KoneenTila.PROCESSING && getProductAmount() == 0){
 				synchronized (this) {
 					try {
 						this.wait(250); //0.25 sekuntia
 					} catch (InterruptedException e) {
 						System.out.println("Juoman keittäminen keskeytyi keittimessä " + this);
 						e.printStackTrace();
 					}
 				}

 				addProgress(5);
 				timespent += 500;
 				
 				System.out.println("Time spent processing: " + timespent + " milliseconds. Progress " + getProgress() + " %");
 				
 				if(progress == 100){ //Kun päästään 100 %:iin on tuote valmis
 					running = false;
 					makeProduct();
 					this.setTila(KoneenTila.READY);
 					System.out.println("Processor: Juoma valmis keittimessä " + this + ", juomaa " + productAmount + " litraa.");
 				}
 			}//while(running...)
			
			//Pieni odotus
			synchronized (this) {
 				try {
 					this.wait(100); //puoli sekunti
 				} catch (InterruptedException e) { e.printStackTrace(); }
 			}
 			resetProgress(); //Prosessin loputtua nollataan prosessin edistymisestä huolehtiva attribuutti
 		}//while(true)
  	}//run
  		
 	//------------ USER (CLIENT) ---------------------
 	
  		// 	Käyttäjän nimi 
 	public void setUser(String k){
 		user = k;
 	}
 	
 	public String getUser(){
 		return user;
 	}
 	
 		//	Käyttäjän id 
 	
 	public UUID getUserId(){
 		return userId;
 	}
 	
 	public void setUserId(UUID id){
 		userId = id;
 	}
 	
 	//------------ CONVEYER -------------------
 	
 	public void setConveyer(int c){
 		conveyer = c;
 	}
 	
 	public int getConveyer(){
 		return conveyer;
 	}
 	
 	//-------------- PUMP ---------------------
 	
 	public void setPump(int p){
 		pump = p;
 	}
 	
 	public int getPump(){
 		return pump;
 	}
 	
 	//--------- SET RESERVED (reserve-painike) --------------
 	
 	/**
 	 * Metodi huolehtii prosessorin varaamisesta ja varauksen vapauttamisesta.
 	 * @param r
 	 */
 	public void setReserved(boolean r){
 		//>>> Reserve-painike vapautetaan...
 		//Ok, kunhan prosessointi ei ole käynnissä
 		if(r == false && tila != KoneenTila.PROCESSING){
 			reserved = r;
 			userId = null;
 			user = null;
 		}
 		//Reserve-painikkeen painaminen pohjaan on aina ok
 		else if(r == true){
 			reserved = r;
 			System.out.println("Vapautetaan prosessori!" + reserved);
 			
 			//Asetetaan vielä prosessorin tila kuntoon.
 			if(getProductAmount() != 0){	//Jos prosessorissa on valmista juomaa, tila = READY
 				setTila(KoneenTila.READY);
 			} 
 			else if(isFull() && getProductAmount() == 0){ //Jos prosessori on täynnä prosessoimatonta materiaalia, tila = FULL
 				setTila(KoneenTila.FULL);
 			} else {
 				setTila(KoneenTila.FREE); //Muulloin tila = FREE
 			}
 		}
 	}
 
 	//------------ SET RUNNING (start-painike) --------------------
 	
 	public void setRunning(boolean r){
 		
 		// >>> Start-painike painetaan pohjaan...
 		if(r == true){
 			/*...kun
 			1. keitintä ei parhailaan täytetä eikä tyhjennetä
 			2. keittimessä on jotain mitä prosessoida (ei tyhjä)
 			3. keittimessä ei ole valmista juomaa
 			 */
 			if(reserved && tila != KoneenTila.EMPTYING && tila != KoneenTila.FILLING && tila != KoneenTila.READY && !isEmpty() && getProductAmount() == 0){
 				running = r;
 				setTila(KoneenTila.PROCESSING);
 				System.out.println("Processor: Käynnistetään prosessori.");
 			} else {
 				System.out.println("Prosessorin " + this + " start-painiketta ei voi painaa.");
 				System.out.println("Prosessori tilassa: " + getTila());
 			}
 		}
 		
 		// >>> Start-painike vapautetaan....
 		else if(r == false){
 			//Keitin on täynnä tai vapaa ja ei tyhjä
 			if(tila == KoneenTila.READY || tila == KoneenTila.FULL || tila == KoneenTila.FREE){
 				running = false;
 				//Asetetaan keitin vielä oikeaan tilaan
 				if(isFull() && getProductAmount() == 0){ //tila = FULL, jos täynnä, muttei sisällä valmista juomaa
 					setTila(KoneenTila.FULL);
 				} else if(getProductAmount() != 0){ //tila = READY, jos sisältää valmista huomaa
 					setTila(KoneenTila.READY);
 				}
 				else {
 					setTila(KoneenTila.FREE); //Muulloin tila = FREE
 				}
 			}
 		}//if
 	}//setRunning
 

 	// ------------- MATERIAL AND WATER ----------------------

 	public int getWaterAmount(){
 		return waterAmount;			
 	}
 	
 	public int getMaterialAmount(){
 		return materialAmount;
 	}
 	
 	/**
 	 * Lisätään raaka-ainetta keittimeen (prosessoriin)
 	 * @param maara
 	 */
 	public void addMaterial(int maara){
 		materialAmount += maara;
 		if(isFull()){
 			setTila(KoneenTila.FULL);
 		}
 	}
 	
 	/**
 	 * Tyhjentää keittimen kaikista aineista.
 	 */
 	public void emptyProcessor(){
 		waterAmount = 0;
 		materialAmount = 0;
 		productAmount = 0;
 	}
 	 
 	/**
 	 * @return Keittimen raaka-ainekapasiteetti (kiloa)
 	 */
 	public int getMaterialAmountVolume(){
 		return materialAmountVolume;
 	}
 	
 	//----------- PRODUCT ---------------------
 	
 	/**
 	 * Method that calculates the amount of product that can be produced from material and 'makes the product'.
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
 		return (int)(100 * ((double)materialAmount / (double)materialAmountVolume) ); //prosentteina
 	}
 	
 	public int getProductPercentage(){
 		return (int) (100 * ((double)productAmount / (double)waterAmountVolume));
 	}
 	
 	//---------- PROSESSORI TILA YMS. ----------
 	
	public void setTila(KoneenTila t){
 		if(t == KoneenTila.FREE || t == KoneenTila.FILLING || t == KoneenTila.PROCESSING){
 			progress = 0;
 		}
 		tila = t;
 	}
 	
 	public KoneenTila getTila(){
 		return tila;
 	}
 	
 	/**
 	 * Returns true if materialAmount == materialAmountVolume. Else false.
 	 */
 	public boolean isFull(){
 		if (materialAmount >= materialAmountVolume){
 			return true;
 		}
 		else{
 			return false;
 		}
 	}
 	 	
 	/**
 	 * Returns true if there is no material, water or product in the processor.
 	 */
 	public boolean isEmpty(){
 		return (materialAmount == 0 && waterAmount == 0 && productAmount == 0);
 	}
 	
 	/**
 	 * Returns true if processor is reserved and false if it's not.
 	 */
 	public boolean isReserved(){
 		return reserved;
 	}
 	
 	/**
 	 * Returns true if processor is running.
 	 */
 	public boolean isRunning(){
 		return running;
 	}
 	 	 	
 }