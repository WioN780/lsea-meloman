package com.meloman.project.data_model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Represents an artist who creates media items like albums and tracks.
 * The Artist class stores information about the artist's real name, aliases,
 * associated groups, and URLs related to the artist.
 */

@Getter
@Setter
public class Artist extends MediaOwner {
    private String realName;
    private List<String> aliases;
    private List<String> groups;

    /**
     * Constructs an Artist object with the specified details.
     *
     * @param id The unique identifier of the artist.
     * @param name The name of the artist.
     * @param realName The real name of the artist.
     * @param aliases A list of aliases used by the artist.
     * @param groups A list of groups the artist has been associated with.
     * @param urls A set of URLs associated with the artist.
     */

    public Artist(String id, String name,
                  String realName, List<String> aliases, List<String> groups, Set<String> urls) {
        super(id, name, new ArrayList<>(), urls);
        this.realName = realName;
        this.aliases = aliases;
        this.groups = groups;
    }

    public Artist(String id, String name, List<MediaItem> ownedItems,
                  String realName, List<String> aliases, List<String> groups, Set<String> urls) {
        super(id, name, ownedItems, urls);
        this.realName = realName;
        this.aliases = aliases;
        this.groups = groups;
    }

    public Artist(String artistId, String artistName) {
        super(artistId, artistName);
    }

    /**
     * Creates a deep clone of the artist, including cloning the aliases and groups.
     *
     * @return A new cloned Artist object.
     */

    @Override
    public Artist clone() {
        Artist cloned = (Artist) super.clone();
        if (this.aliases != null) {
            cloned.setAliases(new ArrayList<>(this.aliases));
        }
        if (this.groups != null) {
            cloned.setGroups(new ArrayList<>(this.groups));
        }
        return cloned;
    }

    /**
     * Returns a string representation of the artist, including the artist's ID,
     * name, real name, aliases, and groups.
     *
     * @return A string describing the artist.
     */

    @Override
    public String toString() {
        return "Artist [" + super.toString() +
                ", realName=" + realName +
                ", aliases=" + aliases +
                ", groups=" + groups + "]";
    }
}


