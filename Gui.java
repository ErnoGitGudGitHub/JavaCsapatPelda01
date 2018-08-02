import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.UIManager;

public class Gui {
	private JFrame programAblak;	// mainWIndow
	private JPanel balMegjelenítés;	//bal oldala
	private JPanel jobbMegjelenítés;	//a jobb oldala a megjelenítésnek	
	private ArrayList<BufferedImage> textúrák;	//a textúrákat itt lehet elérni
	private boolean editor;	//Editor mód aktív?	
	private cPálya ezredesJátékosMegjelenítés;
	private cPálya jaffaJátékosMegjelenítés;	//a két megjelenítendő oldal. a két játékos
	
	public boolean getEditor() {	//azt adja meg, hogy az Editor mód aktív-e.
		return editor;
	}
	
	public void pályaNévFrissít(String fájlnév) {
		programAblak.setTitle("CSK1 - " + fájlnév);
	}
	
	public void setBillentyűzetVezérlés(ArrayList<KeyStroke> billentyűk, ArrayList<AbstractAction> parancsok){
		for(int i=0; i<parancsok.size(); i++){
			jobbMegjelenítés.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(billentyűk.get(i), "parancs"+i);
			jobbMegjelenítés.getActionMap().put("parancs"+i, parancsok.get(i));
		}
	}
	
	public boolean Pause(boolean Pause) {
		if (editor) return true;
		if(Pause) {
			int válasz = JOptionPane.NO_OPTION;
			Object[] options = {"Folytatás",
								"Szünet",
                                "Kilépés"};
			while(válasz != JOptionPane.OK_OPTION){
				válasz = JOptionPane.showOptionDialog(programAblak,
						"A játék szüneteltetve van. A folytatáshoz nyomja meg a FOLYTATÁS gombot", "Szünet",
			    JOptionPane.YES_NO_CANCEL_OPTION,
			    JOptionPane.QUESTION_MESSAGE,
			    UIManager.getIcon("FileView.computerIcon"),
			    options,
			    options[0]);
				if(válasz==JOptionPane.CANCEL_OPTION) System.exit(0);
			}
			return false;
		}
		return true;
		
	}
	
	public void teljesKépernyőÁllít(boolean fs){	//ha a paraméter igaz akkor teljes képernyőre vált, egyébkén  alap kisablakot
		if(fs){
			if(programAblak.getExtendedState()==JFrame.MAXIMIZED_BOTH) return;
			programAblak.dispose();
			programAblak.setUndecorated(true);
			programAblak.setExtendedState(JFrame.MAXIMIZED_BOTH);
			programAblak.setVisible(true);
		} else {
			if(programAblak.getExtendedState()==JFrame.NORMAL) System.exit(7);	//A kisképernyő gombal kilép a program, ha már kis képernyőn volt.
			programAblak.dispose();
			programAblak.setUndecorated(false);
			programAblak.setExtendedState(JFrame.NORMAL);
			programAblak.setSize(1024, 768);
			programAblak.setVisible(true);
		}
	}
	
	public void JátékNézet(){
		editor=false;
	}
	
	public void SzerkeszőiNézet(){
		editor=true;
	}
		
	private void osztottképernyőInit(){	//beállítja a képernyőt osztott képernyős módra.
		GridLayout layout = new GridLayout(1, 2);
		programAblak.setLayout(layout);
		balMegjelenítés=new JPanel(new BorderLayout());
		jobbMegjelenítés=new JPanel(new BorderLayout());
		programAblak.add(jobbMegjelenítés);
		programAblak.add(balMegjelenítés);
		ezredesJátékosMegjelenítés = new cPálya();
		jaffaJátékosMegjelenítés = new cPálya();
		
		balMegjelenítés.add(ezredesJátékosMegjelenítés);
		jobbMegjelenítés.add(jaffaJátékosMegjelenítés);
		programAblak.setVisible(true);
	}
	
	public void textúrákBeállítása(String textúrákElérésiÚtvonala){	//a textúrákat állítja be, az elérési útvonalról.
		textúrák = textúrákBeolvasása(textúrákElérése(textúrákElérésiÚtvonala));
		cPálya.addKépek(textúrák);
	}
	
