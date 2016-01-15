package hojserver.tehdaskoneet;

/*
- raaka-aineen tyyppi
- täyttöaste 0-10000 kiloa
- siiloa ei voi tyhjentää ja täyttää samaan aikaan
- Samasta siilosta voi ottaa raaka-ainetta vain yksi kuljetin kerrallaan

 */

public class Silo {

	private final int capacity = 10000; //kiloa
	private int filled; //0-10000 kiloa				//Not happy with the name, but I'll go with this
	
	public Silo(){
		//Oletusarvoisesti siilo on tyhjä
		filled = 0;
	}
	
	//---- GETTERS AND SETTERS ----
	
	public int getKapasiteetti(){
		return capacity;
	}
	
	public int getTäyttöaste(){
		return filled;
	}
	
	/**
	 * 
	 * @param fill
	 */
	public void resetFilled(int fill){ 
		if(fill <= capacity){
			filled = fill;
		} else{
			filled = capacity;
		}
	}
	
	/**
	 * 
	 * @param addition
	 */
	public void fillSilo(int addition){
		if(filled + addition <= capacity){
			filled += addition;
		}
		else{
			//TODO
		}
	}
	
	
}
