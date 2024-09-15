import lib.scripts.*;

import java.awt.Canvas;
import javax.swing.JFrame;
import java.awt.Dimension;
import javax.swing.ImageIcon;
import java.awt.image.BufferStrategy;
import java.awt.Graphics;
import java.lang.Math;
import java.awt.Color;
import java.awt.Robot;
import java.awt.AWTException;

public class Game extends Canvas implements Runnable {

    private static final long serialVersionUID = 1L;

    public static int width = 570;
    public static int height = (int)((float)width / 16 * 9);
    public static int scale = 1;

    private static String title = "Game";

    private Thread thread;
    private JFrame frame;
    private Keyboard key;
    private Mouse mouse;
    private boolean running = false;

    private Camera camera;
    private World world;

    private Player player;

    private boolean paused, allowPause, allowUnpause, allowRightClick;

    private Robot r;

    private Face[] renderedFaces;

    public Game() throws AWTException {
        setPreferredSize(new Dimension(width*scale, height*scale));

        frame = new JFrame();

        camera = new Camera(width, height, 2.0944);

        world = new World(2);

        player = new Player();

        key = new Keyboard();
        addKeyListener(key);
        mouse = new Mouse();
        addMouseListener(mouse);
        addMouseMotionListener(mouse);
        r = new Robot();
    }

    public synchronized void start() {
        running = true;
        paused = false;
        thread = new Thread(this, "Display");
        thread.start();
    }

