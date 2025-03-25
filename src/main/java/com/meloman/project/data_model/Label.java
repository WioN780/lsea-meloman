package com.meloman.project.data_model;

import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Label extends MediaOwner {
    private String parentLabel;
    private List<String> subLabels;
    private String contactInfo;

    public Label(String id, String name, String parentLabel, List<String> subLabels,
                 List<MediaItem> ownedItems, List<String> urls, String contactInfo) {
        super(id, name, ownedItems, urls);
        this.parentLabel = parentLabel;
        this.subLabels = subLabels;
        this.contactInfo = contactInfo;
    }

    @Override
    public Label clone() {
        Label cloned = (Label) super.clone();
        if (this.subLabels != null) {
            cloned.setSubLabels(new ArrayList<>(this.subLabels));
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
