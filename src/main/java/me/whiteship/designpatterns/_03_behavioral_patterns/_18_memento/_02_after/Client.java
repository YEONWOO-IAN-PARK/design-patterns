package me.whiteship.designpatterns._03_behavioral_patterns._18_memento._02_after;

// Client : CareTaker에 해당함
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
