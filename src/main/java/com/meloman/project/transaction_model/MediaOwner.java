package com.meloman.project.transaction_model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Abstract base class representing an owner of media items.
 * This class provides common fields and behavior for entities
 * that possess media, such as artists, albums, or playlists.
 */
@Getter
@Setter
public abstract class MediaOwner implements Serializable, Cloneable, Comparable<MediaOwner> {

    /**
     * Unique identifier for the version of serialised item.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Unique identifier for the media owner.
     */
    protected String id;

    /**
     * Name of the media owner.
     */
    protected String name;

    /**
     * List of media items owned by the media owner.
     */
    protected List<MediaItem> ownedItems;

    /**
     * Set of associated URLs for the media owner (e.g., external links).
     */
    protected Set<String> urls;

    /**
     * Constructs a MediaOwner with the given ID and name.
     * Initializes an empty list of media items and URLs.
     *
     * @param id   the unique identifier
     * @param name the name of the media owner
     */
    public MediaOwner(String id, String name) {
        this.id = id;
        this.name = name;
        this.ownedItems = new ArrayList<>();
        this.urls = new HashSet<>();
    }

    /**
     * Constructs a MediaOwner with the given ID, name, and owned items.
     * Initializes an empty set of URLs.
     *
     * @param id         the unique identifier
     * @param name       the name of the media owner
     * @param ownedItems the list of media items
     */
    public MediaOwner(String id, String name, List<MediaItem> ownedItems) {
        this.id = id;
        this.name = name;
        this.ownedItems = ownedItems;
        this.urls = new HashSet<>();
    }

    /**
     * Constructs a MediaOwner with the given ID, name, owned items, and URLs.
     *
     * @param id         the unique identifier
     * @param name       the name of the media owner
     * @param ownedItems the list of media items
     * @param urls       the set of associated URLs
     */
    public MediaOwner(String id, String name, List<MediaItem> ownedItems, Set<String> urls) {
        this.id = id;
        this.name = name;
        this.ownedItems = ownedItems;
        this.urls = urls;
    }

    /**
     * Compares this MediaOwner to another based on their names.
     * Handles null values.
     *
     * @param other the other MediaOwner to compare to
     * @return a negative integer, zero, or a positive integer to show the difference
     */
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

    /**
     * Creates and returns a deep clone of this MediaOwner.
     * Clones the list of owned items and the set of URLs.
     *
     * @return a clone of this MediaOwner instance
     * @throws AssertionError if the cloning fails
     */
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

    /**
     * Returns a string representation of the MediaOwner.
     *
     * @return a string including the ID, name, and URLs
     */
    @Override
    public String toString() {
        return "MediaOwner [id=" + id + ", name=" + name + ", urls=" + urls + "]";
    }
}
