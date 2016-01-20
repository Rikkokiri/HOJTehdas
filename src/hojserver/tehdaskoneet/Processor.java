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
	private final int processingtime = 20000; //millisekuntia
	
	private int waterAmount;
	private int materialAmount;
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
		
		while(running && tila == KoneenTila.PROSESSING){ 		//Tuplaehto turhaan?
		
			synchronized (this) {
				try {
					this.wait(processingtime/40000); //puoli sekunti					
				} catch (InterruptedException e) {
					System.out.println("Juoman keittäminen keskeytyi keittimessä " + this);
					e.printStackTrace();
				}
			}
			
			progress += 0.5;
			
			if(progress == 100){ //Kun on odotettu prosessointiajan verran, juoma valmis
				running = false;
				this.setTila(KoneenTila.READY);
				System.out.println("Juoma valmis keittimessä " + this);
			}
		}//while
		
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
	
	public void setReserved(boolean r){
		//>>> Reserve-painike vapautetaan...
		//Ok, kunhan prosessointi ei ole käynnissä
		if(r == false && tila != KoneenTila.PROSESSING){
			reserved = r;
		}
		//Reserve-painikkeen painaminen pohjaan on aina ok
		if(r == true){
			reserved = r;
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
			} else {
				System.out.println("Prosessorin " + this + " start-painiketta ei voi painaa.");
			}
		}
		
		//Start-painike vapautetaan....
		if(r == false){
			//Keitin on täynnä tai vapaa ja ei tyhjä
			if(tila == KoneenTila.READY || tila == KoneenTila.FULL || tila == KoneenTila.FREE){
				running = false;
			}
		}//if
		
	}//setRunning

	// <<<< VESI JA RAAKA-AINE >>>>
	
	// --- Getterit ---
	public int getWaterAmount(){
		return waterAmount;
	}
	
	public int getMaterialAmount(){
		return materialAmount;
	}
	
	public void addMaterial(int maara){
		materialAmount = materialAmount + maara;
	}
	
	// <<<< Tila jne. >>>>
	
	public double getProgress(){
		return progress;
	}
	
	public int getMaterialAmountVolume(){
		return materialAmountVolume;
	}
	
	public void process(int maara){ 		//TODO Turha metodi?
		progress =  progress + maara;
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
		return (materialAmount == 0 && waterAmount == 0);
	}
	
	/**
	 * Metodi, jolla tyhjennetään keitin, kun juoma on valmista.
	 */
	//JÄRKEVÄ????
	public void emptyingProcessor(){
		waterAmount = 0;
		materialAmount = 0;
	}
	
}
