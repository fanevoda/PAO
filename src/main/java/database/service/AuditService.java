package database.service;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.sql.Timestamp;
import java.util.Date;
import java.util.RandomAccess;

public class AuditService {

    private static AuditService instance;

    private AuditService(){};

    public static AuditService getInstance(){
        if(instance == null){
            instance = new AuditService();
        }
        return instance;
    }

    public void writeToFile(String filePath, String line)
    {
        try(RandomAccessFile randomAccessFile = new RandomAccessFile(filePath, "rw")){
            File file = new File(filePath);
            randomAccessFile.seek(file.length());
            randomAccessFile.write("\n".getBytes());
            Timestamp timestamp = new Timestamp(new Date().getTime());
            String toWrite = timestamp.toString() + ", " + line;
            randomAccessFile.write(toWrite.getBytes());
        }catch(IOException e){
            e.printStackTrace();
        }
    }

}
