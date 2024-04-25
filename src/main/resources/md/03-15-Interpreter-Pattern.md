## 인터프리터 패턴 (Interpreter Pattern)
### 자주 등장하는 문제를 간단한 언어로 정의하고 재사용하는 패턴
- 반복되는 문제 패턴을 언어 또는 문법으로 정의하고 확장할 수 있다.

- 장점
  - 자주 등장하는 문제 패턴을 얻어 문법을 정의 할 수 있다.
  - 기존 코드를 변경하지 않고 새로운 Expression을 추가할 수 있다.
- 단점
  - 복잡한 문법을 표현하려면 Expression과 Parser가 복잡해진다.


- 적용 전
  - 간단한 계산은 할 수 있으나 새로운 연산이 추가되거나 표현식의 규칙이 변경될 경우 코드 수정이 필요하다.

```java
public class PostfixNotation {

  private final String expression;

  public PostfixNotation(String expression) {
    this.expression = expression;
  }

  public static void main(String[] args) {
    PostfixNotation postfixNotation = new PostfixNotation("123+-");
    postfixNotation.calculate();
  }

  private void calculate() {
    Stack<Integer> numbers = new Stack<>();

    for (char c : this.expression.toCharArray()) {
      switch (c) {
        case '+':
          numbers.push(numbers.pop() + numbers.pop());
          break;
        case '-':
          int right = numbers.pop();
          int left = numbers.pop();
          numbers.push(left - right);
          break;
        default:
          numbers.push(Integer.parseInt(c + ""));
      }
    }

    System.out.println(numbers.pop());
  }
}
```





- 적용 후
  - 문제를 표현식으로 정의하고 해당 표현식을 처리하는 파서를 생성해 표현식 별 파싱 방법을 정의해서 같은 방식의 문제 패턴을 유연하게 처리할 수 있다.
  - 반복적으로 재사용할 수 있는 특정 패턴의 해결방법을 구현했지만 복잡도가 크게 상승했다.
  - 해당 구조를 AST(Abstract Syntax Tree)라고 한다.

```java
public class App {

  public static void main(String[] args) {
    // 표현식을 PostFix 연산식으로 파싱한다.
    PostfixExpression expression = PostfixParser.parse("xyz+-a+");
    int result = expression.interpret(Map.of('x', 1, 'y', 2, 'z', 3, 'a', 4));
    System.out.println(result);
  }
}
```

```java
public class PostfixParser {

  public static PostfixExpression parse(String expression) {
    Stack<PostfixExpression> stack = new Stack<>();
    for (char c : expression.toCharArray()) {
      stack.push(getExpression(c, stack));
    }
    return stack.pop();
  }

  private static PostfixExpression getExpression(char c, Stack<PostfixExpression> stack) {
    switch (c) {
      case '+':
        return new PlusExpression(stack.pop(), stack.pop());
      case '-':
        PostfixExpression right = stack.pop();
        PostfixExpression left = stack.pop();
        return new MinusExpression(left, right);
      default:
        return new VariableExpression(c);
    }
  }
}
```

```java
public interface PostfixExpression {

  int interpret(Map<Character, Integer> context);

}
```

```java
public class PlusExpression implements PostfixExpression {

  private PostfixExpression left;

  private PostfixExpression right;

  public PlusExpression(PostfixExpression left, PostfixExpression right) {
    this.left = left;
    this.right = right;
  }

  @Override
  public int interpret(Map<Character, Integer> context) {
    return left.interpret(context) + right.interpret(context);
  }
}
```

```java
public class MinusExpression implements PostfixExpression {

  private PostfixExpression left;

  private PostfixExpression right;

  public MinusExpression(PostfixExpression left, PostfixExpression right) {
    this.left = left;
    this.right = right;
  }

  @Override
  public int interpret(Map<Character, Integer> context) {
    return left.interpret(context) - right.interpret(context);
  }
}
```

```java
public class VariableExpression implements PostfixExpression {

  private Character character;

  public VariableExpression(Character character) {
    this.character = character;
  }

  @Override
  public int interpret(Map<Character, Integer> context) {
    return context.get(this.character);
  }
}
```

---
### 자바와 스프링에서 찾아보는 인터프리터 패턴

- 스프링의 SpEl (Spring Expression Language)
  - 스프링에서 제공하는 SpEl 클래스를 사용해서 강력한 기능을 사용할 수 있다.

```java
public class InterpreterInSpring {

  public static void main(String[] args) {
    Book book = new Book("spring");

    ExpressionParser parser = new SpelExpressionParser();
    Expression expression = parser.parseExpression("title");
    System.out.println(expression.getValue(book));
  }
}
```

- 자바
  - Pattern의 matches(regex, string)을 사용하여 전달된 표현식에 맞는 결과를 반환한다.

```java
public class InterpreterInJava {

  public static void main(String[] args) {
    System.out.println(Pattern.matches(".pr...", "spring"));
    System.out.println(Pattern.matches("[a-z]{6}", "spring"));
    System.out.println(Pattern.matches("white[a-z]{4}[0-9]{4}", "whiteship2000"));
    System.out.println(Pattern.matches("\\d", "1")); // one digit
    System.out.println(Pattern.matches("\\D", "a")); // one non-digit
  }
}
```