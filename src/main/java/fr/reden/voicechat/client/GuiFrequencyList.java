package fr.reden.voicechat.client;

import fr.reden.voicechat.common.capability.CustomFrequency;
import fr.reden.voicechat.common.item.ItemRegistry;
import fr.reden.voicechat.common.item.ItemTalkieWalkie;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.client.GuiScrollingList;

import java.awt.*;

public class GuiFrequencyList extends GuiScrollingList
{
    private final GuiTalkieWalkie parent;
    private CustomFrequency selectedCustomFrequency;
    private boolean isFocused;

    public GuiFrequencyList(GuiTalkieWalkie parent, int x, int y, int width, int height, int entryHeight)
    {
        super(parent.mc, width, height, y, y + height, x, entryHeight, parent.mc.displayWidth, parent.mc.displayHeight);
        this.parent = parent;
    }

    @Override
    protected int getSize()
    {
        return parent.getCustomFrequencyList().size();
    }

    public CustomFrequency getSelectedEntry()
    {
        for (int i = 0; i < getSize(); i++)
        {
            if (isSelected(i))
            {
                return parent.getCustomFrequencyList().get(i);
            }
        }
        return null;
    }

    @Override
    protected void elementClicked(int index, boolean doubleClick)
    {
        CustomFrequency customFrequency = parent.getCustomFrequencyList().get(index);
        parent.getFrequencyTextfield().setText(String.valueOf(customFrequency.frequency));
        selectedCustomFrequency = customFrequency;
        parent.updateButtonsState();
        this.isFocused = true;
    }

    @Override
    protected boolean isSelected(int index)
    {
        CustomFrequency customFrequency = parent.getCustomFrequencyList().get(index);
        return selectedCustomFrequency == customFrequency && isFocused;
    }

    @Override
    protected void drawBackground()
    {
        if (getSize() == 0)
        {
            String text = I18n.format("gui.talkie_walkie.no_custom_frequency");
            int textWidth = Minecraft.getMinecraft().fontRenderer.getStringWidth(text);
            int x = this.left + (this.right - this.left - textWidth) / 2;
            int y = this.top + (this.bottom - this.top - Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT) / 2;
            Minecraft.getMinecraft().fontRenderer.drawString(text, x, y, Color.WHITE.getRGB());
        }
    }

    @Override
    protected void drawSlot(int slotIdx, int entryRight, int slotTop, int slotBuffer, Tessellator tess)
    {
        CustomFrequency customFrequency = parent.getCustomFrequencyList().get(slotIdx);

        FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
        String text = customFrequency.name + " " + customFrequency.frequency + "Hz";
        int textWidth = Minecraft.getMinecraft().fontRenderer.getStringWidth(text);
        fontRenderer.drawString(text, this.left + (this.right - this.left - textWidth) / 2, slotTop + (this.slotHeight - fontRenderer.FONT_HEIGHT) / 2, Color.WHITE.getRGB());
    }

    public void setFocused(boolean focused)
    {
        isFocused = focused;
    }
}
