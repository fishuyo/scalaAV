
package com.fishuyo
package graphics

import javax.media.opengl._
import com.jogamp.common.nio.Buffers
import java.nio.FloatBuffer

object Field2D {
  def apply(width:Int, height:Int ) = new Field2D { w=width; h=height; data = Buffers.newDirectFloatBuffer( w*h ); }
}

class Field2D extends GLAnimatable {

  var data:FloatBuffer = _
  var w:Int = _
  var h:Int = _

  def apply( x:Int, y:Int ) = data.get( w*y + x)
  def getToroidal( i:Int, j:Int ) = {
    var x = i; var y = j;
    while( x < 0 ) x += w; while( x >= w ) x -= w;
    while( y < 0 ) y += h; while( y >= h ) y -= h;
    data.get( w*y + x );
  }

  def allocate( x:Int, y:Int ) = { w=x; h=y; data = Buffers.newDirectFloatBuffer( w*h ); }
  def set( x:Int, y:Int, v:Float) = data.put( w*y + x, v)
  def set( a: Array[Float] ) = {
    //should check array size matches field size
    data.put( a )
    data.rewind
  }

  override def onDraw( gl: GL2 ) = {
    import GL._; import gl._
    
    updateTexture( GLContext.getCurrent.getGL.getGL2 );
    
    glEnable(GL_TEXTURE_2D);
    glBegin(GL2.GL_QUADS);
    glTexCoord2f(0.0f, 0.0f); glVertex3f(-1.0f, -1.0f, 0.0f);
    glTexCoord2f(0.0f, 1.0f); glVertex3f(-1.0f, 1.0f,  0.0f);
    glTexCoord2f(1.0f, 1.0f); glVertex3f(1.0f, 1.0f, 0.0f);
    glTexCoord2f(1.0f, 0.0f); glVertex3f(1.0f, -1.0f, 0.0f);
    glEnd();
    glDisable(GL_TEXTURE_2D); 
  
  }
  //override def step( dt: Float ) = {}

  def updateTexture( gl: GL2 ) = {
    import GL._; import gl._

    glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
    glTexImage2D(GL_TEXTURE_2D, 0, 3, w, h, 0, GL_LUMINANCE, GL_FLOAT, data);

    glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL2.GL_CLAMP);
    glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL2.GL_CLAMP);
    glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
    glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
    glTexEnvf(GL2ES1.GL_TEXTURE_ENV, GL2ES1.GL_TEXTURE_ENV_MODE, GL2ES1.GL_DECAL);
    glEnable(GL_TEXTURE_2D);

  }



}
