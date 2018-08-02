//
//
//  Generated by StarUML(tm) Java Add-In
//	Modified by Harangozó
//	@Last modification: 2016.04.29.
//
//  @ Project : Grafikus
//  @ File Name : FLAF.java
//  @ Generation date : 2016.03.28.
//  @ Author : kifli csapat
//
//

public class FLAF {
    Csillagkapu[] csk;
    private FLAFLövedék lövedék;	// a fegyverből kilőtt lövedék referenciéja

    public FLAF(){	// konstruktor
        csk = new Csillagkapu[4];	//nincs lista kezdetben
        csk[Szin.kék.getValue()] = null;
        csk[Szin.sárga.getValue()] = null;
        csk[Szin.zöld.getValue()] = null;
        csk[Szin.piros.getValue()] = null;
        lövedék = null;	//nincs lövedék kezdetben
    }
    
    public void lő(Szin szin, Irány irány, DinamikusMező szomszéd){	//a fegyverhez tartozó lövés fügvény
    	if(szomszéd==null){
    		Hanglejátszó.getInstance().playSound(Hanglejátszó.getInstance().nem_talált);
    		return;	//ha olyan szomszédot kaptunk ami null, akkor visszatérünk, nem csinálunk semmit
    	}
        FLAFLövedék l = null;	//a lövedék amit ki fog lőni
        l = new FLAFLövedék(this, irány, szin);	//a lövedék létrehozása, ehhez a fegyverhez tartozik, a paraméterben kapott irányba tart, és színű
        setLövedék(l);	//eltároljuk a lövedéket amit ki fogunk lőni
        szomszéd.setLövedék(l);	//a paraméterül kapott mezőbe elhelyezzük a lövedéket
       
        if (csk != null){	//ha van csillagkapu ami a fegyverhez tartozik
        	if(csk[szin.getValue()]!=null)
        	csk[szin.getValue()].megsemmisül();	//akkor megsemmisítjük azt a csillagkaput
	        csk[szin.getValue()]=null;	//és kiszedjük a fegyver álltal számontartott csillagkapuk listájából
        }     
        
        szomszéd.mezőSzámítás();	//meghívjuk a szomszédra a mezőszámítást, hogy a lövedék elkezdhessen mozogni INSTANT
    }

    public Csillagkapu getCsK(Szin szin){	//a fegyver visszaadja az általa tárolt csillagkapuk listájábol a kért színűt, ha van, egyébként null-t ad
    	return csk[szin.getValue()];
    }

    public void setCsK(Szin szin, Csillagkapu cs){	//eltárolja a fegyver a paraméterben kapott csillagkaput
    	csk[szin.getValue()]=cs;
    	
        if (csk[szin.pár().getValue()]!=null){	//ha a hozzáadást követően a pár már megvan
        	csk[szin.getValue()].setMásik(csk[szin.pár().getValue()]);
        	csk[szin.pár().getValue()].setMásik(csk[szin.getValue()]);
        	Hanglejátszó.getInstance().playSound(Hanglejátszó.getInstance().féregjárat_kinyílt);
        }
    }
 
    public FLAFLövedék getLövedék(Szin szin) {
    	if(lövedék!=null)	//ha van kilőtt lövedék
    		if (lövedék.getSzín()==szin) return lövedék;	//és az a lövedék a paraméterben kapott színű, visszatérünk vele
    	return null;	//ha nincs kilőtt lövedék, vagy az nem a paraméterben kapott színű, akkor nullal térünk vissza
    }
 
    public void setLövedék(FLAFLövedék fl) {	//a paraméterben kapott lövedéket tárolja el a fegyver
        lövedék = fl;	//beállítja mint a FLAFhoz tartozó lövedék
    }
}