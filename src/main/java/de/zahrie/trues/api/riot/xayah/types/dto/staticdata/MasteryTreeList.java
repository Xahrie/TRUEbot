package de.zahrie.trues.api.riot.xayah.types.dto.staticdata;

import java.io.Serial;
import java.util.List;

import de.zahrie.trues.api.riot.xayah.types.dto.DataObject;

public class MasteryTreeList extends DataObject {
    @Serial
    private static final long serialVersionUID = -3220972376433060053L;
    private List<MasteryTreeItem> masteryTreeItems;

    /*
     * (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if(this == obj) {
            return true;
        }
        if(obj == null) {
            return false;
        }
        if(getClass() != obj.getClass()) {
            return false;
        }
        final MasteryTreeList other = (MasteryTreeList)obj;
        if(masteryTreeItems == null) {
          return other.masteryTreeItems == null;
        } else return masteryTreeItems.equals(other.masteryTreeItems);
    }

    /**
     * @return the masteryTreeItems
     */
    public List<MasteryTreeItem> getMasteryTreeItems() {
        return masteryTreeItems;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (masteryTreeItems == null ? 0 : masteryTreeItems.hashCode());
        return result;
    }

    /**
     * @param masteryTreeItems
     *        the masteryTreeItems to set
     */
    public void setMasteryTreeItems(final List<MasteryTreeItem> masteryTreeItems) {
        this.masteryTreeItems = masteryTreeItems;
    }
}
