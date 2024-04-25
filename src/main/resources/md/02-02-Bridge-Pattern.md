## 브릿지 패턴 (Bridge Pattern)
### 추상적인 것과 구체적인 것을 분리하여 연결하는 패턴
- 하나의 계층 구조일 때 보다 각기 나누었을 때 독립적인 계층 구조로 발전 시킬 수 있다.
- 장점
  - 추상적인 코드를 구체적인 코드 변경 없이도 독립적으로 확장할 수 있다.
  - 추상적인 코드와 구체적인 코드를 분리해 객체의 결합도를 낮출 수 있다.
- 단점
  - 계층 구조가 늘어나 복잡도가 증가할 수 있다.

---
**그렇다면 추상적인 것과 구체적인 것을 나누는 기준은 무엇일까?**
브릿지 패턴에서 추상적인 것과 구체적인 것을 나누는 기준은 그들의 역할, 책임, 그리고 추상화 수준에 있다.

### 추상적인 것
- 역할과 책임 : 추상적인 부분은 시스템에서 수행해야 하는 고수준의 작업이나 기능을 정의한다. (주체가 되는 객체의 추상) 이는 시스템의 '**무엇을**'할 것인지에 대한 정의로, 구체적인 방법이나 알고리즘은 포함하지 않는다.
- 추상화 수준 : 이 부분은 추상적이고 일반적인 인터페이스를 제공하여, 다양한 구현을 통해 실제 작업을 수행할 수 있는 방법을 정의한다. 즉, 사용자 또는 클라이언트 코드가 상호작용하는 부분이다. 구체적인 구현 세부 사항으로부터 분리된 고수준의 인터페이스를 말한다.

### 구체적인 것
- 역할과 책임 : 구체적인 부분은 추상적인 부분에서 정의된 인터페이스의 실제 구현을 담당한다. 이는 '**어떻게**' 작업을 수행할 것인지에 대한 세부 사항을 포함하며, 구체적인 알고리즘, 프로세스, 기술적 세부 사항 등을 정의한다.
- 추상화 수준 : 이 부분은 낮은 수준의 구현 세부 사항을 다루며, 실제로 시스템의 기능이 어떻게 작동하는지를 정의한다. 이는 보통 특정 기술, 프로토콜, 데이터 포맷에 대한 구현을 포함한다.
---
  
- 적용 전
  - 직접 구현 : 각 'Champion' 구현체는 Champion 인터페이스 내의 모든 메서드를 직접 구현해야한다. 이로인해 스킨별로 동일한 기능을 중복해서 구현해야 하며, 각 챔피언 클래스는 특정 스킨에 강하게 결합하게 된다.
  - 확장성 문제 : 새로운 스킨을 추가하거나 챔피언의 기능을 변경할 때, 각각의 구현 클래스를 수정해야 하기 때문에 유지보수성이 떨어짐.
  - 중복 코드 : 스킨별 챔피언 클래스 간에는 많은 중복 코드가 발생 할 수 있다.
    
```java
public class App {

    public static void main(String[] args) {
        Champion kda아리 = new KDA아리();
        kda아리.skillQ();
        kda아리.skillR();
    }
}
```

```java
public interface Champion {
    void move();
    void skillQ();
    void skillW();
    void skillE();
    void skillR();
}
```

```java
public class KDA아리 implements Champion {

  @Override
  public void move() {
    System.out.println("KDA 아리 move");
  }

  @Override
  public void skillQ() {
    System.out.println("KDA 아리 Q");
  }

  @Override
  public void skillW() {
    System.out.println("KDA 아리 W");
  }

  @Override
  public void skillE() {
    System.out.println("KDA 아리 E");
  }

  @Override
  public void skillR() {
    System.out.println("KDA 아리 R");
  }
  
}
```

```java
public class PoolParty아리 implements Champion {

    @Override
    public void move() {
        System.out.println("PoolParty move");
    }

    @Override
    public void skillQ() {
        System.out.println("PoolParty Q");
    }

    @Override
    public void skillW() {
        System.out.println("PoolParty W");
    }

    @Override
    public void skillE() {
        System.out.println("PoolParty E");
    }

    @Override
    public void skillR() {
        System.out.println("PoolParty R");
    }
    
}
```

```java
public class 정복자아리 implements Champion {
    @Override
    public void move() {
        System.out.println("정복자 아리 move");
    }

    @Override
    public void skillQ() {
        System.out.println("정복자 아리 Q");
    }

    @Override
    public void skillW() {
        System.out.println("정복자 아리 W");

    }

    @Override
    public void skillE() {
        System.out.println("정복자 아리 E");
    }

    @Override
    public void skillR() {
        System.out.println("정복자 아리 R");
    }
    
}
```



- 적용 후
  - 추상화와 구현의 분리 : 'Champion' 인터페이스(Default Champion)는 스킨(Skin)을 포함하여 구현한다. 이로인해 스킨이 변경되어도 Champion의 구현 로직은 변하지 않는다.
  - 확장 용이성 : 새로운 스킨이 추가되거나 챔피언의 기능이 변경될 때, 스킨 구현체나 DefaultChampion만 수정하면 된다. 챔피언과 스킨 간의 결합도가 낮아져 각각 독립적으로 확장할 수 있다.
  - 코드 중복 감소 : 스킨별 동작을 'Skin' 인터페이스의 구현체에서 관리하기 때문에, 각 챔피언 클래스에서 중복되는 코드의 양이 현저히 줄어든다.
  - 구체적인 구현과 추상적인 부분의 독립적인 발전 가능 : 'Champion'과 'Skin'은 독립적인 계층 구조로 발전할 수 있으며 하나를 변경해도 다른 하나에 영향을 미치지 않는다.
  
```java
public class App {

    public static void main(String[] args) {
        Champion kda아리 = new 아리(new KDA());
        kda아리.skillQ();
        kda아리.skillW();

        Champion poolParty아리 = new 아리(new PoolParty());
        poolParty아리.skillR();
        poolParty아리.skillW();
    }
}
```

```java
public class DefaultChampion implements Champion {

    private Skin skin;

    private String name;

    public DefaultChampion(Skin skin, String name) {
        this.skin = skin;
        this.name = name;
    }

    @Override
    public void move() {
        System.out.printf("%s %s move\n", skin.getName(), this.name);
    }

    @Override
    public void skillQ() {
        System.out.printf("%s %s Q\n", skin.getName(), this.name);
    }

    @Override
    public void skillW() {
        System.out.printf("%s %s W\n", skin.getName(), this.name);
    }

    @Override
    public void skillE() {
        System.out.printf("%s %s E\n", skin.getName(), this.name);
    }

    @Override
    public void skillR() {
        System.out.printf("%s %s R\n", skin.getName(), this.name);
    }

    @Override
    public String getName() {
        return null;
    }
}
```

```java
public class 아리 extends DefaultChampion {

    public 아리(Skin skin) {
        super(skin, "아리");
    }
}
```

```java
public interface Skin {
    String getName();
}
```

```java
public class KDA implements Skin{
    @Override
    public String getName() {
        return "KDA";
    }
}
```

```java
public class PoolParty implements Skin {
    @Override
    public String getName() {
        return "PoolParty";
    }
}
```

