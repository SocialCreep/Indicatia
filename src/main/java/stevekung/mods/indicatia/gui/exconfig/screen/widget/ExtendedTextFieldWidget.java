package stevekung.mods.indicatia.gui.exconfig.screen.widget;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import stevekung.mods.indicatia.config.ExtendedConfig;
import stevekung.mods.indicatia.gui.exconfig.TextFieldConfigOption;

@OnlyIn(Dist.CLIENT)
public class ExtendedTextFieldWidget extends TextFieldWidget
{
    private final TextFieldConfigOption textFieldOption;
    private String displayName;
    private String displayPrefix;

    public ExtendedTextFieldWidget(int x, int y, int width, TextFieldConfigOption textFieldOption)
    {
        super(Minecraft.getInstance().fontRenderer, x, y, width, 20, "");
        this.textFieldOption = textFieldOption;
        this.setText(textFieldOption.get());
        this.setVisible(true);
        this.setMaxStringLength(13);
    }

    public void setValue(String value)
    {
        this.textFieldOption.set(value);
        ExtendedConfig.INSTANCE.save();
    }

    public void setDisplayName(String name)
    {
        this.displayName = name;
    }

    public String getDisplayName()
    {
        return this.displayName;
    }

    public void setDisplayPrefix(String name)
    {
        this.displayPrefix = name;
    }

    public String getDisplayPrefix()
    {
        return this.displayPrefix;
    }
}