package ritogaems.tov.gameEngine;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

import java.io.IOException;
import java.util.HashMap;

import ritogaems.tov.gameEngine.audio.BackgroundMusic;
import ritogaems.tov.gameEngine.audio.SoundEffect;
import ritogaems.tov.gameEngine.io.FileHandler;

/**
 * @author Darren McGarry
 * @author Zoey Longridge
 * @author Andrew Woods
 * @author Michael Purdy
 *         Store of assets that hold assets loaded into
 *         the game so that they that be reused
 *         <p/>
 *         Edited from gage
 *         Darren
 *         - Background music
 *         - Sound Effects
 *         - Sound pool
 *         Zoey
 *         - Bitmap
 *         Andrew
 *         - Bitmap array
 *         Michael
 *         - Text files
 */
public class AssetStore {

    /**
     * Bitmap Asset Store
     */
    private HashMap<String, Bitmap> bitmap;

    /**
     * Bitmap Array Asset Store
     */
    private HashMap<String, Bitmap[]> bitmapArray;

    /**
     * Background music asset store
     */
    private HashMap<String, BackgroundMusic> backgroundMusic;

    /**
     * Sound effects asset store
     */
    private HashMap<String, SoundEffect> soundEffects;

    /**
     * Sound pool
     */
    private SoundPool soundPool;

    /**
     * text files asset store
     */
    private HashMap<String, String> textFiles;

    /**
     * File handler
     */
    private FileHandler fileHandler;

    /**
     * Constructor
     *
     * @param fileHandler The handler for reading files
     */
    public AssetStore(FileHandler fileHandler) {
        this.fileHandler = fileHandler;
        bitmap = new HashMap<>();
        bitmapArray = new HashMap<>();
        backgroundMusic = new HashMap<>();
        soundEffects = new HashMap<>();
        soundPool = createSoundPool();
        textFiles = new HashMap<>();
    }

    /**
     * Add bgm to respective hash map
     *
     * @param assetName Name of the asset
     * @param asset     The asset
     * @return If the add was successful
     */
    private boolean add(String assetName, BackgroundMusic asset) {
        if (backgroundMusic.containsKey(assetName)) return false;

        backgroundMusic.put(assetName, asset);
        return true;
    }

    /**
     * Add soundEffect to respective hash map
     *
     * @param assetName Name of the asset
     * @param asset     The asset
     * @return If the add was successful
     */
    private boolean add(String assetName, SoundEffect asset) {
        if (soundEffects.containsKey(assetName)) return false;

        soundEffects.put(assetName, asset);
        return true;
    }

    /**
     * Add bitmap to respective hash map
     *
     * @param assetName Name of the asset
     * @param asset     The asset
     * @return If the add was successful
     */
    private boolean add(String assetName, Bitmap asset) {
        if (bitmap.containsKey(assetName))
            return false;
        bitmap.put(assetName, asset);
        return true;
    }

    /**
     * Add BitmapArray to respective hash map
     *
     * @param assetName Name of the asset
     * @param asset     The asset
     */
    public void add(String assetName, Bitmap[] asset) {
        if (bitmapArray.containsKey(assetName))
            return;
        bitmapArray.put(assetName, asset);
    }

    /**
     * Add text file to respective hash map
     *
     * @param assetName Name of the asset
     * @param asset     The asset
     * @return If the add was successful
     */
    private boolean add(String assetName, String asset) {
        if (textFiles.containsKey(assetName)) return false;
        textFiles.put(assetName, asset);
        return true;
    }

    /**
     * Add background music to respective hash map
     *
     * @param assetName Name of the asset
     * @param assetPath The path to the asset
     * @return If the deploy was successful
     */
    private boolean deployBgm(String assetName, String assetPath) {
        boolean loadSuccessful;
        try {
            BackgroundMusic backgroundMusic = fileHandler.loadBgm(assetPath);
            loadSuccessful = add(assetName, backgroundMusic);
        } catch (IOException e) {
            System.err.print("Could not add the loaded asset.");
            loadSuccessful = false;
        }
        return loadSuccessful;
    }

    /**
     * Get the background music from the respective hash map,
     * If it doesn't exist, add it
     *
     * @param assetName Name of the asset
     * @param assetPath The asset
     * @return The background music
     */
    public BackgroundMusic getBgm(String assetName, String assetPath) {
        if (backgroundMusic.containsKey(assetName)) {
            return backgroundMusic.get(assetName);
        } else {
            if (deployBgm(assetName, assetPath)) {
                return backgroundMusic.get(assetName);
            } else {
                return null;
            }
        }
    }

