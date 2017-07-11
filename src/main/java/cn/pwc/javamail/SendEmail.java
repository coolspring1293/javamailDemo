package cn.pwc.javamail;

import com.sun.mail.util.MailSSLSocketFactory;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * Created by 26229 on 2017/4/25.
 */
public class SendEmail {

    private static SendEmail sendEmail;

    private static Properties properties = System.getProperties();

    private MyAuthenticator authenticator;

    private Session session;

    private SendEmail(){
    }

    /**
     * 初始化
     * @param username
     * @param password
     * @param host
     */
    private SendEmail(String username, String password, String host){
        //因为有些邮箱服务器是需要开启ssl加密的（现在知道qq需要）得另外去开启
        Integer beginIndex = host.indexOf(".")+1;
        Integer endIndex = host.lastIndexOf(".");
        if(host.substring(beginIndex,endIndex).equals("qq")){
            ssl();
        }
        //身份凭证
        authenticator = new MyAuthenticator(username,password);
        properties.put("mail.smtp.host",host);
        properties.put("mail.smtp.auth","true");
        session = Session.getDefaultInstance(properties,authenticator);
        session.setDebug(true);
    }

    public static SendEmail getSendEmail() throws IOException{
        //加载mail的配置文件
        InputStream inputStream = SendEmail.class.getClassLoader().getResourceAsStream("mail.properties");
        properties.load(inputStream);
        //生成sendEmail实例并初始化
        sendEmail = new SendEmail(properties.getProperty("username"),properties.getProperty("password"),properties.getProperty("server"));
        return sendEmail;
    }

    /**
     * 开启ssl加密
     */
    private void ssl(){
        MailSSLSocketFactory sf = null;
        try {
            sf = new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);
            properties.put("mail.smtp.ssl.enable","true");
            properties.put("mail.smtp.ssl.socketFactory",sf);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
    }

    /**
     * 简单的邮件发送，发送给一个人，无附件
     * @param to
     * @param theme
     * @param content
     * @throws Exception
     */
    public void send(String to, String theme, String content) throws Exception{
        //创建邮件实体
        Message message = new MimeMessage(session);
        message.addRecipient(Message.RecipientType.TO,new InternetAddress(to));
        message.setFrom(new InternetAddress(authenticator.getUsername()));
        message.setSubject(theme);
        message.setSentDate(new Date());
        message.setContent(content,"text/html;charset=utf-8");
        Transport.send(message);
    }

    /**
     * 邮件发送，多人，有附件
     * @param to
     * @param theme
     * @param content
     * @param file
     * @throws Exception
     */
    public void attachments(List<String> to, String theme, String content, List<String> file) throws Exception{
        //把String类型转为InternetAddress
        InternetAddress[] addresses = new InternetAddress[to.size()];
        Integer flag = 0;
        for (String add : to){
            addresses[flag] = new InternetAddress(add);
            flag++;
        }
        //将附件和内容放进Multipart
        Multipart multipart = new MimeMultipart();
        for (String path : file){
            MimeBodyPart bodyPart = new MimeBodyPart();
            FileDataSource fileDataSource = new FileDataSource(path);
            bodyPart.setDataHandler(new DataHandler(fileDataSource));
            bodyPart.setFileName(fileDataSource.getName());
            multipart.addBodyPart(bodyPart);
        }
        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        mimeBodyPart.setText(content);
        multipart.addBodyPart(mimeBodyPart);
        //创建消息实体
        Message message = new MimeMessage(session);
        message.setSubject(theme);
        message.setFrom(new InternetAddress(authenticator.getUsername()));
        message.addRecipients(Message.RecipientType.TO,addresses);
        message.setContent(multipart);
        message.setSentDate(new Date());
        Transport.send(message);
    }

}
