package com.meloman.project.communications;

/**
 * Represents the type of action the client wants the server to perform.
 */
public enum RequestType {
    //Fetch user's liked songs/playlists
    VIEW_LIKED_ITEMS,
    //Create a new user profile
    ADD_NEW_USER,
    //Remove a user
    DELETE_USER,
    //Add an item to a user's liked list
    ADD_USER_LIKED_ITEM,
    //Retrieve the highest-rated item
    GET_TOP_RATED_ITEM
}