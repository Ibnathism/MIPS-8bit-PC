import java.util.HashMap;

public class CommandProcessor {

    String decimalToBinary(int dec) {

        String result= "00000000";
        int i=result.length()-1;
        while(dec!=0)
        {
            char a[]=result.toCharArray();
            a[i--]= String.valueOf(dec%2).charAt(0);
            result=new String(a);
            dec=dec/2;

        }

        return result;
    }

    public HashMap<String, String> getCommandOpCodeMap() {
        return commandOpCodeMap;
    }

    public void setCommandOpCodeMap(HashMap<String, String> commandOpCodeMap) {
        this.commandOpCodeMap = commandOpCodeMap;
    }

    public HashMap<String, String> getCommandTypeMap() {
        return commandTypeMap;
    }

    public void setCommandTypeMap(HashMap<String, String> commandTypeMap) {
        this.commandTypeMap = commandTypeMap;
    }

    public HashMap<String, String> getRegisterAddressMap() {
        return registerAddressMap;
    }

    public void setRegisterAddressMap(HashMap<String, String> registerAddressMap) {
        this.registerAddressMap = registerAddressMap;
    }

    public HashMap<String, String> getLabelAddressMap() {
        return labelAddressMap;
    }

    public void setLabelAddressMap(HashMap<String, String> labelAddressMap) {
        this.labelAddressMap = labelAddressMap;
    }

    HashMap<String,String>  commandOpCodeMap ;
    HashMap<String,String> commandTypeMap;
    HashMap<String,String> registerAddressMap;
    HashMap<String, String> labelAddressMap;


    public CommandProcessor() {
         commandOpCodeMap = new HashMap<>();
        commandTypeMap = new HashMap<>();
        registerAddressMap = new HashMap<>();
        labelAddressMap = new HashMap<>();

        commandOpCodeMap.put("SUB", "0000");
        commandOpCodeMap.put("SUBI", "0001");
        commandOpCodeMap.put("AND", "0010");
        commandOpCodeMap.put("ANDI", "0011");
        commandOpCodeMap.put("OR", "0100");
        commandOpCodeMap.put("LW", "0101");
        commandOpCodeMap.put("ADDI", "0110");
        commandOpCodeMap.put("BNEQ", "0111");
        commandOpCodeMap.put("ADD", "1000");
        commandOpCodeMap.put("SRL", "1001");
        commandOpCodeMap.put("ORI", "1010");
        commandOpCodeMap.put("SLL", "1011");
        commandOpCodeMap.put("NOR", "1100");
        commandOpCodeMap.put("J", "1101");
        commandOpCodeMap.put("SW", "1110");
        commandOpCodeMap.put("BEQ", "1111");

        commandTypeMap.put("SUB", "R");
        commandTypeMap.put("SUBI", "I");
        commandTypeMap.put("AND", "R");
        commandTypeMap.put("ANDI", "I");
        commandTypeMap.put("OR", "R");
        commandTypeMap.put("LW", "I");
        commandTypeMap.put("ADDI", "I");
        commandTypeMap.put("BNEQ", "I");
        commandTypeMap.put("ADD", "R");
        commandTypeMap.put("SRL", "R");
        commandTypeMap.put("ORI", "I");
        commandTypeMap.put("SLL", "R");
        commandTypeMap.put("NOR", "R");
        commandTypeMap.put("J", "J");
        commandTypeMap.put("SW", "I");
        commandTypeMap.put("BEQ", "I");

        registerAddressMap.put("$zero", "0000");
        registerAddressMap.put("$t0", "0001");
        registerAddressMap.put("$t1", "0010");
        registerAddressMap.put("$t2", "0011");
        registerAddressMap.put("$t3", "0100");
        registerAddressMap.put("$t4", "0101");
        registerAddressMap.put("$sp", "0110");

    }

    public  String parseCommandLine(String inputLine, int sizeForLabel) {

        String[] spaceSplitStringArray  = inputLine.split(" ");


        String operationCommandString = spaceSplitStringArray[0];
        operationCommandString = operationCommandString.trim();
        String operandsString = "";

        String machineBinaryInstruction = commandOpCodeMap.get(operationCommandString.toUpperCase());

        for (int i = 1; i < spaceSplitStringArray.length ; i++) {
            operandsString += spaceSplitStringArray[i];
        }

        operandsString = operandsString.replaceAll("\\s","");

       String commandType =  commandTypeMap.get(operationCommandString.toUpperCase());

       if(commandType.equalsIgnoreCase("R")){


           if(operationCommandString.toUpperCase().equals("SLL") || operationCommandString.toUpperCase().equals("SRL") ) {
                machineBinaryInstruction += parseRTypeShiftInstruction(operandsString);
           } else {
               machineBinaryInstruction += parseRTypeNormalInstruction(operandsString);
           }


       } else if (commandType.equalsIgnoreCase("I")) {

           if(operationCommandString.toUpperCase().equals("LW") || operationCommandString.toUpperCase().equals("SW")) {
               machineBinaryInstruction += parseITypeLoadStoreInstruction(operandsString);
           } else if (operationCommandString.toUpperCase().equals("BEQ") || operationCommandString.toUpperCase().equals("BNEQ")) {
               machineBinaryInstruction += parseITypeBranchInstruction(operandsString);


           } else {
               machineBinaryInstruction += parseITypeIntermediateInstruction(operandsString);

           }


       } else if(commandType.equalsIgnoreCase("J") ) {

           machineBinaryInstruction += parseJTypeInstruction(operandsString);

       }



        return machineBinaryInstruction;

    }


