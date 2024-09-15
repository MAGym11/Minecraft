package lib.scripts;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

public class Face {
    public int direction;
    public int x, y, z;
    public Point[] points;
    public int size;
    public Color colour;
    public boolean render;

    public Face(int x, int y, int z, int direction, int size, int type) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.direction = direction;
        this.size = size;
        this.points = new Point[4];
        if (direction == 0) {
            points[0] = new Point(x, y, z);
            points[1] = new Point(x, y - size, z);
            points[2] = new Point(x, y - size, z - size);
            points[3] = new Point(x, y, z - size);
        } else if (direction == 1) {
            points[0] = new Point(x, y, z);
            points[1] = new Point(x, y + size, z);
            points[2] = new Point(x, y + size, z + size);
            points[3] = new Point(x, y, z + size);
        } else if (direction == 2) {
            points[0] = new Point(x, y, z);
            points[1] = new Point(x - size, y, z);
            points[2] = new Point(x - size, y, z - size);
            points[3] = new Point(x, y, z - size);
        } else if (direction == 3) {
            points[0] = new Point(x, y, z);
            points[1] = new Point(x + size, y, z);
            points[2] = new Point(x + size, y, z + size);
            points[3] = new Point(x, y, z + size);
        } else if (direction == 4) {
            points[0] = new Point(x, y, z);
            points[1] = new Point(x - size, y, z);
            points[2] = new Point(x - size, y - size, z);
            points[3] = new Point(x, y - size, z);
        } else if (direction == 5) {
            points[0] = new Point(x, y, z);
            points[1] = new Point(x + size, y, z);
            points[2] = new Point(x + size, y + size, z);
            points[3] = new Point(x, y + size, z);
        }

        update(type);
    }

    public void render(Graphics g, Camera camera) {
        g.setColor(this.colour);

        for (int i = 0; i < points.length; i++) {
            points[i].project(camera);
        }

        if (!(  (points[0].transZ < camera.focalDistance && points[1].transZ < camera.focalDistance && points[2].transZ < camera.focalDistance && points[3].transZ < camera.focalDistance) ||
                (points[0].projX < 0 && points[1].projX < 0 && points[2].projX < 0 && points[3].projX < 0))) {
            camera.nRenderedFaces++;    
            this.render = true;
        }
    }

    public void update(int type) {
        Random random = new Random();
        int randValue;
        switch (type) {
            case 1:
                randValue = random.nextInt(0x3f);
                this.colour = new Color(randValue + (randValue << 8) + (randValue << 16));
                break;
            case 2:
                this.colour = new Color((random.nextInt(0x7f) << 8) + 0x7f00);
                break;
            case 3:
                randValue = random.nextInt(0x3f);
                this.colour = new Color(randValue + 0x7f + (randValue << 8) + 0x7f00 + (randValue << 16) + 0x7f0000);
                break;
            case 4:
                randValue = random.nextInt(0x3f);
                this.colour = new Color(randValue + (randValue << 8) + 0x3f00 + (randValue << 16) + 0x3f0000);

        }
    }
}
