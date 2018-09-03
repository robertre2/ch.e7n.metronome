package ch.e7n.metronome;

import android.app.Activity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by robertre on 8/25/14.
 */
public class Sound {
    private static final int BYTES_PER_SAMPLE = 2;
    private final byte[] data;
    private final int samples;

    private Sound(byte[] data) {
        this.data = data;
        this.samples = data.length / BYTES_PER_SAMPLE;
    }

    /**
     * Load 16-bit single channel signed PCM raw sound from resources.
     * @param activity
     * @param resourceId
     * @return
     */
    public static Sound fromResource(Activity activity, int resourceId) {
        try {
            InputStream is = activity.getResources().openRawResource(resourceId);
            int length = is.available();
            byte[] res = new byte[length];
            int actuallyRead = is.read(res);
            if(actuallyRead != length) {
                throw new RuntimeException("Could not read all bytes of resource");
            }
            return new Sound(res);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Sound scale(Sound source, double gain) {
        ByteBuffer src = ByteBuffer.wrap(source.data);
        ByteBuffer dest = ByteBuffer.allocate(source.data.length);
        src.order(ByteOrder.LITTLE_ENDIAN);
        dest.order(ByteOrder.LITTLE_ENDIAN);
        for(int i=0;i<source.samples;++i) {
            short value = src.getShort();
            short newValue = (short)(value * gain);
            dest.putShort(newValue);
        }
        return new Sound(dest.array());
    }

    public static Sound silence(int samples) {
        byte[] res = new byte[samples*BYTES_PER_SAMPLE];
        return new Sound(res);
    }

    /**
     * Change the length of the sound clip to the specified number of samples by adding
     * silence at the end or by cutting off the sound.
     * @param source
     * @param samples
     * @return
     */
    public static Sound resize(Sound source, int samples) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            if(samples <= source.samples) {
                baos.write(source.data, 0, samples*BYTES_PER_SAMPLE);
            } else {
                baos.write(source.data);
                int silentSamples = samples - source.samples;
                byte[] silence = new byte[silentSamples*BYTES_PER_SAMPLE];
                baos.write(silence);
            }
            return new Sound(baos.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    int getSamples() {
        return samples;
    }

    public byte[] getData() {
        return data;
    }
}
