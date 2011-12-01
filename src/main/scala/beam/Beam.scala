
package beam

import scalacl._

class Ray( val o: Vec3, val d: Vec3 ){
 def apply( t: Float ) : Vec3 = o + d*t
 override def toString() = o + " -> " + d
}

class Beam( val o: Vec3, val d: (Vec3,Vec3,Vec3,Vec3) ){
  
  implicit val context = Context.best

  val x = CLArray( d._1.x, d._2.x, d._3.x, d._4.x )
  val y = CLArray( d._1.y, d._2.y, d._3.y, d._4.y )
  val z = CLArray( d._1.z, d._2.z, d._3.z, d._4.z )



}
