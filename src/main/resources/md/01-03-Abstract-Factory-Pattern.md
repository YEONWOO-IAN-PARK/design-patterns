## 추상 팩토리 패턴 (Abstract Factory Pattern)
### 서로 관련있는 여러 객체를 만들어주는 인터페이스
- 구체적으로 어떤 클래스의 인스턴스를 (concrete product) 사용하는지 감출 수 있다.

<br>

- 추상 팩토리 패턴 적용 전
  - createShip()으로 배를 만들때 마다 배의 파츠인 Achor와 Wheel을 어떤 종류로 만들 것인지 매번 클라이언트 코드를 수정해야 할 수 있다.
  
```java
public class WhiteshipFactory extends DefaultShipFactory {

    @Override
    public Ship createShip() {
        Ship ship = new Whiteship();
        ship.setAnchor(new WhiteAnchor());
        ship.setWheel(new WhiteWheel());
        return ship;
    }
}
```

```java
public class WhiteAnchor implements Anchor {
}
```

```java
public class WhiteWheel implements Wheel {
}
```

---

- 추상 팩토리 패턴 적용 후
  
```java
public class WhiteshipFactory extends DefaultShipFactory {

    private ShipPartsFactory shipPartsFactory;  // 추상 팩토리

    public WhiteshipFactory(ShipPartsFactory shipPartsFactory) {
        // 어떤 추상 팩토리를 인자로 넣어주냐에 따라 Anchor와 Wheel의 파츠 종류가 결정됨
        this.shipPartsFactory = shipPartsFactory;
    }

    @Override
    public Ship createShip() {
        Ship ship = new Whiteship();
        ship.setAnchor(shipPartsFactory.createAnchor());
        ship.setWheel(shipPartsFactory.createWheel());
        return ship;
    }
}
```

- 추상 팩토리(인터페이스) 생성  
```java
public interface ShipPartsFactory {

    Anchor createAnchor();

    Wheel createWheel();

}
```

```java
public class WhiteshipPartsFactory implements ShipPartsFactory {

    @Override
    public Anchor createAnchor() {
        return new WhiteAnchor();
    }

    @Override
    public Wheel createWheel() {
        return new WhiteWheel();
    }
}
```  

- Pro 파츠를 생산하고 싶다면? 
  - 서브 클래스인 Pro 파츠를 생산하는 팩토리
  
```java
public class WhitePartsProFactory implements ShipPartsFactory {
    @Override
    public Anchor createAnchor() {
        return new WhiteAnchorPro();
    }

    @Override
    public Wheel createWheel() {
        return new WhiteWheelPro();
    }
}
```

```java
public class WhiteWheelPro implements Wheel {
}
```
  
```java
public class WhiteAnchorPro implements Anchor{
}
```

  
- 최종 테스트
  - 추상 팩토리의 서브 클래스(구현체 팩토리)를 어떤것을 사용할 것인지 명시
  - 서브 클래스의 생성자 인자로 어떤 종류의 부품을 생성하는 서브 클래스(구현체 팩토리)를 사용할 것인지 명시
```java
public class ShipInventory {

    public static void main(String[] args) {
        ShipFactory shipFactory = new WhiteshipFactory(new WhitePartsProFactory());
        Ship ship = shipFactory.createShip();
        System.out.println(ship.getAnchor().getClass());
        System.out.println(ship.getWheel().getClass());
    }
}
```