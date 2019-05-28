/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.web.util;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author Administrador
 */
public class MailUtil {
    
    private String mailServer;
    private int portMailServer;
    private String fromMailAddress;

    public MailUtil(String mailServer, String portMailServer, String fromMailAddress) {
        this.mailServer = mailServer;
        this.portMailServer = Integer.parseInt(portMailServer);
        this.fromMailAddress = fromMailAddress;
    }    

    public void sendEmail(String aToEmailAddr, String aSubject, String aBody) {
        //Here, no Authenticator argument is used (it is null).
        //Authenticators are used to prompt the user for user
        //name and password.
        Properties properties = new Properties();
        properties.put("mail.smtp.host", mailServer);
        properties.put("mail.smtp.port", portMailServer);
        properties.put("mail.smtp.auth", "false");
        /*properties.put("mail.smtp.user", "controlt_notificaciones@aerocivil.gov.co");
         properties.put("mail.smtp.password", "7uRnos2013");*/
        //Authenticator a1= new PopupAuthenticator();
        //p.setProperty()
        //Session session = Session.getDefaultInstance( fMailServerConfig, null );
        Session session = Session.getDefaultInstance(properties, null);
        try {
            MimeMessage message = new MimeMessage(session);

            //the "from" address may be set in code, or set in the
            //config file under "mail.from" ; here, the latter style is used
            message.setFrom( new InternetAddress(fromMailAddress) );
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(aToEmailAddr));
            message.setSubject(aSubject);
            message.setText(aBody);
            Transport.send(message);
        } catch (MessagingException ex) {
            System.err.println("Cannot send email. " + ex);
        }
    }
}
