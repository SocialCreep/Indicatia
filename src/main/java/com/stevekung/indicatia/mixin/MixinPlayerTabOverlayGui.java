package com.stevekung.indicatia.mixin;

import java.util.List;

import javax.annotation.Nullable;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import com.google.common.collect.Ordering;
import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.platform.GlStateManager;
import com.stevekung.indicatia.config.ExtendedConfig;
import com.stevekung.indicatia.config.IndicatiaConfig;
import com.stevekung.indicatia.config.PingMode;
import com.stevekung.indicatia.event.IndicatiaEventHandler;
import com.stevekung.indicatia.hud.InfoUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.overlay.PlayerTabOverlayGui;
import net.minecraft.client.network.play.NetworkPlayerInfo;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerModelPart;
import net.minecraft.scoreboard.ScoreCriteria;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.GameType;

@Mixin(PlayerTabOverlayGui.class)
public abstract class MixinPlayerTabOverlayGui extends AbstractGui
{
    @Shadow
    @Final
    private Minecraft mc;

    @Shadow
    @Final
    private static Ordering<NetworkPlayerInfo> ENTRY_ORDERING;

    @Shadow
    private ITextComponent footer;

    @Shadow
    private ITextComponent header;

    @Shadow
    public abstract ITextComponent getDisplayName(NetworkPlayerInfo info);

    @Shadow
    protected abstract void drawScoreboardValues(ScoreObjective objective, int p_175247_2_, String name, int p_175247_4_, int p_175247_5_, NetworkPlayerInfo info);

    @Overwrite
    public void render(int width, Scoreboard scoreboard, @Nullable ScoreObjective scoreObjective)
    {
        List<NetworkPlayerInfo> list = MixinPlayerTabOverlayGui.ENTRY_ORDERING.sortedCopy(this.mc.player.connection.getPlayerInfoMap());
        int listWidth = 0;
        int j = 0;

        for (NetworkPlayerInfo info : list)
        {
            int ping = info.getResponseTime();
            String pingText = String.valueOf(ping);

            if (ExtendedConfig.INSTANCE.pingMode == PingMode.PING_AND_DELAY)
            {
                pingText = String.valueOf(ping) + "/" + String.format("%.2f", (float)ping / 1000) + "s";
            }

            int pingWidth = IndicatiaConfig.GENERAL.enableCustomPlayerList.get() ? this.mc.fontRenderer.getStringWidth(pingText) : 0;
            int stringWidth = this.mc.fontRenderer.getStringWidth(this.getDisplayName(info).getFormattedText()) + pingWidth;
            listWidth = Math.max(listWidth, stringWidth);

            if (scoreObjective != null && scoreObjective.getRenderType() != ScoreCriteria.RenderType.HEARTS)
            {
                stringWidth = this.mc.fontRenderer.getStringWidth(" " + scoreboard.getOrCreateScore(info.getGameProfile().getName(), scoreObjective).getScorePoints());
                j = Math.max(j, stringWidth);
            }
        }

        list = list.subList(0, Math.min(list.size(), 80));
        int playerListSize = list.size();
        int playerCount = playerListSize;
        int columnSize;

        for (columnSize = 1; playerCount > 20; playerCount = (playerListSize + columnSize - 1) / columnSize)
        {
            ++columnSize;
        }

        boolean flag = this.mc.isIntegratedServerRunning() || this.mc.getConnection().getNetworkManager().isEncrypted();
        int l;

        if (scoreObjective != null)
        {
            if (scoreObjective.getRenderType() == ScoreCriteria.RenderType.HEARTS)
            {
                l = 90;
            }
            else
            {
                l = j;
            }
        }
        else
        {
            l = 0;
        }

        int i1 = Math.min(columnSize * ((flag ? 9 : 0) + listWidth + l + 13), width - 50) / columnSize;
        int j1 = width / 2 - (i1 * columnSize + (columnSize - 1) * 5) / 2;
        int yOffset = 10;
        int l1 = i1 * columnSize + (columnSize - 1) * 5;
        List<String> list1 = null;

        if (this.header != null)
        {
            list1 = this.mc.fontRenderer.listFormattedStringToWidth(this.header.getFormattedText(), width - 50);

            for (String s : list1)
            {
                l1 = Math.max(l1, this.mc.fontRenderer.getStringWidth(s));
            }
        }

        List<String> list2 = null;

        if (this.footer != null)
        {
            list2 = this.mc.fontRenderer.listFormattedStringToWidth(this.footer.getFormattedText(), width - 50);

            for (String s1 : list2)
            {
                l1 = Math.max(l1, this.mc.fontRenderer.getStringWidth(s1));
            }
        }

        if (list1 != null)
        {
            AbstractGui.fill(width / 2 - l1 / 2 - 1, yOffset - 1, width / 2 + l1 / 2 + 1, yOffset + list1.size() * 9, Integer.MIN_VALUE);

            for (String s2 : list1)
            {
                int i2 = this.mc.fontRenderer.getStringWidth(s2);
                this.mc.fontRenderer.drawStringWithShadow(s2, width / 2 - i2 / 2, yOffset, -1);
                yOffset += 9;
            }
            ++yOffset;
        }

        AbstractGui.fill(width / 2 - l1 / 2 - 1, yOffset - 1, width / 2 + l1 / 2 + 1, yOffset + playerCount * 9, Integer.MIN_VALUE);

        for (int size = 0; size < playerListSize; ++size)
        {
            int j5 = size / playerCount;
            int j2 = size % playerCount;
            int k2 = j1 + j5 * i1 + j5 * 5;
            int l2 = yOffset + j2 * 9;

            AbstractGui.fill(k2, l2, k2 + i1, l2 + 8, this.mc.gameSettings.func_216839_a(553648127));
            GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.enableAlphaTest();
            GlStateManager.enableBlend();
            GlStateManager.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);

            if (size < list.size())
            {
                NetworkPlayerInfo info1 = list.get(size);
                GameProfile profile = info1.getGameProfile();

                if (flag)
                {
                    PlayerEntity player = this.mc.world.getPlayerByUuid(profile.getId());
                    boolean flag1 = player != null && player.isWearing(PlayerModelPart.CAPE) && ("Dinnerbone".equals(profile.getName()) || "Grumm".equals(profile.getName()));
                    this.mc.getTextureManager().bindTexture(info1.getLocationSkin());
                    int i3 = 8 + (flag1 ? 8 : 0);
                    int j3 = 8 * (flag1 ? -1 : 1);
                    AbstractGui.blit(k2, l2, 8, 8, 8.0F, i3, 8, j3, 64, 64);

                    if (player != null && player.isWearing(PlayerModelPart.HAT))
                    {
                        int k3 = 8 + (flag1 ? 8 : 0);
                        int l3 = 8 * (flag1 ? -1 : 1);
                        AbstractGui.blit(k2, l2, 8, 8, 40.0F, k3, 8, l3, 64, 64);
                    }
                    k2 += 9;
                }

                String s4 = this.getDisplayName(info1).getFormattedText();

                if (info1.getGameType() == GameType.SPECTATOR)
                {
                    this.mc.fontRenderer.drawStringWithShadow(TextFormatting.ITALIC + s4, k2, l2, -1862270977);
                }
                else
                {
                    this.mc.fontRenderer.drawStringWithShadow(s4, k2, l2, -1);
                }

                if (scoreObjective != null && info1.getGameType() != GameType.SPECTATOR)
                {
                    int l5 = j2 + listWidth + 1;
                    int i6 = l5 + l;

                    if (i6 - l5 > 5)
                    {
                        this.drawScoreboardValues(scoreObjective, l2, profile.getName(), l5, i6, info1);
                    }
                }
                this.drawPing(i1, k2 - (flag ? 9 : 0), l2, info1);
            }
        }

