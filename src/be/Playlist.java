package be;

import java.util.List;

public class Playlist {

    private List<Song> playlist;
    private String name;
    private final int ID;
    private int totallenght;

    public Playlist(int id, String name, int totallenght){
        this.name=name;
        this.ID=id;
        this.totallenght=totallenght;
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

    public int getTotallenght() { return totallenght;}

    public void setName(String name) {
        this.name = name;
    }
}
