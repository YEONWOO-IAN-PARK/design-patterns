## 커맨드 패턴 (Command Pattern)
### 요청을 캡슐화하여 호출자(invoker)와 수신자(receiver)를 분리하는 패턴
- 요청을 처리하는 방법이 바뀌어도, 호출자의 코드는 변경되지 않는다.

- 장점
  - 기존 코드를 변경하지 않고 새로운 커맨드를 만들 수 있다.
  - 수신자의 코드(Game, Light)가 변경되어도 호출자의 코드는 변경되지 않는다.
  - 커맨드 객체를 로깅, DB에 저장, 네트워크로 전송하는 등 다양한 방법으로 활용할 수도 있다.

- 단점
  - 코드가 복잡하고 클래스가 많아진다.



- 적용 전
  - Button은 Light라는 클래스와 강하게 결합되어 있어서 버튼을 누를 때마다 오직 불을 켜고 끄는 동작만 가능하다.
  - 버튼의 기능을 변경하거나 추가할 때마다 'Button'클래스를 수정해야 하므로 유연성이 떨어진다.
  
```java
public class MyApp {

    private Game game;

    public MyApp(Game game) {
        this.game = game;
    }

    public void press() {
        game.start();
    }

    public static void main(String[] args) {
        Button button = new Button(new Light());
        button.press();
        button.press();
        button.press();
        button.press();
    }
}
```

```java
public class Button {

    private Light light;

    public Button(Light light) {
        this.light = light;
    }

    public void press() {
        light.off();
    }

    public static void main(String[] args) {
        Button button = new Button(new Light());
        button.press();
        button.press();
        button.press();
        button.press();
    }
}
```

```java
public class Game {

    private boolean isStarted;

    public void start() {
        System.out.println("게임을 시작합니다.");
        this.isStarted = true;
    }

    public void end() {
        System.out.println("게임을 종료합니다.");
        this.isStarted = false;
    }

    public boolean isStarted() {
        return isStarted;
    }
}
```

```java
public class Light {

    private boolean isOn;

    public void on() {
        System.out.println("불을 켭니다.");
        this.isOn = true;
    }

    public void off() {
        System.out.println("불을 끕니다.");
        this.isOn = false;
    }

    public boolean isOn() {
        return this.isOn;
    }
}
```





적용 후
 - Command 인터페이스를 통해 모든 명령어는 execute()와 undo()를 통해서 구현해야 한다.
 - 구체적인 명령 클래스를 생성하고 execute()와 undo()를 통해 구체적인 작업을 생성한다.
 - 호출자(Button, MyApp)는 구체적인 명령 클래스를 받아서 이를 실행하거나 취소하는 역할을 담당한다. 이로인해 호출자는 수신자의 구체적인 클래스를 몰라도 된다.
 - Button 클래스에서 명령을 실행할 때 stack에 명령어를 저장함으로써 undo()를 실행할때마다 가장 최근에 실행된 명령어를 취소할 수 있다.
 - 새로운 명령어가 필요할 경우, 새로운 Command 구현체를 생성하기만 하면 된다.


```java
public class MyApp {

    private Command command;

    public MyApp(Command command) {
        this.command = command;
    }

    public void press() {
        command.execute();
    }

    public static void main(String[] args) {
        MyApp myApp = new MyApp(new GameStartCommand(new Game()));
        myApp.press();
        myApp.command.undo();
    }
}
```

```java
public class Button {

    private Stack<Command> commands = new Stack<>();

    public void press(Command command) {
        command.execute();
        commands.push(command);
    }

    public void undo() {
        if (!commands.isEmpty()) {
            Command command = commands.pop();
            command.undo();
        }
    }

    public static void main(String[] args) {
        Button button = new Button();
        button.press(new GameStartCommand(new Game()));
        button.press(new LightOnCommand(new Light()));
        button.undo();
        button.undo();
    }

}
```

```java
public interface Command {

    void execute();

    void undo();

}
```

```java
public class GameStartCommand implements Command {

    private Game game;

    public GameStartCommand(Game game) {
        this.game = game;
    }

    @Override
    public void execute() {
        game.start();
    }

    @Override
    public void undo() {
        new GameEndCommand(this.game).execute();
    }
}
```

```java
public class GameEndCommand implements Command {

    private Game game;

    public GameEndCommand(Game game) {
        this.game = game;
    }

    @Override
    public void execute() {
        game.end();
    }

    @Override
    public void undo() {
        new GameStartCommand(this.game).execute();
    }
}
```

```java
public class LightOnCommand implements Command {

    private Light light;

    public LightOnCommand(Light light) {
        this.light = light;
    }

    @Override
    public void execute() {
        light.on();
    }

    @Override
    public void undo() {
        new LightOffCommand(this.light).execute();
    }
}
```

```java
public class LightOffCommand implements Command {

    private Light light;

    public LightOffCommand(Light light) {
        this.light = light;
    }

    @Override
    public void execute() {
        light.off();
    }

    @Override
    public void undo() {
        new LightOnCommand(this.light).execute();
    }
}
```

---

### 자바와 스프링에서 찾아보는 커맨드 패턴

- 자바
  - submit(Runnable task)를 통해 각각의 작업(Runnable)을 캡슐화했다.
    
```java
public class CommandInJava {

    public static void main(String[] args) {
        Light light = new Light();
        Game game = new Game();
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        executorService.submit(light::on);
        executorService.submit(game::start);
        executorService.submit(game::end);
        executorService.submit(light::off);
        executorService.shutdown();
    }
}
```

- 스프링
  - SimpleJdbcInsert 클래스의 execute를 통해 모든 실행 처리를 할 수 있다.

```java
public class CommandInSpring {

    private DataSource dataSource;

    public CommandInSpring(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void add(Command command) {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(dataSource)
                .withTableName("command")
                .usingGeneratedKeyColumns("id");

        Map<String, Object> data = new HashMap<>();
        data.put("name", command.getClass().getSimpleName());
        data.put("when", LocalDateTime.now());
        insert.execute(data);
    }

}
```