	public Gui() throws IOException{	//a főablak létrehozása
		programAblak =  new JFrame("CSK1");
		programAblak.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		programAblak.setSize(1024, 768);
		ImageIcon img = new ImageIcon("ikon.jpg");	//ikon beállítása
		programAblak.setIconImage(img.getImage());
		programAblak.setVisible(true);	//láthatóvá tétele a főablaknak
		osztottképernyőInit();
		JátékNézet();	//alap játékos nézet
	}
	
	public void üdvözlőAblak(String helpButton) {
		int válasz = JOptionPane.NO_OPTION;
		Object[] options = {"Játék",
                            "Kilépés"};
		while(válasz != JOptionPane.OK_OPTION){
			String üdvözlő = "A játék során az Ezredest és a Jaffát kell irányítani\n"
					+ "Az "
					+ helpButton
					+ " billentyű segítségével a sugót lehet előhozni\n"
					+ "A játék véget ér, ha teljesül valamely karakter küldetése";
			válasz = JOptionPane.showOptionDialog(programAblak,üdvözlő,
		    "Üdvözöllek a játékban", JOptionPane.YES_NO_OPTION,
		    JOptionPane.QUESTION_MESSAGE,
		    UIManager.getIcon("FileChooser.homeFolderIcon"),
		    options,
		    options[0]);
			if(válasz==JOptionPane.NO_OPTION) System.exit(0);
		}
	}
	
	private ArrayList<String> textúrákElérése(String elérésiÚtvonal) {
				
		String sor=null;	//a sorokat ide olvassuk majd be
		BufferedReader reader=null;	//Üres buffer reader
		ArrayList<String> ret = new ArrayList<String>();
		
		
		String conc=elérésiÚtvonal.substring(0,elérésiÚtvonal.lastIndexOf('/')+1);

		try {
			reader = new BufferedReader(new FileReader(elérésiÚtvonal));	//az elérési útvonalon levő filet megnyitjuk
			
			while ((sor = reader.readLine()) != null) {	//soronként beolvassuk a filet
				String add = conc+sor;
				ret.add(add);	//minden sort hozáadunk a listához
			}//a folyamat végére a teljes textúra elérést beolvastuk
		}
		catch (FileNotFoundException e) {	//Ha az elérési útvonal hibás, akkor a file nem nyitható meg
			System.err.println("A textúraElérési file elérési útvonala hibás, a file nincs meg:");
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("IO kivétel volt a textúraElérési file elérésekor:");
			e.printStackTrace();	//Ha io kivétel volt, kiírjuk a stacket
		}
		finally {
		    try {
				reader.close();	//Lezárjuk a readert
			} catch (IOException e) {
				System.err.println("IO kivétel volt a textúraElérési file lezárásakor:");
				e.printStackTrace();	//Ha nem volt reader akkor a StackTracet kiírjuk
			}
		}
		return ret;
	}
	
	private ArrayList<BufferedImage> textúrákBeolvasása(ArrayList<String> elérésiÚtvonal){
		 ArrayList<BufferedImage> ret=new ArrayList<BufferedImage>();
		 
		 for (String útvonal : elérésiÚtvonal) {
			 try {
				 ret.add(ImageIO.read(new File(útvonal)));
			} catch (Exception e) {
				System.err.println("Kivétel volt a textúrák betöltésekor: "+útvonal+" eléréssel");
				e.printStackTrace();	//Ha nem volt reader akkor a StackTracet kiírjuk
			}
		}
		return ret;
	}
		
	public void guiFrissít(DinamikusMező balDinamikus, DinamikusMező jobbDinamikus) throws IOException{
		ezredesJátékosMegjelenítés.setKözép(balDinamikus);	//bal megjelenítés
		jaffaJátékosMegjelenítés.setKözép(jobbDinamikus);	//jobb megjelenítés
		programAblak.repaint();	//újra rajzolás
		programAblak.validate();
	}

