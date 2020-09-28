package myjinji;

import java.io.*;
import java.net.Socket;
import java.util.*;

public class Command {

    /**
     * 下载文件
     * @param file 文件名
     * @param connector Connector对象
     */
    public static void download(String file, Connector connector) throws IOException {
        int dataPort = CommonUtils.pasv(connector);

        new DataPipe(connector.getIp(), dataPort, file, 1).start();

        connector.cmdWriter.sendCMD("RETR " + file);

        // 150 Opening BINARY mode data connection for EasyFTP.jar (10007 bytes).
        String msg = connector.cmdReader.readCMD();

        if (Setting.verbose == 1) {
            System.out.println(msg);
        }
        msg = connector.cmdReader.readCMD();
        if (Setting.verbose == 1) {
            System.out.println(msg);
        }
    }

    /**
     * 上传文件
     * @param connector Connector对象
     * @param file 文件名
     */
    public static void upload(String file, Connector connector) throws IOException {
        int dataPort = CommonUtils.pasv(connector);

        new DataPipe(connector.getIp(), dataPort, file, 0).start();

        connector.cmdWriter.sendCMD("STOR " + file);
        String msg = connector.cmdReader.readCMD();
        if (Setting.verbose == 1) {
            System.out.println(msg);
        }
        msg = connector.cmdReader.readCMD();
        if (Setting.verbose == 1) {
            System.out.println(msg);
        }
    }

    /**
     * ls or dir
     * 列出文件
     * @param connector Connector对象
     */
    public static void listFile(Connector connector) {
        int dataPort = CommonUtils.pasv(connector);

        new DataPipe(connector.getIp(), dataPort).start();

        connector.cmdWriter.sendCMD("LIST");
        String msg = connector.cmdReader.readCMD(); // 150 Opening data connection for directory list.
        if (Setting.verbose == 1) {
            System.out.println(msg);
        }

        msg = connector.cmdReader.readCMD();  // 226 File sent ok

        if (Setting.verbose == 1) {
            System.out.println(msg);
        }
    }

