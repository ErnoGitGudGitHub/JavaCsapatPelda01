import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class PályaelemVisitor implements iPályaelemKépeVisitor {	//a pályaelem visitor patternhez az interface implementáció
	static ArrayList<BufferedImage> textúrák;	//a texturákat fogja eltárolni itt
	
	public void setTextúrák(ArrayList<BufferedImage> t){
		textúrák=t;	//eltárolja a kapott textúrákat
	}
	
	@Override
	public BufferedImage visit(Padló pe) {	//visszatér az adott paraméterhez specifikusan tartozó textúrával
		return textúrák.get(6);
	}

	@Override
	public BufferedImage visit(Szakadék pe) {	//szakadék visitor
		return textúrák.get(7);
	}

	@Override
	public BufferedImage visit(Fal pe) {	//fal visitor
		return textúrák.get(8);
	}

	@Override
	public BufferedImage visit(SpeciálisFal pe) {	//seciális fal visitor
		return textúrák.get(9);
	}

	@Override
	public BufferedImage visit(Mérleg pe) {	//mérleghez tartozó visitor
		if(pe.getElégTömeg() && pe.getInaktív()) return textúrák.get(24);	//ha elég a tömeg, de nincs mit vezérelni, akkor inaktív mérleg textúra
		if (pe.getElégTömeg()) {
			return textúrák.get(10);	//ha nyit a mérleg
		} else return textúrák.get(11);	//a zárt mérleg textúrája
	}

	@Override
	public BufferedImage visit(Ajtó pe) {	//az ajtóhoz tartozó visitor
		if(pe.getAkadály()){
			return textúrák.get(13);	//ha zárva van akkor a zárt textúrát adja
		} else return textúrák.get(12);	//amúgy a nyitott textúrával tér vissza
	}

}
