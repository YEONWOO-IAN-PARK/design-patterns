## 어댑터 패턴 (Adapter Pattern)
### 기존 코드를 클라이언트가 사용하는 인터페이스의 구현체로 바꿔주는 패턴
- 클라이언트가 사용하는 인터페이스를 따르지 않는 기존 코드를 재사용 할 수 있게 해준다.
  
- 어댑터 패턴 사용 전
  
```java
public class LoginHandler {

    UserDetailsService userDetailsService;

    public LoginHandler(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    public String login(String username, String password) {
        UserDetails userDetails = userDetailsService.loadUser(username);
        if (userDetails.getPassword().equals(password)) {
            return userDetails.getUsername();
        } else {
            throw new IllegalArgumentException();
        }
    }
}
```
  
```java
public interface UserDetailsService {

    UserDetails loadUser(String username);

}
```
  
```java
public interface UserDetails {

    String getUsername();

    String getPassword();

}
```

- 현재 사용 안됨  
  
```java
public class AccountService {

    public Account findAccountByUsername(String username) {
        Account account = new Account();
        account.setName(username);
        account.setPassword(username);
        account.setEmail(username);
        return account;
    }

    public void createNewAccount(Account account) {

    }

    public void updateAccount(Account account) {

    }

}
```
  
```java
public class Account {

    private String name;

    private String password;

    private String email;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
```

---

- 어댑터 패턴 적용 후
  - 기존 객체를 상속받은 어댑터 클래스를 생성했기 때문에 기존 코드를 변경할 필요가 없다.
    - UserDetails -> AccountUserDetails
    - UserDetailsService -> AccountUserDetailsService
  - 기존 코드의 재사용성을 높이고, 시스템의 결합도를 낮추는 효과적인 방법을 제공하여, 소프트웨어의 유지보수성과 확장성을 향상시킨다.
  
```java
public class App {

    public static void main(String[] args) {
        AccountService accountService = new AccountService();
        UserDetailsService userDetailsService = new AccountUserDetailsService(accountService);
        LoginHandler loginHandler = new LoginHandler(userDetailsService);
        String login = loginHandler.login("keesun", "keesun");
        System.out.println(login);
    }
}
```
  
```java
public class AccountUserDetailsService implements UserDetailsService {

    private AccountService accountService;

    public AccountUserDetailsService(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public UserDetails loadUser(String username) {
        return new AccountUserDetails(accountService.findAccountByUsername(username));
    }
}
```
  
```java
public class AccountUserDetails implements UserDetails {

    private Account account;

    public AccountUserDetails(Account account) {
        this.account = account;
    }

    @Override
    public String getUsername() {
        return account.getName();
    }

    @Override
    public String getPassword() {
        return account.getPassword();
    }
}
```

---

### 어댑터 패턴의 장단점

- 장점
  - 기존 코드를 변경하지 않고 원하는 인터페이스 구현체를 만들어 재사용 할 수 있다. (Open-Closed Principle; 확장에는 열려있고 변경에는 닫혀있음)
  - 기존 코드가 하던 일과 특정 인터페이스 구현체로 변환하는 작업을 각기 다른 클래스로 분리하여 관리할 수 있다.
- 단점
  - 새 클래스가 생겨 복잡도가 증가할 수 있다. 경우에 따라서는 기존 코드가 해당 인터페이스를 구현하도록 수정하는 것이 좋은 선택이 될 수도 있다.
