package de.hh.changeRing.migration;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;

import static com.google.common.base.Charsets.UTF_8;

/**
 * ----------------GNU General Public License--------------------------------
 * <p/>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p/>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * <p/>
 * ----------------in addition-----------------------------------------------
 * <p/>
 * In addition, each military use, and the use for interest profit will be
 * excluded.
 * Environmental damage caused by the use must be kept as small as possible.
 */
public class ParseDumpFile {
    private String content;

    private ParseDumpFile(File from, File toFolder) {
        try {
            this.content = Files.toString(from, UTF_8);
            replace("`", "");
            replace("SET @saved_cs_client     = @@character_set_client;", "");
            replace("SET character_set_client = utf8;", "");
            replace("SET character_set_client = @saved_cs_client;", "");
            //content = content.replaceAll("ENGINE=MyISAM AUTO_INCREMENT=[0-9]+ DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci", "");
            replace("ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci PACK_KEYS=0;", "");
            //replace("text collate latin1_german2_ci", "varchar(2048)");
            //replace("collate latin1_german2_ci ", "");
            //replace(" auto_increment", "");
            replace("\\'", "''");
            replace("),(", ")\n,(");
            content = content.replaceAll("LOCK TABLES [a-z]+ WRITE;", "");
            replace("UNLOCK TABLES;", "");
            toFolder.delete();
            toFolder.mkdirs();
            for (String contentPart : Splitter.on("DROP TABLE IF EXISTS ").split(content)) {
                String tableName = contentPart.substring(0, contentPart.indexOf(';'));
                if (!tableName.contains(" ")) {
                    File file = new File(toFolder, tableName + ".sql");
                    file.delete();
                    Files.write("DROP TABLE IF EXISTS " + contentPart, file, UTF_8);
                    log(tableName, file, contentPart);
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void log(String tableName, File file, String contentPart) {
        String msg = tableName;
        while (msg.length() < 15) {
            msg += " ";
        }
        msg += ((file.length() / 1024)) + " KB";
        while (msg.length() < 25) {
            msg += " ";
        }
        msg += contentPart.split("\n").length + " rows";
        System.out.println(msg);
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            throw new IllegalArgumentException("expect original dump file");
        }
        File from = new File(args[0]);
        File to = new File("parsedDump");
        new ParseDumpFile(from, to);
    }

    private void replace(String separator, String joiner) {
        Iterable<String> split = Splitter.on(separator).split(content);
        content = Joiner.on(joiner).join(split);
    }
}
