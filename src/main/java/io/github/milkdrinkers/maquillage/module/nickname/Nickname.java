package io.github.milkdrinkers.maquillage.module.nickname;

public class Nickname {
    private String nickname; // The nickname of the player
    private String username; // The username of the player who owns this nickname

    public Nickname(String nickname, String username) {
        this.nickname = nickname;
        this.username = username;
    }

    /**
     * Get the nickname of the player
     *
     * @return nickname
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * Set the nickname of the player
     *
     * @param nickname the nickname to set
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * Get the username of the player who owns this nickname
     *
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Set the username of the player who owns this nickname
     *
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }
}
