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
import java.util.Random;

public class AutoThermostatThread extends Thread {
    public AutoThermostatThread() {

    }

    public String getAutoThermostatControllers() {
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

    public String getRoomThermostat(Long roomId) {
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

    public void turnOnThermostat(Long thermostatId) {
        try {
            URL url = new URL("https://walid-ouchtiti.cleverapps.io/api/thermostats/" + thermostatId + "/switchOn");
            HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
            httpCon.setDoOutput(true);
            httpCon.setRequestMethod("PUT");
            OutputStreamWriter out = new OutputStreamWriter(
                    httpCon.getOutputStream());
            out.write("");
            out.close();
            httpCon.getInputStream();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void turnOffThermostat(Long thermostatId) {
        try {
            URL url = new URL("https://walid-ouchtiti.cleverapps.io/api/thermostats/" + thermostatId + "/switchOff");
            HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
            httpCon.setDoOutput(true);
            httpCon.setRequestMethod("PUT");
            OutputStreamWriter out = new OutputStreamWriter(
                    httpCon.getOutputStream());
            out.write("");
            out.close();
            httpCon.getInputStream();

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
                /* Get all auto thermostat controllers */
                String autoLights = this.getAutoThermostatControllers();

                JSONParser parser = new JSONParser();
                JSONArray obj = (JSONArray) parser.parse(autoLights.toString());

                for (int i = 0; i < obj.size(); i++) {
                    /* Retrieve the auto thermostat controller and create object */
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

                    /* Get the current temperature from the latitude and longitude */
                    /* Generate random temperature */
                    Random random = new Random();
                    int randomTmp =  random.nextInt(30 - (-10) + 1) + (-10);
                    int minTmp = Integer.parseInt(autoLight.getMinTemperature());

                    /* Get the thermostat auto controller */
                    String lights = this.getRoomThermostat(autoLight.getRoomId());
                    JSONObject jsonObject = (JSONObject) parser.parse(lights);
                    String thermostat = jsonObject.get("thermostatId").toString();

                    if (randomTmp < minTmp) {
                        /* Turn on thermostat */
                        System.out.println("Switching on thermostat");
                        System.out.println("Outside temperature: " + randomTmp);
                        System.out.println("User minimum temperature: " + minTmp);
                        this.turnOnThermostat(Long.valueOf(thermostat));
                    }
                    else {
                        /* Turn off thermostat */
                        System.out.println("Switching off thermostat");
                        System.out.println("Outside temperature: " + randomTmp);
                        System.out.println("User minimum temperature: " + minTmp);
                        this.turnOffThermostat(Long.valueOf(thermostat));
                    }

                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }
}