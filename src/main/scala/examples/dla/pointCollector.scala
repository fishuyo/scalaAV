
package com.fishuyo
package examples.dla3d

import maths._
import graphics._
import ray._
import io._

import java.awt.event._
import javax.media.opengl._
import javax.media.opengl.fixedfunc.{GLLightingFunc => L}

import scala.collection.mutable.ListBuffer

object Main extends App {

  GLScene.push( ParticleCollector )

  ParticleCollector.seedLine( Vec3(0,-1,0), Vec3(0,1,0) )
  //ParticleCollector.collection = new Particle :: List()

  val win = new GLRenderWindow
  win.addKeyMouseListener( Input )

}

object Input extends KeyMouseListener {
  override def keyPressed(e:KeyEvent) = {
    val k=e.getKeyCode
    k match {
      case KeyEvent.VK_R => ParticleCollector.rotate = !ParticleCollector.rotate
      case KeyEvent.VK_F => ParticleCollector.thresh += .01f
      case KeyEvent.VK_V => ParticleCollector.thresh -= .01f 
      case KeyEvent.VK_O => ParticleCollector.writeOrientedPoints("points.xyz")
      case KeyEvent.VK_P => ParticleCollector.writePoints2D("points2Dspun.xyz")
      case KeyEvent.VK_M => Main.win.toggleCapture
      case _ => null
    }
  }
}

class Particle( var pos :Vec3=Vec3(0), var vel:Vec3=Vec3(0) ){

  var c: RGB = RGB.green
  
  def step( dt: Float ) = {
    //pos += Particle.randomVec(.01f) //random walk
    pos += vel * dt //linear motion
  }

  def onDraw( gl: GL2 ) = {
    gl.glVertex3f( pos.x, pos.y, pos.z )
    //GLDraw.cube( pos, .01f, false, c )(gl)
  }

}

object Particle {
  val rand = util.Random
  
  def apply() = new Particle( randomVec(2.f), randomVec(4.f) );
  def randomVec(s: Float) : Vec3 = new Vec3( rand.nextFloat * s - s/2, rand.nextFloat * s - s/2, rand.nextFloat * s - s/2)
}

object ParticleCollector extends GLAnimatable {

  var thresh = .01f;
  var rot = 0.f
  var rotate = false;

  var particles = generateParticles(6000)
  var collection = List[Particle]()

  def generateParticles( n: Int): ListBuffer[Particle] = {
    val p = Particle()
    n match {
      case 0 => val l = new ListBuffer[Particle](); l += p
      case _ => generateParticles(n-1) += p
    }
  }

  def seedLine( from:Vec3, to:Vec3, d:Float = .01f ) = {

    val dir = to - from
    var dist = dir.mag
    val r = new Ray( from, dir.normalize )
    while( dist >= 0.f ){
      collection = new Particle(r(dist)) :: collection
      dist -= d
    }

  }


  override def step( dt: Float )= {

    if( rotate ) rot += 4.f
    if( rot > 180.f) rot = -180.f

    particles.foreach( ( p : Particle) => {

      val tmp = p
      p.step(dt)

      collection.find( (q: Particle) => {
        (q.pos - p.pos).mag < thresh
      }) match {
        case Some(u) => {p.vel = Vec3(0); p.c = RGB.blue; collection = collection :+ p; particles -= p; }//particles += Particle() }
        case None => None
      }

      p.step(dt)
      if( p.pos.mag > 1.1f ){
        particles -= p;
        particles += Particle()
      }
    })

  }

  override def onDraw( gl: GL2 ) = {

    gl.glDisable(L.GL_LIGHTING)
    gl.glEnable(GL3.GL_PROGRAM_POINT_SIZE)
    gl.glEnable (GL.GL_BLEND);
    gl.glBlendFunc (GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);

    gl.glRotatef(rot,0.f,1.f,0.f)

    gl.glPointSize( 5.0f )
    gl.glColor4f( 0.f, 1.f, 0.f, .2f)
    gl.glBegin( GL.GL_POINTS )
      particles.foreach( _.onDraw( gl ) )
      gl.glColor4f( 0.f, 0.f, 1.f, .2f)
      collection.foreach( _.onDraw( gl ) )
    gl.glEnd
    
    gl.glDisable(GL3.GL_PROGRAM_POINT_SIZE)
    gl.glEnable(L.GL_LIGHTING)
  }

  def writeOrientedPoints( file: String ) = {
    
    val out = new java.io.FileWriter( file )

    collection.foreach( (p) => {
      val s = .01f
      val x = p.pos.x; val y = p.pos.y; val z = p.pos.z;

      out.write( x + " " + y + " " + (z+s) + " 0 0 1\n" )
      out.write( x + " " + y + " " + (z-s) + " 0 0 -1\n" )
      out.write( x + " " + (y+s) + " " + z + " 0 1 0\n" )
      out.write( x + " " + (y-s) + " " + z + " 0 -1 0\n" )
      out.write( (x+s) + " " + y + " " + z + " 1 0 0\n" )
      out.write( (x-s) + " " + y + " " + z + " -1 0 0\n" )

    })

    out.close
  }
  
  def writePoints2D( file: String) = {
    
    val out = new java.io.FileWriter( file )  
    
    var r=0.f
    var list = List[Particle]()

    while( r < 2 * math.Pi ){
      val tx = math.cos( r ).toFloat
      val tz = math.sin( r ).toFloat
      collection.foreach( (p) => {
        list = new Particle( Vec3( tx*p.pos.x, p.pos.y, tz*p.pos.x ), Vec3(0) ) :: list
      })
      r += .5f;
    }

    
    list.foreach( (p) => {
      val s = .01f
      val x = p.pos.x; val y = p.pos.y; val z = p.pos.z;

      out.write( x + " " + y + " " + (z+s) + " 0 0 1\n" )
      out.write( x + " " + y + " " + (z-s) + " 0 0 -1\n" )
      out.write( x + " " + (y+s) + " " + z + " 0 1 0\n" )
      out.write( x + " " + (y-s) + " " + z + " 0 -1 0\n" )
      out.write( (x+s) + " " + y + " " + z + " 1 0 0\n" )
      out.write( (x-s) + " " + y + " " + z + " -1 0 0\n" )

    })

    out.close
  }

}

