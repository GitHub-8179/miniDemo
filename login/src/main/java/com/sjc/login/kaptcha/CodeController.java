package com.sjc.login.kaptcha;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.impl.DefaultKaptcha;

@Controller
public class CodeController {
    
	@Autowired
    private Producer captchaProducer = null;
    @Autowired
    private DefaultKaptcha defaultKaptcha;
    
    @RequestMapping("/kaptcha")
    public void getKaptchaImage(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        response.setDateHeader("Expires", 0);
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");
        response.setContentType("image/jpeg");
        //生成验证码
        String capText = defaultKaptcha.createText();
//        String capText = captchaProducer.createText();
        BufferedImage bi = captchaProducer.createImage(capText);

        System.out.println(capText);
        session.setAttribute(Constants.KAPTCHA_SESSION_KEY, capText);
        //向客户端写出
//        BufferedImage bi = defaultKaptcha.createImage(capText);
        ServletOutputStream out = response.getOutputStream();
        ImageIO.write(bi, "jpg", out);
        
        try {
            out.flush();
        } finally {
            out.close();
        }
    }
}

