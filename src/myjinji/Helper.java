package myjinji;

/**
 * 命令帮助
 * @author bogendihong
 */
public class Helper {
    public static void cmdHelper(String cmdName) {
        switch (cmdName) {
            case "help":
                System.out.println("help\t\t打印本地帮助信息");
                break;
            case "download":
                System.out.println("download <filename>\t\t获取服务器文件");
                break;
            case "upload":
                System.out.println("upload <filename>\t\t上传本地文件到服务器");
                break;
            case "status":
                System.out.println("status\t\t查看当前设置");
                break;
            case "transMode":
                System.out.println("transMode <binary || ascii>\t\t更改数据传输方式");
                break;
            case "ls":
                System.out.println("ls <filename || directory>\t\t列出文件");
                break;
            case "dir":
                System.out.println("dir <filename || directory>\t\t列出文件");
                break;
            case "tree":
                System.out.println("tree\t\t查看文件树");
                break;
            case "locate":
                System.out.println("locate <filename>\t\t全局查找文件位置");
                break;
            case "binary":
                System.out.println("binary\t\t设置为二进制传输模式");
                break;
            case "ascii":
                System.out.println("ascii\t\t设置为ascii传输模式");
                break;
            case "verbose":
                System.out.println("verbose\t\t显示详细信息");
                break;
            case "mkdir":
                System.out.println("mkdir <directory>\t\t创建文件夹");
                break;
            case "rmdir":
                System.out.println("rmdir <directory>\t\t删除文件夹");
                break;
            case "quit":
                System.out.println("quit\t\t退出登录");
                break;
            case "exit":
                System.out.println("exit\t\t退出程序");
                break;
            case "reconnect":
                System.out.println("reconnect\t\t重新连接");
                break;
            case "open":
                System.out.println("open <ip>:<port>\t\t关闭当前连接并开启一个新连接");
                break;
            case "rename":
                System.out.println("rename <source> <destination>\t\t重命名文件或文件夹");
                break;
            case "setname":
                System.out.println("setname <name>\t\t为当前连接器设置名字");
                break;
            case "switch":
                System.out.println("switch <name>\t\t切换ftp连接器");
                break;
            case "lscon":
                System.out.println("lscon\t\t查看所有ftp连接器");
                break;
            default:
                System.out.println("暂无此命令帮助，请检查命令是否有误");
        }
    }

    public static void help() {
        System.out.println("命令:\n" +
                "upload\t\tdownload\t\tls\t\tdir\t\tstatus\n" +
                "binary\t\tascii\t\ttree\t\tlocate\t\tverbose\n" +
                "quit\t\texit\t\treconnect\t\topen\t\trename\n" +
                "mkdir\t\trmdir\t\tsetname\t\tswitch\t\tlscon");
    }
}
