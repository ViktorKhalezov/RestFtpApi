package com.example.RestFtpApi.service;


import com.example.RestFtpApi.entity.FileInfo;
import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


@Service
public class FtpService {

    @Value("${ftpservice.server}")
    private String server;

    @Value("${ftpservice.port}")
    private int port;

    @Value("${ftpservice.username}")
    private String username;

    @Value("${ftpservice.password}")
    private String password;

    private FTPClient ftpClient;

    private final String PREFIX = "GRP327_";
    private final String TARGET_FOLDER = "фотографии";
    private final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy");


    public void open() throws IOException {
        ftpClient = new FTPClient();

        ftpClient.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));

         ftpClient.connect(server, port);
        ftpClient.enterLocalPassiveMode();
        int reply = ftpClient.getReplyCode();
        if (!FTPReply.isPositiveCompletion(reply)) {
            ftpClient.disconnect();
            throw new IOException("Exception in connecting to FTP Server");
        }

        ftpClient.login(username, password);
    }

    public void close() throws IOException {
        ftpClient.disconnect();
    }

    public List<FileInfo> getFileList(){
        List<FileInfo> fileList = new ArrayList<>();
        try {
            open();
            walkFileTree(ftpClient, fileList, ftpClient.printWorkingDirectory());
           close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        return fileList;
    }


    private List<FileInfo> walkFileTree(FTPClient ftpClient, List<FileInfo> files, String directory) {
        try {
            FTPFile[] ftpFiles = ftpClient.listFiles(directory);
            if(ftpFiles.length > 0) {
                for (FTPFile file : ftpFiles) {
                    if(file.isDirectory() && !file.getName().equals(".") && !file.getName().equals("..")) {
                        ftpClient.changeWorkingDirectory(ftpClient.printWorkingDirectory()+ "/" + file.getName());
                        walkFileTree(ftpClient, files, ftpClient.printWorkingDirectory());
                    }
                    if(file.isFile() && file.getName().startsWith(PREFIX) && ftpClient.printWorkingDirectory().contains(TARGET_FOLDER)) {
                        files.add(new FileInfo(ftpClient.printWorkingDirectory() + "/" + file.getName(),
                                DATE_FORMAT.format(file.getTimestamp().getTime()), file.getSize()));
                    }
                }
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        return files;
    }


}

