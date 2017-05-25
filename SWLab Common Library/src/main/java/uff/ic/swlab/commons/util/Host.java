package uff.ic.swlab.commons.util;

import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

public class Host {

    public static void uploadViaFTP(String server, String user, String pass, String dir, String remoteName, final InputStream in) throws IOException, Exception {
        FTPClient ftpClient = new FTPClient();
        ftpClient.connect(server);
        if (FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
            if (ftpClient.login(user, pass)) {
                ftpClient.mkd(dir);
                ftpClient.storeFile(remoteName, in);
                ftpClient.logout();
            } else
                throw new Exception();
            ftpClient.disconnect();
        } else
            throw new Exception();
    }

}