	public void videóKijátszása(boolean vereség) {
	    try {
	    	if (programAblak.getExtendedState()==JFrame.MAXIMIZED_BOTH) teljesKépernyőÁllít(false);
			if(!vereség) Desktop.getDesktop().open(new File("vége/vége.avi"));
			else Desktop.getDesktop().open(new File("vége/vereség.avi"));
		} catch (IOException e) {
			if(vereség) System.err.println("Hiba a vereség videó beolvasásakor: \"vége/vereség.avi\"");
				else System.err.println("Hiba a vége videó beolvasásakor: \"vége/vége.avi\"");
			e.printStackTrace();
		}
	}

	public void súgóAblak(ArrayList<String> gombok) {
		Object[] súgóOptions = {"Rendben", "Több >>"};
		Object[] többOptions = {"Rendben", "Vissza"};
		
		if(editor){
			String editorSúgóFelirat = gombok.get(19)
					+ ": Szerkesztő/Játék mód közti váltás\n"
					+ gombok.get(28)
					+ ": Padló lerakása\n"
					+ gombok.get(21)
					+ ": Szakadék lerakása\n"
					+ gombok.get(22)
					+ ": Fal lerakása\n"
					+ gombok.get(23)
					+ ": Speciális fal lerakása\n"
					+ gombok.get(24)
					+ ": Ajtó lerakása\n"
					+ gombok.get(25)
					+ ": az Ajtóhoz tartozó Mérleg lerakása\n"
					+ gombok.get(31)
					+ ": inaktív mérleg lerakása\n"
					+ gombok.get(27)
					+ ": Doboz lerakása\n"
					+ gombok.get(26)
					+ ": ZPM lerakása\n"
					+ gombok.get(29)
					+ ": Replikátor lerakása\n"
					+ gombok.get(30)
					+ ": az editor módosításainak\n"
					+ "mentése fileba, a kör eleje óta";
			int válasz=JOptionPane.NO_OPTION;
			while(válasz!=JOptionPane.YES_OPTION){
				válasz=JOptionPane.showOptionDialog(programAblak, editorSúgóFelirat, "Editor súgó", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
						UIManager.getIcon("OptionPane.informationIcon"),	//ikon
						súgóOptions,  //A gombok Objektuma
						súgóOptions[0]); //A gombok közül az alapértelmezett
				if(válasz==JOptionPane.NO_OPTION){
					String játékosTöbb = "A szerkesztői mód segítségével\n"
							+ "a pálya elemeit lehet módosítani.\n"
							+ "Az editor karakterrer a pillanatnyi pozíción,\n"
							+ "a pozícióhoz tartozó pályaelemet lehet módosítani\n"
							+ "vagy a mezőre lehet lehelyezni tárgyakat.\n"
							+ "Az elmentett módosítások az \"editor.txt\" fileba menti\n"
							+ "a program. Annak tartalmát egy pályafileba kell bemásolni.\n"
							+ "A fileban a lehelyezett ZPM ek száma is kiírásra kerül,\n"
							+ "azt össze kell adni, a már pályán levő ZPM ek számával.\n"
							+ "A szerkesztői móddal játékosokat nem lehet lerakni, azt\n"
							+ "csak kézzel lehetséges egy pályához hozzáadni.";
					válasz=JOptionPane.showOptionDialog(programAblak, játékosTöbb, "Több segítség az editorhoz", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
							UIManager.getIcon("OptionPane.informationIcon"),	//ikon
							többOptions,  //A gombok Objektuma
							többOptions[0]); //A gombok közül az alapértelmezett
				}
			}
		}
		else {
			String játékSúgóFelirat = "Ezredes irányítása:\n"
					+ gombok.get(0)
					+ ": észak\n"
					+ gombok.get(3)
					+ ": nyugat\n"
					+ gombok.get(2)
					+ ": dél\n"
					+ gombok.get(1)
					+ ": kelet\n"
					+ gombok.get(4)
					+ ": elsődleges lövés\n"
					+ gombok.get(5)
					+ ": másodlagos lövés\n"
					+ gombok.get(6)
					+ ": tárgy használat"
					+ "\n\nJaffa irányítása:\n"
					+ gombok.get(8)
					+ ": észak\n"
					+ gombok.get(11)
					+ ": nyugat\n"
					+ gombok.get(10)
					+ ": dél\n"
					+ gombok.get(9)
					+ ": kelet\n"
					+ gombok.get(12)
					+ ": elsődleges lövés\n"
					+ gombok.get(13)
					+ ": másodlagos lövés\n"
					+ gombok.get(14)
					+ ": tárgy használat"
					+ "\n\n"
					+ gombok.get(18)
					+ ": Pályra újratöltése\n"
					+ gombok.get(7)
					+ ": Pálya váltása\n"
					+ gombok.get(15)
					+ ": Játék szüneltetése\n"
					+ gombok.get(16)
					+ ": Teljes képernyős mód\n"
					+ gombok.get(17)
					+ ": Kis képernyő/Kilépés;\n"
					+ gombok.get(19)
					+ ": Szerkesztő/Játék mód közti váltás";
			int válasz=JOptionPane.NO_OPTION;
			while(válasz!=JOptionPane.YES_OPTION){
				válasz=JOptionPane.showOptionDialog(programAblak, játékSúgóFelirat, "Játékos súgó", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
						UIManager.getIcon("OptionPane.informationIcon"),	//ikon
						súgóOptions,  //A gombok Objektuma
						súgóOptions[0]); //A gombok közül az alapértelmezett
				if(válasz==JOptionPane.NO_OPTION){
					String játékosTöbb = "Az Ezredessel és a Jaffával a ZPM-eket\n"
							+ "kell összeszedni. Ahhoz, hogy az Ezredes nyerjen\n"
							+ "és a Föld megmeneküljön, majdnem az összes ZPM et \n"
							+ "össze kell gyűjteni. A Jaffának 3 darab ZPM-et kell\n"
							+ "felszednie ahhoz, hogy a gonosz győzedelmeskedjen.\n"
							+ "Ha a Jaffa még nem nyerte meg a játékot, az Ezredesnek\n"
							+ "van még lehetősége megmenteni a bolygót.\n"
							+ "Ha a két játékos összességével a pálya kezdete óta\n"
							+ "felszed 2 darab ZPM-et és még nincs vége a játéknak,\n"
							+ "akkor megjelenik egy eldugott ZPM a pályán, ami\n"
							+ "odáig nem volt ott.\n";
					válasz=JOptionPane.showOptionDialog(programAblak, játékosTöbb, "Több segítség a játékhoz", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
							UIManager.getIcon("OptionPane.informationIcon"),	//ikon
							többOptions,  //A gombok Objektuma
							többOptions[0]); //A gombok közül az alapértelmezett
				}
			}
		}		
	}
	
