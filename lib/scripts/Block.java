package lib.scripts;

import java.awt.Graphics;

public class Block {
    public int x, y, z, size;
    public int xPos, yPos, zPos, pos;
    public Face[] faces;
    public int type;

    public Block(int x, int y, int z, int size, int type, int xPos, int yPos, int zPos, int pos) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.type = type;
        this.size = size;
        this.xPos = xPos;
        this.yPos = yPos;
        this.zPos = zPos;
        this.pos = pos;
        this.faces = new Face[6];
        this.faces[0] = new Face(x + size, y + size, z + size, 0, size, type);
        this.faces[1] = new Face(x, y, z, 1, size, type);
        this.faces[2] = new Face(x + size, y + size, z + size, 2, size, type);
        this.faces[3] = new Face(x, y, z, 3, size, type);
        this.faces[4] = new Face(x + size, y + size, z + size, 4, size, type);
        this.faces[5] = new Face(x, y, z, 5, size, type);
    }

    public void render(Graphics g, Camera camera, Chunk chunk, World world) {
        if (type != 0) {
            if (!(camera.x < faces[0].points[0].x)  && checkAdjacentBlock(0, chunk, world)) faces[0].render(g, camera);
            if (!(camera.x > faces[1].points[0].x) && checkAdjacentBlock(1, chunk, world)) faces[1].render(g, camera);
            if (!(camera.y > -faces[2].points[0].y) && checkAdjacentBlock(2, chunk, world)) faces[2].render(g, camera);
            if (!(camera.y < -faces[3].points[0].y) && checkAdjacentBlock(3, chunk, world)) faces[3].render(g, camera);
            if (!(camera.z < faces[4].points[0].z) && checkAdjacentBlock(4, chunk, world)) faces[4].render(g, camera);
            if (!(camera.z > faces[5].points[0].z) && checkAdjacentBlock(5, chunk, world)) faces[5].render(g, camera);
        }
    }

    private boolean checkAdjacentBlock(int direction, Chunk chunk, World world) {
        switch (direction) {
            case 0:
                if (this.xPos == 15) {
                    if (chunk.xPos == world.size - 1) {
                        return true;
                    } else {
                        return world.chunks[chunk.pos + 1].blocks[pos - xPos].type == 0;
                    }
                } else {
                    return chunk.blocks[pos + 1].type == 0;
                }
            case 1:
                if (this.xPos == 0) {
                    if (chunk.xPos == 0) {
                        return true;
                    } else {
                        return world.chunks[chunk.pos - 1].blocks[pos + 15 - xPos].type == 0;
                    }
                } else {
                    return chunk.blocks[pos - 1].type == 0;
                }
            case 2:
                if (this.yPos == 63) {
                    return true;
                } else {
                    return chunk.blocks[pos + 16].type == 0;
                }
            case 3:
                if (this.yPos == 0) {
                    return true;
                } else {
                    return chunk.blocks[pos - 16].type == 0;
                }
            case 4:
                if (this.zPos == 15) {
                    if (chunk.zPos == world.size - 1) {
                        return true;
                    } else {
                        return world.chunks[chunk.pos + world.size].blocks[pos - zPos*1024].type == 0;
                    }
                } else {
                    return chunk.blocks[pos + 1024].type == 0;
                }
            case 5:
                if (this.zPos == 0) {
                    if (chunk.zPos == 0) {
                        return true;
                    } else {
                        return world.chunks[chunk.pos - world.size].blocks[pos + 15360 - zPos*1024].type == 0;
                    }
                } else {
                    return chunk.blocks[pos - 1024].type == 0;
                }
        }
        return false;
    }

    public void update() {
        for (int i = 0; i < 6; i++) {
            faces[i].update(this.type);
        }
    }
}
