
package ray

class Ray( var o: Vec3, var d: Vec3 ){
 def apply( t: Float ) : Vec3 = o + d*t
 override def toString() = o + " -> " + d
}
