import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.JComponent;

@SuppressWarnings("serial")
public class cPálya extends JComponent {
	static ArrayList<BufferedImage> textúrák;
	static PályaelemVisitor pályaelemVisitor;
	private DinamikusMező közép;
	int w;
	int h;
	int x;	//	x felosztás, egy statikus mezőhoz hány dinamikus mező tartozik szélességben
	int y;	//	y felosztás, egy statikus mezőhoz hány dinamikus mező tartozik magasságban
	static boolean resize;
	
	public cPálya() {
		közép=null;
		textúrák = null;
		pályaelemVisitor = new PályaelemVisitor();
		w=0;
		h=0;
	}
	
	static void addKépek(ArrayList<BufferedImage> bi){
		textúrák = bi;
	}
	
	public void setKözép(DinamikusMező dm){
		közép=dm;
	}
	
	private static BufferedImage resizeImage(BufferedImage originalImage, int x, int y){
		if (resize) {
			int xméret = 128 / x;
			int yméret = 128 / y;
			BufferedImage resizedImage = new BufferedImage(xméret, yméret, 7);
			Graphics2D g = resizedImage.createGraphics();
			g.drawImage(originalImage, 0, 0, xméret, yméret, null);
			g.dispose();
				
			return resizedImage;
		}
		else return originalImage;
	}
			
	private ArrayList<BufferedImage> dinamikusKépe(DinamikusMező dm){
		ArrayList<BufferedImage> ret = null;
		
		Alkalmazás.getPályabetöltő();				//statikus elérés
		x = Pályabetöltő.getSzélességiFelosztás();	//azért így, mert így nem dob warningot
		y = Pályabetöltő.getMagasságiFelosztás();	//azért itt, mert a konstruktor csak egyszer fut le a program futása során
		resize = Pályabetöltő.getResize();
				
		if(dm.getCsk()!=null){
			if(dm.getCsk().getMásik()==null){
				switch (dm.getCsk().getSzín()) {
				case kék:
					if(ret==null) ret=new ArrayList<BufferedImage>(1);
					
					ret.add( resizeImage(textúrák.get(14), x, y));
					
					break;
				case sárga:
					if(ret==null) ret=new ArrayList<BufferedImage>(1);
					ret.add( resizeImage(textúrák.get(16), x, y));
					break;
				case piros:
					if(ret==null) ret=new ArrayList<BufferedImage>(1);
					ret.add( resizeImage(textúrák.get(18), x, y));
					break;
				case zöld:
					if(ret==null) ret=new ArrayList<BufferedImage>(1);
					ret.add( resizeImage(textúrák.get(20), x, y));
					break;
				default:
					break;
				}
			} else {
				switch (dm.getCsk().getSzín()) {
				case kék:
					if(ret==null) ret=new ArrayList<BufferedImage>(1);
					ret.add( resizeImage(textúrák.get(15), x, y));
					break;
				case sárga:
					if(ret==null) ret=new ArrayList<BufferedImage>(1);
					ret.add( resizeImage(textúrák.get(17), x, y));
					break;
				case piros:
					if(ret==null) ret=new ArrayList<BufferedImage>(1);
					ret.add( resizeImage(textúrák.get(19), x, y));
					break;
				case zöld:
					if(ret==null) ret=new ArrayList<BufferedImage>(1);
					ret.add( resizeImage(textúrák.get(21), x, y));
					break;
				default:
					break;
				}				
			}
		}
		if(dm.getTárgy()!=null){
			if(dm.getTárgy().get(0).getTömeg()==0){
				if(ret==null) ret=new ArrayList<BufferedImage>(1);
				ret.add( resizeImage(textúrák.get(5), x, y));	//ZPM sorszáma
				return ret;
			} else {
				if(ret==null) ret=new ArrayList<BufferedImage>(1);
				ret.add( resizeImage(textúrák.get(4), x, y));	//doboz sorszáma
				return ret;
			}
		}
		if (dm.getReplikátor()!=null) {
			if(ret==null) ret=new ArrayList<BufferedImage>(1);
			ret.add( resizeImage(textúrák.get(22), x, y));	//replikátor sorszáma
		}
		if(dm.getEzredes()!=null && dm.getEzredes().getéletbenVan()){
			if(dm.getEzredes().getGonosz()==true){
				if(dm.getEzredes().getDoboz()!=null){
					if(ret==null) ret=new ArrayList<BufferedImage>(1);
					ret.add( resizeImage(textúrák.get(1), x, y));	//dobozos JAFFA
					//return ret;
				} else {
					if(ret==null) ret=new ArrayList<BufferedImage>(1);
					ret.add( resizeImage(textúrák.get(0), x, y));	//sima JAFFA
					//return ret;
				}
			} else {
				if(dm.getEzredes().getDoboz()!=null){
					if(ret==null) ret=new ArrayList<BufferedImage>(1);
					ret.add( resizeImage(textúrák.get(3), x, y));	//dobozos O'neill
					//return ret;
				} else {
					if(ret==null) ret=new ArrayList<BufferedImage>(1);
					ret.add( resizeImage(textúrák.get(2), x, y));	//sima O'neill
					//return ret;
				}
			}
		}
		
		if (dm.getEditor()!=null) {
			if (dm.getEditor().getAktív()) {
				if(ret==null) ret=new ArrayList<BufferedImage>(1);
				ret.add( resizeImage(textúrák.get(23), x, y) );
			}
		}
		
		return ret;
	}
		
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if(textúrák==null) return;
		AffineTransform centerTransformation = new AffineTransform();
		AffineTransform at = new AffineTransform();
		centerTransformation.translate(getWidth() / 2, getHeight() / 2);	//beállítja a komponens közepére a tranlációt
		centerTransformation.translate(-textúrák.get(0).getWidth()/2, -textúrák.get(0).getHeight()/2);	//a pivot pontot a kép közepére rakja.

