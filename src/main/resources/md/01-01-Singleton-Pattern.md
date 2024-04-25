## 싱글톤 패턴 (Singleton Pattern)
### 인스턴스를 오직 한개만 제공하는 클래스

- 시스템 런타임, 환경 세팅에 대한 정보 등, 인스턴스가 여러개 일 때 문제가 생길 수 있는 경우가 있다. 이 때, 인스턴스를 오직 한 개만 만들어 제공하는 클래스가 필요하다.

<br>

1. 가장 기본적인 싱글턴 패턴
- 멀티스레딩 환경에서 안전하지 않을 수 있다. 여러 스레드가 getInstance() 메서드를 동시에 호출한다면 인스턴스가 두 번 생성될 수 있기 때문이다.
- 이를 방지하려면 동기화를 추가할 필요가 있다.
  
```java
public class Settings1 {

    // 인스턴스를 저장할 static 변수. 클래스 로딩 시점에서 한 번만 할당되며, 클래스의 유일한 인스턴스를 참조한다.
    private static Settings1 instance;

    // 외부에서 인스턴스 생성을 방지하기 위해 생성자를 private으로 선언. 이로써 클래스 내부에서만 인스턴스를 생성할 수 있다.
    private Settings1() { }

    // 유일한 Settings1 인스턴스에 접근하기 위한 public static 메서드
    // 첫 호출 시 인스턴스를 생성하며, 이후 호출에서는 이미 생성된 인스턴스를 반환
    // 이 메서드는 멀티스레딩 환경에서는 추가적인 동기화 처리가 필요할 수 있음
    public static Settings1 getInstance() {
        if (instance == null) {
            instance = new Settings1();
        }

        return instance;
    }

}
```

<br>

2. 멀티스레딩 환경에서 Thread Safe한 싱글턴 패턴
- 스레드1, 스레드2 가 동시에 getInstance()를 실행했을 때 객체가 두 번 생성되는 경우가 있다.
- getInstance() 메서드에 synchronized 키워드를 사용해서 동기화 메서드로 만든다.
- 위 방법은 스레드마다 메서드가 처리될때 순서가 정해지기 떄문에 성능상 불리할 경우가 생길 수 있다.
- 객체 생성비용이 비싸지 않다면, **이른 초기화(Eager Initialization)**를 사용하는 방법도 있다. 미리 만들어 놓은 인스턴스를 반환하기 때문에 Thread Safe하다.
  - 객체 생성비용이 비싸고 자주 사용되지 않는다면 굳이 이 방법을 고집할 필요가 없다.

```java
public class Settings {
    private static final Settings INSTANCE;
    
    static {
      try {
        INSTANCE = new Settings();
      } catch (Exception e) {
        throw new RuntimeException("Settings 객체 생성 중 예외 발생 : ", e);
      }
    }
    
    private Settings() throws Exception {
        
    }

    public static Settings getInstance() {
        return INSTANCE;
    }
}
```

<br>

3. synchronized 메서드 사용
- 그리고 위 방법보다 더 나은 방법으로는 getInstance() 메서드 자체를 동기화(synchronized) 시키는 방법이다.
- 하지만 이 방법도 그다지 효과적이진 않다. 이유는 getInstance()가 호출될 때마다 동기화를 실행시키기 때문이다.(성능과 연관)
```java
public class Settings {

    private static Settings instance;

    private Settings() { }
    
    public static synchronized Settings getInstance() {
        if (instance == null) {
            instance = new Settings();
        }

        return instance;
    }
}
```

<br>

4. Double Checked Locking
- 위 방법보다 조금 더 나은 방법은 아래와 같다. (자바 1.5 이상에서만 가능)
  - volatile : java 메모리 모델에 따라, volatile 변수에 대한 쓰기와 읽기는 항상 메인 메모리를 통해 이루어진다. 이는 instance 변수의 값을 읽고 쓸 때 다른 스레드에 의해 변경된 최신 값이 보장되도록 함으로써 스레드 간에 변수가 올바르게 보이도록 한다. volatile 키워드 없이는, 컴파일러 최적화로 인해 변경사항이 즉각적으로 반영되지 않을 수 있다.
