public class Main {
    public static void main(String[] args) {

        if (args.length == 2) {
            Dictionary dictionary = new Dictionary(args[0]);
            TextFile textFile = new TextFile(args[1], dictionary);

            textFile.spellCheck();
        } else {
            System.out.println(
                    "Usage: java src/main/java/Main.java <dictionary_file_path> <file_to_check_path>");
        }

    }
}