    public synchronized void stop() {
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        long lastTime = System.nanoTime();
        long timer = System.currentTimeMillis();
        final double ns = 1_000_000_000.0 / 20;
        double fractionOfFrame = 0;
        int frames = 0;
        int updates = 0;
        allowRightClick = true;
        requestFocus();
        while (running) {
            if (!paused) {
                long now = System.nanoTime();
                fractionOfFrame += (now - lastTime) / ns;
                lastTime = now;
                while (fractionOfFrame >= 1) {
                    update();
                    updates++;
                    fractionOfFrame--;
                }
                render();
                frames++;

                if (System.currentTimeMillis() - timer > 1000) {
                    timer += 1000;
                    frame.setTitle(title + " | " + frames + "fps | " + updates + "ups");
                    updates = 0;
                    frames = 0;
                }
            } else {
                boolean escape = key.keys[27];
                if (!escape) {
                    allowUnpause = true;
                } else if (escape && allowUnpause) {
                    paused = false;
                    allowPause = false;
                    allowUnpause = false;
                }
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    System.out.println(e);
                }
            }
        }
        stop();
    }

    public void render() {
        try {
            BufferStrategy bs = getBufferStrategy();
            if (bs == null) {
                createBufferStrategy(3);
                return;
            }

            Graphics g = bs.getDrawGraphics();
            camera.updateDimensions(this);
            g.setColor(new Color(0x36c7f2));
            g.fillRect(0, 0, this.getWidth(), this.getHeight());
            camera.nRenderedFaces = 0;
            world.render(g, camera);
            renderedFaces = new Face[camera.nRenderedFaces];
            camera.nRenderedFaces = 0;
            for (int i = 0; i < world.chunks.length; i++) {
                for (int j = 0; j < 16384; j++) {
                    for (int k = 0; k < 6; k++) {
                        if (world.chunks[i].blocks[j].faces[k].render) {
                            renderedFaces[camera.nRenderedFaces] = world.chunks[i].blocks[j].faces[k];
                            camera.nRenderedFaces++;
                            world.chunks[i].blocks[j].faces[k].render = false;
                        }
                    }
                }
            }
            quicksort(0, camera.nRenderedFaces - 1);
            for (int i = 0; i < camera.nRenderedFaces; i++) {
                g.setColor(renderedFaces[i].colour);
                g.fillPolygon(new int[]{renderedFaces[i].points[0].projX, renderedFaces[i].points[1].projX, renderedFaces[i].points[2].projX, renderedFaces[i].points[3].projX}, new int[]{renderedFaces[i].points[0].projY, renderedFaces[i].points[1].projY, renderedFaces[i].points[2].projY, renderedFaces[i].points[3].projY}, 4);
            }
            g.setColor(Color.white);
            g.drawLine((this.getWidth() >> 1) - 16, this.getHeight() >> 1, (this.getWidth() >> 1) + 16, this.getHeight() >> 1);
            g.drawLine(this.getWidth() >> 1, (this.getHeight() >> 1) - 16, this.getWidth() >> 1, (this.getHeight() >> 1) + 16);

            g.dispose();
            bs.show();
        } catch (OutOfMemoryError outofMemory) {
            throw outofMemory;
        }
    }

    public void update() {
        if (!key.keys[27]) {
            allowPause = true;
        }
        if (key.keys[27] && allowPause) {
            paused = true;
            allowUnpause = false;
            allowPause = false;
        }

        r.mouseMove(frame.getX() + (this.getWidth()>>1), frame.getY() + (this.getHeight()>>1));

        camera.yRot += (Mouse.x + 7 - (this.getWidth()>>1)) / 512.0;
        if (camera.yRot >= Math.PI*2) {
            camera.yRot -= Math.PI*2;
        } else if (camera.yRot < 0) {
            camera.yRot += Math.PI*2;
        }
        camera.sy = Math.sin(camera.yRot);
        camera.cy = Math.cos(camera.yRot);

        camera.xRot -= (Mouse.y + 30 - (this.getHeight()>>1)) / 512.0;
        if (camera.xRot < -Math.PI/2) {
            camera.xRot = -Math.PI/2;
        } else if (camera.xRot > Math.PI/2) {
            camera.xRot = Math.PI/2;
        }
        camera.sx = Math.sin(camera.xRot);
        camera.cx = Math.cos(camera.xRot);
        
        player.update(world);
        
        if (key.keys[68]) {
            if (key.keys[16] || player.findBlock(world, player.x, player.y - 1, player.z).type == 0) {
                player.xA += (int)(1024 * Math.cos(camera.yRot));
                player.zA += (int)(1024 * -Math.sin(camera.yRot));
            } else {
                player.xA += (int)(2048 * Math.cos(camera.yRot));
                player.zA += (int)(2048 * -Math.sin(camera.yRot));
            }
        }
        if (key.keys[65]) {
            if (key.keys[16] || player.findBlock(world, player.x, player.y - 1, player.z).type == 0) {
                player.xA -= (int)(1024 * Math.cos(camera.yRot));
                player.zA -= (int)(1024 * -Math.sin(camera.yRot));
            } else {
                player.xA -= (int)(2048 * Math.cos(camera.yRot));
                player.zA -= (int)(2048 * -Math.sin(camera.yRot));
            }
        }
        if (key.keys[32] && player.findBlock(world, player.x, player.y - 1, player.z).type != 0) {
            player.yA = 205;
        }
        if (key.keys[87]) {
            if (key.keys[16] || player.findBlock(world, player.x, player.y - 1, player.z).type == 0) {
                player.zA += (int)(1024 * Math.cos(camera.yRot));
                player.xA += (int)(1024 * Math.sin(camera.yRot));
            } else {
                player.zA += (int)(2048 * Math.cos(camera.yRot));
                player.xA += (int)(2048 * Math.sin(camera.yRot));
            }
        }
        if (key.keys[83]) {
            if (key.keys[16] || player.findBlock(world, player.x, player.y - 1, player.z).type == 0) {
                player.zA -= (int)(1024 * Math.cos(camera.yRot));
                player.xA -= (int)(1024 * Math.sin(camera.yRot));
            } else {
                player.zA -= (int)(2048 * Math.cos(camera.yRot));
                player.xA -= (int)(2048 * Math.sin(camera.yRot));
            }
        }
        if (key.keys[49]) {
            player.selectedBlockType = 1;
        }
        if (key.keys[50]) {
            player.selectedBlockType = 2;
        }
        if (key.keys[51]) {
            player.selectedBlockType = 3;
        }
        if (key.keys[52]) {
            player.selectedBlockType = 4;
        }

        if (!Mouse.rightClick) {
            allowRightClick = true;
        }
        
        if (Mouse.rightClick && allowRightClick) {
            camera.castRay(world, player, true);
            allowRightClick = false;
        }
        if (Mouse.leftClick) {
            camera.castRay(world, player, false);
        }

        camera.x = player.x;
        camera.y = -(player.y + 512 + 256);
        camera.z = player.z;
    }

    private void quicksort(int lo, int hi) {
        if (lo >= 0 && hi >= 0 && lo < hi) {
            int p = partition(lo, hi);
            quicksort(lo, p);
            quicksort(p + 1, hi);
        }
    }

    private int partition(int lo, int hi) {
        double pivot = (this.renderedFaces[((hi + lo) / 2)].points[0].transZ + this.renderedFaces[((hi + lo) / 2)].points[1].transZ + this.renderedFaces[((hi + lo) / 2)].points[2].transZ + this.renderedFaces[((hi + lo) / 2)].points[3].transZ) / 4.0;

        int i = lo - 1;

        int j = hi + 1;

        while (true) {
            do {
                i++;
            } while ((this.renderedFaces[i].points[0].transZ + this.renderedFaces[i].points[1].transZ + this.renderedFaces[i].points[2].transZ + this.renderedFaces[i].points[3].transZ) / 4.0 > pivot);

            do {
                j--;
            } while ((this.renderedFaces[j].points[0].transZ + this.renderedFaces[j].points[1].transZ + this.renderedFaces[j].points[2].transZ + this.renderedFaces[j].points[3].transZ) / 4.0 < pivot);

            if (i >= j) {
                return j;
            }
            
            Face temp = this.renderedFaces[i];
            this.renderedFaces[i] = this.renderedFaces[j];
            this.renderedFaces[j] = temp;
        }
    }
    public static void main(String[] args) throws AWTException{
        Game game = new Game();
        game.frame.setTitle(title);
        game.frame.add(game);
        game.frame.pack();
        game.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        game.frame.setLocationRelativeTo(null);
        game.frame.setVisible(true);
        ImageIcon icon = new ImageIcon("././assets/icon.png");
        game.frame.setIconImage(icon.getImage());

        game.start();
    }
}
