package com.meloman.project.data_model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract base class representing an owner of media.
 */

@Getter
@Setter
public abstract class MediaOwner implements Cloneable {
    protected String id;
    protected String name;
    protected List<MediaItem> ownedItems;
    protected List<String> urls;

    public MediaOwner(String id, String name) {
        this.id = id;
        this.name = name;
        this.ownedItems = new ArrayList<>();
        this.urls = new ArrayList<>();
    }

    public MediaOwner(String id, String name, List<MediaItem> ownedItems) {
        this.id = id;
        this.name = name;
        this.ownedItems = ownedItems;
        this.urls = new ArrayList<>();
    }

    public MediaOwner(String id, String name, List<MediaItem> ownedItems, List<String> urls) {
        this.id = id;
        this.name = name;
        this.ownedItems = ownedItems;
        this.urls = urls;
    }

    @Override
    public MediaOwner clone() {
        try {
            MediaOwner cloned = (MediaOwner) super.clone();
            if (this.ownedItems != null) {
                List<MediaItem> clonedItems = new ArrayList<>();
                for (MediaItem item : this.ownedItems) {
                    clonedItems.add(item.clone());
                }
                cloned.setOwnedItems(clonedItems);
            }
            if (this.urls != null) {
                cloned.setUrls(new ArrayList<>(this.urls));
            }
            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e);
        }
    }

    @Override
    public String toString() {
        return "MediaOwner [id=" + id + ", name=" + name + ", urls=" + urls + "]";
    }
}
