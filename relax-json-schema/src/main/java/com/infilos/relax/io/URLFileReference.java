package com.infilos.relax.io;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class URLFileReference implements FileReference<URL> {
    private final URL inputFile;
    private InputStream is;

    public URLFileReference(URL source) {
        inputFile = source;
    }

    @Override
    public InputStream getInputStream() throws Exception {
        if (null == is) {
            is = inputFile.openStream();
        }
            
        return is;
    }

    @Override
    public String getLocator() {
        return inputFile.toExternalForm();
    }


    @Override
    public String getFileName() {
        String[] pathParts = inputFile.getFile().split("/");
        return pathParts[pathParts.length - 1];
    }

    @Override
    public void close() throws IOException {
        if (null != is) {
            is.close();
        }
    }
}