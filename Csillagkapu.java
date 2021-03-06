//
//
//  Generated by StarUML(tm) Java Add-In
//	Modified by Harangozó
//	@Last modification: 2016.04.29.
//
//  @ Project : Grafikus
//  @ File Name : Csillagkapu.java
//  @ Generation date : 2016.03.28.
//  @ Author : kifli csapat
//
//

public class Csillagkapu {
	Szin szin;   						// A Csillagkapu színe
	private DinamikusMező hova;	 		// Az érkezési mező, ahova ez a csillagkapu kirak
	private Csillagkapu másik;			// A Csillagkapu párja, ha a féregjárat aktív
	private Irány frontIrány;
	
	public Csillagkapu(Szin sz,Irány i, DinamikusMező érkező) { // Konstruktor szín és érkező paraméterekkel
	    szin = sz;	//a paraméterben kapott színt kapja meg a csillagkapu
	    másik = null;	// Alapból a másik csillagkapu ismeretlen, a FLAF majd nyomon tartja, kinek kivel kell lennie
	    hova = érkező;	//a paraméterül kapott érkezőt állítja be érkezőnek
	    frontIrány = i;
	}
	
	public Irány getFrontIrány(){
		return frontIrány;
	}
	
	public Csillagkapu getMásik() {		// Visszaadja a másik Csillagkaput
		return másik;	//visszatér az eltárolt csillgakapuval ha van, ha nincs null al
	}
	
	public void setMásik(Csillagkapu csk1) { // Beállítja a másik Csillagkaput
		másik = csk1;	//beállítja a paraméterül kapott értékre a másik attribútumot (null vagy csk)
	}
	
	public void megsemmisül() { // Kiveszi a Csillagkaput a pályáról
		DinamikusMező sz1 = null, sz2 = null, sz3 = null, sz4 = null;
		if (hova.getSzomszéd(Irány.n) != null) { // északi szomszéd
			sz1 = hova.getSzomszéd(Irány.n);
		}
		if (hova.getSzomszéd(Irány.w) != null) { // nyugati szomszéd
			sz2 = hova.getSzomszéd(Irány.w);
		}
		if (hova.getSzomszéd(Irány.s) != null) { // déli szomszéd
			sz3 = hova.getSzomszéd(Irány.s);
		}
		if (hova.getSzomszéd(Irány.e) != null) { // keleti szomszéd
			sz4 = hova.getSzomszéd(Irány.e);
		}
		
		if (sz1 != null) {
			if(sz1.getCsk()==this){ // Ha az északi szomszédban van a Csillagkapu...
				sz1.setCsk(null);	// kivesszük az északi szomszédból
			}
		}
		if (sz2 != null) {
			if(sz2.getCsk()==this){ // Ha a nyugati szomszédban van a Csillagkapu...
				sz2.setCsk(null);	// kivesszük a nyugati szomszédból
			}
		}
		if (sz3 != null) {
			if(sz3.getCsk()==this){	// Ha a déli szomszédban van a Csillagkapu...
				sz3.setCsk(null);	// kivesszük a déli szomszédból
			}
		}
		if (sz4 != null) {
			if(sz4.getCsk()==this){	// Ha a keleti szomszédban van a Csillagkapu...
				sz4.setCsk(null);	// kivesszük a keleti szomszédból
			}
		}

		// Ha ennek a Csillagkapunak a szomszédja létezik, akkor a szomszéd ezen Csillagkapura
		// mutató referenciáját töröljük
		if(this.getMásik()!=null){
			this.getMásik().setMásik(null);
		}
	}
	
	public Szin getSzín() { 	// Visszaadja a Csillagkapu színét
		return szin;
	}
	
	public DinamikusMező getHova() {	// Visszaadja a kimeneti Csillagkapu mezőjét
		return hova;
	}
	
}