# Voze Spell Checker

## Instructions

1. Clone the repo

    ```shell
    git clone git@github.com:Alan-Miller/Voze.git
    ```

1. Run this command in the command line:

## Decisions I made

#### Jaro algorithm

- Determines matching characters
- Simplifies transpositions
- Uses common Jaro formula

#### Natural English

- Algorithm assumes intentional writing, with misspellings
- Fuzzy checker for natural words, not usernames and random strings

#### 

## To-do
1. Add README instructions
1. Add README notes about assumptions and decisions
1. Flag sentence-starters w/o improper spellings
1. Consider flagging capped words in-sentence w/o improper spellings
1. Only capture maybe proper nouns w/o improper spellings (starters)
1. Check the non-propers case-insensitively
1. Add context
1. Handles s from "'s"
1. Consider moving TextFile to other place?
1. Consider removing "clear values" in Jaro.calc, changing variable scope
1. Add package and optional arg
1. Command line
1. Tests
1. Re-check all requirements


## Questions

1. JAR file better?
1. tests? Junit?
1. conventions? advice? problems?
1. context?
1. H Include package?
1. H Organize files?
