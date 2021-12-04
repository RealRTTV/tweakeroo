package fi.dy.masa.tweakeroo.mixin;


import fi.dy.masa.tweakeroo.config.Configs;
import net.minecraft.client.render.Camera;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Camera.class)
public class MixinCamera {
    // thanks wurst :p
    @Inject(at = {@At("HEAD")}, method = {"clipToSpace(D)D"}, cancellable = true)
    private void onClipToSpace(double desiredCameraDistance, CallbackInfoReturnable<Double> ci) {
        if (Configs.Disable.DISABLE_F5_AVOID_BLOCKS.getBooleanValue())
            ci.setReturnValue(desiredCameraDistance);
    }
}
