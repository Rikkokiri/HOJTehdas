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
	
	private int tankToBeEmptied;
	 
	//------------ KONSTRUKTORI --------------
	public BottlePump(Tank[] tanks, int id){
		super();
		this.tanks = tanks;
		this.identity = id;
		tankToBeEmptied = -1;
	}
	
	//------------------- ID ----------------------
	
	public int getIdentity(){
		return identity;
	}
	
	//----------- TANK TO BE EMPTIED ----------------
	
	public int getTankToBeEmptied(){
		return tankToBeEmptied;
	}
	
	public void setTankToBeEmptied(int i){
		tankToBeEmptied = i;
	}
	
	//----------- STOP PUMP ----------------
	
	@Override
	public void stopPump(){
		running = false;
		if(tankToBeEmptied != -1){
			tanks[tankToBeEmptied].setTila(KoneenTila.FREE);
			setTankToBeEmptied(-1);
		}	
	}
	
	//-------------- RUN-METODI ---------------------
	public void run(){
		while(true){
			
			while(isRunning()){ 	//Kun pullotuspumppu on asetettu käyntiin....
				
				//Iteroidaan säiliöiden läpi ja katsomaan, mikä niistä on tyhjennettävissä
				for(int i = 0; i < tanks.length; i++){
					/* Säiliötä voi alkaa tyhjentää, jos
					 * - sen tila on vapaa/tyhjennyksessä/täysi
					 * - säiliössä on nestettä
					 * - säiliön reserve-painike on pohjassa
					 * - tankkia ei käytä mikään muu pumppu
					 */
					while((tanks[i].getTila() == KoneenTila.FREE || tanks[i].getTila() == KoneenTila.EMPTYING || tanks[i].getTila() == KoneenTila.FULL)
							&& tanks[i].getAmountOfLiquid() != 0 && tanks[i].isReserved() && (tanks[i].getBottlePump() == identity || tanks[i].getBottlePump() == -1)){
		
							//Tank to be emptied chosen
							tankToBeEmptied = i;
						
							//Asetetaan kypsytyssäiliö tyhjennystilaan
							tanks[i].setTila(KoneenTila.EMPTYING);
							tanks[i].setBottlePump(identity);
							
							//Let's check if we can take what we want
							if(tanks[i].getAmountOfLiquid() > take){
								tanks[i].takeLiquid(take);
							} else {
								//Jos pyydettyä määrää ei nää ole, otetaan kaikki mitä on jäljellä
								tanks[i].takeLiquid(tanks[i].getAmountOfLiquid());
								//Vapautetaan tankki
								tanks[i].setTila(KoneenTila.FREE);
								tanks[i].setReserved(false);
								tanks[i].setBottlePump(-1);
								setTankToBeEmptied(-1);
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