- 멀티스레드가 매우 빈번하게 일어나는 환경에서만 if문 안에 여러 스레드가 동시에 들어올 수 있는데 그때에만 synchronized 키워드가 걸리기 때문에 성능상 이득이 있다.
- 동기화 블록으로 진입하는 동안 다른 스레드에 의해 이미 인스턴스가 생성될 가능성을 고려해 두 번 null 체크에 들어간다.
```java
public class Settings {

  private static volatile Settings instance;

  private Settings() { }

  public static Settings getInstance() {
    // 체크1 : 대부분의 경우 인스턴스가 이미 생성되어 있을 것이기 때문에, 동기화 블록으로 들어가는 비용을 피하기 위한 최적화다.  
    if (instance == null) {
        // 동기화 블럭에 진입 (먼저 진입한 스레드가 완전히 처리될때까지 다음 순번의 스레드는 대기)
        synchronized (Settings.class) {
            // 체크2 : 이 두번째 검사는 동기화 블록 내에서만 발생하기 때문에, 한 번에 오직 하나의 스레드만 인스턴스를 생성할 수 있다. 
            if (instance == null) { 
                instance = new Settings();
            }
        }
    }
    return instance;
  }
}
```

<br>

5. static inner class를 사용한 싱글턴 패턴 구현
- Settings4 인스턴스 생성을 Settings4Holder 라는 내부 정적 클래스(inner static class)에 위임함
- 멀티스레드 환경에서도 안전하고, 또 getInstance()가 호출될 때, 이 클래스로딩이 일어나고 인스턴스를 생성하기 때문에 레이지 로딩(Lazy Loading)도 가능하다.
  - 자바에서는 클래스가 사용되기 전까지 로딩되지 않는다. 즉, Settings4 클래스의 getInstance() 메서드가 호출되기 전까지 Settings4Holder 클래스가 로드되지 않는다.
  - Settings4Holder.INSTANCE에 접근할 때 처음으로 Settings4Holder 클래스가 로드되며, 이 때, Settings4 인스턴스가 생성된다.
- 그렇다면 왜 스레드 세이프한가? 
  - Settings4Holder 클래스의 초기화는 JVM이 수행하며, JVM은 클래스의 초기화 단계를 스레드 세이프하게 관리한다. 이는 별도의 동기화 처리가 없이도 멀티 스레드 환경에서 Settings4 인스턴스의 유일성과 스레드 안전성을 보장한다.
- 성능 부분
  - 이 방식은 getInstance() 메서드에 동기화를 적용할 필요가 없기 때문에, 동기화에 따른 성능 저하가 없다. (synchronized 키워드 사용 안함 -> 접근비용이 낮아짐)
- 요약
  - 이 패턴은 클래스의 로딩 메커니즘과 JVM의 클래스 초기화 과정의 특성을 활용해서, 싱글턴 인스턴스를 필요할 때 생성(lazy loading)하고, 스레드 세이프하며 추가적인 동기화 없이 성능저하를 최소화하는 방식으로 싱글턴 패턴을 구현했다. 

```java
public class Settings4 {

    private Settings4() {}

    private static class Settings4Holder {
        private static final Settings4 INSTANCE = new Settings4();
    }

    public static Settings4 getInstance() {
        return Settings4Holder.INSTANCE;
    }
}
```

<br>

- 싱글턴 패턴을 깨트리는 방법 1
  - 자바의 리플렉션을 사용해서 컴파일타임이 아닌 런타임에 인스턴스를 생성한다.
  - setAccessible(true) 를 사용해 접근제한자가 private로 설정된 생성자 메서드에 접근가능하게 한다. 
```java
import java.lang.reflect.Constructor;

public class App {
  public static void main(String[] args) {
    Settings4 instance1 = Settings4.getInstance();
    Settings4 instance2 = Settings4.getInstance();
    System.out.println(instance1 == instance2);    // 싱글턴으로 만들어졌기 때문에 같은 객체다.

    // 리플렉션을 사용해서 싱글턴 패턴 깨트리기
    Constructor<Settings4> constructor = Settings4.class.getDeclaredConstructor();
    constructor.setAccessible(true);    // private 생성자를 접근가능하게함
    Settings4 newInstance = constructor.newInstance();

    System.out.println(newInstance == instance1); // false 
    
  }
}
```

<br>

