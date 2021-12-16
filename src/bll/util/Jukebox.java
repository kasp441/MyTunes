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

    public void playPause(){
        if (mediaPlayer != null){
            if (!playing){
                playMedia();
            }else{
                pauseMedia();
            }
        }

    }

    public void setCurrentSong(Song song){
        File file = new File(song.getDestination());
        media = new Media(file.toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        currentSong = song;
        mediaPlayer.setOnEndOfMedia(this::skipSong);
    }

    public void skipSong(){
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

    public void backSong(){
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
    public boolean isPlaying(){
        return playing;
    }
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

    public void setVolume(double volume){
        if (mediaPlayer != null){
            mediaPlayer.setVolume(volume);
        }
    }

}
