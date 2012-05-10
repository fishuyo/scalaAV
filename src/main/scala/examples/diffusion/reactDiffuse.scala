
package com.fishuyo
package examples.reactdiffuse

import graphics._
import maths._
import ray._
import io._
import media._

import java.awt.event._


object Main extends App {

  var n = 200;
  val field = new ReactDiffuseField
  field.allocate(n,n)

  GLScene.push( field );

  val win = new GLRenderWindow
  win.addKeyMouseListener( Input )

}

class ReactDiffuseField extends Field2D {
  
  val next = new Field2D

  type RA = List[Float]
  val chemFields = new ChemField( .0002f, (l:RA) => -l(0)*l(1)*l(1) + .05f*(1-l(0)) ) :: new ChemField( .00001f, (l:RA) => l(0)*l(1)*l(1) - (.05f+.0675f)*l(1)) :: List()
  val dx = 2.f / Main.n

  override def allocate( w:Int, h:Int ) = {
    super.allocate(w,h)
    chemFields.foreach( _.allocate(w,h) )
  }

  override def sstep(dt:Float) = {

    for( j <- (-5 to 5); i <- (-5 to 5) ) chemFields(1).set( w/2+i, h/2+j, 1.f )

    if( next.w != w || next.h != h ) next.allocate( w,h )

    for( y <- ( 0 to h-1 ); x <- ( 0 to w-1 )){
      
      chemFields.foreach( _.diffuse(x,y,.1f) )
      chemFields.foreach( _.react( chemFields, x, y, .1f ) )
      
      var v = 0.f
      chemFields.foreach(  f => v += f(x,y) )

      set(x,y,v)
    }
 
    //for( i <- ( 0 until w*h ) ) set(i, next(i) )
  }
}

class ChemField( var alpha:Float, val reactFunc: (List[Float])=>Float )  extends Field2D {

  val dx = 2.f / Main.n

  def diffuse( x: Int, y:Int, dt:Float ) = {
    var v = multiplyKernel(x,y, Kernel.laplacian )
    v = this(x,y) + v * dt * alpha / (dx*dx)
    set(x,y,v)
  }

  def react( chemFields:List[ChemField], x:Int, y:Int, dt:Float ) = {
    val l = chemFields.map( (f) => f(x,y) )
    var v = this(x,y) + dt * reactFunc(l)
    set(x,y,v)
  }

  def multiplyKernel( x:Int, y:Int, l: List[Float] ) : Float = {

    var s = math.sqrt(l.length).toInt //assume odd square kernel
    s = s / 2
    var v = 0.f
    var c = 0
    for( j<-(-s to s); i<-(-s to s)){
      v += l(c) * getToroidal(x+i,y+j)
      c += 1
    }
    v
  }

}

object Kernel {
  def dx2 = List(0.f,0,0, 1,-2,1, 0,0,0 )
  def dy2 = List(0.f,1,0, 0,-2,0, 0,1,0 )
  def laplacian = List(0.f,1,0, 1,-4,1, 0,1,0 )
  def edgeEnhance = List(0.f,0.f,0.f,-1.f,1.f,0.f,0.f,0.f,0.f)
  def edgeDetect = List(0.f,1.f,0.f,1.f,-4.f,1.f,0.f,1.f,0.f)
  def emboss = List(-2.f,-1.f,0.f,-1.f,1.f,1.f,0.f,1.f,2.f)
  def sharpen = List(0.f,0.f,0.f,0.f,0.f, 0.f,0.f,-1.f,0.f,0.f, 0.f,-1.f,5.f,-1.f,0.f, 0.f,0.f,-1.f,0.f,0.f, 0.f,0.f,0.f,0.f,0.f)
  def blur = List(0.f,0.f,0.f,0.f,0.f, 0.f,1.f,1.f,1.f,0.f, 0.f,1.f,1.f,1.f,0.f, 0.f,1.f,1.f,1.f,0.f, 0.f,0.f,0.f,0.f,0.f)
  def gaus = List(.25f,.25f,.25f,.25f,-2.f,.25f,.25f,.25f,.25f)

}

object Input extends KeyMouseListener {

  override def keyPressed( e: KeyEvent ) = {
    val keyCode = e.getKeyCode()
    if( keyCode == KeyEvent.VK_ENTER ){
      Main.field.sstep(.01f)
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
    //if( keyCode == KeyEvent.VK_F ) Main.field.alpha += .05f
    //if( keyCode == KeyEvent.VK_V ) Main.field.alpha -= .05f
  }
}
