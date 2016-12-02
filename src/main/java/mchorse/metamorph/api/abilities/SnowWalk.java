package mchorse.metamorph.api.abilities;

import net.minecraft.block.material.Material;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

/**
 * Snow walk ability
 * 
 * This ability grants player snowy walk. This ability is really cool and 
 * probably only will be used for snow man morph.
 * 
 * Totally not taken from {@link EntitySnowman#onLivingUpdate()}.
 */
public class SnowWalk extends Ability
{
    @Override
    public void update(EntityPlayer player)
    {
        if (!player.onGround)
        {
            return;
        }

        int i = MathHelper.floor(player.posX);
        int j = MathHelper.floor(player.posY);
        int k = MathHelper.floor(player.posZ);

        BlockPos blockpos = new BlockPos(i, j, k);

        if (player.world.getBlockState(blockpos).getMaterial() == Material.AIR && Blocks.SNOW_LAYER.canPlaceBlockAt(player.world, blockpos))
        {
            player.world.setBlockState(blockpos, Blocks.SNOW_LAYER.getDefaultState());
        }
    }
}