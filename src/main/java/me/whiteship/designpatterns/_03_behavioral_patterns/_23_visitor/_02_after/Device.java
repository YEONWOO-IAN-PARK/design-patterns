package me.whiteship.designpatterns._03_behavioral_patterns._23_visitor._02_after;

// Visitor
public interface Device {
    // visit(ElementA)
    void print(Circle circle);
    // visit(ElementB)
    void print(Rectangle rectangle);
    // visit(ElementC)
    void print(Triangle triangle);
}
