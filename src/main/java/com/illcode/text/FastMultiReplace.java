package com.illcode.text;

import org.ahocorasick.trie.*;

import static org.ahocorasick.trie.Trie.TrieBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

/* See https://stackoverflow.com/a/40836618/84923*/

/**
 * A class to perform fast replacement of multiple strings using the Aho-Corasick algorithm.
 */
public class FastMultiReplace
{
    private Map<String,String> replacements;
    private Trie trie;

    /**
     * Construct an empty instance.
     */
    public FastMultiReplace() {
    }

    /**
     * Initialize the trie for performing multi-search-and-replace.
     * @param replacements map from search strings to replacements
     * @param ignoreOverlaps true if overlapping search strings should be ignored.
     * @param ignoreCase true if case should be ignored
     * @param onlyWholeWords only whole words will be matched by search strings
     */
    public void initTrie(Map<String,String> replacements,
                         boolean ignoreOverlaps, boolean ignoreCase, boolean onlyWholeWords) {
        this.replacements = replacements;
        final TrieBuilder builder = Trie.builder();
        if (ignoreOverlaps)
            builder.ignoreOverlaps();
        if (ignoreCase)
            builder.ignoreCase();
        if (onlyWholeWords)
            builder.onlyWholeWords();
        builder.addKeywords(replacements.keySet());
        trie = builder.build();
    }

    /**
     * Perform text replacement based on the strings and settings passed to {@link #initTrie initTrie()}.
     * @param text text
     * @return text with replacements applied
     */
    public String replace(String text) {
        if (trie == null || replacements == null)
            return text;

        final StringBuilder sb = new StringBuilder(text.length() * 5 / 4);

        final Collection<Emit> emits = trie.parseText(text);
        int prevIndex = 0;
        for (Emit emit : emits) {
            final int matchIndex = emit.getStart();
            sb.append(text.substring(prevIndex, matchIndex));
            sb.append(replacements.get(emit.getKeyword()));
            prevIndex = emit.getEnd() + 1;
        }
        sb.append(text.substring(prevIndex));
        return sb.toString();
    }

    public static void main(String[] args) throws IOException {
        final String helpText = "Usage: FastMultiReplace <input file> <search-term> <replacement> [...]";

        if (args.length < 3)
            System.out.println(helpText);
        else if (args.length % 2 == 0)
            System.out.println(helpText);
        else {
            String text = new String(Files.readAllBytes(Paths.get(args[0])));
            Map<String,String> replacements = new TreeMap<>();
            for (int i = 1; i < args.length; i += 2)
                replacements.put(args[i], args[i+1]);
            FastMultiReplace fmr = new FastMultiReplace();
            fmr.initTrie(replacements, true, false, false);
            System.out.print(fmr.replace(text));
        }

    }
}
