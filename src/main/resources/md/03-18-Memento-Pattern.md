## 메멘토 패턴 (Memento Pattern)
### 캡슐화를 유지하면서 객체 내부 상태를 외부에 저장하는 방법
- 객체 상태를 외부에 저장했다가 해당 상태로 다시 복구할 수 있다.

- 장점
  - 캡슐화를 지키면서 객체 상태 스냅샷을 만들 수 있다.
  - 객체 상태를 저장하고 또 복원하는 역할을 CareTaker 에게 위임할 수 있다.
  - 객체 상태가 바뀌어도 클라이언트 코드는 변경되지 않는다.

- 단점
  - 많은 정보를 저장하는 Memento 를 자주 생성하는 경우 메모리 사용량에 많은 영향을 줄 수 있다.


- 적용 전
  - Game 객체의 상태를 직접 접근하여 수정하고 조회하는 방식으로, 캡슐화 원칙을 위반한다.
  - 객체의 상태를 관리하는 로직이 클라이언트 코드에 분산되어 있어, 객체의 상태 변경 시 클라이언트의 코드도 수정해야 한다.
  - 객체의 상태 복원 로직이 클라이언트에 의존하고 있다.

```java
public class Client {

    public static void main(String[] args) {
        Game game = new Game();
        game.setRedTeamScore(10);
        game.setBlueTeamScore(20);

        int blueTeamScore = game.getBlueTeamScore();
        int redTeamScore = game.getRedTeamScore();

        Game restoredGame = new Game();
        restoredGame.setBlueTeamScore(blueTeamScore);
        restoredGame.setRedTeamScore(redTeamScore);
    }
}
```

```java
public class Game implements Serializable {

    private int redTeamScore;

    private int blueTeamScore;

    public int getRedTeamScore() {
        return redTeamScore;
    }

    public void setRedTeamScore(int redTeamScore) {
        this.redTeamScore = redTeamScore;
    }

    public int getBlueTeamScore() {
        return blueTeamScore;
    }

    public void setBlueTeamScore(int blueTeamScore) {
        this.blueTeamScore = blueTeamScore;
    }
}
```


- 적용 후
  - Game 객체의 상태 관리는 완전히 캡슐화 되어 외부에서 직접 접근할 수 없다.
  - 객체의 상태를 GameSave 라는 메멘토 객체에 의해 스냅샷으로 저장하고, 이를 통해 이전 상태로 복원할 수 있어 캡슐화 원칙을 유지한다.
  - GameSave 객체는 Game 객체의 상태를 불변 객체로 저장해 상태정보의 안전한 전달과 복원을 보장한다.


```java
// CareTaker
public class Client {

    public static void main(String[] args) {
        // Game : Originator에 해당
        Game game = new Game();
        game.setBlueTeamScore(10);
        game.setRedTeamScore(20);

        // Memento에 해당
        GameSave save = game.save();    // 객체 상태 스냅샷을 생성 (game 객체와 내부 정보는 같지만 별개의 객체임)

        game.setBlueTeamScore(12);
        game.setRedTeamScore(22);

        game.restore(save);

        System.out.println(game.getBlueTeamScore());
        System.out.println(game.getRedTeamScore());
    }
}
```

```java
// Memento
public final class GameSave {

    private final int blueTeamScore;

    private final int redTeamScore;

    public GameSave(int blueTeamScore, int redTeamScore) {
        this.blueTeamScore = blueTeamScore;
        this.redTeamScore = redTeamScore;
    }

    public int getBlueTeamScore() {
        return blueTeamScore;
    }

    public int getRedTeamScore() {
        return redTeamScore;
    }
}
```

```java
// Originator
public class Game {

    private int redTeamScore;

    private int blueTeamScore;

    public int getRedTeamScore() {
        return redTeamScore;
    }

    public void setRedTeamScore(int redTeamScore) {
        this.redTeamScore = redTeamScore;
    }

    public int getBlueTeamScore() {
        return blueTeamScore;
    }

    public void setBlueTeamScore(int blueTeamScore) {
        this.blueTeamScore = blueTeamScore;
    }

    public GameSave save() {
        return new GameSave(this.blueTeamScore, this.redTeamScore);
    }

    public void restore(GameSave gameSave) {
        this.blueTeamScore = gameSave.getBlueTeamScore();
        this.redTeamScore = gameSave.getRedTeamScore();
    }

}
```


---
### 자바와 스프링에서 찾아보는 메멘토 패턴
