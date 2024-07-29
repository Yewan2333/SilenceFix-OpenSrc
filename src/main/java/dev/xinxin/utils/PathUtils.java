package dev.xinxin.utils;

import dev.xinxin.utils.misc.MinecraftInstance;
import java.util.ArrayList;
import java.util.List;
import javax.vecmath.Vector3d;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

public final class PathUtils
implements MinecraftInstance {
    public static ArrayList<Vec3> findTeleportPath(EntityLivingBase current, EntityLivingBase target, double dashDistance) {
        double curX = current.posX;
        double curY = current.posY;
        double curZ = current.posZ;
        double tpX = target.posX;
        double tpY = target.posY;
        double tpZ = target.posZ;
        Vec3 topFrom = new Vec3(curX, curY, curZ);
        Vec3 to = new Vec3(tpX, tpY, tpZ);
        if (!PathUtils.canPassThrow(new BlockPos(topFrom))) {
            topFrom = topFrom.addVector(0.0, 1.0, 0.0);
        }
        AStarCustomPathFinder pathfinder = new AStarCustomPathFinder(topFrom, to);
        pathfinder.compute();
        int i = 0;
        Vec3 lastLoc = null;
        Vec3 lastDashLoc = null;
        ArrayList<Vec3> path = new ArrayList<Vec3>();
        ArrayList<Vec3> pathFinderPath = pathfinder.getPath();
        for (Vec3 pathElm : pathFinderPath) {
            if (i == 0 || i == pathFinderPath.size() - 1) {
                if (lastLoc != null) {
                    path.add(lastLoc.addVector(0.5, 0.0, 0.5));
                }
                path.add(pathElm.addVector(0.5, 0.0, 0.5));
                lastDashLoc = pathElm;
            } else {
                boolean canContinue = true;
                if (pathElm.squareDistanceTo(lastDashLoc) > dashDistance * dashDistance) {
                    canContinue = false;
                } else {
                    double smallX = Math.min(lastDashLoc.xCoord, pathElm.xCoord);
                    double smallY = Math.min(lastDashLoc.yCoord, pathElm.yCoord);
                    double smallZ = Math.min(lastDashLoc.zCoord, pathElm.zCoord);
                    double bigX = Math.max(lastDashLoc.xCoord, pathElm.xCoord);
                    double bigY = Math.max(lastDashLoc.yCoord, pathElm.yCoord);
                    double bigZ = Math.max(lastDashLoc.zCoord, pathElm.zCoord);
                    int x2 = (int)smallX;
                    block1: while ((double)x2 <= bigX) {
                        int y2 = (int)smallY;
                        while ((double)y2 <= bigY) {
                            int z = (int)smallZ;
                            while ((double)z <= bigZ) {
                                if (!AStarCustomPathFinder.checkPositionValidity(x2, y2, z, false)) {
                                    canContinue = false;
                                    break block1;
                                }
                                ++z;
                            }
                            ++y2;
                        }
                        ++x2;
                    }
                }
                if (!canContinue) {
                    path.add(lastLoc.addVector(0.5, 0.0, 0.5));
                    lastDashLoc = lastLoc;
                }
            }
            lastLoc = pathElm;
            ++i;
        }
        return path;
    }

    private static boolean canPassThrow(BlockPos pos) {
        Block block = Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(pos.getX(), pos.getY(), pos.getZ())).getBlock();
        return block.getMaterial() == Material.air || block.getMaterial() == Material.plants || block.getMaterial() == Material.vine || block == Blocks.ladder || block == Blocks.water || block == Blocks.flowing_water || block == Blocks.wall_sign || block == Blocks.standing_sign;
    }

    public static List<Vector3d> findBlinkPath(final double tpX, final double tpY, final double tpZ) {
        final List<Vector3d> positions = new ArrayList<Vector3d>();
        double curX = PathUtils.mc.thePlayer.posX;
        double curY = PathUtils.mc.thePlayer.posY;
        double curZ = PathUtils.mc.thePlayer.posZ;
        double distance = Math.abs(curX - tpX) + Math.abs(curY - tpY) + Math.abs(curZ - tpZ);
        int count = 0;
        while (distance > 0.0) {
            distance = Math.abs(curX - tpX) + Math.abs(curY - tpY) + Math.abs(curZ - tpZ);
            final double diffX = curX - tpX;
            final double diffY = curY - tpY;
            final double diffZ = curZ - tpZ;
            final double offset = ((count & 0x1) == 0x0) ? 0.4 : 0.1;
            final double minX = Math.min(Math.abs(diffX), offset);
            if (diffX < 0.0) {
                curX += minX;
            }
            if (diffX > 0.0) {
                curX -= minX;
            }
            final double minY = Math.min(Math.abs(diffY), 0.25);
            if (diffY < 0.0) {
                curY += minY;
            }
            if (diffY > 0.0) {
                curY -= minY;
            }
            final double minZ = Math.min(Math.abs(diffZ), offset);
            if (diffZ < 0.0) {
                curZ += minZ;
            }
            if (diffZ > 0.0) {
                curZ -= minZ;
            }
            positions.add(new Vector3d(curX, curY, curZ));
            ++count;
        }
        return positions;
    }

    public static List<Vector3d> findPath(double tpX, double tpY, double tpZ, double offset) {
        ArrayList<Vector3d> positions = new ArrayList<Vector3d>();
        double steps = Math.ceil(PathUtils.getDistance(PathUtils.mc.thePlayer.posX, PathUtils.mc.thePlayer.posY, PathUtils.mc.thePlayer.posZ, tpX, tpY, tpZ) / offset);
        double dX = tpX - PathUtils.mc.thePlayer.posX;
        double dY = tpY - PathUtils.mc.thePlayer.posY;
        double dZ = tpZ - PathUtils.mc.thePlayer.posZ;
        for (double d2 = 1.0; d2 <= steps; d2 += 1.0) {
            positions.add(new Vector3d(PathUtils.mc.thePlayer.posX + dX * d2 / steps, PathUtils.mc.thePlayer.posY + dY * d2 / steps, PathUtils.mc.thePlayer.posZ + dZ * d2 / steps));
        }
        return positions;
    }

    private static double getDistance(double x1, double y1, double z1, double x2, double y2, double z2) {
        double xDiff = x1 - x2;
        double yDiff = y1 - y2;
        double zDiff = z1 - z2;
        return MathHelper.sqrt_double(xDiff * xDiff + yDiff * yDiff + zDiff * zDiff);
    }
}

