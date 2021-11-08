/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 */
package tech.mmmax.kami.api.management;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.entity.Entity;
import tech.mmmax.kami.api.config.ISavable;
import tech.mmmax.kami.api.friends.Friend;
import tech.mmmax.kami.api.management.SavableManager;

public class FriendManager
implements ISavable {
    public static FriendManager INSTANCE;
    List<Friend> friends = new ArrayList<Friend>();

    public FriendManager() {
        SavableManager.INSTANCE.getSavables().add(this);
    }

    public List<Friend> getFriends() {
        return this.friends;
    }

    public boolean isFriend(Entity entity) {
        Friend testFriend = new Friend(entity.getName());
        return this.friends.contains(testFriend);
    }

    public void addFriend(Entity entity) {
        Friend friend = new Friend(entity.getName());
        this.friends.add(friend);
    }

    public void removeFriend(Entity entity) {
        Friend friend = new Friend(entity.getName());
        this.friends.remove(friend);
    }

    @Override
    public void load(Map<String, Object> objects) {
        if (objects.get("friends") != null) {
            List<String> friendsList = (List)objects.get("friends");
            for (String s : friendsList) {
                this.friends.add(new Friend(s));
            }
        }
    }

    @Override
    public Map<String, Object> save() {
        HashMap<String, Object> toSave = new HashMap<String, Object>();
        ArrayList<String> friendList = new ArrayList<String>();
        for (Friend friend : this.friends) {
            friendList.add(friend.toString());
        }
        toSave.put("friends", friendList);
        return toSave;
    }

    @Override
    public String getFileName() {
        return "friends.yml";
    }

    @Override
    public String getDirName() {
        return "misc";
    }
}

