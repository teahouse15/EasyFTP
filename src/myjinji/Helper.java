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
            default:
                System.out.println("暂无此命令帮助，请检查命令是否有误");
        }
    }

    public static void help() {
        System.out.println("命令:\n" +
                "upload\t\tdownload\t\tls\t\tdir\t\tstatus\n" +
                "transMode\t\ttree");
    }
}