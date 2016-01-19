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
	private int progress;
	
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
					this.wait(processingtime);
					//Kun on odotettu prosessointiajan verran, juoma valmis
					running = false;
					// TODO Mihin tilaan keitin asetetaan?
					//this.setTila( ? );
				} catch (InterruptedException e) {
					System.out.println("Juomakeittimen prosessointi keskeytyi");
					e.printStackTrace();
				}
			}
		}
		
	}
	
	
	// <<<< TILA >>>>
	
	public void setTila(KoneenTila tila){
		this.tila = tila;
	}
	
	// <<<< USER >>>>
	
	public void setUser(String k){
		user = k;
	}
	
	public String getUser(){
		return user;
	}
	
	public void setReserved(boolean r){
		reserved = r;
	}
	

	// <<<< VESI JA RAAKA-AINE >>>>
	
	// --- Getterit ---
	public int getWaterAmount(){
		return waterAmount;
	}
	
	public int getMaterialAmount(){
		return materialAmount;
	}
	
	public int getProgress(){
		return progress;
	}
	
	public void process(int maara){
		progress =  progress + maara;
	}
	
	public boolean isReserved(){
		return reserved;
	}
	
	public boolean isRunning(){
		return running;
	}
	
	public void setRunning(boolean r){
		running = r;
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
