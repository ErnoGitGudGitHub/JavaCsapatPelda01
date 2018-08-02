//
//
//  Generated by StarUML(tm) Java Add-In
//	Modified by Harangozó
//	@Last modification: 2016.04.29.
//
//  @ Project : Grafikus
//  @ File Name : Ezredes.java
//  @ Date : 2016.03.28.
//  @ Author : kifli csapat
//
//
import java.util.ArrayList;

public class Ezredes{
	protected FLAF flaf;	//Az Ezredes/Jaffa Féregjárat Létrehozására Alkalmas Fegyvere
	protected int táska;	//Az Ezredes/Jaffa táskája,ebben tárolja a felvett ZPM-eket
	protected boolean életbenVan;	//Megadja hogy az Ezredes/Jaffa életben van-e
	protected DinamikusMező pozíció;	//Az Ezredes/Jaffa pillanatnyi pozíciója
	protected Irány irány;	//Az Ezredes/Jaffa nézésének pillanatnyi nézési iránya
	protected int össz;	//A pályán található összes ZPM száma
	protected Doboz doboz;	//Az Ezredes/Jaffa kezében taláható doboz
	boolean gonosz;
	
	public Ezredes(int db){
		gonosz = false;
		flaf = new FLAF();	//A kapott FLAF lesz a fegyvere
		táska = 0;	//nincs ZPM kezdetben felvéve
		életbenVan = true;	//életben van a kezdetekkor
		össz = db;	//Az átadott darabszámra fog emlékezni mint össz ZPM mennyiség
		doboz = null;	//Nincs nálla doboz
		pozíció = null;	//Nincs a pályán a létrehozásakor. Rá kell állítani a létrehozása után egy DinamikusMezőre
		irány = Irány.n;	//északnak néz kezdetben
	}


	public void bonuszZPM() {
		össz++;
	}
	
	public boolean getGonosz() {
		return gonosz;
	}
	
	public Doboz getDoboz(){
		return doboz;
	}
	
	public void felvesz(Tárgy t){	//Itt a paraméter null, ha előttelevőből akar felvenni, egyébként a ZPM amikor rálép
		if (t!=null) {	//Ha nem null-t akar felvenni, akkor a ZPM et kell hogy megkapja amire rálépett
			if(t.getTömeg()==0){	//ha az valóban ZPM akkor felveszi azt a pályáról arról a mezőröl ahol áll
				Hanglejátszó.getInstance().playSound(Hanglejátszó.getInstance().zpm_felvétel);
				táska++;	//megnöveli a felvett ZPMek számát egyel
				pozíció.setTárgy(null);	//kitörli a mezőröl a ZPMet
				return;	//visszatér a metódusból
			}
		}
		if (doboz==null){	//Ha nincs doboz a kezében
			DinamikusMező tdm = pozíció.getSzomszéd(irány);	//Temp dinamikus mező
			if (tdm==null) {
				return;	//Ha már nincs szomszéd a nézési irányban akkor visszatérünk
			}
			ArrayList<Tárgy> temp =tdm.getTárgy();	//ha ez lefut, akkor megkapjuk a szomszédon levő tárgyak listáját
			if(temp != null){	//Van ott legalább egy tárgy
				if(temp.get(0).getTömeg()==0) {	//Ha ZPM, felvesszük
					táska++;	//megnöveljük a felvett ZPMek számát
					tdm.popTárgy();	//kiszedjük a listából a ZPMet
				}
				else {	//Amugy kézbe veszi a dobozt, mert más nem lehet rajta
					doboz = (Doboz) temp.get(temp.size()-1);
					tdm.popTárgy();	//kiszedjük a legfelső tárgyat
				}
			} 
			else return;	//Ha nincs tárgy a szomszédban, visszatérünk
		}
		else{	//Ha van az ezredes kezében doboz;
			if (!életbenVan) {	//Ha az ezredes halott
				pozíció.setTárgy(doboz);	//akkor abba a mezőbe ahol van, lerakja a dobozt
				doboz = null;	//nem lesz a kezében már a doboz
				return;	//visszatérünk
			}
			
			DinamikusMező tdm = pozíció.getSzomszéd(irány);	//a szomszédot elkérjük, ahova le szeretnénk rakni a dobozt
			if(tdm==null) return;	//ha a mező ahova le akarja rakni a dobozt nem létezik, akkor visszatérünk
			if(tdm.getPár().getPályaelem().valtozhatEAkadalyMivolta()) return;	//ha a ajtóba szeretnénk lerakni visszatérünk, mert oda nem rakhatjuk
			if(tdm.getPár().getPályaelem().getAkadály()==true) return;
			ArrayList<Tárgy> tmp = tdm.getTárgy();	//a mezőn szereplő tárgyak listája
			if(tdm.getEzredes()!=null) return; //ha van ezredes a mezőn, nem tesszük rá a dobozt
			if(tmp!=null){	//ha van tárgy a mezőn
				if(!(tmp.get(0).getTömeg()==0)){	//de az nem ZPM
					tdm.setTárgy(doboz);	//lerakjuk a mezőre a dobozt
					doboz = null;	//és az ezredes kezében már nem lesz
				} else return;	//ZPM re nem rakhatjuk rá a dobozt
			} else {
				tdm.setTárgy(doboz);	//ha még nincs tárgy a mezőn lerakjuk a kezünkből
				doboz = null;
			}
		}
	}
	
