package de.zahrie.trues.api.riot.xayah.types.data.staticdata;

import java.io.Serial;

import de.zahrie.trues.api.riot.xayah.types.data.CoreData;

public class ReforgedRuneSlot extends CoreData.ListProxy<Integer> {
    @Serial
    private static final long serialVersionUID = -7789967190560606286L;

    public ReforgedRuneSlot() {
        super();
    }

    public ReforgedRuneSlot(final int initialCapacity) {
        super(initialCapacity);
    }
}
