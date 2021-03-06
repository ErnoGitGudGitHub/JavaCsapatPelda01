//
//
//  Generated by StarUML(tm) Java Add-In
//	Modified by Harangozó
//	@Last modification: 2016.04.29.
//
//  @ Project : Grafikus
//  @ File Name : Kontroller.java
//  @ Generation date : 2016.03.28.
//  @ Author : kifli csapat
//
//

import java.awt.event.ActionEvent;

import java.io.IOException;
import java.util.ArrayList;
import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.KeyStroke;

public class Kontroller {	//a játék vezérlését tartalmazó osztály
	private Szerkesztői szerkesztői;	//a vezérlendő szerkesztő karakter
	private Ezredes ezredes;	//a vezérlendő ezredes
	private Jaffa jaffa;	//a vezérlendő jaffa
	private ArrayList<Replikátor> replikátor;	//a vezérlendő replikátorok
	private boolean pause;	//a szüneteltetés állapota
	private ArrayList<KeyStroke> vezérlők;	//lista a vezérlőkhöz keystroke
	private ArrayList<String> gombok;	//a vezérlő gombokat tárolja String
	private ArrayList<AbstractAction> parancsok;	//a vezérlő parancsok, amiket elsüt a vezérlő gombok
	private replikátorBot replikátorAi;	//a replikátort mozgató objektum
	private Gui gui;	//a gui, amihez tartozik a vezérlő, ezt megkapja
	

	public Kontroller(Ezredes e, Jaffa j, ArrayList<Replikátor> r, Szerkesztői sz, Gui g) {	//ezredes, jaffa, replikátorok, editor, gui
		gui=g;
		gombok=null;
		ezredes=e;
		jaffa=j;
		replikátor=r;
		szerkesztői = sz;
		vezérlők=null;	//később kerülnek be a keystrokeok
		parancsok=ParancsokDefiniálása();
		replikátorAi = new replikátorBot(replikátor);	//a paraméterben kapott értékeket eltárolja, mást inicializál
	}
	
	public ArrayList<AbstractAction> getParancsok(){	//visszatér a parancs metódusok listájával
		return parancsok;
	}
		
	public boolean getPause() {	//visszatér, hogy a játék szüneteltetve van e
		return pause;
	}
	
	public void setPause() {	//szünetelteti a játékot
		pause=true;
	}
	
	public void replikátorLéptet(){	//a replikátorokat lépésre szólítja
		replikátorAi.replikátorAutomata();
	}
	
	public void setVezérlők(ArrayList<String> arrayList){	//beállítja a vezérlő keystrokeokat a gombok alapján
		if(vezérlők==null) vezérlők=new ArrayList<KeyStroke>();	//ha nincs, létrehoz egy listát
		if(gombok==null) gombok= new ArrayList<String>();	//a gombokat is eltárolja a mit kapott
		for (String sor : arrayList) {	//minden elemen
			KeyStroke egyKeyStroke = KeyStroke.getKeyStroke(sor);	//keystrokeokat csinál
			gombok.add(sor);	//a gombot eltárolja amiből a keystroke készült
			vezérlők.add(egyKeyStroke);	//a keystroket eltárolja
		}
	}
	
