package org.ricardo.jobtrackr;

import org.ricardo.jobtrackr.config.ServerConfig;

import java.io.IOException;

public class Main {
    static void main() throws IOException {
        ServerConfig.start();
    }
}
