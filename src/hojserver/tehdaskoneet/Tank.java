package hojserver.tehdaskoneet;

/*
- juoman nimi
- tilavuus 10000 litraa
- vain yksi pumppu voi täyttää tai tyhjentää säiliötä kerrallaan
 */

public class Tank {

	private final int tilavuus = 10000; //litraa
	private int nestemäärä;
	private boolean reserved;
	
	public Tank(){
		nestemäärä = 0;
	}
	
	public int getNestemäärä(){
		return nestemäärä;
	}
	
	public boolean isReserved(){
		return reserved;
	}
	
	/*
	public void setNestemäärä(int m){
		if(m <= tilavuus){
			nestemäärä = m;
		}else{
			nestemäärä = tilavuus; // ???????
		}
	}
	*/
}
