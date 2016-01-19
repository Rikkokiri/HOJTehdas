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
	
	public BottlePump(Tank[] tanks){
		super();
		this.tanks = tanks;
	}
	
	//>>>> RUN-metodi <<<<
	public void run(){
		//TODO
	}
	
	/**
	 * Moves the amount 'amount' from tank to bottling
	 * @param tanknumber
	 * @param amount
	 */
	public void moveBeverage(int tanknumber, int amount){
		
		//Tarkista onko tankissa pyydetty määrä nestettä
		if(tanks[tanknumber-1].getAmountOfLiquid() >= amount){
			//Aloita siirtäminen...
			
			
		} else { 
			//Tankissa ei ole pyydettyä määrää nestettä.
			//TODO
		}
				
		
		
		
		/*
		synchronized(this){
			try {
				this.wait((amount/500) * 100);
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				System.out.println("Pumpun toiminta häiriintynyt");
				e.printStackTrace();
			}
		}*/
	}
	
	/**
	 * Empties the whole tank.
	 * @param tanknumber
	 */
	public void moveBeverage(int tanknumber){

	}

	
	
}
