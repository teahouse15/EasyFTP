package myjinji;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 工具类
 * @author bogendihong
 */
public class CommonUtils {

    /**
     * 进入被动给模式
     * @param connector
     * @return 数据端口
     */
    public static int pasv(Connector connector) {
        connector.cmdWriter.sendCMD("PASV");
        String msg = connector.cmdReader.readCMD(); // 227 Entering Passive Mode (192,168,0,66,192,90).
        if (Setting.verbose == 1) {
            System.out.println(msg);
        }
        int dataPort = CommonUtils.getDataPort(msg);
        return dataPort;
    }

    /**
     * 获取被动模式端口
     * @param msg 227 Entering Passive Mode (192,168,8,169,192,206).
     * @return 返回端口
     */
    public static int getDataPort(String msg) {
        Pattern pattern = Pattern.compile("(?<=\\()[^\\)]+");
        Matcher matcher = pattern.matcher(msg);
        matcher.find();
        String[] s = matcher.group().split(",");
        return Integer.parseInt(s[s.length - 2]) * 256 + Integer.parseInt(s[s.length - 1]);
    }
}