		pályaelemVisitor.setTextúrák(textúrák);
		
		at.translate(getWidth() / 2, getHeight() / 2);
		at.translate(-textúrák.get(0).getWidth()/2, -textúrák.get(0).getHeight()/2);
		
		if(közép!=null){	
			DinamikusMező futó = közép;
			w= közép.getPár().getPályaelem().képKeresés(pályaelemVisitor).getWidth();
			h= közép.getPár().getPályaelem().képKeresés(pályaelemVisitor).getHeight();
			
			while(futó.getSzomszéd(Irány.w)!=null && futó.getSzomszéd(Irány.w).getPár()==közép.getPár()){
				futó=futó.getSzomszéd(Irány.w);
				at.translate(-w/Pályabetöltő.getSzélességiFelosztás(), 0);
			}
			futó = közép;
			while(futó.getSzomszéd(Irány.n)!=null && futó.getSzomszéd(Irány.n).getPár()==közép.getPár()){
				futó=futó.getSzomszéd(Irány.n);
				at.translate(0, -h/Pályabetöltő.getMagasságiFelosztás());
			}
		}
		if(közép!=null){
			offKirajzol(at, g);
			statikusKirajzol(közép.getPár(), at, g);
			dinamikusKirajzol(közép, centerTransformation, g);
		}
		if(közép!=null && közép.getEzredes()!=null){
			
			if (közép.getEzredes().getFeladatVége()) {
				String feladatVége="";
				g.setFont(new Font("Tahoma", Font.PLAIN, 17));
				
				if(közép.getEzredes().getGonosz()) {
						g.setColor(Color.RED);
						feladatVége="Az emberiségnek VÉGE";
				}
					else{
						g.setColor(Color.ORANGE);
						feladatVége="Az emberiség MEGMENEKÜLT";
					}
				g.drawString(feladatVége, (int)centerTransformation.getTranslateX()-feladatVége.length()*3, (int)centerTransformation.getTranslateY());
				
			}
			g.setFont(new Font("Verdana", Font.PLAIN, 27));	//basic windows font
			String pontKijelző;
			g.setColor(Color.yellow);	//a pont kijelző színe
			pontKijelző="Összes/Felvett: "+Integer.toString(közép.getEzredes().getÖssz())+"/"+közép.getEzredes().getTáska();
			g.drawString(pontKijelző, 40, 45);
		}
		if(közép!=null && közép.getEditor()!=null && közép.getEzredes()==null){
			g.setFont(new Font("Verdana", Font.PLAIN, 27));	//basic windows font
			String editorKijelző="";
			g.setColor(Color.yellow);	//a pont kijelző színe
			if(közép.getEditor().getVanAjtó()) editorKijelző+="Rakj le egy mérleget";
			g.drawString(editorKijelző, 40, 45);
		}
	}
		
	private void dinamikusKirajzol(DinamikusMező dm, AffineTransform center, Graphics g) {
		int centerX= (int) center.getTranslateX();
		int centerY= (int) center.getTranslateY();
		int x = centerX;
		int y = centerY;
		
		DinamikusMező dmm= dm;
		while(dmm!=null){
			dinamikusHozzárajzol(dmm, x, y, g);
			int x2=x;
			int y2=y-h/Pályabetöltő.getSzélességiFelosztás();
			DinamikusMező dmm2 = dmm.getSzomszéd(Irány.n);
			while (dmm2!=null){
				dinamikusHozzárajzol(dmm2, x2, y2, g);
				dmm2=dmm2.getSzomszéd(Irány.n);
				y2-=h/Pályabetöltő.getMagasságiFelosztás();
			}
			dmm2 = dmm.getSzomszéd(Irány.s);
			x2=x;
			y2=y+h/Pályabetöltő.getMagasságiFelosztás();
			while (dmm2!=null){
				dinamikusHozzárajzol(dmm2, x2, y2, g);
				dmm2=dmm2.getSzomszéd(Irány.s);
				y2+=h/Pályabetöltő.getMagasságiFelosztás();
			}
			x+=w/Pályabetöltő.getSzélességiFelosztás();
			dmm=dmm.getSzomszéd(Irány.e);
		}
		
		dmm= dm;
		x= centerX;
		y=centerY;
		while(dmm!=null){
			dinamikusHozzárajzol(dmm, x, y, g);
			int x2=x;
			int y2=y-h/Pályabetöltő.getMagasságiFelosztás();
			DinamikusMező smm2 = dmm.getSzomszéd(Irány.n);
			while (smm2!=null){
				dinamikusHozzárajzol(smm2, x2, y2, g);
				smm2=smm2.getSzomszéd(Irány.n);
				y2-=h/Pályabetöltő.getMagasságiFelosztás();
			}
			smm2 = dmm.getSzomszéd(Irány.s);
			x2=x;
			y2=y+h/Pályabetöltő.getMagasságiFelosztás();;
			while (smm2!=null){
				dinamikusHozzárajzol(smm2, x2, y2, g);
				smm2=smm2.getSzomszéd(Irány.s);
				y2+=h/Pályabetöltő.getMagasságiFelosztás();;
			}
			x-=w/Pályabetöltő.getSzélességiFelosztás();
			dmm=dmm.getSzomszéd(Irány.w);
		}		
	}

	private void statikusHozzárajzol(StatikusMező sm, int x, int y, Graphics g){
		Irány forgatás=Irány.s;
		if(sm!=null && sm.getPályaelem().valtozhatEAkadalyMivolta()){
			boolean négyzetes=textúrák.get(12).getWidth()==textúrák.get(12).getHeight();
			if(sm!=null && sm.getPályaelem().valtozhatEAkadalyMivolta() && négyzetes){
				if(sm.getSzomszéd(Irány.w)!=null && sm.getSzomszéd(Irány.e)!=null)
					if(!sm.getSzomszéd(Irány.w).getPályaelem().getAkadály() && !sm.getSzomszéd(Irány.e).getPályaelem().getAkadály()) forgatás=Irány.e;
				if(sm.getSzomszéd(Irány.n)!=null && sm.getSzomszéd(Irány.s)!=null)
					if(!sm.getSzomszéd(Irány.n).getPályaelem().getAkadály() && !sm.getSzomszéd(Irány.s).getPályaelem().getAkadály()) forgatás=Irány.s;
			}
			if(sm!=null && sm.getPályaelem().valtozhatEAkadalyMivolta() && forgatás==Irány.e && négyzetes){
				AffineTransform affineTransform = new AffineTransform();
				affineTransform.translate(x, y+sm.getPályaelem().képKeresés(pályaelemVisitor).getHeight());
				affineTransform.rotate(IrányForgatás(forgatás));
				Graphics2D graphics2d = (Graphics2D) g;
				graphics2d.drawImage(sm.getPályaelem().képKeresés(pályaelemVisitor), affineTransform, null);
				return;
			}
		}
		
		g.drawImage(sm.getPályaelem().képKeresés(pályaelemVisitor), x, y, null);
	}
	
	private void offHozzárajzol(int x, int y, Graphics g){
		if(textúrák.get(8).getWidth()!=textúrák.get(8).getHeight()) return;
		g.drawImage(textúrák.get(8), x, y, null);
	}
	
	private void offMezőböl(int xCenter, int yCenter, Graphics g){
		int x=xCenter;
		int y=yCenter;
		while(x>0-h){	//a bal félteke off kitöltés
			offHozzárajzol(x, y, g);
			x-=h;
		}
		x=xCenter;
		while(x<this.getWidth()+h){	//a jobb félteke off kitöltés
			offHozzárajzol(x, y, g);
			x+=h;
		}
	}
	
	private void offKirajzol(AffineTransform at, Graphics g){
		int x=(int) at.getTranslateX();
		int y=(int) at.getTranslateY();
		while(y>0-w){
			offMezőböl(x, y, g);
			y-=w;
		}
		y=(int) at.getTranslateY();
		while(y<this.getHeight()+w){
			offMezőböl(x, y, g);;
			y+=w;
		}
	}
	
	private double IrányForgatás(Irány irány){
		switch (irány) {
		case n: return Math.PI;
		case e:	return -Math.PI/2;
		case s:	return 0;
		case w: return Math.PI/2;
		default: return 0;
		}
	}
	
	private void dinamikusHozzárajzol(DinamikusMező dm, int x, int y, Graphics g){
		ArrayList<BufferedImage> din = dinamikusKépe(dm);
		if(din==null) return;		
		AffineTransform affineTransform = new AffineTransform();
		Graphics2D graphics2d = (Graphics2D) g;
		
		affineTransform.translate(din.get(0).getWidth()/2, din.get(0).getHeight()/2);
		affineTransform.translate(x, y);
				
		if (dm.getReplikátor()!=null){
			affineTransform.rotate(IrányForgatás(dm.getReplikátor().getIrány()));
		}
		if(dm.getReplikátor()==null && dm.getEzredes()!=null){
			affineTransform.rotate(IrányForgatás(dm.getEzredes().getIrány()));
		}
		if(dm.getCsk()!=null){
			affineTransform.rotate(IrányForgatás(dm.getCsk().getFrontIrány()));
		}
		affineTransform.translate(-din.get(0).getWidth()/2, -din.get(0).getHeight()/2);
		
		graphics2d.drawImage(din.get(0), affineTransform, null);
		if(din.size()==2) {
			AffineTransform aTransform2 = new AffineTransform();
			aTransform2.translate(din.get(1).getWidth()/2, din.get(0).getHeight()/2);
			aTransform2.translate(x, y);
			if(dm.getEzredes()!=null)aTransform2.rotate(IrányForgatás(dm.getEzredes().getIrány()));
			aTransform2.translate(-din.get(1).getWidth()/2, -din.get(1).getHeight()/2);
			graphics2d.drawImage(din.get(1), aTransform2, null);
		}
		
	}
	
	private void statikusKirajzol(StatikusMező sm, AffineTransform center, Graphics g){
		int centerX= (int) center.getTranslateX();
		int centerY= (int) center.getTranslateY();
		int x = centerX;
		int y = centerY;
		
		StatikusMező smm= sm;	//mozgó statikus mező = a kapott statkius mező
		while(smm!=null){
			statikusHozzárajzol(smm, x, y, g);
			int x2=x;
			int y2=y-h;
			StatikusMező smm2 = smm.getSzomszéd(Irány.n);
			while (smm2!=null){
				statikusHozzárajzol(smm2, x2, y2, g);
				smm2=smm2.getSzomszéd(Irány.n);
				y2-=h;
			}
			smm2 = smm.getSzomszéd(Irány.s);
			x2=x;
			y2=y+h;
			while (smm2!=null){
				statikusHozzárajzol(smm2, x2, y2, g);
				smm2=smm2.getSzomszéd(Irány.s);
				y2+=h;
			}
			x+=w;
			smm=smm.getSzomszéd(Irány.e);
		}
		
		smm= sm;
		x= centerX;
		y=centerY;
		while(smm!=null){
			statikusHozzárajzol(smm, x, y, g);
			int x2=x;
			int y2=y-h;
			StatikusMező smm2 = smm.getSzomszéd(Irány.n);
			while (smm2!=null){
				statikusHozzárajzol(smm2, x2, y2, g);
				smm2=smm2.getSzomszéd(Irány.n);
				y2-=h;
			}
			smm2 = smm.getSzomszéd(Irány.s);
			x2=x;
			y2=y+h;
			while (smm2!=null){
				statikusHozzárajzol(smm2, x2, y2, g);
				smm2=smm2.getSzomszéd(Irány.s);
				y2+=h;
			}
			x-=w;
			smm=smm.getSzomszéd(Irány.w);
		}
	}
		
}
