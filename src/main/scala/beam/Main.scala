
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
  
  RayTracer.start
  RayTracer ! Rays(100)

  Convolver.start
  AudioOut.start

  //AudioOut.open( ) 
  //AudioOut.start
  //JJackSystem.setProcessor( AudioOut )

  //run loop
  //take input and output sound and visual
  //while(true){}
  //AudioOut.write( samples )
  
  /*val size = 1024
  val b = in.buffer( size )
  var left = in.numFrames
  while( left > 0 ){
    val n = math.min( size, left )
    in.read( b )
    AudioOut.write( b(0) )
    left -= n
  }*/


}

