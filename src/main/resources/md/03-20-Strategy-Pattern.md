## 전략 패턴 (Strategy Pattern)
### 여러 알고리즘을 캡슐화하고 상호 교환 가능하게 만드는 패턴
- 컨텍스트에서 사용한 알고리즘을 클라이언트가 선택한다.

- 장점
  - 새로운 전략을 추가하더라도 기존 코드를 변경하지 않는다.
  - 상속 대신 위임을 사용할 수 있다.
  - 런타임에 전략을 변경할 수 있다.

- 단점
  - 복잡도가 증가한다.
  - 클라이언트 코드가 구체적인 전략을 알아야 한다.



- 적용 전
  - Context와 Strategy가 강하게 결합되어 있다.
  - 구체적인 Strategy를 추가하고 싶을 때 마다 Context를 수정해야 하므로 확장성이 제한된다.(OCP원칙 위배)
  - 게임규칙 변경이 필요할 때, 이를 동적으로 처리하는 것이 어렵다. 런타임에 수정이 불가능하고 코드 수정 후 재배포가 필요하다.

```java
public class Client {

    public static void main(String[] args) {
        BlueLightRedLight blueLightRedLight = new BlueLightRedLight(3);
        blueLightRedLight.blueLight();
        blueLightRedLight.redLight();
    }
}
```

```java
public class BlueLightRedLight {

    private int speed;

    public BlueLightRedLight(int speed) {
        this.speed = speed;
    }

    public void blueLight() {
        if (speed == 1) {
            System.out.println("무 궁 화    꽃   이");
        } else if (speed == 2) {
            System.out.println("무궁화꽃이");
        } else {
            System.out.println("무광꼬치");
        }

    }

    public void redLight() {
        if (speed == 1) {
            System.out.println("피 었 습 니  다.");
        } else if (speed == 2) {
            System.out.println("피었습니다.");
        } else {
            System.out.println("피어씀다");
        }
    }
}
```



- 적용 후
  - Context와 Strategy 간의 결합도가 느슨해졌다.
  - Strategy Interface를 생성 및 구현하기 때문에 코드 확장성이 증가하였다.(OCP)
  - 게임규칙 변경이 필요할 때 동적으로 처리할 수 있다. (재배포가 필요 없고 런타임에 처리 가능)

  
```java
public class Client {

    public static void main(String[] args) {
        BlueLightRedLight game = new BlueLightRedLight();
        game.blueLight(new Normal());
        game.redLight(new Fastest());
        game.blueLight(new Speed() {
            @Override
            public void blueLight() {
                System.out.println("blue light");
            }

            @Override
            public void redLight() {
                System.out.println("red light");
            }
        });
    }
}
```

```java
public class BlueLightRedLight {

    public void blueLight(Speed speed) {
        speed.blueLight();
    }

    public void redLight(Speed speed) {
        speed.redLight();
    }
}
```

```java
public class Fastest implements Speed{
    @Override
    public void blueLight() {
        System.out.println("무광꼬치");
    }

    @Override
    public void redLight() {
        System.out.println("피어씀다.");
    }
}
```

```java
public class Faster implements Speed {
    @Override
    public void blueLight() {
        System.out.println("무궁화꽃이");
    }

    @Override
    public void redLight() {
        System.out.println("피었습니다.");
    }
}
```

```java
public class Normal implements Speed {
    @Override
    public void blueLight() {
        System.out.println("무 궁 화    꽃   이");
    }

    @Override
    public void redLight() {
        System.out.println("피 었 습 니  다.");
    }
}
```

```java
public interface Speed {

    void blueLight();

    void redLight();

}
```


---
### 자바와 스프링에서 찾아보는 전략 패턴

```java
public class StrategyInSpring {

    public static void main(String[] args) {
        // ApplicationContext 라는 Strategy Inteface를 상속받은 Concrete Strategy 들이 많다.
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext();
        ApplicationContext applicationContext1 = new FileSystemXmlApplicationContext();
        ApplicationContext applicationContext2 = new AnnotationConfigApplicationContext();

        // 얘도 마찬가지
        BeanDefinitionParser parser = new AnnotationConfigBeanDefinitionParser();
        BeanDefinitionParser parser1 = new AnnotationDrivenBeanDefinitionParser();

        // 얘도 마찬가지
        PlatformTransactionManager platformTransactionManager = new HibernateTransactionManager();
        PlatformTransactionManager platformTransactionManager1 = new JpaTransactionManager();

        // 얘도 마찬가지
        CacheManager cacheManager = new EhCacheCacheManager();
        CacheManager cacheManager1 = new JCacheCacheManager();
    }
}
```

```java
public class StrategyInJava {

    public static void main(String[] args) {
        List<Integer> numbers = new ArrayList<>();
        numbers.add(10);
        numbers.add(5);

        System.out.println(numbers);
        // Collections.sort : 알고리즘 실행
        // Comparator.naturalOrder : 캡슐화된 알고리즘 선택
        Collections.sort(numbers, Comparator.naturalOrder());

        System.out.println(numbers);
    }
}
```
