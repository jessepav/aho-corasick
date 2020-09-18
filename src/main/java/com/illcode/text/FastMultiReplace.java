package com.illcode.text;

import org.ahocorasick.trie.*;

import static org.ahocorasick.trie.Trie.TrieBuilder;

import java.util.Collection;
import java.util.Map;

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
}
