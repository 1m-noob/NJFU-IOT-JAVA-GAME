package fisher_king.src.main;

/**
分离轴定理是一种用来判断两个凸多边形是否相交的方法。它的基本思想是，如果两个凸多边形不相交，
那么一定存在一条分离轴，使得两个凸多边形在这条分离轴上的投影不重叠。
在下面的代码中，isSeparated 方法就是用来检查两个凸多边形在某条分离轴上的投影是否重叠。
如果存在一条分离轴使得两个凸多边形在这条分离轴上的投影不重叠，那么这两个凸多边形就不相交。

 fish类，Net类以及Bullet类都是继承这个旋转矩形类，为检测碰撞提供了便利。（实际上，Net类只有动画，没有实际作用）
*/

public class RotatedRectangle {//旋转矩形类
    public int centerX, centerY, width, height, angle;
    public RotatedRectangle(int centerX, int centerY, int width, int height, int angle) {//旋转矩形的构造方法
        this.centerX = centerX;
        this.centerY = centerY;
        this.width = width;
        this.height = height;
        this.angle = angle;
    }
    public RotatedRectangle(){}

    public double[][] getVertices() {//根据旋转矩形的中心点和旋转角度计算顶点坐标
        double[][] vertices = new double[4][2];
        double angleRad = Math.toRadians(angle%180);
        double cosAngle = Math.cos(angleRad);
        double sinAngle = Math.sin(angleRad);
        vertices[0][0] = centerX + (-width / 2 * cosAngle) - (-height / 2 * sinAngle);
        vertices[0][1] = centerY + (-width / 2 * sinAngle) + (-height / 2 * cosAngle);
        vertices[1][0] = centerX + (width / 2 * cosAngle) - (-height / 2 * sinAngle);
        vertices[1][1] = centerY + (width / 2 * sinAngle) + (-height / 2 * cosAngle);
        vertices[2][0] = centerX + (width / 2 * cosAngle) - (height / 2 * sinAngle);
        vertices[2][1] = centerY + (width / 2 * sinAngle) + (height / 2 * cosAngle);
        vertices[3][0] = centerX + (-width / 2 * cosAngle) - (height / 2 * sinAngle);
        vertices[3][1] = centerY + (-width / 2 * sinAngle) + (height / 2 * cosAngle);
        return vertices;
    }

    public boolean isIntersecting(RotatedRectangle other) {//判断两个旋转矩形是否相交
        double[][] rect1Vertices = this.getVertices();
        double[][] rect2Vertices = other.getVertices();
        for (int i = 0; i < rect1Vertices.length; i++) {
            if (isSeparated(rect1Vertices[i], rect1Vertices[(i + 1) % rect1Vertices.length], rect1Vertices, rect2Vertices)) {
                return false;
            }
        }
        for (int i = 0; i < rect2Vertices.length; i++) {
            if (isSeparated(rect2Vertices[i], rect2Vertices[(i + 1) % rect2Vertices.length], rect1Vertices, rect2Vertices)) {
                return false;
            }
        }
        return true;
    }

    private boolean isSeparated(double[] pointA, double[] pointB, double[][] shapeA, double[][] shapeB) {// 判断两个旋转矩形是否分离
        double[] normal = new double[]{pointB[1] - pointA[1], pointA[0] - pointB[0]};
        double minA = Double.MAX_VALUE;
        double maxA = Double.MIN_VALUE;
        for (double[] vertex : shapeA) {
            double projection = normal[0] * vertex[0] + normal[1] * vertex[1];
            minA = Math.min(minA, projection);
            maxA = Math.max(maxA, projection);
        }
        double minB = Double.MAX_VALUE;
        double maxB = Double.MIN_VALUE;
        for (double[] vertex : shapeB) {
            double projection = normal[0] * vertex[0] + normal[1] * vertex[1];
            minB = Math.min(minB, projection);
            maxB = Math.max(maxB, projection);
        }
        return maxA < minB || maxB < minA;
    }
}
