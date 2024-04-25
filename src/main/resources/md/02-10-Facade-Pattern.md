## 퍼사드 패턴 (Facade Pattern)
### 복잡한 서브 시스템 의존성을 최소화하는 방법
- 클라이언트가 사용해야 하는 복잡한 서브 시스템 의존성을 간단한 인터페이스로 추상화 할 수 있다.

- 장점
  - 서브 시스템에 대한 의존성을 한 곳으로 모을 수 있다.
- 단점
  - 퍼사드 클래스가 서브 시스템에 대한 모든 의존성을 가지게 된다.

- 퍼사드 패턴 적용 전
  - 클라이언트에서 메일 전송 관련 코드들을 그대로 노출한다.
  - 클라이언트에서 저수준의 메일 전송 관련 의존성이 강하게 결합하고 있다.
  
```java
public class Client {

    public static void main(String[] args) {
        String to = "keesun@whiteship.me";
        String from = "whiteship@whiteship.me";
        String host = "127.0.0.1";

        Properties properties = System.getProperties();
        properties.setProperty("mail.smtp.host", host);

        Session session = Session.getDefaultInstance(properties);

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject("Test Mail from Java Program");
            message.setText("message");

            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
```


  
- 퍼사드 패턴 적용 후
  - EmailSender 클래스를 통해 클라이언트는 복잡한 메일 전송 과정을 간단한 인터페이스로 사용할 수 있게 되었다.
  - 이로 인해 클라이언트는 메일을 보내는데 필요한 세부적인 설정이나 절차를 신경쓸 필요 없이 EmailSettings, EmailMessage 정보만 설정해 넘겨주기만 하면 된다.
  
```java
public class Client {

    public static void main(String[] args) {
        EmailSettings emailSettings = new EmailSettings();
        emailSettings.setHost("127.0.0.1");

        EmailSender emailSender = new EmailSender(emailSettings);

        EmailMessage emailMessage = new EmailMessage();
        emailMessage.setFrom("keesun");
        emailMessage.setTo("whiteship");
        emailMessage.setCc("일남");
        emailMessage.setSubject("오징어게임");
        emailMessage.setText("밖은 더 지옥이더라고..");

        emailSender.sendEmail(emailMessage);
    }
}
```

```java
public class EmailSender {

    private EmailSettings emailSettings;

    public EmailSender(EmailSettings emailSettings) {
        this.emailSettings = emailSettings;
    }

    /**
     * 이메일 보내는 메소드
     * @param emailMessage
     */
    public void sendEmail(EmailMessage emailMessage) {
        Properties properties = System.getProperties();
        properties.setProperty("mail.smtp.host", emailSettings.getHost());

        Session session = Session.getDefaultInstance(properties);

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(emailMessage.getFrom()));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(emailMessage.getTo()));
            message.addRecipient(Message.RecipientType.CC, new InternetAddress(emailMessage.getCc()));
            message.setSubject(emailMessage.getSubject());
            message.setText(emailMessage.getText());

            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
```
  
```java
public class EmailSettings {

    private String host;

    // Getter & Setter
}
```
  
```java
public class EmailMessage {

    private String from;

    private String to;
    private String cc;
    private String bcc;

    private String subject;

    private String text;

    // Getter & Setter
}
```


---
- 현재 퍼사드 패턴의 구조 개선을 한다면?
  - EmailSender를 대신하여 EmailService라는 인터페이스를 생성하고 추상 메서드로 sendEmail(EmailMessage em)를 정의한다.
  - EmailService를 구현한 JavaEmailService 구현 클래스를 생성해 기존 EmailSender 클래스를 대체한다.
  - 다른 타입의 메일 발송을 개발하더라도 EmailService를 구현해서 사용할 수 있다.