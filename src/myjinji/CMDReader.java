package myjinji;

import java.io.BufferedReader;
import java.io.IOException;

public class CMDReader {

    // 字符输入流
    static BufferedReader br;

    /**
     * 读取FTP控制端口数据
     * @return 返回文本
     */
    public String readCMD() {
        if (null == br) {
            System.out.println("未初始化输入信道");
            return "";
        }
        try {
            return br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
}