	public void végeAblak(boolean gonoszHaltMeg) {
		int válasz = JOptionPane.NO_OPTION;
		Object[] options = {"Pálya újraindítása",
                            "Kilépés"};
		while(válasz != JOptionPane.OK_OPTION){
			String végeSzöveg;
			if(gonoszHaltMeg) végeSzöveg="Jaffa meghalt!\n A játéknak vége.";
				else végeSzöveg="Ezredes meghalt!\n A játéknak vége.";
			válasz = JOptionPane.showOptionDialog(programAblak,végeSzöveg,
		    "A játéknak vége", JOptionPane.YES_NO_OPTION,
		    JOptionPane.QUESTION_MESSAGE,
		    UIManager.getIcon("OptionPane.warningIcon"),
		    options,
		    options[0]);
			if(válasz==JOptionPane.NO_OPTION) System.exit(0);
		}
	}
	
	public void döntetlenAblak() {
		int válasz = JOptionPane.NO_OPTION;
		Object[] options = {"Pálya újraindítása",
                            "Kilépés"};
		while(válasz != JOptionPane.OK_OPTION){
			String végeSzöveg="A játék döntetlen lett.";
			válasz = JOptionPane.showOptionDialog(programAblak,végeSzöveg,
		    "Döntetlen", JOptionPane.YES_NO_OPTION,
		    JOptionPane.QUESTION_MESSAGE,
		    UIManager.getIcon("OptionPane.warningIcon"),
		    options,
		    options[0]);
			if(válasz==JOptionPane.NO_OPTION) System.exit(0);
		}
	}

}

