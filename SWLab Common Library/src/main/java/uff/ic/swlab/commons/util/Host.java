package uff.ic.swlab.commons.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

public class Host {

    public static void uploadViaFTP(String server, String user, String pass, String remoteName, final InputStream in) throws IOException, Exception {
        FTPClient ftpClient = new FTPClient();
        ftpClient.connect(server);

        String[] path = remoteName.split("/");
        if (FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
            if (ftpClient.login(user, pass)) {
                ftpClient.mkd(String.join("/", Arrays.copyOf(path, path.length - 1)));
                ftpClient.storeFile(remoteName, in);
                ftpClient.logout();
            } else
                throw new Exception();
            ftpClient.disconnect();
        } else
            throw new Exception();
    }

    public static void mkDirsViaFTP(String server, String user, String pass, String remoteName, final InputStream in) throws IOException, Exception {
        FTPClient ftpClient = new FTPClient();
        ftpClient.connect(server);

        if (FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
            if (ftpClient.login(user, pass)) {
                ftpClient.mkd(remoteName.split("\\.")[0]);
                ftpClient.logout();
            } else
                throw new Exception();
            ftpClient.disconnect();
        } else
            throw new Exception();
    }

}
