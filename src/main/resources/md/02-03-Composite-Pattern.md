## 컴포짓 패턴 (Composite Pattern)
### 그룹 전체와 개별 객체를 동일하게 처리할 수 있는 패턴
- 클라이언트 입장에서는 '전체'나 '부분'이나 모두 동일한 컴포넌트로 인식할 수 있는 계층 구조를 만든다. (Part-Whole Hierarchy)

- 장점
  - 복잡한 트리 구조를 편리하게 사용할 수 있다.
  - 다형성과 재귀를 활용할 수 있다.
  - 클라이언트 코드를 변경하지 않고 새로운 엘리먼트 타입을 추가할 수 있다.

- 단점
  - 트리를 만들어야 하기 때문에 (공통된 인터페이스를 정의해야 하기 때문에) 지나치게 일반화해야 하는 경우도 생길 수 있다.


- 적용 전
  - Item과 Bag이 서로 다른 타입으로 처리되어, 클라이언트 코드에서 각각의 가격을 출력하기 위해 각각의 메서드를 작성해야 한다.

```java
public class Client {

    public static void main(String[] args) {
        Item doranBlade = new Item("도란검", 450);
        Item healPotion = new Item("체력 물약", 50);

        Bag bag = new Bag();
        bag.add(doranBlade);
        bag.add(healPotion);

        Client client = new Client();
        client.printPrice(doranBlade);
        client.printPrice(bag);
    }

    private void printPrice(Item item) {
        System.out.println(item.getPrice());
    }

    private void printPrice(Bag bag) {
        int sum = bag.getItems().stream().mapToInt(Item::getPrice).sum();
        System.out.println(sum);
    }

}
```

```java
public class Bag {

    private List<Item> items = new ArrayList<>();

    public void add(Item item) {
        items.add(item);
    }

    public List<Item> getItems() {
        return items;
    }
}
```

```java
public class Item {

    private String name;

    private int price;

    public Item(String name, int price) {
        this.name = name;
        this.price = price;
    }

    public int getPrice() {
        return this.price;
    }
}
```



- 적용 후
  - Component 인터페이스를 도입하여 Item, Bag이 동일한 인터페이스를 구현하게 함으로써, 클라이언트 코드에서 이들을 동일하게 처리할 수 있다. 이를 통해 코드의 유연성과 확장성이 향상된다.
  - Component 인터페이스의 getPrice 메서드를 통해 Item의 가격 뿐만 아니라 Bag 내의 모든 Item과 Bag을 구분하지 않고 가격 정보를 얻을 수 있게 한다.
  - Bag 클래스 내부에 Bag 객체를 포함시켜 더 복잡한 트리 구조, 또는 재귀 탐색을 하게 할 수도 있다.

```java
public class Client {

    public static void main(String[] args) {
        Item doranBlade = new Item("도란검", 450);
        Item healPotion = new Item("체력 물약", 50);

        Bag bag = new Bag();
        bag.add(doranBlade);
        bag.add(healPotion);

        Client client = new Client();
        client.printPrice(doranBlade);
        client.printPrice(bag);
    }

    private void printPrice(Component component) {
        System.out.println(component.getPrice());
    }
}
```

```java
public interface Component {

    int getPrice();

}
```

```java
public class Bag implements Component {

    private List<Component> components = new ArrayList<>();

    public void add(Component component) {
        components.add(component);
    }

    public List<Component> getComponents() {
        return components;
    }

    @Override
    public int getPrice() {
        return components.stream().mapToInt(Component::getPrice).sum();
    }
}
```

```java
public class Item implements Component {

    private String name;

    private int price;

    public Item(String name, int price) {
        this.name = name;
        this.price = price;
    }

    @Override
    public int getPrice() {
        return this.price;
    }
}
```

```java
public class Character implements Component {

    private Bag bag;

    @Override
    public int getPrice() {
        return bag.getPrice();
    }

}
```