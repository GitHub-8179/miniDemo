package com.sjc.mail;


import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

public class SendMail {
    private String username = null;
    private String password = null;
    private Authenticator auth = null;
    private MimeMessage mimeMessage =null;
    private Properties pros = null;
    private Multipart multipart = null;
    private BodyPart bodypart= null;
    /**
     * 初始化账号密码并验证
     * 创建MimeMessage对象
     * 发送邮件必须的步骤:1
     * @param username
     * @param password
     */
    public SendMail(String username,String password){
        this.username = username;
        this.password = password;
    } 
    /**
     * 初始化MimeMessage对象
     * 发送邮件必须的步骤:3
     */
    public void initMessage(){
        this.auth = new Email_Autherticator();
        Session session = Session.getDefaultInstance(pros,auth);
        session.setDebug(true); //设置获取 debug 信息
        mimeMessage = new MimeMessage(session);
    }
    /**
     * 设置email系统参数
     * 接收一个map集合key为string类型，值为String
     * 发送邮件必须的步骤:2
     * mail.smtp.user SMTP的缺省用户名。 
	mail.smtp.host 要连接的SMTP服务器。
	mail.smtp.port 要连接的SMTP服务器的端口号，如果connect没有指明端口号就使用它，缺省值25。
	mail.smtp.connectiontimeout Socket连接超时值，单位毫秒，缺省值不超时。
	mail.smtp.timeout Socket I/O超时值，单位毫秒，缺省值不超时。
	mail.smtp.from SMTP MAIL使用的Email地址，用来设置邮件的return地址。缺省是Message.getFrom()或InternetAddress.getLocalAddress()。注意：mail.smtp.user优先使用
	mail.smtp.localhost localhost名，缺省是InetAddress.getLocalHost().getHostName()。如果JDK和name service正确配置，一般不需设置。
	mail.smtp.ehlo 如果为false，那么不会尝试使用EHLO命令登录，缺省是true。通常EHLO命令失败，会倒退到HELO命令。这个属性只有在服务器没有fail EHLO属性或没有实现EHLO属性。
	mail.smtp.auth 缺省是false，如果为true，尝试使用AUTH命令认证用户。
	mail.host 邮件交互的主机。 mail.transport.protocol 要装入session的协议（smtp、pop3、imap、nntp）。
	mail.user 登录邮件服务器的用户名（发送邮件时需要）。
	mail.from 发件人地址（发送邮件时需要）。
     * @param map
     */
    public void setPros(Map<String,String> map){
        pros = new Properties();
        for(Map.Entry<String,String> entry:map.entrySet()){
            pros.setProperty(entry.getKey(), entry.getValue());
        }
    }
    /**
     * 验证账号密码
     * 发送邮件必须的步骤
     * @author Administrator
     *
     */
    public class Email_Autherticator extends Authenticator
    {
        public PasswordAuthentication getPasswordAuthentication()
        {
            return new PasswordAuthentication(username, password);
        }
    }
    /**
     * 设置发送邮件的基本参数(去除繁琐的邮件设置)
     * @param sub 设置邮件主题
     * @param text 设置邮件文本内容
     * @param rec 设置邮件接收人
     * @throws MessagingException
     * @throws UnsupportedEncodingException
     */
    public void setDefaultMessagePros(String sub,String text,String rec) throws MessagingException, UnsupportedEncodingException{
        mimeMessage.setSubject(sub);
        mimeMessage.setText(text);
        mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(rec));
        mimeMessage.setSentDate(new Date());
        mimeMessage.setFrom(new InternetAddress(username,username));
    }
    /**
     * 设置主题
     * @param subject
     * @throws MessagingException 
     */
    public void  setSubject(String subject) throws MessagingException{
        mimeMessage.setSubject(subject);
    }
    /**
     * 设置日期
     * @param date
     * @throws MessagingException 
     */
    public void  setDate(Date date) throws MessagingException{
        mimeMessage.setSentDate(new Date());
    }
    /**
     * 设置邮件文本内容
     * @param text
     * @throws MessagingException
     */
    public void setText(String text) throws MessagingException{
        mimeMessage.setText(text);
    }
    /**
     * 设置邮件头部
     * @param arg0
     * @param arg1
     * @throws MessagingException
     */
    public void setHeader(String arg0,String arg1) throws MessagingException{
        mimeMessage.setHeader(arg0, arg1);
    }
    /**
     * 设置邮件接收人地址 <单人发送>
     * @param recipient
     * @throws MessagingException
     */
    public void setRecipient(String recipient) throws MessagingException{
        mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
    }
    
    /**
     * 设置邮件接收人地址 <多人发送>
     * @param list
     * @throws MessagingException 
     * @throws AddressException 
     *  // 3. To: 收件人
        message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress("cc@receive.com", "USER_CC", "UTF-8"));
        //    To: 增加收件人（可选）
        message.addRecipient(MimeMessage.RecipientType.TO, new InternetAddress("dd@receive.com", "USER_DD", "UTF-8"));
        //    Cc: 抄送（可选）
        message.setRecipient(MimeMessage.RecipientType.CC, new InternetAddress("ee@receive.com", "USER_EE", "UTF-8"));
        //    Bcc: 密送（可选）
        message.setRecipient(MimeMessage.RecipientType.BCC, new InternetAddress("ff@receive.com", "USER_FF", "UTF-8"));

     */
    public String setRecipients(List<String> recs) throws AddressException, MessagingException{
        if(recs.isEmpty()){
            return "接收人地址为空!";
        }
        for(String str:recs){
            mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(str));
        }
        return "加入接收人地址成功!";
    }
    /**
     * 设置邮件接收人地址 <多人发送>
     * @param StringBuffer<parms,parms2,parms.....>
     * @throws MessagingException 
     * @throws AddressException 
     */
    @SuppressWarnings("static-access")
    public String setRecipients(StringBuffer sb) throws AddressException, MessagingException{
        if(sb==null||"".equals(sb)){
            return "字符串数据为空!";
        }
        Address []address = new InternetAddress().parse(sb.toString());
        mimeMessage.addRecipients(Message.RecipientType.TO, address);
        return "收件人加入成功";
    }
    /**
     * 设置邮件发送人的名字
     * @param from
     * @throws UnsupportedEncodingException
     * @throws MessagingException
     */
    public void setFrom(String from) throws UnsupportedEncodingException, MessagingException{
        mimeMessage.setFrom(new InternetAddress(username,from));
    }
    /**
     * 发送邮件<单人发送>
     * return 是否发送成功
     * @throws MessagingException 
     */
    public String sendMessage() throws MessagingException{
        Transport.send(mimeMessage);
        return "success";
    }
    /**
     * 设置附件
     * @param file 发送文件的路径
     */
    public void setMultipart(String file) throws MessagingException, IOException{
        if(multipart==null){
            multipart = new MimeMultipart();
        }
        multipart.addBodyPart(writeFiles(file));
        mimeMessage.setContent(multipart);
    }
    /**
     * 设置附件<添加多附件>
     * @param fileList<接收List集合>
     * @throws MessagingException
     * @throws IOException
     */
    public void setMultiparts(List<String> fileList) throws MessagingException, IOException{
        if(multipart==null){
            multipart = new MimeMultipart();
        }
        for(String s:fileList){
            multipart.addBodyPart(writeFiles(s));
                }
                      mimeMessage.setContent(multipart);
    }
    /**
     * 发送文本内容，设置编码方式
     * <方法与发送附件配套使用>
     * <发送普通的文本内容请使用setText()方法>
     * @param s
     * @param type
     * @throws MessagingException
     */
    public void setContent(String s,String type) throws MessagingException{
        if(multipart==null){
            multipart = new MimeMultipart();
        }
        bodypart = new MimeBodyPart();
        bodypart.setContent(s, type);
        multipart.addBodyPart(bodypart);
        mimeMessage.setContent(multipart);
        mimeMessage.saveChanges();
    }
    /**
     * 读取附件
     * @param filePath
     * @return
     * @throws IOException
     * @throws MessagingException
     */
    public BodyPart writeFiles(String filePath)throws IOException, MessagingException{
        File file = new File(filePath);
        if(!file.exists()){
            throw new IOException("文件不存在!请确定文件路径是否正确");
        }
        bodypart = new MimeBodyPart();
        DataSource dataSource = new FileDataSource(file);
        bodypart.setDataHandler(new DataHandler(dataSource));
        //文件名要加入编码，不然出现乱码
        bodypart.setFileName(MimeUtility.encodeText(file.getName()));
        return bodypart;
    }

}
    
    
    