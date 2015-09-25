package com.kudosku.falling;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;

public class RendererInfo {

    private int m_rendererType;

    public static class RendererType {
        static int ANDROIDSURFACE = 0;
        static int OPENGLES_10 = 1;
        static int OPENGLES_20 = 2;
    }

    public void setType(int rendererType) {
        m_rendererType = rendererType;
    }

    public int getType() {
       return m_rendererType;
    }
    public String getTypeForStr() {
        switch (m_rendererType) {
            case 0 : // RendererType.ANDROIDSURFACE
                return "Andriod Surface View";
            case 1 : // RendererType.OPENGLES_10
                return "OpenGL ES 1.0";
            case 2 : // RendererType.OPENGLES_20
                return "OpenGL ES 2.0";
            default :
                return "Not Definded Type";
        }
    }

    public static boolean isProbablyEmulator() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1
                && (Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86"));
    }

    public boolean supportsEs2(Context context) {
        return ((ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE)).
                getDeviceConfigurationInfo().
                reqGlEsVersion >= 0x20000 || isProbablyEmulator();
    }

}
