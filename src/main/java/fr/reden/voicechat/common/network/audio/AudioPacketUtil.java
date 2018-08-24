package fr.reden.voicechat.common.network.audio;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Set;
import java.util.UUID;

public class AudioPacketUtil
{

    public static byte[] createAudioPacket(UUID sender, Set<Integer> frequencySet, byte[] audioData)
    {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);

        try
        {
            dataOutputStream.writeInt(audioData.length);
            for (byte anAudioData : audioData)
            {
                dataOutputStream.writeByte(anAudioData);
            }
            dataOutputStream.writeUTF(sender.toString());
            dataOutputStream.writeInt(frequencySet.size());
            for (int frequency : frequencySet)
            {
                dataOutputStream.writeInt(frequency);
            }

            dataOutputStream.flush();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return byteArrayOutputStream.toByteArray();
    }

}
