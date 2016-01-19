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
	
	public Tank(){
		amountOfLiquid = 0;
	}
	
	/**
	 * Returns the amount of liquid in the tank.
	 * @return
	 */
	public synchronized int getAmountOfLiquid(){
		return amountOfLiquid;
	}
	
	public synchronized boolean isReserved(){
		return reserved;
	}
	
	public synchronized void setReserved(boolean r){
		reserved = r;
	}
	
	/**
	 * Method for taking liquid from the tank.
	 * @param amount
	 */
	public synchronized void takeLiquid(int amount){
		if(amount <= amountOfLiquid){
			amountOfLiquid -= amount;
		} else {
			//Yritetään ottaa tankista enemmän nestettä kuin siellä on.
			//TODO
		}
	}
	
	/**
	 * Method for adding liquid to the tank.
	 * @param amount
	 */
	public synchronized void addLiquid(int amount){
		if(amount <=  tilavuus - amountOfLiquid){
			amountOfLiquid += amount;
		} else {
			//Yritetään laittaa tankkiin enemmän nestettä kuin sinne mahtuu.
			//TODO
		}
	}
	
}
