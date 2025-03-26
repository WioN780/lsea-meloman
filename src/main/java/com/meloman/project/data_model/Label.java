package com.meloman.project.data_model;

import lombok.Getter;
import lombok.Setter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
public class Label extends MediaOwner {
    private String parentLabel;
    private Set<String> subLabels;
    private String contactInfo;

    public Label(String id, String name, String parentLabel, Set<String> subLabels,
                 List<MediaItem> ownedItems, Set<String> urls, String contactInfo) {
        super(id, name, ownedItems, urls);
        this.parentLabel = parentLabel;
        this.subLabels = subLabels;
        this.contactInfo = contactInfo;
    }

    @Override
    public Label clone() {
        Label cloned = (Label) super.clone();
        if (this.subLabels != null) {
            cloned.setSubLabels(new HashSet<>(this.subLabels));
        }
        return cloned;
    }

    @Override
    public String toString() {
        return "Label [" + super.toString() +
                ", parentLabel=" + parentLabel +
                ", subLabels=" + subLabels +
                ", contactInfo=" + contactInfo + "]";
    }
}
