package myjinji;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;


/**
 * FTP的数据管道
 * @author bogendihong
 */
public class DataPipe extends Thread {

    // 0为上传 1为下载
    int mode = 1;

    String ip;

    int port;

    String filename = null;

    @Override
    public void run() {
        DataOutputStream dos = null;
        DataInputStream dis = null;
        try {
            // 打开套接字，连接FTP数据通道
            Socket data = new Socket(ip, port);
            System.out.println("已连接数据通道.....");

            // 上传模式
            if (mode == 0) {
                dos = new DataOutputStream(data.getOutputStream());
                dis = new DataInputStream(new FileInputStream(new File(filename)));

                byte[] bytes = new byte[1024];
                int len = -1;
                while ((len = dis.read(bytes)) != -1) {
                    dos.write(bytes, 0, len);
                    dos.flush();
                }
            } else { // 下载模式
                dis = new DataInputStream(data.getInputStream());

                byte[] bytes = new byte[1024];
                int len = -1;
                while ((len = dis.read(bytes)) != -1) {
                    if (!"".equals(filename) && null != filename) {
                        dos = new DataOutputStream(new FileOutputStream(filename, true));
                        dos.write(bytes, 0, len);
                        dos.flush();

                        break;
                    }

                    // 输出文件列表
                    String msg = new String(bytes, 0, len-2);
                    System.out.println(msg);
                }
            }

            data.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭通道
            try {
                if (null != dis) {
                    dis.close();
                }
                if (null != dos) {
                    dos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public  DataPipe(String ip, int port, String filename, int mode) {
        this.ip = ip;
        this.port = port;
        this.filename = filename;
        this.mode = mode;
    }
    public  DataPipe(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

}
