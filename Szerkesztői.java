import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class Szerkesztői {
	protected DinamikusMező pozíció;	//a szerkesztő pillanatnyi pozíciója
	protected Irány irány;	//a szerkesztő nézésének iránya
	ArrayList<String> módosítások;	//a lista amiben tárolja a session lépséeit a szerkesztő
	int szélességiOszt;	//a dinamikus statikushoz viszonyítva hány dinamikus tesz ki egy statikust széltében
	int magasságiOszt;	//a dinamikus mező a statikushoz képest magassági osztása
	int szél;	//szélességi index, a lépésekből számolva
	int mag;	//magassági index a lépésekből kalkulálva
	int zpmek;	//a lerakott zpm eket akkumulálja a mentéshez
	
	Ajtó ajtó;	//a legutóbbi ajtót tárolja, ha a legutobb ajtó volt lerakva
	int ajtóX;	//ajto x koord
	int ajtóY;	//ajtó y koord
	
	int súlykorlát;	//a súlykorlát a mérleghez
	private boolean aktív;	//a szerkesztő mint karakter aktív-e
	
	public boolean getAktív() {	//a szerkesztő karakter aktív e. GETTER
		return aktív;	
	}
	
	public Szerkesztői(DinamikusMező pályaSarka, int szélességiOsztás, int magasságiOsztás){	//konstruktor, x felosztás, y felosztás
		irány=Irány.s;	//kezdeti nézési irány
		aktív=false;	//amikor létrejön, akkor nem aktív a szerkesztői karakter
		setPozíció(pályaSarka);	//a pálya legsarkába állítja a szerkesztőt
		szélességiOszt=szélességiOsztás;	//osztást tárol x	
		magasságiOszt=magasságiOsztás;	//osztás y
		szél=0;	//x pozíció, ami a szélességében mozgást számolja, mivel bal felső sarok 0
		mag=0;	//y=0 mert a bal felső sarokban vagyunk
		zpmek=0;	//még egyet sem raktunk le mert most jön létre
		módosítások = new ArrayList<String>();	//lista a módosítások tárolására
		
		ajtóX=ajtóY=0;	//csak inic
		ajtó=null;	//inic
		súlykorlát=1;	//a súlykorlát kezdetben 1, ami annyit tesz, hogy bármit rárakva a mérleg kapcsol
	}
	
	public boolean getVanAjtó() {	//azt adja meg, hogy tartozik a mérleghez ajtó vagy sem
		return !(ajtó==null);
	}

	public void fordul(Irány i){ //A fordul metódussal tudjuk változtatni az aktuális nézési irányt
	    irány = i;
	}
	
	public Irány getIrány(){	//a nézési irányt adja vissza
		return irány;	
	}

	public DinamikusMező getPozíció(){	//Getter a szerkesztő pillanatnyi pozíciójához
		return pozíció;
	}	
	
	public void módosításokFilebaÍrása(String útvonal){	//a tárolt módosításokat kiírja fileba
		if(zpmek!=0) módosítások.add(Integer.toString(zpmek));	//ha a sessionben raktunk le ZPM et, azok számát a lista végére rakja
		try {
			File fout = new File(útvonal);	//a paraméterbe kapott helyre menti a módosításokat
			FileOutputStream fos = new FileOutputStream(fout);
		 
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
		 
			for (String sor : módosítások) {
				bw.write(sor);	//a lista minden sorát kiírja
				bw.newLine();	//új sor
			}
		 
			bw.close();	//lezárjuk a kimeneti filet
		} catch (IOException e) {
			System.err.println("Az editor nem tud fileba írni");
			e.printStackTrace();
		}
	}
	
	public void setPozíció(DinamikusMező cél){	//a cél mezőre állítjuk úgy, hogy ahol előtte volt onnan eltűnik
		if(pozíció!=null )pozíció.setEditor(null);	//Az eddigi pozícióbol kinullázzuk, ha már valahol állt
		if(cél!=null) cél.setEditor(this);	//a cél mezőre beállítjuk, ha nem a semmibe vezet a cél
		pozíció = cél;	//Frissítjük a pozíciót a célra
	}
	
	public void lép() {	//a szerkesztői nézet lépés metódusa
		DinamikusMező sz = pozíció.getSzomszéd(irány); //lekérjük a szerkesző irányában álló szomszédot
		
		if (sz!=null) {	//ha a lekérdezett szomszéd mező létezik
			setPozíció(sz);	//lépünk mert editor módrol van szó
			
			switch (irány) {	//az irányokba számoljuk az indexeket a pályafileba íráshoz
			case n:	//x csökkenő
				mag--;
				break;
			case e:	//y növekvő
				szél++;
				break;
			case s:	//x növekvő
				mag++;
				break;
			case w:	//y csökkenő
				szél--;
				break;
			default:
				break;
			}
		}
	}
	
	public void replikátorLe() {	//az adott helyre lerak egy replikátort, de mivel az a pálya listájába nem kerül bele, nem mozog
		String repi=szél+" "+mag+" ";
		Replikátor repLe= new Replikátor();
		pozíció.setReplikátor(repLe);
		repi+="R";
		módosítások.add(repi);	//a módosítások közé bekerül, hogy leraktunk egy replikátort
	}
	
	public void changePályaelem(char c){	//a pályaelem betűje szerint
		String változás=szél/szélességiOszt+" "+mag/magasságiOszt+" ";	//kiszámolja a pályakoordináta indexet, osztunk, mert pályaelem
		switch (c) {
		case 'P':
			ajtó=null;	//kinullázzuk az ajtót, mert ez nem ajtó
			pozíció.getPár().setPályaelem(new Padló());	//a konstruktorbán látszik, hogy mi melyik
			változás+="P";
			break;
		case 'A':
			ajtó=new Ajtó();	//az ajtó amit leraktunk
			pozíció.getPár().setPályaelem(ajtó);
			változás+="A";	//kiírja a pályafile sorhoz az adott pályaelem azonosítót
			ajtóX=szél/szélességiOszt;
			ajtóY=mag/magasságiOszt;
			break;
		case 'M':	//mérleg
			if(ajtó==null) pozíció.getPár().setPályaelem(new Mérleg(null,0));	//kezdetben minden mérleg ajtó nélkül kezd
				else pozíció.getPár().setPályaelem(new Mérleg(ajtó,súlykorlát));
			változás+="M";
			if(ajtó!=null) változás+=" "+súlykorlát+" "+ajtóX+" "+ajtóY;
			ajtó=null;
			break;
		case 'N':	//üres mérleg
			pozíció.getPár().setPályaelem(new Mérleg(null,0));
			változás+="M";
			break;
		case 'F':	//fal
			ajtó=null;
			pozíció.getPár().setPályaelem(new Fal());	//a konstruktorbán látszik, hogy mi melyik
			változás+="F";
			break;
		case 'S':	//speckó
			ajtó=null;
			pozíció.getPár().setPályaelem(new SpeciálisFal());
			változás+="S";
			break;
		case '#':	//szakadék
			ajtó=null;
			pozíció.getPár().setPályaelem(new Szakadék());
			változás+="#";
			break;
		default:	//amúgy meg ha más karaktert adunk meg, nem csinál semmit
			break;
		}
		módosítások.add(változás);	//nem felejtjük el hozzáadni a kiírandó sort a módosításokhoz
	}
	
	public void changeTárgy(char t){	//lerak egy tárgyat az adott dinamikus mezőre
		String változás=szél+" "+mag+" ";	//itt dinamikus index alapján dolgozunk, nincs leosztás
		switch (t) {	//két tárgyat lehet lerakni
		case 'D':	//doboz
			pozíció.setTárgy(new Doboz());
			változás+="D";	//a kiírandó sorba hozzáírjuk a doboz azonosítóját
			break;
		case 'Z':	//zpm
			pozíció.setTárgy(new ZPM());
			változás+="Z";
			zpmek++;
			break;
		default:
			break;
		}
		módosítások.add(változás);	//nem felejtjük el hozzáadni a kiírandóhoz a sort
	}

	public void vált(boolean aktivitás) {	//aktivitását állítja az editor karakternek
		aktív = aktivitás;	//amit a paramétersoron kap, azt
	}
}