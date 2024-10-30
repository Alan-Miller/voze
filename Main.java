public class Main {
    public static void main(String[] args) {

        // Command line prompt should have 2 args, pointing to the dictionary file and
        // the file to check
        if (args.length != 2) {
            System.out.println("Usage: java Main.java <dictionary_file_path> <file_to_check_path");
            return;
        }

        Dictionary.create(args[0]);
        TextFile.spellCheck(args[1]);
    }
}

// todo: Flag sentence-starters w/o improper spellings
// todo: Consider flagging capped words in-sentence w/o improper spellings
// todo: Only capture maybe proper nouns w/o improper spellings (starters)
// todo: Check the non-propers case-insensitively
// todo: add tests
// todo: create repo
// todo: add notes about decisions
// todo: add README with instructions
// todo: double-check requirements
// todo: add context
// todo: add package and optional arg
// todo: figure out if i need to do command line differently (not just java *)
// todo: Handles s from "'s"
// todo: consider moving TextFile to other places?
// todo: consider removing "clear values" in Jaro.calc, changing variable scope