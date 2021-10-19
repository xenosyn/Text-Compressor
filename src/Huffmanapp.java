import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * Compares nodes and sorts them in ascending order
 */
class organise_ascend implements Comparator<Node> {
    @Override
    public int compare(Node n1, Node n2) {
        if(n1.getData()>n2.getData())
            return 1;
        return -1;
    }
}

// get codes for encoded data

public class Huffmanapp {
    public static void main(String[] args) throws IOException {
        Huffmanapp Menuloader = new Huffmanapp();
        Menuloader.init();
    }

    /**
     * Main interface for the program
     * @throws IOException for input error
     */
    public void init() throws IOException {
        clearConsole();
        String mainmenu = """
                ****************************************************************
                                  H U F F M A N     A P P
                ****************************************************************
                               
                  Please enter the number to select your option from below:
                  
                  1. Encoding 
                  2. Decoding
                  3. Language Huffman tree generator
                  4. Language Encoding with generated codes
                  5. Language Decoding  with generated codes
                  -1. End program
                      
                """;
        System.out.println(mainmenu);
        Scanner item = new Scanner(System.in);
        int selection = item.nextInt();

        switch (selection) {
            case -1: {
                clearConsole();
                break;
            }
            case 1: {
                compression();
            }
            case 2:{
                decompressed();
            }
            case 3:{
                generate_test_encoding();
                break;
            }
            case 4:{
                language_encoding();
                break;
            }
            case 5:{
                language_decoding();
                break;
            }
            default:{
                init();
            }
        }
    }

    /**
     * Clears the console
     */
    public static void clearConsole() {
        for (int i = 0; i <= 20; i++) {
            System.out.println("\n");
        }
    }

