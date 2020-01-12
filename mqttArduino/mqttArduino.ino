#include <WiFiNINA.h>
#include <ArduinoHttpClient.h>
#include <PubSubClient.h>
#include <elapsedMillis.h>

/* This code is inspired from this link: https://techtutorialsx.com/2017/04/09/esp8266-connecting-to-mqtt-broker/ */

/* WiFi */
const char* ssid = "OnePlus-7-Pro";
const char* password =  "Walid356w";
/* MQTT */
const char* mqttServer = "tailor.cloudmqtt.com";
const int mqttPort = 12491;
const char* mqttUser = "vclydlgd";
const char* mqttPassword = "TpzXBXjXI7yD";
/* HTTP */
const char serverAddress[] = "walid-ouchtiti.cleverapps.io";  // server address
int port = 80;

/* Button and potentiometer */
#define POTENTIOMETER_PIN A4 // TODO: uncomment this line if the potentiometer is connected to analogic pin 4, or adapt...
#define BUTTON_PIN 2

int potentiometerValue;
int buttonState = 0;   

WiFiClient espClient;
PubSubClient client(espClient);

HttpClient clientHttp = HttpClient(espClient, serverAddress, port);

void setup() {
  
  Serial.begin(9600);
  
  WiFi.begin(ssid, password);
  
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.println("Connecting to WiFi..");
    WiFi.begin(ssid, password);
  }
  Serial.println("Connected to the WiFi network");
  
  client.setServer(mqttServer, mqttPort);
  client.setCallback(callback);
  
  while (!client.connected()) {
    Serial.println("Connecting to MQTT...");
    
    if (client.connect("Walid", mqttUser, mqttPassword )) {
      
      Serial.println("connected");  
      
    } else {
      
      Serial.print("Failed with state: ");
      Serial.print(client.state());
      delay(2000);
      
    }
  }

  /* Subscribe to the topic to control light */
  client.subscribe("hue/control");
  
}

void callback(char* topic, byte* payload, unsigned int length) {
  
  Serial.print("Message arrived in topic: ");
  Serial.println(topic);
  
  Serial.print("Message:");
  String message = "";
  String lightId = "";
  for (int i = 0; i < length; i++) {
    Serial.print((char)payload[i]);
    // To test if there's the word "arduino" in the message
    if (i < 7) {
      message = message + (char)payload[i];
    }
    if (i >= 7) {
      lightId = lightId + (char)payload[i];
    }
  }
  /* Test if we got the message we want */
  if (message == "arduino") {
    elapsedMillis timeElapsed;
    unsigned int interval = 60000;

    Serial.println("You have 60 seconds to control the light");
    
    while (timeElapsed < interval) {
      /* Read the states */
      buttonState = digitalRead(BUTTON_PIN);
      potentiometerValue = analogRead(POTENTIOMETER_PIN);
      
      Serial.print("Potentiometer value: ");
      Serial.print(potentiometerValue);
      
      Serial.print(" ; Button value: ");
      Serial.println(buttonState);

      /* Switch light */
      String contentType = "application/json";
      String pathSwitchLight = "/api/lights/" + lightId + "/switchLightArduino";

      /* If button clicked change state of light */
      if (buttonState == 1) {
        Serial.print("Button pressed");
        String data = "";

        clientHttp.put(pathSwitchLight, contentType, data);

        /* Read the status code and body of the response */
        int statusCode = clientHttp.responseStatusCode();
        String response = clientHttp.responseBody();
        Serial.print("Status code: ");
        Serial.println(statusCode);
        Serial.print("Response: ");
        Serial.println(response);

        delay(1000);
      }

      /* Control light level with potentiometer */
      String pathChangeLevel = "/api/lights/" + lightId + "/changeLevelArduino";

      /* Philips hue level range is from 1 to 245 */
      if (potentiometerValue < 1) {
        potentiometerValue = 1;
      }
      else if (potentiometerValue > 245) {
        potentiometerValue = 245;
      }
      
      String data = "{\"level\":";
      data = data + potentiometerValue + "}";

      clientHttp.put(pathChangeLevel, contentType, data);

      /* read the status code and body of the response */
      int statusCode = clientHttp.responseStatusCode();
      String response = clientHttp.responseBody();
      Serial.print("Status code: ");
      Serial.println(statusCode);
      Serial.print("Response: ");
      Serial.println(response);
      
      delay(1000);
  }
  Serial.println("End of light control time");
}
Serial.println("-----------------------");

}

void loop() {
  client.loop();
}
