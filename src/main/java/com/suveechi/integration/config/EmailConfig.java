/*
 * package com.suveechi.integration.config;
 * 
 * import java.util.Properties;
 * 
 * import org.springframework.beans.factory.annotation.Autowired; import
 * org.springframework.beans.factory.annotation.Value; import
 * org.springframework.context.annotation.Bean; import
 * org.springframework.mail.javamail.JavaMailSender; import
 * org.springframework.mail.javamail.JavaMailSenderImpl; import
 * org.springframework.mail.javamail.MimeMessageHelper;
 * 
 * import jakarta.mail.MessagingException; import
 * jakarta.mail.internet.MimeMessage;
 * 
 * public class EmailConfig {
 * 
 * @Autowired private JavaMailSender mailSender;
 * 
 * @Value("${spring.mail.host}") private String host;
 * 
 * @Value("${spring.mail.port}") private int port;
 * 
 * @Value("${spring.mail.username}") private String username;
 * 
 * @Value("${spring.mail.password}") private String password;
 * 
 * @Bean public JavaMailSender javaMailSender() { JavaMailSenderImpl mailSender
 * = new JavaMailSenderImpl(); mailSender.setHost(host);
 * mailSender.setPort(port); mailSender.setUsername(username);
 * mailSender.setPassword(password);
 * 
 * Properties props = mailSender.getJavaMailProperties();
 * props.put("mail.transport.protocol", "smtp"); props.put("mail.smtp.auth",
 * "true"); props.put("mail.smtp.starttls.enable", "true");
 * props.put("mail.debug", "true");
 * 
 * System.out.println("### JavaMailSender Properties ###");
 * System.out.println("Host: " + mailSender.getHost());
 * System.out.println("Port: " + mailSender.getPort());
 * System.out.println("Username: " + mailSender.getUsername());
 * System.out.println("Properties: " + props);
 * System.out.println("#################################");
 * 
 * return mailSender; }
 * 
 * public void sendEmail(String to, String username, String password) throws
 * MessagingException { // Create a MimeMessage MimeMessage message =
 * javaMailSender().createMimeMessage(); MimeMessageHelper helper = new
 * MimeMessageHelper(message, true);
 * 
 * // Email Details String subject = "JSSL LOGIN LINK "; String content =
 * "<p>Hello " + username + ",</p>" + "<p>Here are your account details:</p>" +
 * "<ul>" + "<li><strong>Username:</strong> " + username + "</li>" +
 * "<li><strong>Password:</strong> " + password + "</li>" + "</ul>";
 * helper.setTo(to); helper.setSubject(subject); helper.setText(content, true);
 * // true = HTML content
 * 
 * // Send the email // mailSender.send(message);
 * javaMailSender().send(message); }
 * 
 * public void sendEmailLink(String to, String username, String password, String
 * link) throws MessagingException { // Create a MimeMessage MimeMessage message
 * = mailSender.createMimeMessage(); MimeMessageHelper helper = new
 * MimeMessageHelper(message, true);
 * 
 * // Email Details String subject = "JSSL LOGIN LINK "; String content =
 * "<p>Hello " + username + ",</p>" + "<p>Here are your account details:</p>" +
 * "<ul>" + "<li><strong>Username:</strong> " + username + "</li>" +
 * "<li><strong>Password:</strong> " + password + "</li>" + "</ul>" +
 * "<p>Please use the following link to log in:</p>" + "<p><a href='" + link +
 * "'>" + link + "</a></p>" + "<p>Thank you!</p>";
 * 
 * helper.setTo(to); helper.setSubject(subject); helper.setText(content, true);
 * // true = HTML content
 * 
 * // Send the email mailSender.send(message); }
 * 
 * }
 */
package com.suveechi.integration.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Configuration
public class EmailConfig {

    @Autowired
    private JavaMailSender mailSender;
    
//    public void sendEmail(String to, String username, String password) throws MessagingException, IOException {
//        String clientId = "<your-client-id>";
//        String clientSecret = "<your-client-secret>";
//        String tenantId = "<your-tenant-id>";
//
//        try {
//        	  String accessToken = OAuth2TokenProvider.getAccessToken(clientId, clientSecret, tenantId);
//
//              MimeMessage mimeMessage = mailSender.createMimeMessage();
//              MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
//
//              helper.setFrom("<your-email@example.com>");
//              helper.setTo(to);
//              helper.setSubject("Your Account Details");
//              helper.setText("Hello " + username + ",\n\nYour account has been created. Your credentials are:\n" +
//                      "Username: " + username + "\nPassword: " + password + "\n\nPlease change your password after the first login.", true);
//
//              mimeMessage.setHeader("Authorization", "Bearer " + accessToken);
//              mailSender.send(mimeMessage);
//        }catch (Exception e) {
//			// TODO: handle exception
//		}
//      
//    }
    public int sendEmail(String to, String username, String password) throws MessagingException {
    	 try {
    	        MimeMessage message = mailSender.createMimeMessage();
    	        MimeMessageHelper helper = new MimeMessageHelper(message, true);
    	        helper.setFrom("alerts@jssl.in");
    	        
    	        String subject = "JSSL CREDENTIALS GPMS";
    	        String content = "<p>Hello " + username + ",</p>"
    	                + "<p>Here are your account details:</p>"
    	                + "<ul>"
    	                + "<li><strong>Username:</strong> " + username + "</li>"
    	                + "<li><strong>Password:</strong> " + password + "</li>"
    	                + "</ul>";

    	        helper.setTo(to);
    	        helper.setSubject(subject);
    	        helper.setText(content, true); // true = HTML content

    	        mailSender.send(message);
    	        return 1; // Success
    	    } catch (MessagingException e) {
    	        e.printStackTrace();
    	        return 0; // Failure
    	    }
    }

    public void sendEmailLink(String to, String username, String password) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        // Email Details
        String subject = "JSSL LOGIN LINK ";
        String content = "<p>Hello " + username + ",</p>"
                + "<p>Here are your account details:</p>"
                + "<ul>"
                + "<li><strong>Username:</strong> " + username + "</li>"
                + "<li><strong>Password:</strong> " + password + "</li>"
                + "</ul>"
          //      + "<p>Please use the following link to log in:</p>"
           //     + "<p><a href='" + link + "'>" + link + "</a></p>"
                + "<p>Thank you!</p>";
        helper.setFrom("alerts@jssl.in");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(content, true); // true = HTML content

        // Send the email
        mailSender.send(message);
    }
    
//    public static void main(String[] args) {
//    	EmailConfig cc = new EmailConfig();
//    	try {
//			cc.sendEmail1("1", "1","1");
//		} catch (MessagingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
    
   }

