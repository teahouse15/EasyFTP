package myjinji;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Command {

    /**
     * 下载文件
     * @param file 文件名
     * @param connector Connector对象
     */
    public static void download(String file, Connector connector) throws IOException {
        int dataPort = CommonUtils.pasv(connector);
        connector.cmdWriter.sendCMD("RETR " + file);
        String msg = connector.cmdReader.readCMD();
        System.out.println(msg);
        new DataPipe(connector.getIp(), dataPort, file, 1).start();
        msg = connector.cmdReader.readCMD();
        System.out.println(msg);
    }

    /**
     * 上传文件
     * @param connector Connector对象
     * @param file 文件名
     */
    public static void upload(String file, Connector connector) throws IOException {
        int dataPort = CommonUtils.pasv(connector);
        connector.cmdWriter.sendCMD("STOR " + file);
        String msg = connector.cmdReader.readCMD();
        System.out.println(msg);
        new DataPipe(connector.getIp(), dataPort, file, 0).start();
        msg = connector.cmdReader.readCMD();
        System.out.println(msg);
    }

    /**
     * ls or dir
     * 列出文件
     * @param connector Connector对象
     */
    public static void listFile(Connector connector) {
        int dataPort = CommonUtils.pasv(connector);
        connector.cmdWriter.sendCMD("LIST");
        String msg = connector.cmdReader.readCMD(); // 150 Opening data connection for directory list.
        System.out.println(msg);

        new DataPipe(connector.getIp(), dataPort).start();
        msg = connector.cmdReader.readCMD();  // 226 File sent ok
        System.out.println(msg);
    }

    /**
     * 列出单个文件内容
     * @param connector Connector对象
     * @param filename 想要查看的文件名
     */
    public static void listFile(Connector connector, String filename) {
        int dataPort = CommonUtils.pasv(connector);
        connector.cmdWriter.sendCMD("LIST " + filename);
        String msg = connector.cmdReader.readCMD(); // 150 Opening data connection for directory list.
        System.out.println(msg);


        new DataPipe(connector.getIp(), dataPort).start();
        msg = connector.cmdReader.readCMD();  // 226 File sent ok
        System.out.println(msg);
    }

    /**
     * cd 改变路径
     * @param connector Connector对象
     * @param dirName 文件夹名字
     */
    public static void changeDirectory(Connector connector, String dirName) {
        if (dirName == null) {
            connector.cmdWriter.sendCMD("CWD /");
            System.out.println("回到根目录");
            String msg = connector.cmdReader.readCMD();
            System.out.println(msg);
        } else {
            connector.cmdWriter.sendCMD("CWD " + dirName);
            System.out.println(dirName);
            String msg = connector.cmdReader.readCMD();
            if (msg.startsWith("550")) {
                System.out.println("文件夹错误，请检查是否输入错误");
                return;
            }
            System.out.println(msg);
        }
    }

    public void locate(Connector connector, String filename) {

    }

    /**
     * 状态
     */
    public void status() {

    }

    /**
     * 传输方式 binary ascii
     */
    public void transMode() {

    }


    /**
     * 文件树
     * 劣质命令
     * @param connector Connecotr
     * @param path 路径
     * @param tabs 所处的目录层级
     */
    public static void tree(Connector connector, String path, int tabs) {
        StringBuilder sb = new StringBuilder("\t");
        for (int i = 0; i < tabs; i++) {
            sb.append("\t");
        }
        List<String> dirLists = new ArrayList<>();
        int dataPort = CommonUtils.pasv(connector); // 227 Entering Passive Mode (192,168,0,66,192,90).
        try {
            Socket socket = new Socket(connector.getIp(), dataPort);
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            connector.cmdWriter.sendCMD("LIST " + path);
            String nn = connector.cmdReader.readCMD(); // 150 Opening data connection for directory list
            String msg = null;

            br.readLine(); // .
            br.readLine(); // ..

            // 读取文件列表
            while ((msg = br.readLine()) != null) {
                if (!msg.split("\\s+")[8].equals(".") && !msg.split("\\s+")[8].equals("..") && msg.startsWith("d")) {
                    // 将符合标准的文件夹添加到要递归的List中
                    dirLists.add(path + msg.split("\\s+")[8] + "/");
                    System.out.println(sb + "├─" + path + msg.split("\\s+")[8]);
                    continue;
                }
                System.out.println(sb + "├─" + path + msg.split("\\s+")[8]);
            }
            br.close(); // 关闭PASV所打开的被动端口 让服务器结束传输
            connector.cmdReader.readCMD(); // 226 File Sent Ok
            // 判断服务器是否关闭连接
            if (!socket.isClosed()) {
                socket.close();
            }

            if (dirLists.size() != 0) {
                // 树的递归
                for (String s : dirLists) {
                    tree(connector, s, tabs + 1);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
