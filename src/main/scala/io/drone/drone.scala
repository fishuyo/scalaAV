
package com.fishuyo
package io
package drone

import maths._

import com.codeminders.ardrone._

import java.net._
import java.awt.event._
import de.sciss.osc._

object Drone {

  var ready = false
  var flying = false
  var drone : ARDrone = _
  var ip = "192.168.1.1"
  var pos = Vec3(0)
  var dest = Vec3(0)
  var yaw = 0.f
  var destYaw = 0.f
  //init()

  def init( ) = {
    try {
      drone = new ARDrone( InetAddress.getByName(ip) )
      println("Connecting...")
      connect
      println("Drone Connected.")
      clearEmergency
      drone.waitForReady(5000)
      println("Drone connected and ready!")
      ready = true
      trim

    } catch {
      case e: Throwable => e.printStackTrace
    }
  }

  def connect() = drone.connect
  def disconnect() = drone.disconnect
  def clearEmergency() = drone.clearEmergencySignal
  def trim() = drone.trim
  def takeOff() = drone.takeOff
  def land() = drone.land

  def playLed = drone.playLED(1, 10, 5 )

  def toggleFly = {
  if( flying ) drone.land
  else drone.takeOff
  flying = !flying
  println( "fly: " + flying )
  }

  def move( lr: Float, fb: Float, ud: Float, r: Float ) = drone.move(lr,fb,ud,r)
  def hover = drone.hover

}

object DroneKeyboardControl extends KeyMouseListener {


  override def keyPressed( e: KeyEvent ) = {
    var lr = 0.f
    var fb = 0.f
    var ud = 0.f
    var rot = 0.f
    val keyCode = e.getKeyCode()
    if( keyCode == KeyEvent.VK_ENTER ){

      Drone.toggleFly

      //import util.Random.nextFloat
      //Main.field.c += Vec3( .1f*(nextFloat - .5f), .1f*(nextFloat - .5f), 0 )
      //Main.field.sstep(0)
    }
    if( keyCode == KeyEvent.VK_ESCAPE ) Drone.land
    if( keyCode == KeyEvent.VK_P ) Drone.init
    if( keyCode == KeyEvent.VK_F) lr += -.5f
    if( keyCode == KeyEvent.VK_H) lr += .5f
    if( keyCode == KeyEvent.VK_T) fb += .5f
    if( keyCode == KeyEvent.VK_G) fb += -.5f

    if( keyCode == KeyEvent.VK_J) rot += -.5f
    if( keyCode == KeyEvent.VK_L) rot += .5f
    if( keyCode == KeyEvent.VK_I) ud += .5f
    if( keyCode == KeyEvent.VK_K) ud += -.5f

    if( lr != 0.f || fb != 0.f || rot != 0.f || ud != 0.f ) Drone.move( lr, fb, ud, rot )

    if( keyCode == KeyEvent.VK_SPACE ) Drone.hover

    if( keyCode == KeyEvent.VK_Y ) Drone.takeOff

    if( keyCode == KeyEvent.VK_M ){
      //Main.field.go = !Main.field.go

      //Main.win.capture match{
      //  case v:MediaWriter => v.close(); Main.win.capture = null; Main.field.go = false;
      //  case _ => Main.win.capture = new MediaWriter; Main.field.go = true;
      //}
    }
  }
  override def keyReleased( e: KeyEvent ) = {
    val k = e.getKeyCode()
    if( Drone.drone != null )
    if( k == KeyEvent.VK_SPACE)
    Drone.move(0,0,0,0)
  }
  override def keyTyped( e: KeyEvent ) = {}
}

class DroneOSCControl( val port:Int) {

  val cfg = UDP.Config()
  cfg.localPort = port
  val rcv = UDP.Receiver( cfg )

  //rcv.dump( Dump.Both )
  rcv.action = {
    case(Message( "/drone/connect", _ @ _*), _) => Drone.init
    case(Message( "/drone/takeoff", _ @ _*), _) => println("TAKEOFF!"); Drone.takeOff
    case(Message( "/drone/land", _ @ _*), _) => println("LAND!"); Drone.land
    case(Message( "/drone/move", a:Float,b:Float,c:Float,d:Float ), _) => println("MOVE: " + a + " " + b + " " + c + " " + d); Drone.move(a,b,c,d)
    case(Message( "/drone/moveto", x:Float,y:Float,z:Float,w:Float ), _) => println("MOVE_TO: "+x+" "+y+" "+z);Drone.dest = Vec3(x,y,z); Drone.destYaw = w; while( Drone.destYaw < 0.f ) Drone.destYaw += 360.f
    case(Message( "/drone/hover", _ @ _*), _) => println("HOVEr!"); Drone.hover
    case(Message( name, f @ _*), _) => null


    case( p, addr ) => null

  }
  rcv.connect()
  println( "DroneOSCControl started on port " + port )

}
