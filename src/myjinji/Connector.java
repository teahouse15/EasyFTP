package myjinji;

import java.io.*;
import java.net.Socket;

public class Connector {

    // IP地址
    String ip;

    // 默认端口号
    int port = 21;

    String msg;

    CMDWriter cmdWriter = new CMDWriter();
    CMDReader cmdReader = new CMDReader();

    /**
     * 初始化连接信息
     */
    public void init() {
        try {
            Socket client = new Socket(ip, port);
            cmdReader.br = new BufferedReader(new InputStreamReader(client.getInputStream()));
            cmdWriter.bw = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));

            cmdWriter.sendCMD("USER anonymous");
            msg = cmdReader.readCMD();
            Logger.print("Server", msg);
            cmdWriter.sendCMD("PASS ");
            msg = cmdReader.readCMD();
            Logger.print("Server", msg);
            msg = cmdReader.readCMD();
            Logger.print("Server", msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Connector(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
