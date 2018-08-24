package fr.reden.voicechat.common.network.audio.client;

import fr.reden.voicechat.client.audio.AudioManager;
import fr.reden.voicechat.client.audio.TransmissionUtil;
import fr.reden.voicechat.common.network.udp.IUDPPacketProcessor;
import net.minecraft.client.Minecraft;
import org.lwjgl.openal.AL10;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.ByteBuffer;
import java.util.UUID;

import static fr.reden.voicechat.client.audio.AudioRecorder.SAMPLE_FORMAT;
import static fr.reden.voicechat.client.audio.AudioRecorder.SAMPLE_FREQUENCY;

public class UDPAudioPacketProcessorClient implements IUDPPacketProcessor
{
    @Override
    public void processPacket(DatagramSocket socket, DatagramPacket packet)
    {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(packet.getData());
        DataInputStream dataInputStream = new DataInputStream(byteArrayInputStream);

        try
        {
            int audioDataSize = dataInputStream.readInt();
            byte[] audioData = new byte[audioDataSize];
            for (int i = 0; i < audioDataSize; i++)
            {
                audioData[i] = dataInputStream.readByte();
            }

            UUID senderUUID = UUID.fromString(dataInputStream.readUTF());

            int frequencySetSize = dataInputStream.readInt();

            boolean playAudio = false;

            for (int i = 0; i < frequencySetSize; i++)
            {
                int frequency = dataInputStream.readInt();
                if (TransmissionUtil.canPlayerReceiveOnFrequency(Minecraft.getMinecraft().player, frequency))
                {
                    playAudio = true;
                    break;
                }
            }

            if (playAudio)
            {
                Minecraft.getMinecraft().addScheduledTask(() ->
                {
                    int audioBuf = AL10.alGenBuffers();

                    ByteBuffer audioBuffer = ByteBuffer.allocateDirect(audioDataSize);
                    audioBuffer.put(audioData);
                    audioBuffer.rewind();

                    AL10.alBufferData(audioBuf, SAMPLE_FORMAT, audioBuffer, SAMPLE_FREQUENCY);
                    AudioManager.getInstance().getPlayerAudioSource(senderUUID).pushBufferToQueue(audioBuf, true);
                    AudioManager.getInstance().getPlayerAudioSource(senderUUID).popBufferFromQueue();
                });
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
