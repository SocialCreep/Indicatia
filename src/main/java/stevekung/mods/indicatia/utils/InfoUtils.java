package stevekung.mods.indicatia.utils;

import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.Potion;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.*;
import stevekung.mods.indicatia.config.ExtendedConfig;
import stevekung.mods.indicatia.event.IndicatiaEventHandler;
import stevekung.mods.stevekunglib.util.ColorUtils;

public class InfoUtils
{
    public static final InfoUtils INSTANCE = new InfoUtils();
    public Entity extendedPointedEntity;
    private Entity pointedEntity;

    private InfoUtils() {}

    public int getPing()
    {
        NetworkPlayerInfo info = Minecraft.getMinecraft().getConnection().getPlayerInfo(Minecraft.getMinecraft().player.getUniqueID());

        if (info != null)
        {
            if (info.getResponseTime() > 0)
            {
                return info.getResponseTime();
            }
            else
            {
                return IndicatiaEventHandler.currentServerPing;
            }
        }
        return 0;
    }

    public boolean isHypixel()
    {
        ServerData server = Minecraft.getMinecraft().getCurrentServerData();

        if (server != null)
        {
            Pattern pattern = Pattern.compile("^(?:(?:(?:\\w+\\.)?hypixel\\.net)|(?:209\\.222\\.115\\.(?:18|27|8|40|36|33|19|38|16|43|10|46|48|47|39|20|30|23|21|99)))(?::\\d{1,5})?$", Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(server.serverIP);
            return matcher.find();
        }
        return false;
    }

    public int getCPS()
    {
        Iterator<Long> iterator = IndicatiaEventHandler.LEFT_CLICK.iterator();

        while (iterator.hasNext())
        {
            if (iterator.next().longValue() < System.currentTimeMillis() - 1000L)
            {
                iterator.remove();
            }
        }
        return IndicatiaEventHandler.LEFT_CLICK.size();
    }

    public int getRCPS()
    {
        Iterator<Long> iterator = IndicatiaEventHandler.RIGHT_CLICK.iterator();

        while (iterator.hasNext())
        {
            if (iterator.next().longValue() < System.currentTimeMillis() - 1000L)
            {
                iterator.remove();
            }
        }
        return IndicatiaEventHandler.RIGHT_CLICK.size();
    }

    public String getCurrentGameTime(long worldTicks)
    {
        int hours = (int)((worldTicks / 1000 + 6) % 24);
        int minutes = (int)(60 * (worldTicks % 1000) / 1000);
        String sminutes = "" + minutes;
        String shours = "" + hours;
        String ampm = hours >= 12 ? "PM" : "AM";

        if (hours <= 9)
        {
            shours = 0 + "" + hours;
        }
        if (minutes <= 9)
        {
            sminutes = 0 + "" + minutes;
        }
        return ColorUtils.stringToRGB(ExtendedConfig.gameTimeColor).toColoredFont() + "Game: " + ColorUtils.stringToRGB(ExtendedConfig.gameTimeValueColor).toColoredFont() + shours + ":" + sminutes + " " + ampm;
    }

    public String getMoonPhase(Minecraft mc)
    {
        int[] moonPhaseFactors = { 4, 3, 2, 1, 0, -1, -2, -3 };
        int phase = moonPhaseFactors[mc.world.provider.getMoonPhase(mc.world.getWorldTime())];
        String status;

        switch (phase)
        {
        case 4:
        default:
            status = "Full Moon";
            break;
        case 3:
            status = "Waning Gibbous";
            break;
        case 2:
            status = "Last Quarter";
            break;
        case 1:
            status = "Waning Crescent";
            break;
        case 0:
            status = "New Moon";
            break;
        case -1:
            status = "Waxing Crescent";
            break;
        case -2:
            status = "First Quarter";
            break;
        case -3:
            status = "Waxing Gibbous";
            break;
        }
        return ColorUtils.stringToRGB(ExtendedConfig.moonPhaseColor).toColoredFont() + "Moon Phase: " + ColorUtils.stringToRGB(ExtendedConfig.moonPhaseValueColor).toColoredFont() + status;
    }

    public boolean isSlimeChunk(BlockPos pos)
    {
        int x = MathHelper.intFloorDiv(pos.getX(), 16);
        int z = MathHelper.intFloorDiv(pos.getZ(), 16);
        Random rnd = new Random(ExtendedConfig.slimeChunkSeed + x * x * 4987142 + x * 5947611 + z * z * 4392871L + z * 389711 ^ 987234911L);
        return rnd.nextInt(10) == 0;
    }

    public int getAlternatePotionHUDTextColor(Potion potion)
    {
        int color = 0;

        if (potion == MobEffects.ABSORPTION)
        {
            color = ColorUtils.rgbToDecimal(247, 219, 21);
        }
        else if (potion == MobEffects.REGENERATION)
        {
            color = ColorUtils.rgbToDecimal(244, 120, 226);
        }
        else if (potion == MobEffects.STRENGTH)
        {
            color = ColorUtils.rgbToDecimal(179, 55, 55);
        }
        else if (potion == MobEffects.SPEED)
        {
            color = ColorUtils.rgbToDecimal(120, 201, 224);
        }
        else if (potion == MobEffects.FIRE_RESISTANCE)
        {
            color = ColorUtils.rgbToDecimal(233, 157, 73);
        }
        else if (potion == MobEffects.RESISTANCE)
        {
            color = ColorUtils.rgbToDecimal(137, 140, 154);
        }
        else if (potion == MobEffects.JUMP_BOOST)
        {
            color = ColorUtils.rgbToDecimal(33, 251, 75);
        }
        else if (potion == MobEffects.NIGHT_VISION)
        {
            color = ColorUtils.rgbToDecimal(97, 97, 224);
        }
        else if (potion == MobEffects.WATER_BREATHING)
        {
            color = ColorUtils.rgbToDecimal(79, 122, 202);
        }
        else if (potion == MobEffects.SLOWNESS)
        {
            color = ColorUtils.rgbToDecimal(103, 123, 146);
        }
        else if (potion == MobEffects.HASTE)
        {
            color = ColorUtils.rgbToDecimal(182, 169, 80);
        }
        else if (potion == MobEffects.MINING_FATIGUE)
        {
            color = ColorUtils.rgbToDecimal(90, 81, 29);
        }
        else if (potion == MobEffects.NAUSEA)
        {
            color = ColorUtils.rgbToDecimal(125, 43, 108);
        }
        else if (potion == MobEffects.INVISIBILITY)
        {
            color = ColorUtils.rgbToDecimal(139, 142, 156);
        }
        else if (potion == MobEffects.BLINDNESS)
        {
            color = ColorUtils.rgbToDecimal(90, 90, 90);
        }
        else if (potion == MobEffects.HUNGER)
        {
            color = ColorUtils.rgbToDecimal(99, 133, 92);
        }
        else if (potion == MobEffects.WEAKNESS)
        {
            color = ColorUtils.rgbToDecimal(102, 108, 102);
        }
        else if (potion == MobEffects.POISON)
        {
            color = ColorUtils.rgbToDecimal(81, 152, 50);
        }
        else if (potion == MobEffects.WITHER)
        {
            color = ColorUtils.rgbToDecimal(105, 84, 80);
        }
        else if (potion == MobEffects.HEALTH_BOOST)
        {
            color = ColorUtils.rgbToDecimal(245, 124, 35);
        }
        else if (potion == MobEffects.GLOWING)
        {
            color = ColorUtils.rgbToDecimal(146, 158, 96);
        }
        else if (potion == MobEffects.LEVITATION)
        {
            color = ColorUtils.rgbToDecimal(204, 252, 252);
        }
        else if (potion == MobEffects.LUCK)
        {
            color = ColorUtils.rgbToDecimal(50, 151, 0);
        }
        else if (potion == MobEffects.UNLUCK)
        {
            color = ColorUtils.rgbToDecimal(190, 162, 76);
        }
        //TODO 1.13 slow falling, conduit power
        return color;
    }

    public void processMouseOverEntity(Minecraft mc)
    {
        Entity entity = mc.getRenderViewEntity();
        double distance = 12.0D;

        if (entity != null)
        {
            if (mc.world != null)
            {
                this.extendedPointedEntity = null;
                mc.objectMouseOver = entity.rayTrace(distance, mc.getRenderPartialTicks());
                Vec3d vec3d = entity.getPositionEyes(mc.getRenderPartialTicks());
                boolean flag = false;
                double d1 = distance;

                if (mc.playerController.extendedReach())
                {
                    d1 = distance;
                    distance = d1;
                }
                else
                {
                    if (distance > distance)
                    {
                        flag = true;
                    }
                }

                if (mc.objectMouseOver != null)
                {
                    d1 = mc.objectMouseOver.hitVec.distanceTo(vec3d);
                }

                Vec3d vec3d1 = entity.getLook(1.0F);
                Vec3d vec3d2 = vec3d.addVector(vec3d1.x * distance, vec3d1.y * distance, vec3d1.z * distance);
                this.pointedEntity = null;
                Vec3d vec3d3 = null;
                List<Entity> list = mc.world.getEntitiesInAABBexcluding(entity, entity.getEntityBoundingBox().expand(vec3d1.x * distance, vec3d1.y * distance, vec3d1.z * distance).grow(1.0D, 1.0D, 1.0D), Predicates.and(EntitySelectors.NOT_SPECTATING, (Predicate<Entity>) entry -> entry != null && entry.canBeCollidedWith()));
                double d2 = d1;

                for (int j = 0; j < list.size(); ++j)
                {
                    Entity entity1 = list.get(j);
                    AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().grow(entity1.getCollisionBorderSize());
                    RayTraceResult raytraceresult = axisalignedbb.calculateIntercept(vec3d, vec3d2);

                    if (axisalignedbb.contains(vec3d))
                    {
                        if (d2 >= 0.0D)
                        {
                            this.pointedEntity = entity1;
                            vec3d3 = raytraceresult == null ? vec3d : raytraceresult.hitVec;
                            d2 = 0.0D;
                        }
                    }
                    else if (raytraceresult != null)
                    {
                        double d3 = vec3d.distanceTo(raytraceresult.hitVec);

                        if (d3 < d2 || d2 == 0.0D)
                        {
                            if (entity1.getLowestRidingEntity() == entity.getLowestRidingEntity() && !entity1.canRiderInteract())
                            {
                                if (d2 == 0.0D)
                                {
                                    this.pointedEntity = entity1;
                                    vec3d3 = raytraceresult.hitVec;
                                }
                            }
                            else
                            {
                                this.pointedEntity = entity1;
                                vec3d3 = raytraceresult.hitVec;
                                d2 = d3;
                            }
                        }
                    }
                }
                if (this.pointedEntity != null && flag && vec3d.distanceTo(vec3d3) > distance)
                {
                    this.pointedEntity = null;
                    mc.objectMouseOver = new RayTraceResult(RayTraceResult.Type.MISS, vec3d3, (EnumFacing)null, new BlockPos(vec3d3));
                }
                if (this.pointedEntity != null && (d2 < d1 || mc.objectMouseOver == null))
                {
                    mc.objectMouseOver = new RayTraceResult(this.pointedEntity, vec3d3);

                    if (this.pointedEntity instanceof EntityLivingBase || this.pointedEntity instanceof EntityItemFrame)
                    {
                        this.extendedPointedEntity = this.pointedEntity;
                    }
                }
                mc.mcProfiler.endSection();
            }
        }
    }
}