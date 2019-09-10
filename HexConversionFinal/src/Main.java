import java.io.*;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) throws IOException {


        ArrayList<String> rawCodeLines = new ArrayList<>();


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

        String predefinedline1 = "addi $t0, $zero, 255";
        String predefinedline2 = "sw $t0, 0($sp)";

        String hexString1 = commandProcessor.getCommandInHex(commandProcessor.parseCommandLine(predefinedline1, 0));
        String hexString2 = commandProcessor.getCommandInHex(commandProcessor.parseCommandLine(predefinedline2, 0));

        logisimHexWriter.write(hexString1 + "\n");
        logisimHexWriter.write(hexString2 + "\n");


        try {
            // FileReader reads text files in the default encoding.
            FileReader fileReader =
                    new FileReader(fileName);

            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader =
                    new BufferedReader(fileReader);

            int lineNo = 1;

            while ((line = bufferedReader.readLine()) != null) {


                System.out.println(line);

                String inputLine = new String(line);

                String[] spaceSplitStringArray = inputLine.split(" ");

                if (spaceSplitStringArray.length == 1) {
                    //label

                    String labelName = spaceSplitStringArray[0].replace(":", "");
                    System.out.println("Label name = " + labelName);
                    commandProcessor.getLabelAddressMap().put(labelName, Integer.toString(lineNo));
                    System.out.println("Label found. Given PC of " + lineNo);
                    continue;
                }


                rawCodeLines.add(inputLine);
//

                lineNo++;
//                    String outputCodeBinary = commandProcessor.parseCommandLine(line, instructions.size());
//                    System.out.println(outputCodeBinary);
//                    System.out.println("");
//
//                    if(outputCodeBinary.equalsIgnoreCase("")) {
//                        //label
//                        continue;
//                    }
//
//                    logWriter.write(line +  "      Binary Conversion: ");
//                    logWriter.write(outputCodeBinary);
//                    logWriter.write("\n");
//                    String hexString = commandProcessor.getCommandInHex(outputCodeBinary);
//                    instructions.add(hexString);
//                    if (outputCodeBinary.length() != 20) {
//                        System.out.println("output code length not 20");
//                    }
            }


            // Always close files.
            bufferedReader.close();
        } catch (FileNotFoundException ex) {
            System.out.println(
                    "Unable to open file '" +
                            fileName + "'");
        } catch (IOException ex) {
            System.out.println(
                    "Error reading file '"
                            + fileName + "'");
            // Or we could just do this:
            // ex.printStackTrace();
        }

        System.out.println("DIVIDER\n\n");

        for (int i = 0; i < rawCodeLines.size(); i++) {
            System.out.println(rawCodeLines.get(i));
            String lineRaw = rawCodeLines.get(i);

            String outputCodeBinary = commandProcessor.parseCommandLine(lineRaw, i + 1);
            System.out.println(outputCodeBinary);
            System.out.println("");

            if (outputCodeBinary.equalsIgnoreCase("")) {
                //label
                continue;
            }

            int lineNumber = i + 1;
            logWriter.write(lineNumber + ":" + lineRaw + "      Binary Conversion: ");
            logWriter.write(outputCodeBinary);
            logWriter.write("\n");

            System.out.println(lineRaw);
            String hexString = commandProcessor.getCommandInHex(outputCodeBinary);

            System.out.println(lineRaw);
            System.out.println(outputCodeBinary);
            System.out.println(hexString);
            System.out.println("\n");
            logisimHexWriter.write(hexString);
            logisimHexWriter.write("\n");
            if (outputCodeBinary.length() != 20) {
                System.out.println("output code length not 20");
            }



        }

        logisimHexWriter.close();
        logWriter.close();


    }
}
