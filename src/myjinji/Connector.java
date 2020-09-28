package myjinji;

import java.io.*;
import java.net.Socket;
import java.util.Random;

public class Connector {

    // 连接器名称
    int id;

    // IP地址
    String ip;

    // 默认端口号
    int port = 21;

    // 用户名
    String user = "anonymous";

    // 密码
    String password = "";

    String msg;

    CMDWriter cmdWriter = new CMDWriter();
    CMDReader cmdReader = new CMDReader();


    public Connector(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    /**
     * 初始化连接信息
     */
    public synchronized void init() {
        try {
            Socket client = new Socket(ip, port);
            cmdReader.br = new BufferedReader(new InputStreamReader(client.getInputStream()));
            cmdWriter.bw = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));

            cmdWriter.sendCMD("USER " + user);
            msg = cmdReader.readCMD();
            Logger.print("Server", msg);
            cmdWriter.sendCMD("PASS " + password);
            msg = cmdReader.readCMD();
            Logger.print("Server", msg);
            msg = cmdReader.readCMD();
            Logger.print("Server", msg);
            setId(new Random().nextInt(1000));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isConnect() {
        this.cmdWriter.sendCMD("NOOP");
        String msg = this.cmdReader.readCMD();
        if (msg.startsWith("200")) {
            return true;
        }
        return false;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
