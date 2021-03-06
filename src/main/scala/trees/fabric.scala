
package com.fishuyo
package trees

import maths._
import graphics._

import javax.media.opengl._
import scala.collection.mutable.ListBuffer

object Fabric extends GLAnimatable {

  var g = -10.f
  var fabrics = List[Fabric]( new Fabric )

  def apply( p: Vec3, w: Float, h: Float, d: Float, m:Int = 0) = new Fabric(p,w,h,d,m)

  override def step( dt: Float ) = fabrics.foreach( _.step(dt) )
  override def onDraw( gl: GL2 ) = fabrics.foreach( _.onDraw(gl) )


}

class Fabric( var pos:Vec3=Vec3(0), var width:Float=1.f, var height:Float=1.f, var dist:Float=.05f, mode:Int=0) extends GLAnimatable {

  var stiff = 1.f
  var particles = ListBuffer[VParticle]()
  var field:VecField3D = null

  val nx = (width / dist).toInt
  val ny = (height / dist).toInt
  for( j <- ( 0 until ny ); i <- ( 0 until nx)){

    var x=0.f; var y=0.f; var p:VParticle = null

    mode match {
      case 0 => x = pos.x - width/2 + i*dist; y = pos.y + height/2 - j*dist; p = VParticle( Vec3(x,y,pos.z), dist )
      case 1 => x = pos.z - width/2 + i*dist; y = pos.y + height/2 - j*dist; p = VParticle( Vec3(pos.x,y,x), dist )
      case 2 => x = pos.x - width/2 + i*dist; y = pos.z + height/2 - j*dist; p = VParticle( Vec3(x,pos.y,y), dist )
      case _ => x = pos.x - width/2 + i*dist; y = pos.y + height/2 - j*dist; p = VParticle( Vec3(x,y,pos.z), dist )
    }

    if( i != 0 ) p.linkTo( particles(particles.length-1) )
    if( j != 0 ) p.linkTo( particles( (j-1) * nx + i) )
    if( mode == 2 && j > ny/2 - 5 && j < ny/2 + 5 && i < nx/2 +5 && i>nx/2-5) p.pinTo( p.pos )
    else if( j == 0 ) p.pinTo( p.pos )
    particles += p
  }
  
  var xt=0.f

  override def step( dt: Float ) = {

    //val ts = .015f
    //val steps = ( (dt+xt) / ts ).toInt
    //xt = dt - steps * ts

    //for( t <- (0 until steps)){
      for( s <- (0 until 3) ) particles.foreach( _.solve() )
      if( field != null ) particles.foreach( (p:VParticle) => { p.applyForce( field(p.pos) ) } )
      particles.foreach( _.step(dt) )
    //}

  }

  override def onDraw( gl: GL2) = particles.foreach( _.onDraw(gl) )
  def applyForce( f: Vec3 ) = particles.foreach( _.applyForce(f) )

  def addField( f: VecField3D ) = field = f

}

object VParticle {
  def apply( p:Vec3, d: Float, s: Float = 1.f ) = new VParticle { pos = p; lPos = p; dist = d; stiff = s; } 
}

class VParticle {

  var pos = Vec3(0)
  var lPos = Vec3(0)
  var accel = Vec3(0)
  var mass = 1.f
  var damp = 20.f
  var dist = 1.f
  var stiff = 1.f
  var thick = 1.f
  var w = .5f
  var tearThresh = 1.f
  var pinned = false
  var pinPos = Vec3(0)

  var links = List[VParticle]()

  def onDraw( gl: GL2 ) = {
    gl.glColor3f(1.f,1.f,1.f)
    gl.glLineWidth( thick )
    gl.glBegin( GL.GL_LINES )
    links.foreach( (n) => { gl.glVertex3f(pos.x, pos.y, pos.z); gl.glVertex3f( n.pos.x, n.pos.y, n.pos.z ) } )
    gl.glEnd
  }

  def step( dt: Float ) = {

    if( !pinned ){
      accel += Vec3( 0, Fabric.g, 0 )

      //verlet integration
      val v = pos - lPos
      accel -= v * ( damp / mass )
      lPos = pos
      pos = pos + v + accel * ( .5f * dt * dt )

      accel.zero
    }
  }

  def applyForce( f: Vec3 ) = accel += (f / mass)

  def solve() : Unit = {
    links.foreach( (n) => {

      //n.solveConstraints()
      val d = pos - n.pos
      val mag = d.mag
      if( mag == 0.f ) return
      val diff = (dist - mag) / mag

      //tear here

      pos = pos + d * w * diff
      n.pos = n.pos - d * n.w * diff
    })

    if( pinned ) pos = lPos
  }

  def linkTo( p: VParticle ) = (links = links :+ p )

  def pinTo( p: Vec3 ) = {
    pos = p
    lPos = p
    pinPos = p
    pinned = true
  }


  def writePoints( file: String ) = {}

}

