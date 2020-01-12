package com.emse.spring.faircorp.autoControlThread;

import com.emse.spring.faircorp.DTO.AutoControllerDto;
import com.emse.spring.faircorp.model.Status;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AutoLightThread extends Thread {
    public AutoLightThread() {

    }

    public String getAutoLightControllers() {
        try {
            URL urlForGetRequest = new URL("https://walid-ouchtiti.cleverapps.io/api/autoLightControllers");
            String readLine = null;
            HttpURLConnection conection = (HttpURLConnection) urlForGetRequest.openConnection();
            conection.setRequestMethod("GET");
            int responseCode = conection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(conection.getInputStream()));
                StringBuffer response = new StringBuffer();
                while ((readLine = in.readLine()) != null) {
                    response.append(readLine);
                }
                in.close();

                /* Return string result */
                return response.toString();
            }
            else {
                System.out.println("HTTP GET Error");
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getRoomLights(Long roomId) {
        try {
            URL urlForGetRequest = new URL("https://walid-ouchtiti.cleverapps.io/api/rooms/" + roomId);
            String readLine = null;
            HttpURLConnection conection = (HttpURLConnection) urlForGetRequest.openConnection();
            conection.setRequestMethod("GET");
            int responseCode = conection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(conection.getInputStream()));
                StringBuffer response = new StringBuffer();
                while ((readLine = in.readLine()) != null) {
                    response.append(readLine);
                }
                in.close();

                /* Return string result */
                return response.toString();
            }
            else {
                System.out.println("HTTP GET Error");
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void turnOffLight(Long lightId) {
        try {
            /* Rest Api */
            URL url = new URL("https://walid-ouchtiti.cleverapps.io/api/lights/" + lightId + "/switchOff");
            HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
            httpCon.setDoOutput(true);
            httpCon.setRequestMethod("PUT");
            OutputStreamWriter out = new OutputStreamWriter(
                    httpCon.getOutputStream());
            out.write("");
            out.close();
            httpCon.getInputStream();

            /* Philips hue */
            URL url1 = new URL("https://walid-ouchtiti.cleverapps.io/api/lights/" + lightId + "/lightOffMqtt");
            HttpURLConnection httpCon1 = (HttpURLConnection) url1.openConnection();
            httpCon1.setRequestProperty("Content-Type", "application/json; charset=utf8");
            httpCon1.setDoOutput(true);
            httpCon1.setRequestMethod("PUT");
            OutputStreamWriter out1 = new OutputStreamWriter(
                    httpCon1.getOutputStream());
            out1.write("{\"on\": false}");
            out1.close();
            httpCon1.getInputStream();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void turnOnLight(Long lightId) {
        try {
            URL url = new URL("https://walid-ouchtiti.cleverapps.io/api/lights/" + lightId + "/switchOn");
            HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
            httpCon.setDoOutput(true);
            httpCon.setRequestMethod("PUT");
            OutputStreamWriter out = new OutputStreamWriter(
                    httpCon.getOutputStream());
            out.write("");
            out.close();
            httpCon.getInputStream();

            /* Philips hue */
            URL url1 = new URL("https://walid-ouchtiti.cleverapps.io/api/lights/" + lightId + "/lightOnMqtt");
            HttpURLConnection httpCon1 = (HttpURLConnection) url1.openConnection();
            httpCon1.setRequestProperty("Content-Type", "application/json");
            httpCon1.setDoOutput(true);
            httpCon1.setRequestMethod("PUT");
            OutputStreamWriter out1 = new OutputStreamWriter(
                    httpCon1.getOutputStream());
            out1.write("");
            out1.close();
            httpCon1.getInputStream();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while(true) {
            try {
                Thread.sleep(60000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            try {
                /* Get all auto light controllers */
                String autoLights = this.getAutoLightControllers();

                JSONParser parser = new JSONParser();
                JSONArray obj = (JSONArray) parser.parse(autoLights.toString());

                for (int i = 0; i < obj.size(); i++) {
                    /* Retrieve the auto light controller and create object */
                    JSONObject lightController = (JSONObject) obj.get(i);
                    AutoControllerDto autoLight = new AutoControllerDto();
                    autoLight.setRoomId((Long) lightController.get("roomId"));
                    if (lightController.get("autoLightControlState") == "ON") {
                        autoLight.setAutoLightControlState(Status.ON);
                    }
                    else {
                        autoLight.setAutoLightControlState(Status.OFF);
                    }
                    if (lightController.get("autoThermostatControlState") == "ON") {
                        autoLight.setAutoThermostatControlState(Status.ON);
                    }
                    else {
                        autoLight.setAutoThermostatControlState(Status.OFF);
                    }
                    autoLight.setId((Long) lightController.get("id"));
                    autoLight.setSunriseTime(lightController.get("sunriseTime").toString());
                    autoLight.setSunsetTime(lightController.get("sunsetTime").toString());
                    autoLight.setLatitude(lightController.get("latitude").toString());
                    autoLight.setLongitude(lightController.get("longitude").toString());
                    autoLight.setMinTemperature(lightController.get("minTemperature").toString());

                    /* Get current time */
                    DateFormat dateFormat = new SimpleDateFormat("hh:mm:ss a");
                    Date date = new Date();
                    String currentDate = dateFormat.format(date).toString();

                    /* Compare current time to given time */
                    try {
                        /* Get the rooms lights */
                        String lights = this.getRoomLights(autoLight.getRoomId());
                        JSONObject jsonObject = (JSONObject) parser.parse(lights);
                        JSONArray allRoomLights = (JSONArray) jsonObject.get("lightsIds");

                        /* If it's day time (lights off) */
                        if ((dateFormat.parse(currentDate).after(dateFormat.parse(autoLight.getSunriseTime()))) &&
                                (dateFormat.parse(currentDate).before(dateFormat.parse(autoLight.getSunsetTime())))) {
                            /* Turn off the lights of the room */
                            for (int j = 0; j < allRoomLights.size(); j++) {
                                this.turnOffLight((Long) allRoomLights.get(j));
                            }
                        }
                        /* If it's night time (lights on) */
                        else {
                            /* Turn on the lights of the room */
                            for (int j = 0; j < allRoomLights.size(); j++) {
                                this.turnOnLight((Long) allRoomLights.get(j));
                            }
                        }

                    } catch (java.text.ParseException e) {
                        e.printStackTrace();
                    }

                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }
}