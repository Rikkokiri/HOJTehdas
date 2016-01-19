package hojserver.tehdaskoneet;

/*
- raaka-aineen tyyppi
- täyttöaste 0-10000 kiloa
- siiloa ei voi tyhjentää ja täyttää samaan aikaan
- Samasta siilosta voi ottaa raaka-ainetta vain yksi kuljetin kerrallaan

 */

public class Silo {

	private final int capacity = 10000; //kiloa
	private int degreeOfFilling; //0-10000 kiloa				//Not happy with the name, but I'll go with this
	private boolean reserved;
	private KoneenTila tila;
	
	//Constructor
	public Silo(){
		//Oletusarvoisesti siilo on tyhjä
		degreeOfFilling = 0;
		tila = KoneenTila.FREE;
	}
	
	//---- GETTERS AND SETTERS ----
	
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
	
	public boolean isReserved(){
		return reserved;
	}
	
	/**
	 * Metodilla muutetaan siilon varauksen tilaa. Asetetaan siis vapaaksi/varatuksi.
	 * @param r
	 */
	public void setReserved(boolean r){
		
		//TODO Miten varauksen tekemiseen / vapauttamiseen reagoidaan eri tiloissa
		
		if(tila == KoneenTila.EMPTYING){
			//TODO
		}
		if(tila == KoneenTila.FILLING){
			//TODO
			
		}
		
		reserved = r;

	}
	
	//------------------
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
	
	/**
	 * 
	 * @param fill
	 */
	/*
	 * TURHA METODI?
	 */
	public void resetDegreeOfFilling(int fill){ 
		if(fill <= capacity){
			degreeOfFilling = fill;
		} else{
			degreeOfFilling = capacity;
		}
	}
	
	/**
	 * Method that adds material to silo.
	 * @param addition
	 */
	public void addToSilo(int addition){
		if(degreeOfFilling + addition <= capacity){
			degreeOfFilling += addition;
		}
		else{
			//TODO
			
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
			//Testaamista varten?
			System.out.println("Tried to take " + take + " kilos but only " + degreeOfFilling + " kilos in silo. Taking " + degreeOfFilling + " kilos.");
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
