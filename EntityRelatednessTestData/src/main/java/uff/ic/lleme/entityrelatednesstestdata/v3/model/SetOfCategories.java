package uff.ic.lleme.entityrelatednesstestdata.v3.model;

import java.util.HashMap;
import java.util.Map;

public class SetOfCategories {

    private static SetOfCategories setOfCategories = null;
    private final Map<String, Category> CATEGORIES = new HashMap<>();

    private SetOfCategories() {

    }

    public static SetOfCategories getInstance() {
        if (setOfCategories == null)
            setOfCategories = new SetOfCategories();
        return setOfCategories;
    }

    public Category addCategory(String label) throws Exception {
        if (CATEGORIES.containsKey(label))
            return CATEGORIES.get(label);
        else {
            Category category = new Category(label);
            CATEGORIES.put(category.getLabel(), category);
            return category;
        }

    }

    public Category getCategory(String label) {
        return CATEGORIES.get(label);
    }
}
