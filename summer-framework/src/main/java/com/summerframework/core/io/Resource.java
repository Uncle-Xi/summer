package com.summerframework.core.io;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public interface Resource extends InputStreamSource {

    boolean exists();

    URL getURL() throws IOException;

    File getFile() throws IOException;

    String getFilename();
}
