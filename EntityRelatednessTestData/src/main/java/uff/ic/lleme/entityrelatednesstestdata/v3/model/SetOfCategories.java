package uff.ic.lleme.entityrelatednesstestdata.v3.model;

import java.util.HashMap;
import java.util.Map;

public class SetOfCategories {

    private static SetOfCategories setOfCategories = null;
    private Map<String, Category> categories = new HashMap<>();

    private SetOfCategories() {

    }

    public static SetOfCategories getInstance() {
        if (setOfCategories == null)
            setOfCategories = new SetOfCategories();
        return setOfCategories;
    }

    public Category addCategory(String label) throws Exception {
        if (categories.containsKey(label))
            return categories.get(label);
        else {
            Category category = new Category(label);
            categories.put(category.getLabel(), category);
            return category;
        }

    }

    public Category getCategory(String label) {
        return categories.get(label);
    }
}
