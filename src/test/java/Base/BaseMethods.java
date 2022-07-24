package Base;

import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.time.LocalDate;
import java.util.Properties;


public class BaseMethods {

    public final static String project = "HadrianExercise";

    //public RemoteWebDriver driver;
    public final static String home = System.getProperty("user.home");
    private final LocalDate now = LocalDate.now();
    private final String today = now.toString();

    public String reportFilePath(String project, String environmentLabel, String reportType, String extension) {
        String reportFilePath = home + "\\IdeaProjects\\".concat(project).concat("\\TestResults\\").concat(environmentLabel).concat(reportType).concat("-").concat(today).concat(extension);
        return reportFilePath;
    }


    public String driverFilePath(String project, String driver) {
        String driverFilePath = home + "\\IdeaProjects\\".concat(project).concat("\\Drivers\\").concat(driver);
        return driverFilePath;
    }

    @Parameters({"project","environmentLabel", "reportType", "extension", "recipient", "subject", "body"})
    public void sendEmail(@Optional("qa-automation") String project, @Optional("n/a") String environmentLabel, @Optional("n/a") String reportType, @Optional(".html")String extension, @Optional("n/a")String recipient, @Optional("n/a")String subject, @Optional("n/a")String body) {
        String to = recipient;
        String from = "chorusqa@gmail.com";

        //Get the session object
        Properties props = System.getProperties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("chorusqa@gmail.com", "opbpvpsaxhznrizd");
            }
        });

        //compose the message
        try{
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO,new InternetAddress(to));
            message.setSubject(subject);

            //send attachment
            Multipart multipart = new MimeMultipart();
            BodyPart messageBodyPart1 = new MimeBodyPart();
            String filename = reportFilePath(project,environmentLabel, reportType, extension);
            DataSource source = new FileDataSource(filename);
            messageBodyPart1.setDataHandler(new DataHandler(source));
            messageBodyPart1.setFileName(filename);
            BodyPart messageBodyPart2 = new MimeBodyPart();
            messageBodyPart2.setText(body);
            multipart.addBodyPart(messageBodyPart1);
            multipart.addBodyPart(messageBodyPart2);
            message.setContent(multipart);

            // Send message
            Transport.send(message);
            System.out.println("message sent successfully....");
            System.out.println("EMAIL TO " + recipient + " CREATED SUCCESSFULLY!!!");

        }catch (MessagingException mex) {mex.printStackTrace();}
    }
}