- 싱글턴 패턴을 깨트리는 방법 2
  - 직렬화 : 자바에서 객체의 상태를 바이트 스트림으로 변환하는 과정이다. 이렇게 변환된 바이트 스트림은 파일, DB, 메모리 등에 저장하거나 네트워크를 통해 다른 JVM으로 전송할 수 있다. 직렬화의 주된 목적은 객체의 영속성을 제공하고, 객체를 분산환경에서 공유할 수 있게 해주는 것이다.
  - 역직렬화 : 직렬화된 바이트 스트림을 다시 자바 객체로 변환하는 과정이다. 이를 통해 객체의 상태가 복원되며 프로그램은 이 객체를 사용하여 작업을 할 수 있다.
  - serialVersionUID : 직렬화 -> 역직렬화 과정에서 클래스의 버전을 확인하는데 사용된다. 명시적으로 serialVersionUID를 선언하지 않은 경우, JVM은 클래스의 세부정보를 기반으로 자동으로 이 값을 생성한다.
    - 클래스의 구조가 변경될 때(필드 추가/삭제, 클래스 이름 변경 등) serialVersionUID가 일치하지 않으면 역직렬화 과정에서 InvalidClassException이 발생 할 수 있다. 따라서 직렬화된 객체와 클래스 사이의 호환성을 보장하기 위해 serialVersionUID를 명시적으로 선언하는 것이 좋다.
  - try-with-resource : 자바7 부터 도입된 try-with-resource 문은 자원을 사용한 후에 이를 자동으로 닫아주는 기능을 제공한다. 이 문법을 사용하려면 해당 리소스가 AutoClosable 인터페이스를 구현해야 한다. 이 인터페이스는 void close()를 정의하고 있으며, try-with-resources 문이 종료될때 자동으로 close()를 호출한다.
    - 해당 블록은 파일 I/O, DB 연결, 네트워크 연결 등 리소스를 사용한 후 명시적으로 닫아주는 코드를 작성할 필요가 없다.
```java
import java.io.Serializable;

public class Settings4 implements Serializable {

  private Settings4() {
  }

  private static class Settings4Holder {
    private static final Settings4 INSTANCE = new Settings4();
  }

  public static Settings4 getInstance() {
    return Settings4Holder.INSTANCE;
  }
}
```

```java
import me.whiteship.designpatterns._01_creational_patterns._01_singleton.Settings4;

import java.io.*;

public class App {
  public static void main(String[] args) {
    Settings4 settings4 = Settings4.getInstance();
    Settings4 settings44 = null;

    // 직렬화를 사용해서 인스턴스 생성하기
    try (ObjectOutput out = new ObjectOutputStream(new FileOutputStream("settings.obj"))) {
      out.write(settings4);
    }

    // 역직렬화를 사용해서 인스턴스 생성하기
    try (ObjectInput in = new ObjectInputStream(new FileInputStream("settings.obj"))) {
      settings44 = (Settings4) in.readObject();
    }

    System.out.println(settings4 == settings44);
  }
}
```

<br>

- Enum을 사용해서 싱글턴 패턴을 구현하기
  - 장점 : 리플렉션으로도 싱글턴 패턴을 깨지 못한다, 직렬화/역직렬화에 안전하다
  - 단점 : 상속을 사용하지 못한다. 인스턴스를 미리 생성해 놓는다.

1. enum 타입의 인스턴스를 리팩토링 할 수 있는가? -> enum타입의 인스턴스는  static final로 간주하기 때문에 리팩토링 불가하다.
2. enum 으로 싱글턴 패턴을 구현할 때 단점? -> 상속을 사용할 수 없다. 그리고 lazy loading이나 동적인 데이터 변경이 불가능하다.
3. 직렬화&역직렬화 시에 별도 구현할 메서드가 있는가? -> enum은 내부적으로 Enum 클래스를 상속받고 있다. 그리고 Enum 클래스는 Serializable 클래스를 구현하고 있기 때문에 별도의 직렬화 관련 메서드를 구현할 필요가 없다.

```java
public enum Settings5 {

    INSTANCE;

}
```

<br>

---
- 자바에서 enum을 사용하지 않고 싱글턴 패턴을 구현하는 방법?
```java
public class Singleton {
    private Singleton() {}
    
    private static class SingletonHolder {
        private static final Singleton INSTANCE = new Singleton();
    }
  
    public static Singleton getInstance() {
        SingletonHolder.INSTANCE;
    }
    
}
```

