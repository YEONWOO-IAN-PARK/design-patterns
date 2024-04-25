## 방문자 패턴 (Visitor Pattern)
### 기존 코드를 변경하지 않고 새로운 기능을 추가하는 방법
- 더블 디스패치(Double Dispatch)를 활용 할 수 있다.

- 장점
  - 기존 코드를 변경하지 않고 새로운 코드를 추가할 수 있다.
  - 추가 기능을 한 곳에 모아둘 수 있다.
  
- 단점
  - 복잡하다
  - 새로운 Element를 추가하거나 제거할 때 모든 Visitor 코드를 변경해야 한다.


- 적용 전
  - 구현체를 직접 알고있어야 하고 원하는 기기가 추가될 때마다 구현체 내부의 코드를 추가/변경해야 한다.(OCP 위반)
  - Shape의 구현체는 그 자체의 기능(형태를 나타내는 것) 이외에도 특정 Device에 대한 출력 방식을 관리하는 여러 책임을 지니게된다. (SRP 위반)
  - 강한 결합, 확장성 제한, 코드 중복, 유연성 부족 등의 문제를 가진다.

```java
public class Client {

    public static void main(String[] args) {
        Shape rectangle = new Rectangle();
        Device device = new Phone();
        rectangle.printTo(device);
    }
}
```

```java
public class Rectangle implements Shape {

    @Override
    public void printTo(Device device) {
        if (device instanceof Phone) {
            System.out.println("print Rectangle to phone");
        } else if (device instanceof Watch) {
            System.out.println("print Rectangle to watch");
        }
    }
}
```

```java
public class Phone implements Device{
}
```

```java
public interface Shape {

    void printTo(Device device);

}
```

```java
public interface Device {
}
```




- 적용 후
  - 새로운 Device의 추가, 변경 시에 Shape 구현체를 변경할 필요가 없어 확장성이 증가하였다. (OCP)
  - Shape 인터페이스는 출력에 대한 책임을 지지않고 자신의 기본 기능에 집중한다. 출력은 Device의 구현체에서 담당한다. (SRP)
  - 더블 디스패치의 사용으로 Shape와 Device의 추상 메서드 파라미터를 부모 클래스(인터페이스)로 사용함으로써 유연하고 동적인 처리를 할 수 있게 되었다.
  - OCP와 SRP를 지키고 있지만, (Shape 구현체를 추가하거나 하는 작업에서) OCP나 SRP가 깨질 가능성도 충분히 있다.
  
```java
public class Client {

    public static void main(String[] args) {
        // Element e = new ElementA
        Shape rectangle = new Rectangle();
        // Visitor v = new ConcreteVisitor
        Device device = new Pad();
        // e.accept(v) -> First dispatch 
        rectangle.accept(device);
    }
}
```

```java
// ElementA
public class Rectangle implements Shape {
    
    @Override
    public void accept(Device device) {
        device.print(this);
    }
}
```

```java
// Element
public interface Shape {

    // accept(Visitor)
    void accept(Device device);

}
```

```java
// Concrete Visitor
public class Pad implements Device {
    // visit(ElementA)
    @Override
    public void print(Circle circle) {
        System.out.println("Print Circle to Pad");
    }
    // visit(ElementB)
    @Override
    public void print(Rectangle rectangle) {
        System.out.println("Print Rectangle to Pad");
    }
    // visit(ElementC)
    @Override
    public void print(Triangle triangle) {
        System.out.println("Print Triangle to Pad");
    }
}
```

```java
// Visitor
public interface Device {
    // visit(ElementA)
    void print(Circle circle);
    // visit(ElementB)
    void print(Rectangle rectangle);
    // visit(ElementC)
    void print(Triangle triangle);
}
```

---
### 자바와 스프링에서 찾아보는 방문자 패턴
- 자바
  - FileVisitor, SimpleFileVisitor 
  - AnnotationValueVisitor 
  - ElementVisitor 
- 스프링
  - BeanDefinitionVisitor

