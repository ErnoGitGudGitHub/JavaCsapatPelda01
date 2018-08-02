import java.util.ArrayList;
import java.util.Random;

public class replikátorBot {	//ez mozgatja a replikátorokat
	private ArrayList<Replikátor> replikátor;	//a replikátorok listája, akiket mozgatni kell
	private int i;	//mennyi időközönként léptessen, egy sebességhez hasonló érték
	private ArrayList<DinamikusMező> előző;	//a replikátorok hol tartózkodtak a legutóbbi mozgatás előtt
	private Random randomGenerátor;	//a randomgenerátor

	public replikátorBot(ArrayList<Replikátor> r) {	//konstruktora a replikátort mozgatóhoz
		replikátor = r;	//a paraméterül kapott replikátorok listáját eltárolja
		i=0;	//az időváltozó init
		előző=new ArrayList<DinamikusMező>();	//az előző mezőket tároló listát létrehozása
		for (Replikátor egy : replikátor) {	//az előző mezőket tartalmazó lista feltöltése
			előző.add(egy.getPozíció());	//az előző pozíció mindegyik esetében a mostani lesz kezdetben
		}
		randomGenerátor = new Random();	//a random generátor létrehozása
	}
	
	public void replikátorAutomata(){	//a replikátorok mozgatásáért felelős metódus
		i++;	//növeljük az idő tényezőt
		int előzőIndex=0;	//belső változó, hogy a foreach hanyadiknál jár, az előzőket tartalmazó lista indexeléséhez
		for (Replikátor rep : replikátor) {	
			if(rep.getÉletbenvan())	//a listában az adott replikátor ha életben van
				if(i>=4){	//az időtényező ennél az értéknél nagyobb
					if((randomGenerátor.nextInt(32)/8)==0) randomForgás(előzőIndex);	//random forgatás, egy bizonyos eséllyel
					if(rep.getPozíció().getSzomszéd(rep.getIrány())!=null) rep.lép();	//ha nem null ra akar lépni, lép
					if(rep.getPozíció()==előző.get(előzőIndex)){	//ha nem tudott a lépés hatására továbbmenni,
						randomForgás(előzőIndex);	//akkor valaminek ütközött, ezért fordul, hogy tovább haladhasson
					}
					előző.set(előzőIndex, rep.getPozíció());	//eltároljuk a mozgatás utáni helyzetet, innen fogjuk tuni, ha arra nem tud tovább menni
				}
			előzőIndex++;	//az index növelése, ha a replikátor nincs életben, akkor is.
		}
		if(i>=4) i=0;		//lenullázzuk az időtényezőt, ha lépésre került már a sor
	}
	
	private void randomForgás(int index){	//véletlenszerűen elforgatja a paraméterben kapott indexű replikátort
		Irány ir=null;	//a forgatási irány
		switch (randomGenerátor.nextInt(4)) {	//4 irány közül random valamelyik
		case 0: ir=Irány.n;
			break;
		case 1: ir=Irány.e;
			break;
		case 2: ir=Irány.s;
			break;
		case 3: ir=Irány.w;
			break;
		}
		replikátor.get(index).fordul(ir);	//a random irányba fordítjuk a replikátort
	}
	
}
