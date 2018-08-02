import java.io.File;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Hanglejátszó {
	private static Hanglejátszó instance = null;
	
	File csillagkapu_blokkolt_túlvég = new File("hangok\\csillagkapu_blokkolt_túlvég.wav");
	File csillagkapu_lekerült = new File("hangok\\csillagkapu_lekerült.wav");
	File csillagkapun_keresztül = new File("hangok\\csillagkapun_keresztül.wav");
	File ezredes_szakadékban = new File("hangok\\ezredes_szakadékban.wav");
	File féregjárat_kinyílt = new File("hangok\\féregjárat_kinyílt.wav");
	File mérleg_nyit = new File("hangok\\mérleg_nyit.wav");
	File mérleg_zár = new File("hangok\\mérleg_zár.wav");
	File nem_talált = new File("hangok\\nem_talált.wav");
	File replikátor_szakadékban = new File("hangok\\replikátor_szakadékban.wav");
	File lépés = new File("hangok\\lépés.wav");
	File doboz_szakadékban = new File("hangok\\doboz_szakadékban.wav");
	File restart = new File("hangok\\restart.wav");
	File zpm_felvétel = new File("hangok\\zpm_felvétel.wav");
	File inaktív_mérleg = new File("hangok\\inaktív_mérleg.wav");
	File replikátor_lelövése = new File("hangok\\replikátor_lelövése.wav");
	File zpm_leesik= new File("hangok\\zpm_leesik.wav");
	//File  = new File("hangok\\.wav");
	
	
    protected Hanglejátszó() {} 	//ne lehessen példányosítani, védett
    
    public static Hanglejátszó getInstance() {	//elérhető példány
		if (instance == null) instance = new Hanglejátszó();	//singleton
		return instance;	//ha nincs példány, létrehoz, és visszatér vele
    }
    
    public void playSound(File sound) {	//a definiált hangok lejátszhatók vele, vagy egyéb File ok.
 	   try {
 		   Clip clip = AudioSystem.getClip();	//a hang kép létrehozása
 		   clip.open(AudioSystem.getAudioInputStream(sound));	//audió beolvasása
 		   clip.start();	//hang kijátszása
 		   
 	   } catch(Exception e){
 		   System.err.println("A hangfile elérésekor hiba jelentkezett: "+sound.getName());
 		   e.printStackTrace();
 	   }
    }
}

