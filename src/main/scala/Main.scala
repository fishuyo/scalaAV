
package com.fishuyo
import graphics._

object Main extends App{
  
  //build scene by pushing objects to singleton GLScene (GLRenderWindow renders it by default)
  GLScene.push( trees.Trees );
  GLScene.push( trees.Fabric );
  val window = new graphics.GLRenderWindow

}

