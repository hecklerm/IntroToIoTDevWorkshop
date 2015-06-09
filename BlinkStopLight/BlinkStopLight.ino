/*
  BlinkStopLight
  Creates a life-like stoplight with green, yellow, & red LEDs.
 
  Author: ??? I can't remember writing this originally, but I've found no 
          record of it online (it clearly sprang forth from the original 
          Blink sketch) and I've since updated it. I claim no 
          ownership, please use & reuse if you would like, however 
          you would like. :)
*/

const int greenled = 13;
const int yellowled = 11;
const int redled = 9;

void setup() {                
  // Initialize digital pins for output.
  pinMode(greenled, OUTPUT);     
  pinMode(yellowled, OUTPUT);     
  pinMode(redled, OUTPUT);     
}

// the loop routine runs over and over again forever:
void loop() {
  digitalWrite(redled, LOW);     // Turn LEDs off (LOW voltage level)
  digitalWrite(yellowled, LOW);
  digitalWrite(greenled, HIGH);  // Turn LED on (HIGH voltage level)
  delay(1000);                   // Time in ms; green lights are FAST!

  digitalWrite(redled, LOW);
  digitalWrite(yellowled, HIGH); // Yellow/amber light
  digitalWrite(greenled, LOW);
  delay(2000);

  digitalWrite(redled, HIGH);
  digitalWrite(yellowled, LOW);
  digitalWrite(greenled, LOW);
  delay(5000);                   // Just waiting here at the red light...
}

