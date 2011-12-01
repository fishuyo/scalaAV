
package beam

import javax.imageio._
import java.io._
//import javax.swing._
import java.awt.image.BufferedImage

import de.sciss.synth.io._

import scalacl._

object Main extends App{
  
  implicit val context = Context.best

  //build scene
  Scene.initCubeRoom()

  val window = new GLRenderWindow
  

  //partition scene

  //build beam tree


  val sound = new SAudioFile( "acoustics.wav" )
  val in = AudioFile.openRead( "acoustics.wav" )

  //val samples = sound.getSamples
  
  AudioOut.open( sound.stream.getFormat ) 
  AudioOut.line.get.start
  //JJackSystem.setProcessor( AudioOut )

  //run loop
  //take input and output sound and visual
  //while(true){}
  //AudioOut.write( samples )
  
  val size = 1024
  val b = in.buffer( size )
  var left = in.numFrames
  while( left > 0 ){
    val n = math.min( size, left )
    in.read( b )
    AudioOut.write( b(0) )
    left -= n
  }


  /*var nbytes=0
  var len=1024
  val buf = new Array[Byte](len*4)
  while(nbytes != -1){
  
    nbytes = sound.stream.read( buf, 0, len )
    
    if( nbytes > 0 )
      AudioOut.write( buf, 0, nbytes ) 
  }*/

//  AudioOut.line.get.drain

//  AudioOut.line.get.close

}

