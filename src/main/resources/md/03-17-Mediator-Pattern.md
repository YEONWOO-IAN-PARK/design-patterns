## 중재자 패턴 (Mediator Pattern)
### 여러 객체들이 소통하는 방법을 캡슐화하는 패턴
- 여러 컴포넌트 간의 결합도를 중재자를 통해 낮출 수 있다.
  
- 장점
  - 컴포넌트 코드를 변경하지 않고 새로운 중재자를 만들어 사용할 수 있다.
  - 각각의 컴포넌트 코드를 보다 간결하게 유지할 수 있다.
  
- 단점
  - 중재자 역할을 하는 클래스의 복잡도와 결합도가 증가한다.


- 적용 전
  - Guest 클래스와 CleaningService, Restaurant 클래스의 결합도가 높다.
  - (중복되는 형태의 코드들도 보이고 있다)
  - 중재자를 생성함으로써 각 컴포넌트 간 의존성을 낮출 수 있을 것 같다.

```java
public class Hotel {

    public static void main(String[] args) {
        Guest guest = new Guest();
        guest.getTower(3);
        guest.dinner();

        Restaurant restaurant = new Restaurant();
        restaurant.clean();
    }
}
```

```java
public class Guest {

    private Restaurant restaurant = new Restaurant();

    private CleaningService cleaningService = new CleaningService();

    public void dinner() {
        restaurant.dinner(this);
    }

    public void getTower(int numberOfTower) {
        cleaningService.getTower(this, numberOfTower);
    }

}
```

```java
public class CleaningService {
    public void clean(Gym gym) {
        System.out.println("clean " + gym);
    }

    public void getTower(Guest guest, int numberOfTower) {
        System.out.println(numberOfTower + " towers to " + guest);
    }

    public void clean(Restaurant restaurant) {
        System.out.println("clean " + restaurant);
    }
}
```

```java
public class Restaurant {

    private CleaningService cleaningService = new CleaningService();
    public void dinner(Guest guest) {
        System.out.println("dinner " + guest);
    }

    public void clean() {
        cleaningService.clean(this);
    }
}
```





  
- 적용 후
  - 중재자(FrontDesk)를 생성함으로써 각 컴포넌트간 결합도를 낮추었다.

```java
public class Guest {

    private Integer id;

    private FrontDesk frontDesk = new FrontDesk();

    public void getTowers(int numberOfTowers) {
        this.frontDesk.getTowers(this, numberOfTowers);
    }

    private void dinner(LocalDateTime dateTime) {
        this.frontDesk.dinner(this, dateTime);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
```

```java
public class FrontDesk {

    private CleaningService cleaningService = new CleaningService();

    private Restaurant restaurant = new Restaurant();

    public void getTowers(Guest guest, int numberOfTowers) {
        cleaningService.getTowers(guest.getId(), numberOfTowers);
    }

    public String getRoomNumberFor(Integer guestId) {
        return "1111";
    }

    public void dinner(Guest guest, LocalDateTime dateTime) {
        restaurant.dinner(guest.getId(), dateTime);
    }
}
```

```java
public class CleaningService {

    private FrontDesk frontDesk = new FrontDesk();

    public void getTowers(Integer guestId, int numberOfTowers) {
        String roomNumber = this.frontDesk.getRoomNumberFor(guestId);
        System.out.println("provide " + numberOfTowers + " to " + roomNumber);
    }
}
```

```java
public class Restaurant {
    public void dinner(Integer id, LocalDateTime dateTime) {

    }
}
```


--- 
### 자바와 스프링에서 찾아보는 중재자 패턴
- 자바
  - Executor
  - ExecutorService

- 스프링
  - DispatcherServlet : 클라이언트의 모든 요청을 받아들여 목적에 맞게 실행시킴