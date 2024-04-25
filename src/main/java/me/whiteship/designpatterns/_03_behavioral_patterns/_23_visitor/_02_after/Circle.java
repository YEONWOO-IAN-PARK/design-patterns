package me.whiteship.designpatterns._03_behavioral_patterns._23_visitor._02_after;

// ElementA
public class Circle implements Shape {

    // accept(Visitor)
    @Override
    public void accept(Device device) {
        device.print(this);
    }
}
