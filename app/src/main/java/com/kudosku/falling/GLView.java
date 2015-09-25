package com.kudosku.falling;

import android.content.Context;
import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class GLView extends GLSurfaceView implements GLSurfaceView.Renderer {

    private boolean rendererSet;

    public GLView(Context context) {
        super(context);

        if (RendererInfo.isProbablyEmulator()) {
            // Avoids crashes on startup with some emulator images.
            this.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        }

        this.setEGLContextClientVersion(2);
        this.setRenderer(this);
        rendererSet = true;
    }

    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {

    }

    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {

    }

    @Override
    public void onDrawFrame(GL10 unused) {

    }

}
