package com.kudosku.falling;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class GLRenderer implements GLSurfaceView.Renderer {

    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        // Clear Background Testing: Red, If Test is clear, Color is clear (Alpha is 1.0f)
        GLES20.glClearColor(0, 0, 0, 0);
    }

    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        // set View port
        GLES20.glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 unused) {
        // Clear renderer
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
    }

}
