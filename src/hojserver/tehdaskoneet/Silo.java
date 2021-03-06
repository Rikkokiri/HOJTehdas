package hojserver.tehdaskoneet;

/*
- raaka-aineen tyyppi
- täyttöaste 0-10000 kiloa
- siiloa ei voi tyhjentää ja täyttää samaan aikaan
- Samasta siilosta voi ottaa raaka-ainetta vain yksi kuljetin kerrallaan

 */

/*
 * Luokka siiloille
 */

public class Silo {

	private final int capacity = 10000; //kiloa
	private int degreeOfFilling; //0-10000 kiloa
	
	private boolean reserved;
	private KoneenTila tila;
	private int conveyer;
	
	
	// ---------- KONSTRUKTORI ---------- //
	
	public Silo(){
		degreeOfFilling = 0;
		tila = KoneenTila.FREE;
		conveyer = -1;
	}
	
	// ---------- GETTERS AND SETTERS ---------- //
	
	public int getCapacity(){
		return capacity;
	}
	
	public int getDegreeOfFilling(){
		return degreeOfFilling;
	}
	
	public KoneenTila getTila(){
		return tila;
	}
	
	public void  setTila(KoneenTila t){
		tila = t;
	}
	public int getConveyer(){
		return conveyer;
	}
	
	public void setConveyer(int conv){
		conveyer = conv;
	}
	
	public boolean isReserved(){
		return reserved;
	}
	
	public void setReserved(boolean r){
		// Jos varaus poistetaan niin tila muutetaan
		if (!r){
			tila = KoneenTila.FREE;
		}
		
		reserved = r;

	}
	
	/**
	 * Method tells whether the silo is full or not
	 * @return boolean true if silo is full, false if silo is not full
	 */
	public boolean isFull(){
		if(degreeOfFilling >= capacity){
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isEmpty(){
		if (degreeOfFilling <= 0){
			return true;
		}
		else{
			return false;
		}
	}
	
	/**
	 * 
	 * @param fill
	 */
	public void resetDegreeOfFilling(int fill){ 
		if(fill <= capacity){
			degreeOfFilling = fill;
		} else{
			degreeOfFilling = capacity;
		}
	}
	
	
	// ---------- LISÄYS / POISTO ---------- //
	
	/**
	 * Method that adds material to silo.
	 * @param addition
	 */
	public void addToSilo(int addition){
		if(degreeOfFilling + addition <= capacity){
			degreeOfFilling += addition;
		}
	}
	
	public void removeFromSilo(int sub){
		if (degreeOfFilling - sub >= 0){
			degreeOfFilling = degreeOfFilling - sub;
		}
	}
	
	/**
	 * Fills the silo to the maximum capacity.
	 */
	public void fillSilo(){
		degreeOfFilling = capacity;
	}
	
	/**
	 * Method for taking material from the silo. 
	 * @param take
	 */
	public void takeFromSilo(int take){
		if(take <= degreeOfFilling){
			degreeOfFilling -= take;
		} else {
			degreeOfFilling = 0;
		}
	}
	
	/**
	 * Takes everything from the silo.
	 */
	public void emptySilo(){
		degreeOfFilling = 0;
	}
	
} //Processor
