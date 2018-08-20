package fr.reden.voicechat.client.audio;

import org.lwjgl.openal.AL10;

import static org.lwjgl.openal.AL10.*;

public class Source
{
    private int sourceID;

    public Source()
    {
        this.sourceID = AL10.alGenSources();
        AL10.alSourcef(this.sourceID, AL10.AL_GAIN, 1);
        AL10.alSourcef(this.sourceID, AL10.AL_PITCH, 1);

        AL10.alSourcei(this.sourceID, AL_SOURCE_RELATIVE, AL_TRUE);
        AL10.alSource3f(this.sourceID, AL_POSITION, 0.0f, 0.0f, 0.0f);
    }

    public void pushBufferToQueue(int buffer)
    {
        AL10.alSourceQueueBuffers(this.sourceID, buffer);

        if (!isPlaying())
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

    public void play()
    {
        AL10.alSourcePlay(this.sourceID);
    }

    public void play(int buffer)
    {
        AL10.alSourcei(this.sourceID, AL10.AL_BUFFER, buffer);
        play();
    }

    public void pause()
    {
        AL10.alSourcePause(this.sourceID);
    }

    public void setVolume(float volume)
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
