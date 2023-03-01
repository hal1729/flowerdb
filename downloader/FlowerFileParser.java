import java.io.*;
import java.util.regex.*;
import java.util.HashMap;

public class FlowerFileParser {
    final String FOLDER_NAME    = "html-files/";
    Pattern namePattern = Pattern.compile("<h1>([^<]*)(<small>)?\\(?([^\\)<]*)\\)?<"); //<h1>([^<]*)(<small>)?\\(([^\\))]*[)\\<]");
    Pattern allPropPattern = Pattern.compile("<h2 id=\"plant-data\" .*?>(.*?)<aside");
    Pattern propPattern = Pattern.compile("<h5[^>]*>(.*?):<\\/h5>(.*?)(?=<h5[^>]*>)"); //<h5>(.*?)<\\/h5>(.*?)<h5>");

    HashMap<String, Integer> propMap = new HashMap<>();


    public static void main(String[] args) {
        if (args.length<1) {
            System.err.println("Usage: java FlowerFileParser outFile");
            System.exit(1);
        }
        FlowerFileParser fp = new FlowerFileParser();
        fp.populatePatterns();
        fp.parseFiles(args[0]);
    }

    void populatePatterns() {
        propMap.put("Plant Type",2);
        propMap.put("Color",3);
        propMap.put("Hardiness Zones",4);
        propMap.put("Blooms in",5);
        propMap.put("Height",6);
        propMap.put("Soil needs",7);
        propMap.put("Sun needs",8);
        propMap.put("Water needs",9);
        propMap.put("Maintenance",10);
    }

    
    void parseFiles(String outFile) {
        BufferedWriter outr = null;
        try {
            outr = new BufferedWriter(new FileWriter(outFile));
            outr.write("Sci Name,Name, Plant Type,Color,Hardiness Zones,Blooms in,Height,Soil needs,Sun needs,Water needs,Maintenance\n");

            String[] fFiles = (new File(FOLDER_NAME)).list();
            for (String fFile : fFiles){
                
                String[] line = parseFile(FOLDER_NAME+fFile);
                //System.out.println(line);
                outr.write(String.join("|",line)+"\n"); 
            }
            outr.close();
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    String[] parseFile(String fileName) {
        System.out.println("Reading from "+fileName);
        String[] line = new String[11];
        File outFile = new File(fileName);
        if (!outFile.exists()) {
            System.err.println(fileName + " does not exist. Skipping.");
            return null;
        }
        BufferedReader bfDataFile;
        try {
            bfDataFile = new BufferedReader(new FileReader(fileName));
			String dataLine = "";
            String dataLineS = bfDataFile.readLine();
			while (dataLineS != null) {
                dataLine += dataLineS;
                dataLineS = bfDataFile.readLine();
			}

            Matcher m = namePattern.matcher(dataLine);
            m.find();
            String sciName = m.group(1).trim();
            //System.out.println("--" + sciName);
            String commonName = sciName;
            if (m.group(3)!=null) {
                commonName = m.group(3).trim();
            }
            if ("".equals(commonName)) {
                commonName = sciName;
            }
            line[0] = sciName;
            line[1] = commonName;
            
            //System.out.println(line);
            Matcher mprop = allPropPattern.matcher(dataLine);
            mprop.find();
            //System.out.println(mprop.group());
            
            String propString = mprop.group(1)+"<h5>";
            Matcher miprop = propPattern.matcher(propString);
            while(miprop.find()) {
                String propName  =  miprop.group(1).replaceAll("<[^>]*?>","").trim();
                String propValue =  miprop.group(2).replaceAll("<[^>]*?>","").trim();
                if (propMap.containsKey(propName)) {
                    if (propMap.get(propName)==4) {
                        if (propValue.contains("-")) {
                            String[] hs = propValue.split("-");
                            int hs1 = Integer.parseInt(hs[0]);
                            int hs2 = Integer.parseInt(hs[1]);
                            propValue = ":";
                            for(;hs1<=hs2; hs1++) {
                                propValue = propValue + hs1+":";
                            }
                        }
                    } else if (propMap.get(propName)==9) {
                        if (propValue.equals("avarage")) {
                            propValue = "average";
                        }
                    }
                    line[propMap.get(propName)] = "\""+ propValue +"\"";
                } else {
                    System.out.println("------- NOT FOUND "+ propName+": " + propValue);    
                }
                //System.out.println(propName+": " + propValue);
            }


			bfDataFile.close();
            //System.exit(1);
		} catch (IOException e) {
			e.printStackTrace();
		} 
        return line;
    }
}