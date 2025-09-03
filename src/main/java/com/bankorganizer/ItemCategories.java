package com.bankorganizer;

import java.util.*;

public class    ItemCategories
{
    // categoryId → list of lowercase match patterns
    public static final Map<Integer, List<String>> CATEGORY_PATTERNS = new HashMap<>();

    static
    {
        CATEGORY_PATTERNS.put(1, Arrays.asList("helm", "full helm", "med helm"));
        CATEGORY_PATTERNS.put(2, Arrays.asList("attack potion", "super attack"));
        CATEGORY_PATTERNS.put(3, Arrays.asList("rune", "air rune", "mind rune"));
        CATEGORY_PATTERNS.put(4, Arrays.asList("shark", "lobster"));
        CATEGORY_PATTERNS.put(5, Arrays.asList("dragon", "dragon platebody", "dragon scimitar"));
    }
}
