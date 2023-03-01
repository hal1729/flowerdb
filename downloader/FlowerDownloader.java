import java.io.*;
import java.net.*;
import java.lang.Thread;
import java.util.Random;

public class FlowerDownloader {  
    final String FOLDER_NAME    = "html-files/";
    final String URLLISTFILE    = "flower-sci-names.csv";
    Random rand = new Random();

    public static void main(String[] args) {
        if (args.length<1 || !(new File(args[0]).exists())) {
            System.err.println("Usage: java FlowerDownloader datafilename");
            System.exit(1);
        }
        FlowerDownloader fd = new FlowerDownloader();
        fd.downloadFiles(args[0]);
    }

    void downloadFiles(String listFileName) {
        System.out.println("Data file " + listFileName);
        BufferedReader bfListFile;
        try {
            bfListFile = new BufferedReader(new FileReader(listFileName));
			String header = bfListFile.readLine();

            String dataLine = bfListFile.readLine();

			while (dataLine != null) {
                String[] data = dataLine.split(",");
                System.out.printf("Downloading %s to %s\n",data[0],FOLDER_NAME + data[1]);
				downloadURLtoFile(data[0],FOLDER_NAME + data[1]);
                Thread.sleep(1800+rand.nextInt(1800));
                // read next line  
				dataLine = bfListFile.readLine();
                //System.exit(1);
			}

			bfListFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
            
        }
    }

    void downloadURLtoFile(String strURL, String fileName) {
        File outFile = new File(fileName);
        if (outFile.exists()) {
            System.err.println(fileName + " exists. Skipping.");
            return;
        }

        URL url;
        InputStream is = null;
        BufferedReader br;
        BufferedWriter outr = null;
        String line;
         
        try {
            url = new URL(strURL);
            is = url.openStream();  // throws an IOException
            br = new BufferedReader(new InputStreamReader(is));
            outr = new BufferedWriter(new FileWriter(outFile));

            while ((line = br.readLine()) != null) {
                outr.write(line);
            }
            outr.close();
        } catch (MalformedURLException mue) {
            mue.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            try {
                if (is != null) is.close();
            } catch (IOException ioe) {
                // nothing to see here
            }
        }
    }
}