package ch.e7n.metronome;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import ch.e7n.metronome.parser.ConcreteMusicObject;
import ch.e7n.metronome.parser.DefaultSequence;
import ch.e7n.metronome.parser.MusicIterator;
import ch.e7n.metronome.parser.MusicObject;
import ch.e7n.metronome.parser.Note;
import ch.e7n.metronome.parser.Rest;
import ch.e7n.metronome.parser.TimeSignature;

public class Metronome {

    private static final double EPSILON = 1e-9;
    private double bpm = 90;
    private int beat = 4;

    private static final int SAMPLE_RATE = 8000;

    private boolean play = true;

    private AudioGenerator audioGenerator = new AudioGenerator(SAMPLE_RATE);
    private int currentBeat = 0;

    private final Sound clickStrong;
    private final Sound clickHalfStrong;
    private final Sound clickWeak;
    private Sound silence;

    private MusicObject sequenceToPlay;
    private MusicIterator playIterator;
    private double currentTime;
    private TimeSignature currentTimeSignature;

    public Metronome(Activity activity) {
        audioGenerator.createPlayer();
        this.clickStrong = Sound.fromResource(activity, R.raw.click);
        this.clickHalfStrong = Sound.scale(clickStrong, 0.5);
        this.clickWeak = Sound.scale(clickStrong, 0.25);
        this.currentTime = 0;
        this.sequenceToPlay = new DefaultSequence();
        this.playIterator = sequenceToPlay.getIterator();
        this.currentTimeSignature = new TimeSignature(4, 4);
    }


    public void calcSilence() {
        int silenceSamples = (int) (((60/bpm)*SAMPLE_RATE)-clickStrong.getSamples());
        silence = Sound.silence(silenceSamples);
    }

    public void play() {
        calcSilence();
        do {
            ConcreteMusicObject toPlay;
            synchronized (this) {
                toPlay = playIterator.get();
                if(playIterator.hasNext()) {
                    playIterator = playIterator.next();
                } else {
                    playIterator = sequenceToPlay.getIterator();
                }
            }

            if(toPlay instanceof TimeSignature) {
                // Time signature rests current time
                currentTimeSignature = (TimeSignature) toPlay;
                currentTime = 0;
            } else if(toPlay instanceof Rest) {
                double length = toPlay.length() / currentTimeSignature.getBeat();
                int samples = (int)(((60/bpm)*SAMPLE_RATE) * length);
                Sound s = Sound.resize(silence, samples);
                audioGenerator.writeSound(s);
                currentTime += toPlay.length();
            } else if(toPlay instanceof Note) {
                double length = toPlay.length() / currentTimeSignature.getBeat();
                int samples = (int)(((60/bpm)*SAMPLE_RATE) * length);

                Sound baseSound;
                String nameOfBaseSound;
                if((currentTime + EPSILON) % currentTimeSignature.getTime() < 2*EPSILON) {
                    baseSound = clickStrong;
                    nameOfBaseSound = "S";
                } else if((currentTime + EPSILON) % currentTimeSignature.getStrongBeat() < 2*EPSILON) {
                    baseSound = clickHalfStrong;
                    nameOfBaseSound = "H";
                } else {
                    baseSound = clickWeak;
                    nameOfBaseSound = "w";
                }

                Sound s = Sound.resize(baseSound, samples);
                audioGenerator.writeSound(s);
                currentTime += toPlay.length();
            }


/*
            Message msg = new Message();
            msg.obj = ""+(currentBeat+1);
            if(currentBeat == 0) {
                audioGenerator.writeSound(clickStrong);
            } else if(currentBeat*2 == beat) {
                audioGenerator.writeSound(clickHalfStrong);
            } else {
                audioGenerator.writeSound(clickWeak);
            }

            if(play) {
                audioGenerator.writeSound(silence);
            }

            currentBeat = (currentBeat+1) % beat;*/
        } while(play);
    }

    public void stop() {
        play = false;
        audioGenerator.destroyAudioTrack();
    }


    public void setBpm(int bpm) {
        this.bpm = bpm;
    }


    synchronized public void setSequenceToPlay(MusicObject musicObject) {
        this.currentTime = 0;
        this.sequenceToPlay = musicObject;
        this.playIterator = sequenceToPlay.getIterator();
        this.currentTimeSignature = new TimeSignature(4, 4);
    }
}
