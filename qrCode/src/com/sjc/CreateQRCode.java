package com.sjc;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.UnsupportedEncodingException;

import javax.imageio.ImageIO;

import com.swetake.util.Qrcode;

public class CreateQRCode {

	public static void main(String[] args) throws Exception {
		
		int v = 21;
		int width =  67 + 12 * (v - 1)  ;
		int height =  67 + 12 * (v - 1)  ;
		Qrcode qr = new Qrcode();
	        /**
	         * 纠错等级分为
	         * level L : 最大 7% 的错误能够被纠正；
	         * level M : 最大 15% 的错误能够被纠正；
	         * level Q : 最大 25% 的错误能够被纠正；
	         * level H : 最大 30% 的错误能够被纠正；
	         */
		qr.setQrcodeErrorCorrect('L');
		qr.setQrcodeEncodeMode('B');//注意版本信息 N代表数字 、A代表 a-z,A-Z、B代表 其他)
		qr.setQrcodeVersion(v);//版本号  1-40
        String qrData = "https://hao.360.cn/?s0001";//内容信息
        byte[] d = qrData.getBytes("utf-8");//汉字转格式需要抛出异常
	
        //缓冲区
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);

        //绘图
        Graphics2D gs = bufferedImage.createGraphics();

        gs.setBackground(Color.WHITE);
        gs.setColor(Color.BLACK);
        gs.clearRect(0, 0, width, height);
        
      //偏移量
        int pixoff = 2;
        
        /**
         * 容易踩坑的地方
         * 1.注意for循环里面的i，j的顺序，
         *   s[j][i]二维数组的j，i的顺序要与这个方法中的 gs.fillRect(j*3+pixoff,i*3+pixoff, 3, 3);
         *   顺序匹配，否则会出现解析图片是一串数字
         * 2.注意此判断if (d.length > 0 && d.length < 120)
         *   是否会引起字符串长度大于120导致生成代码不执行，二维码空白
         *   根据自己的字符串大小来设置此配置
         */
        if (d.length > 0 && d.length < 120) {
            boolean[][] s = qr.calQrcode(d);

            for (int i = 0; i < s.length; i++) {
                for (int j = 0; j < s.length; j++) {
                    if (s[j][i]) {
                        gs.fillRect(j * 3 + pixoff, i * 3 + pixoff, 3, 3);
                    }
                }
            }
        }
        
        /* 判断是否需要添加logo图片  图片会影响数据 */  
            File icon = new File("E:\\20170111.jpg");  
            if(icon.exists()){  
                int width_4 =  width / 4;  
                int width_8 = width_4 / 2;  
                int height_4 = height / 4;  
                int height_8 = height_4 / 2;  
                Image img = ImageIO.read(icon);  
                gs.drawImage(img, width_4 + width_8, height_4 + height_8,width_4,height_4, null);  
                gs.dispose();  
                bufferedImage.flush();  
            }
            
        gs.dispose();
        bufferedImage.flush();
        //设置图片格式，与输出的路径
        ImageIO.write(bufferedImage, "png", new File("D:/qrcode.png"));
        System.out.println("二维码生成完毕");
        
	}
	
}
