package uff.ic.swlab.commons.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

public class Host {

    public static void uploadViaFTP(String server, Integer port, String user, String pass, String remoteName, final InputStream in) throws IOException, Exception {
        FTPClient ftpClient = new FTPClient();
        ftpClient.connect(server, port == null || port == 0 ? 21 : port);

        try {
            String[] dirs = remoteName.split("/");
            if (FTPReply.isPositiveCompletion(ftpClient.getReplyCode()))
                if (ftpClient.login(user, pass)) {
                    ftpClient.mkd(String.join("/", Arrays.copyOfRange(dirs, 0, dirs.length - 1)));
                    ftpClient.storeFile(remoteName, in);
                    ftpClient.logout();
                }
            ftpClient.disconnect();
        } catch (IOException e) {
            try {
                ftpClient.disconnect();
            } catch (IOException e2) {
            }
            throw e;
        }
    }

    public static void mkDirsViaFTP(String server, Integer port, String user, String pass, String remoteName) throws Exception {
        FTPClient ftpClient = new FTPClient();
        ftpClient.connect(server, port == null || port == 0 ? 21 : port);

        try {
            if (FTPReply.isPositiveCompletion(ftpClient.getReplyCode()))
                if (ftpClient.login(user, pass)) {
                    String[] dirs = remoteName.split("/");
                    ftpClient.mkd(String.join("/", Arrays.copyOfRange(dirs, 0, dirs.length - 1)));
                    ftpClient.logout();
                }
            ftpClient.disconnect();
        } catch (IOException e) {
            try {
                ftpClient.disconnect();
            } catch (IOException e2) {
            }
            throw e;
        }
    }

}
