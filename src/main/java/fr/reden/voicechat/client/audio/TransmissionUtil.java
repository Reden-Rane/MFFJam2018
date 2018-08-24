package fr.reden.voicechat.client.audio;

import fr.reden.voicechat.common.item.ItemRegistry;
import fr.reden.voicechat.common.item.ItemTalkieWalkie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class TransmissionUtil
{

    public static boolean canPlayerReceiveOnFrequency(EntityPlayer player, int frequency)
    {
        for (ItemStack stack : player.inventory.mainInventory)
        {
            if (!stack.isEmpty() && stack.getItem() == ItemRegistry.TALKIE_WALKIE)
            {
                int talkieFrequency = ((ItemTalkieWalkie) ItemRegistry.TALKIE_WALKIE).getFrequency(stack);
                if (frequency == talkieFrequency)
                {
                    return true;
                }
            }
        }
        return false;
    }

}
