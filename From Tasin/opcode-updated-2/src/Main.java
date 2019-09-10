import java.io.*;
import java.util.HashMap;

public class Main {

        public static void main(String [] args) throws IOException {


//            String number = "4";
//
//            int intNumber = Integer.parseInt(number);
//
//            System.out.println(String.format("%4s", Integer.toBinaryString(intNumber)).replace(" ", "0")
//            );

            CommandProcessor commandProcessor = new CommandProcessor();

            Writer logWriter = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream("log.txt"), "utf-8"));

            Writer logisimHexWriter = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream("hex.txt"), "utf-8"));

            logisimHexWriter.write("v2.0 raw\n");

            // The name of the file to open.
            String fileName = "input.txt";

            // This will reference one line at a time
            String line = null;

            try {
                // FileReader reads text files in the default encoding.
                FileReader fileReader =
                        new FileReader(fileName);

                // Always wrap FileReader in BufferedReader.
                BufferedReader bufferedReader =
                        new BufferedReader(fileReader);

                while((line = bufferedReader.readLine()) != null) {
                    System.out.println(line);
                    String outputCodeBinary = commandProcessor.parseCommandLine(line);
                    System.out.println(outputCodeBinary);
                    System.out.println("");

                    logWriter.write(line +  "      Binary Conversion: ");
                    logWriter.write(outputCodeBinary);
                    logWriter.write("\n");


                    int decimal = Integer.parseInt(outputCodeBinary,2);
                    String outputCodeHex = Integer.toString(decimal,16);

                    logisimHexWriter.write(outputCodeHex);
                    logisimHexWriter.write("\n");

                    if (outputCodeBinary.length() != 20) {
                        System.out.println("output code length not 20");
                    }
                }

                // Always close files.
                bufferedReader.close();
            }
            catch(FileNotFoundException ex) {
                System.out.println(
                        "Unable to open file '" +
                                fileName + "'");
            }
            catch(IOException ex) {
                System.out.println(
                        "Error reading file '"
                                + fileName + "'");
                // Or we could just do this:
                // ex.printStackTrace();
            }

            logisimHexWriter.close();
            logWriter.close();
        }


}
