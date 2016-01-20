package hojserver.tehdaskoneet;

/*
- Siirtää juomaa 500 litraa sekunnissa
- Pumpulle annetaan parametrina siirrettävä määrä tai se voidaan asettaa tyhjentämään koko yksikkö
- Säiliöitä ei saa ylitäyttää
- Tyhjästä säiliöstä ei voi pumpata pullotukseen
- Pullotukseen oletetaan mahtuvan aina niin paljon kuin säiliössä on juomaa
 */

public class BottlePump extends Pump {

	private Tank[] tanks;
	private final int wait = 100;
	private final double speed = 0.5; //litraa/millisekunti
	 
	public BottlePump(Tank[] tanks){
		super();
		this.tanks = tanks;
	}
	
	//>>>> RUN-metodi <<<<
	public void run(){
		
		while(isRunning()){
			
			for(Tank tank : tanks){
				while(tank.canBeEmptied()){
					tank.setTila(KoneenTila.EMPTYING);
					tank.takeLiquid((int)(wait*speed));
					
					synchronized(this){
						try {
							this.wait(wait);
						} catch (InterruptedException e) { e.printStackTrace(); }
					} //synchronized
				} //while - filling
			}//for(tanks)
		} //while(isRunning)
	} //run
	
	/**
	 * Moves the amount 'amount' from tank to bottling
	 * @param tanknumber
	 * @param amount
	 */
	/*
	 * Tässä metodissa ei vain siinä mielessä ole mitään järkeä, että käyttäjä ei voi mitenkään antaa
	 * pumpulle siirrettävää määrää.
	 */
	public synchronized void moveBeverage(int tanknumber, int amount){
		
		//Tarkista onko tankissa pyydetty määrä nestettä
		if(tanks[tanknumber-1].getAmountOfLiquid() >= amount){

			tanks[tanknumber-1].setTila(KoneenTila.EMPTYING);
			
			//TODO Nykyinen ratkaisu ei mahdollista tankissa olevan määrän reaaliaikaista päivittämistä.
			
			//Aloita siirtäminen...
			synchronized(this){
				try {
					System.out.println(amount + " litran siirtäminen kestää " + (amount/500)*100 + " sekuntia");
					this.wait((amount/500)*100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			tanks[tanknumber-1].takeLiquid(amount);
			
		} else { 
			//Tankissa ei ole pyydettyä määrää nestettä.
			//TODO
		}

	}
	


	public synchronized void moveBeverage(int tanknumber){
		
		/*
		boolean emptyingStarted = false;
		while(!emptyingStarted){
			for(Tank tankki : tanks){
				if(tankki.canBeEmptied()){
					
				}}}*/

	}

	
	
}