	public ArrayList<KeyStroke> getVezérlők(){	//a keystrokeokat adja vissza, ami a vezérlőgombokból készült
		return vezérlők;
	}
	
	
	@SuppressWarnings("serial")
	private ArrayList<AbstractAction> ParancsokDefiniálása(){	//az parancsokat hozza létre
		ArrayList<AbstractAction> ret = new ArrayList<AbstractAction>();
				
		AbstractAction ezredesÉszak = new AbstractAction() {	//lépés északra
			@Override
			public void actionPerformed(ActionEvent e) {
				if (pause) return;	//ha nincs szüneteltetve
				if (gui.getEditor()){	//ha szerkesztői módban vagyunk
					szerkesztői.fordul(Irány.n);
					szerkesztői.lép();
					return;
				}
				if(!ezredes.getéletbenVan()) return;	//az ezredes halott
				if(ezredes.getIrány()!=Irány.n){	//ha nem arra néz, csak forgatjuk az ezredest
					ezredes.fordul(Irány.n);
					return;
				}
				ezredes.lép();	//egyébként léptetjük
			}
		};
		ret.add(ezredesÉszak);
		
		AbstractAction ezredesKelet = new AbstractAction() {	//ezredes léptetés keletre, mint északra
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (pause) return;
				if (gui.getEditor()){
					szerkesztői.fordul(Irány.e);
					szerkesztői.lép();
					return;
				}
				if(!ezredes.getéletbenVan()) return;
				if(ezredes.getIrány()!=Irány.e){
					ezredes.fordul(Irány.e);
					return;
				}
				ezredes.fordul(Irány.e);
				ezredes.lép();
			}
		};
		ret.add(ezredesKelet);
		
