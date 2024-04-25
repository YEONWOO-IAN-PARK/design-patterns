package me.whiteship.designpatterns._03_behavioral_patterns._15_interpreter._02_after;

import java.util.Map;

public class App {

    public static void main(String[] args) {
        // 표현식을 PostFix 연산식으로 파싱한다.
        PostfixExpression expression = PostfixParser.parse("xyz+-a+");
        int result = expression.interpret(Map.of('x', 1, 'y', 2, 'z', 3, 'a', 4));
        System.out.println(result);
    }
}
