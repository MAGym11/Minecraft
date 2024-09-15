package lib.scripts;

import java.awt.Graphics;

public class Point {
    public int x, y, z;
    public int projX, projY;
    public float transZ;
    public int xSection, ySection;  

    public Point(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void render(Graphics g, Camera camera) {
        project(camera);
        if (this.projX >=0 && this.projX < camera.width && this.projY >= 0 && this.projY < camera.height) camera.pixels[projX + projY * camera.width] = 0xffffff;
    }

    public void update(int x, int y, int z) {
        this.x += x;
        this.y += y;
        this.z += z;
    }

    public void project(Camera camera) {
        this.transZ = (float)(((this.x - camera.x) * camera.sy + (this.z - camera.z) * camera.cy) * camera.cx + (-this.y - camera.y) * -camera.sx);
        if (this.transZ > 0) {
            this.projX = (int)((camera.focalDistance * ((this.x - camera.x) * camera.cy + (this.z - camera.z) * -camera.sy) / (this.transZ)) * (camera.width/570.0) + (camera.width >> 1));
            this.projY = (int)((camera.focalDistance * (((this.x - camera.x) * camera.sy + (this.z - camera.z) * camera.cy) * camera.sx + (-this.y - camera.y) * camera.cx) / (this.transZ)) * (camera.height/320.0) + (camera.height >> 1));
        } else {
            this.projX = (int)((camera.focalDistance * ((this.x - camera.x) * camera.cy + (this.z - camera.z) * -camera.sy)) * (camera.width/570.0) + (camera.width >> 1));
            this.projY = (int)((camera.focalDistance * (((this.x - camera.x) * camera.sy + (this.z - camera.z) * camera.cy) * camera.sx + (-this.y - camera.y) * camera.cx)) * (camera.height/320.0) + (camera.height >> 1));
        }
    }
}