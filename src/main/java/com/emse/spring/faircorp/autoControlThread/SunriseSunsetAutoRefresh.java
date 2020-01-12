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
import java.util.concurrent.TimeUnit;

public class SunriseSunsetAutoRefresh extends Thread {
    public SunriseSunsetAutoRefresh() {

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

    public String getSunriseAndSunsetTimes(String latitude, String longitude) {
        try {
            URL urlForGetRequest = new URL("https://api.sunrise-sunset.org/json?lat=" + latitude + "&lng=" + longitude + "&date=today");
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

    public void updateSunriseSunsetTimes(Long lightControllerId, String sunriseTime, String sunsetTime) {
        try {
            /* Rest Api */
            URL url = new URL("https://walid-ouchtiti.cleverapps.io/autoLightControllers/" + lightControllerId + "/sunset-sunrise");
            HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
            httpCon.setRequestProperty("Content-Type", "application/json; charset=utf8");
            httpCon.setDoOutput(true);
            httpCon.setRequestMethod("PUT");
            OutputStreamWriter out = new OutputStreamWriter(
                    httpCon.getOutputStream());
            out.write("{\"sunriseTime\": \"" + sunriseTime + "\", \"sunsetTime\": \"" + sunsetTime + "\"}");
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
            /* Refresh every 24 hours */
            try {
                TimeUnit.HOURS.sleep(24);
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
                    autoLight.setId((Long) lightController.get("id"));
                    autoLight.setSunriseTime(lightController.get("sunriseTime").toString());
                    autoLight.setSunsetTime(lightController.get("sunsetTime").toString());
                    autoLight.setLatitude(lightController.get("latitude").toString());
                    autoLight.setLongitude(lightController.get("longitude").toString());

                    /* Get new sunrise and sunset times */
                    String sunriseSunset = this.getSunriseAndSunsetTimes(autoLight.getLatitude(), autoLight.getLongitude());
                    JSONObject object = (JSONObject) parser.parse(sunriseSunset);
                    JSONObject info = (JSONObject) object.get("results");


                    /* Change sunset and sunrise times */
                    this.updateSunriseSunsetTimes(autoLight.getId(), info.get("sunrise").toString(), info.get("sunset").toString());

                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }
}