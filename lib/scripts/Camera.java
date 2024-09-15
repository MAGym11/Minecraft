package lib.scripts;

import java.lang.Math;
import java.awt.Component;

public class Camera {

    public int width, height;
    public int[] pixels;
    public int x, y, z;
    public double fov;
    public int focalDistance;
    public double xRot, yRot;
    public int nRenderedFaces;
    public double sy, cy, sx, cx;

    public Camera(int width, int height, double fov) {
        this.width = width;
        this.height = height;
        this.pixels = new int[width * height];
        this.x = 0;
        this.y = 0;
        this.z = 0;
        this.fov = fov;
        this.focalDistance = (int)((285) / Math.tan(fov / 2.0));
        this.xRot = 0;
        this.yRot = 0;
        this.sy = Math.sin(this.yRot);
        this.cy = Math.cos(this.yRot);
        this.sx = Math.sin(this.xRot);
        this.cx = Math.cos(this.xRot);
    }

    public void clear() {
        for (int i = 0; i < pixels.length; i++) {
            pixels[i] = 0x333333;
        }
    }

    public void updateDimensions(Component c) {
        this.width = c.getWidth();
        this.height = c.getHeight();
        this.focalDistance = (int)((285) / Math.tan(fov / 2));
    }

    public void update(int x, int y, int z) {
        this.x += (x * Math.cos(this.yRot) + z * Math.sin(this.yRot));
        this.y += y;
        this.z += (x * -Math.sin(this.yRot) + z * Math.cos(this.yRot));
    }

    public void castRay(World world, Player player, boolean action) {
        if (action) {
            Block blockbefore = findBlock(world, player.x, player.y + 512, player.z);
            for (int i = 1; i < 19; i++) {
                blockbefore = findBlock(world, (int)(((i-1)*128) * Math.cos(this.xRot) * Math.sin(this.yRot)) + this.x, (int)(((i-1)*128) * Math.sin(this.xRot)) - this.y, (int)(((i-1)*128) * Math.cos(this.xRot) * Math.cos(this.yRot)) + this.z);
                try {
                    if (findBlock(world, (int)((i*128) * Math.cos(this.xRot) * Math.sin(this.yRot)) + this.x, (int)((i*128) * Math.sin(this.xRot)) - this.y, (int)((i*128) * Math.cos(this.xRot) * Math.cos(this.yRot)) + this.z).type != 0) {
                        if (!(blockbefore.x == findBlock(world, player.x, player.y, player.z).x && (blockbefore.y >> 9) == (player.y >> 9) && blockbefore.z == findBlock(world, player.x, player.y, player.z).z) &&
                            !(blockbefore.x == findBlock(world, player.x, player.y, player.z).x && (blockbefore.y >> 9) == (player.y >> 9) + 1 && blockbefore.z == findBlock(world, player.x, player.y, player.z).z)) {
                                if (i != 0) {
                                    blockbefore.type = player.selectedBlockType;
                                    blockbefore.update();
                                }
                        }
                        break;
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    if (!(blockbefore.x == findBlock(world, player.x, player.y, player.z).x &&
                        (blockbefore.y >> 9) == (player.y >> 9) + 1 &&
                        blockbefore.z == findBlock(world, player.x, player.y, player.z).z)) {
                            blockbefore.type = player.selectedBlockType;
                            blockbefore.update();
                    }
                    break;
                }
            }
        } else {
            Block block = findBlock(world, player.x, player.y, player.z);
            for (int i = 1; i < 10; i++) {
                try {
                    block = findBlock(world, (int)((i*256) * Math.cos(this.xRot) * Math.sin(this.yRot)) + this.x, (int)((i*256) * Math.sin(this.xRot)) - this.y, (int)((i*256) * Math.cos(this.xRot) * Math.cos(this.yRot)) + this.z);
                    if (block.type == 1) break;
                    if (block.type != 0) {
                        block.type = 0;
                        break;
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    if (block.type == 1) break;
                    if (block.type != 0) {
                        block.type = 0;
                        break;
                    }
                }
            }
        }
    }

    public Block findBlock(World world, int x, int y, int z) {
        int offset = world.size << 12;
        return world.chunks[((x + offset) >> 13) + ((z + offset) >> 13)*world.size]
        .blocks[(((x + offset) >> 9) - (((x + offset) >> 13) << 4)) + ((y >> 9) << 4) + (((z + offset) >> 9) - (((z + offset) >> 13) << 4) << 10)];
    }
}