    /**
     * Add sound effect to respective hash map
     *
     * @param assetName Name of the asset
     * @param assetPath The path to the asset
     * @return If the deploy was successful
     */
    private boolean deploySfx(String assetName, String assetPath) {
        boolean loadSuccessful;
        try {
            SoundEffect soundEffect = fileHandler.loadSfx(assetPath, soundPool);
            loadSuccessful = add(assetName, soundEffect);
        } catch (IOException e) {
            System.err.print("Could not add the loaded asset.");
            loadSuccessful = false;
        }
        return loadSuccessful;
    }

    /**
     * Get the sound effect from the respective hash map,
     * If it doesn't exist, add it
     *
     * @param assetName Name of the asset
     * @param assetPath The path to the asset
     * @return The sound effect
     */
    public SoundEffect getSfx(String assetName, String assetPath) {
        if (soundEffects.containsKey(assetName)) {
            return soundEffects.get(assetName);
        } else {
            if (deploySfx(assetName, assetPath)) {
                return soundEffects.get(assetName);
            } else {
                return null;
            }
        }
    }

    /**
     * Add bitmap to respective hash map
     *
     * @param assetName Name of the asset
     * @param assetPath The path to the asset
     * @return If the deploy was successful
     */
    private boolean deployBitmap(String assetName, String assetPath) {
        boolean loadSuccessful;
        try {
            Bitmap bitmap = fileHandler.loadBitmap(assetPath, null);
            loadSuccessful = add(assetName, bitmap);
        } catch (IOException e) {
            System.err.print("Could not add the loaded asset.");
            loadSuccessful = false;
        }
        return loadSuccessful;
    }

    /**
     * Get the bitmap from the respective hash map,
     * If it doesn't exist, add it
     *
     * @param assetName Name of the asset
     * @param assetPath The path to the asset
     * @return The bitmap
     */
    public Bitmap getBitmap(String assetName, String assetPath) {
        if (bitmap.containsKey(assetName)) {
            return bitmap.get(assetName);
        } else {
            if (deployBitmap(assetName, assetPath)) {
                return bitmap.get(assetName);
            } else {
                return null;
            }
        }
    }

    /**
     * Get the bitmap array from the respective hash map,
     * If it doesn't exist return null
     *
     * @param assetName Name of the asset
     * @return The bitmap
     */
    public Bitmap[] getBitmapArray(String assetName) {
        if (bitmapArray.containsKey(assetName)) {
            return bitmapArray.get(assetName);
        } else {
            return null;
        }
    }

    /**
     * Add text file to respective hash map
     *
     * @param assetName Name of the asset
     * @param assetPath The path to the asset
     * @return If the deploy was successful
     */
    private boolean deployTextFile(String assetName, String assetPath) {
        boolean loadSuccessful;
        try {
            String fileText = fileHandler.loadTextFile(assetPath);
            loadSuccessful = add(assetName, fileText);
        } // end of try
        catch (IOException e) {
            System.err.print("Could not add the loaded asset.");
            loadSuccessful = false;
        } // end of catch
        return loadSuccessful;
    }

    /**
     * Get the text file from the respective hash map,
     * If it doesn't exist, add it
     *
     * @param assetName Name of the asset
     * @param assetPath The path to the asset
     * @return The bitmap
     */
    public String getTextFile(String assetName, String assetPath) {
        if (textFiles.containsKey(assetName)) {
            return textFiles.get(assetName);
        } else {
            if (deployTextFile(assetName, assetPath)) {
                return textFiles.get(assetName);
            } else {
                return null;
            }
        }
    }

    //SoundPool specific methods
    private SoundPool createSoundPool() {
        if (Build.VERSION.SDK_INT >= 21) {
            return createNewSoundPool();
        } else {
            return new SoundPool(SoundEffect.MAX_SIMUL_SFX, AudioManager.STREAM_MUSIC, 0);
        }
    }

    @TargetApi(21)
    private SoundPool createNewSoundPool() {
        AudioAttributes audioAttributes = new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_GAME)
                .setLegacyStreamType(AudioManager.STREAM_MUSIC).setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).build();

        return new SoundPool.Builder().setAudioAttributes(audioAttributes).setMaxStreams(SoundEffect.MAX_SIMUL_SFX).build();
    }
}
