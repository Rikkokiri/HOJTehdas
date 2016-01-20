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
	private final int take = 50;
	 
	public BottlePump(Tank[] tanks){
		super();
		this.tanks = tanks;
	}
	
	public void run(){
		while(true){
			
			while(isRunning()){
				//Let's check which tank can be emptied first
				for(Tank tank : tanks){
				
					if((tank.getTila() == KoneenTila.FREE || tank.getTila() == KoneenTila.EMPTYING || tank.getTila() == KoneenTila.FULL)
							&& tank.getAmountOfLiquid() != 0 && tank.isReserved()){
						
						System.out.println("Päästäänkö tänne?");
						
						tank.setTila(KoneenTila.EMPTYING);
						
						//Let's check if we can take what we want
						if(tank.getAmountOfLiquid() >= take){
							tank.takeLiquid(take);
						} else {
							//Taking all that's left
							tank.takeLiquid(tank.getAmountOfLiquid());
							tank.setTila(KoneenTila.FREE);
							tank.setReserved(false);
						}
						synchronized(this){
							try {
								this.wait(100);
							} catch (InterruptedException e) { e.printStackTrace(); }
						} //synchronized
					}
				} //for
				
			} //while(isRunning)
			
			//Let's wait a little so while(true)-loop doesn't spin like crazy.
			synchronized(this){
				try {
					this.wait(500);
				} catch (InterruptedException e) {	e.printStackTrace(); }
			}
		} //while(true)
	}
	
	/*
	//>>>> RUN-metodi <<<<
	public void run(){
		while(true){
			
			while(isRunning()){		
				for(Tank tank : tanks){
					
					while(tank.canBeEmptied()){
						
						tank.setTila(KoneenTila.EMPTYING);
						tank.takeLiquid((int)(50));
						
						System.out.println("Taking out liquid from " + tank);
						
						synchronized(this){
							try {
								this.wait(100);
							} catch (InterruptedException e) { e.printStackTrace(); }
						} //synchronized
					} //while - filling
				}//for(tanks)
			} //while(isRunning}
			
			synchronized(this){
				try {
					this.wait(250);
				} catch (InterruptedException e) { e.printStackTrace(); }
			} //synchronized
		}//while true
	} //run
	*/
	
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
}
