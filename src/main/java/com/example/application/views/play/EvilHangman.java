package com.example.application.views.play;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class EvilHangman {
    private HashMap<String, ArrayList<String>> dictionary;
    private String wordPattern;
    private int numberOfGuesses;
    private  ArrayList<String> guesses;

    public EvilHangman() {
        dictionary = new HashMap<>();
        wordPattern = initializeDictionary();
        numberOfGuesses = 15;
        guesses = new ArrayList<>();
    }

    public String getWordPattern() {
        return wordPattern;
    }

    public HashMap<String, ArrayList<String>> getDictionary() {
        return dictionary;
    }

    public int getNumberOfGuesses() {
        return numberOfGuesses;
    }

    public ArrayList<String> getGuesses() {
        return guesses;
    }

    private String initializeDictionary() {
        HashMap<Integer, ArrayList<String>> entireDictionary = new HashMap<>();

        ArrayList<String> data = new ArrayList<>();

        /*
         code for URL reading (lines 50-66) retrieved from JavaTPoint Tutorial
         https://www.javatpoint.com/java-get-data-from-url
        */

        try
        {
            URL url = new URL("http://nifty.stanford.edu/2011/schwarz-evil-hangman/dictionary.txt");
            URLConnection urlConnection = url.openConnection(); // creating a urlconnection object

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line;

            while ((line = bufferedReader.readLine()) != null)
            {
                data.add(line);
            }
            bufferedReader.close();
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        for (String word : data.stream().map(v -> v += "@").collect(Collectors.joining()).split("@")) {
            if (!entireDictionary.containsKey(word.length())) {
                entireDictionary.put(word.length(), new ArrayList<>());
            }
            entireDictionary.get(word.length()).add(word);
        }

        ArrayList<Integer> wordLengths = new ArrayList<>(entireDictionary.keySet());

        int wordLength = wordLengths.get((int) (Math.random() * (wordLengths.size() - 1)));

        String chosenWordPattern = Arrays.stream(new String[wordLength]).map(v -> v = "_").collect(Collectors.joining());

        dictionary.put(chosenWordPattern, entireDictionary.get(wordLength));

        return chosenWordPattern;
    }

    private String createWordPattern (String word, String letter) {
        StringBuilder newWordPattern = new StringBuilder();
        for (char ch : word.toCharArray()) {
            String currentChar = "" + ch + "";
            if (currentChar.equalsIgnoreCase(letter)) {
                newWordPattern.append(letter);
            } else {
                newWordPattern.append("_");
            }
        }
        return newWordPattern.toString();
    }

    private int createNumberOfChanges(String word) {
        int numChanges = 0;
        char[] currentWord = word.toCharArray();
        char[] currentWordPattern = wordPattern.toCharArray();

        for (int i = 0; i < currentWord.length; i++) {
            if (currentWord[i] != '_' && currentWordPattern[i] == '_') {
                numChanges++;
            }
        }

        return numChanges;
    }

    public int newGuess(String guess) {

        System.out.println("{");
        guesses.add(guess);

        HashMap<String, ArrayList<String>> tempDictionary = new HashMap<>();

        ArrayList<String> wordList = dictionary.get(wordPattern);

        for (String word : wordList.stream().map(v -> v += "@").collect(Collectors.joining()).split("@")) {
            String currentWordPattern = createWordPattern(word, guess);
            if (!tempDictionary.containsKey(currentWordPattern)) {
                tempDictionary.put(currentWordPattern, new ArrayList<>());
            }
            tempDictionary.get(currentWordPattern).add(word);
        }

        int greatestSize = 0;

        for (String currentWordPattern : tempDictionary.keySet()) {
            wordList = tempDictionary.get(currentWordPattern);

            if (wordList.size() > greatestSize) {
                greatestSize = wordList.size();
            }
        }

        ArrayList<String> wordPatterns = new ArrayList<>();

        for (String currentWordPattern : tempDictionary.keySet()) {
            wordList = tempDictionary.get(currentWordPattern);

            if (wordList.size() == greatestSize) {
                wordPatterns.add(currentWordPattern);
            }
        }

        String minimumChangesWordPattern = wordPatterns.get(0);
        int minimumChanges = createNumberOfChanges(wordPatterns.get(0));

        for (String currentWordPattern : wordPatterns.stream().map(v -> v += "@").collect(Collectors.joining()).split("@")) {
            int numChanges = createNumberOfChanges(currentWordPattern);

            if (numChanges < minimumChanges) {
                minimumChangesWordPattern = currentWordPattern;
                minimumChanges = numChanges;
            }
        }

        wordPattern = combine(wordPattern, minimumChangesWordPattern);


        ArrayList<String> newWordlist = tempDictionary.get(minimumChangesWordPattern);
        dictionary = new HashMap<>();
        dictionary.put(wordPattern, newWordlist);

        System.out.println("dictionary: " + dictionary);

        if (minimumChanges == 0) {
            numberOfGuesses--;
        }

        return minimumChanges;
    }

    private String combine(String string1, String string2) {
        StringBuilder newString = new StringBuilder();
        int num = 0;

        if (string1.length() < string2.length()) {
            num = string1.length();
        }
        if (string2.length() < string1.length()) {
            num = string2.length();
        }
        if (string1.length() == string2.length()) {
            num = string1.length();
        }

        for (int i = 0; i < num; i++) {
            boolean isString1AnUnderscore = !(string1.charAt(i) <= 96 || string1.charAt(i) >= 123);
            boolean isString2AnUnderscore = !(string2.charAt(i) <= 96 || string2.charAt(i) >= 123);

            if (string1.charAt(i) == '_' && isString2AnUnderscore) {
                newString.append(string2.charAt(i));
            }
            if (string2.charAt(i) == '_' && isString1AnUnderscore) {
                newString.append(string1.charAt(i));
            }
            if (string1.charAt(i) == '_' && string2.charAt(i) == '_') {
                newString.append("_");
            }
            if (isString1AnUnderscore && isString2AnUnderscore) {
                newString.append(string1.charAt(i));
            }
        }
        return newString.toString();
    }

    public String getRandomWord() {
        ArrayList<String> wordList = dictionary.get(wordPattern);
        return wordList.get((int) (Math.random() * wordList.size()));
    }

    public static void main(String[] args) {
        EvilHangman evilHangman = new EvilHangman();
        for (String letter : new String[] {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"}) {
            evilHangman.newGuess(letter);
        }
    }
}
