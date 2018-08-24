package fr.reden.voicechat.client.gui;

import fr.reden.voicechat.common.CommonProxy;
import fr.reden.voicechat.common.VoiceChatMod;
import fr.reden.voicechat.common.capability.CustomFrequenciesCapability;
import fr.reden.voicechat.common.capability.CustomFrequency;
import fr.reden.voicechat.common.item.ItemRegistry;
import fr.reden.voicechat.common.item.ItemTalkieWalkie;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.config.GuiButtonExt;
import org.apache.commons.lang3.math.NumberUtils;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.io.IOException;
import java.util.List;

public class GuiTalkieWalkie extends GuiScreen
{
    public static final ResourceLocation GUI_TEXTURES = new ResourceLocation(VoiceChatMod.MOD_ID, "textures/gui/talkie_walkie_gui.png");

    private final EntityPlayer player;
    private final ItemStack stack;
    private GuiTextField frequencyTextfield;
    private GuiButtonExt switchButton;
    private GuiFrequencyList customFrequenciesList;
    private GuiTextField customNameTextfield;
    private GuiButtonExt addButton;
    private GuiButtonExt removeButton;
    private int guiLeft;
    private int guiTop;
    private int xSize;
    private int ySize;

    public GuiTalkieWalkie(EntityPlayer player, ItemStack stack)
    {
        super();
        this.player = player;
        this.stack = stack;
        this.xSize = 248;
        this.ySize = 166;
    }

    @Override
    public boolean doesGuiPauseGame()
    {
        return false;
    }

    @Override
    public void initGui()
    {
        super.initGui();
        this.buttonList.clear();

        this.guiLeft = (this.width - this.xSize) / 2;
        this.guiTop = (this.height - this.ySize) / 2;
        this.frequencyTextfield = new GuiTextField(0, mc.fontRenderer, this.guiLeft + 110, this.guiTop + 6, 90, 20);
        this.frequencyTextfield.setText(String.valueOf(((ItemTalkieWalkie) ItemRegistry.TALKIE_WALKIE).getFrequency(getStack())));
        this.frequencyTextfield.setMaxStringLength(16);
        this.switchButton = new GuiButtonExt(1, this.frequencyTextfield.x, this.frequencyTextfield.y + this.frequencyTextfield.height, this.frequencyTextfield.width, 10, I18n.format("gui.talkie_walkie.switch"));

        this.customFrequenciesList = new GuiFrequencyList(this, this.guiLeft + 5, this.guiTop + 38, this.xSize - 10, this.ySize - 65, 20);
        this.customNameTextfield = new GuiTextField(2, mc.fontRenderer, this.guiLeft + (this.xSize - 90) / 2, this.guiTop + this.ySize - 25, 50, 18);
        this.addButton = new GuiButtonExt(3, this.customNameTextfield.x + this.customNameTextfield.width + 2, this.customNameTextfield.y - 1, 20, 20, "+");
        this.removeButton = new GuiButtonExt(4, this.addButton.x + this.addButton.width + 2, this.customNameTextfield.y - 1, 20, 20, "-");

        this.buttonList.add(switchButton);
        this.buttonList.add(addButton);
        this.buttonList.add(removeButton);

        updateButtonsState();
    }

    @Override
    public void updateScreen()
    {
        super.updateScreen();
        this.frequencyTextfield.updateCursorCounter();
        this.customNameTextfield.updateCursorCounter();
    }

    private void drawBackground()
    {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(GUI_TEXTURES);
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        drawBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);

        String frequencyText = I18n.format("gui.talkie_walkie.frequency") + ":";
        int freqTextWidth = mc.fontRenderer.getStringWidth(frequencyText);
        mc.fontRenderer.drawString(frequencyText, this.frequencyTextfield.x - 5 - freqTextWidth, this.guiTop + 12, Color.BLACK.getRGB());
        String hzText = "Hz";
        mc.fontRenderer.drawString(hzText, this.frequencyTextfield.x + this.frequencyTextfield.width + 2, this.guiTop + 12, Color.BLACK.getRGB());
        this.frequencyTextfield.drawTextBox();

        String nameText = I18n.format("gui.talkie_walkie.name") + ":";
        int nameTextWidth = mc.fontRenderer.getStringWidth(nameText);
        mc.fontRenderer.drawString(nameText, this.customNameTextfield.x - nameTextWidth - 5, this.customNameTextfield.y + 6, Color.BLACK.getRGB());

        this.customFrequenciesList.drawScreen(mouseX, mouseY, partialTicks);
        this.customNameTextfield.drawTextBox();
    }

    @Override
    public void handleMouseInput() throws IOException
    {
        int mouseX = Mouse.getEventX() * this.width / this.mc.displayWidth;
        int mouseY = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;

        super.handleMouseInput();
        this.customFrequenciesList.handleMouseInput(mouseX, mouseY);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.frequencyTextfield.mouseClicked(mouseX, mouseY, mouseButton);
        this.customNameTextfield.mouseClicked(mouseX, mouseY, mouseButton);

        if (frequencyTextfield.isFocused())
        {
            customFrequenciesList.setFocused(false);
        }

        updateButtonsState();
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
        super.keyTyped(typedChar, keyCode);
        this.frequencyTextfield.textboxKeyTyped(typedChar, keyCode);
        this.customNameTextfield.textboxKeyTyped(typedChar, keyCode);
        updateButtonsState();
    }

    public void updateButtonsState()
    {
        this.switchButton.visible = canApplyFrequency() && isFrequencyNew();
        this.addButton.enabled = canAddEntry();
        this.removeButton.enabled = canRemoveEntry();
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException
    {
        super.actionPerformed(button);

        CustomFrequenciesCapability cap = player.getCapability(CommonProxy.SAVED_FREQUENCIES_CAPABILITY, null);

        if (button == addButton && canAddEntry())
        {
            CustomFrequency customFrequency = new CustomFrequency(this.customNameTextfield.getText(), Integer.valueOf(this.frequencyTextfield.getText()));
            cap.addCustomFrequency(customFrequency);
            cap.sync();
        }
        else if (button == removeButton && canRemoveEntry())
        {
            CustomFrequency selectedCustomFrequency = customFrequenciesList.getSelectedEntry();
            cap.removeCustomFrequency(selectedCustomFrequency);
            cap.sync();
        }
        else if (button == switchButton && canApplyFrequency())
        {
            int frequency = Integer.valueOf(this.frequencyTextfield.getText());
            ((ItemTalkieWalkie) ItemRegistry.TALKIE_WALKIE).setFrequency(getStack(), frequency);
        }
    }

    private boolean isFrequencyNew()
    {
        return Integer.valueOf(this.frequencyTextfield.getText()) != ((ItemTalkieWalkie) ItemRegistry.TALKIE_WALKIE).getFrequency(getStack());
    }

    private boolean canRemoveEntry()
    {
        return customFrequenciesList.getSelectedEntry() != null;
    }

    private boolean canApplyFrequency()
    {
        return NumberUtils.isDigits(this.frequencyTextfield.getText()) && Integer.valueOf(this.frequencyTextfield.getText()) > 0;
    }

    private boolean canAddEntry()
    {
        return canApplyFrequency() && !this.customNameTextfield.getText().isEmpty();
    }

    public List<CustomFrequency> getCustomFrequencyList()
    {
        return player.getCapability(CommonProxy.SAVED_FREQUENCIES_CAPABILITY, null).getCustomFrequencyList();
    }

    public ItemStack getStack()
    {
        return stack;
    }

    public GuiTextField getFrequencyTextfield()
    {
        return frequencyTextfield;
    }
}
