
int const potPin = A0;
int potval;
int angle;

void setup() {
  // put your setup code here, to run once:
  Serial.begin(9600);

}

void loop() {
  // put your main code here, to run repeatedly:
  potval = analogRead(potPin);
  angle = map(potval,1023,0,0,179);
  angle = angle -90;
  if(abs(angle) < 9) angle = 0;
  angle = angle * 10 / 90;
  angle = -angle;
  Serial.println(angle);
  delay(100);

}
