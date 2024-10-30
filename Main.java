public class Main {
    public static void main(String[] args) {

        // Command line prompt should have 2 args, pointing to the dictionary file and
        // the file to check
        if (args.length < 2) {
            System.out.println("Usage: java Main.java <dictionary_file_path> <file_to_check_path> -jw(OPTIONAL)");
            return;
        }
        if (args.length == 3 && args[2].equals("-jw")) {

            System.out.println("package!");
            return;
        }

        Dictionary.create(args[0]);
        TextFile.spellCheck(args[1]);
    }
}
