/*
 * Decompiled with CFR 0.151.
 */
package tech.mmmax.kami.api.friends;

import java.util.Objects;

public class Friend {
    String ign;

    public Friend(String ign) {
        this.ign = ign;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        Friend friend = (Friend)o;
        return this.ign.equals(friend.ign);
    }

    public int hashCode() {
        return Objects.hash(this.ign);
    }

    public String toString() {
        return this.ign;
    }
}

