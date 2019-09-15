package com.stevekung.indicatia.command;

import com.mojang.brigadier.CommandDispatcher;
import com.stevekung.indicatia.event.IndicatiaEventHandler;
import com.stevekung.stevekungslib.utils.LangUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.item.FishingRodItem;

public class AutoFishCommand
{
    public static void register(CommandDispatcher<CommandSource> dispatcher)
    {
        dispatcher.register(Commands.literal("autofish").requires(requirement -> requirement.hasPermissionLevel(0)).executes(requirement -> AutoFishCommand.doAutofish(requirement.getSource())));
    }

    private static int doAutofish(CommandSource source)
    {
        if (!IndicatiaEventHandler.START_AUTO_FISH)
        {
            ClientPlayerEntity player = Minecraft.getInstance().player;
            boolean mainHand = player.getHeldItemMainhand().getItem() instanceof FishingRodItem;
            boolean offHand = player.getHeldItemOffhand().getItem() instanceof FishingRodItem;

            if (player.getHeldItemMainhand().getItem() instanceof FishingRodItem)
            {
                offHand = false;
            }

            if (mainHand || offHand)
            {
                IndicatiaEventHandler.START_AUTO_FISH = true;
                source.sendFeedback(LangUtils.translateComponent("commands.auto_fish.enable"), false);
                return 1;
            }
            else
            {
                source.sendErrorMessage(LangUtils.translateComponent("commands.auto_fish.not_equipped_fishing_rod"));
                return 0;
            }
        }
        else
        {
            IndicatiaEventHandler.START_AUTO_FISH = false;
            source.sendFeedback(LangUtils.translateComponent("commands.auto_fish.disable"), false);
            return 1;
        }
    }
}