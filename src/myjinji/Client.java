package myjinji;

import java.io.IOException;
import java.util.*;

public class Client {

    static Scanner scanner = new Scanner(System.in);
    static Map<Integer, Connector> connectorList = new HashMap<>();

    public static void main(String[] args) {
        Client client = new Client();
        client.start("192.168.13.130", 21);
    }


    public void start(String ip, int port) {
        Connector connector = new Connector(ip, port);
        connector.init();
        connectorList.put(connector.getId(), connector);

        Helper.help();
        while (true) {
            System.out.print("ftp(" + connector.getIp() + ")> ");
            String command = scanner.nextLine();

            String cmd = command.split(" ")[0];
            String arg = null;
            try {
                arg = command.split(" ")[1];
            } catch (ArrayIndexOutOfBoundsException e) {
            }

            if (cmd.equals("help")) {
                if (null != arg) {
                    Helper.cmdHelper(arg);
                } else {
                    Helper.help();
                }
            } else if (cmd.equals("pwd")) {
                Command.pwd(connector);
            } else if (cmd.equals("upload")) {
                if (null != arg) {
                    try {
                        Command.upload(arg, connector);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Helper.cmdHelper("upload");
                }
            } else if (cmd.equals("download")) {
                if (null != arg) {
                    try {
                        Command.download(arg, connector);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Helper.cmdHelper("download");
                }
            } else if (cmd.equals("ls") || cmd.equals("dir")) {
                if (null != arg) {
                    System.out.println(arg);
                    Command.listFile(connector, arg);
                } else {
                    Command.listFile(connector);
                }
            } else if (cmd.equals("quit") || cmd.equals("exit")) {
                connector.cmdWriter.sendCMD("QUIT");
                System.out.println(connector.cmdReader.readCMD());
                return;
            } else if (cmd.equals("cd")) {
                Command.changeDirectory(connector, arg);
            } else if (cmd.equals("locate")) {
                if (null != arg) {
                    Command.locate(connector, arg, "/");
                } else {
                    Helper.cmdHelper("locate");
                }
            } else if (cmd.equals("tree")) {
                Command.tree(connector, "/", 0);
            } else if (cmd.equals("binary")) {
                Command.binary(connector);
            } else if (cmd.equals("ascii")) {
                Command.ascii(connector);
            } else if (cmd.equals("verbose")) {
                Command.verbose();
            } else if (cmd.equals("rename")) {
                if (null != arg) {
                    Command.rename(connector, arg, command.split(" ")[2]);
                } else {
                    Helper.cmdHelper("rename");
                }
            } else if (cmd.equals("mkdir")) {
                if (null != arg) {
                    Command.mkdir(connector, arg);
                } else {
                    Helper.cmdHelper("mkdir");
                }
            } else if (cmd.equals("rmdir")) {
                if (null != arg) {
                    Command.rmdir(connector, arg);
                } else {
                    Helper.cmdHelper("rmdir");
                }
            } else if (cmd.equals("reconnect")) {
                Command.reconnect(connector);
            } else if (cmd.equals("status")) {
                Command.status(connector);
            } else if (cmd.equals("setid")) {
                if (null != arg) {
                    Command.setID(connector, Integer.parseInt(arg));
                } else {
                    Helper.cmdHelper("setname");
                }
            } else if (cmd.equals("open")) {
                Connector connector1 = Command.open(arg);

                connectorList.put(connector1.getId(), connector1);
            } else if (cmd.equals("switch")) {
                connector = connectorList.get(arg);
                connector.init();
            } else if (cmd.equals("lscon")) {
                Command.listFTPConnector(connectorList);
            } else if (cmd.equals("delete")) {
                if (null != arg) {
                    Command.delete(connector, arg);
                } else {
                    Helper.cmdHelper("delete");
                }
            } else {
                System.out.println("请检查命令是否有误!");
            }
        }
    }
}
