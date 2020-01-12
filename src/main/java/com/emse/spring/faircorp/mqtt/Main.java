package com.emse.spring.faircorp.mqtt;

public class Main {
    public static void main(String[] args) {
        Mqtt mqtt = new Mqtt();
        mqtt.subscribe();
    }
}