   public String getCommandInHex(String binaryLine) {


        int decimal = Integer.parseInt(binaryLine,2);
        String outputCodeHex = Integer.toString(decimal,16);

        //return outputCodeHex;

        int decimal1 = Integer.parseInt(binaryLine.substring(0,4),2);
        String hex1 = Integer.toString(decimal1, 16);


       int decimal2 = Integer.parseInt(binaryLine.substring(4,8),2);
       String hex2 = Integer.toString(decimal2, 16);


       int decimal3 = Integer.parseInt(binaryLine.substring(8,12),2);
       String hex3 = Integer.toString(decimal3, 16);


       int decimal4 = Integer.parseInt(binaryLine.substring(12,16),2);
       String hex4 = Integer.toString(decimal4, 16);


       int decimal5 = Integer.parseInt(binaryLine.substring(16,20),2);
       String hex5 = Integer.toString(decimal5, 16);

       String newHex = hex1 + hex2 + hex3 + hex4 + hex5;

      // System.out.println("NEWHEX " + newHex);
       //System.out.println("OLDHEX " + outputCodeHex);

       if( outputCodeHex.length() == 5 && !outputCodeHex.equalsIgnoreCase(newHex)) {
           System.out.println("Error in binary to hex converstion");
       }
       return newHex;
    }



    private String parseITypeIntermediateInstruction(String operandString) {
        String[] operandArr = operandString.split(",");

        String binaryResult = "";

        String r1 = registerAddressMap.get( operandArr[1].toLowerCase());
        String r2 = registerAddressMap.get( operandArr[0].toLowerCase());

        int shiftAmount = Integer.parseInt(operandArr[2]);

        String shft = String.format("%8s", Integer.toBinaryString(shiftAmount)).replace(" ", "0");

        if(shiftAmount < 0) {
            //System.out.println("BINARY OF " + shiftAmount + " is " + shft);

            shft = shft.substring(shft.length() - 8,shft.length());
            //System.out.println(shft);
        }
        binaryResult = r1 + r2 + shft;
        return binaryResult;
    }

    private String parseJTypeInstruction(String operandString) {

        int shiftAmount;


        try {
            shiftAmount = Integer.parseInt(operandString);
        } catch (NumberFormatException e) {
            shiftAmount = Integer.parseInt(labelAddressMap.get(operandString));
        }
        String shft = String.format("%8s", Integer.toBinaryString(shiftAmount)).replace(" ", "0");

        return shft + "00000000";

    }

    private String parseITypeBranchInstruction(String operandString) {
        String[] operandArr = operandString.split(",");

        String binaryResult = "";

        String r1 = registerAddressMap.get( operandArr[0].toLowerCase());
        String r2 = registerAddressMap.get( operandArr[1].toLowerCase());

        int shiftAmount ;
                try {
                    shiftAmount = Integer.parseInt(operandArr[2]);
                } catch (NumberFormatException e) {
                    System.out.println(operandArr[2]);
                    System.out.println(labelAddressMap.get(operandArr[2]));
                    shiftAmount = Integer.parseInt(labelAddressMap.get(operandArr[2]));
                }

        String shft = String.format("%8s", Integer.toBinaryString(shiftAmount)).replace(" ", "0");

        binaryResult = r1 + r2 + shft;
        return binaryResult;

    }

    private String parseITypeLoadStoreInstruction(String operandString) {

        operandString = operandString.replaceAll("[(]", ",");
        operandString = operandString.replaceAll("[)]", "");

       // System.out.println(operandString);

        String[] operandArr = operandString.split(",");

        String binaryResult = "";

        String r2 = registerAddressMap.get( operandArr[0].toLowerCase());
        String r1 = registerAddressMap.get( operandArr[2].toLowerCase());



        int shiftAmount = Integer.parseInt(operandArr[1]);

        String shft = String.format("%8s", Integer.toBinaryString(shiftAmount)).replace(" ", "0");

        binaryResult = r1 + r2 + shft;
        return binaryResult;
    }

    public String parseRTypeNormalInstruction(String operandString) {
        String[] operandArr = operandString.split(",");

        String binaryResult = "";

        String rd = registerAddressMap.get( operandArr[0].toLowerCase());
        String r1 = registerAddressMap.get( operandArr[1].toLowerCase());
        String r2 = registerAddressMap.get( operandArr[2].toLowerCase());
        String shft = "0000";
        binaryResult = r1 + r2 + rd + shft;
        return binaryResult;



    }

    public String parseRTypeShiftInstruction(String operandString) {
        String[] operandArr = operandString.split(",");

        String binaryResult = "";

        String r1 = "0000";
        String r2 = registerAddressMap.get( operandArr[1].toLowerCase());
        String rd = registerAddressMap.get( operandArr[0].toLowerCase());




        int shiftAmount = Integer.parseInt(operandArr[2]);

        String shft = String.format("%4s", Integer.toBinaryString(shiftAmount)).replace(" ", "0");

        binaryResult = r1 + r2 + rd + shft;
        return binaryResult;
    }



}
