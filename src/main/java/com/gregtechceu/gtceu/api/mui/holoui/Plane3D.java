package com.gregtechceu.gtceu.api.mui.holoui;

import com.mojang.blaze3d.vertex.PoseStack;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.ApiStatus;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

/**
 * Highly experimental
 */
@ApiStatus.Experimental
public class Plane3D {

    @Getter
    private float width = 480, height = 270;
    @Getter
    @Setter
    private float scale = 1f;
    private float anchorX = 0.5f, anchorY = 0.5f;
    private float normalX = 0, normalY = 0, normalZ = 1;

    public void transformRectangle(PoseStack poseStack) {
        // translate to anchor
        poseStack.translate(-this.width * this.anchorX, -this.height * this.anchorY, 0);
        // translate for scale and rotation
        poseStack.translate(this.width / 2f, this.height / 2f, 0);
        // scale to size. 0.0625 is 1/16
        poseStack.scale(0.0625f * this.scale, 0.0625f * this.scale, 0.0625f * this.scale);
        // rotate 180 deg
        poseStack.mulPose(new Quaternionf(180, 0, 0, 1));
        // apply facing direction
        // spotless:off
        if (this.normalX != 0 || this.normalY != 0 || this.normalZ != 1) {
            Matrix4f rotation = new Matrix4f()
                    .m00(-this.normalZ + (this.normalY * this.normalY * (1 + this.normalZ)) / (this.normalX * this.normalX + this.normalY * this.normalY))
                    .m10(-(this.normalX * this.normalY * (1 + this.normalZ)) / (this.normalX * this.normalX + this.normalY * this.normalY))
                    .m20(this.normalX)
                    .m01(-(this.normalX * this.normalY * (1 + this.normalZ)) / (this.normalX * this.normalX + this.normalY * this.normalY))
                    .m11(-this.normalZ + (this.normalX * this.normalX * (1 + this.normalZ)) / (this.normalX * this.normalX + this.normalY * this.normalY))
                    .m21(this.normalY)
                    .m02(-this.normalX)
                    .m12(-this.normalY)
                    .m22(-this.normalZ);
            poseStack.mulPoseMatrix(rotation);
        }
        // spotless:on
        // un-translate for scale and rotation
        poseStack.translate(-(this.width / 2f), -(this.height / 2f), 0);
    }

    public void setSize(float w, float h) {
        this.width = w;
        this.height = h;
    }

    public void setWidthWithProp(float w) {
        float factor = w / this.width;
        this.width = w;
        this.height *= factor;
    }

    public void setHeightWithProp(float h) {
        float factor = h / this.height;
        this.width *= factor;
        this.height = h;
    }

    public void setNormal(float x, float y, float z) {
        float square = x * x + y * y + z * z;
        if (square != 1) {
            float factor = (float) Math.sqrt(square);
            x /= factor;
            y /= factor;
            z /= factor;
        }
        this.normalX = x;
        this.normalY = y;
        this.normalZ = z;
    }

    public void setAnchor(float x, float y) {
        this.anchorX = x;
        this.anchorY = y;
    }
}
