

import beam.AudioOut;
import de.gulden.framework.jjack.*;

public class Maain {

  public static void main( String[] args ){
    Class.forName("de.gulden.framework.jjack.JJackSystem");
  
  //build scene

  //partition scene

  //build beam tree


  //val sound = new AudioFile( "grand.wav" )

  //AudioOut.open( sound.stream.getFormat ) 
  //AudioOut.line.get.start
    //JJackAudioProcessor a = new AudioOut();
    AudioOut a;
    JJackSystem.setProcessor( (JJackAudioProcessor)a );

  //run loop
  //take input and output sound and visual
    while(true){}

  }

}
