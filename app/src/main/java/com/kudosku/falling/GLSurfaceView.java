package com.kudosku.falling;

import android.content.Context;
import android.graphics.PixelFormat;

public class GLSurfaceView extends android.opengl.GLSurfaceView {

    private GLRenderer glrender;

    public GLSurfaceView(Context context) {
        super(context);
        glrender = new GLRenderer();
        setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        setRenderer(glrender);
        getHolder().setFormat(PixelFormat.TRANSLUCENT);
    }
}
