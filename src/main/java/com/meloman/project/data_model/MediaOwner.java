package com.meloman.project.data_model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Abstract base class representing an owner of media.
 */

@Getter
@Setter
public abstract class MediaOwner implements Cloneable, Comparable<MediaOwner> {
    protected String id;
    protected String name;
    protected List<MediaItem> ownedItems;
    protected Set<String> urls;

    public MediaOwner(String id, String name) {
        this.id = id;
        this.name = name;
        this.ownedItems = new ArrayList<>();
        this.urls = new HashSet<String>() {
        };
    }

    public MediaOwner(String id, String name, List<MediaItem> ownedItems) {
        this.id = id;
        this.name = name;
        this.ownedItems = ownedItems;
        this.urls = new HashSet<String>();
    }

    public MediaOwner(String id, String name, List<MediaItem> ownedItems, Set<String> urls) {
        this.id = id;
        this.name = name;
        this.ownedItems = ownedItems;
        this.urls = urls;
    }

    @Override
    public int compareTo(MediaOwner other) {
        if (this.name == null && other.name == null) {
            return 0;
        } else if (this.name == null) {
            return -1;
        } else if (other.name == null) {
            return 1;
        } else {
            return this.name.compareToIgnoreCase(other.name);
        }
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
                cloned.setUrls(new HashSet<>(this.urls));
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
