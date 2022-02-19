import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class Main {

    //Edit START
    static String bigFilePath = "/Users/synan/Downloads/discogs_20220201_releases.xml";
    static String smallFilesFolder = "/Users/synan/Downloads/data/";

//Edit END


    static long start = System.currentTimeMillis();
    public static void main(String[] args) {
        parser();
    }

    public static void parser() {
        int dd = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(bigFilePath))) {
            StringBuilder cline = new StringBuilder("<root>").append("\n");
            int file_number = 0;
            int cc = 0;
            for (String line; (line = br.readLine()) != null; ) {
                if (dd == 0) {
                    dd++;
                    continue;
                }
                int linel = line.length();
                if (linel < 10) {
                    cline.append(line).append(" ");
                    continue;
                }
                String last = line.substring(linel - 10);
                boolean release_end = false;
                if (!Objects.equals(last, "</release>")) {
                    cline.append(line).append(" ");
                } else {
                    cline.append(line).append("\n");
                    release_end = true;
                }
                cc++;
                if (cc % 100000 == 0) {

                    if (!release_end) {
                        cc--;
                        continue;
                    }

                    file_number++;
                    String path = smallFilesFolder + file_number + ".xml";
                    try {
                        PrintWriter dataWriter = new PrintWriter(path, StandardCharsets.UTF_8);
                        dataWriter.print(cline.append("</root>").toString().trim());
                        cline = new StringBuilder("<root>").append("\n");
                        dataWriter.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            file_number++;
            String path = smallFilesFolder + file_number + ".xml";
            try {
                PrintWriter lastWriter = new PrintWriter(path, StandardCharsets.UTF_8);

                cline = new StringBuilder(cline.toString().replace("</releases>","").trim());

                lastWriter.print(cline.append("\n").append("</root>"));
                lastWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            long end = System.currentTimeMillis();
            System.out.println("completed in " + (end - start) / 1000 + " seconds");
            // line is not visible here.
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}