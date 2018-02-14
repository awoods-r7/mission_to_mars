package ritogaems.tov.gameEngine.io;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.media.SoundPool;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import ritogaems.tov.gameEngine.audio.BackgroundMusic;
import ritogaems.tov.gameEngine.audio.SoundEffect;
import ritogaems.tov.gameEngine.entity.Player;

/**
 * @author Darren McGarry
 * @author Michael Purdy
 *         Handles the reading and writing of files to the device
 *         <p/>
 *         Michael
 *         - Text files and saving
 *         Darren
 *         - Original framework
 *         - Back ground music
 *         - Sound effects
 *         - Bitmaps
 */
public class FileHandler {

    private Context context;
    private AssetManager assetManager;
    private String externalStorageDir;


    public FileHandler(Context context) {
        this.context = context;
        assetManager = context.getAssets();
        externalStorageDir = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
    } // end of FileHandler

    public BackgroundMusic loadBgm(String filepath) throws IOException {
        try {
            AssetFileDescriptor descriptor = assetManager.openFd(filepath);
            return new BackgroundMusic(descriptor);
        } catch (IOException e) {
            throw new IOException("Could not load bgm '" + filepath + "'");
        }
    } // end of loadBgm

    public SoundEffect loadSfx(String filepath, SoundPool pool) throws IOException {
        try {
            AssetFileDescriptor descriptor = assetManager.openFd(filepath);
            int sfxID = pool.load(descriptor, 1);
            return new SoundEffect(pool, sfxID);
        } catch (IOException e) {
            throw new IOException("Could not load soundEffect '" + filepath + "'");
        }
    } // end of loadSfx

    public String loadTextFile(String fileDirectory) throws IOException {
        String fileText;
        InputStream rawFile = assetManager.open(fileDirectory);
        int size = rawFile.available();
        byte[] buffer = new byte[size];
        rawFile.read(buffer);
        rawFile.close();

        fileText = new String(buffer);

        return fileText;
    }

    public InputStream readAsset(String assetName) throws IOException {
        return assetManager.open(assetName);
    } // end of InputStream

    public Bitmap loadBitmap(String fileName, Bitmap.Config format) throws IOException {
        Options options = new Options();
        options.inPreferredConfig = format;
        InputStream in = null;
        Bitmap bitmap = null;
        try {
            in = assetManager.open(fileName);
            bitmap = BitmapFactory.decodeStream(in);
            if (bitmap == null) {
                throw new IOException("Could not load bitmap [" + fileName + "]");
            } // end of if
        } // end of try
        catch (IOException e) {
            throw new IOException("Could not load bitmap [" + fileName + "]");
        } // end of catch
        finally {
            if (in != null) {
                try {
                    in.close();
                } // end of try
                catch (IOException e) {
                    e.printStackTrace();
                } // end of catch
            } // end of if
        } // end of finally
        return bitmap;
    } // end of loadBitmap


    public InputStream readFile(String fileName) throws IOException {
        return new FileInputStream(externalStorageDir + fileName);
    } // end of readFile

    public OutputStream writeFile(String fileName) throws IOException {
        return new FileOutputStream(externalStorageDir + fileName);
    } // end of writeFile

//    private final String gameSavesDir = "saves";

    /**
     * Saves the state of the game
     * (includes save number to allow possibility of multiple save games
     * not implemented)
     *
     * @param player
     * @param saveNum
     */
    public void save(Player player, String mapName, int saveNum) {
        try {
            // open an output stream to write the game objects to
            FileOutputStream fileOutputStream = context.openFileOutput(getSaveLocation(saveNum), Context.MODE_PRIVATE);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(new SaveProperties(player, mapName));
            objectOutputStream.close();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public SaveProperties load(int saveNum) {
        try {
            FileInputStream fileInputStream = context.openFileInput(getSaveLocation(saveNum));
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            SaveProperties loadedProperties = (SaveProperties) objectInputStream.readObject();
            objectInputStream.close();
            fileInputStream.close();
            return loadedProperties;
        } catch (IOException | ClassNotFoundException e) {
            Log.d("[DEBUG  ]", "Error loading save file.");
            return null;
        }
    }

    private String getSaveLocation(int saveNum) {
        return "SaveGame" + saveNum + ".bin";
    }
} // end of FileHandler