	public void lő(int hanyadlagos) {	//Paraméterben azt kapjuk meg, hanyadlagos lövést süssük el
		switch (hanyadlagos) {
		case 1:  //Az elsődleges kék lövés
			flaf.lő(Szin.kék, irány, pozíció.getSzomszéd(irány));
			break;
		case 2:  //A másodlagos sárga lövés
			flaf.lő(Szin.sárga,irány,pozíció.getSzomszéd(irány));
			break;
		default: break;
		}
	}
	
	public void setPozíció(DinamikusMező cél){	//a cél mezőre állítjuk az ezredest úgy, hogy ahol előtte volt onnan eltűnik
		if(pozíció!=null )pozíció.setEzredes(null);	//Az eddigi pozícióbol kinullázzuk az ezredest, ha már valahol állt
		if(cél!=null) cél.setEzredes(this);	//a cél mezőre beállítjuk az ezredest, ha nem a semmibe vezet a cél
		pozíció = cél;	//Frissítjük a pozíciót a célra
	}
	
	public void lép() {
		pozíció.mezőSzámítás();
		if(!életbenVan) return;	//nem mozoghatunk ha már meghaltunk
		DinamikusMező sz = pozíció.getSzomszéd(irány); //lekérjük az ezredes nézési irányának megfelelő szomszédot
		
		if (sz!=null) {	//ha a lekérdezett szomszéd mező létezik
			if(sz.getEzredes()!=null && sz.getEzredes().getéletbenVan()) return;	//de azon már van egy ezredes, visszatérünk
			ArrayList<Tárgy> t = sz.getTárgy(); 	//Megnézzük, van-e a mezőn tárgy
			if(t!=null){	//ha van tárgy a mezőn
				if(t.get(0).getAkadály()) return;	//ha az a tárgy akadály, visszatérünk, mert arra nem mehetünk	
				setPozíció(sz);	//átlépünk a szomszédos mezőre mert a tárgyra ráléphetünk
				Hanglejátszó.getInstance().playSound(Hanglejátszó.getInstance().lépés);	//történt egy lépés, lépéshang
				felvesz(t.get(0));	//és felvesszük az ott lévő tárgyat
				return;	//visszatérünk mert a lépés véget ért
			}
			Pályaelem pe = sz.getPár().getPályaelem(); //A statikus mezőn levő pályaelemet lekérdezzük
			if(pe.getAkadály()){	//Ha ez a pályaelem akadály
				if(sz.getCsk()==null) return;//Ha nincs a pályaelemen csillagkapu,nem léphetünk, visszatérünk
				if(sz.getCsk().getMásik()==null) return; //ha nem aktív a féregjárat visszatérünk, nem léphetünk akadályba
				if(sz.getCsk().getFrontIrány()!=pozíció.getEzredes().getIrány().ellentett()) return;	//ha a csillagkapu iránya nem ellentétes az ezredesével, visszatérünk
				if(sz.getCsk().getMásik().getHova().getEzredes()!=null){
					Hanglejátszó.getInstance().playSound(Hanglejátszó.getInstance().csillagkapu_blokkolt_túlvég);
					return;	//ha a csillagkapu pár kilépési oldalán ezredes van, nem tudunk átmenni
				}
				// ha a célmezőn van tárgy
				ArrayList<Tárgy> t2 = sz.getCsk().getMásik().getHova().getTárgy();
				if(t2!=null && t2.get(0).getTömeg()!=0){
					Hanglejátszó.getInstance().playSound(Hanglejátszó.getInstance().csillagkapu_blokkolt_túlvég);
					return;
				}
				setPozíció(sz.getCsk().getMásik().getHova());
				if(t2!=null){
					if(t2.get(0).getAkadály()){
						Hanglejátszó.getInstance().playSound(Hanglejátszó.getInstance().csillagkapu_blokkolt_túlvég);
						return; // ha az Doboz, akkor nem lépünk
					}
					if(t2.get(0) != null) felvesz(t2.get(0)); // ha ZPM, akkor felvesszük mielőtt rálépünk
				}
				setPozíció(sz.getCsk().getMásik().getHova());	//különben léptetjük az ezredest a csillagkapun keresztül
				irány=sz.getCsk().getMásik().getFrontIrány();
				Hanglejátszó.getInstance().playSound(Hanglejátszó.getInstance().csillagkapun_keresztül);
				return; //és visszatérünk mert a lépés véget ért a csillagkapun át
			}
			Hanglejátszó.getInstance().playSound(Hanglejátszó.getInstance().lépés);
			setPozíció(sz);	//lépünk nem akadály mezőre szabadon
		}
	}

