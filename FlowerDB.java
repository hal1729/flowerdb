import java.io.*;

public class FlowerDB {
    Flower[] flowers = new Flower[227];
     String dataFileName = "flower-data.csv";
    public static void main(String[] args) {
        FlowerDB flowerDB = new FlowerDB();
        //System.out.println(selection.x);
        flowerDB.populateFlowers();
        flowerDB.printFlowers();        
    } 

    void populateFlowers() {
        System.out.println("Loading data file " + dataFileName);
        BufferedReader bfDataFile;
        try {
            bfDataFile = new BufferedReader(new FileReader(dataFileName));
			String header = bfDataFile.readLine();

            String dataLine = bfDataFile.readLine();
            int fNum = 0;
			while (dataLine != null) {
                //System.out.println(dataLine);
                String[] data = dataLine.split("\\|");
                flowers[fNum] = new Flower(data[0],data[1]);
                
                
                // read next line
                fNum++;  
				dataLine = bfDataFile.readLine();
			}

			bfDataFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		} 
    }

    void printFlowers() {
        for (Flower flower: flowers) {
            System.out.println(flower.commonName);
        }
    }

}

class Flower {
    String sciName;
    String commonName;
    
    public Flower(String sciName, String commonName) {
        this.sciName = sciName;
        this.commonName = commonName;
    }
}