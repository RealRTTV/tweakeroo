package fi.dy.masa.tweakeroo.mixin;

import java.text.SimpleDateFormat;
import java.util.function.Supplier;

import fi.dy.masa.tweakeroo.config.FeatureToggle;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.block.BlockState;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.MutableWorldProperties;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import fi.dy.masa.tweakeroo.config.Configs;

@Mixin(ClientWorld.class)
public abstract class MixinClientWorld extends World
{
    protected MixinClientWorld(MutableWorldProperties properties, RegistryKey<World> registryKey, DimensionType dimensionType, Supplier<Profiler> supplier, boolean bl, boolean bl2, long l)
    {
        super(properties, registryKey, dimensionType, supplier, bl, bl2, l);
    }

    @Inject(method = "tickEntity", at = @At("HEAD"), cancellable = true)
    private void disableClientEntityTicking(Entity entity, CallbackInfo ci)
    {
        if (Configs.Disable.DISABLE_CLIENT_ENTITY_UPDATES.getBooleanValue() &&
            (entity instanceof PlayerEntity) == false)
        {
            ci.cancel();
        }
    }

    /* TODO 1.17 is this still needed?
    @Inject(method = "addEntitiesToChunk", at = @At("HEAD"), cancellable = true)
    private void fixChunkEntityLeak(WorldChunk chunk, CallbackInfo ci)
    {
        if (Configs.Fixes.CLIENT_CHUNK_ENTITY_DUPE.getBooleanValue())
        {
            for (int y = 0; y < 16; ++y)
            {
                // The chunk already has entities, which means it's a re-used existing chunk,
                // in such a case we don't want to add the from the world entities again, otherwise
                // they are basically duped within the Chunk.
                if (chunk.getEntitySectionArray()[y].size() > 0)
                {
                    ci.cancel();
                    return;
                }
            }
        }
    }
    */

    @Inject(method = "scheduleBlockRerenderIfNeeded", at = @At("HEAD"), cancellable = true)
    private void disableChunkReRenders(BlockPos pos, BlockState old, BlockState updated, CallbackInfo ci)
    {
        if (Configs.Disable.DISABLE_CHUNK_RENDERING.getBooleanValue())
        {
            ci.cancel();
        }
    }

    @Inject(method = "scheduleBlockRenders", at = @At("HEAD"), cancellable = true)
    private void disableChunkReRenders(int x, int y, int z, CallbackInfo ci)
    {
        if (Configs.Disable.DISABLE_CHUNK_RENDERING.getBooleanValue())
        {
            ci.cancel();
        }
    }

    @Inject(method = "updateListeners", at = @At("HEAD"), cancellable = true)
    private void disableChunkReRenders(BlockPos pos, BlockState oldState, BlockState newState, int flags, CallbackInfo ci)
    {
        if (Configs.Disable.DISABLE_CHUNK_RENDERING.getBooleanValue())
        {
            ci.cancel();
        }
    }

    @Shadow
    @Final
    private ClientPlayNetworkHandler netHandler;

    @Inject(method = "disconnect", at = @At("HEAD"), cancellable = true)
    private void disconnect(CallbackInfo ci)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        if (MinecraftClient.getInstance().player != null && FeatureToggle.TWEAK_AUTO_LOGOUT.getBooleanValue() &&
        MinecraftClient.getInstance().player.getHealth() <= (float) Configs.Generic.HEALTH_LOGOUT_THRESHOLD.getDoubleValue() && MinecraftClient.getInstance().world != null)
        {
            this.netHandler.getConnection().disconnect(Text.of("Health logout threshold reached, disconnected at " + sdf.format(System.currentTimeMillis())));
            ci.cancel();
        }
    }
}
