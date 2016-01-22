package hojserver.tehdaskoneet;

/*
- Siirtää juomaa 500 litraa sekunnissä
- Säiliöitä ei saa ylitäyttää
- Tyhjästä säiliöstä ei voi pumpata pullotukseen
- Pullotukseen oletetaan mahtuvan aina niin paljon kuin säiliössä on juomaa
 */

public class BottlePump extends Pump {

	private Tank[] tanks;
	private final int take = 50; //Kerralla tankista otettava määrä (litraa)
	 
	//------------ KONSTRUKTORI --------------
	public BottlePump(Tank[] tanks){
		super();
		this.tanks = tanks;
	}
	
	//-------------- RUN-METODI ---------------------
	public void run(){
		while(true){
			
			while(isRunning()){ 	//Kun pullotuspumppu on asetettu käyntiin....
				
				//Iteroidaan säiliöiden läpi ja katsomaan, mikä niistä on tyhjennettävissä
				for(Tank tank : tanks){
					/* Säiliötä voi alkaa tyhjentää, jos
					 * - sen tila on vapaa/tyhjennyksessä/täysi
					 * - säiliössä on nestettä
					 * - säiliön reserve-painike on pohjassa
					 */
					if((tank.getTila() == KoneenTila.FREE || tank.getTila() == KoneenTila.EMPTYING || tank.getTila() == KoneenTila.FULL)
							&& tank.getAmountOfLiquid() != 0 && tank.isReserved()){
					
						//Asetetaan kypsytyssäiliö tyhjennystilaan
						tank.setTila(KoneenTila.EMPTYING);
						
						//Let's check if we can take what we want
						if(tank.getAmountOfLiquid() >= take){
							tank.takeLiquid(take);
						} else {
							//Jos pyydettyä määrää ei nää ole, otetaan kaikki mitä on jäljellä
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
			
			//Pieni (0.5 sekunnin) viive hidastamaan while(true)-loopin pyörimistä
			synchronized(this){
				try {
					this.wait(500);
				} catch (InterruptedException e) {	e.printStackTrace(); }
			}
		} //while(true)
	} //run 
//-----------------------------------------------------
}