		AbstractAction ezredesDél = new AbstractAction() { //ezredes léptetés délre, mint északra
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (pause) return;
				if (gui.getEditor()){
					szerkesztői.fordul(Irány.s);
					szerkesztői.lép();
					return;
				}
				if(!ezredes.getéletbenVan()) return;
				if(ezredes.getIrány()!=Irány.s){
					ezredes.fordul(Irány.s);
					return;
				}
				ezredes.fordul(Irány.s);
				ezredes.lép();
			}
		};
		ret.add(ezredesDél);
		
		AbstractAction ezredesNyugat = new AbstractAction() {//ezredes léptetés nyugatra, mint északra
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (pause) return;
				if (gui.getEditor()){
					szerkesztői.fordul(Irány.w);
					szerkesztői.lép();
					return;
				}
				if(!ezredes.getéletbenVan()) return;
				if(ezredes.getIrány()!=Irány.w){
					ezredes.fordul(Irány.w);
					return;
				}
				ezredes.fordul(Irány.w);
				ezredes.lép();
			}
		};
		ret.add(ezredesNyugat);
		
		AbstractAction ezredesLőElsődleges = new AbstractAction() { //ezredes lövés elsődleges metódus
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (pause) return;
				if (gui.getEditor()) return;
				if(!ezredes.getéletbenVan()) return;
				ezredes.lő(1);
			}
		};
		ret.add(ezredesLőElsődleges);
		
		AbstractAction ezredesLőMásodlagos = new AbstractAction() {	//ezredes lövés másodlagos
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (pause) return;
				if (gui.getEditor()) return;
				if(!ezredes.getéletbenVan()) return;
				ezredes.lő(2);
			}
		};
		ret.add(ezredesLőMásodlagos);
		
		AbstractAction ezredesHasznál = new AbstractAction() {	//használ fv
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (pause) return;
				if (gui.getEditor()) return;
				if(!ezredes.getéletbenVan()) return;
				ezredes.felvesz(null);
			}
		};
		ret.add(ezredesHasznál);
		
		AbstractAction pályaVáltás = new AbstractAction() {	//pálytá vált a metódus
			
			@Override
			public void actionPerformed(ActionEvent e) {
				pause=true;
				final JFileChooser fc = new JFileChooser(System.getProperty("user.dir")+"/palyak");	//kiválaszthatjuk a pályát
				int returnVal = fc.showOpenDialog(null);
				
				 if (returnVal == JFileChooser.CANCEL_OPTION) {
					 pause=false;
				 } else if (returnVal == JFileChooser.APPROVE_OPTION) {
					try {	
						Alkalmazás.pályaVáltás(fc.getSelectedFile().getAbsolutePath());	//az alkalmazáson keresztül váltunk pályát
					} catch (IOException e1) {
						System.err.println("A pályafile amire váltani akarunk hibás elérés");
						e1.printStackTrace();
					}
				}
			}
		};
		ret.add(pályaVáltás);
		
	///A jaffa parancsai innentől kezdődnek, ugyan olyanok mint az ezredesé
		AbstractAction jaffaÉszak = new AbstractAction() {	//jaffa észak
			
			@Override
			public void actionPerformed(ActionEvent e) {	//a jaffa editor módban is mozoghat
				if(!jaffa.getéletbenVan()) return;
				if(jaffa.getIrány()!=Irány.n){
				jaffa.fordul(Irány.n);
				return;
				}
				jaffa.lép();
			}
		};
		ret.add(jaffaÉszak);
		
		AbstractAction jaffaKelet = new AbstractAction() {	//jaffa kelet
			
			@Override
			public void actionPerformed(ActionEvent e) {//a jaffa editor módban is mozoghat
				if(!jaffa.getéletbenVan()) return;
				if(jaffa.getIrány()!=Irány.e){
				jaffa.fordul(Irány.e);
				return;
				}
				jaffa.lép();
			}
		};
		ret.add(jaffaKelet);
		
		AbstractAction jaffaDél = new AbstractAction() { //jaffa dél
			
			@Override
			public void actionPerformed(ActionEvent e) {//a jaffa editor módban is mozoghat
				
				if(!jaffa.getéletbenVan()) return;
				if(jaffa.getIrány()!=Irány.s){
				jaffa.fordul(Irány.s);
				return;
				}
				jaffa.lép();
			}
		};
		ret.add(jaffaDél);
		
		AbstractAction jaffaNyugat = new AbstractAction() {	//jaffa nyugat
			
			@Override
			public void actionPerformed(ActionEvent e) {//a jaffa editor módban is mozoghat
				if(!jaffa.getéletbenVan()) return;
				if(jaffa.getIrány()!=Irány.w){
				jaffa.fordul(Irány.w);
				return;
				}
				jaffa.lép();
			}
		};
		ret.add(jaffaNyugat);
		
		AbstractAction jaffaElsődleges = new AbstractAction() { //jaffa lő
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(!jaffa.getéletbenVan()) return;
				jaffa.lő(1);
			}
		};
		ret.add(jaffaElsődleges);
		
		AbstractAction jaffaMásodlagos = new AbstractAction() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				if(!jaffa.getéletbenVan()) return;
				jaffa.lő(2);
			}
		};
		ret.add(jaffaMásodlagos);
		
		AbstractAction jaffaHasznál = new AbstractAction() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				if(!jaffa.getéletbenVan()) return;
				jaffa.felvesz(null);
			}
		};
		ret.add(jaffaHasznál);
		
		AbstractAction pillanatÁllj = new AbstractAction() {	//a szüneteltető metódus
			
			@Override
			public void actionPerformed(ActionEvent e) {
				pause=!pause;	//szüneteltetünk
				gui.Pause(pause);	//a felugró ablakot hívjuk
				pause=!pause;	//ha onnan visszatértünk, és az nem lépett ki, akkor mehet tovább a játék
			}
		};
		ret.add(pillanatÁllj);	//hozzáadjuk a parancsot a visszatérő listához
		
		AbstractAction teljesKépernyő = new AbstractAction() {	//teljes képernyő
			
			@Override
			public void actionPerformed(ActionEvent e) {
				 gui.teljesKépernyőÁllít(true);	//tejles képernyőre állít
			}
		};
		ret.add(teljesKépernyő);
		
		AbstractAction kilépésTeljesKépernyőböl = new AbstractAction() {	//kis képernyő
			
			@Override
			public void actionPerformed(ActionEvent e) {
				gui.teljesKépernyőÁllít(false);	//kis képernyőre állít, false paraméter segítségével
			}
		};
		ret.add(kilépésTeljesKépernyőböl);
		
		AbstractAction pályaÚjraBetölt = new AbstractAction() {	//pálya restart
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					Hanglejátszó.getInstance().playSound(Hanglejátszó.getInstance().restart);	//restart hang kijátszása
					Alkalmazás.pályaVáltás(null);
				} catch (IOException e1) {
					System.err.println("A restart nem sikerült, a fileval valami baj van");
					e1.printStackTrace();
				}
			}
		};
		ret.add(pályaÚjraBetölt);
		

		AbstractAction szerkesztőireVáltás = new AbstractAction() {	//vált a szerkesztői, és a játékos mód között
			
			@Override
			public void actionPerformed(ActionEvent e) {
					if (gui.getEditor()) {	//ha most editor
						gui.JátékNézet();	//akkor játékosra vált
						szerkesztői.vált(false);	//és deaktiválja a szerkesztő karaktert
					} else {	//amugy meg szerkesztőire vált
						if (pause) pause=!pause;	//szünetelteti a játékot
						gui.SzerkeszőiNézet();	//editor mód
						szerkesztői.vált(true);	//editor karakter aktiválás
					}
			}
		};
		ret.add(szerkesztőireVáltás);
		
		AbstractAction súgó = new AbstractAction() {	//súgót jeleníttet meg
			
			@Override
			public void actionPerformed(ActionEvent e) {
				pause=!pause;	//szünet
				gui.súgóAblak(gombok);	//a gombokat átadja a súgóAblaknak, hogy tudja milyen bind van
				pause=!pause;	//ha visszatérünk, és nem léptünk ki, folytatjuk a játékot
			}
		};
		ret.add(súgó);
		
		AbstractAction eSzakadék = new AbstractAction() {	//editor szakadék le
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(gui.getEditor()){
					szerkesztői.changePályaelem('#');
				}
			}
		};
		ret.add(eSzakadék);
		
		AbstractAction eFal = new AbstractAction() {	//editor fal lerak
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(gui.getEditor()){
					szerkesztői.changePályaelem('F');
				}
			}
		};
		ret.add(eFal);
		
		AbstractAction eSpec = new AbstractAction() {	//editor speciális fal lerak
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(gui.getEditor()){
					szerkesztői.changePályaelem('S');
				}
			}
		};
		ret.add(eSpec);
		
		AbstractAction eAjtó = new AbstractAction() {	//editor ajtó lerakása
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(gui.getEditor()){
					szerkesztői.changePályaelem('A');
				}
			}
		};
		ret.add(eAjtó);
		
		AbstractAction eMérleg = new AbstractAction() {	//editor mérleg lerakása
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(gui.getEditor()){
					if (szerkesztői.getVanAjtó()) {
						szerkesztői.changePályaelem('M');
					}
				}
			}
		};
		ret.add(eMérleg);
		
		AbstractAction eZPM = new AbstractAction() {	//editor ZPM lerakása
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(gui.getEditor()){
					szerkesztői.changeTárgy('Z');
				}
			}
		};
		ret.add(eZPM);
		
		AbstractAction eDoboz = new AbstractAction() {	//editor doboz lerakása
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(gui.getEditor()){
					szerkesztői.changeTárgy('D');
				}
			}
		};
		ret.add(eDoboz);
		
		AbstractAction ePadló = new AbstractAction() {	//editor padló lerakása
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(gui.getEditor()){
						szerkesztői.changePályaelem('P');
				}
			}
		};
		ret.add(ePadló);
		
		AbstractAction eReplikátor = new AbstractAction() {	//editor replikátor lerakása
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(gui.getEditor()){
						szerkesztői.replikátorLe();
				}
			}
		};
		ret.add(eReplikátor);
		
		AbstractAction pályaMent = new AbstractAction() {	//az editorral végzett módosítások fileba írása
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(gui.getEditor()){	//ha szerkesztői módban vagyunk akkor
					Alkalmazás.editorMentése("editor.txt");	//editor.txt be kiírja a módosításokat, amiket a szerkesztőivel csináltunk
				}
			}
		};
		ret.add(pályaMent);
		
		AbstractAction eÜresMérleg = new AbstractAction() {	//editor inaktív mérleg lerakása
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(gui.getEditor()){
					szerkesztői.changePályaelem('N');
				}
			}
		};
		ret.add(eÜresMérleg);
		
		
		return ret;	//visszaadjuk a listát, amihez a parancsokat adtuk hozzá
	}
	
}
