package fr.reden.voicechat.client.audio;

import org.lwjgl.openal.AL10;
import org.lwjgl.util.vector.Vector3f;

import static org.lwjgl.openal.AL10.*;

public class Source
{
    private int sourceID;

    public Source()
    {
        this.sourceID = AL10.alGenSources();
        setGain(1);
        setPitch(1);
    }

    public void pushBufferToQueue(int buffer, boolean forcePlay)
    {
        AL10.alSourceQueueBuffers(this.sourceID, buffer);

        if (!isPlaying() && forcePlay)
        {
            this.play();
        }
    }

    public void popBufferFromQueue()
    {
        int bufferProcessed = AL10.alGetSourcei(this.sourceID, AL10.AL_BUFFERS_PROCESSED);

        while (bufferProcessed-- > 0)
        {
            int uiBuffer = AL10.alSourceUnqueueBuffers(this.sourceID);
            AL10.alDeleteBuffers(uiBuffer);
        }
    }

    public void setPosition(Vector3f pos, boolean relativeToListener)
    {
        AL10.alSourcei(this.sourceID, AL_SOURCE_RELATIVE, relativeToListener ? AL_TRUE : AL_FALSE);
        AL10.alSource3f(this.sourceID, AL_POSITION, pos.x, pos.x, pos.z);
    }

    public void setLooping(boolean looping)
    {
        AL10.alSourcei(this.sourceID, AL_LOOPING, looping ? AL_TRUE : AL_FALSE);
    }

    public void play()
    {
        AL10.alSourcePlay(this.sourceID);
    }

    public void play(int buffer)
    {
        pushBufferToQueue(buffer, true);
    }

    public void pause()
    {
        AL10.alSourcePause(this.sourceID);
    }

    public void setGain(float volume)
    {
        AL10.alSourcef(this.sourceID, AL10.AL_GAIN, volume);
    }

    public void setPitch(float pitch)
    {
        AL10.alSourcef(this.sourceID, AL10.AL_PITCH, pitch);
    }

    public boolean isPlaying()
    {
        return AL10.alGetSourcei(this.sourceID, AL10.AL_SOURCE_STATE) == AL10.AL_PLAYING;
    }

    public int getSourceID()
    {
        return sourceID;
    }

    public void destroy()
    {
        AL10.alDeleteSources(this.sourceID);
    }
}
