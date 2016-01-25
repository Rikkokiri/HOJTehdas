package hojserver.tehdaskoneet;

/*
- Siirtää juomaa 500 litraa sekunnissä
- Säiliöitä ei saa ylitäyttää
- Tyhjästä säiliöstä ei voi pumpata pullotukseen
- Pullotukseen oletetaan mahtuvan aina niin paljon kuin säiliössä on juomaa
 */

// TODO PulloPumpussa jotai vikaa?
// Ei suostu välillä ottamaan säiliöistä tavaraa

public class BottlePump extends Pump {

	private Tank[] tanks;
	private final int take = 50; //Kerralla tankista otettava määrä (litraa)
	private final int identity;
	
	private Tank tankToBeEmptied;
	 
	//------------ KONSTRUKTORI --------------
	public BottlePump(Tank[] tanks, int id){
		super();
		this.tanks = tanks;
		this.identity = id;
		tankToBeEmptied = null;
	}
	
	//------------------- ID ----------------------
	
	public int getIdentity(){
		return identity;
	}
	
	//----------- TANK TO BE EMPTIED ----------------
	
	public Tank getTankToBeEmptied(){
		return tankToBeEmptied;
	}
	
	//----------- STOP PUMP ----------------
	
	@Override
	public void stopPump(){
		running = false;
		if(tankToBeEmptied != null){
			tankToBeEmptied.setTila(KoneenTila.FREE);
			tankToBeEmptied = null;
		}	
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
					 * - tankkia ei käytä mikään muu pumppu
					 */
					while((tank.getTila() == KoneenTila.FREE || tank.getTila() == KoneenTila.EMPTYING || tank.getTila() == KoneenTila.FULL)
							&& tank.getAmountOfLiquid() != 0 && tank.isReserved() && (tank.getBottlePump() == identity || tank.getBottlePump() == -1)){
		
							//Tank to be emptied chosen
							tankToBeEmptied = tank;
						
							//Asetetaan kypsytyssäiliö tyhjennystilaan
							tank.setTila(KoneenTila.EMPTYING);
							tank.setBottlePump(identity);
							
							//Let's check if we can take what we want
							if(tank.getAmountOfLiquid() > take){
								tank.takeLiquid(take);
							} else {
								//Jos pyydettyä määrää ei nää ole, otetaan kaikki mitä on jäljellä
								tank.takeLiquid(tank.getAmountOfLiquid());
								//Vapautetaan tankki
								tank.setTila(KoneenTila.FREE);
								tank.setReserved(false);
								tank.setBottlePump(-1);
								tankToBeEmptied = null;
							}
							synchronized(this){
								try {
									this.wait(100);
								} catch (InterruptedException e) { e.printStackTrace(); }
							} //synchronized
						} //while(ehtokasa)
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
