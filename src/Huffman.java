import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Stream;

public class Huffman {
    /**
     *
     * @param root - main root node
     * @param s - gets coded data
     * @param code_dict - stores the coded data and character into hashmap
     */
    public static void code_dictionary(Node root, String s, Map<Character, byte[]> code_dict) {
        if (root.getLeft() == null && root.getRight() == null && Character.isDefined(root.getSymbol())) {
            code_dict.put(root.getSymbol(), s.getBytes());

            return;
        }
        code_dictionary(root.getLeft(), s + "0", code_dict);
        code_dictionary(root.getRight(), s + "1", code_dict);
    }

    /**
     *
     * @param root - root node
     * @param s - gets number encoded char
     * @param c - get character
     * @param encoded - iterates to get encoded string
     */
    public static void get_Huffmancode(Node root, String s, char c, ArrayList<String> encoded) {
        if (root.getLeft() == null && root.getRight() == null && Character.isDefined(root.getSymbol())) {
            if (Objects.equals(root.getSymbol(), c)) {
                encoded.add(s);
            }
            return;
        }
        get_Huffmancode(root.getLeft(), s + "0", c, encoded);
        get_Huffmancode(root.getRight(), s + "1", c, encoded);
    }

    /**
     *
     * @param a - gets byte array list
     * @return byte array
     */
    public static byte[] buff(ArrayList<Byte> a){
        byte[] buffer_array = new byte[a.size()];
        for(int i=0 ; i<a.size();i++){
            buffer_array[i]=a.get(i);
        }
        return buffer_array;
    }

    /**
     *
     * @param huffman_code - coded hashmap
     * @param encode_this - String to append encoded data to
     * @return encoded String
     *
     */
    public static String encode(Map<Character, byte[]> huffman_code , String encode_this) {
        char[] raw = encode_this.toCharArray();
        StringBuilder encode_string = new StringBuilder();
        for (char bit : raw) {
            if (huffman_code.containsKey(bit)) {
                encode_string.append(new String(huffman_code.get(bit)));
            }
            else{
                encode_string.append(new String(huffman_code.get('_')));
            }
        }
        return encode_string.toString();
    }

    /**
     *
     * @param huffman_code - get  hashmap with huffman code
     * @param decode_this - get byte data
     * @param name - File name to be assigned when finished decoding
     */
    public static void decode(HashMap<Character, byte[]> huffman_code, byte[] decode_this, String name) {
        ArrayList<Byte> buffer = new ArrayList<>();
        StringBuilder decode_string = new StringBuilder();
        for (byte bit : decode_this) {
            buffer.add(bit);
            for (Character code : huffman_code.keySet()) {
                byte[] c1 = huffman_code.get(code);
                byte[] buffee_temp = buff(buffer);
                if (c1.length == buffee_temp.length && Arrays.equals(c1, buffee_temp)) {
                    decode_string.append(code);
                    buffer.clear();
                }
            }
        }
        try {
            FileWriter myWriter = new FileWriter(name + ".txt");
            myWriter.write(decode_string.toString());
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public static Map<Character, byte[]> getcodes(String in){
        int num;
        Node root = null;
        ArrayList<Node> tree = new ArrayList<>();
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

        for (Character key : keys) {
            tree.add(new Node(filter_data.get(key), key));
        }

        while (1 < tree.size()) {
            tree.sort(new organise_ascend());
            Node n1 = tree.get(0);
            Node n2 = tree.get(1);
            tree.remove(0);
            tree.remove(0);
            root = new Node(n1.getData() + n2.getData(), n1, n2);
            tree.add(root);
        }

        assert root != null;
        Huffman.code_dictionary(root, "", coded_data);
        return coded_data;
    }

}
