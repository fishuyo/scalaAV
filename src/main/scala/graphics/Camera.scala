
package com.fishuyo
package graphics
import maths._

import javax.media.opengl._

/**
* Camera
*/
object Camera {
  val v = 2.f
  val a = 90.f
  val rad = scala.math.Pi / 180.f 
  var position = Vec3(0,0,2)
  var velocity = Vec3(0,0,0)
  var direction = Vec3(0,0,0)
  var upDirection = Vec3(0,1,0)

  var elevation=0.0f
  var azimuth=0.0f
  var roll=0.0f
  var w = Vec3( 0,0,0 ) //angluar velocity

  def forward() = velocity = Vec3( math.sin( azimuth * rad),0, -math.cos(azimuth*rad) ).normalize * v
  def backward() = velocity = Vec3( math.sin( (180.f + azimuth) * rad),0, -math.cos((180.f+azimuth)*rad) ).normalize * v
  def left() = velocity = Vec3( math.sin( (270.f + azimuth) * rad),0, -math.cos((270.f+azimuth)*rad) ).normalize * v
  def right() = velocity = Vec3( math.sin( (90.f + azimuth) * rad),0, -math.cos((90.f+azimuth)*rad) ).normalize * v
  def up() = velocity = Vec3(0,v,0)
  def down() = velocity = Vec3(0,-v,0)

  def lookUp = w = Vec3(-a,0,0) 
  def lookDown = w = Vec3(a,0,0)
  def lookLeft = w = Vec3(0,-a,0)
  def lookRight = w = Vec3(0,a,0)

  def stop = velocity = Vec3(0)
  def stopLook = w = Vec3(0)

  def step( dt: Float ) = {
    position += velocity * dt
    val a = w * dt
    elevation += a.x; azimuth += a.y; roll += a.z;
    if( elevation > 180.f ) elevation = -180.f
    if( elevation < -180.f ) elevation = 180.f
    if( azimuth > 180.f ) azimuth = -180.f
    if( azimuth < -180.f ) azimuth = 180.f
  }
}
 
