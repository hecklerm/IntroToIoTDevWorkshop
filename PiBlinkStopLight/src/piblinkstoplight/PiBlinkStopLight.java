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
package piblinkstoplight;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.wiringpi.Gpio;

/**
 * PiBlinkStopLight replicates the functionality of Arduino sketch I created
 * ("BlinkStopLight") for the Raspberry Pi using Pi4j.
 * 
 * @author Mark Heckler (mark.heckler@gmail.com, @mkheck)
 */
public class PiBlinkStopLight {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        GpioController gpio = GpioFactory.getInstance();
        GpioPinDigitalOutput ledGreen = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_13);
        GpioPinDigitalOutput ledYellow = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_11);
        GpioPinDigitalOutput ledRed = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_09);

        // Ensure we start with the LEDs off
        ledGreen.low();
        ledYellow.low();
        ledRed.low();
        
        for (int i = 0; i < 5; i++) {
            ledRed.low();
            ledYellow.low();
            ledGreen.high();
            Gpio.delay(1000);

            ledRed.low();
            ledYellow.high();
            ledGreen.low();
            Gpio.delay(2000);

            ledRed.high();
            ledYellow.low();
            ledGreen.low();
            Gpio.delay(5000);
        }
        
        ledRed.low();
        ledYellow.low();
        ledGreen.low();
        gpio.shutdown();
    }    
}
