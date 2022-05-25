package groovyworld.bot.setup;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

@SuppressWarnings("ALL")
public class TokenReader {

    public static String get() throws IOException {
        File file = new File("../token.txt");
        BufferedReader reader = new BufferedReader(new FileReader("../token.txt"));
        StringBuilder builder = new StringBuilder();

        if (file == null) {
            file.getParentFile().mkdirs();
            file.createNewFile();
        }

        if (file != null) {
            try {
                String line = reader.readLine();
                while (line != null) {
                    builder.append(line);
                    builder.append(System.lineSeparator());
                    line = reader.readLine();
                }
                String toStr = builder.toString();
            } finally {
                builder.toString();
            }
        }
        return builder.toString();
    }

}
