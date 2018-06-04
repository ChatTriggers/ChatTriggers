package com.chattriggers.ctjs.minecraft.objects;

import com.chattriggers.ctjs.CTJS;
import com.chattriggers.ctjs.minecraft.mixins.MixinSoundHandler;
import com.chattriggers.ctjs.minecraft.wrappers.Client;
import com.chattriggers.ctjs.minecraft.wrappers.Player;
import com.chattriggers.ctjs.minecraft.wrappers.World;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import lombok.Getter;
import net.minecraft.client.audio.SoundCategory;
import net.minecraft.client.audio.SoundManager;
import paulscode.sound.SoundSystem;

import java.io.File;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;

public class Sound {
    private SoundSystem sndSystem;
    private ScriptObjectMirror config;
    private String source;
    @Getter
    private boolean listening = false;

    /**
     * Instances a new Sound with certain properties. These properties
     * should be passed through as a normal JavaScript object.
     *
     * REQUIRED:
     * &emsp;source (String) - filename, relative to ChatTriggers assets directory
     *
     * OPTIONAL:
     * &emsp;priority (boolean) - whether or not this sound should be prioritized, defaults to false
     * &emsp;loop (boolean) - whether or not to loop this sound over and over, defaults to false
     * &emsp;stream (boolean) - whether or not to stream this sound rather than preload it (should be true for large files), defaults to false
     *
     * CONFIGURABLE (can be set in config object, or changed later, but MAKE SURE THE WORLD HAS LOADED)
     * &emsp;category (String) - which category this sound should be a part of, see {@link #setCategory(String)}.
     * &emsp;volume (float) - volume of the sound, see {@link #setVolume(float)}
     * &emsp;pitch (float) - pitch of the sound, see {@link #setPitch(float)}
     * &emsp;x, y, z (float) - location of the sound, see {@link #setPosition(float, float, float)}. Defaults to the players position.
     * &emsp;attenuation (int) - fade out model of the sound, see {@link #setAttenuation(int)}
     * @param config the JavaScript config object
     */
    public Sound(ScriptObjectMirror config) {
        this.config = config;
        this.source = (String) config.get("source");


        if (World.isLoaded()) {
            loadSndSystem();

            try {
                bootstrap();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        } else {
            listening = true;
        }

        CTJS.getInstance().getSounds().add(this);
        Thread.dumpStack();
    }

    public void onWorldLoad() {
        listening = false;

        Thread.dumpStack();
        System.out.println("Loading sound system");
        loadSndSystem();
        System.out.println("sound system loaded: " + sndSystem);

        try {
            System.out.println("Bootstrapping");
            bootstrap();
        } catch (MalformedURLException exc) {
            exc.printStackTrace();
        }

    }

    private void loadSndSystem() {
        SoundManager sndManager = ((MixinSoundHandler) Client.getMinecraft().getSoundHandler()).getSndManager();

        try {
            Field field = sndManager.getClass().getDeclaredField("sndSystem");
            field.setAccessible(true);
            sndSystem = (SoundSystem) field.get(sndManager);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void bootstrap() throws MalformedURLException {
        String source = (String) config.get("source");
        boolean priority = (boolean) config.getOrDefault("priority", false);
        boolean loop = (boolean) config.getOrDefault("loop", false);
        boolean stream = (boolean) config.getOrDefault("stream", false);

        URL url = new File(CTJS.getInstance().getAssetsDir().getAbsolutePath() + "\\" + source).toURI().toURL();
        float x = (float) config.getOrDefault("x", (float) Player.getX());
        float y = (float) config.getOrDefault("y", (float) Player.getY());
        float z = (float) config.getOrDefault("z", (float) Player.getZ());
        int attModel = (int) config.getOrDefault("attenuation", 1);
        int distOrRoll = 16;

        if (stream) {
            sndSystem.newStreamingSource(
                    priority,
                    source,
                    url,
                    source,
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
                    source,
                    url,
                    source,
                    loop,
                    x,
                    y,
                    z,
                    attModel,
                    distOrRoll
            );
        }

        if (config.hasMember("volume")) {
            setVolume((float) config.get("volume"));
        }

        if (config.hasMember("pitch")) {
            setPitch((float) config.get("pitch"));
        }

        if (config.hasMember("category")) {
            setCategory((String) config.get("category"));
        }
    }

    /**
     * Sets the category of this sound, making it respect the Player's sound volume sliders.
     * Options are: MASTER, MUSIC, RECORDS, WEATHER, BLOCKS, MOBS, ANIMALS, PLAYERS, and AMBIENT
     *
     * @param category the category
     */
    public Sound setCategory(String category) {
        SoundCategory category1 = SoundCategory.valueOf(category.toUpperCase());
        setVolume(Client.getMinecraft().gameSettings.getSoundLevel(category1));

        return this;
    }

    /**
     * Sets this sound's volume.
     * Will override the category if called after {@link #setCategory(String)}, but not if called before.
     *
     * @param volume New volume, float value ( 0.0f - 1.0f ).
     */
    public Sound setVolume(float volume) {
        sndSystem.setVolume(this.source, volume);
        return this;
    }

    /**
     * Updates the position of this sound
     *
     * @param x the x coordinate
     * @param y the y coordinate
     * @param z the z coordinate
     */
    public Sound setPosition(float x, float y, float z) {
        sndSystem.setPosition(this.source, x, y, z);
        return this;
    }

    /**
     * Sets this sound's pitch.
     *
     * @param pitch A float value ( 0.5f - 2.0f ).
     */
    public Sound setPitch(float pitch) {
        sndSystem.setPitch(this.source, pitch);
        return this;
    }

    /**
     * Sets the attenuation (fade out over space) of the song.
     * Models are:
     * &emsp; NONE(0) - no fade
     * &emsp; ROLLOFF(1) - this is the default, meant to be somewhat realistic
     * &emsp: LINEAR(2) - fades out linearly, as the name implies
     *
     * @param model the model
     */
    public Sound setAttenuation(int model) {
        sndSystem.setAttenuation(this.source, model);
        return this;
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
}
