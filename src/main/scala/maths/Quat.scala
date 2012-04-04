/* 
* Quaternions!
*   Port of allocore implementation in al_Quat.hpp
*
*/

package com.fishuyo
package maths


object Quat {
  val eps = 0.0000001
  val acc_max = 1.000001
  val acc_min = 0.999999
  def apply( w:Float, x:Float, y:Float, z:Float ) = new Quat(w,x,y,z)
}

class Quat(var w:Float, var x:Float, var y:Float, var z:Float ){
  implicit def toF( d: Double ) = d.toFloat

  def unary_- = Quat( -w, -x, -y, -z ) 
  def +(v: Quat) = Quat( w+v.w, x+v.x, y+v.y, z+v.z )
  def -(v: Quat) = Quat( w-v.w, x-v.x, y-v.y, z-v.z )
  def *(q: Quat) = Quat( w*q.w-x*q.x-y*q.y-z*q.z, w*q.x+x*q.w+y*q.z-z*q.y, w*q.y+y*q.w+z*q.x-x*q.z, w*q.z+z*q.w+x*q.y-y*q.x )
  def *(s: Float ) = Quat(s*w, s*x, s*y, s*z)
  def /(s: Float ) = Quat(w/s, x/s, y/s, z/s)
  
  def +=(v: Quat) = { w+=v.w; x+=v.x; y+=v.y; z+=v.z }
  def -=(v: Quat) = { w-=v.w; x-=v.x; y-=v.y; z-=v.z }
  def *=(s: Float) = { w*=s; x*=s; y*=s; z*=s }
  

  def dot(v: Quat) : Float = w*v.w + x*v.x + y*v.y + z*v.z
  //def cross( v: Quat) = Quat( y*v.z - z*v.y, z*v.x - x*v.z, x*v.y - y*v.x )
  def magSq() = this dot this
  def mag = math.sqrt( magSq() )
  def normalize = {
    val m = magSq()
    if( m*m < Quat.eps ) identity()
    else if( m > Quat.acc_max || m < Quat.acc_min ) this * (1.0f / math.sqrt( m ))
    else this
  }

  def conj = Quat( w, -x,-y,-z )
  def sgn = Quat(w,x,y,z).normalize
  def inverse = sgn conj
  def recip = conj / magSq   

  def zero() = {w=0;x=0;y=0;z=0;this}
  def identity() = {w=1;x=0;y=0;z=0;this}

  def fromAxisX( ang: Float ) = {w=math.cos(ang*.5f);x=math.sin(ang*.5f);y=0;z=0}
  def fromAxisY( ang: Float ) = {w=math.cos(ang*.5f);x=0;y=math.sin(ang*.5f);z=0}
  def fromAxisZ( ang: Float ) = {w=math.cos(ang*.5f);x=0;y=0;z=math.sin(ang*.5f)}
  def fromAxisAngle( ang: Float, axis:Vec3 ) = { 
    val sin2a = math.sin(ang*.5f).toFloat
    w = math.cos(ang*.5f).toFloat
    x = axis.x * sin2a
    y = axis.y * sin2a
    z = axis.z * sin2a
  }
  def fromEuler( eu:Vec3 ) = { //eu = Vec3( az, el, ba )
    val c1 = math.cos(eu.x*.5f).toFloat; val c2 = math.cos(eu.y*.5f).toFloat; val c3 = math.cos(eu.z*.5f).toFloat   
    val s1 = math.sin(eu.x*.5f).toFloat; val s2 = math.sin(eu.y*.5f).toFloat; val s3 = math.sin(eu.z*.5f).toFloat   
    val tw = c1*c2; val tx = c1*s2; val ty = s1*c2; val tz = -s1*s2
    w = tw*c3 - tz*s3; x = tx*c3 + ty*s3; y = ty*c3 - tx*s3; z = tw*s3 + tz*c3
  }
  
  override def toString() = "[" + w + " " + x + " " + y + " " + z + "]"
}


