package be;

import java.util.List;

public class Playlist {

    private List<Song> playlist;
    private String name;
    private final int ID;

    public Playlist(int id, String name){
        this.name=name;
        this.ID=id;
    }

    public List<Song> getPlaylist() {
        return playlist;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return ID;
    }

    public void setName(String name) {
        this.name = name;
    }
}
