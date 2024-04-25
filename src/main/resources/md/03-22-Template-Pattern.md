## 템플릿 메서드 패턴 (Template Method Pattern)
### 알고리즘 구조를 서브 클래스가 확장 할 수 있도록 템플릿으로 제공하는 방법
- 추상 클래스는 템플릿을 제공하고 하위 클래스는 구체적인 알고리즘을 제공한다.

- 장점
  - 템플릿 코드를 재사용하고 중복 코드를 줄일 수 있다.
  - 템플릿 코드를 변경하지 않고 상속을 받아서 구체적인 알고리듬만 변경할 수 있다.
- 단점
  - 리스코프 치환 원칙을 위반할 수도 있다. 
  - 상속을 사용해서 process 메서드를 오버라이드하고 내용을 이상하게 만든다면? => 해결책 : final 키워드로 process() 의 규칙을 지켜낸다.
  - 알고리즘 구조가 복잡할 수록 템플릿을 유지하기 어려워진다.

## 템플릿 콜백 패턴 (Template Callback Pattern)
### 콜백으로 상속 대신 위임을 사용하는 템플릿 패턴
- 상속 대신 익명 내부 클래스 또는 람다 표현식을 활용할 수 있다.

- 적용 전
  - 작은 코드의 차이를 위해 FileProcessor 전체 클래스를 복사 해 코드의 중복이 심하다.
  - 새로운 기능을 추가하기 위해서는 비슷한 코드의 새로운 클래스를 계속 생성해야 한다.

```java
public class Client {

    public static void main(String[] args) {
        FileProcessor fileProcessor = new FileProcessor("number.txt");
        int result = fileProcessor.process();
        System.out.println(result);
    }
}
```

```java
public class FileProcessor {

    private String path;
    public FileProcessor(String path) {
        this.path = path;
    }

    public int process() {
        try(BufferedReader reader = new BufferedReader(new FileReader(path))) {
            int result = 0;
            String line = null;
            while((line = reader.readLine()) != null) {
                result += Integer.parseInt(line);
            }
            return result;
        } catch (IOException e) {
            throw new IllegalArgumentException(path + "에 해당하는 파일이 없습니다.", e);
        }
    }
}
```

```java
public class MultuplyFileProcessor {

    private String path;
    public MultuplyFileProcessor(String path) {
        this.path = path;
    }

    public int process() {
        try(BufferedReader reader = new BufferedReader(new FileReader(path))) {
            int result = 0;
            String line = null;
            while((line = reader.readLine()) != null) {
                result *= Integer.parseInt(line);
            }
            return result;
        } catch (IOException e) {
            throw new IllegalArgumentException(path + "에 해당하는 파일이 없습니다.", e);
        }
    }
}
```


- 적용 후
  - 추상 클래스를 사용한 템플릿 메서드 패턴을 적용해 코드를 재사용 할 수 있게 만들었다.
  - 템플릿 콜백 패턴을 사용해 런타임에 연산 방식을 변경 할 수 있게 되었다.
  - 템플릿 콜백 패턴을 사용해 Operator의 구현클래스로 다양한 알고리즘을 교체할 수 있다.

```java
public class Client {

    public static void main(String[] args) {
        FileProcessor fileProcessor = new Multiply("number.txt");
        int result = fileProcessor.process((sum, number) -> sum += number);
        System.out.println(result);
    }
}
```

```java
// Template Method pattern을 위한 Concrete Class
public class Multiply extends FileProcessor {
    public Multiply(String path) {
        super(path);
    }

    @Override
    protected int getResult(int result, int number) {
        return result *= number;
    }

}
```

```java
// Template Callback pattern을 위한 Concrete Callback
public class Plus implements Operator {
    @Override
    public int getResult(int result, int number) {
        return result += number;
    }
}
```

```java
// Template Method pattern을 위한 Abstract Class
public abstract class FileProcessor {

    private String path;
    public FileProcessor(String path) {
        this.path = path;
    }

    public final int process(Operator operator) {
        try(BufferedReader reader = new BufferedReader(new FileReader(path))) {
            int result = 0;
            String line = null;
            while((line = reader.readLine()) != null) {
                result = getResult(result, Integer.parseInt(line));
            }
            return result;
        } catch (IOException e) {
            throw new IllegalArgumentException(path + "에 해당하는 파일이 없습니다.", e);
        }
    }

    protected abstract int getResult(int result, int number);

}
```

```java
// Callback Interface
public interface Operator {

    abstract int getResult(int result, int number);
}
```


---

### 자바와 스프링에서 찾아보는 템플릿 메서드 패턴

- 자바의 HttpServlet 
  - HttpServlet이 제공하는 doGet, doPost 메서드를 오버라이딩하여 사용할 수 있다.
  - 즉, 제공된 템플릿에서 추가로 사용자 정의 로직을 작성할 수 있다.
  - 전체적인 흐름을 제어하면서도 실제 요청의 처리는 개별 서블릿 클래스에 의해 결정된다. (제어의역전)
```java
public class MyHello extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
```


- 스프링에서 제공하는 템플릿 메서드 패턴
  - WebSecurityConfigurerAdapter 의 configure : 해당 메서드를 오버라이딩함으로써 세부 사항에 사용자 정의 로직을 추가할 수 있다.

- 스프링에서 제공하는 템플릿 콜백 패턴
  - JdbcTemplate : 위임을 사용하여 특정 인터페이스를 구현해 execute 메서드를 실행한다.
  - RestTemplate : exchange 메서드를 통해 개발자는 응답받을 타입만 지정하면 된다. 내부적으로는 다양한 방식으로 응답을 파싱하고 처리하는 여러 전략이 콜백 형태로 구현되어 있다.


```java
public class TemplateInSpring {

    public static void main(String[] args) {
        // TODO 템플릿-콜백 패턴
        // JdbcTemplate
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.execute("insert");

        // RestTemplate
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.set("X-COM-PERSIST", "NO");
        headers.set("X-COM-LOCATION", "USA");

        HttpEntity<String> entity = new HttpEntity<String>(headers);
        ResponseEntity<String> responseEntity = restTemplate
                .exchange("http://localhost:8080/users", HttpMethod.GET, entity, String.class);
    }

    @Configuration
    class SecurityConfig extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.authorizeRequests().anyRequest().permitAll();
        }
    }
}
```