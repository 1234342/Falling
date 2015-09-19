package com.kudosku.falling;

import android.content.Context;

public class GLSurfaceView extends android.opengl.GLSurfaceView {

    private GLRenderer glrender;

    public GLSurfaceView(Context context) {
        super(context);
        glrender = new GLRenderer();
        setRenderer(glrender);
    }
}
