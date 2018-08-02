//
//
//  Generated by StarUML(tm) Java Add-In
//	Modified by Harangozó
//	@Last modification: 2016.04.29
//
//  @ Project : Grafikus
//  @ File Name : Replikátor.java
//  @ Generation date : 2016.03.28.
//  @ Author : kifli csapat
//
//

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Alkalmazás {
	private static Szerkesztői editor;	//az editor módhoz tartozó osztály
	private static Ezredes ezredes;	//Az egyik játékos által irányított Ezredest reprezentálja.
	private static Jaffa jaffa;	//A másik játékos által irányított Jaffát reprezentálja.
	private static ArrayList<Replikátor> replikátor;	//A pályán elhelyezett véletlenszerűen mozó replikátort reprezentálja.
	private static Pályabetöltő pályabetöltő;	//A pálya betöltéséért felel.
	private static DinamikusMező pályaSarka;	//A (0,0)-ás pozícióban lévő DinamikusMezőt tárolja.
	private static Kontroller vezérlő;	//A játékosok ezen keresztül irányítják a karaktereiket.
	private static Gui gui;
	private static String pillanatnyiMap;
	
	public static void editorMentése(String útvonal){	//kimenti az editorban számontartott változásokat az elérési útvonali fileba
		editor.módosításokFilebaÍrása(útvonal);
	}
	
	public static Pályabetöltő getPályabetöltő() {
		return pályabetöltő;
	}
	
	public static void pályaVáltás(String elérésiÚtvonal) throws IOException{
		if(elérésiÚtvonal==null){
			pályaVáltás(pillanatnyiMap);
			editor.vált((editor!=null && editor.getAktív()));
			return;
		}
		pillanatnyiMap=elérésiÚtvonal;
		pályabetöltő = new Pályabetöltő();
		pályabetöltő.pályaBeolvas(elérésiÚtvonal);
		pályaSarka=pályabetöltő.pályaLétrehoz();
		boolean temp =(editor!=null && editor.getAktív());
		editor=new Szerkesztői(pályaSarka,Pályabetöltő.getSzélességiFelosztás(),Pályabetöltő.getMagasságiFelosztás());
		editor.vált(temp);
		ezredes=pályabetöltő.getEzredes();
		jaffa=pályabetöltő.getJaffa();
		replikátor=pályabetöltő.getReplikátor();
		if(gui==null) gui = new Gui();
		gui.textúrákBeállítása(pályabetöltő.getTextúrákElérése());
		vezérlő=new Kontroller(ezredes, jaffa, replikátor, editor, gui);
		vezérlő.setVezérlők(pályabetöltő.vezérlőkBeolvasása("bind.txt"));
		gui.setBillentyűzetVezérlés(vezérlő.getVezérlők(), vezérlő.getParancsok());
		String pályaNév = new java.io.File(elérésiÚtvonal).getName();	//pl. pálya.txt
		gui.pályaNévFrissít(pályaNév.substring(0, pályaNév.length()-4 ));
	}
	
	public static void main(String args[]) throws IOException{
		if(args.length != 0)
	    {
			pillanatnyiMap=args[0];
	    } else
		pillanatnyiMap="palyak/palya.txt";
		pályaVáltás(pillanatnyiMap);
		frissítés();
		gui.üdvözlőAblak(pályabetöltő.vezérlőkBeolvasása("bind.txt").get(20));		
		
		TimerTask frissít = new TimerTask() {
			@Override
			public void run() {
				try {
					if(vezérlő!=null && !vezérlő.getPause())
					frissítés();
				} catch (IOException e) {
					System.err.println("A vezérlő file elérése nem sikerült.");
					e.printStackTrace();
				}				
			}
		};
		
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(frissít, 0, 50);
	}
	
	public static void frissítés() throws IOException{
		if(vezérlő.getPause()) return;
		if(!ezredes.getéletbenVan() || !jaffa.getéletbenVan()){
			gui.végeAblak(!jaffa.getéletbenVan());
			pályaVáltás(pillanatnyiMap);
		}
				
		if((ezredes.getTáska()+jaffa.getTáska())==2 && ezredes.getÖssz()>=2){
			DinamikusMező temp = pályabetöltő.getHiddenZpm();
			if(temp!=null){	//ha teljesül a bonusz feltétel, akkor lerak egyet, és ameddig nem veszik fel, ha ott van nem rakja le újra
				temp.setTárgy(new ZPM());
				ezredes.bonuszZPM();
				jaffa.bonuszZPM();
			}
		}
		if(ezredes.getFeladatVége() || jaffa.getFeladatVége()) {
			vezérlő.setPause();
			if(ezredes.getFeladatVége())gui.videóKijátszása(false);
			else gui.videóKijátszása(true);//csak egyszer fut le, mert aztán már pausálva van
		}
		if((ezredes.getTáska()+jaffa.getTáska())==ezredes.getÖssz() && !(ezredes.getFeladatVége() || jaffa.getFeladatVége())){	//Döntetlen
			gui.döntetlenAblak();	//a döntetlen ablak felugrik, ha tud, mert nem volt vége. Újraindítás vagy kilépés
			pályaVáltás(pillanatnyiMap);	//mivel az ablakból csak a pálya ujraindítással lehet visszajönni
		}
		if (gui.getEditor()) {
			gui.guiFrissít(jaffa.getPozíció(), editor.getPozíció());
			return;
		}
		gui.guiFrissít(jaffa.getPozíció(), ezredes.getPozíció());
		vezérlő.replikátorLéptet();
		
		DinamikusMező futó = pályaSarka;
		
		while (futó!=null){
			DinamikusMező futó2 = futó;
			while(futó2!=null){
				futó2.mezőSzámítás();
				futó2=futó2.getSzomszéd(Irány.s);
			}
			futó=futó.getSzomszéd(Irány.e);
		}
	}

	
}