        if (list2 != null)
        {
            yOffset = yOffset + playerCount * 9 + 1;
            AbstractGui.fill(width / 2 - l1 / 2 - 1, yOffset - 1, width / 2 + l1 / 2 + 1, yOffset + list2.size() * 9, Integer.MIN_VALUE);

            for (String s3 : list2)
            {
                int j5 = this.mc.fontRenderer.getStringWidth(s3);
                this.mc.fontRenderer.drawStringWithShadow(s3, width / 2 - j5 / 2, yOffset, -1);
                yOffset += 9;
            }
        }
    }

    @Overwrite
    protected void drawPing(int x1, int x2, int y, NetworkPlayerInfo info)
    {
        int ping = InfoUtils.INSTANCE.isHypixel() && info.getGameProfile().getName().equals(ExtendedConfig.INSTANCE.hypixelNickName) ? IndicatiaEventHandler.currentServerPing : info.getResponseTime();

        if (IndicatiaConfig.GENERAL.enableCustomPlayerList.get())
        {
            TextFormatting color = TextFormatting.GREEN;
            String pingText = String.valueOf(ping);

            if (ping >= 200 && ping < 300)
            {
                color = TextFormatting.YELLOW;
            }
            else if (ping >= 300 && ping < 500)
            {
                color = TextFormatting.RED;
            }
            else if (ping >= 500)
            {
                color = TextFormatting.DARK_RED;
            }

            if (ExtendedConfig.INSTANCE.pingMode == PingMode.PING_AND_DELAY)
            {
                pingText = String.valueOf(ping) + "/" + String.format("%.2f", (float)ping / 1000) + "s";
                //this.mc.fontRenderer.setUnicodeFlag(true);TODO
            }

            this.mc.fontRenderer.drawStringWithShadow(color + pingText, x1 + x2 - this.mc.fontRenderer.getStringWidth(pingText), y + 0.625F, 0);

            if (ExtendedConfig.INSTANCE.pingMode == PingMode.PING_AND_DELAY)
            {
                //this.mc.fontRenderer.setUnicodeFlag(false);
            }
        }
        else
        {
            GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.mc.getTextureManager().bindTexture(AbstractGui.GUI_ICONS_LOCATION);
            int state;

            if (ping < 0)
            {
                state = 5;
            }
            else if (ping < 150)
            {
                state = 0;
            }
            else if (ping < 300)
            {
                state = 1;
            }
            else if (ping < 600)
            {
                state = 2;
            }
            else if (ping < 1000)
            {
                state = 3;
            }
            else
            {
                state = 4;
            }
            this.blitOffset += 100.0F;
            this.blit(x2 + x1 - 11, y, 0, 176 + state * 8, 10, 8);
            this.blitOffset -= 100.0F;
        }
    }
}