	public void fordul(Irány i){ //A fordul metódussal tudjuk változtatni az aktuális nézési irányt
	    irány = i;
	}
	
	public Irány getIrány(){	//a nézési irányt adja vissza
		return irány;	
	}
	
	public void meghal(){	//az ezredes meghal metódusa
		if(!életbenVan) return;
		életbenVan = false;	//Először beálítjuk az ezredest halottnak
		if(doboz!=null) felvesz(null);	//ha van az ezredesnél doboz meghívjuk a felveszt nullal, így el fogja dobni a dobozt
		táska = 0; 	//Elveszti az össes eddig összegyűjtött ZPM jét, mert szakadékba esett.
		Hanglejátszó.getInstance().playSound(Hanglejátszó.getInstance().ezredes_szakadékban);
	}
	
	public boolean getFeladatVége(){	//visszatér, hogy az ezredes teljesítette-e a küldetését
		if(össz==1) return(táska == 1);
		if(össz==2) return(táska == 1);
		return(táska == össz-2);	//összeszedte az összes ZPM et max kettő kivételével az EZredes?
	}
	
	public boolean getéletbenVan(){	//Getter, hogy az ezredes életben van-e
		return életbenVan;
	}
	
	public FLAF getFLAF(){	//Getter az ezredes fegyveréhez
		return flaf;
	}
	
	public int getÖssz(){	//Getter az összes pályán levő ZPM menyiségéhez
		return össz;
	}
	
	public int getTáska(){	//Getter a felvett ZPM ek számához
		return táska;
	}
	
	public DinamikusMező getPozíció(){	//Getter az ezredes pillanatnyi pozíciójához
		return pozíció;
	}	
}