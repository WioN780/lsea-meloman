package com.meloman.project.data_model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter
@Setter
public class Artist extends MediaOwner {
    private String realName;
    private List<String> aliases;
    private List<String> groups;

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

    @Override
    public String toString() {
        return "Artist [" + super.toString() +
                ", realName=" + realName +
                ", aliases=" + aliases +
                ", groups=" + groups + "]";
    }
}


