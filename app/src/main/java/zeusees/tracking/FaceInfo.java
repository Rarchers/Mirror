package zeusees.tracking;

import android.graphics.Path;
import android.graphics.PointF;

public class FaceInfo {
    Path clip_up;
    Path clip_down;

    public FaceInfo(PointF[] pointFS,int width){

        clip_up = getClip_up(pointFS,width);
        clip_down = getClip_down(pointFS,width);
    }

    public Path getClip_up() {
        return clip_up;
    }

    public Path getClip_down() {
        return clip_down;
    }

    private Path getClip_down(PointF[] pointFS, int width){
        Path path = new Path();
        path.moveTo(width-pointFS[61].x,pointFS[61].y);
        path.quadTo((width-pointFS[61].x+width-pointFS[63].x)/2,(pointFS[61].y+pointFS[63].y)/2,width-pointFS[63].x,pointFS[63].y);
        path.quadTo((width-pointFS[63].x+width-pointFS[103].x)/2,(pointFS[63].y+pointFS[103].y)/2,width-pointFS[103].x,pointFS[103].y);
        path.quadTo((width-pointFS[103].x+width-pointFS[2].x)/2,(pointFS[103].y+pointFS[2].y)/2,width-pointFS[2].x,pointFS[2].y);
        path.quadTo((width-pointFS[2].x+width-pointFS[42].x)/2,(pointFS[2].y+pointFS[42].y)/2,width-pointFS[42].x,pointFS[42].y);
        path.quadTo((width-pointFS[42].x+width-pointFS[4].x)/2,(pointFS[42].y+pointFS[4].y)/2,width-pointFS[4].x,pointFS[4].y);
        path.quadTo((width-pointFS[4].x+width-pointFS[30].x)/2,(pointFS[4].y+pointFS[30].y)/2,width-pointFS[30].x,pointFS[30].y);
        path.quadTo((width-pointFS[30].x+width-pointFS[32].x)/2,(pointFS[30].y+pointFS[32].y)/2,width-pointFS[32].x,pointFS[32].y);
        path.quadTo((width-pointFS[32].x+width-pointFS[64].x)/2,(pointFS[32].y+pointFS[64].y)/2,width-pointFS[64].x,pointFS[64].y);
        path.quadTo((width-pointFS[64].x+width-pointFS[65].x)/2,(pointFS[64].y+pointFS[65].y)/2,width-pointFS[65].x,pointFS[65].y);
        path.quadTo((width-pointFS[65].x+width-pointFS[61].x)/2,(pointFS[65].y+pointFS[61].y)/2,width-pointFS[61].x,pointFS[61].y);

        return path;
    }
    private Path getClip_down1(PointF[] pointFS, int width){
        Path path = new Path();
        path.moveTo(width-pointFS[61].x,pointFS[61].y);
        path.lineTo(width-pointFS[63].x,pointFS[63].y);
        path.lineTo(width-pointFS[103].x,pointFS[103].y);
        path.lineTo(width-pointFS[2].x,pointFS[2].y);
        path.lineTo(width-pointFS[42].x,pointFS[42].y);
        path.lineTo(width-pointFS[4].x,pointFS[4].y);
        path.lineTo(width-pointFS[30].x,pointFS[30].y);
        path.lineTo(width-pointFS[32].x,pointFS[32].y);
        path.lineTo(width-pointFS[64].x,pointFS[64].y);
        path.lineTo(width-pointFS[65].x,pointFS[65].y);
        path.lineTo(width-pointFS[61].x,pointFS[61].y);
        return path;
    }
//  path.quadTo((width-pointFS[61].x+width-pointFS[63].x)/2,(pointFS[61].y+pointFS[63].y)/2,width-pointFS[63].x,pointFS[63].y);
    private Path getClip_up(PointF[] pointFS,int width){
        Path path = new Path();
        path.moveTo(width-pointFS[45].x,pointFS[45].y);
        path.lineTo(width-pointFS[37].x,pointFS[37].y);
        path.lineTo(width-pointFS[39].x,pointFS[39].y);
        path.lineTo(width-pointFS[38].x,pointFS[38].y);
        path.lineTo(width-pointFS[26].x,pointFS[26].y);
        path.lineTo(width-pointFS[33].x,pointFS[33].y);
        path.lineTo(width-pointFS[50].x,pointFS[50].y);
        path.lineTo(width-pointFS[25].x,pointFS[25].y);
        path.lineTo(width-pointFS[36].x,pointFS[36].y);
        path.lineTo(width-pointFS[40].x,pointFS[40].y);

        return path;
    }



}
