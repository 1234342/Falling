package com.kudosku.falling;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class GLView extends GLSurfaceView implements GLSurfaceView.Renderer {

    private boolean m_rendererSet;

    private static native void nativeOnCreated();
    private static native void nativeOnChanged(int width, int height);
    private static native void nativeOnRendererLoop();
    private static native void nativeOnTouchEvent(int x, int y, int touchFlag);

    public GLView(Context context) {
        super(context);

        if (RendererInfo.isProbablyEmulator()) {
            // Avoids crashes on startup with some emulator images.
            this.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        }

        this.setEGLContextClientVersion(2);
        this.setRenderer(this);
        this.requestFocus();
        this.setFocusableInTouchMode(true);
        m_rendererSet = true;
    }

    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        if (m_rendererSet)
            nativeOnCreated();
    }

    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        if (m_rendererSet)
            nativeOnChanged(width, height);
    }

    @Override
    public void onDrawFrame(GL10 unused) {
        if (m_rendererSet)
            nativeOnRendererLoop();
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (m_rendererSet)
            nativeOnTouchEvent((int) event.getX(), (int) event.getY(), event.getAction());
        return true;
    }
}
