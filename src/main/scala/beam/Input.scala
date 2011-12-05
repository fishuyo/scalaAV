/*
*  Adapted from Input.java by iuiz found in
*  morg.volumeshadow.exampleImplementation
*/
package beam

import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import java.awt.event.MouseMotionListener

import javax.media.opengl.GLAutoDrawable

object Input extends KeyListener with MouseListener with MouseMotionListener {

  var moving = 0
  var looking = 0

	def keyPressed(e: KeyEvent) = {
		val keyCode = e.getKeyCode();
    //println( "keypressed: " + keyCode )

		if (keyCode == KeyEvent.VK_ESCAPE) {
			System.exit(0);
		}
		if (keyCode == KeyEvent.VK_W) {
			Camera.forward
      moving += 1
		}
		if (keyCode == KeyEvent.VK_S) {
			Camera.backward
      moving += 1
		}
		if (keyCode == KeyEvent.VK_A) {
			Camera.left
      moving += 1
		}
		if (keyCode == KeyEvent.VK_D) {
		  Camera.right
      moving += 1
    }
		if (keyCode == KeyEvent.VK_SPACE) {
			Camera.up
      moving += 1
		}
		if (keyCode == KeyEvent.VK_SHIFT) {
      Camera.down
      moving += 1
    }
		if (keyCode == KeyEvent.VK_Q) {
			//this.rotation = !rotation;
		}
		if (keyCode == KeyEvent.VK_UP) { Camera.lookUp; looking+=1 }
		if (keyCode == KeyEvent.VK_DOWN) { Camera.lookDown; looking+=1 }
		if (keyCode == KeyEvent.VK_LEFT) { Camera.lookLeft; looking+=1 }
		if (keyCode == KeyEvent.VK_RIGHT) { Camera.lookRight; looking+=1 }

    if( keyCode == KeyEvent.VK_O ) Convolver ! Write
    if( keyCode == KeyEvent.VK_T) RayTracer.max_depth += 1
    if( keyCode == KeyEvent.VK_G) RayTracer.max_depth -= 1
    if( keyCode == KeyEvent.VK_Y) RayTracer.nrays += 10
    if( keyCode == KeyEvent.VK_H) RayTracer.nrays -= 10

	}

	def keyReleased(e: KeyEvent) = {
		val k = e.getKeyCode();

		if (k == KeyEvent.VK_W || k == KeyEvent.VK_S || k == KeyEvent.VK_A || k == KeyEvent.VK_D || k == KeyEvent.VK_SPACE || k == KeyEvent.VK_SHIFT) {
      Camera.stop
		}
		if (k == KeyEvent.VK_LEFT || k == KeyEvent.VK_RIGHT || k == KeyEvent.VK_UP || k == KeyEvent.VK_DOWN) {
      Camera.stopLook
		}
	}

	def keyTyped(e: KeyEvent) = {
	}

	def mouseReleased(e: MouseEvent) = {
		/*int x = e.getX();
		int y = e.getY();
		this.mouseClickX = x;
		this.mouseClickY = y;

		this.dragging = false;*/
	}

	def mousePressed(e: MouseEvent) = {
		/*int x = mouseEvent.getX();
		int y = mouseEvent.getY();
		this.mouseClickX = x;
		this.mouseClickY = y;*/
	}

	def mouseClicked(e: MouseEvent) = {
	}

	def mouseEntered(e: MouseEvent) = {
	}

  def mouseExited(e: MouseEvent) = {
	}

	def mouseMoved(e: MouseEvent) = {
		/*int x = e.getX();
		int y = e.getY();
		int dx = Math.abs(x - this.mouseClickX);
		int dy = Math.abs(y - this.mouseClickY);

		if (dragging == false) {
		}

		// set to true, so that the camera movement doesn't trigger window events
		this.dragging = true;
		// Calculate mouse movements
		if (x < this.mouseClickX) {
			this.camera.turnLeft(dx);
		} else if (x > this.mouseClickX) {
			this.camera.turnRight(dx);
		}
		if (y < this.mouseClickY) {
			this.camera.turnUp(dy);
		} else if (y > this.mouseClickY) {
			this.camera.turnDown(dy);
		}
		this.mouseClickX = x;

		this.mouseClickY = y;
	*/
  }

	def mouseDragged(e: MouseEvent) = {
	}
}
