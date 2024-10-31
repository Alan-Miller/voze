# Voze Spell Checker

## Instructions

1. In a shell terminal, `cd` into a directory where you wish to clone the repo, for example:

    ```shell
    cd code/java/
    ```

1. Clone the repo

    ```shell
    git clone git@github.com:Alan-Miller/voze.git
    ```

1. `cd` into the `voze` folder

    ```shell
    cd voze/
    ```

1. To run the app with the the supplied text file, run the command below as-is in the command line. Or, to use a different text file, replace `./file-to-check.txt` with the path to a different .txt file.

    ```shell
    java src/main/java/Main.java ./dictionary.txt ./file-to-check.txt
    ```

1. Optionally, run the unit tests found in `src/test/java` in a text editor.

## Decisions I made

### Choice of language and the Jaro algorithm

The word suggestions requirement presented one of the most interesting problems. Solutions I considered:

#### _1. Use Elixir's out-of-the-box string comparison tools_

```elixir
iex> String.jaro_distance("easy", "peasy")
0.9333333333333332

iex> String.jaro_distance("lemon", "squeezy")
0.44761904761904764
```

Elixir is a language I've worked in for years, and its `String` type has a built-in function for computing the similarity between strings, outputting a number between 0 (complete mismatch) and 1 (exact match). This would have provided an easy solution for word suggestions.
Ultimately, I didn't think an Elixir app like this would leave us with as much to talk about. The language itself and its functional nature might be less relevant to Voze, and Elixir's `String.jaro_distance` function would do a lot of the heavy lifting, leaving less for me to do. I decided to write the app in Java (and get those sweet extra credit points).

#### _2. Third-party Java packages_

There are several different algorithms for calculating similarity between strings. I considered simply finding some lightweight Java packages to do the job, even considering multiple packages based on their algorithms' different strengths. In a real-world scenario, I would seriously consider this approach. No need to solve a problem someone else already invested a lot of effort into solving and wrapping up in a nice, lightweight package. And installing multiple packages would let our team consider some A/B testing to see which package provided the best word suggestions. In a code challenge situation however, I thought writing the code myself would create more opportunities for discussion.

#### _3. Write a Jaro Score algorithm myself in Java_

Writing the algorithm myself was a lot of fun and hopefully provides more to talk about. I did some research into different word-comparison algorithms like the Levenshtein Distance Equation, the Jaro Score Algorithm, Jaro-Winkler Distance, and a few others. Considering the time restraints and complexity of some of the algorithms, as well as the strengths I perceived in each of them for the task at hand, I found Jaro to be a relatively straightforward and understandable approach, especially when checking words written in natural English with intention (as opposed to comparing randomized strings or fuzzy-matching on crazy-looking usernames or something).

The key approach of the algorithm is to:

1. Calculate the number of character matches, regardless of position within the allowed distance
1. Calculate the number of transpositions required to put all characters in correct position
1. Input these in a formula to calculate a score between 0 (no match) and 1 (perfect match)

After reading up on the key concepts, I was confident I could implement a working version in Java. Not all people seem to understand or explain the parts of the Jaro algorithm in the same way. For example, my calculation of transpositions is one of the simpler parts of my algorithm and might differ from how others do it. However, I'm happy with the resulting word suggestions I'm seeing, as well as the Jaro scores being calculated.

### Proper nouns

#### _The problem_

One interesting problem was how to handle proper nouns. The challenge prompt merely states they should be handled, without stating how. One solution might be to expand the dictionary.txt file to include a list of proper nouns, though this might only be useful if we have some idea of which proper nouns users are likely to use (a list of common names, or a glossary of terms for a given industry). A more robust app might use a large language model (LLM) to make this decision by examining parts of speech or relationships between words.

#### _Observations_

I assumed the text file to check would include natural (but sometimes misspelled) language about a variety of things and with just about any type of proper noun. Without advanced tools for identifying proper nouns, I was left with these observations:

- Capitalized words are often proper nouns, but they could just be the first word of a sentence.
- The first word of a sentence could be a proper noun or capitalized improper noun (or other).
- A proper noun—or the first word in a sentence—might have been capitalized properly.
- Proper nouns sometimes have corresponding lowercase spellings ("Chase" and "chase").

#### _Addressing edge cases_

The "Misspellings" section of the output outputs any lowercase word with no exact dictionary match, assuming it must be misspelled. The "Possible Misspellings" section displays words that are capitalized at the start of sentences and do not have an exact dictionary match. It ignores capitalized words mid-sentence, assuming they are correctly capitalized proper nouns (which we probably cannot verify with our given tool set), and it ignores sentence-starting words that _do_ have dictionary matches, assuming they are either non-proper nouns or at least correctly spelled proper nouns. But without an exhaustive lookup file of proper nouns, the choice seems to be between:

- _Ignoring_ mid-sentence capitalized words not in the dictionary, assuming they are properly spelled proper nouns,
- _Flagging_ mid-sentence capitalized words not in the dictionary, assuming they are misspelled

I decided to err on the side of not overwhelming the user with a ton of false positives (proper nouns that are in fact correctly spelled). With an imperfect system for identifying proper nouns in edge cases, I went with a solution that identifies words that are almost certainly misspelled while also allowing the user to double-check some edge cases where the program cannot reasonably know.

- Mid-sentence capitalized words are assumed to be _correctly_ spelled proper nouns regardless of whether they have an exact dictionary match
- Sentence-starting words are assumed to be _incorrectly_ spelled 

## Final Notes

- This README has been spell-checked with the Voze Spell Checker
- Thank you for considering me  :)
