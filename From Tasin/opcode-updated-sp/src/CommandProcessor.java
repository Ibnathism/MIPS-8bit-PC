import java.util.HashMap;

public class CommandProcessor {

    HashMap<String,String>  commandOpCodeMap ;
    HashMap<String,String> commandTypeMap;
    HashMap<String,String> registerAddressMap;

    public CommandProcessor() {
         commandOpCodeMap = new HashMap<>();
        commandTypeMap = new HashMap<>();
        registerAddressMap = new HashMap<>();

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

    public  String parseCommandLine(String inputLine) {

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
           } else if (operationCommandString.toUpperCase().equals("BNE") || operationCommandString.toUpperCase().equals("BNEQ")) {
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

        return outputCodeHex;
    }



    private String parseITypeIntermediateInstruction(String operandString) {
        String[] operandArr = operandString.split(",");

        String binaryResult = "";

        String r1 = registerAddressMap.get( operandArr[1].toLowerCase());
        String r2 = registerAddressMap.get( operandArr[0].toLowerCase());

        int shiftAmount = Integer.parseInt(operandArr[2]);

        String shft = String.format("%8s", Integer.toBinaryString(shiftAmount)).replace(" ", "0");

        binaryResult = r1 + r2 + shft;
        return binaryResult;
    }

    private String parseJTypeInstruction(String operandString) {

        int shiftAmount = Integer.parseInt(operandString);
        String shft = String.format("%8s", Integer.toBinaryString(shiftAmount)).replace(" ", "0");

        return shft + "00000000";

    }

    private String parseITypeBranchInstruction(String operandString) {
        String[] operandArr = operandString.split(",");

        String binaryResult = "";

        String r1 = registerAddressMap.get( operandArr[0].toLowerCase());
        String r2 = registerAddressMap.get( operandArr[1].toLowerCase());

        int shiftAmount = Integer.parseInt(operandArr[2]);

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
