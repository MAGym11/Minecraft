package lib.scripts;

public class Player {
    public int x, y, z;
    public int width, height;
    public int xA, yA, zA, xV, yV, zV;
    public int selectedBlockType;

    public Player() {
        this.x = 0;
        this.y = 17920; //1024*512;
        this.z = 0;
        this.width = 307;
        this.height = 920;
        this.xA = 0;
        this.yA = -41;
        this.zA = 0;
        this.xV = 0;
        this.yV = 0;
        this.zV = 0;
        this.selectedBlockType = 2;
    }
    
    public void update(World world) {
        this.yV += yA;
        this.y += yV;
        if (this.y > 32767 || findBlock(world, this.x, this.y, this.z).type != 0) {
            this.yV = 0;
            this.y = (((this.y >> 9) + 1) << 9);
        };

        this.xV += xA/20;
        this.x += xV;
        this.zV += zA/20;
        this.z += zV;

        if (this.x > (world.size << 12) - 1 - (this.width >> 1)) {
            this.x = (world.size << 12) - 1 - (this.width >> 1);
        } else if (this.x < -(world.size << 12) + (this.width >> 1)) {
            this.x = -(world.size << 12) + (this.width >> 1);
        }
        if (this.z >= (world.size << 12) - 1 - (this.width >> 1)) {
            this.z = (world.size << 12) - 1 - (this.width >> 1);
        } else if (this.z < -(world.size << 12) + (this.width >> 1)) {
            this.z = -(world.size << 12) + (this.width >> 1);
        }
        if ((this.y >> 9) < 64) {
            if (findBlock(world, this.x, this.y, this.z + (this.width >> 1)).type != 0 && findBlock(world, this.x, this.y, this.z + (this.width >> 1)).z > findBlock(world, this.x, this.y, this.z).z) {
                this.z = findBlock(world, this.x, this.y, this.z + (this.width >> 1)).z - (this.width >> 1);
            } else if (findBlock(world, this.x, this.y, this.z - (this.width >> 1)).type != 0 && findBlock(world, this.x, this.y, this.z - (this.width >> 1)).z < findBlock(world, this.x, this.y, this.z).z) {
                this.z = findBlock(world, this.x, this.y, this.z).z + (this.width >> 1);
            } else if (findBlock(world, this.x, this.y + this.height, this.z + (this.width >> 1)).type != 0 && findBlock(world, this.x, this.y + this.height, this.z + (this.width >> 1)).z > findBlock(world, this.x, this.y + this.height, this.z).z) {
                this.z = findBlock(world, this.x, this.y + this.height, this.z + (this.width >> 1)).z - (this.width >> 1);
            } else if (findBlock(world, this.x, this.y + this.height, this.z - (this.width >> 1)).type != 0 && findBlock(world, this.x, this.y + this.height, this.z - (this.width >> 1)).z < findBlock(world, this.x, this.y + this.height, this.z).z) {
                this.z = findBlock(world, this.x, this.y + this.height, this.z).z + (this.width >> 1);
            }
            if (findBlock(world, this.x + (this.width >> 1), this.y, this.z).type != 0 && findBlock(world, this.x + (this.width >> 1), this.y, this.z).x > findBlock(world, this.x, this.y, this.z).x) {
                this.x = findBlock(world, this.x + (this.width >> 1), this.y, this.z).x - (this.width >> 1);
            } else if (findBlock(world, this.x - (this.width >> 1), this.y, this.z).type != 0 && findBlock(world, this.x - (this.width >> 1), this.y, this.z).x < findBlock(world, this.x, this.y, this.z).x) {
                this.x = findBlock(world, this.x, this.y, this.z).x + (this.width >> 1);
            } else if (findBlock(world, this.x + (this.width >> 1), this.y + this.height, this.z).type != 0 && findBlock(world, this.x + (this.width >> 1), this.y + this.height, this.z).x > findBlock(world, this.x, this.y + this.height, this.z).x) {
                this.x = findBlock(world, this.x + (this.width >> 1), this.y + this.height, this.z).x - (this.width >> 1);
            } else if (findBlock(world, this.x - (this.width >> 1), this.y + this.height, this.z).type != 0 && findBlock(world, this.x - (this.width >> 1), this.y + this.height, this.z).x < findBlock(world, this.x, this.y + this.height, this.z).x) {
                this.x = findBlock(world, this.x, this.y + this.height, this.z).x + (this.width >> 1);
            }
        }

        if (findBlock(world, this.x, this.y - 1, this.z).type != 0) {
            xA = -xV * 15;
            zA = -zV * 15;
            if (xA == 15 || xA == -15) {xV = 0; xA = 0;}
            if (zA == 15 || zA == -15) {zV = 0; zA = 0;}
        } else {
            xA = -xV * 10;
            zA = -zV * 10;
            if (xA == 10 || xA == -10) {xV = 0; xA = 0;}
            if (zA == 10 || zA == -10) {zV = 0; zA = 0;}
        }

        yA = -41;
    }

    public Block findBlock(World world, int x, int y, int z) {
        int offset = world.size << 12;
        return world.chunks[((x + offset) >> 13) + ((z + offset) >> 13)*world.size].blocks[(((x + offset) >> 9) - (((x + offset) >> 13) << 4)) + ((y >> 9) << 4) + ((((z + offset) >> 9) - (((z + offset) >> 13) << 4)) << 10)];
    }
}
