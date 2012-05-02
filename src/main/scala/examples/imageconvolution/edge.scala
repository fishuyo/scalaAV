
package com.fishuyo
package examples.edgedetect

import graphics._
import maths._
import ray._
import media._

import java.awt.event._
import java.nio._
import javax.imageio._
import java.io._
import java.awt.image.BufferedImage

import com.jogamp.common.nio.Buffers
import com.jogamp.opengl.util.awt.Screenshot

object Input extends KeyListener with MouseListener with MouseMotionListener {

  def mouseReleased( e: MouseEvent) = {}
  def mousePressed( e: MouseEvent) = {

    val x = e.getX
    val y = e.getY
    println( x + " " + y )
    
    val o = Camera.position
    val v = Camera.projectPoint( x, y )
    println( v )
    
    val r = new Ray( o, v-o )
    val xx = ((r(200.f).x + 1.f) * Main.n/2).toInt  
    val yy = ((r(200.f).y + 1.f) * Main.n/2).toInt 
    println( xx + " " + yy )
    //if( xx >= 0 && xx <= Main.n-1 && yy >= 0 && yy <= Main.n-1 ) Main.field.set( xx,yy, 1.f )
  }

  def mouseClicked( e: MouseEvent) = {}
  def mouseEntered( e: MouseEvent) = {}
  def mouseExited( e: MouseEvent) = {}
  def mouseMoved( e: MouseEvent) = {
    val x = e.getX
    val y = e.getY
    
    val o = Camera.position
    val v = Camera.projectPoint( x, y )
    
    val r = new Ray( o, v-o )
    val xx = ((r(200.f).x + 1.f) * Main.n/2).toInt  
    val yy = ((r(200.f).y + 1.f) * Main.n/2).toInt 
    if( xx >= 0 && xx <= Main.n-1 && yy >= 0 && yy <= Main.n-1 ){
      println( "("+xx+", "+ yy+" ) : " + Main.field(xx,yy) );
    }
  }
  def mouseDragged( e: MouseEvent) = {
    mousePressed(e);
  }
  
  def keyPressed( e: KeyEvent ) = {
    val keyCode = e.getKeyCode()
    if( keyCode == KeyEvent.VK_ENTER ){
      Main.field.sstep(0)
    }
    if( keyCode == KeyEvent.VK_M ){
      //Main.field.go = !Main.field.go
      
      Main.win.capture match{ 
        case v:MediaWriter => v.close(); Main.win.capture = null; Main.field.go = false;
        case _ => Main.win.capture = new MediaWriter; Main.field.go = true;
      }
    }
    if( keyCode == KeyEvent.VK_R ){
      Main.field.readImage( "input.png" )
    }
  }
  def keyReleased( e: KeyEvent ) = {}
  def keyTyped( e: KeyEvent ) = {}


}

object Main extends App {

  var n = 600;
  val field = new EdgeField
  field.allocate(n,n)

  GLScene.push( field );

  val win = new GLRenderWindow
  win.glcanvas.addKeyListener( Input )
  win.glcanvas.addMouseListener( Input )
  win.glcanvas.addMouseMotionListener( Input )

}

class EdgeField extends Field2D {
  
  val colors = Array( Vec3(.3,.7,.1), Vec3(.6, .1, .1), Vec3( .1, 0,.8), Vec3( 1,.9,.9))
  var walk = (0,0) //Vec3(0) //Field2D
  var c = Vec3(1)

  val next = new Vec3Field2D

  override def sstep(dt:Float) = {

    if( next.w == 0 ) next.allocate( w,h )

  
  }

  def color( x: Float ) : Vec3 = {

    var i = math.abs(2*x*colors.length-1).toInt - 1
    if( i < 0 ) i = 0
    if( i >= colors.length-1) i = colors.length-2
    val c1 = colors(i)
    val c2 = colors(i+1)
    c1 * (1-x) + c2 * (x)

  }

}


