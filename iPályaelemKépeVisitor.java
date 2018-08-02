import java.awt.image.BufferedImage;

public interface iPályaelemKépeVisitor {	//a visitor pattern interfésze
	BufferedImage visit(Padló pe);
	BufferedImage visit(Szakadék pe);
	BufferedImage visit(Fal pe);	//a különböző pályaelemek felsorolása, egyesével, specifikusan
	BufferedImage visit(SpeciálisFal pe);
	BufferedImage visit(Mérleg pe);
	BufferedImage visit(Ajtó pe);
}