<br>

- private 생성자와 static 메소드를 사용하는 방법의 단점은? 
  - 단점 : 멀티스레딩 환경에서 안전하지 못하다. 여러 스레드가 동시에 객체생성을 요청할 경우 인스턴스가 여러개 생성될 가능성 있음
```java
public class Singleton {
    private static Singleton instance;
    
    private Singleton() {}
  
    public static Singleton getInstance() {
        if(instance == null) {
          instance = new Singleton();
        }
        return instance;
    }
}
```

- enum을 사용해 싱글톤 패턴을 구현하는 방법의 장점과 단점은? 
  - 장점 : 자동으로 직렬화/역직렬화 지원(enum class 자체가 Enum을 상속받기 때문), 리플렉션을 이용한 싱글톤 파훼가 불가능하다. 코드가 간단하다
  - 단점 : 인스턴스를 미리 생성해 놓는다. 상속을 하지 못하기 때문에 확장성에 제한이 있다. enum 클래스는 내부적으로 static final이기 때문에 어떤 요소도 수정될 수 없다.

<br>
  
- static inner 클래스를 사용해 싱글톤 패턴을 구현하라
  - 멀티 스레딩 환경에서 안전함 (정적 내부 클래스의 호출 시 JVM의 클래스 로딩 자체가 동기화를 지원하기 때문)
  - synchronized 키워드를 사용하지 않아서 객체 생성 비용이 낮아짐
  - 객체를 미리 생성하지 않고 필요 시에만 생성함
```java
public class Singleton {
    private Singleton() {}  // private 생성자 메서드
    
    private static class SingletonHolder {  // 정적 내부 클래스를 사용해서 객체 생성 (해당 클래스 호출 시 싱글턴 객체 생성)
        private static final Singleton INSTANCE = new Singleton();
    }
    
    public static Singleton getInstance() { // 해당 메서드 호출 시 정적 내부 클래스를 호출하여 싱글턴 객체 반환
      return SingletonHolder.INSTANCE;
    }
}
```

---

- 자바와 스프링에서 찾아보는 싱글턴 패턴
  - Runtime 객체
```java
public class App {

    public static void main(String[] args) {
      Runtime runtime = Runtime.getRuntime();
      System.out.println(runtime.maxMemory());
      System.out.println(runtime.freeMemory());
    }

}
```

```java
  public class Runtime {
      private static final Runtime currentRuntime = new Runtime();
  
      private static Version version;
  
      /**
       * Returns the runtime object associated with the current Java application.
       * Most of the methods of class {@code Runtime} are instance
       * methods and must be invoked with respect to the current runtime object.
       *
       * @return  the {@code Runtime} object associated with the current
       *          Java application.
       */
      public static Runtime getRuntime() {
          return currentRuntime;
      }
  
      /** Don't let anyone else instantiate this class */
      private Runtime() {}
      ...
```

<br>

- 싱글톤 스코프
  - Spring에서 빈의 스코프는 기본적으로 싱글톤입니다. 그러나 필요에 따라 프로토타입(Prototype), 요청(Request), 세션(Session) 등 다른 스코프를 지정할 수 있습니다. 이러한 스코프는 각각의 빈 생명 주기와 생성 방식에 영향을 미치며, 어플리케이션의 요구 사항에 따라 적절히 선택하여 사용해야 합니다.

- 싱글톤(Singleton): 스프링 컨테이너 내에 단 하나의 빈 인스턴스만을 생성합니다.
- 프로토타입(Prototype): 빈을 요청할 때마다 새로운 인스턴스를 생성합니다.
- 요청(Request): HTTP 요청마다 빈 인스턴스를 새로 생성합니다. (웹 어플리케이션 컨텍스트에서 사용)
- 세션(Session): HTTP 세션마다 빈 인스턴스를 새로 생성합니다. (웹 어플리케이션 컨텍스트에서 사용)

```java
public class SpringExample {

    public static void main(String[] args) {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(SpringConfig.class);
        String hello = applicationContext.getBean("hello", String.class);
        String hello2 = applicationContext.getBean("hello", String.class);
        System.out.println(hello == hello2);
    }

}
```

