package com.meloman.project.service_model;

import com.meloman.project.data_model.MediaItem;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <ID> the type of the user ID, can store any value as ID (experimental)
 */
@Getter
@Setter
public class User<ID> implements Cloneable {

    private ID userId;
    private String username;
    private String email;
    private String phoneNumber;
    private List<MediaItem> ownedItems;
    private Set<MediaItem> likedItems;

    public User(ID userId, String username, String email, String phoneNumber) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.ownedItems = new ArrayList<>();
        this.likedItems = new HashSet<>();
    }

    public void addOwnedItem(MediaItem item) {
        this.ownedItems.add(item);
    }

    public void likeItem(MediaItem item) {
        this.likedItems.add(item);
    }

    @Override
    public User<ID> clone() {
        try {
            User<ID> cloned = (User<ID>) super.clone();

            List<MediaItem> clonedOwnedItems = new ArrayList<>();
            for (MediaItem mi : this.ownedItems) {
                clonedOwnedItems.add(mi.clone());
            }
            cloned.setOwnedItems(clonedOwnedItems);

            Set<MediaItem> clonedLikedItems = new HashSet<>();
            for (MediaItem mi : this.likedItems) {
                clonedLikedItems.add(mi.clone());
            }
            cloned.setLikedItems(clonedLikedItems);

            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError("Clone not supported", e);
        }
    }

    @Override
    public String toString() {
        return "User [userId=" + userId +
                ", username=" + username +
                ", ownedItems=" + ownedItems +
                ", likedItems=" + likedItems + "]";
    }
}
