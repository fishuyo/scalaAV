
package com.fishuyo
package examples.mat200c

import graphics._
import maths._
import ray._

import java.awt.event._

object Mouse extends MouseListener with MouseMotionListener {

  def mouseReleased( e: MouseEvent) = {}
  def mousePressed( e: MouseEvent) = {

    val x = e.getX
    val y = e.getY
    println( x + " " + y )
    
    val o = Camera.position
    val v = Camera.projectPoint( x, y )
    println( v )
    
    val r = new Ray( o, v-o )
    val xx = ((r(200.f).x + 1.f) * 32).toInt  
    val yy = ((r(200.f).y + 1.f) * 32).toInt 
    println( xx + " " + yy )
    if( xx >= 0 && xx <= 63 && yy >= 0 && yy <= 63 ) Main.field.set( xx,yy, 1.f )
  }

  def mouseClicked( e: MouseEvent) = {}
  def mouseEntered( e: MouseEvent) = {}
  def mouseExited( e: MouseEvent) = {}
  def mouseMoved( e: MouseEvent) = {}
  def mouseDragged( e: MouseEvent) = {}


}

object Main extends App {

  val n = 64;
  val field = Field2D( n, n )
  field.data.put( Array(1.0f, 0.f,0.f,0.f,0.f,1.f,0.f,0.f,0.f,0.f,1.f,0.f,0.f,0.f,0.f,1.f ));
  field.data.rewind();
  println( field.data.capacity );

  GLScene.push( field );

  val win = new GLRenderWindow
  win.glcanvas.addMouseListener( Mouse )
  win.glcanvas.addMouseMotionListener( Mouse )

}
