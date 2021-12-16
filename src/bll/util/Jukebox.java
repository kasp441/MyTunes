package bll.util;

import be.Song;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Jukebox {
private List<Song> songList;
int currentSongIndex;
Song currentSong;
boolean playing;
Media media;
MediaPlayer mediaPlayer;

    public Jukebox() {
        currentSongIndex = 0;
        currentSong = null;
        playing = false;
        songList = new ArrayList<>();
    }

    /**
     sets the playlist of songs
     */
    public void setSongList(List<Song> inputSongs){
        songList = inputSongs;
    }

    public void pauseMedia(){
        playing = false;
        mediaPlayer.pause();
    }

    public void stopMedia(){
        playing = false;
        mediaPlayer.stop();
    }

    public void playMedia(){
        playing = true;
        mediaPlayer.play();
    }

    /**
     * If a mediaplayer exists, this method will pause or start the current mediaplayer
     */
    public void playPause(){
        if (mediaPlayer != null){
            if (!playing){
                playMedia();
            }else{
                pauseMedia();
            }
        }

    }

    /**
     * Assigns
     * @param song the song you want to play
     */
    public void setCurrentSong(Song song){
        File file = new File(song.getDestination());
        media = new Media(file.toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        currentSong = song;
        mediaPlayer.setOnEndOfMedia(this::skipSong);
    }

    /**
     *
     * If a song is currently chosen, skipSong will stop the music, go to the next song in the current songlist
     * and start playing that song.
     */
    public void skipSong(){
        if (mediaPlayer != null){
            if (currentSongIndex < songList.size()-1){
                currentSongIndex++;
                stopMedia();
                Song song = songList.get(currentSongIndex);
                setCurrentSong(song);
                playMedia();
            }
            else{
                currentSongIndex =0;
                stopMedia();
                Song song = songList.get(currentSongIndex);
                setCurrentSong(song);
                playMedia();
            }
        }
    }

    /**
     *
     * If a song is currently chosen, backSong will stop the music, go to the previous song in the current songlist
     * and start playing that song.
     */
    public void backSong(){
        if (mediaPlayer != null){
            if (currentSongIndex > 0){
                currentSongIndex--;
                stopMedia();
                Song song = songList.get(currentSongIndex);
                setCurrentSong(song);
                playMedia();
            }
            else{
                currentSongIndex =songList.size()-1;
                stopMedia();
                Song song = songList.get(currentSongIndex);
                setCurrentSong(song);
                playMedia();
            }
        }
    }
    public boolean isPlaying(){
        return playing;
    }

    /**
     * Finds the title of the song that is currently playing, or default text if nothing is playing
     * @return
     */
    public String getCurrentSongTitle(){
        if (playing){
            return currentSong.getTitle() + " is playing";
        }
        else
            return "Nothing is playing";
    }

    public Song getCurrentSong(){
        return currentSong;
    }


    public void setCurrentSongIndex(int index){
        this.currentSongIndex = index;
    }

    public List<Song> getSongList() {
        return songList;
    }

    /**
        Will set the volume of the current mediaplayer
     */
    public void setVolume(double volume){
        if (mediaPlayer != null){
            mediaPlayer.setVolume(volume);
        }
    }

}
