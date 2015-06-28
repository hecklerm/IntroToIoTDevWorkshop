/*
 * The MIT License
 *
 * Copyright 2015 Mark A. Heckler
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package pihcsr04basictest;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.wiringpi.Gpio;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * PiHCSR04BasicTest replicates the functionality of Arduino sketch I created
 * ("HCSR04BasicTest") for the Raspberry Pi using Pi4j.
 *
 * @author Mark Heckler (mark.heckler@gmail.com, @mkheck)
 */
public class PiHCSR04BasicTest {
    private static final int TIMEOUT = 500;
    
    GpioController gpio = GpioFactory.getInstance();
    GpioPinDigitalOutput triggerPin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_08);
    GpioPinDigitalInput echoPin = gpio.provisionDigitalInputPin(RaspiPin.GPIO_10);

    public String getDistanceReading() throws Exception {
        long duration, inches, cm;
        
        fireTrigger();
        if (isEcho()) {
            duration = timeEcho();
            
            // Convert the time into a distance
            inches = microsecondsToInches(duration);
            cm = microsecondsToCentimeters(duration);
            return inches + "in, " + cm + "cm";
        } else {
            // Nothing to convert; echo failed to materialize (known issue)
            return "DANGER WILL ROBINSON! TOO CLOSE!";
        }
    }

    private void fireTrigger() {
        // The ping is triggered by a HIGH pulse of 2 or more microseconds.
        // Send a short LOW pulse beforehand to ensure a clean HIGH pulse.
        triggerPin.setState(PinState.LOW);
        Gpio.delayMicroseconds(3);

        triggerPin.setState(PinState.HIGH);
        Gpio.delayMicroseconds(10);
        
        triggerPin.setState(PinState.LOW);
    }

    private boolean isEcho() {
        int countdown = TIMEOUT;

        while(echoPin.isLow() && countdown > 0) {
            countdown--;
        }
        
        return (countdown <= 0 ? false : true);
    }

    private long timeEcho() throws Exception {
        int countdown = TIMEOUT;
        long start = System.nanoTime();
        while(echoPin.isHigh() && countdown > 0) {
            countdown--;
        }
        long end = System.nanoTime();
        
        if(countdown == 0) {
            throw new Exception( "Timeout waiting for echo reading." );
        }
        
        // Return value in microseconds
        //return (long) Math.ceil((end - start)/ 1000.0);
        return (end - start)/ 1000;
    }

    long microsecondsToInches(long microseconds) {
        /*
         According to Parallax's datasheet, there are 73.746 microseconds per inch,
         i.e. sound travels at 1130 feet/second. This gives the distance travelled 
         by the ping, outbound and return, so we divide by 2 to get the distance 
         of the sensed object.
         */
        //return (long) ((double) microseconds / 73.746 / 2);
        return (long) (microseconds/73.746/2);
    }

    long microsecondsToCentimeters(long microseconds) {
        /*
         The speed of sound is 340 m/s or 29 microseconds per centimeter.
         The ping travels out and back, so to find the distance of the
         object, we take half of the distance travelled.
         */
        //return (long) ((double) microseconds / 29d / 2);
        return microseconds/29/2;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        PiHCSR04BasicTest disTest = new PiHCSR04BasicTest();
        for (int i=0; i<200; i++) {
            try {
                System.out.println(disTest.getDistanceReading());
            } catch (Exception ex) {
                Logger.getLogger(PiHCSR04BasicTest.class.getName()).log(Level.SEVERE, null, ex);
            }
            Gpio.delay(100);
        }        
    }

}
