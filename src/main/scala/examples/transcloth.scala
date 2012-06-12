
package com.fishuyo
package trans.dronefabric
import maths._
import graphics._
import spatial._
import trees._

import de.sciss.osc._
import java.awt.event._

object Main extends App{
  

  //var trees = TreeNode( Vec3(0), .1f ) :: TreeNode( Vec3(3.f,0,0), .3f ) :: TreeNode( Vec3( 6.f,0,0), .3f) :: TreeNode( Vec3(9.f,0,0), .1f) :: List()
  var fabrics = Fabric( Vec3(3.f,0,0), 1.f,1.f,.05f,1) :: Fabric( Vec3(0,0,3.f),1.f,1.f,.05f) :: Fabric( Vec3(-3.f,0,0),1.f,1.f,.05f,1) :: List()

  //trees(0).branch( 6, 45.f, .8f, 0)
  //trees(1).branch( 5, 10.f, .9f, 0)
  //trees(2).branch( 10, 20.f, .5f, 0)
  //trees(3).branch( 8, 20.f, .5f, 0)

  //build scene by pushing objects to singleton GLScene (GLRenderWindow renders it by default)
  //trees.foreach( t => GLScene.push( t ) )
  fabrics.foreach( f => GLScene.push( f ) )
  val window = new GLRenderWindow
  val window2 = new GLRenderWindow
  val window3 = new GLRenderWindow
  window.addKeyMouseListener( Input )
  window2.addKeyMouseListener( Input )
  window3.addKeyMouseListener( Input )


}

object Input extends io.KeyMouseListener {

  var g = true

  override def keyPressed( e: KeyEvent ){
    val k = e.getKeyCode
    k match {
      case KeyEvent.VK_G => 
        g = !g
        if(g) Fabric.g = -10.f else Fabric.g = 0.f
        println( "Gravity: " + Fabric.g )

      case _ => null
    }

  }


}

object DroneWindField extends VecField3D(10,Vec3(0),5.f) {

  var dronePos = Vec3(0)

}

object TransTrack {

  val d2r = math.Pi/180.f
  val cfg         = UDP.Config()
  cfg.localPort   = 10000  // 0x53 0x4F or 'SO'
  val rcv         = UDP.Receiver( cfg )
  val sync = new AnyRef
  
  println( "TransTrack listening on UDP " + cfg.localPort )

  //rcv.dump( Dump.Both )
  rcv.action = {
    case (Message( name, x:Float,y:Float,z:Float,w:Float, _ @ _* ), _) =>
      if( name.startsWith("/tracker1")){
        DroneWindField.dronePos = Vec3(x,y,z)
      }
  }

  rcv.connect()
  //sync.synchronized( sync.wait() )
 
}

