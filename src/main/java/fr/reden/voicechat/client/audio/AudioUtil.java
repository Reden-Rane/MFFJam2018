package fr.reden.voicechat.client.audio;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.openal.AL10;
import org.lwjgl.util.WaveData;

import java.io.IOException;
import java.io.InputStream;

public class AudioUtil
{
    public static int wavToBuffer(ResourceLocation location) throws IOException
    {
        int buffer = AL10.alGenBuffers();
        InputStream stream = Minecraft.getMinecraft().getResourceManager().getResource(location).getInputStream();
        WaveData waveFile = WaveData.create(stream);
        AL10.alBufferData(buffer, waveFile.format, waveFile.data, waveFile.samplerate);
        waveFile.dispose();
        return buffer;
    }
}
