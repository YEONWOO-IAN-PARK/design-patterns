package me.whiteship.designpatterns._03_behavioral_patterns._23_visitor._02_after;

// Element
public interface Shape {

    // accept(Visitor)
    void accept(Device device);

}
