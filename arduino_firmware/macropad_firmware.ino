
const int columnPins[] = {4, 5, 6, 7};
const int NUM_COLUMNS = sizeof(columnPins)/sizeof(columnPins[0]);

const int rowPins[] = {13, A0, A1};
const int NUM_ROWS = sizeof(rowPins)/sizeof(rowPins[0]);

const int ledPins[] = {9, 10, 11};
int currentColor[] = {0, 0, 0};

String incomingMsg = "";


const int encoderAPins[] = {3, A2};
volatile int aTurn;
unsigned long waitTimeA;
unsigned long lastTimeA;

const int encoderBPins[] = {2, 12};
volatile int bTurn;
unsigned long waitTimeB;
unsigned long lastTimeB;


void setup() {

  Serial.begin(9600);
  
  for (int i = 0; i < NUM_COLUMNS; i++) {
    pinMode(columnPins[i], INPUT_PULLUP);
  }


  for (int i = 0; i < NUM_ROWS; i++) {
    pinMode(rowPins[i], OUTPUT);
    digitalWrite(rowPins[i], HIGH);
  }


  for (int i = 0; i < 3; i++) {
    pinMode(ledPins[i], OUTPUT);
    digitalWrite(ledPins[i], LOW);
  }


  for (int i = 0; i < 2; i++) {
    pinMode(encoderAPins[i], INPUT_PULLUP);
  }
  attachInterrupt(digitalPinToInterrupt(encoderAPins[0]), manageEncoderA, FALLING);
  aTurn = 0;
  waitTimeA = 50;
  lastTimeA = 0;
  

  for (int i = 0; i < 2; i++) {
    pinMode(encoderBPins[i], INPUT_PULLUP);
  }
  attachInterrupt(digitalPinToInterrupt(encoderBPins[0]), manageEncoderB, FALLING);
  bTurn = 0;
  waitTimeB = 50;
  lastTimeB = 0;



}

void loop() {
  for (int i = 0; i < NUM_ROWS; i++) {
    digitalWrite(rowPins[i], LOW);

    for (int j = 0; j < NUM_COLUMNS; j++) {
      if (digitalRead(columnPins[j]) == LOW) {
        unsigned int initTime = millis();
        unsigned int duration = 0;
        boolean longPress = false;

        while (digitalRead(columnPins[j]) == LOW) {
          duration = millis() - initTime;
          if (duration > 600)
            longPress = true;

          if (longPress) {
            //Hacer que las luces parpadéen para indicar 'longpress'
            if((duration - 600) % 400 < 200) {
              setColor(0, 0, 0);
            }
            else {
              setColor(currentColor[0], currentColor[1], currentColor[2]);
            }
          }

        }
        setColor(currentColor[0], currentColor[1], currentColor[2]);

        duration = millis() - initTime;
        if (duration > 50) {  //Debouncing time
          byte msg;
          if (!longPress) {
            msg = 2*(i*NUM_COLUMNS + j);
            /*Serial.print("Comando ");
            Serial.println(2*(i*NUM_COLUMNS + j));*/
          }
          else {
            msg = 2*(i*NUM_COLUMNS + j) + 1;
            /*Serial.print("Comando ");
            Serial.println(2*(i*NUM_COLUMNS + j) + 1);*/
          }
          Serial.write(msg);
        }

      }
    }
    digitalWrite(rowPins[i], HIGH);
  }

  if (aTurn > 0) {
    Serial.write(2*(NUM_ROWS*NUM_COLUMNS));
    lastTimeA = millis();
    aTurn = 0;
  }
  else if (aTurn < 0) {
    Serial.write(2*(NUM_ROWS*NUM_COLUMNS) + 1);
    lastTimeA = millis();
    aTurn = 0;
  }
  waitTimeA = millis() - lastTimeA;


  if (bTurn > 0) {
    Serial.write(2*(NUM_ROWS*NUM_COLUMNS + 1));
    lastTimeB = millis();
    bTurn = 0;
  }
  else if (bTurn < 0) {
    Serial.write(2*(NUM_ROWS*NUM_COLUMNS + 1) + 1);
    lastTimeB = millis();
    bTurn = 0;
  }
  waitTimeB = millis() - lastTimeB;


  if (Serial.available() > 0) {
    byte incomingByte = Serial.read();

    if (incomingByte == '\n') {
      setColor (incomingMsg);
      incomingMsg = "";
    }
    else {
      incomingMsg += (char) incomingByte;
    }
  }
  //delay (50);

}

void setColor (byte r, byte g, byte b) {

  analogWrite(ledPins[0], r);
  analogWrite(ledPins[1], g);
  analogWrite(ledPins[2], b);
}

void setColor (String msg) {
  char c;
  String buffer = "";
  byte color[3] = {0, 0, 0};
  int led = 0;


  for (int i = 0; i < msg.length(); i++) {
    char currentChar = msg.charAt(i);
    if (currentChar == ',') {
      color[led] = buffer.toInt();
      led++;
      buffer = "";
      if (led >= 3)
        break;
    }
    else {
      buffer += currentChar;
    }
  }
  if (buffer.length() > 0 && led < 3) {
    color[led] = buffer.toInt();
  }

  setColor(color[0], color[1], color[2]);
  currentColor[0] = color[0];
  currentColor[1] = color[1];
  currentColor[2] = color[2];

}

void manageEncoderA () {
  //boolean aValue = digitalRead(encoderAPins[0]);
  boolean bValue = digitalRead(encoderAPins[1]);

  if (waitTimeA < 100) return;

  if (bValue == LOW) {
    aTurn += 1;
  }
  else {
    aTurn -= 1;
  }

}

void manageEncoderB () {
  //boolean aValue = digitalRead(encoderAPins[0]);
    boolean bValue = digitalRead(encoderBPins[1]);

  if (waitTimeB < 100) return;

  if (bValue == LOW) {
    bTurn += 1;
  }
  else {
    bTurn -= 1;
  }

  
}

