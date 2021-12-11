package fi.dy.masa.tweakeroo.mixin;

import net.minecraft.text.ClickEvent;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import fi.dy.masa.tweakeroo.config.FeatureToggle;
import fi.dy.masa.tweakeroo.util.MiscUtils;

@Mixin(value = net.minecraft.client.gui.hud.ChatHud.class, priority = 1100)
public abstract class MixinChatHud extends net.minecraft.client.gui.DrawableHelper
{
    @ModifyVariable(method = "addMessage(Lnet/minecraft/text/Text;I)V", at = @At("HEAD"), argsOnly = true)
    private Text modifyText(Text componentIn)
    {
        LiteralText newComponent = new LiteralText("");
        if (FeatureToggle.TWEAK_CHAT_TIMESTAMP.getBooleanValue())
        {
            newComponent.append(MiscUtils.getChatTimestamp() + " ");
        }
        if (FeatureToggle.TWEAK_CLICK_TO_COPY_CHAT_MESSAGE.getBooleanValue())
        {
                newComponent.append(componentIn).styled(style -> style.withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, componentIn.getString())));
        }
        else
        {
            newComponent.append(componentIn);
        }
        return newComponent;
    }

    @Redirect(method = "render", at = @At(value = "INVOKE",
                target = "Lnet/minecraft/client/gui/hud/ChatHud;fill(Lnet/minecraft/client/util/math/MatrixStack;IIIII)V", ordinal = 0))
    private void overrideChatBackgroundColor(net.minecraft.client.util.math.MatrixStack matrixStack, int left, int top, int right, int bottom, int color)
    {
        if (FeatureToggle.TWEAK_CHAT_BACKGROUND_COLOR.getBooleanValue())
        {
            color = MiscUtils.getChatBackgroundColor(color);
        }

        fill(matrixStack, left, top, right, bottom, color);
    }
}
