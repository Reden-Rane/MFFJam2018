package fr.reden.voicechat.common.network.audio.server;

import fr.reden.voicechat.client.audio.TransmissionUtil;
import fr.reden.voicechat.common.network.audio.AudioPacketUtil;
import fr.reden.voicechat.common.network.udp.IUDPPacketProcessor;
import net.minecraft.server.management.PlayerList;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

import static fr.reden.voicechat.common.VoiceChatMod.getProxy;

public class UDPAudioPacketProcessorServer implements IUDPPacketProcessor
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
            Set<Integer> frequencySet = new TreeSet<>();

            for (int i = 0; i < frequencySetSize; i++)
            {
                frequencySet.add(dataInputStream.readInt());
            }

            FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() ->
            {
                PlayerList playerList = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList();

                //On envoie le paquet sur les fréquences communes utilisées par l'émetteur et les récepteurs
                playerList.getPlayers().stream().filter(playerMP -> !playerMP.getUniqueID().equals(senderUUID)).forEach(playerMP ->
                {
                    Set<Integer> commonFrequencies = new TreeSet<>();

                    for (int frequency : frequencySet)
                    {
                        if (TransmissionUtil.canPlayerReceiveOnFrequency(playerMP, frequency))
                        {
                            commonFrequencies.add(frequency);
                        }
                    }

                    if (commonFrequencies.size() > 0)
                    {
                        IAudioNetworkServerBound serverBound = getProxy().getAudioNetworkServerBound();

                        if (serverBound.getSocket() != null)
                        {
                            byte[] data = AudioPacketUtil.createAudioPacket(senderUUID, commonFrequencies, audioData);
                            AudioNetworkClientInfo playerInfo = serverBound.getClientInfo(playerMP);
                            if (playerInfo != null)
                            {
                                serverBound.getSocket().sendPacket(data, playerInfo.getAddress(), playerInfo.getAudioPort());
                            }
                        }
                    }
                });

            });

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                dataInputStream.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
}
