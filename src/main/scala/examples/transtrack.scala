
package com.fishuyo
package examples.transtrack

import graphics._
import maths._
import ray._

import de.sciss.osc._
import com.codeminders.ardrone._

object Main extends App {


  import Implicits._

  println( """
  Listening on UDP port 10001
  Send "/quit" to terminate.
  """ )

  val cfg         = UDP.Config()
  cfg.localPort   = 10001  // 0x53 0x4F or 'SO'
  val rcv         = UDP.Receiver( cfg )
  val sync = new AnyRef

  rcv.dump( Dump.Both )
  rcv.action = {

    case (Message( name, x,y,z, _ @ _* ), _) =>
      println("xyz")
    case (Message( name, v @ _* ), _) =>
      println( "Received message '" + name + "'" )

      if( name.endsWith("toggle4") ) Drone.toggleFly
      if( name == "/takeoff" ) sync.synchronized( sync.notifyAll() )
      if( name == "/quit" ) sync.synchronized( sync.notifyAll() )
    case (p, addr) => println( "Ignoring: " + p + " from " + addr )
  }
  
  rcv.connect()
  sync.synchronized( sync.wait() )
  
  GLScene.push( new GLDrawable{})

  val win = new GLRenderWindow
  //win.glcanvas.addMouseListener( Mouse )
  //win.glcanvas.addMouseMotionListener( Mouse )

}


object Drone {

  var flying = false
  val drone = new ARDrone
  
  try {
    connect
    clearEmergency
    drone.waitForReady(5000)
    println("Drone connected and ready...")
    
    trim

  } catch {
    case e: Throwable => e.printStackTrace
  }

  def connect() = drone.connect
  def disconnect() = drone.disconnect
  def clearEmergency() = drone.clearEmergencySignal
  def trim() = drone.trim
  def takeOff() = drone.takeOff
  def land() = drone.land

  def toggleFly = {
    if( flying ) drone.land
    else drone.takeOff
    flying = !flying
  }

}
