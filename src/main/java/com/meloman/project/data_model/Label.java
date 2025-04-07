package com.meloman.project.data_model;

import lombok.Getter;
import lombok.Setter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Represents a music label, which may have sub-labels and contact information.
 * Labels are used to manage and organize media items such as albums and tracks.
 */

@Getter
@Setter
public class Label extends MediaOwner {
    private String parentLabel;
    private Set<String> subLabels;
    private String contactInfo;

    /**
     * Constructs a new Label object with the specified details.
     *
     * @param id The unique identifier for the label.
     * @param name The name of the label.
     * @param parentLabel The parent label under which this label operates.
     * @param subLabels A set of sub-labels under this label.
     * @param ownedItems A list of media items owned by this label.
     * @param urls A set of URLs related to this label (e.g., official website).
     * @param contactInfo Contact information for the label (e.g., email or phone number).
     */

    public Label(String id, String name, String parentLabel, Set<String> subLabels,
                 List<MediaItem> ownedItems, Set<String> urls, String contactInfo) {
        super(id, name, ownedItems, urls);
        this.parentLabel = parentLabel;
        this.subLabels = subLabels;
        this.contactInfo = contactInfo;
    }

    public Label(String labelId, String labelName) {
        super(labelId, labelName);

    }

    /**
     * Creates a deep clone of the label, including cloning the sub-labels set.
     *
     * @return A new Label object that is a copy of the current label.
     */

    @Override
    public Label clone() {
        Label cloned = (Label) super.clone();
        if (this.subLabels != null) {
            cloned.setSubLabels(new HashSet<>(this.subLabels));
        }
        return cloned;
    }

    /**
     * Returns a string representation of the label, including the label's ID, name,
     * parent label, sub-labels, and contact information.
     *
     * @return A string that describes the label and its details.
     */

    @Override
    public String toString() {
        return "Label [" + super.toString() +
                ", parentLabel=" + parentLabel +
                ", subLabels=" + subLabels +
                ", contactInfo=" + contactInfo + "]";
    }
}
