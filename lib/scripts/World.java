package lib.scripts;

import java.awt.Graphics;

public class World {
    public Chunk[] chunks;
    public int size;

    public World(int size) {
        this.size = size;
        this.chunks = new Chunk[size * size];
        for (int z = 0; z < size; z++) {
            for (int x = 0; x < size; x++) {
                chunks[x + z * size] = new Chunk((int)(x * 512 * 16 -512 * 16 * (size/2.0)), (int)(z * 16 * 512 - 16 * 512 * (size/2.0)), x, z, x + z * size);
            }
        }
    }

    public void render(Graphics g, Camera camera) {
        for (int i = 0; i < chunks.length; i++) {
            chunks[i].render(g, camera, this);
        }
    }
}
