package lib.scripts;

import java.awt.Graphics;

public class Chunk {
    public int x, z, size;
    public Block[] blocks;
    public int pos;
    public int xPos, zPos;

    public Chunk(int x, int z, int xPos, int zPos, int pos) {
        this.x = x;
        this.z = z;
        this.pos = pos;
        this.xPos = xPos;
        this.zPos = zPos;
        this.blocks = new Block[16 * 64 * 16];
        for (int zi = 0; zi < 16; zi++) {
            for (int yi = 0; yi < 64; yi++) {
                for (int xi = 0; xi < 16; xi++) {
                    if (yi > 31) {
                        blocks[xi + yi*16 + zi*64*16] = new Block(xi * 512 + this.x, yi * 512, zi * 512 + this.z, 512, 0, xi, yi, zi, xi + yi*16 + zi*64*16);
                    } else if (yi == 0) {
                        blocks[xi + yi*16 + zi*64*16] = new Block(xi * 512 + this.x, yi * 512, zi * 512 + this.z, 512, 1, xi, yi, zi, xi + yi*16 + zi*64*16);
                    } else if (yi == 31) {
                        blocks[xi + yi*16 + zi*64*16] = new Block(xi * 512 + this.x, yi * 512, zi * 512 + this.z, 512, 2, xi, yi, zi, xi + yi*16 + zi*64*16);
                    } else if (yi < 29) {
                        blocks[xi + yi*16 + zi*64*16] = new Block(xi * 512 + this.x, yi * 512, zi * 512 + this.z, 512, 3, xi, yi, zi, xi + yi*16 + zi*64*16);
                    } else {
                        blocks[xi + yi*16 + zi*64*16] = new Block(xi * 512 + this.x, yi * 512, zi * 512 + this.z, 512, 4, xi, yi, zi, xi + yi*16 + zi*64*16);
                    }
                }
            }
        }
    }

    public void render(Graphics g, Camera camera, World world) {
        for (int zi = 0; zi < 16; zi++) {
            for (int yi = 0; yi < 64; yi++) {
                for (int xi = 0; xi < 16; xi++) {
                    if (!(xi == 0 || xi == 15 || yi == 0 || yi == 63 || zi == 0 || zi == 15)) {
                        if (!(blocks[(xi+1) + (yi)*16 + (zi)*64*16].type != 0 && blocks[(xi-1) + (yi)*16 + (zi)*64*16].type != 0 && blocks[(xi) + (yi+1)*16 + (zi)*64*16].type != 0 && blocks[(xi) + (yi-1)*16 + (zi)*64*16].type != 0 && blocks[(xi) + (yi)*16 + (zi+1)*64*16].type != 0 && blocks[(xi) + (yi)*16 + (zi-1)*64*16].type != 0)) {
                            blocks[(xi) + (yi)*16 + (zi)*64*16].render(g, camera, this, world);
                        }
                    } else {
                        blocks[(xi) + (yi)*16 + (zi)*64*16].render(g, camera, this, world);
                    }
                }
            }
        }
    }
}
