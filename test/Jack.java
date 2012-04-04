package beam;

import de.gulden.framework.jjack.*;

public class Jack {

  public static void main( String[] args ){
    Gain g = new Gain();
    JJackSystem.setProcessor( g );

    while(true){}
  }
}
