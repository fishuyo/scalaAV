
package com.fishuyo
import maths._
import graphics._

import javax.media.opengl._
import javax.media.opengl.fixedfunc.{GLLightingFunc => L}

import scala.collection.mutable.ListBuffer

class Particle( var pos :Vec3, var vel:Vec3 ){

  var c: RGB = RGB.green
  
  def step( dt: Float ) = {
    pos += vel * dt 
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

object ParticleCollector {

  var thresh = .01f;

  var particles = generateParticles(6000)
  var collection = List[Particle]( new Particle( Vec3(0), Vec3(0)) )

  def generateParticles( n: Int): ListBuffer[Particle] = {
    val p = Particle()
    n match {
      case 0 => val l = new ListBuffer[Particle](); l += p
      case _ => generateParticles(n-1) += p
    }
}


  def step( dt: Float )= {

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

  def onDraw( gl: GL2 ) = {

    gl.glDisable(L.GL_LIGHTING)
    gl.glEnable(GL3.GL_PROGRAM_POINT_SIZE)

    gl.glPointSize( 2.0f )
    gl.glColor3f( 0.f, 1.f, 0.f)
    gl.glBegin( GL.GL_POINTS )
      particles.foreach( _.onDraw( gl ) )
      gl.glColor3f( 0.f, 0.f, 1.f)
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

