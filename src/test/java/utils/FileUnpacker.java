package utils;

import java.io.InputStream;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class FileUnpacker {

    private String archiveName = "someNameZIP.zip";

    public InputStream extractFileFromZip(String fileNameToExtract) throws Exception {
        ZipInputStream zipInputStream = new ZipInputStream(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(archiveName)));
        ZipEntry zipEntry;
        while ((zipEntry = zipInputStream.getNextEntry()) != null) {
            if (zipEntry.getName().contains(fileNameToExtract)) {
                return zipInputStream;
            }
        }
        return InputStream.nullInputStream();
    }

    public String getArchiveName() {
        return archiveName;
    }
}
