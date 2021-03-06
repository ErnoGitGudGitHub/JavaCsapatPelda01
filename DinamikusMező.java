//
//
//  Generated by StarUML(tm) Java Add-In
//	Modified by Harangozó
//	@Last modification: 2016.04.29.
//
//  @ Project : Grafikus
//  @ File Name : DinamikusMező.java
//  @ Generation date : 2016.03.28.
//  @ Author : kifli csapat
//
//

import java.util.ArrayList;

public class DinamikusMező{
	private ArrayList<Tárgy> tárgy;	//a tárgyak listája, ha a lista üres, azt eldobjuk, és nullra állítjuk
	private Ezredes ezredes;	//a mezőn levő ezredes
	private FLAFLövedék lövedék;	//A mezőben található lövedék
	private DinamikusMező[] szomszéd;	//A szomszédokat tárolja, kezdetben mind a 4 irány null
	private StatikusMező pár;	//az adott DinamikusMezőhöz tartozó StatikusMező
	private Csillagkapu csk;	//a mezőben levő csillagkapu
	private Replikátor replikátor;	//A DinamikusMezőn lévő Replikátort tárolja.
	private Szerkesztői editor;
	
	public Szerkesztői getEditor(){
		return editor;
	}
	
	public void setEditor(Szerkesztői szerkesztői) {
		editor=szerkesztői;
	}

	public DinamikusMező() {
		tárgy = null;
		ezredes = null;
		lövedék = null;
		editor=null;

		szomszéd = new DinamikusMező[]{null, null, null , null};
		pár = null;
		csk = null;
		replikátor=null;
	}
	
	public int getTömeg(Pályaelem pe) {	//rekurzívan kiszámolja az össze DinamikusMező tömegét, ami a paraméterhez tartozik
		int ret=0;
		
		if(szomszéd[Irány.n.getValue()]!=null && szomszéd[Irány.n.getValue()].getPár().getPályaelem()!=pe){//ha az északi nem tartozik hozzánk!
			if(szomszéd[Irány.s.getValue()]!=null && szomszéd[Irány.s.getValue()].getPár().getPályaelem()==pe)	//ha a déli ugyan oda, 'pe'hez tartozik
				if(szomszéd[Irány.e.getValue()]!=null && szomszéd[Irány.e.getValue()].getPár().getPályaelem()==pe){	//ha a keleti és a déli is ugyan oda tartozik
					ret+=szomszéd[Irány.e.getValue()].getTömeg(pe);	//a keleti szomszéd tömegére kíváncsiak vagyunk
					ret+=szomszéd[Irány.s.getValue()].getTömeg(pe);	//és a déli tömegére is
				}
				else //ha csak a déli tartozik hozzánk
					if(szomszéd[Irány.s.getValue()]!=null) ret+=szomszéd[Irány.s.getValue()].getTömeg(pe);	//a délire azért meghívjuk
		}	//ha az északi hozzánk tartozik
		else	//akkor csak délre kérdezzük
			if(szomszéd[Irány.s.getValue()]!=null && szomszéd[Irány.s.getValue()].getPár().getPályaelem()==pe)	//ha a déli szomszéd még hozzánk tartozik
				ret+=szomszéd[Irány.s.getValue()].getTömeg(pe);	//a déli szomszéd tömegét hozzáadjuk

		if(pár.getPályaelem()==pe){	//ha az adott DinamikusMező a 'pe' hez tartozik
			if(this.getEzredes()!=null) ret+=65;	//ha ezredes tartózkodik a mezőn, előre kalibrált tömeggel számol
			if (tárgy!=null) {	//ha van tárgy is a mezőn, akkor azoknak a tömegét is hozzáadja
				for(int i=0;i<tárgy.size();i++)	//végig a listán
					ret+=tárgy.get(i).getTömeg();	//lásd: egymásra tehető dobozok
			}
		}
		return ret;
	}
	
	
	public ArrayList<Tárgy> getTárgy(){	//Getter a tárgy ArrayListhez
		return tárgy;	//visszatérünk a poppa, vagy nullal
	}
	
	public Tárgy popTárgy(){	//kiszedi a tárgyat az adott DinamikusMezőröl
		Tárgy ret=null;
		
		if(tárgy!=null){	//ha van tárgylista. Ekkor abban legalább egy elem is van
			ret=tárgy.remove(tárgy.size()-1);	//poppolunk a listából
			if(tárgy.size()==0) tárgy=null;	//ha popp után üres, eldobjuk a listát.
		}
		return ret;	//visszatérünk a poppa, vagy nullal
	}
	
	public void setTárgy(Tárgy t) {	//a paraméterben kapott tárgyat a DinamikusMezőre állítja a listába
		if(t==null){	//ha a kapott tárgy null pointer, akkor az azt jelenti mindent törölni kell
			if(tárgy!=null){
				if(tárgy.get(0).getTömeg()!=0) Hanglejátszó.getInstance().playSound(Hanglejátszó.getInstance().doboz_szakadékban);
				tárgy.clear();	//ha nullát akarunk tárgyként lerakni és van már lent tárgy, azt mindent töröljük
			}
			 tárgy=null;	//beállítjuk a mező tárgyának a nullt
			 
			 return;	
		}

		if(tárgy==null)	//Ha nincs lista
			tárgy=new ArrayList<Tárgy>();	//akkor létrehozunk egyet
		if(t.getTömeg()==0){	//ha ZPM et rakunk olyan helyre editor módban, ahol dobozok vannak már, akkor kiszedjük a dobozokat
			tárgy.clear();
		}
		tárgy.add(t);	//és hozzáadjuk a paramétert amit a metódus kapott
	}
	
	public void mezőSzámítás(){	
		pár.mezőSzámítás(this);	//a pályaelem hatást gyakorolhat a DinamikusMező elemeire
		if(lövedék!=null) lövedék.mozog(this);	//ha van a mezőn lövedék akkor tovább engedjük mozogni		
	}
	
	public Ezredes getEzredes() {	//Getter 
		return ezredes;
	}
	
	public void setEzredes(Ezredes e) {	//Setter
		ezredes = e;
	}
	public Replikátor getReplikátor(){	//Getter
		return replikátor;
		
	}
	public void setReplikátor(Replikátor r){	//Setter
		replikátor=r;
	}
	
	public FLAFLövedék getLövedék() {	//Getter
		return lövedék;
	}
	
	public void setLövedék(FLAFLövedék löv) { //Setter
		lövedék = löv;
	}
	
	public DinamikusMező getSzomszéd(Irány ir) {	//Getter
		return szomszéd[ir.getValue()];
	}
	
	public void setSzomszéd(Irány ir, DinamikusMező dm) { //Setter
		szomszéd[ir.getValue()] = dm;
	}
	
	public StatikusMező getPár() {	//Getter 
		return pár;
	}
	
	public void setPár(StatikusMező sm) {	//Setter	
		pár = sm;
	}
	
	public Csillagkapu getCsk() {	//Getter
		return csk;
	}
	
	public void setCsk(Csillagkapu cskap) {	//Setter
		csk = cskap;
	}
}
