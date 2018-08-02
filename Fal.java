//
//
//  Generated by StarUML(tm) Java Add-In
//	Modified by Harangozó
//	@Last modification: 2016.04.29.
//
//  @ Project : Grafikus
//  @ File Name : Fal.java
//  @ Date : 2016.03.28.
//  @ Author : kifli csapat
//
//
import java.awt.image.BufferedImage;

public class Fal extends Pályaelem { // öröklünk a Pályaelem ősosztályból
	public Fal() {
		akadály = true;  // A fal megakadályozza, hogy átlőjjünk/áthaladjunk rajta
	}
	
	public void esemény(DinamikusMező dm) { // egy lövedék rámegy akkor szüntesse meg		
		if(dm.getLövedék()!=null){ //ha egy lövedék kerül a fal dinamikus mezőjére
			dm.getLövedék().becsapódik(dm);
		}
	}
	
	@Override
	public BufferedImage képKeresés(iPályaelemKépeVisitor visitor) {
		return visitor.visit(this);
	}
}