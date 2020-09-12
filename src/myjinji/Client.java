package myjinji;

import java.io.IOException;
import java.util.Scanner;

public class Client {

    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        start("192.168.13.130", 21);
    }


    public static void start(String ip, int port) {
        Connector connector = new Connector(ip, port);
        connector.init();

        Helper.help();
        while (true) {
            System.out.print("ftp(" + connector.getIp() + ")> ");
            String command = scanner.nextLine();

            String cmd = command.split(" ")[0];
            String arg = null;
            try {
                arg = command.split(" ")[1];
            } catch (ArrayIndexOutOfBoundsException e) { }

            if (cmd.equals("help")) {
                if (null != arg) {
                    Helper.cmdHelper(arg);
                } else {
                    Helper.help();
                }
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
            } else {
                System.out.println("请检查命令是否有误!");
            }
        }
    }
}
