/*
 * Project: JJack - Java bridge API for the JACK Audio Connection Kit
 * Class:   de.gulden.application.jjack.clients.Gain
 * Version: 0.3
 *
 * Date:    2007-12-30
 *
 * Licensed under the GNU Lesser General Public License (LGPL).
 * This comes with NO WARRANTY. See file LICENSE for details.
 *
 * Author:  Jens Gulden
 */

package beam;

import de.gulden.framework.jjack.*;
//import de.gulden.util.swing.SliderLabeled;
import java.nio.FloatBuffer;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.io.Serializable;

/**
 * JJack example client: Change the audio volume.
 *  
 * @author  Jens Gulden
 * @version  0.3
 */
public class Gain extends JJackClient implements Serializable, ChangeListener {

    // ------------------------------------------------------------------------
    // --- final static field                                               ---
    // ------------------------------------------------------------------------

    private static final double POW = Math.log(10) / Math.log(2);


    // ------------------------------------------------------------------------
    // --- field                                                            ---
    // ------------------------------------------------------------------------

    protected float volume = 1.0f;


    // ------------------------------------------------------------------------
    // --- constructor                                                      ---
    // ------------------------------------------------------------------------

    public Gain() {
        super(true);
    }


    // ------------------------------------------------------------------------
    // --- methods                                                          ---
    // ------------------------------------------------------------------------

    public float getVolume() {
        return volume;
    }

    public void setVolume(float volume) {
        if (volume != this.volume) {
            this.volume = volume;
            if (gui != null) {
        gui.validate();
            }
        }
    }

    /**
     * Process multiple samples from input buffer to output buffer.
     * This is regularly called by the JACK daemon.
     *  
     * @param e event object with references to input buffer and output buffer.
     * @see  de.gulden.framework.jjack.JJackAudioProcessor#process(de.gulden.framework.jjack.JJackAudioProcessEvent)
     */
    public void process(JJackAudioEvent e) {
        float v = getVolume();
        for (int i=0; i<e.countChannels(); i++) {
        	FloatBuffer in = e.getInput(i);
        	FloatBuffer out = e.getOutput(i);
        	int cap = in.capacity();
        	for (int j=0; j<cap; j++) {
        		float a = in.get(j);
        		a *= v;
        		if (a>1.0) {
        			a = 1.0f;
        		} else if (a<-1.0) {
        			a = -1.0f;
        		}
        		out.put(j, a);
        	}
        }
    }

    /**
     *  
     * @see  javax.swing.event.ChangeListener#stateChanged(javax.swing.event.ChangeEvent)
     */
    public void stateChanged(ChangeEvent e) {
        setVolume(f(((JSlider)gui).getValue()));
    }

    /**
     * Creates the user interface.
     *  
     * @return  visible component with the user interface, or <code>null</code> if this is an invisible audio processor
     */
    /*protected JComponent createUI() {
        SliderLabeled slider = new SliderLabeled() {
        	/**
        	 * Propagate value from controller-class to slider.
        	 
        	public void validate() {
        		super.validate();
        		float v = getVolume();
        		setValue(f_(v));
        		updateUI();
        	}
        	/**
        	 * Propagate value from slider to label.
        	 
        	 public void updateUI() {
        	 	JSlider slider = getSlider();
        	 	JLabel label = getLabel();
        	 	if ((slider != null) && (label != null)) {
        			float v = f(getValue());
        			label.setText(String.valueOf((int)(v*100))+"%");
        	 	}
        	 }
        };
        slider.setMajorTickSpacing(100);
        slider.setMaximum(200);
        slider.setMinorTickSpacing(10);
        slider.setOrientation(javax.swing.JSlider.VERTICAL);
        slider.setPaintTicks(true);
        slider.setValue(100);
        slider.updateUI();
        slider.addChangeListener(this);
        return slider;
    }*/


    // ------------------------------------------------------------------------
    // --- static methods                                                   ---
    // ------------------------------------------------------------------------

    private static float f(int x) {
        return (float)Math.pow(((double)x)/100.0,POW);
    }

    private static int f_(float y) {
        return (int)(Math.pow(y,(1.0/POW))*100.0);
    }

} // end Gain

