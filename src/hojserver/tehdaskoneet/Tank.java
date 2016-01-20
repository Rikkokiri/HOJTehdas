package hojserver.tehdaskoneet;

/*
- juoman nimi
- tilavuus 10000 litraa
- vain yksi pumppu voi täyttää tai tyhjentää säiliötä kerrallaan
 */

public class Tank extends Thread {

	private final int tilavuus = 10000; //litraa
	private int amountOfLiquid;
	private boolean reserved;
	
	private KoneenTila tila;
	private int pump;
	
	public Tank(){
		amountOfLiquid = 0;
		reserved = false;
		tila = KoneenTila.FREE;
		pump = -1;
	}
	
	/**
	 * Returns the amount of liquid in the tank.
	 * @return
	 */
	public synchronized int getAmountOfLiquid(){
		return amountOfLiquid;
	}
	

	//---- Tankin tila ---------
	
	//Varaus
	
	public synchronized boolean isReserved(){
		return reserved;
	}
	
	public synchronized void setReserved(boolean r){
		reserved = r;
	}
	
	public void setTila(KoneenTila t){
		tila = t;
	}
	
	public KoneenTila getTila(){
		return tila;
	}
	
	public boolean isFull(){
		if (amountOfLiquid >= tilavuus){
			return true;
		}
		else{
			return false;
		}
	}
	
	//--- Can tank be emptied or filled? ---------
	
	public boolean canBeEmptied(){
		//Tankkia voidaan alkaa tyhjeentää, jos sitä ei parhaillaan täytetä eikä tyhjennetä ja tankissa on nestettä 
		if(tila != KoneenTila.FILLING && reserved && amountOfLiquid != 0){
			return true;
		} else {
			return false;
		}
	}
	
	public boolean canBeFilled(){
	//Tankkia voidaan alkaa tyhjeentää, jos sitä ei parhaillaan täytetä eikä se ole täynnä
		if(tila != KoneenTila.EMPTYING && tila != KoneenTila.FILLING && reserved && amountOfLiquid != tilavuus){
			return true;
		} else {
			return false;
		}
	}
	
	// ------------------------------------
	//		      PUMP
	
	public int getPump(){
		return pump;
	}
	
	public void setPump(int p){
		pump = p;
	}
	
	//---------------------------------------
	//		Nesteen siirtely
	
	/**
	 * Method for taking liquid from the tank.
	 * @param amount
	 */
	public synchronized void takeLiquid(int amount){
		if(amount <= amountOfLiquid){
			amountOfLiquid -= amount;
		}
	}
	
	/**
	 * Method for adding liquid to the tank.
	 * @param amount
	 */
	public synchronized void addLiquid(int amount){
		if(amount <=  tilavuus - amountOfLiquid){
			amountOfLiquid += amount;
		}
	}
	
}
