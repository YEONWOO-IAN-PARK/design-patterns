package me.whiteship.designpatterns._03_behavioral_patterns._21_strategy._02_after;

// Context에 해당함
public class BlueLightRedLight {

    public void blueLight(Speed speed) {
        speed.blueLight();
    }

    public void redLight(Speed speed) {
        speed.redLight();
    }
}
