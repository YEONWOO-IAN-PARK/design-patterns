## 팩토리 메서드 패턴 (Factory Method Pattern)
### 구체적으로 어떤 인스턴스를 만들지 서브 클래스가 정한다.

- 다양한 구현체(Product)가 있고, 그 중에서 특정한 구현체를 만들 수 있는 다양한 팩토리(Creator)를 제공할 수 있다.


- 변경 전
  - 구현체가 없이 단일 객체가 다양한 요구사항을 받아들이며 인스턴스를 생성하고 있다.
  
```java
public class Client {

    public static void main(String[] args) {
        Ship whiteship = ShipFactory.orderShip("Whiteship", "keesun@mail.com");
        System.out.println(whiteship);

        Ship blackship = ShipFactory.orderShip("Blackship", "keesun@mail.com");
        System.out.println(blackship);
    }

}
```
  
```java
public class Ship {

    private String name;

    private String color;

    private String logo;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    @Override
    public String toString() {
        return "Ship{" +
                "name='" + name + '\'' +
                ", color='" + color + '\'' +
                ", logo='" + logo + '\'' +
                '}';
    }
}
```
  
```java
public class ShipFactory {

    public static Ship orderShip(String name, String email) {
        // validate
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("배 이름을 지어주세요.");
        }
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("연락처를 남겨주세요.");
        }

        prepareFor(name);

        Ship ship = new Ship();
        ship.setName(name);

        // Customizing for specific name
        if (name.equalsIgnoreCase("whiteship")) {
            ship.setLogo("\uD83D\uDEE5️");
        } else if (name.equalsIgnoreCase("blackship")) {
            ship.setLogo("⚓");
        }

        // coloring
        if (name.equalsIgnoreCase("whiteship")) {
            ship.setColor("whiteship");
        } else if (name.equalsIgnoreCase("blackship")) {
            ship.setColor("black");
        }

        // notify
        sendEmailTo(email, ship);

        return ship;
    }

    private static void prepareFor(String name) {
        System.out.println(name + " 만들 준비 중");
    }

    private static void sendEmailTo(String email, Ship ship) {
        System.out.println(ship.getName() + " 다 만들었습니다.");
    }

}
```

  
  
- 변경 후
  - 객체 생성 로직과 클라이언트 코드를 분리한다. 이로인해 클라이언트 코드의 변경 없이도 새로운 객체 타입을 쉽게 추가할 수 있다.
  - 클라이언트는 생성하려는 객체의 클래스에 대해 알 필요가 없으며, 단지 인터페이스나 추상클래스를 통해 상호작용한다.
    - 아래 예시 코드에서는 직접적으로 new WhiteshipFactory()처럼 구체적인 클래스를 명시했지만 내부에서 발생하는 createShip() 메서드의 구체적인 로직을 알 필요가 없다는 점이 중요하다.
  - 객체의 생성을 서브클래스에 맡김으로써, 클라이언트와 팩토리 메서드를 구현하는 클래스 간의 결합도를 낮출 수 있다.
  
```java
public class Client {

    public static void main(String[] args) {
        Client client = new Client();
        client.print(new WhiteshipFactory(), "whiteship", "keesun@mail.com");
        client.print(new BlackshipFactory(), "blackship", "keesun@mail.com");
    }

    private void print(ShipFactory shipFactory, String name, String email) {
        System.out.println(shipFactory.orderShip(name, email));
    }

}
```
  
```java
public interface ShipFactory {

    default Ship orderShip(String name, String email) {
        validate(name, email);
        prepareFor(name);
        Ship ship = createShip();
        sendEmailTo(email, ship);
        return ship;
    }

    void sendEmailTo(String email, Ship ship);

    Ship createShip();

    private void validate(String name, String email) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("배 이름을 지어주세요.");
        }
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("연락처를 남겨주세요.");
        }
    }

    private void prepareFor(String name) {
        System.out.println(name + " 만들 준비 중");
    }

}
```
  
```java
public abstract class DefaultShipFactory implements ShipFactory {

    @Override
    public void sendEmailTo(String email, Ship ship) {
        System.out.println(ship.getName() + " 다 만들었습니다.");
    }

}
```
  
```java
public class Ship {
  ...
}
```
  
```java
public class WhiteshipFactory extends DefaultShipFactory {

    @Override
    public Ship createShip() {
        return new Whiteship();
    }
}

```
  
```java
public class BlackshipFactory extends DefaultShipFactory {
    @Override
    public Ship createShip() {
        return new Blackship();
    }
}
```
  
```java
public class Whiteship extends Ship {

    public Whiteship() {
        setName("whiteship");
        setLogo("\uD83D\uDEE5");
        setColor("white");
    }
}
```
  
```java
public class Blackship extends Ship {

    public Blackship() {
        setName("blackship");
        setColor("black");
        setLogo("⚓");
    }
}
```

<br>

---
- 팩토리 메서드 패턴을 적용했을 때의 장점, 단점은?
  - 장점
    1. 클라이언트 코드와 인스턴스를 생성할 클래스를 분리함으로써, 기존 코드를 변경하지 않아도 된다.(유연성 & 확장성)
    2. 클라이언트가 구체 클래스에 의존하지 않고 인터페이스나 추상 클래스를 통해 객체를 생성하므로, 시스템의 결합도가 낮아지고, 유지보수성이 향상된다.(결합도 감소)
    3. 객체 생성을 서브클래스에 위임함으로써 어떤 서브 클래스의 인스턴스를 생성할지 결정하는 로직을 클라이언트로 부터 분리할 수 있다.(책임 분리)
  - 단점
    1. 생성 로직 별 팩토리 클래스를 구현해야 하므로 복잡도가 증가하고 클래스의 수가 늘어날 수 있다. (복잡도 증가)
    2. 클라이언트 코드가 더 추상화된 방법으로 객체를 생성하므로 시스템 전체 구조를 이해하는데 노력이 더 필요할 수 있다. (가독성 저하)

- 확장에 열려있고 변경에 닫혀있는 객체 지향 원칙을 설명하세요.
  - Open-Closed 원칙(개방폐쇄 원칙)
    - 클래스, 모듈, 함수와 같은 객체의 확장에 대해서는 열려있어야 하지만, 변경에 대해서는 닫혀 있어야 한다.
    - 기존 코드를 변경 하지 않으면서도 시스템의 기능을 확장 할 수 있어야 한다.

- 자바8에 추가된 default 메서드에 대해 설명하세요.
  - 자바 8부터 인터페이스에 구현 메서드를 작성할 수 있다.
  - 해당 인터페이스를 구현하는 클래스에서 오버라이드 할 수 있다.
  - default 메서드의 주 목적은 인터페이스에 새로운 메서드를 추가하면서도 기존에 이 인터페이스를 구현한 클래스에 대한 호환성을 유지하는 것이다.
---


