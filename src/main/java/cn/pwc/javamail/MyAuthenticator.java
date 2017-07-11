package cn.pwc.javamail;


import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 * Created by 26229 on 2017/4/25.
 */
public class MyAuthenticator extends Authenticator {

    private String username;

    private String password;

    public MyAuthenticator() {
    }

    public MyAuthenticator(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    //必须实现该方法
    @Override
    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(username,password);
    }
}
