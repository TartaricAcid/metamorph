package mchorse.metamorph.client;

import mchorse.metamorph.api.morphs.AbstractMorph;
import mchorse.metamorph.api.morphs.CustomMorph;
import mchorse.metamorph.api.morphs.EntityMorph;
import mchorse.metamorph.capabilities.morphing.IMorphing;
import mchorse.metamorph.capabilities.morphing.Morphing;
import mchorse.metamorph.client.gui.GuiMenu;
import mchorse.metamorph.client.gui.GuiOverlay;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Rendering handler
 *
 * This handler is rendering handler which is responsible for few things:
 * 
 * - Overlays (survival morph menu, morph acquiring)
 * - Player model
 */
@SideOnly(Side.CLIENT)
public class RenderingHandler
{
    private GuiMenu overlay;
    private GuiOverlay morphOverlay;

    public RenderingHandler(GuiMenu overlay, GuiOverlay morphOverlay)
    {
        this.overlay = overlay;
        this.morphOverlay = morphOverlay;
    }

    /**
     * Draw HUD additions 
     */
    @SubscribeEvent
    public void onHUDRender(RenderGameOverlayEvent.Post event)
    {
        ScaledResolution resolution = event.getResolution();

        if (event.getType() == RenderGameOverlayEvent.ElementType.ALL)
        {
            this.overlay.render(resolution.getScaledWidth(), resolution.getScaledHeight());
            this.morphOverlay.render(resolution.getScaledWidth(), resolution.getScaledHeight());
        }
    }

    /**
     * Morph (render) player into custom model if player has its variables
     * related to morphing (model and skin)
     */
    @SubscribeEvent
    @SuppressWarnings({"rawtypes", "unchecked"})
    public void onPlayerRender(RenderPlayerEvent.Pre event)
    {
        EntityPlayer player = event.getEntityPlayer();
        IMorphing capability = Morphing.get(player);

        if (capability == null || !capability.isMorphed())
        {
            return;
        }

        AbstractMorph morph = capability.getCurrentMorph();
        EntityLivingBase entity = null;

        if (morph instanceof EntityMorph)
        {
            entity = ((EntityMorph) morph).getEntity(Minecraft.getMinecraft().world);

            /* Make transformation seamless... */
            entity.rotationYaw = player.rotationYaw;
            entity.rotationPitch = player.rotationPitch;
            entity.rotationYawHead = player.rotationYawHead;
            entity.renderYawOffset = player.renderYawOffset;

            entity.prevRotationYaw = player.prevRotationYaw;
            entity.prevRotationPitch = player.prevRotationPitch;
            entity.prevRotationYawHead = player.prevRotationYawHead;
            entity.prevRenderYawOffset = player.prevRenderYawOffset;
        }

        Render render = morph.renderer;

        if (render != null)
        {
            event.setCanceled(true);
            boolean inGUI = Minecraft.getMinecraft().currentScreen != null;

            if (inGUI)
            {
                float scale = 1;
                float height = morph.getHeight(player);

                if (height > 2F)
                {
                    scale = 1.3F / height;
                }

                GlStateManager.pushMatrix();
                GlStateManager.scale(scale, scale, scale);
            }

            if (morph instanceof CustomMorph)
            {
                render.doRender(player, event.getX(), event.getY(), event.getZ(), player.rotationYaw, event.getPartialRenderTick());
            }
            else if (morph instanceof EntityMorph)
            {
                render.doRender(entity, event.getX(), event.getY(), event.getZ(), player.rotationYaw, event.getPartialRenderTick());
            }

            if (inGUI)
            {
                GlStateManager.popMatrix();
            }
        }
    }
}