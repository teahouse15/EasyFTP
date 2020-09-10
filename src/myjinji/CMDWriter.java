package myjinji;

import java.io.BufferedWriter;
import java.io.IOException;

public class CMDWriter {

    // 字符输出流
    static BufferedWriter bw;

    /**
     * 向FTP控制端口发送命令
     * @param cmd 命令
     */
    public static void sendCMD(String cmd) {
        if (null == bw) {
            System.out.println("未初始化输出信道");
            return;
        }
        try {
            bw.write(cmd + "\r\n");
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