    /**
     * 列出单个文件内容
     * @param connector Connector对象
     * @param filename 想要查看的文件名
     */
    public static void listFile(Connector connector, String filename) {
        int dataPort = CommonUtils.pasv(connector);
        new DataPipe(connector.getIp(), dataPort).start();
        connector.cmdWriter.sendCMD("LIST " + filename);
        String msg = connector.cmdReader.readCMD(); // 150 Opening data connection for directory list.
        System.out.println(msg);


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

    public static void locate(Connector connector, String filename, String path) {
        List<String> dirLists = new ArrayList<>();
        int dataPort = CommonUtils.pasv(connector); // 227 Entering Passive Mode (192,168,0,66,192,90).
        try {
            Socket socket = new Socket(connector.getIp(), dataPort);
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            connector.cmdWriter.sendCMD("LIST " + path);

            String message = connector.cmdReader.readCMD(); // 150 Opening data connection for directory list
            if (Setting.verbose == 1) {
                System.out.println(message);
            }
            String msg = null;

            br.readLine(); // .
            br.readLine(); // ..

            // 读取文件列表
            while ((msg = br.readLine()) != null) {
                // 先判断此目录下是否有想要查找的文件
                if (filename.equals(msg.split("\\s+")[8])) {
                    System.out.println(path + filename);
                    continue;
                }
                if (!msg.split("\\s+")[8].equals(".") && !msg.split("\\s+")[8].equals("..") && msg.startsWith("d")) {
                    // 将符合标准的文件夹添加到要递归的List中
                    dirLists.add(path + msg.split("\\s+")[8] + "/");
                    continue;
                }
            }
            br.close(); // 关闭PASV所打开的被动端口 让服务器结束传输
            message = connector.cmdReader.readCMD(); // 226 File Sent Ok

            if (Setting.verbose == 1) {
                System.out.println(message);
            }

            // 判断服务器是否关闭连接
            if (!socket.isClosed()) {
                socket.close();
            }

            if (dirLists.size() != 0) {
                // 递归
                for (String s : dirLists) {
                    locate(connector, filename, s);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 调整FTP传输模式为Binary
     * @param connector
     */
    public static void binary(Connector connector) {
        if (Setting.transMode == 0) {
            if (Setting.verbose == 1) {
                System.out.println("200 Type set to I.");
            }
            return;
        }

        connector.cmdWriter.sendCMD("TYPE A");
        String msg = connector.cmdReader.readCMD();
        if (Setting.verbose == 1) {
            System.out.println(msg);
        }
    }

    public static void ascii(Connector connector) {
        if (Setting.transMode == 1) {
            if (Setting.verbose == 1) {
                System.out.println("200 Type set to A.");
            }
            return;
        }

        connector.cmdWriter.sendCMD("TYPE A");
        String msg = connector.cmdReader.readCMD();
        if (Setting.verbose == 1) {
            System.out.println(msg);
        }
    }

    /**
     * 设置是否显示详细信息
     *
     */
    public static void verbose() {
        if (Setting.verbose == 1) {
            Setting.verbose = 0;
            System.out.println("关闭详细模式");
        } else {
            Setting.verbose = 1;
            System.out.println("开启详细模式");
        }
    }

    /**
     * 重命名
     * @param connector
     * @param source 原文件名
     * @param destination 目标文件名
     */
    public static void rename(Connector connector, String source, String destination) {
        System.out.println("文件名 " + source + " -> " + destination);
        connector.cmdWriter.sendCMD("RNFR " + source);
        String msg = connector.cmdReader.readCMD();
        if (msg.startsWith("550")) {
            System.out.println(msg);
            return;
        }
        System.out.println(msg);
        connector.cmdWriter.sendCMD("RNTO " + destination);
        System.out.println(connector.cmdReader.readCMD());
    }

    /**
     * 创建文件夹
     * @param connector 连接类
     * @param dirName 文件夹名字
     */
    public static void mkdir(Connector connector, String dirName) {
        connector.cmdWriter.sendCMD("MKD " + dirName);
        String msg = connector.cmdReader.readCMD();
        if (msg.startsWith("550")) {
            System.out.println(msg);
            return;
        }
        System.out.println(msg);
    }

    /**
     * 删除文件夹
     * @param connector
     * @param dirName 文件夹
     */
    public static void rmdir(Connector connector, String dirName) {
        connector.cmdWriter.sendCMD("RMD " + dirName);
        String msg = connector.cmdReader.readCMD();
        if (msg.startsWith("550")) {
            System.out.println(msg);
            return;
        }
        System.out.println(msg);
    }

    public static void reconnect(Connector connector) {
        if (connector.isConnect()) {
            System.out.println("连接持续中: " + connector.getIp());
        } else {
            System.out.println("已超时，将自动连接");
            connector.init();
        }
    }

    /**
     * 状态
     */
    public static void status(Connector connector) {
        System.out.println("服务器: " + connector.getIp());
        String mode = Setting.transMode==1?"Ascii":"Binary";
        String verbose = Setting.verbose==1?"开":"关";
        System.out.println("当前传输模式: " + mode + ", 详细模式: " + verbose);
    }

    public static void pwd(Connector connector) {
        connector.cmdWriter.sendCMD("PWD");
        String msg = connector.cmdReader.readCMD();
        if (msg.startsWith("421")) {
            System.out.println("连接已超时!");
            return;
        }
        System.out.println(msg);
    }

    /**
     * 打开一个新连接
     * @param addr
     * @param user
     * @param password
     */
    public static Connector open(String addr, String port, String user, String password) {
        Connector connector = new Connector(addr, Integer.parseInt(port));
        connector.setUser(user);
        connector.setPassword(password);

        connector.init();
        return connector;
    }

    /**
     * 打开一个新连接
     * @param addr
     * @param addr
     * @param user
     */
    public static Connector open(String addr, String user) {
        Connector connector = new Connector(addr, 21);
        connector.setUser(user);

        connector.init();
        return connector;
    }

    /**
     * 打开一个新连接
     * @param addr
     * @param port
     */
    public static Connector open(String addr, int port) {
        Connector connector = new Connector(addr, port);
        connector.init();

        return connector;
    }

    /**
     * 打开一个新连接
     * @param addr
     */
    public static Connector open(String addr) {
        Connector connector = new Connector(addr, 21);

        return connector;
    }

    /**
     * 为FTP连接器设置名字
     * @param connector
     * @param name
     */
    public static void setName(Connector connector, String name) {
        connector.setName(name);
        System.out.println(connector.getIp() + " --> " + connector.getName());
    }

    /**
     * 列出所有ftp连接器
     * @param connectorList
     */
    public static void listFTPConnector(Map<String, Connector> connectorList) {
        System.out.println("名称\t\tIP地址\t\t端口");
        for (Map.Entry<String, Connector> entry : connectorList.entrySet()) {
            System.out.println(entry.getValue().getName() + "\t\t" + entry.getValue().getIp() + "\t\t" + entry.getValue().getPort());
        }
    }

    /**
     * 两个服务器之间互传
     * 从服务器1上下载，上传到服务器2
     * @param connector1 server1
     * @param connector2 server2
     * @param file 文件
     */
    public static void one2two(Connector connector1, Connector connector2, String file) {

    }

    /**
     * 删除文件
     * @param connector
     * @param file 文件名
     */
    public static void delete(Connector connector, String file) {
        connector.cmdWriter.sendCMD("DELE " + file);
        String msg = connector.cmdReader.readCMD();
        if (msg.startsWith("421")) {
            System.out.println("421 Timeout.");
            System.exit(0);
        }
        System.out.println(msg);
    }

    /**
     * 文件树
     * 劣质命令(不要使用)
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
