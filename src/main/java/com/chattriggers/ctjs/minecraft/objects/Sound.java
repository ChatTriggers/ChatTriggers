package com.chattriggers.ctjs.minecraft.objects;

import com.chattriggers.ctjs.CTJS;
import com.chattriggers.ctjs.minecraft.mixins.MixinSoundHandler;
import com.chattriggers.ctjs.minecraft.wrappers.Client;
import com.chattriggers.ctjs.minecraft.wrappers.Player;
import net.minecraft.client.audio.SoundCategory;
import net.minecraft.client.audio.SoundManager;
import paulscode.sound.SoundSystem;

import java.io.File;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;

public class Sound {
    private static SoundSystem sndSystem;

    private String source;
    private SoundCategory category;

    public Sound(String fileName, boolean priority, boolean loop, boolean stream) throws MalformedURLException {
        if (sndSystem == null) {
            loadSoundSystem();
        }

        URL url = new File(CTJS.getInstance().getAssetsDir().getAbsolutePath() + "\\" + fileName).toURI().toURL();
        float x = (float) Player.getX();
        float y = (float) Player.getY();
        float z = (float) Player.getZ();
        int attModel = 1;
        int distOrRoll = 16;

        if (stream) {
            sndSystem.newStreamingSource(
                    priority,
                    fileName,
                    url,
                    fileName,
                    loop,
                    x,
                    y,
                    z,
                    attModel,
                    distOrRoll
            );
        } else {
            sndSystem.newSource(
                    priority,
                    fileName,
                    url,
                    fileName,
                    loop,
                    x,
                    y,
                    z,
                    attModel,
                    distOrRoll
            );
        }

        source = fileName;
    }

    /**
     * Sets the category of this sound, making it respect the Player's sound volume sliders.
     * Options are: MASTER, MUSIC, RECORDS, WEATHER, BLOCKS, MOBS, ANIMALS, PLAYERS, and AMBIENT
     *
     * @param category the category
     */
    public void setCategory(String category) {
        this.category = SoundCategory.valueOf(category.toUpperCase());

        setVolume(Client.getMinecraft().gameSettings.getSoundLevel(this.category));
    }

    /**
     * Sets this sound's volume.
     *
     * @param volume New volume, float value ( 0.0f - 1.0f ).
     */
    public void setVolume(float volume) {
        sndSystem.setVolume(this.source, volume);
    }

    /**
     * Updates the position of this sound
     *
     * @param x the x coordinate
     * @param y the y coordinate
     * @param z the z coordinate
     */
    public void setPosition(float x, float y, float z) {
        sndSystem.setPosition(this.source, x, y, z);
    }

    /**
     * Sets this sound's pitch.
     *
     * @param pitch A float value ( 0.5f - 2.0f ).
     */
    public void setPitch(float pitch) {
        sndSystem.setPitch(this.source, pitch);
    }

    /**
     * Sets the attenuation (fade out over space) of the song.
     * Models are:
     * &emsp; NONE(0) - no fade
     * &emsp; ROLLOFF(1) - this is the default, meant to be somewhat realistic
     * &emsp; LINEAR(2) - fades out linearly, as the name implies
     *
     * @param model the model
     */
    public void setAttenuation(int model) {
        sndSystem.setAttenuation(this.source, model);
    }

    /**
     * Plays/resumes the sound
     */
    public void play() {
        sndSystem.play(this.source);
    }

    /**
     * Pauses the sound, to be resumed later
     */
    public void pause() {
        sndSystem.pause(this.source);
    }

    /**
     * Completely stops the song
     */
    public void stop() {
        sndSystem.stop(this.source);
    }

    /**
     * I really don't know what this does
     */
    public void rewind() {
        sndSystem.rewind(this.source);
    }

    private void loadSoundSystem() {
        SoundManager sndManager = ((MixinSoundHandler) Client.getMinecraft().getSoundHandler()).getSndManager();

        try {
            Field field = sndManager.getClass().getDeclaredField("sndSystem");
            field.setAccessible(true);
            sndSystem = (SoundSystem) field.get(sndManager);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
