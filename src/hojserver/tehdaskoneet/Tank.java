package hojserver.tehdaskoneet;

/*
- Tilavuus 10000 litraa
- Vain yksi pumppu voi täyttää tai tyhjentää säiliötä kerrallaan.
 */

public class Tank extends Thread {

	private final int tilavuus = 10000; //litraa
	private int amountOfLiquid;
	private boolean reserved;
	
	private KoneenTila tila;
	private int pump;
	private int bottlepump;
	
	//------------ KONSTRUKTORI -------------
	public Tank(){
		amountOfLiquid = 0;
		reserved = false;
		tila = KoneenTila.FREE;
		pump = -1;
		bottlepump = -1;
	}
	
	//------------- TILA ------------------
	
	public void setTila(KoneenTila t){
		tila = t;
	}
	
	public KoneenTila getTila(){
		return tila;
	}
	
	public synchronized boolean isReserved(){
		return reserved;
	}
	
	public synchronized void setReserved(boolean r){
		if(r == false){
			setBottlePump(-1);
			if(isFull()){
				setTila(KoneenTila.FULL);
			} else {
				setTila(KoneenTila.FREE);
			}
		}
		reserved = r;
	}
	
	public boolean isFull(){
		if (amountOfLiquid >= tilavuus){
			return true;
		}
		else{
			return false;
		}
	}

	//-------------- PUMP ----------------------

	public int getPump(){
		return pump;
	}
	
	public void setPump(int p){
		pump = p;
	}
	
	//-------------- BOTTLE PUMP ----------------------	
	
	public int getBottlePump(){
		return bottlepump;
	}
	
	public void setBottlePump(int p){
		bottlepump = p;
	}
	
	//---------------- LIQUID -----------------------
	
	/**
	 * Returns the amount of liquid in the tank.
	 * @return
	 */
	public synchronized int getAmountOfLiquid(){
		return amountOfLiquid;
	}
	
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
