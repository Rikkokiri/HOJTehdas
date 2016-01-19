package hojserver.tehdaskoneet;

/*
- Siirtää juomaa 500 litraa sekunnissa
- Pumpulle annetaan parametrina siirrettävä määrä tai se voidaan asettaa tyhjentämään koko yksikkö
- Säiliöitä ei saa ylitäyttää
- Tyhjästä säiliöstä ei voi pumpata pullotukseen
- Pullotukseen oletetaan mahtuvan aina niin paljon kuin säiliössä on juomaa
 */

public class TankPump extends Pump {
	
	private Processor[] processors;
	private Tank[] tanks;
	
	public TankPump(Processor[] processors, Tank[] tanks){
		super();
		this.processors = processors;
		this.tanks = tanks;
	}
	
	public void run(){
		//TODO
		
		
	}
	
}