    /**
     * takes and input of file and performs operations to compress and output a .bin file
     * @throws IOException for input error
     */
    public void compression() throws IOException {
        //need file input
        // input and make hashmap
        int num;
        Node root = null;
        ArrayList<Node> tree = new ArrayList<>();
        System.out.println("Enter file path");
        Scanner item = new Scanner(System.in);
        String file_input = item.nextLine();
        String selection_input = file_input.replace("\\","\\\\");
        String in = inout.readFileAsString(selection_input);
        String[] get_name = selection_input.split("\\\\");
        String name = get_name[get_name.length - 1];
        String Filename = name.substring(0, name.length() - 4);
        Map<Character, Integer> filter_data = new HashMap<>();
        Set<Character> keys = filter_data.keySet();
        Map<Character, byte[]> coded_data = new HashMap<>();
        char[] character = in.toCharArray();
        for (char c : character) {
            if (!filter_data.containsKey(c)) {
                filter_data.put(c, 1);
            } else {
                num = filter_data.get(c);
                num++;
                filter_data.put(c, num);
            }
        }
        // add to tree

        System.out.println("Constructing Tree ....");
        for (Character key : keys) {
            tree.add(new Node(filter_data.get(key), key));
        }

        while (tree.size() != 1) {
            tree.sort(new organise_ascend());
            Node n1 = tree.get(0);
            Node n2 = tree.get(1);
            tree.remove(0);
            tree.remove(0);
            root = new Node(n1.getData() + n2.getData(), n1, n2);
            tree.add(root);
        }

        // generate the codes
        System.out.println("\n Generating code data .... ");
        assert root != null;
        Huffman.code_dictionary(root, "", coded_data);

        System.out.println("\n Encoding file");
        String str_encoded = Huffman.encode(coded_data, in);

        // write to file
        byte[] binary_write = inout.GetBinary(str_encoded);
        try {
            OutputStream outputStream = new FileOutputStream(Filename + "_compressed.bin");
            outputStream.write(binary_write);
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            OutputStream outputStream = new FileOutputStream(Filename + "_compressed.ser");
            ObjectOutputStream myObjectOutStream = new ObjectOutputStream(outputStream);
            myObjectOutStream.writeObject(coded_data);
            myObjectOutStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

            System.out.println("\n");

            String compress_screen = """
                
                Press the following 
                0. To Quit 
                1. To compress a new file
                or any other number to head back to main menu
                                           
                """;
            System.out.println(compress_screen);
            Scanner select = new Scanner(System.in);
            int selection = select.nextInt();
            switch (selection) {
                case 0: {
                    clearConsole();
                    break;
                }
                case 1: {
                    compression();
                }
                default:{
                    init();
                }
        }
    }


    /**
     * takes and input for file and creates new file with decoded data
     * @throws IOException for input error
     */
    public void decompressed() throws IOException {
        System.out.println("Enter file path");
        Scanner item = new Scanner(System.in);
        String selection_input = item.nextLine();
        String[] get_name = selection_input.split("\\\\");
        String name = get_name[get_name.length - 1];
        String Filename = name.substring(0, name.length() - 4);
        String Final_filename = Filename.replace("_compressed","_decompressed");
        byte[] encode = null;
        System.out.println("Getting compressed  file....");
        try {
            byte[] allBytes = Files.readAllBytes(Paths.get(name));
            encode = inout.GetString(allBytes).getBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }

        HashMap<Character, byte[]> newHashMap;

        System.out.println("\n Getting Huffman tree data....");
        try {
            FileInputStream fileInput = new FileInputStream(selection_input.replace(".bin",".ser"));
                ObjectInputStream objectInput
                        = new ObjectInputStream(fileInput);
                newHashMap = (HashMap)objectInput.readObject();

                objectInput.close();
                fileInput.close();
            }

            catch (IOException obj1) {
                obj1.printStackTrace();
                return;
            }

            catch (ClassNotFoundException obj2) {
                System.out.println("Class not found");
                obj2.printStackTrace();
                return;
            }

        System.out.println("\n Decoding ....");

        // Displaying content in "newHashMap.txt" using
        // Iterator

        assert encode != null;
        Huffman.decode(newHashMap,encode,Final_filename);

        System.out.println("\n");
        String decompress_screen = """
                
                Press the following 
                0. To Quit 
                1. To decompress a new file
                or any other number to head back to main menu
           
                """;
        System.out.println(decompress_screen);
        Scanner select = new Scanner(System.in);
        int selection = select.nextInt();
        switch (selection) {
            case 0: {
                clearConsole();
                break;
            }
            case 1: {
                decompressed();
            }
            default:{
                init();
            }
            }
    }

    /**
     * generates huffman codes that are then stored in a serialised file
     * @throws IOException for input error
     */
    public void generate_test_encoding() throws IOException {
        System.out.println("Enter file path");
        Scanner item = new Scanner(System.in);
        String file_input = item.nextLine();
        String selection_input = file_input.replace("\\","\\\\");
        String in = inout.readFileAsString(selection_input);
        Map<Character, byte[]> coded_data = Huffman.getcodes(in + "_");
        try {
            OutputStream outputStream = new FileOutputStream(  "encoding.ser");
            ObjectOutputStream myObjectOutStream = new ObjectOutputStream(outputStream);
            myObjectOutStream.writeObject(coded_data);
            myObjectOutStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("\n");

        String compress_screen = """
                
                Press the following 
                0. To Quit 
                1. To generate a new encoding file
                or any other number to head back to main menu
                                           
                """;
        System.out.println(compress_screen);
        Scanner select = new Scanner(System.in);
        int selection = select.nextInt();
        switch (selection) {
            case 0: {
                clearConsole();
                break;
            }
            case 1: {
                generate_test_encoding();
            }
            default:{
                init();
            }
        }
    }

    /**
     * generates decoded data by grabbing huffman code file decoding the input file.
     * @throws IOException for input error
     */
    public void language_decoding() throws IOException {
        System.out.println("Enter file path of .bin file ");
        Scanner item2 = new Scanner(System.in);
        String selection_input = item2.nextLine();
        String[] get_name = selection_input.split("\\\\");
        String name = get_name[get_name.length - 1];
        String Filename = name.substring(0, name.length() - 4);
        String Final_filename = Filename.replace("_compressed","_decompressed");
        byte[] encode = null;
        System.out.println("Getting compressed  file....");
        try {
            byte[] allBytes = Files.readAllBytes(Paths.get(name));
            encode = inout.GetString(allBytes).getBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }
        HashMap<Character, byte[]> newHashMap;
        System.out.println("\n Getting Huffman tree data....");
        try {
            FileInputStream fileInput = new FileInputStream(String.valueOf(Paths.get("encoding.ser")));
            ObjectInputStream objectInput
                    = new ObjectInputStream(fileInput);
            newHashMap = (HashMap)objectInput.readObject();

            objectInput.close();
            fileInput.close();
        }
        catch (IOException obj1) {
            obj1.printStackTrace();
            return;
        }
        catch (ClassNotFoundException obj2) {
            System.out.println("Class not found");
            obj2.printStackTrace();
            return;
        }
        // Displaying content in "newHashMap.txt" using
        // Iterator
        assert encode != null;
        Huffman.decode(newHashMap,encode,Final_filename);


        System.out.println("\n");

        String compress_screen = """
                
                Press the following 
                0. To Quit 
                1. To decode again 
                or any other number to head back to main menu
                                           
                """;
        System.out.println(compress_screen);
        Scanner select = new Scanner(System.in);
        int selection = select.nextInt();
        switch (selection) {
            case 0: {
                clearConsole();
                break;
            }
            case 1: {
                language_decoding();
            }
            default:{
                init();
            }
        }
    }

    /**
     * generates huffman code from the huffman coding file and outputs to a file.
     * @throws IOException
     */
    public void language_encoding() throws IOException {
        System.out.println("Enter file path");
        Scanner item3 = new Scanner(System.in);
        String file_input = item3.nextLine();
        String selection_input = file_input.replace("\\","\\\\");
        String[] get_name = selection_input.split("\\\\");
        String name = get_name[get_name.length - 1];
        String Filename = name.substring(0, name.length() - 4);
        String in = inout.readFileAsString(selection_input);
        HashMap<Character, byte[]> newHashMap;
        try {
            FileInputStream fileInput = new FileInputStream(String.valueOf(Paths.get("encoding.ser")));
            ObjectInputStream objectInput
                    = new ObjectInputStream(fileInput);
            newHashMap = (HashMap)objectInput.readObject();

            objectInput.close();
            fileInput.close();
        }

        catch (IOException obj1) {
            obj1.printStackTrace();
            return;
        }

        catch (ClassNotFoundException obj2) {
            System.out.println("Class not found");
            obj2.printStackTrace();
            return;
        }
        String str_encoded = Huffman.encode(newHashMap,in);
        byte[] binary_write = inout.GetBinary(str_encoded);
        try {
            OutputStream outputStream = new FileOutputStream(Filename + "_compressed_with_English.bin");
            outputStream.write(binary_write);
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("\n");

        String compress_screen = """
                
                Press the following 
                0. To Quit 
                1. To compress a new file
                or any other number to head back to main menu
                                           
                """;
        System.out.println(compress_screen);
        Scanner select = new Scanner(System.in);
        int selection = select.nextInt();
        switch (selection) {
            case 0: {
                clearConsole();
                break;
            }
            case 1: {
                language_encoding();
            }
            default:{
                init();
            }
        }

    }
}









