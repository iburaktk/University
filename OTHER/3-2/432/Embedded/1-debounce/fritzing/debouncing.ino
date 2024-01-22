int inPin = 7;         	  // the number of the input pin
int outPin = 13;       	  // the number of the output pin

int counter = 0;       	  // how many times we have seen new value
int reading;           	  // the current value read from the input pin
int current_state = LOW;  // the debounced input value
int debounce_count = 10;  // number of millis/samples to consider before declaring a debounced input

void setup()
{
  pinMode(inPin, INPUT);
  pinMode(outPin, OUTPUT);
  digitalWrite(outPin, current_state); // setup the Output LED for initial state
}


void loop()
{
    reading = digitalRead(inPin);

    if(reading == current_state && counter > 0)
    {
      counter--;
    }
    if(reading != current_state)
    {
       counter++; 
    }
    if(counter >= debounce_count)
    {
      counter = 0;
      current_state = reading;
      digitalWrite(outPin, current_state);
    }
    delay(1)
}
