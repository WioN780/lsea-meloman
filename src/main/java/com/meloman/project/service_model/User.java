package com.meloman.project.service_model;

import com.meloman.project.transaction_model.MediaItem;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Class representing potential user of the application
 *
 * <ID> the type of the user ID, provides storing any value as ID (experimental)
 *
 */
@Getter
@Setter
public class User<ID> implements Serializable, Cloneable {

    /**
     * Unique identifier for the version of serialised item.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Unique user identifier
     */
    private ID userId;
    /**
     * Nickname of the user on the app
     */
    private String username;
    /**
     * Email of the user on the application
     */
    private String email;
    /**
     * Phone number of the user on the application(with country code)
     */
    private String phoneNumber;
    /**
     * Abstract list of any owned Media Items
     */
    private ArrayList<MediaItem> ownedItems;
    /**
     * Abstract set of any liked Media Items
     */
    private HashSet<MediaItem> likedItems;

    /**
     * Constructs a new User instance with the provided user ID, username, email, and phone number.
     * Initializes empty collections for owned and liked media items.
     *
     * @param userId      the unique ID of the user
     * @param username    the user's chosen username
     * @param email       the user's email address
     * @param phoneNumber the user's phone number
     */
    public User(ID userId, String username, String email, String phoneNumber) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.ownedItems = new ArrayList<>();
        this.likedItems = new HashSet<>();
    }

    /**
     * Adds a media item to the list of items owned by the user.
     *
     * @param item the media item to add
     */
    public void addOwnedItem(MediaItem item) {
        this.ownedItems.add(item);
    }

    /**
     * Adds a media item to the set of items liked by the user.
     *
     * @param item the media item to add
     */
    public void likeItem(MediaItem item) {
        this.likedItems.add(item);
    }

    /**
     * Removes a media item to the list of items owned by the user.
     *
     * @return boolean True value successful
     * @param item the media item to remove
     */
    public boolean removeOwnedItem(MediaItem item) {
        return item != null && ownedItems.remove(item);
    }

    /**
     * Removes a media item to the set of items liked by the user.
     *
     * @return boolean True value successful
     * @param item the media item to remove
     */
    public boolean removeLikedItem(MediaItem item) {
        return item != null && likedItems.remove(item);
    }

    /**
     * Creates and returns a deep copy of this user.
     * Both the owned and liked media item collections are cloned,
     * along with their contents.
     *
     * @return a clone of this user
     * @throws AssertionError if cloning is not supported
     */
    @Override
    public User<ID> clone() {
        try {
            User<ID> cloned = (User<ID>) super.clone();

            ArrayList<MediaItem> clonedOwnedItems = new ArrayList<>();
            for (MediaItem mi : this.ownedItems) {
                clonedOwnedItems.add(mi.clone());
            }
            cloned.setOwnedItems(clonedOwnedItems);

            HashSet<MediaItem> clonedLikedItems = new HashSet<>();
            for (MediaItem mi : this.likedItems) {
                clonedLikedItems.add(mi.clone());
            }
            cloned.setLikedItems(clonedLikedItems);

            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError("Clone not supported", e);
        }
    }

    /**
     * Override for printing full information about the user
     *
     * @return String containing formatted info
     */
    @Override
    public String toString() {
        return "User [userId=" + userId +
                ", username=" + username +
                ", ownedItems=" + ownedItems +
                ", likedItems=" + likedItems + "]";
    }
}
