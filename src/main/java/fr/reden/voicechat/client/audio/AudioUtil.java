package fr.reden.voicechat.client.audio;

import net.minecraft.util.ResourceLocation;
import org.lwjgl.openal.AL10;
import org.lwjgl.util.WaveData;

public class AudioUtil
{
    public static int wavToBuffer(ResourceLocation location)
    {
        int buffer = AL10.alGenBuffers();
        WaveData waveFile = WaveData.create(location.getResourcePath());
        AL10.alBufferData(buffer, waveFile.format, waveFile.data, waveFile.samplerate);
        waveFile.dispose();
        return buffer;
    }
}
