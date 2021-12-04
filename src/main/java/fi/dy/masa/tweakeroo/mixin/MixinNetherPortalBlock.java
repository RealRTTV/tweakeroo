package fi.dy.masa.tweakeroo.mixin;

import fi.dy.masa.tweakeroo.config.Configs;
import net.minecraft.block.BlockState;
import net.minecraft.block.NetherPortalBlock;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(NetherPortalBlock.class)
public class MixinNetherPortalBlock {
    @Inject(method = "randomDisplayTick", at = @At("HEAD"), cancellable = true)
    private void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random, CallbackInfo ci)
    {
        if (random.nextInt(100) == 0)
        {
            world.playSound((double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D, SoundEvents.BLOCK_PORTAL_AMBIENT, SoundCategory.BLOCKS, (float) Configs.Generic.NETHER_PORTAL_VOLUME.getDoubleValue(), random.nextFloat() * 0.4F + 0.8F, false);
        }
        ci.cancel();
    }
}
